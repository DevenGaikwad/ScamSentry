import { createContext, useCallback, useContext, useEffect, useRef, useState } from 'react'
import type { AnchorHTMLAttributes, ReactNode } from 'react'
import { articles, site } from './site'
import { usePreferences } from './preferences'
import { richNavigation, routeMotif } from './motion/navigation'
import type { Transition } from './motion/navigation'

const Routing = createContext<(path: string) => void>(() => {})
const Choreography = createContext<{ transition: Transition | null; settle: (id: number) => void }>({ transition:null,settle:()=>{} })
export const useTransition = () => useContext(Choreography)
export function Router({ children, onNavigate }: { children: (path: string) => ReactNode; onNavigate: () => void }) {
  const [path, setPath] = useState(() => window.location.pathname.replace(/\/$/, '') || '/')
  const [transition,setTransition]=useState<Transition|null>(null)
  const current=useRef(path), generation=useRef(0)
  const { running }=usePreferences()
  const settle=useCallback((id:number)=>setTransition(value=>value?.id===id?null:value),[])
  const commit=useCallback((next:string)=>{
    if(next===current.current)return
    const from=current.current;current.current=next
    onNavigate()
    const allowed=richNavigation(from,next,running,matchMedia('(forced-colors: active)').matches,!!document.querySelector('dialog[open]'))&&typeof Element.prototype.animate==='function'
    const id=++generation.current
    setTransition(allowed?{id,from,to:next,motif:routeMotif(next),phase:'departing',duration:innerWidth<768?720:1120}:null)
    setPath(next)
  },[onNavigate,running])
  useEffect(() => {
    const pop = () => commit(window.location.pathname.replace(/\/$/, '') || '/')
    window.addEventListener('popstate', pop)
    return () => window.removeEventListener('popstate', pop)
  }, [commit])
  useEffect(()=>{
    if(!running)setTransition(null)
  },[running])
  const transitionId=transition?.id, duration=transition?.duration
  useEffect(()=>{
    if(transitionId===undefined||duration===undefined)return
    const phase=(value:Transition['phase'])=>setTransition(t=>t?.id===transitionId?{...t,phase:value}:t)
    const timers=[setTimeout(()=>phase('scene'),duration*.18),setTimeout(()=>phase('arriving'),duration*.42),setTimeout(()=>phase('settling'),duration*.92),setTimeout(()=>settle(transitionId),duration)]
    // Completion is independent of graphics callbacks; restoration always has a bound.
    const watchdog=setTimeout(()=>settle(transitionId),duration+180)
    const abort=()=>settle(transitionId)
    const forced=matchMedia('(forced-colors: active)')
    document.addEventListener('visibilitychange',abort);forced.addEventListener('change',abort)
    return()=>{timers.forEach(clearTimeout);clearTimeout(watchdog);document.removeEventListener('visibilitychange',abort);forced.removeEventListener('change',abort)}
  },[transitionId,duration,settle])
  function navigate(next: string) {
    if (next === current.current) return
    window.history.pushState(null, '', next)
    commit(next)
  }
  return <Routing.Provider value={navigate}><Choreography.Provider value={{transition,settle}}>{children(path)}</Choreography.Provider></Routing.Provider>
}
export function Link({ href, onClick, ...props }: AnchorHTMLAttributes<HTMLAnchorElement> & { href: string }) {
  const navigate = useContext(Routing)
  return <a {...props} href={href} onClick={event => {
    onClick?.(event)
    if (event.defaultPrevented || event.button !== 0 || event.metaKey || event.ctrlKey || event.shiftKey || event.altKey || props.target || props.download !== undefined || href.includes('#') || !href.startsWith('/') || href.startsWith('//')) return
    event.preventDefault(); navigate(href)
  }} />
}
export function routeInfo(path: string) {
  const pages: Record<string, [string, string]> = {
    '/': ['A second look before you act.', 'Inspect messages and links locally. Clear observations, an English research model and your independent judgment.'],
    '/analyze': ['Analyze a message', 'Inspect a message or link in your browser. Your draft stays in memory and no link is opened.'],
    '/learn': ['Build your second-look instinct', 'Six practical guides to sender identity, private codes, payments, links, uncertainty and promotional messages.'],
    '/evidence': ['Know the model. Know its limits.', 'Historical benchmark, class supports, confusion matrix, uncertainty and provenance for the approved ScamSentry model.'],
    '/privacy': ['Your input stays here.', 'Understand local browser processing, public asset downloads, preferences and reviewed redacted exports.'],
    '/android': ['A second look, on Android.', 'Download the existing test-signed Android research preview with local screenshot text recognition and review.'],
  }
  const article = articles.find(a => `/learn/${a.slug}` === path)
  return pages[path] ?? (article ? [article.title, article.lead] : ['Page not found', 'This page could not be found. Return to ScamSentry or analyze a message.'])
}
export function usePageMetadata(path: string) {
  useEffect(() => {
    const [title, description] = routeInfo(path)
    document.title = `${title} — ScamSentry`
    document.querySelector('meta[name="description"]')?.setAttribute('content', description)
    const known = ['/', '/analyze', '/learn', '/evidence', '/privacy', '/android', ...articles.map(a => `/learn/${a.slug}`)].includes(path)
    const canonical = site.origin + (known ? path : '/')
    document.querySelector('link[rel="canonical"]')?.setAttribute('href', canonical)
    document.querySelector('meta[property="og:url"]')?.setAttribute('content', canonical)
    document.querySelector('meta[property="og:title"]')?.setAttribute('content', `${title} — ScamSentry`)
    document.querySelector('meta[property="og:description"]')?.setAttribute('content', description)
  }, [path])
}
