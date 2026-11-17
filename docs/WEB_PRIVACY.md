# Browser privacy boundaries

Message input, results and drafts live in browser memory. They are not submitted to a server, URL query/fragment, analytics service, feedback endpoint or browser-history store. Only the optional exact localStorage allowlist `scamsentry.theme` (system/light/dark) and `scamsentry.motion` (auto/paused) stores presentation preferences. No draft, input, result or estimate is serialized. There is no sessionStorage history, IndexedDB database, application CacheStorage store, service worker, account or remote explanation API. Denied preference storage falls back safely to system defaults. Public paths and `history.state = null` carry no input. Native links support deep reload and back/forward. Reload/close discards the draft; Clear removes the current message/result and invalidates worker requests.

The host receives ordinary page, font, JS and model-asset requests and may retain normal access logs. Initial connectivity is required. This is not a zero-network or offline-PWA claim. The model download is a public fixed asset independent of the input, requested with credentials omitted. The model is content-addressed and digest checked before use.

Reports contain the product category, version metadata, observed-pattern names and link count. Original input, destination addresses and screenshots are omitted. The user previews the report before a file download. Downloaded files are then controlled by the user's device/browser.

Tests use fictional canaries to inspect request URLs/methods and assert no input is transmitted. Raw real user examples are not part of test artifacts. Browser OCR is not in this preview. Native Android retention and exports have their own documented boundaries in PRIVACY.md.

Provider content terms were reviewed on 16 November 2026. Only public code, compatible fonts, fictional demos and attributed model-derived assets are eligible for hosting. Raw training messages and private app content are excluded. Model vocabularies can reveal fragments; typed minimization is not differential privacy. Source rights and hosting eligibility remain independent checks.
