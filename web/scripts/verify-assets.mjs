import { readFile, readdir, stat } from 'node:fs/promises'
import { createHash } from 'node:crypto'
import { gzipSync, brotliCompressSync } from 'node:zlib'
import assert from 'node:assert/strict'
const checkpoint = JSON.parse(await readFile('../assets/evidence/v3/STARTING_CHECKPOINT.json', 'utf8'))
for (const [path, expected] of Object.entries(checkpoint.frozen_files)) {
  assert.equal(createHash('sha256').update(await readFile(`../${path}`)).digest('hex'), expected, `Immutable boundary changed: ${path}`)
}
const prompt = await readFile('../docs/FRONTEND_SPEC_V3.md')
assert.equal(createHash('sha256').update(prompt).digest('hex'), checkpoint.prompt_sha256)
assert.equal(createHash('sha256').update(prompt.subarray(0, checkpoint.preserved_prefix_bytes)).digest('hex'), checkpoint.prefix_sha256)
const followUp = await readFile('../docs/FRONTEND_MOTION_SPEC_V3_1.md')
assert.equal(createHash('sha256').update(followUp).digest('hex'), 'b23416c8085f8a9b97f78343f73f13dd06bdc211d43072194e1adbb876f4ded6', 'Motion specification must remain verbatim')
const fonts = JSON.parse(await readFile('../assets/evidence/v3/FONT_LEDGER.json', 'utf8'))
for (const font of fonts) {
  const bytes = await readFile(`../${font.file}`)
  assert.equal(bytes.length, font.bytes); assert.equal(createHash('sha256').update(bytes).digest('hex'), font.sha256)
}
assert(fonts[0].axes.some(a => a.tag === 'wdth' && a.min <= 100 && a.max >= 112))
assert(fonts.reduce((sum, f) => sum + f.bytes, 0) <= 200 * 1024)
const files = await readdir('dist/assets')
const sizes = []
for (const file of files) {
  const raw = await readFile(`dist/assets/${file}`)
  sizes.push({ file, bytes: raw.length, gzip: gzipSync(raw).length, brotli: brotliCompressSync(raw).length })
}
assert(sizes.filter(f => f.file.endsWith('.js') && !f.file.startsWith('analysis.worker')).reduce((n, f) => n + f.gzip, 0) < 250 * 1024)
assert(sizes.filter(f => f.file.endsWith('.css')).reduce((n, f) => n + f.gzip, 0) < 50 * 1024)
assert.equal((await stat('dist/model/model-14927b442dc9f33a.json')).size, 2369544)
console.log(JSON.stringify({ immutable_boundaries: 'PASS', prompt_preserved: 'PASS', fonts_bytes: fonts.reduce((n, f) => n + f.bytes, 0), build: sizes }, null, 2))
