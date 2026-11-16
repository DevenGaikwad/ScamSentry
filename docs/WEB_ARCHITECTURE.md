# Browser companion architecture

React 19.3.1, TypeScript 6.0.3 and Vite 8.3.8 build static assets from `web/`. TypeScript 7.0.2 was evaluated but excluded because typescript-eslint 8.72.2 declares TypeScript <6.1 support. No peer checks were bypassed. React's approved model is not in the initial JS bundle; an ES-module worker loads the content-addressed JSON only when analysis is requested.

The worker validates byte length, SHA-256, schema, class order, numeric shapes, finite parameters, normalizer, privacy transform, feature semantics, calibration and model-policy compatibility. The manifest is embedded in the matching hashed worker. There is no service worker or private-input cache. A corrupt asset yields a limited model state; local rule observations can still be shown. No toy-model or score fallback exists.

The end-to-end contract is schema 2, `ordinal_v2`, `local_context_v2`, `lexical_v2`, with the immutable `ss-char-lr-0.1.0` estimator and `nfkc_ascii_ws_v1` preprocessing. 53 fictional cases compare complete Python/Kotlin/TypeScript outputs, including intermediate norms/logits, observations, flags and limitations. Separate synthetic word/hybrid exports exercise actual scikit-learn fit arithmetic. Those research models are never imported by the shipping worker.

Input remains in React memory, with monotonically increasing request IDs. Editing, clearing or canceling terminates the worker and invalidates pending results. Model loading and processing have distinct states. Components display observed patterns, actual destination hosts, statistical resemblance and limitations separately. Addresses are text; no navigation or reputation request is made.

The UI has a desktop navigation rail, paired editor/evidence panes, compact mobile navigation, large touch controls, semantic input labels and dialogs, visible focus styles and reduced-motion support. Navigation does not encode drafts into a route. A redacted download is available only after a preview.

Browser OCR is deferred. An eligible complete worker/WASM/language-data asset set and mobile-browser memory/quality checks were not established in this execution. This preview explicitly directs screenshot users to Android, which retains bundled ML Kit. No screenshot control returns fabricated text. A PWA and first-use offline browser operation are not claimed.

Build: `cd web`, `npm ci`, `npm run typecheck`, `npm run lint`, `npm test`, `npm run build`. `npm run test:e2e` exercises the production static output through Playwright. Tests need a functioning Chromium runtime; local container IPC restrictions and CI execution are separate evidence gates.
