import { useEffect, useLayoutEffect, useRef, useState } from 'react'
import { Analyze } from './Analyze'
import { Android, Article, Evidence, Home, Learn, NotFound, Privacy } from './Pages'
import { PreferencesProvider, usePreferences } from './preferences'
import { Link, Router, usePageMetadata, useTransition } from './router'
import { creatorCredit, navigation, site } from './site'
import { Arrow, containDialogFocus, External, Mark, PointerBrackets, ReadingRail, useEditorialEffects } from './ui'
import { useAnalysis } from './useAnalysis'
import type { AnalysisSession } from './useAnalysis'
import { RouteScene, useRoutePlane } from './motion/RouteScene'
import './style.css'
import './motion/motion.css'

function PreferenceControls() {
  const { theme, motion, reduced, setTheme, toggleMotion } = usePreferences()
  return <div className="preference-controls"><label className="theme-control"><span aria-hidden="true">◐</span><span className="sr-only">Theme</span><select aria-label="Theme" value={theme} onChange={event => setTheme(event.target.value as 'system' | 'light' | 'dark')}><option value="system">System</option><option value="light">Light</option><option value="dark">Dark</option></select></label><button className="motion-control" aria-pressed={motion === 'paused'} onClick={toggleMotion}><span className="status-square" aria-hidden="true" />{motion === 'paused' ? 'Resume motion' : reduced ? 'Motion reduced' : 'Pause motion'}</button></div>
}
function Header({ path }: { path: string }) {
  const [open, setOpen] = useState(false)
  const dialog = useRef<HTMLDialogElement>(null)
  const trigger = useRef<HTMLButtonElement>(null)
  useEffect(() => {
    if (!open) return
    const node = dialog.current
    const overflow = document.body.style.overflow
    document.body.style.overflow = 'hidden'; node?.showModal()
    const desktop = matchMedia('(min-width: 1100px)')
    const resize = () => { if (desktop.matches) setOpen(false) }
    desktop.addEventListener('change', resize)
    return () => { desktop.removeEventListener('change', resize); node?.close(); document.body.style.overflow = overflow; trigger.current?.focus() }
  }, [open])
  useEffect(() => setOpen(false), [path])
  const active = (target: string) => path === target || target === '/learn' && path.startsWith('/learn/')
  return <header className="site-header"><Link className="brand" href="/" aria-label="ScamSentry home"><Mark /><span>ScamSentry<span className="brand-sub">A SECOND LOOK</span></span></Link><nav className="desktop-navigation" aria-label="Main navigation">{navigation.map(item => <Link href={item.path} key={item.path} aria-current={active(item.path) ? 'page' : undefined}>{item.name}</Link>)}</nav><div className="desktop-preferences"><PreferenceControls /></div><div className="mobile-header-actions"><Link href="/analyze" aria-current={path === '/analyze' ? 'page' : undefined}>Analyze <Arrow /></Link><button ref={trigger} className="menu-trigger" aria-expanded={open} aria-controls="mobile-menu" aria-haspopup="dialog" onClick={() => setOpen(true)}>Menu <span aria-hidden="true">+</span></button></div>
    {open && <dialog ref={dialog} className="mobile-menu" id="mobile-menu" aria-labelledby="menu-title" onKeyDown={containDialogFocus} onCancel={event => { event.preventDefault(); setOpen(false) }}><div className="dialog-header"><h2 className="eyebrow" id="menu-title">SCAMSENTRY / CHAPTERS</h2><button className="icon-button" aria-label="Close menu" onClick={() => setOpen(false)} autoFocus>×</button></div><nav aria-label="Mobile navigation"><Link href="/" aria-current={path === '/' ? 'page' : undefined} onClick={() => setOpen(false)}><span className="menu-number" aria-hidden="true">00</span><span>Home</span><Arrow /></Link>{navigation.map(item => <Link key={item.path} href={item.path} aria-current={active(item.path) ? 'page' : undefined} onClick={() => setOpen(false)}><span className="menu-number" aria-hidden="true">{item.chapter}</span><span>{item.name}</span><Arrow /></Link>)}</nav><PreferenceControls /><p className="muted">Local inspection. Independent judgment.</p></dialog>}
  </header>
}
function Shell({ path, session }: { path: string; session: AnalysisSession }) {
  const renderedPath = useRef(path)
  const pendingFocus = useRef(false)
  const { transition } = useTransition()
  const plane = useRoutePlane(path)
  const { running } = usePreferences()
  usePageMetadata(path); useEditorialEffects(path)
  useLayoutEffect(() => {
    if (renderedPath.current !== path) {
      renderedPath.current = path
      pendingFocus.current = true
      window.scrollTo({ top: 0, behavior: 'instant' })
    }
  }, [path])
  useEffect(() => {
    if (transition || !pendingFocus.current) return
    pendingFocus.current = false
    document.querySelector<HTMLElement>('main h1')?.focus({ preventScroll: true })
  }, [path, transition])
  let content
  if (path === '/') content = <Home />
  else if (path === '/analyze') content = <Analyze session={session} />
  else if (path === '/learn') content = <Learn />
  else if (path.startsWith('/learn/')) content = <Article slug={path.slice('/learn/'.length)} />
  else if (path === '/evidence') content = <Evidence />
  else if (path === '/privacy') content = <Privacy />
  else if (path === '/android') content = <Android />
  else content = <NotFound />
  return <><a className="skip-link" href="#main-content">Skip to content</a><Header path={path} /><ReadingRail path={path} /><RouteScene transition={transition} /><main ref={plane} inert={!!transition} data-route-phase={transition?.phase ?? 'idle'} id="main-content" className={path === '/analyze' ? 'analyze-page' : 'public-page'} key={path} tabIndex={-1}>{content}</main><footer className="site-footer" inert={!!transition}><div className="footer-top"><Link href="/" className="footer-brand"><Mark />ScamSentry</Link><span className="eyebrow">A PERSONAL ACADEMIC RESEARCH PREVIEW / WEB {site.version}</span><button className="back-top" onClick={() => { document.querySelector<HTMLElement>('main h1')?.focus({ preventScroll: true }); scrollTo({ top: 0, behavior: running ? 'smooth' : 'instant' }) }}>Back to top <Arrow /></button></div><p className="creator-credit">{creatorCredit}</p><div className="footer-bottom"><span>No sender is authenticated. Verify independently.</span><External href={site.repository}>GitHub source</External><Link href="/privacy">Privacy</Link><Link href="/evidence">Evidence & credits</Link></div></footer><PointerBrackets /></>
}
export default function App() {
  const session = useAnalysis()
  return <PreferencesProvider busy={session.busy}><Router onNavigate={session.leave}>{path => <Shell path={path} session={session} />}</Router></PreferencesProvider>
}
