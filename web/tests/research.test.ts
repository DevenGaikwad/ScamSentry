import { readFileSync } from 'node:fs'
import { expect,it } from 'vitest'
import { ResearchBlocks } from '../src/core/research-blocks'
const pack=JSON.parse(readFileSync(new URL('../../ml/artifacts/parity/research_blocks_synthetic_v2.json',import.meta.url),'utf8'))
it('real synthetic word and hybrid exports match all intermediate estimates',()=>{
 for(const item of pack.models){const model=new ResearchBlocks(item.bundle);for(const f of item.fixtures){const actual=model.predict(f.text);expect(actual.normalized).toBe(f.expected.normalized);expect(actual.active_count).toBe(f.expected.active_count);expect(actual.norm).toBeCloseTo(f.expected.norm,8);for(const key of ['logits','probabilities','block_norms'] as const)actual[key].forEach((x,i)=>expect(x).toBeCloseTo(f.expected[key][i],8))}}
})
