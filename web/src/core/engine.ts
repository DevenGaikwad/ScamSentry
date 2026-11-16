import contract from './contracts.generated.json'

export interface Observation { code:string; source:string; severity:string; title:string; detail:string }
export interface Address { original:string; host:string|null; codes:string[]; valid:boolean }
export interface Estimate { normalized:string; logits:number[]; probabilities:number[]; active_count:number; norm:number }
export type Risk = 'higher'|'checking'|'lower'|'unable'
export interface Analysis {
  schema_version:number; policy_version:string; observation_version:string; url_version:string; normalizer_version:string; model_id:string;
  risk:Risk; estimate:Estimate|null; observations:Observation[]; urls:Address[]; quality_flags:string[]; limitations:string[]; standalone_url:boolean; links_truncated:boolean
}
export interface Bundle {
  schema_version:number; model_id:string; classes:string[]; normalizer:string; privacy_transform:string; ngram_range:number[];sublinear_tf:boolean;l2_normalize:boolean;
  vocabulary:string[];idf:number[];coefficients:number[][];intercept:number[];temperature:number;policy:{version:string;confidence:number;margin:number}
}
const whitespace=new Set([9,10,11,12,13,32,133,160,5760,8192,8193,8194,8195,8196,8197,8198,8199,8200,8201,8202,8232,8233,8239,8287,12288])
export function normalize(text:string):string {
  for(let i=0;i<text.length;i++){
    const n=text.charCodeAt(i)
    if(n>=0xd800&&n<=0xdbff){const next=text.charCodeAt(++i);if(!(next>=0xdc00&&next<=0xdfff))throw new Error('Invalid Unicode')}
    else if(n>=0xdc00&&n<=0xdfff)throw new Error('Invalid Unicode')
  }
  return Array.from(text.normalize('NFKC'),c=>{const n=c.codePointAt(0)!;return whitespace.has(n)?' ':n>=65&&n<=90?String.fromCodePoint(n+32):c}).join('').replace(/ +/g,' ').replace(/^ +| +$/g,'')
}
// Python Unicode word boundaries differ from JavaScript's ASCII \b.
const word='[\\p{L}\\p{N}_]'
const boundary=`(?:(?<=${word})(?!${word})|(?<!${word})(?=${word}))`
export function minimize(text:string):string {
  return text
    .replace(new RegExp(`${boundary}[\\p{L}\\p{N}_.+-]+@[\\p{L}\\p{N}_.-]+\\.[a-z]{2,}${boundary}`,'giu'),' emailtoken ')
    .replace(new RegExp(`${boundary}(?:\\+?\\p{Nd}[\\p{Nd} ()-]{6,}\\p{Nd})${boundary}`,'gu'),' numbertoken ')
    .replace(/https?:\/\/[^\s<>]+|www\.[^\s<>]+/giu,' urltoken ')
    .replace(/\p{Nd}+/gu,' numbertoken ')
}
function validNumber(x:unknown):x is number {return typeof x==='number'&&Number.isFinite(x)}
export function validateBundle(value:unknown):Bundle {
  if(!value||typeof value!=='object')throw new Error('Invalid model')
  const b=value as Bundle
  if(b.schema_version!==1||b.normalizer!=='nfkc_ascii_ws_v1'||b.privacy_transform!=='typed_tokens_v1'||JSON.stringify(b.classes)!=='["ordinary","promotional-or-spam","smishing-like"]'||JSON.stringify(b.ngram_range)!=='[3,5]'||b.sublinear_tf!==true||b.l2_normalize!==true)throw new Error('Incompatible model contract')
  if(typeof b.model_id!=='string'||!b.model_id||!Array.isArray(b.vocabulary)||b.vocabulary.length<1||b.vocabulary.length>60000||new Set(b.vocabulary).size!==b.vocabulary.length||b.vocabulary.some(t=>typeof t!=='string'||Array.from(t).length<3||Array.from(t).length>5))throw new Error('Invalid vocabulary')
  const n=b.vocabulary.length
  if(!Array.isArray(b.idf)||b.idf.length!==n||b.idf.some(v=>!validNumber(v)||v<=0)||!Array.isArray(b.intercept)||b.intercept.length!==3||b.intercept.some(v=>!validNumber(v))||!Array.isArray(b.coefficients)||b.coefficients.length!==3||b.coefficients.some(row=>!Array.isArray(row)||row.length!==n||row.some(v=>!validNumber(v)))||!validNumber(b.temperature)||b.temperature<=0)throw new Error('Invalid parameters')
  if(!b.policy||b.policy.version!=='ordinal_v1'||![b.policy.confidence,b.policy.margin].every(v=>validNumber(v)&&v>=0&&v<=1))throw new Error('Invalid model policy')
  return b
}
export class LinearModel {
  readonly bundle:Bundle
  private vocabulary:Map<string,number>
  constructor(value:unknown){this.bundle=validateBundle(value);this.vocabulary=new Map(this.bundle.vocabulary.map((v,i)=>[v,i]))}
  predict(text:string):Estimate {
    if(Array.from(text).length>4000)throw new Error('Input too long')
    const normalized=normalize(minimize(text)),cps=Array.from(normalized),counts=new Map<number,number>()
    for(let n=3;n<=5;n++)for(let i=0;i<=cps.length-n;i++){const k=this.vocabulary.get(cps.slice(i,i+n).join(''));if(k!==undefined)counts.set(k,(counts.get(k)??0)+1)}
    const weights=new Map<number,number>();let square=0
    for(const [i,c] of counts){const w=(1+Math.log(c))*this.bundle.idf[i];weights.set(i,w);square+=w*w}
    const norm=Math.sqrt(square);if(norm)for(const [i,w] of weights)weights.set(i,w/norm)
    const logits=this.bundle.coefficients.map((row,k)=>{let sum=0;for(const [i,v] of weights)sum+=row[i]*v;return this.bundle.intercept[k]+sum})
    const max=Math.max(...logits),exps=logits.map(x=>Math.exp((x-max)/this.bundle.temperature)),sum=exps.reduce((a,b)=>a+b,0)
    return {normalized,logits,probabilities:exps.map(x=>x/sum),active_count:weights.size,norm}
  }
}
export function activeSecretRequest(text:string):boolean {
  const quotes=Array.from(text.matchAll(/["“]([^"”]{1,180})["”]/g))
  const request=/(?=\b(share|send|tell|reveal|reply\s+with|provide|give)\b([^.!?;:]{0,55}?)\b(otp|one[ -]time (?:code|password)|pin|password|verification code)\b)/g
  const negation=/\b(?:never|do not|don't|don’t|must not|should not|cannot|can't|can’t|not to)\s+(?:(?:ever|again|please|really|just|even|you|under any circumstances)\s+){0,3}$/
  const exception=/\b(?:except|unless|but|however|instead|actually)\b/
  const before=/(?:educational(?: example)?|quoted example|scam awareness|(?:a )?scam (?:may )?says?|attackers? (?:may )?(?:ask|say))\s*[:,-]?\s*$/
  const after=/^\s*(?:is|are)\s+(?:a |an )?(?:typical |common )?(?:scam|example|quoted|educational)/
  for(const m of text.matchAll(request)){
    const start=m.index,end=start+m[1].length+m[2].length+m[3].length
    const clause=Math.max(...['.','!','?',';'].map(c=>text.lastIndexOf(c,start-1)))+1
    const prefix=text.slice(Math.max(clause,start-80),start).replace(/,/g,' ').replace(/\s+/g,' ')
    if(negation.test(prefix)&&!exception.test(m[2]))continue
    const educational=quotes.some(q=>q.index+1<=start&&end<=q.index+1+q[1].length&&(before.test(text.slice(Math.max(0,q.index-90),q.index))||after.test(text.slice(q.index+q[0].length,q.index+q[0].length+80))))
    if(!educational)return true
  }
  return false
}
export function observations(text:string):Observation[]{
  const value=normalize(text)
  const obs:Observation[]=contract.rules.filter(r=>r.code==='secret_request'?activeSecretRequest(value):new RegExp(r.pattern,'s').test(value)).map(({code,severity,title,detail})=>({code,source:'rule',severity,title,detail}))
  if(Array.from(text).some(c=>'\u200b\u200c\u200d\ufeff'.includes(c)))obs.push({code:'invisible_char',source:'rule',severity:'caution',title:'Invisible characters',detail:'Hidden formatting can change text and links. Review the original carefully.'})
  return obs
}
function ipv6(host:string):boolean{
  if(!host.includes(':')||host.includes(':::'))return false
  const compressed=host.includes('::');if(!compressed&&(host.startsWith(':')||host.endsWith(':')))return false;if(compressed&&host.indexOf('::')!==host.lastIndexOf('::'))return false
  const parts=host.split(':').filter(Boolean)
  return parts.every(p=>/^[0-9a-f]{1,4}$/i.test(p))&&(compressed?parts.length<8:parts.length===8)
}
export function inspectOne(original:string):Address {
  try {
    const candidate=original.includes('://')?original:'https://'+original
    const split=candidate.indexOf('://'),scheme=candidate.slice(0,split).toLowerCase()
    if(!['http','https'].includes(scheme)||Array.from(candidate).some(c=>c.codePointAt(0)!<33||'\\\u200b\u200c\u200d\ufeff'.includes(c)))throw new Error('Syntax')
    const authority=candidate.slice(split+3).split(/[/#?]/,1)[0]
    if((authority.match(/@/g)??[]).length>1)throw new Error('Authority')
    const hostport=authority.split('@').at(-1)!;let host:string,port:string|undefined
    if(hostport.startsWith('[')){
      const match=hostport.match(/^\[([0-9a-f:]+)\](?::([0-9]+))?$/i)
      if(!match||!ipv6(match[1]))throw new Error('IPv6')
      host=match[1].toLowerCase();port=match[2]
    }else{
      const parts=hostport.split(':');if(parts.length>2)throw new Error('Port')
      port=parts[1];if(port!==undefined&&!/^[0-9]+$/.test(port))throw new Error('Port')
      const raw=parts[0].replace(/\.+$/,'');if(/[%ßςẞ]/.test(raw))throw new Error('IDNA')
      host=/^[\x21-\x7e]+$/.test(raw)?raw.toLowerCase():new URL('https://'+raw).hostname.toLowerCase()
      if(!host||host.length>253||host.split('.').some(l=>!/^[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?$/.test(l)))throw new Error('Host')
      if(/^[0-9.]+$/.test(raw)){
        const octets=raw.split('.');if(octets.length!==4||octets.some(x=>!/^\d+$/.test(x)||Number(x)>255||(x!=='0'&&x.startsWith('0'))))throw new Error('IPv4')
        if(host!==raw)throw new Error('Ambiguous IPv4')
      }else if(/^(?:[0-9]+|0x[0-9a-f]+)$/.test(host.split('.').at(-1)!))throw new Error('Ambiguous numeric host')
    }
    if(port!==undefined&&(!Number.isSafeInteger(Number(port))||Number(port)<1||Number(port)>65535))throw new Error('Port')
    const codes:string[]=[]
    if(authority.includes('@'))codes.push('userinfo')
    if(host.includes('xn--'))codes.push('idn')
    if(scheme==='http')codes.push('unencrypted_scheme')
    if(['bit.ly','tinyurl.com','t.co'].includes(host))codes.push('shortener')
    if(Array.from(original).length>180)codes.push('long_address')
    return {original,host,codes,valid:true}
  }catch{return {original,host:null,codes:['unsupported_or_malformed'],valid:false}}
}
export function extractUrls(text:string):{addresses:Address[];truncated:boolean}{
  const pattern=/(?<![\p{L}\p{N}_])(?:https?:\/\/|[a-z][a-z0-9+.-]*:\/\/|www\.)[^\s<>"\u200b]+|(?<![@\p{L}\p{N}_])(?:[a-z0-9-]+\.)+(?:[a-z]{2,63}|xn--[a-z0-9-]{2,59})\.?(?![\p{L}\p{N}_.-])(?:[/?:#][^\s<>"]*)?/giu
  const addresses:Address[]=[]
  for(const m of text.matchAll(pattern)){if(addresses.length===40)return {addresses,truncated:true};let original=m[0].replace(/[.,;!)}]+$/g,'');while(original.endsWith(']')&&(original.match(/\]/g)??[]).length>(original.match(/\[/g)??[]).length)original=original.slice(0,-1);addresses.push(inspectOne(original))}
  return {addresses,truncated:false}
}
function supported(text:string):boolean{const letters=Array.from(normalize(text)).filter(c=>/\p{L}/u.test(c));return letters.length>=8&&letters.every(c=>c>='a'&&c<='z')}
export function analyze(text:string,model:LinearModel|null,source='text'):Analysis {
  const meta={schema_version:2,policy_version:'ordinal_v2',observation_version:'local_context_v2',url_version:'lexical_v2',normalizer_version:'nfkc_ascii_ws_v1',model_id:model?.bundle.model_id??'unavailable'}
  const result=(risk:Risk,estimate:Estimate|null,obs:Observation[],urls:Address[],flags:string[],standalone=false,truncated=false):Analysis=>({...meta,risk,estimate,observations:obs,urls,quality_flags:flags,limitations:flags.map(k=>contract.limitations[k as keyof typeof contract.limitations]),standalone_url:standalone,links_truncated:truncated})
  let value:string
  try{if(!text.trim()||Array.from(text).length>4000||Array.from(text).some(c=>c.codePointAt(0)!<32&&!'\t\n\r'.includes(c)))throw new Error('Input');value=normalize(text)}catch{return result('unable',null,[],[],['invalid_input','research_scope'])}
  const obs=observations(text),{addresses:urls,truncated}=extractUrls(text),standalone=urls.length===1&&text.trim()===urls[0].original,scope=supported(text)
  const estimate=model&&scope&&!standalone?model.predict(text):null,flags:string[]=[]
  if(!model&&!standalone)flags.push('model_unavailable')
  if(!scope&&!standalone)flags.push('unsupported_scope')
  if(source==='ocr')flags.push(Array.from(text.trim()).length<20?'short_ocr':'ocr_review')
  if(estimate&&estimate.active_count<4)flags.push('weak_features')
  if(Array.from(value).length>30&&new Set(Array.from(value.replaceAll(' ',''))).size<4)flags.push('repetitive_input')
  if(standalone)flags.push('standalone_url')
  if(truncated)flags.push('link_limit')
  flags.push('research_scope');if(urls.length)flags.push('lexical_only')
  let risk:Risk
  if(obs.some(o=>o.code==='secret_request'))risk='higher'
  else if(standalone)risk=urls[0].valid?'checking':'unable'
  else if(flags.some(f=>['model_unavailable','unsupported_scope','short_ocr','weak_features','repetitive_input','link_limit'].includes(f)))risk='unable'
  else {
    const p=estimate!.probabilities,sorted=[...p].sort((a,b)=>b-a),policy=model!.bundle.policy,label=p.indexOf(Math.max(...p))
    if(urls.some(u=>u.codes.length)||sorted[0]<policy.confidence||sorted[0]-sorted[1]<policy.margin)risk='checking'
    else if(label===2)risk='higher'
    else if(label===1||obs.some(o=>o.severity==='caution'))risk='checking'
    else risk='lower'
  }
  return result(risk,estimate,obs,urls,flags,standalone,truncated)
}
