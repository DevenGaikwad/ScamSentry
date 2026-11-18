import { useEffect, useRef } from 'react'
import { usePreferences } from '../preferences'
import { damping } from './geometry'

const editable = 'textarea,input,select,[contenteditable="true"],[contenteditable=""]'
const actionable = 'a[href],button,summary,[role="button"],[data-bracket]'
export function PointerBrackets() {
  const ref = useRef<HTMLDivElement>(null)
  const { running } = usePreferences()
  useEffect(() => {
    const root = ref.current
    if (!root) return
    root.hidden = true
    const capability = matchMedia('(any-hover: hover) and (any-pointer: fine)'), forced = matchMedia('(forced-colors: active)')
    if (!running) return
    let frame = 0, last = 0, initialized = false, target: HTMLElement | null = null
    let pointer = { x: 0, y: 0 }, current = { x: 0, y: 0, w: 34, h: 34 }, goal = { ...current }
    const hide = () => { root.hidden = true; initialized = false; target=null; targetObserver?.disconnect(); cancelAnimationFrame(frame); frame = 0 }
    const blocked = () => !capability.matches || forced.matches || !!document.querySelector('dialog[open]') || !!document.activeElement?.matches(editable)
    const tick = (now: number) => {
      frame = 0
      if (blocked()) { hide(); return }
      const dt = last ? now - last : 16; last = now
      const a = damping(dt, 48), b = damping(dt, 100)
      current.x += (goal.x - current.x) * a; current.y += (goal.y - current.y) * a
      current.w += (goal.w - current.w) * b; current.h += (goal.h - current.h) * b
      root.style.transform = `translate3d(${current.x-current.w/2}px,${current.y-current.h/2}px,0)`
      root.style.width = `${current.w}px`; root.style.height = `${current.h}px`; root.hidden = false
      if (Math.max(Math.abs(goal.x-current.x),Math.abs(goal.y-current.y),Math.abs(goal.w-current.w),Math.abs(goal.h-current.h)) > .08) frame = requestAnimationFrame(tick)
    }
    const schedule = () => { if (!frame) { last = 0; frame = requestAnimationFrame(tick) } }
    const bounds = () => {
      if (target && (!target.isConnected || target.closest('[inert],:disabled,[aria-disabled="true"]'))) target = null
      const rect = target?.getBoundingClientRect()
      if (rect && rect.width > 0 && rect.width < 320 && rect.height < 120) {
        goal = { x: rect.left + rect.width / 2, y: rect.top + rect.height / 2, w: rect.width + 12, h: rect.height + 12 }
        root.dataset.state = 'snap'
      } else { goal = { ...pointer, w: 34, h: 34 }; root.dataset.state = 'follow' }
      schedule()
    }
    const move = (event: PointerEvent) => {
      if (event.pointerType !== 'mouse' || blocked()) { hide(); return }
      pointer = { x: event.clientX, y: event.clientY }
      const element = event.target instanceof Element ? event.target : null
      if (element?.closest(editable)) { hide(); return }
      const next=element?.closest<HTMLElement>(actionable) ?? null
      if(next!==target){targetObserver?.disconnect();target=next;if(target)targetObserver?.observe(target)}
      if (!initialized) { current = { ...pointer, w: 34, h: 34 }; initialized = true }
      bounds()
    }
    const leave = (event: PointerEvent) => { if (!event.relatedTarget) hide() }
    const scroll = () => { target = null; targetObserver?.disconnect(); if (initialized) bounds() }
    const resize = () => { if (initialized) bounds() }
    const focus = () => { if (blocked()) hide() }
    const targetObserver=new ResizeObserver(()=>{if(initialized)bounds()})
    const observer = new MutationObserver(focus)
    observer.observe(document.body, { subtree: true, childList: true, attributes: true, attributeFilter: ['open','inert','disabled'] })
    document.addEventListener('pointermove', move, { passive: true }); document.addEventListener('pointerout', leave)
    document.addEventListener('focusin', focus); addEventListener('blur', hide); addEventListener('scroll', scroll, { passive: true }); addEventListener('resize', resize)
    capability.addEventListener('change', hide); forced.addEventListener('change', hide)
    return () => { hide(); observer.disconnect(); targetObserver?.disconnect(); document.removeEventListener('pointermove', move); document.removeEventListener('pointerout', leave); document.removeEventListener('focusin', focus); removeEventListener('blur', hide); removeEventListener('scroll', scroll); removeEventListener('resize', resize); capability.removeEventListener('change', hide); forced.removeEventListener('change', hide) }
  }, [running])
  return <div ref={ref} hidden className="pointer-brackets" aria-hidden="true"><i /><i /><i /><i /></div>
}
