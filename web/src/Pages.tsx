import { HeroNetwork } from './motion/HeroNetwork'
import benchmark from './benchmark.json'
import manifest from './release-manifest.json'
import { articles, creatorCredit, site } from './site'
import { Link } from './router'
import { Arrow, Chapter, DecodedLabel, External, PageIntro, SignalDiagram } from './ui'

export function Home() {
  return <>
    <section className="home-hero" aria-labelledby="home-title"><DecodedLabel>SCAMSENTRY / A PERSONAL RESEARCH PREVIEW</DecodedLabel>
      <div className="hero-stage">
        <HeroNetwork />
        <h1 id="home-title" tabIndex={-1} aria-label="A second look. Before you act.">
          <span className="hero-mask"><span>A second look.</span></span>{' '}
          <span className="hero-mask"><span>Before you act.</span></span>
        </h1>
      </div>
      <div className="hero-bottom"><Link href="/analyze" className="hero-cta" data-bracket>Inspect a message <Arrow down /></Link><div className="hero-support"><p>A little distance from the urgency. Inspect messages and links with clear observations and a local research model.</p><span className="eyebrow">PAUSE / INSPECT / VERIFY</span></div></div>
    </section>
    <section className="home-flow"><div className="section-heading reveal"><span className="eyebrow">LOCAL BY DESIGN</span><h2>Your input.<br />Your perspective.</h2><p>No inbox access. No account. No automatic link opening. The page and approved model download first; your message is inspected in this browser.</p></div><SignalDiagram /></section>
    <section className="principles reveal" aria-label="How to use a second look">{[['01', 'See the observations.', 'A request for a secret, a payment prompt or an unusual address can be inspected without pretending to know who sent it.'], ['02', 'Read the uncertainty.', 'Text resemblance is a statistical estimate. Scope, missing context and unsupported input remain visible.'], ['03', 'Verify independently.', 'Use an app or contact you already trust. Keep codes private and confirm unexpected payments.']].map(([n, title, body]) => <div key={n}><span className="eyebrow">{n}</span><h3>{title}</h3><p>{body}</p></div>)}</section>
    <section className="home-evidence reveal"><span className="eyebrow">SMALL MODEL / OPEN EVIDENCE</span><div><h2>Useful questions.<br />Honest limits.</h2><p>A historical English short-message benchmark informs this research preview. It does not establish protection against current campaigns or verify authenticity.</p><Link href="/evidence" className="underlined">Read the evidence <Arrow /></Link></div><dl className="home-facts"><div><dt>Historical test records</dt><dd>{benchmark.records}</dd></div><div><dt>Model classes</dt><dd>{benchmark.class_order.length.toString().padStart(2, '0')}</dd></div><div><dt>Message uploads</dt><dd>0</dd></div></dl></section>
    <Chapter number="00 / 06" to="/analyze" label="Take a closer look." />
  </>
}
export function Learn() {
  return <><PageIntro number="02" label="PRACTICAL KNOW-HOW" title={<>Build your<br />second-look instinct.</>} lead="Six checks to make room for better judgment. No panic required." />
    <div className="learn-index ruled-list">{articles.map((article, i) => <Link key={article.slug} href={`/learn/${article.slug}`} className="learn-row reveal" data-bracket><span className="eyebrow">0{i + 1} / {article.label}</span><h2 className="row-title">{article.shortTitle}</h2><p>{article.lead}</p><Arrow /></Link>)}</div>
    <Chapter number="02 / 06" to="/evidence" label="Know the evidence." />
  </>
}
export function Article({ slug }: { slug: string }) {
  const article = articles.find(a => a.slug === slug)
  if (!article) return <NotFound />
  return <><PageIntro number="02" label={article.label} title={article.title} lead={article.lead} /><article className="article-layout"><div className="article-side"><Link href="/learn" className="underlined">All six guides</Link><p className="eyebrow">READ / CONSIDER / VERIFY</p></div><div className="prose"><h2>A practical second look.</h2><p>{article.body}</p><section className="fictional-example"><span className="eyebrow">ILLUSTRATION / FICTIONAL CONTENT</span><blockquote dir="auto">{article.example}</blockquote><p>{article.explanation}</p></section><h2>What you can do.</h2><ol className="practical-steps">{article.steps.map(step => <li key={step}>{step}</li>)}</ol><p className="muted">A tool result supports inspection. It does not prove authenticity or replace an independent check.</p><Link href="/analyze" className="primary link-button">Inspect a message <Arrow /></Link></div></article><Chapter number="02 / 06" to="/evidence" label="See how we measure." /></>
}
const pct = (value: number) => `${(value * 100).toFixed(2)}%`
export function Evidence() {
  const classes = ['Ordinary', 'Promotion / spam', 'Smishing-like']
  const [macroWhole, macroFraction] = (benchmark.macro_f1 * 100).toFixed(2).split('.')
  const matrix = benchmark.confusion_matrix
  const tp = matrix[2][2], fn = matrix[2][0] + matrix[2][1], fp = matrix[0][2] + matrix[1][2], tn = benchmark.records - tp - fn - fp
  const accepted = benchmark.policy_categories.higher + benchmark.policy_categories.lower
  const abstained = benchmark.records - accepted
  const acceptedErrors = Math.round(benchmark.accepted_error * accepted), abstainedErrors = Math.round(benchmark.abstained_error * abstained)
  return <><PageIntro number="03" label="TRANSPARENT BY DEFAULT" title={<>Know the model.<br />Know its limits.</>} lead="Numbers need a denominator. Evidence needs context." />
    <section className="evidence-lead reveal"><div className="benchmark-number">{macroWhole}<wbr />.{macroFraction}<wbr /><span>%</span></div><div><span className="eyebrow">PRESERVED HISTORICAL BENCHMARK</span><h2>Three-class macro-F1</h2><p>{benchmark.records} test records under {manifest.protocol_id}. Group-bootstrap 95% interval: {pct(benchmark.group_bootstrap_macro_f1_95ci[0])}–{pct(benchmark.group_bootstrap_macro_f1_95ci[1])}.</p><p className="muted">Model-only historical measurement. This is not a current-campaign protection rate, sender verification, browser OCR accuracy or a product-policy success rate.</p></div></section>
    <section className="editorial-section reveal"><div className="section-heading"><span className="eyebrow">01 / APPROVED BASELINE</span><h2>Small model.<br />Clear contract.</h2></div><div className="prose"><p>Character TF–IDF and multinomial logistic regression, with scalar-temperature calibration. Python, Kotlin and this browser use the same approved JSON parameters.</p><dl className="metadata"><div><dt>Model</dt><dd>{manifest.model_id}</dd></div><div><dt>Protocol</dt><dd>{manifest.protocol_id}</dd></div><div><dt>Analysis policy</dt><dd>ordinal_v2</dd></div><div><dt>Format / bytes</dt><dd>{manifest.format} / {manifest.byte_size.toLocaleString('en-US')}</dd></div><div><dt>SHA-256</dt><dd className="digest">{manifest.model_sha256}</dd></div></dl><External href={`${site.repository}/blob/main/docs/MODEL_CARD.md`}>Read the model card</External></div></section>
    <SignalDiagram kind="evidence" />
    <section className="editorial-section reveal"><div className="section-heading"><span className="eyebrow">02 / CLASSIFICATION COUNTS</span><h2>Every class.<br />Every count.</h2><p>Rows are the true class. Columns are the predicted class. The diagonal counts correct classifications.</p></div><div className="table-area"><table className="matrix"><caption>Historical test confusion matrix — {benchmark.records} records</caption><thead><tr><th scope="col">True ↓ / Predicted →</th>{classes.map(c => <th scope="col" key={c}>{c}</th>)}<th scope="col">Total</th></tr></thead><tbody>{matrix.map((row, i) => <tr key={i}><th scope="row">{classes[i]}</th>{row.map((value, j) => <td className={i === j ? 'diagonal' : ''} key={j}>{value}</td>)}<td>{row.reduce((a, b) => a + b, 0)}</td></tr>)}</tbody><tfoot><tr><th scope="row">Total</th>{classes.map((_, j) => <td key={j}>{matrix.reduce((sum, row) => sum + row[j], 0)}</td>)}<td>{benchmark.records}</td></tr></tfoot></table><p className="muted">Accuracy: {pct(benchmark.accuracy)}. Class imbalance means accuracy alone can hide errors in smaller classes.</p></div></section>
    <section className="editorial-section reveal"><div className="section-heading"><span className="eyebrow">03 / SUPPORTS & DENOMINATORS</span><h2>Read beneath<br />the average.</h2></div><div className="table-area"><table><caption>Class metrics from the preserved benchmark</caption><thead><tr><th scope="col">Class</th><th scope="col">Support</th><th scope="col">Precision</th><th scope="col">Recall</th><th scope="col">F1</th></tr></thead><tbody>{(['0', '1', '2'] as const).map((key, i) => <tr key={key}><th scope="row">{classes[i]}</th><td>{benchmark.class_report[key].support}</td><td>{pct(benchmark.class_report[key].precision)}</td><td>{pct(benchmark.class_report[key].recall)}</td><td>{pct(benchmark.class_report[key]['f1-score'])}</td></tr>)}</tbody></table><h3>Smishing-like, one versus rest.</h3><dl className="count-grid">{[['True positives', tp], ['False negatives', fn], ['False positives', fp], ['True negatives', tn]].map(([label, value]) => <div key={label}><dt>{label}</dt><dd>{value}</dd></div>)}</dl><p>Recall = {tp}/{tp + fn}; precision = {tp}/{tp + fp}; false positive rate = {fp}/{fp + tn}. Ordinary messages predicted smishing-like: {matrix[0][2]}/{benchmark.class_report['0'].support}. These denominators describe different questions.</p></div></section>
    <section className="editorial-section reveal"><div className="section-heading"><span className="eyebrow">04 / UNCERTAINTY & CALIBRATION</span><h2>A range,<br />with a context.</h2></div><div className="prose"><p>The macro-F1 interval uses {benchmark.bootstrap.repetitions} {benchmark.bootstrap.method} bootstrap repetitions, seed {benchmark.bootstrap.seed}, resampling the {benchmark.bootstrap.unit}. Heuristic grouping can leave latent campaign overlap. A single-source historical interval does not establish future performance.</p><dl className="metadata"><div><dt>Log loss</dt><dd>{benchmark.log_loss.toFixed(5)}</dd></div><div><dt>Multiclass Brier, sum</dt><dd>{benchmark.brier_multiclass_sum.toFixed(5)}</dd></div><div><dt>ECE, 10 equal-width bins</dt><dd>{benchmark.ece_10_equal_width.toFixed(5)}</dd></div></dl><p>Calibration belongs to this benchmark distribution. It is not the probability that a message is authentic.</p><details><summary>Historical reliability bins</summary><div className="table-area"><table><caption>Observed accuracy and mean confidence by populated bin</caption><thead><tr><th scope="col">Lower edge</th><th scope="col">Records</th><th scope="col">Accuracy</th><th scope="col">Confidence</th></tr></thead><tbody>{benchmark.reliability.map(bin => <tr key={bin.lower}><th scope="row">{bin.lower.toFixed(1)}</th><td>{bin.n}</td><td>{pct(bin.accuracy)}</td><td>{pct(bin.confidence)}</td></tr>)}</tbody></table></div></details></div></section>
    <section className="editorial-section reveal"><div className="section-heading"><span className="eyebrow">05 / PRODUCT POLICY, SEPARATELY</span><h2>More than<br />a model score.</h2></div><div className="prose"><p>The browser combines independent observations, scope checks and estimates into four ordinal categories. These category counts are distinct from the model confusion matrix.</p><dl className="count-grid">{[['Higher risk', benchmark.policy_categories.higher], ['Needs checking', benchmark.policy_categories.checking], ['Lower observed risk', benchmark.policy_categories.lower], ['Unable to assess reliably', benchmark.policy_categories.unable]].map(([name, count]) => <div key={name}><dt>{name}</dt><dd>{count}</dd></div>)}</dl><p>Historical selective coverage: {accepted}/{benchmark.records} = {pct(benchmark.selective_coverage)}. The accepted subset contains Higher risk and Lower observed risk results. Errors in that subset: {acceptedErrors}/{accepted} = {pct(benchmark.accepted_error)}. Errors among the remaining checking/unable results: {abstainedErrors}/{abstained} = {pct(benchmark.abstained_error)}. These are historical subset measurements, not authenticity probabilities.</p><External href={`${site.repository}/blob/main/docs/METRIC_DEFINITIONS.md`}>Metric definitions and policy denominators</External></div></section>
    <section className="editorial-section reveal"><div className="section-heading"><span className="eyebrow">06 / OPEN LIMITS</span><h2>Research<br />stays visible.</h2></div><div className="prose"><p>English short messages are the research scope. Marathi, Hindi and Hinglish are not validated. Script checks do not establish language support. Obfuscation remains a known weakness. Sparse input, unavailable models and unsupported content can limit analysis.</p><p>The latest grouped comparison tested four configurations. Each missed the predeclared smishing recall floor, so this release uses the approved estimator.</p><p>Physical-phone timings, accessibility, release first-use offline OCR and 16 KB runtime checks remain separate open Android gates. Browser OCR and PWA support are outside this preview.</p><External href={`${site.repository}/blob/main/docs/ML_V4_RESULTS.md`}>Development comparisons and limitations</External></div></section>
    <section className="editorial-section reveal"><div className="section-heading"><span className="eyebrow">07 / CREDIT & PROVENANCE</span><h2>Made with<br />accountability.</h2></div><div className="prose"><p className="creator-credit">{creatorCredit}</p><p>Dataset: Sandhya Mishra and Devpriya Soni (2022), Mendeley Data V1, DOI 10.17632/f45bkkt8pr.1. CC BY 4.0. Changes: minimization, normalization, deduplication, grouping and quarantine. No endorsement implied. Raw training messages are not hosted.</p><p>Archivo by the Archivo Project Authors / Omnibus-Type and Geist Mono by the Geist Project Authors use the SIL Open Font License 1.1. The Signal Aperture mark, diagrams and interface are original project contributions.</p><External href={`${site.repository}/blob/main/THIRD_PARTY_NOTICES.md`}>Licences and asset provenance</External></div></section>
    <Chapter number="03 / 06" to="/privacy" label="Understand what stays local." />
  </>
}
export function Privacy() {
  return <><PageIntro number="04" label="YOUR INPUT / YOUR CHOICE" title={<>Keep it personal.<br />Keep it local.</>} lead="A useful second look does not need your inbox." /><SignalDiagram kind="privacy" />
    <div className="privacy-principles">{[
      ['01', 'Memory, not a message database', 'Drafts and results stay in browser memory. Clear removes the current draft, result, timing and report state and invalidates active work. Reloading or closing the page discards the draft. Navigating between these pages keeps an idle draft in this tab; leaving active analysis cancels it.'],
      ['02', 'Local analysis, public assets', 'The host delivers the page, fonts, scripts and approved model. A local Web Worker processes your input. Your message is not an inference request, URL parameter, history-state payload, analytics event or feedback submission. Inspected addresses are not visited.'],
      ['03', 'Only two preference keys', 'Optional localStorage keys scamsentry.theme (system, light or dark) and scamsentry.motion (auto or paused) remember presentation choices. No draft or result is stored. Storage denial is harmless. No sessionStorage message history, IndexedDB database, service worker or application CacheStorage store is used. Your browser may normally cache public static assets.'],
      ['04', 'You review every export', 'The redacted report preview shows the exact download text: category, model/policy identifiers, observation titles and link count. It omits original text, destinations, account identifiers, email addresses, phone numbers and screenshots. A file is created only after you choose Download summary. Cancel and Close create no file.'],
      ['05', 'A precise offline claim', 'Initial app and model downloads need connectivity. This is not an offline PWA and includes no browser OCR. The host receives ordinary page and static-asset requests and may retain access logs. Local processing does not mean zero network activity.'],
    ].map(([n, title, body]) => <section className="privacy-row reveal" key={n}><span className="eyebrow">{n}</span><h2>{title}</h2><p>{body}</p></section>)}</div><External href={`${site.repository}/blob/main/docs/WEB_PRIVACY.md`} className="underlined">Read the technical privacy notes</External><Chapter number="04 / 06" to="/android" label="Take it to Android." /></>
}
export function Android() {
  return <><PageIntro number="05" label="NATIVE RESEARCH PREVIEW" title={<>A second look.<br />On Android.</>} lead="Local screenshot text recognition, an editable review and an explicit analysis step." />
    <section className="android-intro reveal">
      <div className="prose">
        <h2>Your screenshot.<br />Your review.</h2>
        <p>Recognize Latin text on Android, correct it in the editor, then inspect it on your device.</p>
        <p>Native preview 0.3.0 / version code 3. Android 8 / API 26 or newer.</p>
        <p className="muted">This preview uses a disposable test signing key. Check signing compatibility before updating an existing installation. Physical-device checks are tracked in the release notes.</p>
        <External href={site.release} className="underlined">Open the Android release</External>
      </div>
    </section>
    <section aria-labelledby="downloads-title" className="downloads reveal">
      <span className="eyebrow">TEST-SIGNED ANDROID PREVIEW</span>
      <h2 id="downloads-title">Choose your build.</h2>
      {[
        ['arm64-v8a', 'Compatible ARM phones'],
        ['x86_64', 'Matching emulator or device'],
        ['universal', 'Multi-architecture fallback'],
      ].map(([abi, label]) => <div className="download-row" key={abi}>
        <div><span className="eyebrow">{label}</span><h3>{abi}</h3></div>
        <External className="primary link-button" href={site.release.replace('/tag/', '/download/') + '/ScamSentry_0.3.0-preview_' + abi + '_TEST-SIGNED.apk'}>Download APK</External>
      </div>)}
      <External href={site.release.replace('/tag/', '/download/') + '/DOWNLOAD_SHA256SUMS.txt'}>Release checksums</External>
    </section>
    <Chapter number="05 / 06" to="/analyze" label="Try the browser tool." /></>
}
export function NotFound() {
  return <><PageIntro number="404" label="A MISSING CHAPTER" title={<>This page<br />isn’t here.</>} lead="The address may have changed, or the page may not exist." /><div className="not-found-actions"><Link href="/" className="primary link-button">Return home <Arrow /></Link><Link href="/analyze" className="secondary link-button">Analyze a message <Arrow /></Link></div><Chapter number="— / 06" to="/learn" label="Find a useful next step." /></>
}
