/// <reference lib="webworker" />
import manifest from './release-manifest.json'
import { LinearModel,analyze } from './core/engine'
let cached:Promise<LinearModel>|null=null
async function load():Promise<LinearModel>{
  const response=await fetch(manifest.asset_path,{credentials:'omit',cache:'force-cache'})
  if(!response.ok)throw new Error('Model download failed')
  const raw=await response.arrayBuffer()
  if(raw.byteLength!==manifest.byte_size)throw new Error('Model size mismatch')
  const hash=Array.from(new Uint8Array(await crypto.subtle.digest('SHA-256',raw)),b=>b.toString(16).padStart(2,'0')).join('')
  if(hash!==manifest.model_sha256)throw new Error('Model integrity mismatch')
  const model=new LinearModel(JSON.parse(new TextDecoder('utf-8',{fatal:true}).decode(raw)))
  if(model.bundle.model_id!==manifest.model_id)throw new Error('Model identity mismatch')
  return model
}
self.onmessage=async(event:MessageEvent<{id:number;text:string;source:string}>)=>{
  const {id,text,source}=event.data
  self.postMessage({id,type:'status',stage:'loading'})
  const start=performance.now()
  try{
    if(!cached)cached=load()
    let model:LinearModel|null=null
    try{model=await cached}catch{cached=null}
    self.postMessage({id,type:'status',stage:'processing'})
    const scoreStart=performance.now(),result=analyze(text,model,source)
    self.postMessage({id,type:'result',result,timings:{total_ms:performance.now()-start,analysis_ms:performance.now()-scoreStart},model_error:!model})
  }catch{self.postMessage({id,type:'error',message:'Analysis could not finish. Review the text and try again.'})}
}
