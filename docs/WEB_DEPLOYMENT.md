# Static deployment and local demonstration

The exact repository is DevenGaikwad/ScamSentry, with `web` as the deployment root. Build with a compatible Node 24 environment, `npm ci`, then `npm run build`; publish `dist`. Vercel configuration specifies Vite, the build/output commands, a restrictive CSP, no-referrer, nosniff and denied camera/microphone/geolocation. The worker needs same-origin model downloads only. There are no server functions, accounts, databases, keys or environment secrets.

`web/vercel.json` applies immutable caching to content-addressed model assets and revalidation to the page. JS/worker assets carry Vite hashes and embed the matching manifest, preventing a silent old-model/new-normalizer combination. No service worker is installed. Public HTTPS is required for browser cryptographic integrity checks; localhost is suitable for development.

**Owner instruction, 17 November 2026: defer Vercel activation and deployment until a later session.** Do not generate another time-limited activation link during the independent Android/GitHub work. The static source and build remain ready.

The earlier account review reported Hobby, but the target team returned 403. That is historical account evidence, not verified current target entitlement. When the owner resumes deployment, recheck the free target scope, terms and usage before proceeding. No paid workaround was used, and no live URL is advertised.

Local run from `web`: `npm ci`, `npm run build`, `npm run preview`. Open the displayed localhost URL. The app is functional text/URL analysis with the approved baseline. Browser OCR and PWA behavior are deliberately not claimed. The public repository includes the production build recipe and workflow tests even if optional hosting remains blocked.
