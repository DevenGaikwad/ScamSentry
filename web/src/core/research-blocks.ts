/** Research-only schema 2. Not imported by the production worker. */
import { normalize,minimize } from './engine'
export interface Block {kind:'char'|'word';vocabulary:string[];idf:number[];weight:number;ngram_range:number[]}
export interface BlockBundle {schema_version:number;model_id:string;classes:string[];normalizer:string;privacy_transform:string;final_l2:boolean;blocks:Block[];coefficients:number[][];intercept:number[];temperature:number}
export class ResearchBlocks {
 private maps:Map<string,number>[]
 constructor(readonly bundle:BlockBundle){
  if(bundle.schema_version!==2||bundle.final_l2||bundle.normalizer!=='nfkc_ascii_ws_v1'||bundle.privacy_transform!=='typed_tokens_v1'||bundle.blocks.length<1||bundle.blocks.length>2)throw new Error('Invalid research contract')
  this.maps=bundle.blocks.map(b=>new Map(b.vocabulary.map((v,i)=>[v,i])))
 }
 predict(text:string){
  const normalized=normalize(minimize(text)),weights=new Map<number,number>(),norms:number[]=[];let offset=0
  this.bundle.blocks.forEach((b,bi)=>{
   const cps=Array.from(normalized),tokens=(normalized.match(/[a-z]+(?:'[a-z]+)?/g)??[]).filter(w=>w.length>=2),features:string[]=[]
   for(let n=b.ngram_range[0];n<=b.ngram_range[1];n++){
    const values=b.kind==='char'?cps:tokens
    for(let i=0;i<=values.length-n;i++)features.push(values.slice(i,i+n).join(b.kind==='char'?'':' '))
   }
   const counts=new Map<number,number>();for(const f of features){const i=this.maps[bi].get(f);if(i!==undefined)counts.set(i,(counts.get(i)??0)+1)}
   const raw=new Map<number,number>();let square=0
   for(const [i,c] of counts){const v=(1+Math.log(c))*b.idf[i];raw.set(i,v);square+=v*v}
   const norm=Math.sqrt(square);norms.push(norm)
   if(norm)for(const [i,v] of raw)weights.set(offset+i,v/norm*b.weight)
   offset+=b.vocabulary.length
  })
  const logits=this.bundle.coefficients.map((row,k)=>{let score=0;for(const [i,v] of weights)score+=row[i]*v;return this.bundle.intercept[k]+score}),max=Math.max(...logits),exps=logits.map(x=>Math.exp((x-max)/this.bundle.temperature)),total=exps.reduce((a,b)=>a+b,0)
  return {normalized,logits,probabilities:exps.map(x=>x/total),active_count:weights.size,norm:Math.sqrt(Array.from(weights.values()).reduce((a,b)=>a+b*b,0)),block_norms:norms}
 }
}
