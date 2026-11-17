import { describe, expect, it, vi, afterEach } from 'vitest'
import { redactedSummary } from '../src/Analyze'
import { analyze } from '../src/core/engine'
import { creatorCredit } from '../src/site'
import { readPreference, preferenceKeys } from '../src/preferences'
describe('editorial presentation privacy boundaries', () => {
  afterEach(() => vi.unstubAllGlobals())
  it('redacted export omits all raw fields even when the engine contains them', () => {
    const raw = 'Send your OTP immediately. https://bank.example@review.test/private FICTIONAL_PRIVATE_CANARY email=person@example.test 9876543210'
    const result = analyze(raw, null)
    const summary = redactedSummary(result)
    expect(result.urls.length).toBeGreaterThan(0)
    for (const value of ['FICTIONAL_PRIVATE_CANARY', 'review.test', 'person@example.test', '9876543210', raw]) expect(summary).not.toContain(value)
    expect(summary).toContain('Request for a secret')
    expect(summary).toContain('Model: unavailable')
  })
  it('ignores unknown preference values without reading draft keys', () => {
    const getItem = vi.fn(() => 'private-draft'); vi.stubGlobal('localStorage', { getItem })
    expect(readPreference(preferenceKeys.theme, ['system', 'light', 'dark'], 'system')).toBe('system')
    expect(getItem).toHaveBeenCalledExactlyOnceWith('scamsentry.theme')
  })
  it('preference denial keeps functional system defaults', () => {
    vi.stubGlobal('localStorage', { getItem() { throw new Error('Denied') } })
    expect(readPreference(preferenceKeys.motion, ['auto', 'paused'], 'auto')).toBe('auto')
  })
  it('uses the exact full current creator credit', () => {
    expect(creatorCredit).toBe('Deven Sachin Gaikwad · B.Tech Electronics and Communication Engineering (AI and ML) · DES Pune University.')
  })
})
