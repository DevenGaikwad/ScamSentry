import { useEffect, useRef } from 'react'
import { useTransition } from '../router'
import { routeInfo } from '../router'
import { arrivalKeyframes } from './navigation'
import type { Transition } from './navigation'

/** Only allowlisted static metadata enters the outgoing representation. Never a DOM copy. */
export function RouteScene({ transition }: { transition: Transition | null }) {
  if(!transition)return null
  const [title,description]=routeInfo(transition.from)
  return <div key={transition.id} className={`route-scene motif-${transition.motif}`} data-phase={transition.phase} data-motif={transition.motif} aria-hidden="true" inert>
    <svg viewBox="0 0 1440 850" preserveAspectRatio="xMidYMid slice" className="depth-geometry">
      <g className="depth-rails" fill="none" stroke="currentColor"><path d="M-80 80L490 250L720 425L950 250L1520 80M-80 770L490 600L720 425L950 600L1520 770M0 425H1440M720 0V850M90 0L720 425L1350 850M1350 0L720 425L90 850"/></g>
      <g className="depth-rings" fill="none" stroke="currentColor"><circle cx="720" cy="425" r="210"/><circle cx="720" cy="425" r="285"/><ellipse cx="720" cy="425" rx="390" ry="160"/></g>
      <g className="depth-frames" fill="none" stroke="currentColor">{[0,1,2,3].map(i=><rect key={i} x={120+i*105} y={70+i*63} width={1200-i*210} height={710-i*126}/>)}</g>
      <g className="depth-markers" fill="currentColor"><rect x="400" y="320" width="7" height="7"/><rect x="1030" y="525" width="7" height="7"/><rect x="715" y="210" width="7" height="7"/></g>
    </svg>
    <div className="outgoing-public-plane"><span className="eyebrow">SCAMSENTRY / PUBLIC CHAPTER</span><p className="public-plane-title">{title}</p><p>{description}</p><div className="public-plane-rule"/></div>
    <span className="scene-coordinate eyebrow">SCAMSENTRY / {transition.motif.toUpperCase()} / PUBLIC CHAPTER</span>
  </div>
}
export function useRoutePlane(path: string) {
  const ref=useRef<HTMLElement>(null)
  const { transition, settle }=useTransition()
  const id=transition?.id, duration=transition?.duration
  useEffect(()=>{
    if(id===undefined||duration===undefined||!ref.current)return
    let animation: Animation | undefined
    try {
      animation=ref.current.animate(arrivalKeyframes,{duration,easing:'linear',fill:'both'})
      void animation.finished.catch(error=>{if(!(error instanceof DOMException&&error.name==='AbortError'))settle(id)})
    } catch { settle(id) }
    return ()=>animation?.cancel()
  },[id,duration,path,settle])
  return ref
}
