/** Public, seeded decoration only. Coordinates never depend on messages or results. */
export type Point3 = { x: number; y: number; z: number }
export type Point2 = { x: number; y: number }
export type Strand = { points: Point3[]; phase: number }
export function damping(deltaMs: number, tauMs: number) {
  return 1 - Math.exp(-Math.min(40, Math.max(0, deltaMs)) / tauMs)
}
export function seeded(seed: number) {
  let value = seed >>> 0 || 1
  return () => { value ^= value << 13; value ^= value >>> 17; value ^= value << 5; return (value >>> 0) / 4294967296 }
}
export function cubic(a: Point3, b: Point3, c: Point3, d: Point3, t: number): Point3 {
  const u = 1 - t
  return { x: u ** 3 * a.x + 3 * u * u * t * b.x + 3 * u * t * t * c.x + t ** 3 * d.x,
    y: u ** 3 * a.y + 3 * u * u * t * b.y + 3 * u * t * t * c.y + t ** 3 * d.y,
    z: u ** 3 * a.z + 3 * u * u * t * b.z + 3 * u * t * t * c.z + t ** 3 * d.z }
}
export function heroGeometry(compact = false, seed = 37031) {
  const random = seeded(seed)
  const boxes = [
    { x: -.43, y: -.18, z: -.08 }, { x: -.44, y: .13, z: .10 }, { x: -.29, y: .32, z: -.15 },
    { x: 0, y: .03, z: .16 }, { x: .43, y: -.18, z: -.08 }, { x: .44, y: .13, z: .10 }, { x: .29, y: .32, z: -.15 },
  ].filter((_, i) => !compact || i !== 2 && i !== 6)
  const strands: Strand[] = []
  for (let i = 0; i < (compact ? 24 : 42); i++) {
    const fromLeft = i % 2 === 0
    const peripheral = boxes[(fromLeft ? 0 : compact ? 3 : 4) + i % (compact ? 2 : 3)]
    const core = { x: (fromLeft ? -.045 : .045), y: .03 + (random() - .5) * .14, z: .16 }
    const a = fromLeft ? peripheral : core, d = fromLeft ? core : peripheral
    const spread = (random() - .5) * .28
    const b = { x: a.x * .66 + d.x * .34, y: a.y + spread, z: (random() - .5) * .6 }
    const c = { x: a.x * .34 + d.x * .66, y: d.y - spread, z: (random() - .5) * .6 }
    strands.push({ points: Array.from({ length: 33 }, (_, j) => cubic(a, b, c, d, j / 32)), phase: random() })
  }
  return { boxes, strands }
}
/** Rotation order: world Y, then X. Camera looks toward +z; near plane is rejected. */
export function project(point: Point3, width: number, height: number, angle = 0): Point2 | null {
  if (![point.x, point.y, point.z, width, height, angle].every(Number.isFinite) || width <= 0 || height <= 0) return null
  const x = point.x * Math.cos(angle) + point.z * Math.sin(angle)
  const z0 = -point.x * Math.sin(angle) + point.z * Math.cos(angle)
  const tilt = angle * .35
  const y = point.y * Math.cos(tilt) - z0 * Math.sin(tilt)
  const z = point.y * Math.sin(tilt) + z0 * Math.cos(tilt)
  const focal = 2.8
  if (z >= focal - .25) return null
  const scale = focal / (focal - z), unit = Math.min(width, height * 1.9)
  return { x: width * .5 + x * unit * scale, y: height * .38 + y * unit * scale }
}
export const cubeEdges = [[0,1],[0,2],[0,4],[1,3],[1,5],[2,3],[2,6],[3,7],[4,5],[4,6],[5,7],[6,7]] as const
export function cubeVertices(center: Point3, size: number): Point3[] {
  return Array.from({ length: 8 }, (_, i) => ({ x: center.x + (i & 1 ? 1 : -1) * size,
    y: center.y + (i & 2 ? 1 : -1) * size, z: center.z + (i & 4 ? 1 : -1) * size }))
}
