import { useEffect, useRef, useState } from 'react'
import type { KeyboardEvent, ReactNode } from 'react'
import { Link } from './router'
import { usePreferences } from './preferences'

export function containDialogFocus(event: KeyboardEvent<HTMLDialogElement>) {
  if (event.key !== 'Tab') return
  const dialog = event.currentTarget
  const items = Array.from(dialog.querySelectorAll<HTMLElement>('a[href],button:not([disabled]),input:not([disabled]),select:not([disabled]),textarea:not([disabled]),[tabindex]:not([tabindex="-1"])')).filter(element => element.getClientRects().length > 0)
  const first = items[0], last = items.at(-1)
  if (!first) { event.preventDefault(); dialog.focus(); return }
  if (event.shiftKey && document.activeElement === first) { event.preventDefault(); last?.focus() }
  else if (!event.shiftKey && document.activeElement === last) { event.preventDefault(); first.focus() }
}

export function Mark({ className = '' }: { className?: string }) {
  return <svg className={`ss-mark ${className}`} width="32" height="32" viewBox="0 0 64 64" fill="none" stroke="currentColor" strokeWidth="7" strokeLinejoin="miter" aria-hidden="true"><path d="M30 12H16L8 20V28L28 36V44L20 52H6M58 12H44L36 20V28L56 36V44L48 52H34" /></svg>
}
export function Arrow({ down = false }: { down?: boolean }) { return <svg className="arrow" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.4" aria-hidden="true"><path d={down ? 'M5 5L19 19M5 19H19V5' : 'M5 19L19 5M5 5H19V19'} /></svg> }
export function External({ href, children, className }: { href: string; children: ReactNode; className?: string }) {
  return <a className={className} href={href} target="_blank" rel="noopener noreferrer">{children}<Arrow /><span className="sr-only"> (opens in a new tab)</span></a>
}
export function DecodedLabel({ children }: { children: string }) {
  const { running } = usePreferences()
  const [visual, setVisual] = useState(children)
  const element = useRef<HTMLSpanElement>(null)
  const started = useRef(false)
  useEffect(() => {
    if (!running || !element.current || started.current) return
    let timer: ReturnType<typeof setInterval> | undefined
    const observer = new IntersectionObserver(entries => {
      if (!entries.some(e => e.isIntersecting)) return
      started.current = true; observer.disconnect()
      let frame = 0
      const alphabet = '01/—·'
      timer = setInterval(() => {
        frame++
        setVisual(Array.from(children, (character, index) => character === ' ' || index < frame * 3 ? character : alphabet[(index + frame) % alphabet.length]).join(''))
        if (frame * 3 >= children.length) { clearInterval(timer); setVisual(children) }
      }, 32)
    })
    observer.observe(element.current)
    return () => { observer.disconnect(); clearInterval(timer) }
  }, [running, children])
  return <span className="eyebrow" ref={element}><span className="sr-only">{children}</span><span aria-hidden="true">{running ? visual : children}</span></span>
}
export function PageIntro({ number, label, title, lead }: { number: string; label: string; title: ReactNode; lead: string }) {
  return <header className="page-intro"><DecodedLabel>{`${number} / ${label}`}</DecodedLabel><h1 tabIndex={-1}>{title}</h1><p className="lead">{lead}</p></header>
}
export function Chapter({ number, to, label }: { number: string; to: string; label: string }) {
  return <nav className="chapter reveal" aria-label="Next chapter"><Link href={to} className="chapter-link" data-bracket><span className="eyebrow">END OF CHAPTER · {number}</span><span className="chapter-title">{label}<Arrow /></span><span className="eyebrow">CONTINUE READING</span></Link></nav>
}
export function SignalDiagram({ kind = 'hero' }: { kind?: 'hero' | 'privacy' }) {
  const ref = useRef<SVGSVGElement>(null)
  useEffect(() => {
    const node = ref.current
    if (!node) return
    const observer = new IntersectionObserver(entries => { node.dataset.visible = entries[0]?.isIntersecting ? 'true' : 'false' })
    observer.observe(node)
    return () => observer.disconnect()
  }, [])
  return <figure className={`signal-figure ${kind} reveal`}>
    <svg ref={ref} data-visible="false" viewBox="0 0 720 264" className="signal-diagram" role="img" aria-label={kind === 'hero' ? 'Conceptual signal flow: a message is inspected locally, then you independently verify the request.' : 'Public assets download to this browser. Your input stays in a local worker. A redacted file leaves only when you review and download it.'}>
      <g fill="none" stroke="currentColor" strokeWidth="1">
        <path className="diagram-rule" d="M0 132H128L176 84H280M0 172H108L176 104H280M0 92H136L176 132H280M440 132H520L572 80H720M440 152H548L592 196H720" />
        <path className="signal-packet" d="M0 132H128L176 84H280M440 132H520L572 80H720" />
        <path className="diagram-boundary" d="M248 24H472V240H248Z" strokeDasharray="3 7" />
        <path d="M292 72H428V192H292Z" />
        <path d="M310 92H340V122H310ZM380 142H410V172H380Z" />
        <path d="M340 106H370V156H380M322 122V156H360" />
      </g>
      <g fill="currentColor"><rect className="diagram-node" x="124" y="128" width="8" height="8" /><rect className="diagram-node node-two" x="516" y="128" width="8" height="8" /><circle cx="360" cy="132" r="5" /></g>
      <g className="diagram-labels" fill="currentColor"><text x="0" y="238">{kind === 'hero' ? '01 / YOUR INPUT' : '01 / PUBLIC ASSETS'}</text><text x="264" y="258">02 / LOCAL INSPECTION</text><text x="546" y="238">{kind === 'hero' ? '03 / YOUR JUDGMENT' : '03 / REVIEWED EXPORT'}</text></g>
    </svg>
    <figcaption><span className="status-square" aria-hidden="true" /> {kind === 'hero' ? 'Conceptual signal flow. No live traffic or inference progress is represented.' : 'Input is processed in browser memory. Only an explicitly downloaded redacted file leaves this flow.'}</figcaption>
  </figure>
}
export function useEditorialEffects(path: string) {
  const { running } = usePreferences()
  useEffect(() => {
    const nodes = document.querySelectorAll<HTMLElement>('.reveal')
    if (!running || !('IntersectionObserver' in window)) { nodes.forEach(n => n.classList.add('revealed')); return }
    const observer = new IntersectionObserver(entries => entries.forEach(entry => {
      if (entry.isIntersecting) { entry.target.classList.add('revealed'); observer.unobserve(entry.target) }
    }), { threshold: 0.08 })
    nodes.forEach(n => { if (!n.classList.contains('revealed')) { n.classList.add('reveal-ready'); observer.observe(n) } })
    return () => { observer.disconnect(); nodes.forEach(n => n.classList.add('revealed')) }
  }, [path, running])
}
export function ReadingRail({ path }: { path: string }) {
  const ref = useRef<HTMLDivElement>(null)
  useEffect(() => {
    let frame = 0
    const update = () => {
      cancelAnimationFrame(frame)
      frame = requestAnimationFrame(() => {
        const available = document.documentElement.scrollHeight - innerHeight
        ref.current?.style.setProperty('--progress', String(available > 0 ? Math.min(1, scrollY / available) : 0))
      })
    }
    update(); addEventListener('scroll', update, { passive: true }); addEventListener('resize', update)
    return () => { cancelAnimationFrame(frame); removeEventListener('scroll', update); removeEventListener('resize', update) }
  }, [path])
  return <div className="reading-rail" ref={ref} aria-hidden="true"><span /></div>
}
export function PointerBrackets() {
  const ref = useRef<HTMLDivElement>(null)
  const { running } = usePreferences()
  useEffect(() => {
    const element = ref.current
    if (!element || !running || !matchMedia('(hover: hover) and (pointer: fine)').matches) return
    const over = (event: PointerEvent) => {
      const target = (event.target as Element).closest<HTMLElement>('[data-bracket]')
      if (!target) { element.hidden = true; return }
      const rect = target.getBoundingClientRect()
      element.style.left = `${rect.left - 5}px`; element.style.top = `${rect.top - 5}px`
      element.style.width = `${rect.width + 10}px`; element.style.height = `${rect.height + 10}px`; element.hidden = false
    }
    const hide = () => { element.hidden = true }
    document.addEventListener('pointerover', over); addEventListener('scroll', hide, { passive: true })
    return () => { hide(); document.removeEventListener('pointerover', over); removeEventListener('scroll', hide) }
  }, [running])
  return <div ref={ref} hidden className="pointer-brackets" aria-hidden="true" />
}
