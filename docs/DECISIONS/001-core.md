# Architecture decisions, release 0.1.0 preview

| Decision | Evidence and consequence |
|---|---|
| Three source labels remain separate | Promotion is not a verified smishing annotation. UCI is not merged into a fraud class. |
| Use primary Mendeley v1 only | Verified publisher CC BY 4.0; CSV-only archive with no contradictory notice. UCI adds 5,200 exact normalized overlaps among 5,574 parsed records and remains conditional after older readme review. |
| Minimize identifiers before training and deployment features | Reduces unnecessary vocabulary exposure and dataset shortcuts. Raw-text rule/URL channels preserve useful local observations. Names may remain. |
| Group globally before split | Exact/template links and shingle-verified neighbors avoid obvious train/test template reuse; conflict components quarantined. Unknown campaign independence is not proved. |
| Direct character LR deployment | Development C2 macro-F1 .900; compact MLP .862 and group CV mean .867. LR has a small transparent export and faithful feature-difference contributions. Comparison table preserves alternative results. |
| Scalar temperature | One-parameter calibration is portable and uses a separate calibration partition. No live-distribution calibration claim. |
| Conservative ordinal states | Fixed .62 confidence/.18 margin plus explicit observations; avoid a combined fabricated confidence score. |
| Metadata-only optional history | Avoid collecting raw messages. Room metadata is not encrypted and history remains opt-in/manual. No raw-text save mode is exposed. |
| No external URL execution | Offline lexical inspection cannot verify live reputation. Learned URL research is deferred; unavailable webpage features are not replaced with zeros. |
| Bundled Latin OCR | First-use offline-capable dependency selected; resource/package and real-device checks remain required. |
| Modest module graph | Pure JVM core plus Android app; manual Application graph is enough for this semester. |
| Dark security-studio identity | Semantic blue/purple/red/amber/green tokens; no fake scan timings, threat counters or ratings. |

No paid service, external permission correspondence, inbox donation, remote model or public publication is required for the local workflow. The preview must retain measured limitations rather than claiming release readiness before device gates pass.
