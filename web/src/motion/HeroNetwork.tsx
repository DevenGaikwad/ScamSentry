import { useEffect, useRef } from 'react'
import { usePreferences } from '../preferences'
import { cubeEdges, cubeVertices, heroGeometry, project } from './geometry'

export function HeroNetwork() {
  const host = useRef<HTMLDivElement>(null), canvas = useRef<HTMLCanvasElement>(null)
  const { running, dark } = usePreferences()
  useEffect(() => {
    const element = host.current, node = canvas.current
    if (!element || !node) return
    let ctx: CanvasRenderingContext2D | null = null
    try { ctx = node.getContext('2d', { alpha: true }) } catch { /* The original SVG remains composed. */ }
    if (!ctx) { element.dataset.renderer = 'static'; return }
    const context = ctx
    let width = 0, height = 0, frame = 0, visible = true, destroyed = false, last = 0, elapsed = 0, draws = 0
    let geometry = heroGeometry(), cubes = geometry.boxes.map((p,i) => cubeVertices(p,i===3?.058:.021))
    const palette = getComputedStyle(element), wire = palette.getPropertyValue('--wire').trim(), ink = palette.getPropertyValue('--wire-node').trim()
    const fail = () => { cancelAnimationFrame(frame); frame = 0; element.dataset.renderer = 'static'; element.dataset.running = 'false' }
    const draw = () => {
      if (destroyed || !width || !height) return
      try {
        context.clearRect(0,0,width,height)
        const angle = running ? Math.sin(elapsed / 18000) * .075 : 0
        context.strokeStyle = wire; context.lineWidth = .85
        for (const strand of geometry.strands) {
          context.beginPath()
          strand.points.forEach((p,i) => { const v=project(p,width,height,angle); if(v) { if(i===0)context.moveTo(v.x,v.y);else context.lineTo(v.x,v.y) } })
          context.stroke()
        }
        context.strokeStyle = ink; context.lineWidth = 1
        for (const cube of cubes) {
          const vertices = cube.map(p=>project(p,width,height,angle))
          context.beginPath()
          for (const [a,b] of cubeEdges) { const va=vertices[a],vb=vertices[b];if(va&&vb){context.moveTo(va.x,va.y);context.lineTo(vb.x,vb.y)} }
          context.stroke()
        }
        // Six travelling markers; each remains on its own sampled strand.
        if (running) for (let i=0;i<geometry.strands.length;i+=7) {
          const strand=geometry.strands[i], t=(elapsed/6500+strand.phase)%1, k=t*32, a=strand.points[Math.floor(k)], b=strand.points[Math.min(32,Math.floor(k)+1)], f=k%1
          const v=project({x:a.x+(b.x-a.x)*f,y:a.y+(b.y-a.y)*f,z:a.z+(b.z-a.z)*f},width,height,angle)
          if(v){context.fillStyle=ink;context.fillRect(v.x-1.8,v.y-1.8,3.6,3.6)}
        }
        element.dataset.renderer = 'canvas'; element.dataset.frames = String(++draws)
      } catch { fail() }
    }
    const tick = (now: number) => {
      frame = 0
      if (destroyed || !running || !visible || document.hidden) return
      if (!last || now-last >= 32) { elapsed += last ? Math.min(now-last,50) : 0; last=now; draw() }
      if (element.dataset.renderer !== 'static') frame=requestAnimationFrame(tick)
    }
    const sync = () => {
      cancelAnimationFrame(frame); frame=0; last=0
      const active=running&&visible&&!document.hidden
      element.dataset.running=String(active)
      draw()
      if(active&&element.dataset.renderer!=='static') frame=requestAnimationFrame(tick)
    }
    const resize = () => {
      const rect=element.getBoundingClientRect(), w=Math.round(rect.width), h=Math.round(rect.height)
      if(w===width&&h===height)return
      width=w;height=h
      const ratio=Math.min(devicePixelRatio||1,1.75)
      node.width=Math.max(1,Math.round(width*ratio));node.height=Math.max(1,Math.round(height*ratio));context.setTransform(ratio,0,0,ratio,0,0)
      geometry=heroGeometry(width<640);cubes=geometry.boxes.map((p,i)=>cubeVertices(p,(width<640?i===2:i===3)?.058:.021))
      sync()
    }
    const observer=new ResizeObserver(resize), intersection=new IntersectionObserver(entries=>{visible=entries[0]?.isIntersecting??false;sync()})
    observer.observe(element);intersection.observe(element);document.addEventListener('visibilitychange',sync);resize()
    return () => { destroyed=true;cancelAnimationFrame(frame);observer.disconnect();intersection.disconnect();document.removeEventListener('visibilitychange',sync);element.dataset.running='false' }
  }, [running,dark])
  return <div ref={host} className="hero-network" aria-hidden="true" data-renderer="static" data-running="false">
    <svg className="hero-static" viewBox="0 0 1000 500" preserveAspectRatio="none"><g fill="none" stroke="currentColor"><path d="M40 95C280 0 300 275 455 185M40 95C270 200 320 60 455 185M40 275C210 40 400 380 455 215M40 275C310 420 320 100 455 215M180 425C260 40 340 380 455 215M545 185C700 20 750 250 960 95M545 185C650 310 780 0 960 95M545 215C700 380 780 100 960 275M545 215C720 40 760 440 960 275M545 215C620 420 720 230 820 425"/><path d="M455 145H545V235H455ZM470 130H560V220H470ZM455 145L470 130M545 145L560 130M545 235L560 220M455 235L470 220M20 80H60V110H20ZM940 80H980V110H940ZM20 260H60V290H20ZM940 260H980V290H940Z"/></g></svg>
    <canvas ref={canvas} />
  </div>
}
