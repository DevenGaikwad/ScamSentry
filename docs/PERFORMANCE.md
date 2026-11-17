# Performance and device protocol

Exact-source final functional evidence is [CI run 44012744040](https://github.com/DevenGaikwad/ScamSentry/actions/runs/44012744040), source `bc227ba189796c1167c3cd21c3a7ec973156ccfd`. [Observed report](../assets/evidence/v2/final/RELEASE_VERIFICATION.json) and [artifact inventory](../assets/evidence/v2/final/ARTIFACT_INVENTORY.json) retain machine-readable values. Functional tests, download sizes and CPU intervals are distinct from physical-phone performance.

## Native runtime and benchmark preparation

The Kotlin runtime discards its parsed JSONObject after validation, caches the inverse vocabulary, streams active character counts and calculates estimate/explanations from one weight extraction. Separate numeric and complete-contract suites preserve the frozen model behavior. These structural changes do not establish a phone speedup without measurements.

`android/benchmark` contains cold startup, warm analysis/result scrolling, navigation and learning/history Macrobenchmark journeys. The target inherits release minification/resource shrinking, with debug signing and a profileable manifest. Both external text and actual photo-picker/OCR/review/edit/result functional smoke cases passed on API 35 x86_64 KVM with verified offline radios/default connectivity. The emulator result is not a latency, heap or battery benchmark.

Run `./gradlew :benchmark:connectedBenchmarkAndroidTest` on a real, charged, thermally stable device (API 34+ preferred). Record hardware/chipset/RAM, OS/API, ABI, source/build/model digests, connectivity, battery and thermal state. Preserve original JSON and Perfetto outputs. Do not suppress emulator/low-battery quality errors to fabricate hardware measurements.

Collect at least 10 cold starts, 30 warm text samples and 10 screenshot workflows, with warm-up and input set declared. Report median/p95/sample count/uncertainty and retain outliers. Measure cold model load, features, scoring, explanations, import/decode/recognition/review and complete time to visible result separately. Include maximum input and image dimensions/bytes/EXIF stress. Repeated component loops do not replace complete-workflow latency.

No generated Baseline Profile is invented or checked in. Generate it only from actual exercised journeys, then repeat the same protocol with/without it. Physical heap/allocation/battery, startup and OCR latency remain open. Provisional p95 text <250 ms and a few-second screenshot workflow are goals rather than reported phone outcomes.

## Executed browser interval

Final environment: Chromium `159.0.8154.6`, `linux` / `x64`, production static build, 25 fictional iterations. The first sample starts a cold worker with a populated HTTP cache; 24 later samples reuse the validated model.

| Measured interval | Actual value | Scope |
|---|---:|---|
| Warm worker analysis median | 0.300 ms | Instrumented analysis interval; excludes asset download/UI/rendering |
| Warm worker analysis p95 | 0.600 ms | 24 warm samples; narrow fictional workload |
| Warm worker total median | 0.400 ms | Worker-local interval, separate from Playwright roundtrip |

Original values and automation roundtrips are in [web_desktop-chromium_timings.json](../assets/evidence/v2/final/web_desktop-chromium_timings.json). This is Linux/headless Chromium evidence; mobile viewports are not physical mobile hardware. No fresh-network cold-download, heap, battery or Android latency claim is made. Earlier run timings remain historical evidence rather than silently being replaced.

## Signed download sizes and alignment

| Final test-signed APK | Exact bytes | Decimal MB |
|---|---:|---:|
| arm64-v8a | 15,747,556 | 15.748 |
| x86_64 | 16,308,699 | 16.309 |
| universal | 45,833,462 | 45.833 |

The actual arm64 download is below the provisional 20 MB goal. These are signed download sizes, not installed footprint. The universal fallback includes arm64-v8a, armeabi-v7a, x86 and x86_64; split downloads are ABI-specific. Exact signatures and SHA-256 values are in [RELEASE_HANDOFF.md](RELEASE_HANDOFF.md).

APK signature verification, ZIP alignment and all packaged 64-bit ELF PT_LOAD alignment checks passed. Static 16 KB alignment does not prove execution on an actual 16 KB page-size system; that runtime gate remains open. [APK inspection](../assets/evidence/v2/final/apk_inspection.json).
