import { describe,it,expect } from 'vitest'
import { readFileSync } from 'node:fs'
import { LinearModel,analyze,normalize,minimize,inspectOne } from '../src/core/engine'
import manifest from '../src/release-manifest.json'
const model=new LinearModel(JSON.parse(readFileSync(new URL('../public'+manifest.asset_path,import.meta.url),'utf8')))
const fixtures=JSON.parse(readFileSync(new URL('../../ml/artifacts/parity/full_golden_v2.json',import.meta.url),'utf8')).fixtures
function compare(expected:unknown,actual:unknown,path:string){
 if(typeof expected==='number'){expect(typeof actual,path).toBe('number');expect(Math.abs(expected-(actual as number)),path).toBeLessThanOrEqual(1e-6)}
 else if(Array.isArray(expected)){expect(Array.isArray(actual),path).toBe(true);expect((actual as unknown[]).length,path).toBe(expected.length);expected.forEach((v,i)=>compare(v,(actual as unknown[])[i],path+'/'+i))}
 else if(expected&&typeof expected==='object'){expect(Object.keys(actual as object).sort(),path).toEqual(Object.keys(expected).sort());for(const [k,v] of Object.entries(expected))compare(v,(actual as Record<string,unknown>)[k],path+'/'+k)}
 else expect(actual,path).toEqual(expected)
}
describe('complete Python–browser contract',()=>{
 for(const f of fixtures)it(f.id,()=>{expect(minimize(f.text),f.id+'/minimized').toBe(f.minimized);compare(f.expected,analyze(f.text,f.model_available?model:null,f.source),f.id)})
})
it('preserves Unicode features and identifier minimization',()=>{
 expect(normalize(' Ａ\t B  😀 ')).toBe('a b 😀')
 expect(()=>normalize('\ud800')).toThrow()
 expect(minimize('Email bücher@example.org and １２３４')).toBe('Email  emailtoken  and  numbertoken ')
})
it('rejects corrupt numeric and policy contracts',()=>{
 const b=JSON.parse(readFileSync(new URL('../public'+manifest.asset_path,import.meta.url),'utf8'))
 b.policy.confidence=-1;expect(()=>new LinearModel(b)).toThrow()
 b.policy.confidence=.62;b.coefficients[0][0]='1';expect(()=>new LinearModel(b)).toThrow()
})
it('handles case and malformed authority deterministically',()=>{
 expect(inspectOne('HTTP://Example.org')).toEqual({original:'HTTP://Example.org',host:'example.org',codes:['unencrypted_scheme'],valid:true})
 expect(inspectOne('https://a@b@review.test').valid).toBe(false)
})
