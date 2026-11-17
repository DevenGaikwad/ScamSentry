import { readFile, writeFile, mkdir } from 'node:fs/promises'
// Explicit public documents, not a catch-all rewrite. Missing model/font/JS assets stay 404.
const base = await readFile('dist/index.html', 'utf8')
const routes = [
  ['analyze', 'Analyze a message', 'Inspect a message or link in your browser. Your draft stays in memory and no link is opened.'],
  ['learn', 'Build your second-look instinct', 'Six practical guides to sender identity, private codes, payments, links, uncertainty and promotional messages.'],
  ['evidence', 'Know the model. Know its limits.', 'Historical benchmark, class supports, confusion matrix, uncertainty and provenance for the approved ScamSentry model.'],
  ['privacy', 'Your input stays here.', 'Understand local browser processing, public asset downloads, preferences and reviewed redacted exports.'],
  ['android', 'A second look, on Android.', 'Download the existing test-signed Android research preview with local screenshot text recognition and review.'],
  ['learn/sender-identity', 'A name is not an identity.', 'A familiar name can make an unfamiliar request feel ordinary. Slow down at the request itself.'],
  ['learn/authentication-codes', 'Keep the code to yourself.', 'Receiving a code and being asked to reveal a code are different events.'],
  ['learn/unexpected-payments', 'Give payments a second check.', 'Urgency is a reason to verify the destination before money moves.'],
  ['learn/actual-host', 'Read the destination.', 'The most recognizable words in an address may not be the host.'],
  ['learn/useful-uncertainty', 'Make room for uncertainty.', 'A useful tool needs a clear way to say that it cannot assess something reliably.'],
  ['learn/promotion-and-smishing', 'An offer is not a verdict.', 'Unwanted advertising and credential-seeking messages are different patterns.'],
  ['404', 'Page not found', 'This page could not be found. Return to ScamSentry or analyze a message.'],
]
const origin = base.match(/<link rel="canonical" href="([^"]+)"/)?.[1].replace(/\/$/, '')
if (!origin) throw new Error('Canonical origin missing')
const escape = value => value.replaceAll('&', '&amp;').replaceAll('"', '&quot;').replaceAll('<', '&lt;')
for (const [path, title, description] of routes) {
  const canonical = `${origin}/${path === '404' ? '' : path}`
  const html = base.replace(/<title>.*?<\/title>/, `<title>${escape(title)} — ScamSentry</title>`)
    .replace(/(<meta name="description" content=")[^"]+/, `$1${escape(description)}`)
    .replace(/(<meta property="og:title" content=")[^"]+/, `$1${escape(title)} — ScamSentry`)
    .replace(/(<meta property="og:description" content=")[^"]+/, `$1${escape(description)}`)
    .replace(/(<meta property="og:url" content=")[^"]+/, `$1${canonical}`)
    .replace(/(<link rel="canonical" href=")[^"]+/, `$1${canonical}`)
  const target = `dist/${path}.html`
  await mkdir(target.slice(0, target.lastIndexOf('/')), { recursive: true })
  await writeFile(target, html)
}
await writeFile('dist/robots.txt', `User-agent: *\nAllow: /\nSitemap: ${origin}/sitemap.xml\n`)
await writeFile('dist/sitemap.xml', `<?xml version="1.0" encoding="UTF-8"?><urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">${['', ...routes.filter(r => r[0] !== '404').map(r => r[0])].map(path => `<url><loc>${origin}/${path}</loc></url>`).join('')}</urlset>\n`)
console.log(`Generated ${routes.length} public documents; no asset fallback.`)
