import { createContext, useContext, useEffect, useState } from 'react'
import type { AnchorHTMLAttributes, ReactNode } from 'react'
import { articles, site } from './site'

const Routing = createContext<(path: string) => void>(() => {})
export function Router({ children, onNavigate }: { children: (path: string) => ReactNode; onNavigate: () => void }) {
  const [path, setPath] = useState(() => window.location.pathname.replace(/\/$/, '') || '/')
  useEffect(() => {
    const pop = () => { onNavigate(); setPath(window.location.pathname.replace(/\/$/, '') || '/') }
    window.addEventListener('popstate', pop)
    return () => window.removeEventListener('popstate', pop)
  }, [onNavigate])
  function navigate(next: string) {
    if (next === path) return
    onNavigate()
    window.history.pushState(null, '', next)
    setPath(next)
  }
  return <Routing.Provider value={navigate}>{children(path)}</Routing.Provider>
}
export function Link({ href, onClick, ...props }: AnchorHTMLAttributes<HTMLAnchorElement> & { href: string }) {
  const navigate = useContext(Routing)
  return <a {...props} href={href} onClick={event => {
    onClick?.(event)
    if (event.defaultPrevented || event.button !== 0 || event.metaKey || event.ctrlKey || event.shiftKey || event.altKey || props.target || !href.startsWith('/') || href.startsWith('//')) return
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
