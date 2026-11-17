import { execFileSync, spawnSync } from 'node:child_process'
import { mkdtempSync, readFileSync } from 'node:fs'
import { tmpdir } from 'node:os'
import { join } from 'node:path'
import { serve } from './lab-server.mjs'
const checkpoint = JSON.parse(readFileSync('../assets/evidence/v3/STARTING_CHECKPOINT.json', 'utf8'))
const baseline = checkpoint.starting_commit
const repository = execFileSync('git', ['rev-parse', '--show-toplevel'], { encoding: 'utf8' }).trim()
try { execFileSync('git', ['cat-file', '-e', `${baseline}^{commit}`], { stdio: 'ignore' }) } catch { execFileSync('git', ['fetch', '--depth=1', 'origin', baseline], { stdio: 'inherit' }) }
const directory = mkdtempSync(join(tmpdir(), 'scamsentry-lab-baseline-'))
const archive = execFileSync('git', ['archive', baseline, 'web'], { cwd: repository, maxBuffer: 16 * 1024 * 1024 })
const extraction = spawnSync('tar', ['-x', '-C', directory], { input: archive, stdio: ['pipe', 'inherit', 'inherit'] })
if (extraction.status) throw new Error('Baseline extraction failed')
const cwd = join(directory, 'web')
execFileSync('npm', ['ci', '--prefer-offline', '--no-audit', '--no-fund'], { cwd, stdio: 'inherit' })
execFileSync('npm', ['run', 'build'], { cwd, stdio: 'inherit' })
serve(join(cwd, 'dist'), 4174)
