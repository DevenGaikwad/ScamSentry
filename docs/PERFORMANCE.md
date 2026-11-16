# Performance and device protocol

The native runtime discards its parsed JSONObject after validation, caches the inverse vocabulary, streams active character counts and computes estimate/explanations from one weight extraction. These structural changes preserve the frozen numeric contract. They do not establish a phone speedup without measurements.

`android/benchmark` prepares release-like Macrobenchmark journeys for cold startup, warm text analysis/result scrolling, navigation and learning/history scrolling. The target benchmark variant inherits minification/resource shrinking from release, uses a local test signature and is profileable. Run `./gradlew :benchmark:connectedBenchmarkAndroidTest` on a real, charged, thermally stable device (API 34+ preferred), with model, ABI, OS, build/source commit and conditions recorded. Preserve JSON and Perfetto outputs. Do not suppress emulator or low-battery quality errors to report phone performance.

Collect at least 10 cold iterations and 10 warm journeys, with warm-up described. Report median, p95, sample count and uncertainty; preserve outliers and thermal notes. Separately measure cold model load, feature accumulation, scoring, explanations, OCR import/decode/recognition/review and complete time to a visible result. Screenshot imports must use fictional fixtures, with image dimensions/bytes/EXIF and model identifiers recorded. Repeated component loops do not replace complete-workflow latency.

No Baseline Profile text is invented or checked in. Generate one only from exercised release-like journeys, then repeat the same measurements with/without it. The existing module is preparation; hardware execution, baseline-profile generation, physical heap and battery evidence remain blocked until an actual device is available.

Browser worker timings and headless automation timings are separate from phone measurements. The production browser has an optional per-analysis local CPU timing in technical details. No desktop result is advertised as Android latency. Local Chromium launch is restricted by this container's Unix-socket policy, so browser workflow evidence must come from an unrestricted CI/runtime before that gate passes.

APK byte sizes, signature verification, ZIP alignment and ELF PT_LOAD alignment are machine checks. Actual 16 KB page-size execution remains a separate runtime test. A below-20 MB arm64 goal is provisional until the actual signed artifact is measured.
