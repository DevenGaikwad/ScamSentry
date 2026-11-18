# Performance and device protocol

Browser 0.4.0 and Android 0.3.0-preview were tested from source `01765677ea3e7a95b8983498168604cfec83090c`. The current warm worker analysis median was **0.400 ms** and p95 was **0.700 ms** on 24 warm fictional inputs in Linux Chromium. One cold-worker input used a populated HTTP cache. The release verification holds the full sample list and scope. Physical-phone latency, battery, allocations and fresh-network browser load remain unmeasured. [Verification report](https://github.com/DevenGaikwad/ScamSentry/releases/download/v0.3.0-preview/RELEASE_VERIFICATION.json)

## Native runtime and benchmark preparation

The Kotlin runtime discards its parsed JSONObject after validation, caches the inverse vocabulary, streams active character counts and calculates estimate/explanations from one weight extraction. Separate numeric and complete-contract suites preserve the frozen model behavior. These structural changes do not establish a phone speedup without measurements.

`android/benchmark` contains cold startup, warm analysis/result scrolling, navigation and learning/history Macrobenchmark journeys. The target inherits release minification/resource shrinking, with debug signing and a profileable manifest. Both external text and actual photo-picker/OCR/review/edit/result functional smoke cases passed on API 35 x86_64 KVM with verified offline radios/default connectivity. The emulator result is not a latency, heap or battery benchmark.

Run `./gradlew :benchmark:connectedBenchmarkAndroidTest` on a real, charged, thermally stable device (API 34+ preferred). Record hardware/chipset/RAM, OS/API, ABI, source/build/model digests, connectivity, battery and thermal state. Preserve original JSON and Perfetto outputs. Do not suppress emulator/low-battery quality errors to fabricate hardware measurements.

Collect at least 10 cold starts, 30 warm text samples and 10 screenshot workflows, with warm-up and input set declared. Report median/p95/sample count/uncertainty and retain outliers. Measure cold model load, features, scoring, explanations, import/decode/recognition/review and complete time to visible result separately. Include maximum input and image dimensions/bytes/EXIF stress. Repeated component loops do not replace complete-workflow latency.

No generated Baseline Profile is invented or checked in. Generate it only from actual exercised journeys, then repeat the same protocol with/without it. Physical heap/allocation/battery, startup and OCR latency remain open. Provisional p95 text <250 ms and a few-second screenshot workflow are goals rather than reported phone outcomes.

## Historical v2 browser interval

Final environment: Chromium `159.0.8154.6`, `linux` / `x64`, production static build, 25 fictional iterations. The first sample starts a cold worker with a populated HTTP cache; 24 later samples reuse the validated model.

| Measured interval | Actual value | Scope |
|---|---:|---|
| Warm worker analysis median | 0.300 ms | Instrumented analysis interval; excludes asset download/UI/rendering |
| Warm worker analysis p95 | 0.600 ms | 24 warm samples; narrow fictional workload |
| Warm worker total median | 0.400 ms | Worker-local interval, separate from Playwright roundtrip |

Original values and automation roundtrips are in [web_desktop-chromium_timings.json](../assets/evidence/v2/final/web_desktop-chromium_timings.json). This is Linux/headless Chromium evidence; mobile viewports are not physical mobile hardware. No fresh-network cold-download, heap, battery or Android latency claim is made. Earlier run timings remain historical evidence rather than silently being replaced.

## Signed download sizes and alignment

| Android 0.3.0-preview APK | Exact bytes | Decimal MB |
|---|---:|---:|
| arm64-v8a | 15,276,765 | 15.277 |
| x86_64 | 15,837,908 | 15.838 |
| universal | 45,362,671 | 45.363 |

The arm64 download meets the provisional 20 MB goal. Sizes refer to signed APK downloads. The universal fallback contains arm64-v8a, armeabi-v7a, x86 and x86_64. [Artifact inventory](https://github.com/DevenGaikwad/ScamSentry/releases/download/v0.3.0-preview/ARTIFACT_INVENTORY.json)

APK signature, ZIP alignment and packaged 64-bit ELF PT_LOAD alignment checks passed. Execution on a device with 16 KB pages remains open. [Release audit](V4_ACCEPTANCE.md)
