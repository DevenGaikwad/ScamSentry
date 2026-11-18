export type FlowKind = 'hero' | 'privacy' | 'evidence'
export type FlowNode = { id:string; x:number; y:number; w:number; h:number; lines:string[] }
export type FlowEdge = { id:string; from:string; to:string; points:[number,number][] }
export type FlowGraph = { nodes:FlowNode[]; edges:FlowEdge[]; boundary?:{x:number;y:number;w:number;h:number}; label:string; caption:string }
const node=(id:string,x:number,y:number,w:number,lines:string[]):FlowNode=>({id,x,y,w,h:48,lines})
export function flowGraph(kind:FlowKind):FlowGraph {
  if(kind==='privacy')return {
    nodes:[node('assets',4,42,130,['PUBLIC APP','+ MODEL']),node('app',210,42,128,['THIS BROWSER']),node('input',210,190,128,['YOUR INPUT']),node('worker',410,116,128,['LOCAL WORKER']),node('review',578,116,136,['REVIEW RESULT']),node('download',578,258,136,['DOWNLOAD','ON YOUR CHOICE'])],
    edges:[{id:'public-download',from:'assets',to:'app',points:[[134,66],[210,66]]},{id:'asset-worker',from:'app',to:'worker',points:[[338,66],[374,66],[374,140],[410,140]]},{id:'input-worker',from:'input',to:'worker',points:[[338,214],[374,214],[374,140],[410,140]]},{id:'local-result',from:'worker',to:'review',points:[[538,140],[578,140]]},{id:'explicit-export',from:'review',to:'download',points:[[646,164],[646,258]]}],
    boundary:{x:178,y:14,w:542,h:232},label:'Public assets download to this browser. Your input stays in a local worker. A redacted file leaves only when you review and download it.',
    caption:'Public assets arrive first. Input and results stay in browser memory. The final arrow represents an explicit reviewed, redacted download—not an upload.' }
  if(kind==='evidence')return {
    nodes:[node('groups',4,128,128,['GROUPED','TRAINING']),node('calibration',198,128,132,['SEPARATE','CALIBRATION']),node('historical',400,128,132,['HISTORICAL','TEST']),node('read',590,128,126,['READ COUNTS','+ LIMITS'])],
    edges:[{id:'fit-calibrate',from:'groups',to:'calibration',points:[[132,152],[198,152]]},{id:'frozen-evaluate',from:'calibration',to:'historical',points:[[330,152],[400,152]]},{id:'report',from:'historical',to:'read',points:[[532,152],[590,152]]}],
    label:'Conceptual research method: grouped training, separate calibration, a historical test, then counts and limitations.',
    caption:'A conceptual view of the preserved research protocol. Animation is illustrative; no training, rescoring or live measurement is running.' }
  return {
    nodes:[node('input',4,128,126,['YOUR INPUT']),node('inspect',290,128,140,['LOCAL','INSPECTION']),node('observations',582,34,134,['OBSERVATIONS']),node('estimate',582,128,134,['TEXT ESTIMATE']),node('limits',582,222,134,['SCOPE + LIMITS'])],
    edges:[{id:'input-inspection',from:'input',to:'inspect',points:[[130,152],[202,152],[250,152],[290,152]]},{id:'observation-branch',from:'inspect',to:'observations',points:[[430,152],[484,152],[536,58],[582,58]]},{id:'estimate-branch',from:'inspect',to:'estimate',points:[[430,152],[582,152]]},{id:'limits-branch',from:'inspect',to:'limits',points:[[430,152],[484,152],[536,246],[582,246]]}],
    boundary:{x:270,y:96,w:180,h:112},label:'Conceptual signal flow: a message is inspected locally, then you independently verify the request.',
    caption:'Conceptual inspection, followed by your independent judgment. The moving squares are illustrations—not live traffic or inference progress.' }
}
export const edgePath=(edge:FlowEdge)=>edge.points.map(([x,y],i)=>`${i?'L':'M'}${x} ${y}`).join(' ')
export function pointOnPolyline(points:[number,number][], fraction:number):[number,number] {
  const lengths=points.slice(1).map(([x,y],i)=>Math.hypot(x-points[i][0],y-points[i][1]))
  let remaining=Math.min(1,Math.max(0,fraction))*lengths.reduce((a,b)=>a+b,0)
  for(let i=0;i<lengths.length;i++) {
    if(remaining<=lengths[i]||i===lengths.length-1){const t=lengths[i]?remaining/lengths[i]:0;return[points[i][0]+(points[i+1][0]-points[i][0])*t,points[i][1]+(points[i+1][1]-points[i][1])*t]}
    remaining-=lengths[i]
  }
  return points[0]
}
