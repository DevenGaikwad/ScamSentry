import { useEffect, useRef } from 'react'
import { usePreferences } from '../preferences'
import { heroPipelines, measureTrack, positionOnTrack, uniqueSegments } from './geometry'

const layouts = Object.entries(heroPipelines).map(([name, pipeline]) => ({
  name,
  ...pipeline,
  segments: uniqueSegments(pipeline.edges),
  tracks: pipeline.markers.map(id => pipeline.edges.find(edge => edge.id === id)!),
}))

export function HeroNetwork() {
  const host = useRef<HTMLDivElement>(null)
  const { running } = usePreferences()

  useEffect(() => {
    const element = host.current
    if (!element || !running || typeof ResizeObserver !== 'function' || typeof IntersectionObserver !== 'function') return

    let frame = 0
    let previous = 0
    let elapsed = 0
    let frames = 0
    let visible = false
    let disposed = false
    let failed = false
    let markers: { node: SVGRectElement; track: ReturnType<typeof measureTrack> }[] = []

    function stop() {
      cancelAnimationFrame(frame)
      frame = 0
      previous = 0
      element!.dataset.running = 'false'
    }

    function animate(now: number) {
      frame = 0
      if (disposed || failed || !visible || document.hidden) return
      try {
        if (!previous || now - previous >= 32) {
          elapsed += previous ? Math.min(50, now - previous) : 0
          previous = now
          for (const [index, { node, track }] of markers.entries()) {
            const phase = (elapsed * .062 / track.length + index / markers.length + .12) % 1
            const [x, y] = positionOnTrack(track, phase * track.length)
            node.setAttribute('transform', 'translate(' + x + ' ' + y + ')')
            node.setAttribute('opacity', String(Math.min(1, phase * 18, (1 - phase) * 18)))
          }
          element!.dataset.frames = String(++frames)
          element!.dataset.running = 'true'
        }
        frame = requestAnimationFrame(animate)
      } catch {
        failed = true
        stop()
      }
    }

    function sync() {
      stop()
      if (!disposed && !failed && visible && !document.hidden && markers.length) {
        frame = requestAnimationFrame(animate)
      }
    }

    function resize() {
      if (disposed || failed) return
      markers = []
      for (const layout of layouts) {
        const svg = element!.querySelector<SVGSVGElement>('svg[data-pipeline="' + layout.name + '"]')
        if (!svg?.clientWidth || !svg.clientHeight) continue
        const nodes = svg.querySelectorAll<SVGRectElement>('.pipeline-packets rect')
        markers = layout.tracks.map((edge, index) => ({
          node: nodes[index],
          track: measureTrack(edge.points, svg.clientWidth / layout.width, svg.clientHeight / layout.height),
        }))
        break
      }
      sync()
    }

    const size = new ResizeObserver(resize)
    const intersection = new IntersectionObserver(([entry]) => {
      visible = entry.isIntersecting
      sync()
    })
    size.observe(element)
    intersection.observe(element)
    document.addEventListener('visibilitychange', sync)
    resize()
    return () => {
      disposed = true
      stop()
      size.disconnect()
      intersection.disconnect()
      document.removeEventListener('visibilitychange', sync)
    }
  }, [running])

  return <div ref={host} className="hero-network" aria-hidden="true" data-running="false">
    {layouts.map(layout => <svg key={layout.name} data-pipeline={layout.name}
      viewBox={'0 0 ' + layout.width + ' ' + layout.height} preserveAspectRatio="none">
      <g className="pipeline-wires">
        {layout.segments.map(([a, b]) => <line key={a.join(',') + '/' + b.join(',')}
          x1={a[0]} y1={a[1]} x2={b[0]} y2={b[1]} />)}
      </g>
      <g className="pipeline-modules">
        {layout.nodes.map(({ id, label, x, y, width, height }) => <g key={id}>
          <rect x={x} y={y} width={width} height={height} />
          <text x={x + width / 2} y={y + height / 2} dominantBaseline="central" textAnchor="middle">{label}</text>
          <rect className="pipeline-contact" x={x + width - 2} y={y + height / 2 - 2} width={4} height={4} />
        </g>)}
      </g>
      {layout.junctions.map(([x, y]) => <rect className="pipeline-contact" key={x} x={x - 2} y={y - 2} width={4} height={4} />)}
      <g className="pipeline-packets">
        {layout.markers.map(id => <rect key={id} x={-2.8} y={-2.8} width={5.6} height={5.6} />)}
      </g>
    </svg>)}
  </div>
}
