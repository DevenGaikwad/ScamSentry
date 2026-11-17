import { useEffect, useRef, useState } from 'react'
import type { Analysis } from './core/engine'
import type { AnalysisSession } from './useAnalysis'
import { examples, riskCopy } from './site'
import { usePreferences } from './preferences'
import { Arrow, Chapter, containDialogFocus, PageIntro } from './ui'
import { Link } from './router'

export function redactedSummary(result: Analysis): string {
  return `ScamSentry — redacted analysis summary\nResult: ${riskCopy[result.risk].title}\nModel: ${result.model_id}; policy: ${result.policy_version}\nObserved patterns: ${result.observations.map(o => o.title).join(', ') || 'No configured patterns'}\nLinks inspected: ${result.urls.length}; destinations omitted.\nOriginal text, links, identifiers and screenshots are omitted.\nThis result does not verify authenticity. Verify unexpected requests independently.\n`
}
function Report({ result, onClose }: { result: Analysis; onClose: () => void }) {
  const ref = useRef<HTMLDialogElement>(null)
  const objectUrl = useRef<string | null>(null)
  const summary = redactedSummary(result)
  useEffect(() => {
    const previous = document.activeElement as HTMLElement | null
    const dialog = ref.current
    const overflow = document.body.style.overflow
    document.body.style.overflow = 'hidden'; dialog?.showModal()
    return () => { dialog?.close(); document.body.style.overflow = overflow; previous?.focus(); if (objectUrl.current) URL.revokeObjectURL(objectUrl.current) }
  }, [])
  function download() {
    if (objectUrl.current) URL.revokeObjectURL(objectUrl.current)
    const url = URL.createObjectURL(new Blob([summary], { type: 'text/plain;charset=utf-8' }))
    objectUrl.current = url
    const anchor = document.createElement('a')
    anchor.href = url; anchor.download = 'ScamSentry_Redacted_Summary.txt'
    document.body.append(anchor); anchor.click(); anchor.remove()
    onClose()
  }
  return <dialog className="report-dialog" ref={ref} aria-labelledby="report-title" onKeyDown={containDialogFocus} onCancel={event => { event.preventDefault(); onClose() }}>
    <div className="dialog-header"><span className="eyebrow">YOU CONTROL WHAT LEAVES</span><button className="icon-button" onClick={onClose} aria-label="Close report preview" autoFocus>×</button></div>
    <h2 id="report-title">Review before downloading.</h2><p>This is the exact text the file will contain. Original text and destinations are omitted.</p><pre data-testid="report-preview">{summary}</pre>
    <div className="button-row"><button className="primary" onClick={download}>Download summary <Arrow down /></button><button className="secondary" onClick={onClose}>Cancel</button></div>
  </dialog>
}
const addressNotes: Record<string, string> = {
  userinfo: 'Text before @ is not the destination. The actual host is shown above.',
  idn: 'Internationalized host. Encoded text is shown for inspection; this alone does not indicate fraud.',
  unencrypted_scheme: 'This address uses HTTP. HTTPS alone would not establish authenticity either.',
  shortener: 'Shortened address. The final destination cannot be followed offline.',
  long_address: 'Long address. Length alone does not establish risk.',
  unsupported_or_malformed: 'Malformed address or unsupported scheme. Verify it independently.',
}
function Result({ session, onEdit }: { session: AnalysisSession; onEdit: () => void }) {
  const result = session.result!
  const copy = riskCopy[result.risk]
  const heading = useRef<HTMLHeadingElement>(null)
  const [report, setReport] = useState(false)
  useEffect(() => {
    if (session.focusPending.current) {
      session.focusPending.current = false
      heading.current?.focus({ preventScroll: true })
      heading.current?.closest('section')?.scrollIntoView({ block: 'start', behavior: 'instant' })
    }
  }, [result, session.focusPending])
  return <div className="result-stack">
    <section className={`risk-result risk-card ${result.risk}`} aria-labelledby="result-title">
      <div className="result-top"><span className="eyebrow">YOUR SECOND LOOK</span><span className="risk-symbol" aria-hidden="true">{result.risk === 'lower' ? <Arrow down /> : copy.symbol}</span></div>
      <h2 ref={heading} tabIndex={-1} id="result-title">{copy.title}</h2><p>{copy.description}</p>
      <p className="result-counts"><span>{result.observations.length} observed {result.observations.length === 1 ? 'pattern' : 'patterns'}</span><span>{result.urls.length} {result.urls.length === 1 ? 'link' : 'links'} inspected</span></p>
    </section>
    <section className="result-section" aria-labelledby="observations-title"><span className="eyebrow">01 / OBSERVATIONS</span><h3 id="observations-title">What we observed.</h3>
      {result.observations.length ? result.observations.map(observation => <div className={`observation ${observation.severity}`} key={observation.code}><span className="observation-marker" aria-hidden="true">{observation.severity === 'high' ? '!' : '·'}</span><div><h4>{observation.title}</h4><p>{observation.detail}</p></div></div>) : <div className="observation"><p>No configured message patterns found. A missing pattern does not establish legitimacy.</p></div>}
    </section>
    <section className="result-section" aria-labelledby="destinations-title"><span className="eyebrow">02 / LINK STRUCTURE</span><h3 id="destinations-title">Look at the destination.</h3><p className="muted">Addresses are text. No link is opened, no redirect followed and no live reputation checked.</p>
      {result.urls.length === 0 ? <p>No link was extracted from this input.</p> : result.urls.map((address, i) => <div className="url-box" key={i}><span className="eyebrow">LINK {i + 1} / ACTUAL HOST</span><strong className="host"><bdi dir="ltr">{address.host ?? 'Unable to parse reliably'}</bdi></strong><code dir="ltr">{address.original}</code>{address.codes.map(code => <p key={code}>{addressNotes[code]}</p>)}</div>)}
    </section>
    <section className="result-section" aria-labelledby="interpretation-title"><span className="eyebrow">03 / STATISTICAL RESEMBLANCE</span><h3 id="interpretation-title">Model interpretation.</h3>
      <p>{result.estimate ? `Text resemblance: ${['ordinary messages', 'promotion or spam', 'smishing-like messages'][result.estimate.probabilities.indexOf(Math.max(...result.estimate.probabilities))]}` : 'Message interpretation is limited. No class estimates are available for this analysis.'}</p><p className="muted">Separate from the observations above. Statistical resemblance cannot identify who sent a message.</p>
      <details><summary>Inspect technical details</summary><p className="mono">{result.model_id} · {result.policy_version}</p>{result.estimate && <><p>Historical class estimates, not probabilities of authenticity:</p><dl className="estimate-table">{['Ordinary', 'Promotion / spam', 'Smishing-like'].map((name, i) => <div key={name}><dt>{name}</dt><dd>{(100 * result.estimate!.probabilities[i]).toFixed(1)}%</dd></div>)}</dl><p>{result.estimate.active_count} active character features. Scalar-temperature calibration. Estimates may not transfer to new campaigns.</p></>}
        {session.timing && <p data-testid="worker-timing" data-analysis-ms={session.timing.analysis_ms} data-worker-total-ms={session.timing.total_ms}>This browser analysis: {session.timing.analysis_ms.toFixed(1)} ms, excluding download. Worker elapsed time including any model load: {session.timing.total_ms.toFixed(1)} ms. These are local browser measurements.</p>}
      </details>
    </section>
    <section className="result-section" aria-labelledby="limits-title"><span className="eyebrow">04 / SCOPE</span><h3 id="limits-title">Read the limits.</h3><details><summary>Scope and limitations ({result.limitations.length})</summary><ul>{result.limitations.map((limitation, i) => <li key={i}>{limitation}</li>)}</ul></details></section>
    <section className="result-section next-step"><span className="eyebrow">05 / YOUR NEXT STEP</span><h3>Pause. Verify independently.</h3><p>Use a phone number or app you already trust. Keep authentication secrets private and confirm unexpected payments.</p></section>
    <div className="button-row"><button className="primary" onClick={() => setReport(true)}>Redacted report <Arrow /></button><button className="secondary" onClick={onEdit}>Edit message <span aria-hidden="true">↑</span></button></div>
    {report && <Report key={session.text} result={result} onClose={() => setReport(false)} />}
  </div>
}
export function Analyze({ session }: { session: AnalysisSession }) {
  const editor = useRef<HTMLTextAreaElement>(null)
  const [selected, setSelected] = useState<string | null>(null)
  const { setInputActive } = usePreferences()
  const focusEditor = () => { editor.current?.focus(); editor.current?.scrollIntoView({ block: 'center', behavior: 'instant' }) }
  useEffect(() => () => setInputActive(false), [setInputActive])
  return <>
    <PageIntro number="01" label="INSPECT LOCALLY" title={<>Take a<br />second look.</>} lead="A message. A link. A moment to think before you act." />
    <div className="analyzer-layout">
      <div className="editor-stack"><form className="editor-panel" onSubmit={event => { event.preventDefault(); editor.current?.blur(); session.submit() }}>
        <div className="section-label"><span className="eyebrow">YOUR INPUT / KEPT IN MEMORY</span><span className="status-square" aria-hidden="true" /></div>
        <label htmlFor="message">Your message</label><p className="muted" id="input-guidance">Paste text or a link. English short-message research scope.</p>
        <textarea ref={editor} id="message" value={session.text} onChange={event => { setSelected(null); session.edit(event.target.value) }} onFocus={() => setInputActive(true)} onBlur={() => setInputActive(false)} placeholder="Paste a message or a link here…" aria-describedby="input-guidance input-count input-privacy" aria-invalid={session.count > 4000 ? true : undefined} spellCheck={false} autoComplete="off" autoCapitalize="off" />
        <div className="input-meta" id="input-count"><span>{session.count > 4000 ? 'Over the limit. Shorten the input to analyze.' : 'Up to 4,000 Unicode code points'}</span><span>{session.count.toLocaleString('en-US')} / 4,000</span></div>
        {session.error && <p className="error">{session.error}</p>}
        <div className="analyze-actions"><button className="primary" type="submit" disabled={!session.text.trim() || session.count > 4000 || session.busy}>Analyze message <Arrow /></button>{session.busy ? <button className="secondary" type="button" onClick={session.cancel}>Cancel</button> : <button className="secondary" type="button" onClick={() => { setSelected(null); session.edit(''); editor.current?.focus() }} disabled={!session.text}>Clear</button>}</div>
        <p className="status-text" role="status" aria-live="polite" aria-atomic="true">{session.busy ? session.stage === 'loading' ? 'Loading and verifying the public model… You can edit or cancel.' : 'Inspecting locally… You can edit or cancel.' : session.announcement}</p>
        <p id="input-privacy" className="editor-footnote"><span aria-hidden="true">·</span> No message uploads. No link opened. Reloading discards the draft.</p>
      </form>
      <section className="examples" aria-labelledby="examples-title"><h2 className="eyebrow" id="examples-title">TRY A FICTIONAL EXAMPLE</h2><p className="muted">Select to edit. Analysis starts only when you choose.</p><div className="ruled-list">{examples.map((example, i) => <button type="button" key={example.label} className={`example-row ${selected === example.label ? 'selected' : ''}`} aria-pressed={selected === example.label} data-bracket onClick={() => { setSelected(example.label); session.edit(example.text); focusEditor() }}><span className="eyebrow">0{i + 1}</span><span className="row-title">{example.label}<small>{example.kind}</small></span><Arrow /></button>)}</div></section>
      <aside className="screenshot-note"><span className="eyebrow">STARTING WITH A SCREENSHOT?</span><p>Android includes local text recognition with an editable review step. Browser screenshot recognition is outside this preview.</p><Link href="/android" className="underlined">Explore Android <Arrow /></Link></aside></div>
      <div className="evidence-column">{session.result ? <Result session={session} onEdit={focusEditor} /> : <section className="empty-result"><span className="eyebrow">THE EVIDENCE / {session.busy ? 'WORKING' : 'READY WHEN YOU ARE'}</span><div className="empty-aperture" aria-hidden="true"><span /><span /><i /></div><h2>{session.busy ? <>A closer look.<br />All here.</> : <>Clarity starts<br />with context.</>}</h2><p>Results separate observed patterns, link structure and statistical resemblance. A lower observed risk result is not a guarantee of safety.</p><ol className="inspection-steps"><li><span>01</span> Inspect the message</li><li><span>02</span> Understand the signals</li><li><span>03</span> Choose your next step</li></ol><p className="muted">No sender is authenticated. Your judgment stays central.</p></section>}</div>
    </div><Chapter number="01 / 06" to="/learn" label="Know what to look for." />
  </>
}
