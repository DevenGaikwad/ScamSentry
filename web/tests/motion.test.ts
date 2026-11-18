import { describe, expect, it } from 'vitest'
import { damping, heroGeometry, project } from '../src/motion/geometry'
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
  it('generates deterministic bounded geometry and finite projections on narrow and high-density layouts',()=>{
    expect(heroGeometry()).toEqual(heroGeometry())
    expect(heroGeometry().strands).toHaveLength(42)
    expect(heroGeometry(true).strands).toHaveLength(24)
    for(const compact of [false,true])for(const [width,height] of [[320,320],[390,600],[1363,800],[2560,1400]]){
      for(const strand of heroGeometry(compact).strands)for(const p of strand.points){
        const v=project(p,width,height,.075)
        expect(v).not.toBeNull();expect(Number.isFinite(v!.x)&&Number.isFinite(v!.y)).toBe(true)
        expect(Math.abs(v!.x)).toBeLessThan(width*2);expect(Math.abs(v!.y)).toBeLessThan(height*3)
      }
    }
    expect(project({x:0,y:0,z:2.7},1363,936)).toBeNull()
    expect(project({x:NaN,y:0,z:0},1363,936)).toBeNull()
    expect(project({x:0,y:0,z:0},0,936)).toBeNull()
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
