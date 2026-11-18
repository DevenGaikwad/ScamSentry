# Motion follow-up v3.1

Baseline: completed v3 source `0e248cc766d58bc97c7dc504bbd03b21b7aa8b57`, documentation/evidence closure `92df2bbaf00aed9431711b4fec793b0b69e615f5`. The old healthy deployment is `dpl_HUWQrorzT2fmY9P5QLJFNZkACyJQ`. The attached 10,985-word specification is retained verbatim in [FRONTEND_MOTION_SPEC_V3_1.md](FRONTEND_MOTION_SPEC_V3_1.md); build verification checks its SHA-256 as well as the preserved v3 prompt, fonts and scientific boundaries.

## Original implementation

| Requirement | Implementation |
|---|---|
| Free-follow cursor | One viewport-fixed four-corner frame, 34 CSS px, native pointer retained; 48 ms positional / 100 ms shape damping; 6 px target padding. Semantic nested links/buttons/disclosures resolve with `closest`; large targets retain the small local frame. Scroll/resize invalidate bounds; no frame loop after settling. |
| Public depth choreography | One router coordinator and monotonic generation. History and destination commit once at accepted intent, with `null` history state. The real destination main plane approaches through perspective while a static public-metadata outgoing plane recedes. Rails, grid, boundary rings, vertical corridor and home convergence share one scene vocabulary. Desktop duration 1120 ms; narrow duration 720 ms. The header remains available. |
| Safety and focus | A strict static-route allowlist; Analyze, unknown routes, open dialogs, paused/reduced/forced-color states use immediate navigation. No analyzer clone, screenshot, texture, foreignObject or data serialization. One final heading-focus owner; scroll restoration to top at route commit, including Back/Forward. Generation checks, cancellation and an independent completion timer/watchdog restore the latest destination. |
| Hero network | Original seeded projected Canvas 2D, 7 nodes / 42 strands on desktop, 5 / 24 on narrow screens; six sparse markers; world-Y then X rotation, near-plane rejection and DPR capped at 1.75. Static SVG survives missing/failing canvas. ResizeObserver and visibility tracking suspend offscreen/hidden work; redraw capped near 30 Hz. Actual FPS remains a measured lab question. |
| Directed diagrams | Separate continuous edge paths, stable IDs, real arrow directions and per-edge arc-length lookup. A single bounded scheduler moves a handful of packets; first reveal draws paths before travel. Home shows inspection outputs; Privacy distinguishes inbound public assets, in-memory input and explicit reviewed export; Evidence illustrates the already completed protocol. No live traffic or model progress is implied. |
| Accessibility | Shared existing motion preference; system reduction and forced colors stop decoration, retain completed wires/paths and preserve readable content. Mobile diagrams include a readable node legend. Decorative layers are noninteractive and hidden from accessibility; transient frames are inert, with no duplicate landmarks or IDs. |

No graphics dependency, server, telemetry, paid service or dataset was added. The existing model, worker, portable engine, benchmark, manifest, research records, Android code and released APKs remain unchanged. This frontend patch is version 0.3.1; it is not a model or Android release. Existing historical statistical limitations remain authoritative.

## Validation and release status

Local type checking, lint, 71 unit/parity tests and production build pass. The first attempted local browser launch fails before application execution because the host denies Chromium's process-singleton socket. This environment failure is not a browser pass. GitHub CI is the execution environment for browser validation and captures; its run, source, artifact and deployment receipts must be added after they actually complete. The browser workflow retains both existing regression suites and new motion/race/privacy/fallback tests across desktop Chromium, mobile Chromium and desktop Firefox.

Use the original CI `web/dist` output for preview and production in the same Vercel project. Git pushes alone do not deploy this standalone project. Preserve the preferred `scamsentry-inspect.vercel.app` hostname, restrictive CSP and old-host redirect. Release requires actual scene visibility, pointer motion, packet travel, a fictional analysis journey and reduced-motion behavior; a phase flag or nonempty canvas alone is insufficient.
