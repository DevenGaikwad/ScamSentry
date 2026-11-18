# Editorial frontend audit

Starting source: `526f6c095fce6d5919a4a2bf1c4d8fc007b7bce9`. Isolated branch: `feat/editorial-frontend-v3`. The preserved specification and prefix hashes are in [STARTING_CHECKPOINT.json](../assets/evidence/v3/STARTING_CHECKPOINT.json).

## Before implementation

The original web app has one React component with in-memory button navigation, a navy sidebar, Inter/Space Grotesk, a shield favicon and no shareable pages, preference controls or browser Android page. Its worker integrity checks, engine, model and benchmark remain approved boundaries. The old creator credit abbreviates the degree and footer omits the middle name.

| Surface | Existing states / retained behaviors | Required redesign |
|---|---|---|
| Analyzer | empty, whitespace, populated, over 4,000 code points, loading, processing, edited, cancelled, result, unavailable model, report preview | Editorial form, request invalidation, neutral categorical results, focus/announcements |
| Education | six topic cards | Ruled index and six original shareable articles |
| Evidence | model metadata and summary benchmark | Accessible confusion matrix, denominators, intervals, uncertainty and full credit |
| Privacy | local-processing explanation | Accessible original flow diagram, explicit preference allowlist |
| Home / Android / unknown route | absent | Complete editorial pages and useful not-found page |
| Global | fixed sidebar, no theme/motion state | Horizontal responsive navigation, native links/history, two neutral themes, controlled original motion and SS monogram |

## Rendered reference observations

`https://www.asklionel.tech/` and `/work` were directly inspected in the cloud Chrome browser on 17 November 2026. The reference has oversized heavy uppercase type, narrow mono labels, thin rules, left reading rail, open whitespace, horizontal navigation, reversible light/dark themes, selectable work rows and a motion-pause control. Selecting Alma changes the expanded row and diagram/content; page entry uses a planar depth transition. These are interaction observations, not copied source/assets. Awards, company content, metrics and personal branding are excluded from ScamSentry. Private analyzer DOM will never be rasterized for transitions.

## Implementation and validation

The editorial redesign is implemented and merged at `0e248cc766d58bc97c7dc504bbd03b21b7aa8b57`. All 67 unit/parity and 42 browser cases passed. Original captures, performance limitations and exact deployment provenance are in [FRONTEND_VALIDATION.md](FRONTEND_VALIDATION.md). The v3.1 motion follow-up is a separate next change; historical model and Android evidence remain separate.
