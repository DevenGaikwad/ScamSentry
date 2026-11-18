# Editorial frontend validation

[Current browser 0.4.0 verification](V4_ACCEPTANCE.md)

Verified continuation: 18 November 2026. The v3 application is complete; this handoff preserves its original passing build before the separately requested motion follow-up.

## Source and production identity

- Merged source: `0e248cc766d58bc97c7dc504bbd03b21b7aa8b57`; tested source `f2b44da27166c2f1c8366d47247152dd1850a2f2`; identical tree `4af39ea26231217396ebb96f43fec9a4650a4639`.
- [Passing CI run 44158226922](https://github.com/DevenGaikwad/ScamSentry/actions/runs/44158226922): browser artifact `14362371953`, ZIP SHA-256 `b347c94528baa8b189f3e751a19ccadde3f1420f5a195e36536f5b092084dfc3`. Verified before extracting original reports/captures.
- Production: `dpl_HUWQrorzT2fmY9P5QLJFNZkACyJQ`, [scamsentry-inspect.vercel.app](https://scamsentry-inspect.vercel.app/). The old hostname redirects with HTTP 307, preserving `/privacy`.
- [Fresh credential-free verification](../assets/evidence/v3/final/PUBLIC_HTTP_VERIFICATION.json) compares all recovered `web/dist` files against HTTPS bytes and records a real unknown-route 404. It does not claim a fresh browser interaction trace.

## Executed checks

67 unit/parity tests passed; 42 browser cases passed across desktop Chromium, mobile Chromium and desktop Firefox, with zero failures, skips or flakes. This includes both themes, analyzer states, cancellation, worker/error behavior, privacy canaries, redacted reports, routing, keyboard/menu behavior, layout matrices, forced colors and narrow/text-spacing fallback. The original production output, rather than a later rebuild, was deployed. Completed Python, Windows and Android evidence remains independently recorded in the v2 release handoff; no retraining or native release was performed for this browser work.

[Curated original screenshots and recording](../assets/evidence/v3/final/PROVENANCE.json) retain exact bytes and a complete inventory of the recovered browser captures. Original baseline/development performance samples are also retained.

## Performance interpretation

[Measured summaries](../assets/evidence/v3/final/PERFORMANCE_SUMMARY.json) and their full lab inputs describe three cold and three warm samples per variant on identical local gzip-enabled servers without CPU/network throttling. These are small laboratory samples, not field Core Web Vitals. Event-duration samples are not field INP. Unsupported measurements must not be interpreted as passing zeros. The mobile project performance fixture reports a desktop-sized viewport; its label is not evidence of a physical phone benchmark.

The redesign substantially reduces transferred font/application bytes (approximately 79.7% cold transfer reduction in the earlier handoff). Firefox's measured LCP is slower than the old baseline in this small sample. Do not claim a universal speed improvement or hide that regression. The raw samples include their earlier source attribution; the successful final-run artifact and current served-file hashes establish the final build relationship separately.

## Preserved scope and open gates

The approved model, portable engine, analysis worker, numerical benchmark and Android source/release remain unchanged. Message text stays in memory and is not captured for public page transitions. English historical text resemblance is not sender authentication; the retained model's severe-obfuscation weakness and native physical-device/accessibility/offline/16 KB runtime gates remain open.

Production dependencies have no reported vulnerabilities in the preserved run. The known development-only picomatch advisory and audit decision remain explicit in `web/scripts/audit-dependencies.mjs`; this document does not claim zero findings across all dependencies.
