export function damping(deltaMs: number, tauMs: number) {
  return 1 - Math.exp(-Math.min(40, Math.max(0, deltaMs)) / tauMs)
}

type Point = readonly [x: number, y: number]
type Module = { id: string; label: string; x: number; y: number; width: number; height: number }
type Connector = { id: string; from: string; to: string; points: Point[] }

function module(id: string, label: string, x: number, y: number, width: number, height = 30): Module {
  return { id, label, x, y, width, height }
}

function connect(from: Module, to: Module, lane: number): Connector {
  const start: Point = [from.x + from.width, from.y + from.height / 2]
  const end: Point = [to.x, to.y + to.height / 2]
  return {
    id: from.id + '-' + to.id, from: from.id, to: to.id,
    points: start[1] === end[1] ? [start, [lane, start[1]], end] : [start, [lane, start[1]], [lane, end[1]], end],
  }
}

// This public diagram illustrates local processing; its geometry has no session inputs.
const message = module('message', 'MESSAGE', 35, 12, 104)
const link = module('link', 'ADDRESS', 35, 327, 104)
const prepare = module('prepare', 'PREPARE', 259, 327, 108)
const model = module('model', 'LOCAL MODEL', 516, 12, 126)
const signals = module('signals', 'OBSERVATIONS', 516, 327, 126)
const review = module('review', 'REVIEW', 812, 327, 108)
const guidance = module('guidance', 'NEXT STEP', 1042, 12, 123)
const compactInput = module('input', 'INPUT', 8, 12, 76, 26)
const compactCheck = module('check', 'LOCAL CHECK', 129, 32, 102, 26)
const compactReview = module('review', 'REVIEW', 276, 12, 76, 26)

export const heroPipelines = {
  wide: {
    width: 1200, height: 374,
    nodes: [message, link, prepare, model, signals, review, guidance],
    edges: [
      connect(message, prepare, 196), connect(link, prepare, 196),
      connect(prepare, model, 432), connect(prepare, signals, 432),
      connect(model, review, 735), connect(signals, review, 735),
      connect(review, guidance, 986),
    ],
    markers: ['message-prepare', 'prepare-model', 'model-review', 'review-guidance'],
    junctions: [[196, 342], [432, 342], [735, 342]],
  },
  compact: {
    width: 360, height: 70,
    nodes: [compactInput, compactCheck, compactReview],
    edges: [connect(compactInput, compactCheck, 106), connect(compactCheck, compactReview, 254)],
    markers: ['input-check', 'check-review'],
    junctions: [],
  },
}

export function uniqueSegments(edges: readonly Connector[]) {
  const segments = new Map<string, readonly [Point, Point]>()
  for (const { points } of edges) {
    for (let i = 1; i < points.length; i++) {
      const from = points[i - 1], to = points[i]
      segments.set([from.join(','), to.join(',')].sort().join('/'), [from, to])
    }
  }
  return [...segments.values()]
}

export function measureTrack(points: readonly Point[], scaleX: number, scaleY: number) {
  let length = 0
  const segments = points.slice(1).map((to, i) => {
    const from = points[i]
    const distance = Math.hypot((to[0] - from[0]) * scaleX, (to[1] - from[1]) * scaleY)
    const start = length
    length += distance
    return { from, to, distance, start }
  }).filter(segment => segment.distance > 0)
  return { segments, length }
}

export function positionOnTrack(track: ReturnType<typeof measureTrack>, distance: number): Point {
  const clamped = Math.max(0, Math.min(track.length, distance))
  for (const { from, to, distance: size, start } of track.segments) {
    if (clamped > start + size) continue
    const fraction = (clamped - start) / size
    return [from[0] + (to[0] - from[0]) * fraction, from[1] + (to[1] - from[1]) * fraction]
  }
  return track.segments.at(-1)?.to ?? [0, 0]
}
