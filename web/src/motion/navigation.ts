import { articles } from '../site'

export type Motif = 'signal' | 'rails' | 'grid' | 'rings' | 'corridor'
export type Phase = 'departing' | 'scene' | 'arriving' | 'settling'
export type Transition = { id: number; from: string; to: string; motif: Motif; phase: Phase; duration: number }
const publicPaths = new Set(['/', '/learn', '/evidence', '/privacy', '/android', ...articles.map(a=>`/learn/${a.slug}`)])
export function isPublicPath(path: string) { return publicPaths.has(path) }
export function routeMotif(path: string): Motif {
  if(path==='/privacy')return 'rings'
  if(path==='/evidence')return 'grid'
  if(path==='/android')return 'corridor'
  if(path==='/learn'||path.startsWith('/learn/'))return 'rails'
  return 'signal'
}
export function richNavigation(from: string, to: string, running: boolean, forced: boolean, modal: boolean) {
  return from!==to&&running&&!forced&&!modal&&isPublicPath(from)&&isPublicPath(to)
}
export const arrivalKeyframes: Keyframe[] = [
  { offset:0, opacity:0, transform:'perspective(1100px) translate3d(30vw,8vh,-1500px) rotateY(-12deg)' },
  { offset:.36, opacity:0, transform:'perspective(1100px) translate3d(30vw,8vh,-1500px) rotateY(-12deg)' },
  { offset:.46, opacity:1, transform:'perspective(1100px) translate3d(27vw,4vh,-1300px) rotateY(-10deg)' },
  { offset:.64, opacity:1, transform:'perspective(1100px) translate3d(12vw,2vh,-650px) rotateY(-5deg)' },
  { offset:.94, opacity:1, transform:'perspective(1100px) translate3d(0,0,0) rotateY(0deg)' },
  { offset:1, opacity:1, transform:'none' },
]
