import { describe, expect, it } from 'vitest'
import { damping, heroPipelines, measureTrack, positionOnTrack, uniqueSegments } from '../src/motion/geometry'
import { edgePath, flowGraph, pointOnPolyline } from '../src/motion/flow'
import { isPublicPath, richNavigation, routeMotif } from '../src/motion/navigation'

describe('public motion geometry and privacy policy',()=>{
  it('never treats private, partial or unknown routes as rich public content',()=>{
    for(const path of ['/analyze','/analyze/result','/learn/private-draft','/not-found','/privacy?draft=private','//example.test']){
      expect(isPublicPath(path)).toBe(false)
      expect(richNavigation('/',path,true,false,false)).toBe(false)
      expect(richNavigation(path,'/',true,false,false)).toBe(false)
    }
    expect(isPublicPath('/learn/actual-host')).toBe(true)
    expect(richNavigation('/learn','/privacy',true,false,false)).toBe(true)
    for(const [running,forced,modal] of [[false,false,false],[true,true,false],[true,false,true]])expect(richNavigation('/learn','/privacy',running,forced,modal)).toBe(false)
    expect(richNavigation('/learn','/learn',true,false,false)).toBe(false)
    expect(new Set(['/','/learn','/evidence','/privacy','/android'].map(routeMotif)).size).toBe(5)
  })
  it('routes each public pipeline through square, bounded segments between real ports', () => {
    const { wide, compact } = heroPipelines
    expect(compact.nodes.length).toBeLessThan(wide.nodes.length)
    expect(wide.edges.length).toBeLessThan(12)
    for (const graph of [wide, compact]) {
      const segments = uniqueSegments(graph.edges)
      expect(segments.length).toBeLessThanOrEqual(22)
      for (const edge of graph.edges) {
        const from = graph.nodes.find(node => node.id === edge.from)!
        const to = graph.nodes.find(node => node.id === edge.to)!
        expect(edge.points[0]).toEqual([from.x + from.width, from.y + from.height / 2])
        expect(edge.points.at(-1)).toEqual([to.x, to.y + to.height / 2])
      }
      for (const [[x, y], [u, v]] of segments) {
        expect([x, y, u, v].every(Number.isFinite)).toBe(true)
        expect((x === u) !== (y === v)).toBe(true)
        expect(Math.min(x, u)).toBeGreaterThan(0)
        expect(Math.max(x, u)).toBeLessThan(graph.width)
        expect(Math.min(y, v)).toBeGreaterThan(0)
        expect(Math.max(y, v)).toBeLessThan(graph.height)
        for (const node of graph.nodes) {
          const intersectsInterior = Math.max(x, u) > node.x && Math.min(x, u) < node.x + node.width
            && Math.max(y, v) > node.y && Math.min(y, v) < node.y + node.height
          expect(intersectsInterior, 'Connector crosses ' + node.id).toBe(false)
        }
      }
    }
  })
  it('travels equal screen distances through an elbow under nonuniform scaling', () => {
    const track = measureTrack([[0, 0], [10, 0], [10, 20]], 2, .5)
    expect(track.length).toBe(30)
    expect([0, 10, 20, 25, 30].map(distance => positionOnTrack(track, distance)))
      .toEqual([[0, 0], [5, 0], [10, 0], [10, 10], [10, 20]])
  })
  it('uses time-based damping with bounded restoration deltas',()=>{
    const a=damping(8,48),b=damping(16,48)
    expect(1-(1-a)**2).toBeCloseTo(b,12)
    expect(damping(100000,48)).toBe(damping(40,48))
    expect(damping(-1,48)).toBe(0)
  })
  it('keeps every packet on one continuous edge with valid node ports and truthful external flow',()=>{
    for(const kind of ['hero','privacy','evidence'] as const){
      const graph=flowGraph(kind),ids=new Set(graph.nodes.map(n=>n.id))
      expect(new Set(graph.edges.map(e=>e.id)).size).toBe(graph.edges.length)
      for(const edge of graph.edges){
        expect(ids.has(edge.from)&&ids.has(edge.to)).toBe(true)
        expect(edgePath(edge).match(/M/g)).toHaveLength(1)
        expect(pointOnPolyline(edge.points,0)).toEqual(edge.points[0])
        expect(pointOnPolyline(edge.points,1)).toEqual(edge.points.at(-1))
        for(const [id,point] of [[edge.from,edge.points[0]],[edge.to,edge.points.at(-1)!]] as const){
          const n=graph.nodes.find(v=>v.id===id)!
          expect(point[0]>=n.x&&point[0]<=n.x+n.w&&point[1]>=n.y&&point[1]<=n.y+n.h).toBe(true)
        }
        for(let i=1;i<=20;i++){
          const a=pointOnPolyline(edge.points,(i-1)/20),b=pointOnPolyline(edge.points,i/20)
          expect(Math.hypot(b[0]-a[0],b[1]-a[1])).toBeLessThan(40)
        }
      }
    }
    const privacy=flowGraph('privacy')
    expect(privacy.edges.filter(e=>e.to==='assets')).toEqual([])
    expect(privacy.edges.filter(e=>e.to==='download').map(e=>e.from)).toEqual(['review'])
    expect(privacy.edges.filter(e=>e.from==='input').map(e=>e.to)).toEqual(['worker'])
  })
})
