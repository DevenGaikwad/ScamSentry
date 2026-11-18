import { useEffect, useId, useMemo, useRef } from 'react'
import { usePreferences } from '../preferences'
import { edgePath, flowGraph, pointOnPolyline } from './flow'
import type { FlowKind } from './flow'

export function SignalDiagram({kind='hero'}:{kind?:FlowKind}) {
  const ref=useRef<SVGSVGElement>(null), drawn=useRef(false)
  const id=useId().replaceAll(':',''), graph=useMemo(()=>flowGraph(kind),[kind])
  const { running }=usePreferences()
  useEffect(()=>{
    const svg=ref.current
    if(!svg)return
    let frame=0, visible=false, last=0, elapsed=0, destroyed=false, revealUntil=0
    const paths=Array.from(svg.querySelectorAll<SVGPathElement>('.flow-edge'))
    const packets=Array.from(svg.querySelectorAll<SVGRectElement>('.flow-packet'))
    // Fixed arc-length lookup, allocated once. Each packet belongs to one continuous edge.
    const tables=graph.edges.map(edge=>Array.from({length:97},(_,i)=>pointOnPolyline(edge.points,i/96)))
    const animations:Animation[]=[]
    const stop=()=>{cancelAnimationFrame(frame);frame=0;last=0;svg.dataset.running='false';packets.forEach(p=>p.setAttribute('opacity','0'))}
    const tick=(now:number)=>{
      frame=0
      if(destroyed||!running||!visible||document.hidden)return
      if(now<revealUntil){frame=requestAnimationFrame(tick);return}
      elapsed+=last?Math.min(40,now-last):0;last=now
      tables.forEach((table,i)=>{
        const t=((elapsed/1000)/(3.2+i*.24)+i*.23)%1, k=t*96, a=table[Math.floor(k)],b=table[Math.min(96,Math.floor(k)+1)],f=k%1
        packets[i].setAttribute('transform',`translate(${a[0]+(b[0]-a[0])*f} ${a[1]+(b[1]-a[1])*f})`)
        packets[i].setAttribute('opacity',String(Math.min(1,t*12,(1-t)*12)))
      })
      svg.dataset.frames=String(Math.floor(elapsed))
      frame=requestAnimationFrame(tick)
    }
    const sync=()=>{
      if(destroyed)return
      stop();svg.dataset.visible=String(visible)
      if(!running||!visible||document.hidden){animations.forEach(a=>a.cancel());return}
      svg.dataset.running='true'
      if(!drawn.current){
        drawn.current=true
        revealUntil=performance.now()+650
        paths.forEach(path=>{try{const length=path.getTotalLength();const a=path.animate([{strokeDasharray:`${length} ${length}`,strokeDashoffset:length},{strokeDasharray:`${length} ${length}`,strokeDashoffset:0}],{duration:650,easing:'ease-out'});void a.finished.catch(()=>{});animations.push(a)}catch{/* Completed path is the fallback. */}})
      }
      frame=requestAnimationFrame(tick)
    }
    const observer=new IntersectionObserver(entries=>{visible=entries[0]?.isIntersecting??false;sync()},{threshold:.1})
    observer.observe(svg);document.addEventListener('visibilitychange',sync)
    return()=>{destroyed=true;stop();observer.disconnect();document.removeEventListener('visibilitychange',sync);animations.forEach(a=>a.cancel())}
  },[graph,running])
  return <figure className={`signal-figure ${kind} reveal`}><svg ref={ref} viewBox="0 0 724 320" data-visible="false" data-running="false" className="signal-diagram flow-diagram" role="img" aria-label={graph.label}>
    <defs><marker id={`${id}-arrow`} viewBox="0 0 8 8" refX="7" refY="4" markerWidth="5" markerHeight="5" orient="auto-start-reverse"><path d="M1 1L7 4L1 7" fill="none" stroke="currentColor"/></marker></defs>
    {graph.boundary&&<rect className="flow-boundary" x={graph.boundary.x} y={graph.boundary.y} width={graph.boundary.w} height={graph.boundary.h} fill="none" stroke="currentColor" strokeDasharray="3 7"/>}
    <g fill="none" stroke="currentColor">{graph.edges.map(edge=><path key={edge.id} id={`${id}-${edge.id}`} className="flow-edge" data-edge={edge.id} data-from={edge.from} data-to={edge.to} d={edgePath(edge)} markerEnd={`url(#${id}-arrow)`}/>)}</g>
    {graph.nodes.map(node=><g key={node.id} className="flow-node" data-node={node.id}><rect x={node.x} y={node.y} width={node.w} height={node.h}/><text x={node.x+node.w/2} y={node.y+(node.lines.length===1?28:20)} textAnchor="middle">{node.lines.map((line,i)=><tspan key={line} x={node.x+node.w/2} dy={i?15:0}>{line}</tspan>)}</text></g>)}
    <g aria-hidden="true" fill="currentColor">{graph.edges.map(edge=><rect key={edge.id} className="flow-packet" data-edge={edge.id} x="-2.5" y="-2.5" width="5" height="5" opacity="0"/>)}</g>
  </svg><ul className="flow-mobile-legend" aria-label="Diagram nodes">{graph.nodes.map(node=><li key={node.id}>{node.lines.join(' ')}</li>)}</ul><figcaption><span className="status-square" aria-hidden="true"/> {graph.caption}</figcaption></figure>
}
