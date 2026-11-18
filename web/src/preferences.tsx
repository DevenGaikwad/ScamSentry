import { createContext, useContext, useEffect, useState } from 'react'
import type { ReactNode } from 'react'

export type Theme = 'system' | 'light' | 'dark'
export type Motion = 'auto' | 'paused'
export const preferenceKeys = { theme: 'scamsentry.theme', motion: 'scamsentry.motion' } as const
export function readPreference<T extends string>(key: string, allowed: readonly T[], fallback: T): T {
  try { const value = localStorage.getItem(key); return allowed.includes(value as T) ? value as T : fallback } catch { return fallback }
}
function persist(key: string, value: string) { try { localStorage.setItem(key, value) } catch { /* Preferences are optional. No message fallback storage. */ } }
const Preferences = createContext({ theme: 'system' as Theme, dark: false, motion: 'auto' as Motion, reduced: false, running: true, setTheme: (_: Theme) => { void _ }, toggleMotion: () => {}, setInputActive: (_: boolean) => { void _ } })
export function PreferencesProvider({ busy, children }: { busy: boolean; children: ReactNode }) {
  const [theme, updateTheme] = useState<Theme>(() => readPreference(preferenceKeys.theme, ['system', 'light', 'dark'], 'system'))
  const [motion, updateMotion] = useState<Motion>(() => readPreference(preferenceKeys.motion, ['auto', 'paused'], 'auto'))
  const [systemDark, setSystemDark] = useState(() => matchMedia('(prefers-color-scheme: dark)').matches)
  const [reduced, setReduced] = useState(() => matchMedia('(prefers-reduced-motion: reduce)').matches)
  const [hidden, setHidden] = useState(() => document.hidden)
  const [forced, setForced] = useState(() => matchMedia('(forced-colors: active)').matches)
  const [inputActive, setInputActive] = useState(false)
  const dark = theme === 'dark' || (theme === 'system' && systemDark)
  const running = !reduced && !forced && motion !== 'paused' && !hidden && !busy && !inputActive
  useEffect(() => {
    const d = matchMedia('(prefers-color-scheme: dark)'), r = matchMedia('(prefers-reduced-motion: reduce)'), f = matchMedia('(forced-colors: active)')
    const syncDark = () => setSystemDark(d.matches), syncReduced = () => setReduced(r.matches), syncHidden = () => setHidden(document.hidden)
    const syncForced = () => setForced(f.matches)
    d.addEventListener('change', syncDark); r.addEventListener('change', syncReduced); f.addEventListener('change', syncForced); document.addEventListener('visibilitychange', syncHidden)
    return () => { d.removeEventListener('change', syncDark); r.removeEventListener('change', syncReduced); f.removeEventListener('change', syncForced); document.removeEventListener('visibilitychange', syncHidden) }
  }, [])
  useEffect(() => {
    document.documentElement.dataset.theme = dark ? 'dark' : 'light'
    document.querySelector('meta[name="theme-color"]')?.setAttribute('content', dark ? '#101010' : '#f5f5f5')
  }, [dark])
  useEffect(() => { document.documentElement.dataset.motion = running ? 'running' : 'paused' }, [running])
  const setTheme = (value: Theme) => { updateTheme(value); persist(preferenceKeys.theme, value) }
  const toggleMotion = () => { const value = motion === 'paused' ? 'auto' : 'paused'; updateMotion(value); persist(preferenceKeys.motion, value) }
  return <Preferences.Provider value={{ theme, dark, motion, reduced, running, setTheme, toggleMotion, setInputActive }}>{children}<div key={String(dark)} className="theme-wipe" aria-hidden="true" /></Preferences.Provider>
}
export const usePreferences = () => useContext(Preferences)
