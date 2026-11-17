import { execFileSync } from 'node:child_process'
import { mkdirSync, writeFileSync } from 'node:fs'
import assert from 'node:assert/strict'
mkdirSync('unit-results', { recursive: true })
function audit(extra, output) {
  let raw
  const args = ['audit', '--json', '--fetch-timeout=45000', '--fetch-retries=0', '--registry=https://registry.npmjs.org', ...extra]
  try { raw = execFileSync('npm', args, { encoding: 'utf8', timeout: 60000, maxBuffer: 8 * 1024 * 1024, stdio: ['ignore', 'pipe', 'inherit'] }) }
  catch (error) { if (!error.stdout) throw error; raw = String(error.stdout) }
  const report = JSON.parse(raw)
  writeFileSync(`unit-results/${output}`, JSON.stringify(report, null, 2) + '\n')
  if (report.error || !report.metadata?.vulnerabilities) throw new Error('Audit could not complete; network/tool failure is not a pass')
  return report
}
const full = audit([], 'npm-audit.json'), production = audit(['--omit=dev'], 'npm-audit-production.json')
assert.equal(production.metadata.vulnerabilities.total, 0, 'Production dependency advisory requires remediation')
const findings = Object.values(full.vulnerabilities ?? {})
const direct = findings.flatMap(finding => finding.via.filter(item => typeof item === 'object'))
assert(direct.every(advisory => advisory.name === 'picomatch' && advisory.url === 'https://github.com/advisories/GHSA-fc5q-39xc-vjpf' && advisory.severity === 'high'), 'New or different advisory; fail closed')
assert(findings.every(finding => finding.nodes.every(node => !node.includes('/react/'))), 'Unexpected production exposure')
const summary = {
  production_vulnerabilities: production.metadata.vulnerabilities,
  full_audit_vulnerabilities: full.metadata.vulnerabilities,
  unresolved: direct.map(({ name, url, range, severity }) => ({ name, url, range, severity })),
  disposition: findings.length ? 'KNOWN UNPATCHED DEVELOPMENT-TOOL ISSUE — retained, not a zero-vulnerability claim' : 'No reported advisories',
  exposure: 'Picomatch 4.0.7 is used by pinned Vite/Vitest/globbing tooling, not by the delivered browser or worker. Builds/tests use repository-owned patterns; message input is never a glob. Production serves precompiled static files and only React/React DOM are production npm dependencies.',
  remediation: 'Upstream latest 4.0.7 has no published fixed version as checked 17 November 2026. Upgrade when a compatible official fix is available; no force downgrade, hidden advisory, registry failure or unreviewed patch is accepted.',
}
writeFileSync('unit-results/dependency-review.json', JSON.stringify(summary, null, 2) + '\n')
console.log(JSON.stringify(summary, null, 2))
