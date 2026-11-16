# ScamSentry v2 — Audited Upgrade, Verification, and GitHub Publication Master Prompt

**Owner:** Deven Sachin Gaikwad  
**Prepared:** 16 November 2026  
**Input:** ScamSentry_0.1.0_Research_Preview.zip  
**Purpose:** Continue the existing Android project, fix reproduced weaknesses, improve measured model and app quality, and create the authorized GitHub repository named ScamSentry.

## Read this first

This is a continuation prompt grounded in an inspected implementation. It replaces a generic request to regenerate the application from scratch. Preserve the useful source, trained baseline, data-rights records, notices, and historical results already delivered. Use the original specification in docs/IMPLEMENTATION_SPEC.md for requirements not superseded here.

The user is requesting a technically strong, aesthetically polished, useful project for Mobile App Development and Cybersecurity. Their preference is a native Android app with a dark technical visual identity, offline analysis, defensible machine learning, and a professional GitHub presentation. Do not interpret “God-level” or “perfect” as permission to fabricate results or claim guaranteed scam detection.

The input archive was reviewed before this prompt was written. Several findings are reproduced in the Python reference implementation; others are static code findings or pending device checks. Maintain that distinction. The author of this prompt did not run the Android app on a physical phone, rebuild it with an SDK, or create the GitHub repository during the audit.

## Navigation

- [1. Execution objective and authorized GitHub outcome](#1-execution-objective-and-authorized-github-outcome)
- [2. Immutable starting evidence](#2-immutable-starting-evidence)
- [3. What the current project already implements](#3-what-the-current-project-already-implements)
- [4. Independent checks performed during this audit](#4-independent-checks-performed-during-this-audit)
- [5. Priority finding: global suppression of secret-request warnings](#5-priority-finding-global-suppression-of-secret-request-warnings)
- [6. Priority finding: validation can report success after skipping a scan](#6-priority-finding-validation-can-report-success-after-skipping-a-scan)
- [7. Important model-selection finding](#7-important-model-selection-finding)
- [8. Important robustness finding and required research response](#8-important-robustness-finding-and-required-research-response)
- [9. Important parity and policy-orchestration finding](#9-important-parity-and-policy-orchestration-finding)
- [10. URL inspection improvements](#10-url-inspection-improvements)
- [11. Privacy, history, and export refinements](#11-privacy-history-and-export-refinements)
- [12. OCR correctness, memory ownership, and image handling](#12-ocr-correctness-memory-ownership-and-image-handling)
- [13. Updated stack: retain the native foundation](#13-updated-stack-retain-the-native-foundation)
- [14. Dependency upgrade acceptance and build reproducibility](#14-dependency-upgrade-acceptance-and-build-reproducibility)
- [15. Research protocol for v2 models](#15-research-protocol-for-v2-models)
- [16. Data expansion and provenance safeguards](#16-data-expansion-and-provenance-safeguards)
- [17. Candidate model implementation details](#17-candidate-model-implementation-details)
- [18. Calibration, operating policy, and limited-support behavior](#18-calibration-operating-policy-and-limited-support-behavior)
- [19. Efficiency improvements in the model runtime](#19-efficiency-improvements-in-the-model-runtime)
- [20. APK size and ABI-specific distribution](#20-apk-size-and-abi-specific-distribution)
- [21. Baseline Profiles, Macrobenchmark, and performance evidence](#21-baseline-profiles-macrobenchmark-and-performance-evidence)
- [22. Modern architecture without unnecessary fragmentation](#22-modern-architecture-without-unnecessary-fragmentation)
- [23. Navigation and adaptive layout upgrade](#23-navigation-and-adaptive-layout-upgrade)
- [24. Visual refinement and interaction quality](#24-visual-refinement-and-interaction-quality)
- [25. Testing that works from the distributed source](#25-testing-that-works-from-the-distributed-source)
- [26. Device verification and realistic environment handling](#26-device-verification-and-realistic-environment-handling)
- [27. CI that proves the public checkout works](#27-ci-that-proves-the-public-checkout-works)
- [28. Security and data-rights release review](#28-security-and-data-rights-release-review)
- [29. Create and initialize the exact GitHub repository](#29-create-and-initialize-the-exact-github-repository)
- [30. Repository content, release assets, and professional presentation](#30-repository-content-release-assets-and-professional-presentation)
- [31. Signing, versioning, and release channels](#31-signing-versioning-and-release-channels)
- [32. Academic and competition-level evidence](#32-academic-and-competition-level-evidence)
- [33. Scope priorities and useful optional extensions](#33-scope-priorities-and-useful-optional-extensions)
- [34. Milestones and concrete deliverables](#34-milestones-and-concrete-deliverables)
- [35. Resume protocol and evidence management](#35-resume-protocol-and-evidence-management)
- [36. Definition of completion for this follow-up](#36-definition-of-completion-for-this-follow-up)
- [Appendix A. Required regression and verification matrix](#appendix-a-required-regression-and-verification-matrix)
- [Appendix B. Performance and quality scorecard](#appendix-b-performance-and-quality-scorecard)
- [Appendix C. Command contracts and publication checklist](#appendix-c-command-contracts-and-publication-checklist)
- [Appendix D. Official references for the upgrade review](#appendix-d-official-references-for-the-upgrade-review)
- [Appendix E. Copy-ready launch instruction](#appendix-e-copy-ready-launch-instruction)

## 1. Execution objective and authorized GitHub outcome

Execute this prompt against the supplied research-preview source. Complete a concrete improvement cycle rather than returning another high-level proposal. Preserve the working baseline, fix priority defects, verify the app, improve research quality, prepare reviewed artifacts, and publish the authorized repository.

The requested GitHub destination is the owner's personal profile: DevenGaikwad. The exact repository name is ScamSentry. Use public visibility for the intended academic and résumé portfolio unless the owner explicitly changes that instruction. The user's current request supplies authorization to create that repository and publish the reviewed project when this prompt is executed. Update earlier project-state text that says publication authorization was not provided.

Verify the authenticated account at execution time. During preparation, the connected profile returned DevenGaikwad. A read-only lookup of DevenGaikwad/ScamSentry returned HTTP 404. That result does not prove permanent name availability or distinguish every access issue. Recheck before creating anything. If a repository already exists, inspect it, preserve its history, and continue there instead of creating a differently named duplicate.

Repository creation and source publication must not be deferred solely because a physical phone is unavailable. Publish an accurately labelled research preview with its unresolved device gates if necessary. Final production claims, signed production releases, and device-performance badges require their own evidence. A public source repository can honestly contain an unfinished verification checklist.

Do not send dataset-permission emails. Prefer sources whose verified terms cover the intended use. Exclude or quarantine permission-dependent or ambiguous additions. Do not purchase services, expose credentials, collect private inboxes, add unrelated collaborators, or modify other repositories. Keep all work within ScamSentry and its explicitly authorized release assets.

## 2. Immutable starting evidence

Before editing, verify the archive's digest and safely extract it into an isolated workspace. Reject path traversal and unexpected symlinks. Record these observed baseline identifiers:

| Item | Observed value |
|---|---|
| Archive SHA-256 | fbfc705ce7959c5ae48c96942475c45f3d39416189150e2123888f52f6766dca |
| Archive entries | 154 |
| Total uncompressed entry bytes | 114,887,257 |
| Bundled message-model identifier | ss-char-lr-0.1.0 |
| Historical protocol | ss-group-v1 |
| Model JSON bytes | 2,369,544 |
| Model SHA-256 | 14927b442dc9f33a35ad7086bd522b87e1110a73a6d1b2826b73ac1bf1a8a8fa |
| Source commit recorded in archive metadata | 499fa7879d499a715306424d0e95da2b2c554158 |
| Debug APK bytes | 58,813,148 |
| Unsigned release APK bytes | 45,741,083 |

The archive contains no Git object history. Treat the recorded source commit as provenance metadata from the earlier build, not as a commit whose presence in a local repository was independently verified during this audit. If the original checkout exists, preserve it. If only the ZIP exists, create an honest import commit and retain the recorded baseline provenance in documentation.

The Android SDK is not bundled. The archive includes Android source, a Gradle wrapper, evidence, model assets, fonts, and APKs. It references SDK packages needed for a build. Do not tell the owner that the ZIP installs Android Studio or contains a complete SDK. Distinguish Android SDK build tools from bundled ML Kit OCR model resources.

Preserve the baseline model and reports under a clearly identified historical directory or tag. New code, model, policy, and dependency versions need new identities. Do not silently rewrite 0.1.0 evidence to make it appear that a newer pipeline produced the earlier results.

## 3. What the current project already implements

The source is a real native Android project using Kotlin, Compose, Material 3, ViewModel, Coroutines and Flow, Room, DataStore, and bundled Latin OCR. It contains typed, pasted, and shared text workflows, screenshot selection and review, local model scoring, lexical URL observations, optional summary-only history, Learn content, settings, and redacted report sharing.

The message model is character n-gram TF-IDF with multinomial logistic regression, temperature calibration, and direct Kotlin sparse inference. It is not a cloud API wrapper. The Android and Python model JSON bytes match. The model bundle is non-executable JSON rather than a Python pickle used on the phone.

History currently saves derived summaries and metadata only. It does not save raw message bodies, complete URLs, screenshot files, or free-form notes. Room is not encrypted, and the privacy document admits that metadata can still reveal usage. Preserve this minimal approach; do not introduce raw-content storage merely to justify adding an encryption library.

The source manifest removes Internet and network-state permissions and does not request inbox, contacts, camera, or broad gallery access. The archive includes merged-manifest reports. These are useful static evidence, but fresh-install offline OCR and device behavior remain unobserved.

The preview includes a build report claiming debug, test-APK, lint, and minified unsigned-release assembly; host JVM golden parity; and compiled instrumentation cases. These archived claims are not equivalent to a fresh build or executed device tests. Preserve them as archived evidence and add independently executed results with dates and environments.

## 4. Independent checks performed during this audit

The archive passed ZIP integrity checking and safe extraction. The two APK digests matched their inventory. Both APKs contained model bytes matching the checked-in Android model asset and the reference bundle.

The delivered Python suite was executed in Python 3.12.14 with NumPy 2.3.5, SciPy 1.17.0, scikit-learn 1.8.0, pandas 2.2.3, and pytest 9.0.3. The result was twelve passed and one skipped. The skipped test requires reference.joblib, a private training artifact deliberately absent from the package. The earlier thirteen-pass report describes the original developer environment, not a fresh public clone.

The project validator returned exit code zero, but Git reported that the extracted directory was not a repository. Code inspection shows that the validator then substitutes an empty file list and still announces distribution-candidate validation. This is a real fail-open condition for one scan stage. Its other bundle and partition checks did run.

Synthetic behavioral probes reproduced the global context-suppression problem described below. No live scam websites, real private messages, external victim systems, or user inbox exports were used. The probes did not retrain the model or reopen the frozen final-test data.

No Android SDK, adb, javac, physical phone, or usable Android test runtime was available for this audit. Consequently, Android builds, phone screenshots, on-device benchmarks, and instrumentation execution were not independently repeated. The new implementation agent must resolve those gates where its environment permits.

## 5. Priority finding: global suppression of secret-request warnings

Inspect ml/src/scamsentry_ml/contracts.py, especially observations, and the equivalent Rules.inspect implementation in android/core-analysis. The current code sets a message-wide quoted flag when any of several phrases occur, including example, educational, quoted, never share, and do not share. If that flag is present anywhere, the secret_request observation is suppressed everywhere.

The following fictional reference probes reproduced the defect:

| Input scenario | Secret-request observation | Python policy result |
|---|---|---|
| “Please send your OTP to confirm the account.” | Present | Higher |
| The same request followed by “For example, use the form.” | Absent | Lower |
| “Never share your OTP. Please send your password now.” | Absent | Lower |
| “Do not share this notice. Send your OTP now.” | Absent | Lower |

These are reference-pipeline results. The same global suppression condition exists in Kotlin source, but the packaged app was not executed during this audit. Do not describe this as an observed physical-phone result.

Replace the global flag with bounded, local context handling. Identify each request span, its containing clause or sentence, and relevant nearby negation or quotation boundaries. A warning sentence must not neutralize a separate affirmative request. The word example in an unrelated clause must not disable a concrete observation.

Keep explanations restrained. Detecting a request for a secret supports a warning about that request; it does not prove the sender's identity or intent. Preserve benign educational and negated examples without letting them become a universal bypass.

Create regression tests before changing behavior. Add positive, negative, mixed-context, multi-sentence, quoted, punctuation, Unicode, and line-break cases in both Python and Kotlin. Freeze a new observation/policy version and report the behavioral improvement separately from message-model accuracy.

## 6. Priority finding: validation can report success after skipping a scan

Inspect scripts/validate_project.py. Its Git listing operation catches a failed subprocess and continues with an empty list. This is why a ZIP extraction can receive an overall “distribution candidates validated” message without a real distribution-file scan.

Implement explicit validation stages with separate results. Bundle integrity, source manifest checks, partition checks, filesystem scanning, tracked-file scanning, secret detection, and release packaging must report pass, fail, skipped, or not applicable independently. A skipped safety-relevant stage must not be included in an all-passed statement.

Support two legitimate modes. Repository mode inspects tracked and relevant untracked files using Git. Archive mode scans the actual extracted tree with an explicit allowlist and exclusion policy. Both modes should reject dangerous artifacts and unexpected paths. Record which mode ran.

Outside Git, either perform the complete filesystem-based scan or fail clearly with an actionable message. Do not require the owner to initialize Git merely to conceal a missing scan. The validator should be useful before first publication and after release download.

Add tests using isolated temporary directories: a clean tree, a forbidden raw-data path, a private-key fixture, a fake token pattern, a keystore-like file, a symlink escape, and a non-Git extraction. Fixtures must be synthetic and harmless. Verify exit codes and stage summaries.

Generate machine-readable validation output. The public README and gate report should summarize executed stages accurately. Do not advertise a green security badge merely because one script returned zero.

## 7. Important model-selection finding

The archived development comparison reports word_lr macro-F1 about 0.921, compared with about 0.900 for the deployed character LR C2. The training code deliberately restricts automatic deployment selection to candidates whose names start with char_lr. That restriction was documented as a portability and maintenance choice.

Preserve that explanation, but investigate it rather than assuming the deployed model is the overall best candidate. A word-feature linear model can be implemented on the phone if tokenization, vocabulary, weighting, and calibration are specified and tested carefully. A hybrid word-and-character model is also a justified research candidate.

Do not call 0.921 a new final-test score. It is a saved development result for another feature family. Do not compare it directly with the deployed model's 0.904 final-test macro-F1 as if they were measurements on the same partition.

Evaluate a small, explicit candidate set: the archived baseline, an improved character model, a word model, a hybrid linear model, and at most one compact neural alternative if resources and data justify it. Compare quality, calibration, robustness, memory, latency, model size, and export complexity.

The archived neural comparison is a character-TF-IDF MLP, not a character CNN or a deployed transformer. Its best development macro-F1 is about 0.862, and no mobile export is claimed. Do not relabel it in a README to make the project sound more advanced.

Choose the v2 champion through a preregistered multi-criteria decision. A more complicated model must earn its place. Preserve a fallback to the known baseline until the new bundle passes parity and compatibility checks.

## 8. Important robustness finding and required research response

The frozen historical report gives clean macro-F1 about 0.904 and severe o-to-zero/l-to-one substitution macro-F1 about 0.397. Zero-width insertion gives about 0.859. The substitution experiment is a controlled text perturbation, not measured OCR quality.

The current minimizer replaces numeric sequences before model features. That behavior can interact badly with obfuscated words and OCR errors. Investigate the mechanism using development examples and independently authored fixtures. Do not globally replace every digit with a letter: amounts, OTPs, phone-like tokens, and URLs carry different meanings.

Create separate processing views. Preserve original text for review and observations. Keep a privacy-minimized model view. If a carefully bounded deobfuscation view is useful, version it, test it, and expose disagreements rather than silently rewriting the user's message.

Use training-only augmentation with recorded parent groups. Generate or curate realistic OCR and character-noise variants after partition assignment. Do not use the already observed final-test perturbations as a new hidden tuning set while continuing to call that test untouched.

Evaluate at several noise severities, including meaning-preserving and meaning-changing cases reviewed separately. Test benign numeric content so stronger obfuscation handling does not create excessive warnings. Include coverage and uncertainty, because robust behavior may involve declining to overinterpret heavily damaged text.

Set improvement targets before experiments, report actual results, and retain failures. No percentage is guaranteed. If the model cannot generalize reliably, improve the review workflow and supported-scope messaging while continuing data-quality work.

## 9. Important parity and policy-orchestration finding

The exported golden suite compares model math and the core Policy.decide function. The actual Android ViewModel adds further behavior: zero-active-feature handling, standalone-URL detection, short-OCR checks, and a forty-link limit. Python evaluation does not apply exactly the same complete orchestration.

A synthetic gibberish reference probe had zero active features and a very high ordinary-class probability driven by the intercept. Python's core policy returned lower. The Android ViewModel already detects zero features and passes a quality flag, so do not report this as an existing Android lower-risk failure. The problem is divergence between evaluated and app-level orchestration.

Move input-quality, standalone-URL, feature-coverage, and link-limit decisions into an explicit versioned analysis contract shared conceptually across Python and Kotlin. Keep the Android shell responsible for platform resources and UI, not hidden inference-policy changes.

Generate end-to-end golden fixtures from the canonical analysis orchestrator. Test raw text through parsing, normalization, model inference, observations, policy, limitations, and result schema. Continue testing numerical model parity separately.

For each fixture, record whether it validates model estimates, observations, product outcome, or all three. Avoid claiming complete pipeline parity from twenty-four coefficient-scoring examples. Include no-feature inputs, short text, standalone addresses, multiple addresses, unsupported scripts, low-quality OCR, model unavailability, and cancellation boundaries.

The new evaluation report must distinguish model metrics from complete application policy behavior. Evaluate original reviewed text for rules and URLs and the minimized view for model features. The old policy results were computed from minimized corpus text and cannot establish the new app's end-to-end warning rate.

## 10. URL inspection improvements

Preserve the offline text-only boundary. Do not fetch supplied URLs, follow redirects, perform DNS lookups, or load WebViews as a side effect of analysis. A future optional reputation service requires a separate decision; it is not necessary for this upgrade.

The Kotlin source lowercases the scheme for validation but compares the original scheme with lowercase http when adding an unencrypted-scheme observation. Python's urlsplit normalizes the scheme. Add a shared uppercase-HTTP fixture and make scheme handling explicit. This is a static code finding pending a newly executed Kotlin regression.

Bring extraction limits, malformed-input handling, host normalization, and code-point limits into parity. Python currently has no corresponding forty-address truncation in its helper. A limit must produce an explicit quality or partial-analysis state in both implementations.

Add local tests for user-info deception, multiple at-signs, ports, bracketed IPv6, IP literals, Unicode names, percent encoding, trailing punctuation, backslashes, invisible characters, missing schemes, and unsupported schemes. Record when parsers disagree and choose a conservative documented interpretation.

Consider a bundled Public Suffix List if registrable-domain display materially improves the product. Version and attribute the asset. An actual host and a registrable domain answer different questions; never label either as verified brand ownership.

Keep a learned URL classifier optional. If added, train only on features available from the supplied URL and bundled assets. Never fill unavailable webpage-derived fields with zeros while presenting the original model's benchmark score.

## 11. Privacy, history, and export refinements

Retain metadata-only, manual, opt-in history by default. The current schema includes identifiers, timestamps, risk categories, summaries, model and policy versions, and input source. It contains no raw message or image fields. Document that metadata itself may be sensitive.

The DAO query displays the latest two hundred entries but does not prune older database rows. Decide whether to add an actual row-retention bound, age-based retention, or a clear explanation that the display limit is not a storage cap. If adding retention, enforce it transactionally and test the real database count.

Keep save/delete/privacy changes serialized. Preserve the queued-save recheck after history is disabled. Add tests for rapid toggling, concurrent save and clear, migration, storage errors, and process recreation.

Fix the editor's static “History is off until you enable it” copy when history is already enabled. Derive the message from actual settings. Review all privacy copy for current-state accuracy.

Export currently reuses one cache filename and deletes existing export files before writing the next. Test overlapping share requests and delayed recipient reads. Prefer uniquely named redacted exports with bounded retention and explicit access grants. Show a useful error if sharing fails instead of swallowing the failure and dismissing the preview.

Do not expand retention or introduce raw-content persistence as a cosmetic upgrade. If raw saved messages become a separately authorized future feature, review encryption, backup, deletion, and migration then. Metadata-only operation remains a strong design choice.

## 12. OCR correctness, memory ownership, and image handling

The existing reader enforces content-URI, MIME, byte, dimension, and downsampling limits and uses bundled Latin OCR. Preserve those safeguards. Improve them through tests and profiling rather than replacing the entire subsystem without evidence.

Review cancellation ownership carefully. Native OCR tasks may continue after coroutine cancellation. The current implementation uses NonCancellable while waiting for the native task and returns a bitmap draft through a cancellable context. Audit every exit path for bitmap cleanup, particularly cancellation between native completion and delivery to the ViewModel. This is a potential lifecycle defect identified by code inspection, not a reproduced device memory leak.

Assign one clear owner to each bitmap and resource handle. Ensure cleanup for success, failure, replacement, cancellation, navigation, and ViewModel disposal. Avoid manual recycling while Compose or native OCR still uses an image. A safer ownership abstraction is preferable to scattered recycle calls.

Test all relevant EXIF orientations, including mirrored forms. The current reader obtains rotation degrees; rotation alone does not represent every orientation transformation. Verify the preview and OCR text against the same intended orientation.

If adding crop, rotate, or contrast assistance, keep the interaction understandable and preserve the original source. Processing must remain local. Never present heuristic image quality as a calibrated OCR certainty percentage.

Use rendered fictional screenshots at several sizes, densities, fonts, contrasts, and noise levels. Report extraction error separately from classifier error. An editable review screen reduces risk but does not prove transcription correctness.

## 13. Updated stack: retain the native foundation

Keep Kotlin and Jetpack Compose as the product foundation. The archive already has a functioning native architecture and packaged model. A wholesale migration to Flutter, React Native, a web wrapper, or a cloud-first stack would spend effort without addressing the reproduced defects.

Use this table as a dated upgrade investigation, not a claim that every listed combination has already been built together:

| Component | Current archive | Verified candidate or decision on 16 November 2026 |
|---|---|---|
| Android Gradle Plugin | 9.1.1 | Investigate stable 9.5.1 using the 9.5 compatibility notes |
| Gradle wrapper | 9.3.1 | AGP 9.5 requires at least 9.6.0 |
| Kotlin and Compose compiler plug-in | 2.4.10 | Investigate stable Kotlin 2.4.21 |
| Compose runtime/UI | BOM 2026.09.00; recorded resolution 1.12.1 | Official stable runtime/UI is 1.12.1; do not force an unnecessary migration |
| Compose Material 3 | BOM-managed | Stable 1.4.0; 1.5.0-rc01 remains a release candidate |
| Room | 2.8.3 | Investigate stable 2.8.5 |
| DataStore | 1.1.7 | Investigate stable 1.3.0 and migration implications |
| Activity | 1.11.0 | Investigate stable 1.13.0 |
| Lifecycle | 2.9.4 | Investigate stable 2.11.0 with its build prerequisites |
| Navigation | Navigation Compose 2.9.5 | Keep initially; Navigation 3 stable 1.2.1 is a separate optional spike |
| OCR | Bundled Latin ML Kit 16.0.1 | Retain unless official compatibility or measured quality justifies replacement |
| Inference | Direct Kotlin linear model | Retain baseline; extend for justified hybrid or compact-model research |
| Java toolchain | 21 | Preserve unless a verified compatibility requirement changes it |
| SDK | Compile 37, target 36, minimum 26 | Review separately; do not raise target merely to remove a warning |

Official references are listed at the end. Recheck current releases when execution occurs. Coordinate Kotlin, built-in Android Kotlin behavior, KSP, AGP, Gradle, and compiler plug-ins. Do not assume an old KSP version is compatible with every new compiler.

Make the build reproducible before updating. Then change a coherent dependency group in a dedicated branch or commit, run the relevant checks, and retain rollback information. Do not upgrade the model-training environment and Android toolchain in the same unreviewable change.

## 14. Dependency upgrade acceptance and build reproducibility

Start by recording the existing dependency graph, lockfiles, wrapper digest, SDK packages, JDK, and build outputs. The existing versions are an important fallback because archived builds reportedly succeeded. Capture a fresh baseline build where tools are available.

Regenerate wrapper scripts through the supported Gradle wrapper process if replacing the abbreviated custom scripts. Preserve the distribution checksum and verify wrapper provenance. Ensure executable permissions are retained in Git and source archives for Unix-like systems. Test Windows invocation independently.

Use stable versions and explicit locks. Avoid plus selectors, latest.release, unreviewed alpha components, and copying prerelease coordinates from the first code snippet on a documentation page. Some official pages show alpha examples while also listing a different stable release; select deliberately.

The updated AGP group must pass core-analysis tests, app unit tests, lint, debug assembly, instrumentation-test APK assembly, minified release assembly, and package inspection. If a dependency changes runtime behavior, require device verification or retain a clearly marked blocked gate.

Resolve lint warnings by assessing their meaning. Update warnings are prompts for review, not automatic instructions to migrate. Do not suppress a genuine target, API, or accessibility problem globally just to obtain a clean report. Record justified remaining warnings.

Keep Python baseline reproduction isolated. A dependency upgrade that changes training results needs a new run environment and manifest. Loading the archived JSON bundle can remain independent of retraining, which protects the demonstration from unnecessary setup complexity.

## 15. Research protocol for v2 models

Preserve ss-group-v1 and its test-access history. The old final test and its perturbation results are now known to the developers. They can remain legacy regression measurements, but must not be described as a fresh blind evaluation of a model designed in response to those failures.

Create a v2 protocol document before tuning. Identify eligible source versions, global grouping rules, raw and minimized text views, training/development/calibration roles, prospective test acquisition, perturbation families, metrics, operating costs, and release criteria.

Use the existing training and development partitions for controlled exploratory comparisons where appropriate. A new random split of the same already inspected corpus is not independent contemporary validation. State exactly what each experiment can establish.

A fresh external or prospective holdout requires eligible data, compatible labels, privacy review, overlap checking, and enough independent examples. If such a holdout cannot be obtained without a permission-dependent source, proceed with honest research comparisons and preserve the deployed baseline until the replacement is defensible.

Do not block all app improvement on obtaining new data. Rule fixes, parity improvements, packaging, tests, interface refinement, and GitHub publication can progress independently. Keep model promotion as its own decision.

Make all model-selection reports identify which data influenced the architecture, normalization, thresholds, and augmentation choices. This prevents a cosmetic protocol rename from concealing adaptation to previously observed failures.

## 16. Data expansion and provenance safeguards

Retain the current primary Mendeley source registry and original attribution. The archive reports 5,971 initial records and 5,740 usable records after conflict quarantine and duplicate reduction. Do not merge new sources by copying arbitrary labelled messages into that table.

The archive's auxiliary audit reports 5,200 exact normalized overlaps among 5,574 UCI records. UCI is marked conditional because of bundled older terms and an appreciated notification request. Preserve that status until the actual terms are reviewed coherently. Do not reinterpret an appreciated notification as proof of a mandatory email, and do not use the source before resolving the project's recorded concern.

Review every new source at the original publisher and file level. Record the licence, version, attribution, third-party provenance, row counts, label meaning, privacy transformations, and source overlap. A hosting-platform badge alone is insufficient.

Keep promotional spam distinct from fraud-like messages. A binary spam dataset may support an auxiliary task, but its spam labels must not automatically become smishing. Resolve contradictory duplicate labels and document quarantine decisions.

Target actual gaps: contemporary impersonation patterns, legitimate transactional messages, difficult benign urgency, independently authored educational counterexamples, OCR mistakes, and supported-language coverage. Quantity should be reported in independent groups as well as rows.

Synthetic examples are useful for regressions, augmentation, and demonstrations. They do not establish real-world protection. Keep parent scenarios and generation lineage, split before generating variants, and label synthetic-only evaluation clearly. No private user messages or saved history may enter training automatically.

## 17. Candidate model implementation details

Implement a well-specified word-feature baseline using a portable tokenizer. Define Unicode normalization, supported character classes, token boundaries, minimum token length, typed placeholders, n-gram composition, vocabulary ordering, TF scaling, IDF, block normalization, coefficients, and calibration. Test the exact same contract in Python and Kotlin.

For a hybrid linear candidate, compare word and character feature blocks with explicit weighting. Decide whether each block is normalized separately and whether a final concatenated normalization is applied. Export that arrangement precisely. Do not introduce an undocumented second L2 normalization during deployment.

Investigate robustness-aware character training with limited, realistic augmentation. Include original examples, controlled noisy variants, and challenging benign controls. Avoid training a system that simply flags all misspelled text or all digit-containing words.

A compact character CNN or eligible small encoder can be a research comparison if the compute budget and rights review permit. Choose one mobile runtime only after conversion and operator checks. ONNX Runtime or LiteRT can be considered for a neural winner; neither is required for a linear model.

Run bounded searches and record seeds, independent group counts, stopping criteria, wall time, memory, and model size. Preserve failed experiments. Repeated tuning of a small development set can overfit even if the final-test file remains untouched.

Select the champion through a documented utility and deployment review. A candidate must improve a meaningful trade-off, pass class-specific and robustness checks, and remain understandable to the owner. Do not replace the model merely because a newer architecture has a more impressive name.

## 18. Calibration, operating policy, and limited-support behavior

Refit calibration only under the declared v2 protocol. Keep model probabilities separate from rule observations and product categories. A number from softmax or temperature scaling is not a probability that the sender is authentic.

The archived policy uses confidence .62 and margin .18 from configuration. Investigate these operating points using a documented development procedure rather than treating them as universal constants. Consider warning cost, missed smishing indicators, ordinary-message nuisance warnings, and abstention coverage.

Feature coverage should be an input-quality indicator. Zero features already triggers a limited result in Android; extend tests to sparse, repetitive, nonsensical, and unfamiliar text. A high intercept-driven estimate on weak evidence must not become persuasive reassurance.

Language support requires more than ASCII letters. Preserve the current honest limitation and evaluate a lightweight local language-support mechanism only if it can be calibrated and maintained. Do not claim Hindi, Marathi, or Hinglish support from English-script acceptance or a multilingual-looking UI.

Define precedence explicitly. Concrete observed requests should remain visible even when statistical interpretation is unavailable. A limited model state should not erase useful safety observations, and a strong rule should not be rewritten as a calibrated combined fraud probability.

Report selective coverage, accepted error, abstained error, and class-specific warning rates. Use group-aware uncertainty intervals when appropriate. A model that appears better only because it refuses most useful inputs needs that reduced coverage shown clearly.

## 19. Efficiency improvements in the model runtime

Profile before selecting a new storage format. The current JSON bundle is about 2.37 MB and uses float64. Its size alone does not prove a performance problem. Measure cold parsing, allocation, loaded heap, feature extraction, scoring, contributions, and complete user-visible latency.

The current LinearModel retains the parsed JSONObject and reconstructs an inverse vocabulary for explanations. Investigate discarding unnecessary parsed structures after validation, caching immutable inverse lookups, and avoiding duplicate weight extraction for prediction and explanation.

Represent active features efficiently and bound intermediate allocations. Avoid building enormous collections when streaming counts is sufficient. Preserve Unicode correctness and deterministic results. Optimization must not change normalization or model semantics silently.

Evaluate float32 or a compact non-executable binary format only through a new bundle schema and parity study. Include numeric tolerances, threshold-adjacent cases, calibration effects, and actual memory savings. A smaller file that changes warning behavior unexpectedly is not an automatic improvement.

Keep model loading off the main thread and cache a validated immutable engine. Separate recoverable load errors from corrupt-bundle errors and make retry behavior deliberate. Do not repeatedly parse the model for every message.

Measure the final candidate in release mode on a phone. Desktop microsecond measurements and host JVM parity cannot establish phone responsiveness. Store both component timings and complete-pipeline timings so optimization targets the actual bottleneck.

## 20. APK size and ABI-specific distribution

The unsigned universal release is 45,741,083 bytes. Its native libraries cover arm64-v8a, armeabi-v7a, x86, and x86_64. Most of the native footprint comes from the bundled OCR pipeline. Retaining desktop-emulator architectures in every phone download adds avoidable weight.

Create an arm64-v8a installable research artifact for compatible phones, an x86_64 test artifact for emulators where needed, and an optional universal fallback if useful. Clearly label compatibility. Do not remove support for an intended physical device before inspecting its ABI.

For GitHub sideloading, provide actual installable APK files. An AAB alone is not a direct replacement for a downloadable APK. A future store-distribution path may use an app bundle separately.

Measure actual artifact sizes after splitting and signing. A useful provisional goal is an arm64 release below roughly twenty megabytes, but this is a hypothesis from the observed package composition, not a guaranteed result. Retain OCR resources needed for first-use offline operation.

The APK inspection found the 64-bit native libraries at the relevant alignment level, while some 32-bit OCR libraries have 4 KB PT_LOAD alignment. Do not report that as a reproduced 64-bit compatibility failure. Verify ZIP alignment, ELF alignment, and actual page-size runtime behavior separately for the distributed ABIs.

Preserve checksums, signing status, source commit, model digest, ABI, minimum SDK, and release channel for every artifact. Never label an unsigned release as directly installable. Keep owner-managed signing secrets outside the repository and packaged source.

## 21. Baseline Profiles, Macrobenchmark, and performance evidence

Add a benchmark module only when it supports real measurements. Define representative startup, text analysis, screenshot review, result scrolling, history, and navigation journeys. Separate cold and warm paths and record actual device and build conditions.

Baseline Profiles can improve execution of common code paths, but generate them from exercised journeys and verify their effect. Do not check in an invented profile or claim a fixed speedup based on another app's example. The official performance guidance is a reference, not this project's measurement.

Use Macrobenchmark for app-level startup and scrolling where appropriate. Add focused component benchmarks for model parsing and inference if they answer a specific question. Avoid mixing debug-build latency with release results.

Record median and tail latency, sample count, warm-up procedure, cancellation behavior, memory observations, and APK size. Explain thermal, background-load, and device limitations. A small test does not justify precise battery-life claims.

If the execution environment cannot run a device, prepare the benchmark implementation and commands, mark results pending, and continue other work. Do not spend hours repeating software-only emulator attempts already known to be unreliable without diagnosing the missing capability.

Optimize only where the measured user experience benefits. Cropping and image downsampling may matter more than a tiny scoring optimization. The strongest efficiency story connects a measured bottleneck, a concrete change, and a repeated measurement.

## 22. Modern architecture without unnecessary fragmentation

Refactor the large ui/App.kt into coherent screen and reusable-component files. Keep a central design system, typed UI state, domain contracts, and analysis orchestration. Avoid a proliferation of Gradle modules merely to make the directory tree look advanced.

Introduce interfaces for model loading, OCR, settings, history, clock, and export where they allow meaningful tests. Constructor injection and simple factories may be sufficient. Hilt or another dependency-injection framework is optional if the complexity and team understanding justify it.

Move Android-independent analysis behavior into core-analysis. Keep platform-specific bitmap, content-URI, lifecycle, and sharing concerns in the Android layer. The Python reference should match the domain contract, not duplicate ad hoc ViewModel branches.

Use immutable results with explicit engine, policy, normalizer, and schema versions. Avoid hard-coded version strings in result UI and report exports. A changed model must update metadata and metrics consistently.

Keep navigation state minimal and free of raw sensitive input when saved-state mechanisms persist it. The current privacy contract intentionally discards drafts after process death; preserve that unless the owner explicitly requests a new retention behavior.

Review concurrency by request identifier and input version. Test new input arriving during model loading, OCR, or export. A result for an earlier input must never appear as the interpretation of newer text.

## 23. Navigation and adaptive layout upgrade

The existing app uses Navigation Compose and a manually chosen wide-screen breakpoint. Retain it initially while fixing correctness. Navigation 3 is a modern optional candidate, not a mandatory rewrite.

If testing Navigation 3, use the verified stable line and inspect its state, entry ownership, predictive-back, lifecycle, and adaptive behavior. Do not copy an alpha dependency from documentation examples unintentionally. Prove that sharing, rotation, back behavior, and privacy boundaries survive the migration.

Adopt window-size classes and meaningful adaptive patterns where they improve layout. Compact phones can keep labelled bottom navigation. Larger widths can use a rail and a useful two-pane editor/result or list/detail arrangement, with controls reachable at large font sizes.

Avoid a wide layout that merely adds a decorative sidebar while the actual task remains awkward. Test landscape, split-screen, keyboard-visible editing, narrow windows, and large fonts. Adapt to available window space rather than assuming a device category.

History rendering can use a lazy list for scalable scrolling, but that does not replace database retention. Preserve scroll position appropriately without persisting raw message drafts. Navigation should restore destinations predictably.

Add regression tests before and after any navigation migration. If no measurable user or maintenance benefit appears, keep the current stable navigation and record the decision. A well-tested existing library is a valid final choice.

## 24. Visual refinement and interaction quality

Preserve the established dark navy-black palette, blue actions, purple explanations, red concerns, amber uncertainty, and green lower observed risk. Use the existing Space Grotesk and Inter fonts with their notices. The owner wants a distinctive, refined app; the visual direction should evolve coherently.

Add a subtle technical grid or depth treatment only where it supports the hero or empty state. Keep text on solid readable surfaces. Avoid a permanent hacker animation, fake scan delay, flashing warnings, or decorative statistics.

Improve hierarchy on results. Lead with the outcome and most actionable observation, then separate model interpretation, URL inspection, limitations, and technical detail. Do not bury a direct credential request under a long list of generic disclaimers.

Use deliberate spacing, consistent control shapes, restrained state transitions, and accessible semantics. Respect system motion preferences. Test actual text contrast and controls, not only static token combinations.

Replace hard-coded product strings with resources where practical. This improves consistency and future localization; it does not establish multilingual model support. Fix state-dependent copy and ensure empty, loading, error, cancelled, and limited-support states all look intentional.

Capture actual packaged-app screenshots only after observing the UI. If no device runtime is available, label design previews honestly and keep runtime screenshots pending. Do not present fabricated phone renders as evidence that the app ran.

## 25. Testing that works from the distributed source

Replace the public-clone reference-test gap with meaningful available tests. Keep the private production reference checkpoint excluded if it is not intended for distribution. Add a tiny synthetic training fixture that fits a real scikit-learn model, exports it through the production exporter, and compares its decision function with the portable runtime.

That fixture verifies implementation consistency, not production accuracy. Keep the actual production bundle's golden tests and metadata checks separately. Do not attach the synthetic model's metrics to the app.

Expand Python and Kotlin tests around the reproduced context bug, malformed URLs, weak-feature inputs, limits, and complete policy orchestration. Use a common safe fixture schema so the two implementations cannot quietly evolve different expectations.

Test bundle schema validation for class order, feature contract, policy version, thresholds, array sizes, finite numbers, positive IDF, calibration bounds, missing assets, and unsupported versions. Python and Kotlin should reject incompatible bundles consistently.

The current exporter hard-codes model identifiers and a baseline evaluation path even when called with a different run directory. Replace those constants with validated run metadata. Export to a versioned staging destination first, then promote deliberately. A research experiment must not overwrite the app's active bundle automatically.

Use regression tests that protect real behavior. Do not inflate counts with assertions that merely repeat constants or mirror implementation statements. Report passed, failed, and skipped tests separately in public documentation.

## 26. Device verification and realistic environment handling

Establish the available Android toolchain and device access early. Check Android Studio or command-line SDK components, JDK, adb, hardware acceleration, and a connected authorized device. Do not assume a complete SDK because an APK exists.

The previous environment attempted software-only emulator boots without reaching an installed app. Diagnose virtualization and acceleration before repeating that approach. Use bounded attempts, record failure reasons, and pivot to an available physical device or suitable CI runner.

Run the existing instrumentation cases and add the new regression journeys. Device tests should cover text input, sharing from a cold and warm start, screenshot selection, OCR review, cancellation, result accuracy relative to fixtures, history controls, export, and model failure behavior.

Use the actual release variant for relevant verification. R8, shrinking, native packaging, and signing can change behavior. Keep debug, instrumented, and release results distinct.

For first-use offline testing, install the intended APK, use a fresh app state, remove connectivity in a documented way, then exercise text and screenshot analysis. Inspect permissions and observed communication separately. A bundled model file is not proof that the entire first-use workflow works.

When device access is unavailable, finish source fixes, tests, build preparation, and GitHub publication with honest pending gates. Provide a precise Windows/Android checklist for the owner. Never replace missing runtime evidence with an invented screenshot or claim that compiled tests were executed.

## 27. CI that proves the public checkout works

Create CI jobs that run on the actual repository. The existing workflow is prepared source only; no remote passing status was established in the input archive. Publish badges only after observing real workflow results.

Use independent jobs for Python contracts, Android host tests, lint/build, package validation, and device instrumentation where supported. Include instrumentation-test APK assembly explicitly. The current workflow builds app variants but does not itself run the compiled Android cases.

Pin actions to reviewed immutable references where feasible and use minimum token permissions. Do not expose release credentials to untrusted pull requests. Separate artifact creation from publication.

Make validation useful in both Git and archive contexts. Run an archive-download smoke test so the same deliverable a faculty reviewer receives can be inspected without hidden repository state. Check executable wrapper permissions and platform-specific scripts.

Use an emulator or managed-device job only after confirming the runner supports it. Record boot, installation, execution, and timeout stages. A skipped job should not become a green device-verified badge. Physical-device timing remains a separate requirement even if emulator functional tests pass.

Upload logs and reports that contain no raw private records or secrets. Store useful failure artifacts, including safe screenshots and test output when authorized. Keep retention reasonable and ensure a rerun does not overwrite the historical evidence associated with a released version.

## 28. Security and data-rights release review

Update the threat model for the actual v2 changes. The reproduced context-suppression defect is a policy integrity concern. New model formats, optional navigation changes, image editing, or export files create additional verification tasks.

Preserve least privilege and the offline core. No direct inbox scanning, notification listener, accessibility service, contacts access, or location is required for this project. Do not add a cloud language model simply to generate more elaborate explanations.

Inspect source, merged manifests, APK assets, dependencies, workflow permissions, release packaging, and tracked files. A clean Python dependency audit does not cover Android native libraries or prove absence of unknown vulnerabilities.

Review the dataset registry rather than assuming the original generic prompt preapproves every listed source. The delivered implementation intentionally marks some candidates conditional or excluded. Preserve those decisions until new evidence supports a change.

Keep original code licensing separate from source-data-derived assets, fonts, SDK terms, and copied examples. Retain attribution in the repository and app. Do not imply endorsement by banks, universities, publishers, or security organizations.

Never publish keystores, passwords, access tokens, private checkpoints containing unintended content, raw/processed private data, or uncontrolled notebook outputs. If a genuine secret is found, stop the relevant publication step, contain it, and document the concrete remediation. Continue unrelated safe work.

## 29. Create and initialize the exact GitHub repository

At execution time, verify the authenticated owner and inspect whether DevenGaikwad/ScamSentry exists. Do not substitute an organization or a second account. The requested name and capitalization are ScamSentry.

Prefer a connected GitHub capability sufficient for repository creation and source operations. If creation is not exposed, use an already authenticated GitHub CLI or another authorized GitHub interface. Do not print tokens, search files for credentials, or create a repository under an unrelated account because authentication is convenient there.

If the repository is absent and access is adequate, create it publicly with a concise accurate description. If it exists, inspect its branch, files, visibility, and history before integrating. Never delete and recreate it, force-push over unknown work, or create ScamSentry-v2 merely to avoid a collision.

A suitable CLI creation command, after account and absence checks, is:

```text
gh repo create DevenGaikwad/ScamSentry --public --source=. --remote=origin --push --description "Offline Android research assistant for suspicious messages and links"
```

Execute that only from the reviewed project root with a valid local repository and intended commits. If origin already exists, inspect it and use a safe matching procedure rather than overwriting it blindly. Do not use shell tracing that can expose authentication material.

After publication, verify the canonical URL, owner, visibility, default branch, pushed commit, and presence of required files. Report success only from the returned or observed repository state. If authentication is genuinely unavailable, finish the concrete local package and identify the specific missing capability without inventing a public link.

## 30. Repository content, release assets, and professional presentation

The repository root should contain the product README, licence, notices, Android project, ML scripts and safe fixtures, source manifests, documentation, CI, and original visual assets. Keep local SDKs, Gradle caches, environments, build output trees, private data, signing settings, and secrets out of source.

Do not add the entire uploaded ZIP or both universal APKs to ordinary Git history. Attach reviewed binaries to GitHub Releases where appropriate. Track small model assets only with a documented reproducibility and size decision. Avoid unnecessary duplicates while preserving the archived baseline identity.

Create a clear README with actual screenshots when available, installation, supported scope, local analysis architecture, measured evidence, privacy behavior, source attribution, limitations, and links to documentation. Show pending device gates openly when they remain unresolved.

Use topics that accurately describe the project, such as android, kotlin, jetpack-compose, cybersecurity, smishing-detection, on-device-ml, and machine-learning. Do not claim awards, endorsements, user counts, ratings, certification, or current-campaign protection without evidence.

Prepare a research-preview release after the code and checks are concrete. Include an ABI-labelled installable demo APK when available, checksum file, model manifest, release notes, and source-to-artifact inventory. Keep unsigned builds clearly labelled. A missing permanent signing identity should not block public source publication.

Preserve honest authorship and contribution history. If importing from a ZIP, state that it is an import of the supplied preview. Do not manufacture prior commits or remove attribution to make the project appear wholly unrelated to its sources.

## 31. Signing, versioning, and release channels

Use a new application version, for example 0.2.0-preview with an increased Android versionCode, for the first audited improvement release. Model, policy, normalizer, bundle schema, and app versions must remain independently identifiable.

The app can use the archived 0.1.0 model while shipping a corrected policy and improved interface. Do not relabel its historical metrics as measurements of a different model. If a new model is promoted, attach its own evaluation and calibration records.

Use owner-managed production signing only when the identity and secure storage are established. Never commit a keystore or password. If a deliberate demo/debug signing identity is used for an academic prerelease, label it as such and explain update compatibility.

A final signed APK must be verified after signing and inspected for package identity, supported ABIs, minimum SDK, bundled model digest, and runtime behavior. Preserve checksums for the actual file users download, not an earlier unsigned intermediate.

Prepare rollback instructions. A failed new model or dependency update should allow restoration of a known-good source and bundle without erasing history. Do not build an insecure remote-update mechanism as a shortcut.

Use release notes that state concrete changes and remaining limits. “Fixed global warning suppression and added end-to-end fixtures” is more useful than “upgraded to a perfect AI engine.”

## 32. Academic and competition-level evidence

Build a project narrative around a useful complete workflow and an evaluated contribution. The strongest contribution can be robust local analysis with faithful observations, consistent desktop/mobile behavior, and a carefully tested privacy boundary. It does not require inventing a novel classifier.

For Mobile App Development, document native UI architecture, lifecycle handling, platform sharing, OCR integration, adaptive layouts, persistence, accessibility, and release packaging. For Cybersecurity, document threat modelling, the reproduced bypass and fix, parsing integrity, least privilege, robustness, and security verification.

Separate report outlines and viva notes can share the same codebase if faculty permit reuse. Do not invent academic approval, teammate details, signatures, or a grading guarantee.

Demonstrate an ordinary message, legitimate urgency, a direct secret request, mixed warning/request context, an OCR correction, a malformed or deceptive address, uncertainty, and history control. Use fictional content and reserved domains. Include a difficult case rather than only easy demonstrations.

Present model experiments with denominators, group isolation, calibration, robustness, and device budgets. Explain why the initial word model deserved investigation and why the final champion was selected. A technically honest negative result can demonstrate better engineering than an unsupported accuracy claim.

Package a short demo recording, real screenshots, architecture figure, reproducibility guide, model card, and failure analysis when those artifacts can be observed and created. Label every unavailable item pending. These make the project easier to review for a competition or résumé without inflating its claims.

## 33. Scope priorities and useful optional extensions

Priority zero is correctness and trustworthy verification: fix global suppression, fail-open validation, export metadata, and orchestration drift. Priority one is clean builds, public repository setup, device execution, packaging, and UI consistency. Priority two is model research, robust data expansion, and performance improvements.

Do not let optional work delay the corrected research preview. Complete an installable useful increment and publish the source with accurate gates. Continue research through separate branches or clearly identified commits.

Useful optional features include QR decoding from a user-selected image, a dedicated URL-inspection view, original educational practice scenarios, and a better explanation-inspection panel. Every feature needs a user benefit, privacy assessment, test plan, and release gate.

QR decoding must never auto-open a decoded URL or execute a payment intent. Show the decoded content and let the user review supported analysis. Camera scanning is a separate optional capability with its own permission and lifecycle checks.

Multilingual models, live threat feeds, account sync, remote explanations, and browser extensions are later research paths. Do not add them merely to make the stack longer. Their datasets, privacy implications, ongoing costs, and evaluation burdens require a concrete justification.

The goal is a stronger project that reduces the owner's workload across two courses. A maintainable native application with measured improvements is more useful than several half-finished frontends.

## 34. Milestones and concrete deliverables

### Milestone A: Preserve and establish the baseline

Verify input digests, inspect the workspace, preserve the historical bundle, record available tools, run the public-checkout tests, and create a v2 state document. Deliver a baseline audit with executed, archived, and blocked evidence clearly separated.

### Milestone B: Repair priority defects

Add failing regressions for global suppression and validator skip behavior, then fix them in both implementations where relevant. Unify policy orchestration and remove hard-coded export metadata. Deliver a coherent change set and updated tests before optional model tuning.

### Milestone C: Modernize safely

Evaluate stable dependency upgrades in compatibility groups, refactor UI boundaries, improve privacy copy and export handling, and add ABI-specific packaging. Deliver successful host/build checks and explicit device requirements.

### Milestone D: Publish the authorized repository

Create or safely continue DevenGaikwad/ScamSentry, push the reviewed source, run CI, and verify the remote repository. This milestone does not require pretending that every physical-device gate is complete. Publish a clearly labelled research preview.

### Milestone E: Improve the evaluated model

Execute the v2 research protocol with eligible data, word/character/hybrid comparisons, calibrated policy, realistic perturbations, and independent evidence where available. Preserve the baseline and promote a replacement only through documented criteria.

### Milestone F: Verify and present

Run real-device workflows, release tests, accessibility checks, and benchmarks. Capture actual screenshots, rehearse the demo, update the two course reports, and publish a truthful release package. Leave any unresolved optional work in a clear roadmap.

## 35. Resume protocol and evidence management

Maintain docs/PROJECT_STATE.md with completed work, exact commits, current versions, experiment identifiers, unresolved findings, device access, publication status, and next actions. Preserve a separate historical record for the original preview.

On resume, read state, inspect the actual Git tree and remote, and verify referenced artifacts. Do not repeat completed training or a failed emulator strategy without a reason. Preserve user changes and avoid resetting a working project.

Evidence must identify its environment and scope. Use categories such as reviewer-executed Python checks, archived host-build evidence, fresh Android build, emulator functional run, physical-device run, and unexecuted plan. A single “all tests pass” phrase is insufficient when a reference test is skipped and instrumentation is blocked.

Store raw private data outside public artifacts. Keep aggregate reports, safe fixtures, digests, and reproducibility instructions. Avoid printing real message text in logs or including it in model-review issues.

When blocked, continue independent authorized work. Missing physical access does not prevent a GitHub repository; missing new data does not prevent a policy fix; missing production signing does not prevent a clearly labelled source prerelease.

Report actual progress without making the owner approve routine choices repeatedly. Ask only for a genuinely missing input that materially affects authorized work, and prepare the dependent result as far as possible first.

## 36. Definition of completion for this follow-up

The follow-up succeeds when the reproduced priority defects are fixed with regressions, the baseline evidence remains traceable, the public checkout validates honestly, the native stack is maintained or upgraded through verified compatibility, and the exact requested GitHub repository exists with reviewed source.

A stronger model is a separate evidenced achievement. Report whether it improved clean performance, difficult inputs, calibration, coverage, latency, memory, and size. If no candidate earns promotion, retain the baseline and explain what was learned.

The app should have a polished, consistent, accessible interface and useful behavior for uncertainty and errors. Real screenshots and phone benchmarks require actual execution. Pending gates must remain visible rather than being erased to obtain a final label.

The final handoff should include the repository URL, pushed commit, CI status, installable artifact if available, signing/channel information, app/model/policy versions, release checksums, executed-test summary, and a concise list of remaining device or research limitations.

Do not end with only another plan when implementation is authorized. Carry out the concrete work, preserve evidence, and deliver the useful increment. Do not send dataset emails, fabricate perfection, or replace the application with an unrelated stack.

---

## Appendix A. Required regression and verification matrix

Translate this matrix into automated tests where possible and documented device checks where necessary. Expected categories must come from the written policy contract, not hand-coded exceptions for the exact demonstration wording.

| ID | Scenario | Required property |
|---|---|---|
| REG-01 | Direct affirmative OTP request | Concrete request observation remains visible |
| REG-02 | Same request plus unrelated “for example” | Unrelated phrase cannot suppress the observation |
| REG-03 | Warning sentence followed by affirmative password request | Analyze local contexts independently |
| REG-04 | “Do not share this notice” plus OTP request | Negation applies to its referent, not the entire message |
| REG-05 | Educational quotation of a request | Avoid unsupported assertion of an active request |
| REG-06 | Legitimate urgency | Urgency alone does not establish smishing |
| REG-07 | Zero-feature gibberish | Limited result consistently in reference and app |
| REG-08 | Very few active features | Development-validated quality handling |
| REG-09 | Bare standalone URL | Lexical URL scope, no fake message-risk percentage |
| REG-10 | Uppercase HTTP scheme | Same scheme observation in Python and Kotlin |
| REG-11 | User-info deception | Correct actual host and explanation |
| REG-12 | Unicode and punycode host | Consistent normalization without automatic fraud attribution |
| REG-13 | IPv6, ports, malformed authority | Bounded, conservative, matching parser behavior |
| REG-14 | Exactly forty and more than forty links | Explicit limit and truncation semantics |
| REG-15 | Corrupt model or unknown policy schema | Controlled unavailable state |
| REG-16 | New run exported | Identifier and evaluation metadata match that run |
| REG-17 | Non-Git source extraction | Real filesystem scan or explicit failure |
| REG-18 | Forbidden data or synthetic secret fixture | Validator rejects the relevant release candidate |
| REG-19 | Missing private reference checkpoint | Available consistency tests still run; skip is reported |
| REG-20 | Rapid repeated screenshot selections | No stale result or orphaned bitmap ownership |
| REG-21 | Cancellation around native OCR completion | Safe ownership and cleanup |
| REG-22 | All supported EXIF orientations | Preview and OCR agree on orientation |
| REG-23 | History off and queued save | No record survives a disabled-history save race |
| REG-24 | More than two hundred saved summaries | Actual retention behavior matches documentation |
| REG-25 | Concurrent exports and delayed recipient read | Unique bounded export lifecycle |
| REG-26 | History enabled while editor is visible | Copy reflects the actual setting |
| REG-27 | Release build, fresh offline install | Bundled text and OCR workflows actually execute |
| REG-28 | Large fonts and assistive reading | Readable, operable result and correction flow |
| REG-29 | Arm64 APK | Correct ABI, package, signature, model, and checksum |
| REG-30 | Public repository and CI | Actual owner/name/visibility/commit/status verified |

Keep model-math parity, observation parity, complete-analysis parity, and Android lifecycle tests as separate suites. A regression can pass in one suite while remaining unverified elsewhere. The gate report must preserve those distinctions.

## Appendix B. Performance and quality scorecard

Use a scorecard to guide improvements without turning aspirational targets into fabricated achievements. Establish the device, input workload, and protocol before choosing final limits. Report baseline, candidate, difference, uncertainty, and whether the measurement is valid for the intended claim.

For priority correctness, require all explicit context and validator regressions to pass. Expand beyond the exact reproduced strings through controlled variations and independent examples. Passing four hard-coded examples is insufficient evidence of a general fix.

For model research, compare candidates on the same permitted development protocol. A reasonable investigation goal is to preserve clean quality while materially improving noise robustness and ordinary-message warning burden. Decide a non-inferiority margin and robustness objective before final measurement. Do not optimize the trade-off after inspecting a prospective holdout.

Report smishing precision and recall with counts, promotional confusion, ordinary-message warning rates, macro-F1, calibration, and selective coverage. State whether a number describes the model, rules, or complete policy. Include confidence intervals and source limitations. A high overall accuracy on ordinary-heavy data is not sufficient.

For device efficiency, provisional goals can include a warm short-message analysis p95 below approximately 250 ms and a typical screenshot workflow within a few seconds. Maximum-length stress inputs, cold model loading, and first OCR use must be reported separately. These are engineering targets to validate, not promises.

For packaging, investigate an arm64 installable release below approximately twenty megabytes while retaining offline OCR. Measure actual signed artifact size and installed footprint where possible. Keep the universal package available only if its value justifies its size.

For interface quality, test at enlarged system font scales, with assistive reading, visible keyboard, and compact/expanded windows. Check real contrast, focus, navigation, and text wrapping. Stable dependency versions and attractive colors do not substitute for an observed usable interface.

For reproducibility, a clean public checkout should run safe tests and build using documented prerequisites. An extracted source archive should pass honest archive-mode validation. A faculty reviewer should not need the original private joblib file merely to verify exporter correctness.

Do not collapse the scorecard into a single “project score.” The owner needs to understand where quality improved, where uncertainty remains, and what the next useful experiment is.

## Appendix C. Command contracts and publication checklist

The implementation agent should add and execute commands with equivalent behavior to these contracts. Exact module names may follow the refactoring, but every documented command must be tested from its stated working directory.

```text
python -m pytest ml/tests -q
python scripts/validate_project.py --mode repository --format json
python scripts/validate_project.py --mode archive --format json
python scripts/check_release_consistency.py
python -m scamsentry_ml.bundle validate --bundle <versioned-bundle>
python -m scamsentry_ml.compare --config <v2-research-config>
python -m scamsentry_ml.export --run <new-run> --output <staging-bundle>
```

The new command flags are requested implementation contracts, not features already present in 0.1.0. Do not paste them into the README as working until implemented. Preserve safe read-only validation and deliberate artifact promotion.

For Android, verify the actual task names and run the relevant wrapper tasks for host tests, app unit tests, lint, debug assembly, test-APK assembly, minified release, ABI packaging, and connected or managed-device tests. Use Windows wrapper syntax in the owner's setup guide.

For GitHub, verify authentication, exact account, existing repository state, reviewed local tree, commit, and remote configuration before creation or push. After push, inspect the actual remote commit and workflow runs. Do not equate a successful local build with passing remote CI.

Before a release, inspect staged/tracked content, licence notices, private-data exclusions, model metadata, source-to-APK linkage, signing status, and checksums. Retain reports for the exact release commit. Upload reviewed binaries as release assets rather than adding generated APKs to ordinary source history.

Never run the frozen v1 final-test command just to prove the new workflow exists. Use saved v1 reports and the new protocol for new experiments. Guard historical artifacts against accidental overwrite.

## Appendix D. Official references for the upgrade review

These sources were checked on 16 November 2026. They are living documentation; recheck them at implementation time and record resolved versions. The table provides a research starting point, not a pretested universal dependency combination.

| Reference | Purpose |
|---|---|
| [AGP 9.5 release notes](https://developer.android.com/build/releases/agp-9-5-0-release-notes) | Stable 9.5.1 context and Gradle/build compatibility |
| [Kotlin release process and history](https://kotlinlang.org/docs/releases.html) | Kotlin 2.4.21 stable candidate |
| [Compose group releases](https://developer.android.com/jetpack/androidx/releases/compose) | Current stable runtime/UI line |
| [Compose Material 3 releases](https://developer.android.com/jetpack/androidx/releases/compose-material3) | Stable 1.4.0 versus 1.5 release candidate |
| [Compose BOM mapping](https://developer.android.com/develop/ui/compose/bom/bom-mapping) | Verify actual dependency resolution |
| [Room releases](https://developer.android.com/jetpack/androidx/releases/room) | Stable 2.8.5 candidate and migration notes |
| [DataStore releases](https://developer.android.com/jetpack/androidx/releases/datastore) | Stable 1.3.0 candidate and API changes |
| [Activity releases](https://developer.android.com/jetpack/androidx/releases/activity) | Stable 1.13.0 candidate |
| [Lifecycle releases](https://developer.android.com/jetpack/androidx/releases/lifecycle) | Stable lifecycle candidate and build prerequisites |
| [Navigation 3 releases](https://developer.android.com/jetpack/androidx/releases/navigation3) | Optional stable 1.2.1 navigation investigation |
| [Window size classes](https://developer.android.com/develop/ui/compose/layouts/adaptive/use-window-size-classes) | Adaptive-layout design reference |
| [Baseline Profiles overview](https://developer.android.com/topic/performance/baselineprofiles/overview) | Profile generation and measured optimization |
| [Macrobenchmark guidance](https://developer.android.com/topic/performance/benchmarking/macrobenchmark-overview) | Repeated app-level measurements |
| [ABI-specific APKs](https://developer.android.com/build/configure-apk-splits) | Smaller GitHub download artifacts |
| [Android page-size compatibility](https://developer.android.com/guide/practices/page-sizes) | Distinguish ZIP, ELF, and runtime compatibility |
| [Build-managed devices](https://developer.android.com/studio/test/managed-devices) | Device-test execution and runner requirements |
| [Android app signing](https://developer.android.com/studio/publish/app-signing) | Signing identities and release verification |
| [GitHub repository creation](https://docs.github.com/en/repositories/creating-and-managing-repositories/creating-a-new-repository) | Correct account and repository creation |
| [GitHub CLI repo create](https://cli.github.com/manual/gh_repo_create) | Authorized CLI fallback for creation |

Archive-specific findings are grounded in the supplied source and saved artifacts, not in external documentation. The current-review behavioral probes are fictional inputs run through the reference code. Keep those evidence categories separate from general platform guidance.

## Appendix E. Copy-ready launch instruction

> Continue the supplied ScamSentry 0.1.0 research preview using this complete audited upgrade prompt. Preserve the archived model and historical results. Begin with the reproduced rule-context and validator defects, add meaningful regressions, unify end-to-end analysis contracts, and improve export metadata. Keep the native Kotlin/Compose offline foundation and evaluate stable dependency upgrades in compatible groups. Research word, character, and hybrid models under an honest new protocol, without relabelling the known v1 test as unseen. Improve OCR lifecycle handling, packaging, accessibility, and actual phone verification. Create or safely continue the public repository DevenGaikwad/ScamSentry, publish reviewed source, run CI, and prepare an accurately labelled release. Do not wait for perfection before publishing the corrected research preview, and do not invent device evidence, accuracy, signing, or repository success. Avoid permission-dependent datasets and do not send emails. Maintain project state and continue all independent authorized work until the concrete handoff is ready.

Start by inspecting the actual workspace and confirming whether the original Git checkout exists. Verify the input digest, preserve important files, and record the baseline. Use this prompt's priorities to avoid repeating completed work.

The final result should make the application more useful, robust, efficient, and reviewable. Every claimed improvement should have a corresponding code change and evidence. A credible project is one the owner can install, explain, reproduce, present, and continue improving.

**End of continuation master prompt.**






---

# Mandatory additive extension v2.1 — Explicit ML Optimization and Conditional Free Web App

**Added:** 16 November 2026  
**Preservation rule:** The complete preceding v2 prompt is retained unchanged. This extension adds requirements; it does not remove the audit, Android application, GitHub instructions, tests, safeguards, or existing research work. The earlier end marker closes the original portion only. Read and execute this extension as part of the final prompt.

**Interpretation:** The user's reference to the “versatile” plugin is interpreted as Vercel, the available deployment integration. The requested browser experience should be convenient to open and demonstrate through a link, as NewsLens was. Preserve ScamSentry's own domain, eligible data, model evidence, privacy behavior, and Android deliverable.

## Additional navigation

- [37. Explicit optimization objective](#37-explicit-optimization-objective)
- [38. Confusion counts and classification metrics](#38-confusion-counts-and-classification-metrics)
- [39. Whole-system metrics and abstention](#39-whole-system-metrics-and-abstention)
- [40. Hyperparameter and feature optimization](#40-hyperparameter-and-feature-optimization)
- [41. Thresholds, calibration, and error reduction](#41-thresholds-calibration-and-error-reduction)
- [42. Model promotion and statistical evidence](#42-model-promotion-and-statistical-evidence)
- [43. Metric artifacts and reproducible reporting](#43-metric-artifacts-and-reproducible-reporting)
- [44. Vercel feasibility and zero-cost conditions](#44-vercel-feasibility-and-zero-cost-conditions)
- [45. Browser application architecture](#45-browser-application-architecture)
- [46. Python, Kotlin, and browser parity](#46-python-kotlin-and-browser-parity)
- [47. Browser screenshot support](#47-browser-screenshot-support)
- [48. Copyright, permissions, and privacy](#48-copyright-permissions-and-privacy)
- [49. GitHub and conditional web deployment](#49-github-and-conditional-web-deployment)
- [50. Added release gates](#50-added-release-gates)
- [51. Final execution instruction](#51-final-execution-instruction)

## 37. Explicit optimization objective

Make model quality an explicit engineering workstream. Improve F1, macro-F1, accuracy, precision, recall, specificity, balanced accuracy, calibration, robustness, and useful coverage through documented experiments. Reduce false negatives and false positives while preserving practical mobile and browser performance.

Track true positives, true negatives, false positives, and false negatives with denominators. Interpret them on a defined task and dataset. More true positives merely because the evaluation contains more positive records is not a demonstrated improvement. Compare rates and counts on comparable examples and report changes in support.

Treat the user's request for “100 percent proper” as a requirement for correct implementation, careful verification, and honest optimization. Do not promise perfect prediction on future messages or manipulate reports to display 100 percent. A flawless score on a small, duplicated, synthetic, or narrow set cannot establish universal scam detection.

Several objectives compete. Raising a warning threshold can reduce false positives while increasing missed detections. Increasing abstention can reduce accepted error while reducing coverage. A larger model can improve one benchmark while worsening latency, memory, or generalization. Make these trade-offs explicit and select a useful operating point.

Do not optimize only the easiest headline metric. The primary research score should include fixed-label three-class macro-F1, with smishing recall and ordinary-message warning burden as critical accompanying measures. Report the complete result even when accuracy rises and minority-class quality falls.

Keep the original historical measurements intact. Improve a new candidate under the declared research protocol, label every metric by dataset and model version, and preserve the ability to compare or roll back. No deployment decision may be justified by an unrepeatable number.

## 38. Confusion counts and classification metrics

Maintain the three-class confusion matrix with rows representing actual labels and columns representing predictions. Fix the label order to ordinary, promotional-or-spam, and smishing-like. Include counts and appropriate normalized views, clearly labelled.

Also produce one-versus-rest confusion counts for every class. For the smishing view, “positive” means smishing-like, and “negative” means the other two source classes together. Preserve promotional-versus-ordinary distinctions in the three-class report.

| Quantity | Required meaning for a selected positive class |
|---|---|
| TP | Actual positive and predicted positive |
| FP | Actual negative but predicted positive |
| FN | Actual positive but predicted negative |
| TN | Actual negative and predicted negative |
| Precision | TP / (TP + FP) |
| Recall / sensitivity / true-positive rate | TP / (TP + FN) |
| Specificity / true-negative rate | TN / (TN + FP) |
| False-negative rate | FN / (FN + TP) |
| False-positive rate | FP / (FP + TN) |
| Negative predictive value | TN / (TN + FN) |
| F1 | 2 TP / (2 TP + FP + FN) |

For three-class accuracy, use the correctly classified records divided by all evaluated records. A binary one-versus-rest accuracy is a different calculation and must not be substituted for the multiclass accuracy.

Compute macro-F1 as the unweighted mean of the three class F1 scores using the fixed class list. Report weighted-F1 separately with its class-support weighting. If reporting micro-F1, explain its relationship to accuracy for ordinary single-label multiclass evaluation rather than presenting it as an independent gain.

Add balanced accuracy, MCC where appropriate, class support, and the exact zero-division policy. A class absent from a split requires an explicit limitation; do not quietly drop it from the macro average and compare the result with a three-class score.

Store undefined metrics as an explicitly documented missing or zero-division outcome. Never display an undefined precision as perfect because there were no positive predictions. Verify calculations with controlled confusion matrices, including no predicted positives, no actual positives, class imbalance, and a deliberately wrong minority-class model.

Counts, rates, and averages must be internally consistent. Derive reports from predictions and labels through one tested implementation. Do not manually type values into the README, presentation, app, or website. Keep confidence intervals and denominators close to important results.

## 39. Whole-system metrics and abstention

Evaluate the statistical classifier, rule engine, and final application policy separately. A rule-triggered warning may succeed when the classifier misses a request. A model argmax can be correct while the displayed product category is unsuitable. Both deserve visible measurement.

Provide a model-only three-class report and a full-pipeline report from the original reviewed input through parsing, observations, quality checks, inference, and decision policy. Do not evaluate all rules on already minimized corpus strings and then describe that as original-input app behavior.

Measure missed smishing indicators and nuisance warnings using explicit product definitions. Ordinary-to-smishing argmax errors, ordinary-to-any-nonordinary errors, and ordinary-to-higher-risk policy warnings are distinct rates. Report each relevant denominator.

Track the original finding that nine of the seventy-five historical smishing examples were classified as promotional and one as ordinary as archived evidence. Those are useful error categories, not a new blind test to tune repeatedly. Investigate equivalent failure mechanisms on permitted development data and independent new examples.

For abstention, report coverage, error among accepted cases, counts of positive and negative records deferred, and unresolved high-risk cases. Do not exclude difficult abstained cases and advertise the resulting accuracy as full-dataset performance.

If a deployment treats abstained positives as missed automatic detections, include that operational recall calculation. If it instead provides a human-review workflow, describe the deferral workload and avoid inventing human-review success. Report both accepted-only quality and total-population outcomes where appropriate.

Evaluate source, language-support, text-length, OCR-quality, and perturbation slices. Small slices require visible support and uncertainty. Avoid making a strong subgroup claim from a handful of examples. Preserve the difference between historical benchmark performance and current Indian campaign coverage.

## 40. Hyperparameter and feature optimization

Implement a bounded, reproducible search using suitable existing tools such as scikit-learn parameter search. Use group-aware folds and fit the entire preprocessing pipeline within each training fold. Vocabulary, IDF, feature selection, resampling, and augmentation must not leak from validation or test.

For linear candidates, investigate regularization strength, class weighting, word and character n-gram ranges, minimum document frequency, vocabulary budgets, sublinear term frequency, feature-block weights, and normalization choices. Keep the search compatible with a clearly specified export contract.

For each feature family, include the archived baseline and a simple sensible default. Investigate the existing stronger word-feature development result and hybrid alternatives instead of limiting champion selection to names beginning with char_lr.

For a justified compact neural candidate, bound learning rate, model width, sequence length, dropout, batch size, regularization, class weighting, and early stopping. Record resource consumption and fit stability. Neural search is optional; a well-optimized linear candidate can remain the best product choice.

For augmentation, vary realistic error families and severity using training-only parents. Measure clean quality and benign numeric cases so a model does not gain noisy-scam recall by warning about every typo or number.

Start with an affordable coarse search and refine only justified regions. Record configurations, fold memberships, seeds, run status, warnings, duration, and artifacts. Prefer CPU-compatible work within the owner's resources. Do not provision paid GPU services or depend on expiring credits.

Keep model selection, calibration fitting, and decision-threshold selection as separate documented steps. If a preprocessing or vocabulary change is made, create a new model identity, refit calibration appropriately, and rerun deployment parity. Do not attach the old calibrated metrics to new features.

Use multiple seeds or suitable grouped resampling where the experimental question needs variability evidence. Repeatedly optimizing one development partition can overfit it; the absence of a final-test read alone does not eliminate that risk.

## 41. Thresholds, calibration, and error reduction

Use threshold sweeps on permitted development predictions to examine precision–recall, false-positive versus false-negative rate, warning volume, coverage, and expected error cost. Show how the chosen point compares with reasonable alternatives.

Choose a documented warning-cost policy before final evaluation. Missing a secret request can be more harmful than a nuisance warning, while constant false alarms make the app less useful. Use explicit configurable costs or constraints and explain the choice; do not assume every user and scenario has identical costs.

Report F-beta, such as F2, only when its greater recall emphasis answers a stated question. Macro-F1 remains required. Improving a recall-weighted score must not hide an unacceptable loss of ordinary-message precision.

Tune confidence and margin gates from evidence. The earlier .62 confidence and .18 margin values are starting settings, not sacred constants. Track the consequences of changing them independently of model coefficients.

Evaluate calibration using defined log loss, multiclass Brier score, reliability diagrams, and a documented calibration-error summary. A calibrated in-domain model can remain wrong on unfamiliar inputs. Never label its estimates as verified sender authenticity.

Use development error analysis to distinguish label problems, context-rule failures, inadequate features, distribution shift, OCR errors, and unsupported inputs. Fix each mechanism at the right layer. Do not increase model size to compensate for a deterministic parser defect.

Preserve hard negatives such as legitimate urgency, promotional offers, and quoted scam education. Also preserve direct affirmative requests surrounded by benign or educational language. Reducing false positives must not reintroduce the global suppression bypass already identified.

## 42. Model promotion and statistical evidence

Predeclare a selection rule that balances macro-F1, smishing recall, ordinary-message warnings, robustness, calibration, coverage, latency, memory, and distribution size. Select from useful trade-offs rather than a single lucky score.

Compare candidates on the same evaluation units and report the absolute and relative differences correctly. A change from .90 to .91 is one percentage point in score units; do not describe it as ten points. State when comparisons use different partitions and therefore do not support a direct improvement claim.

Use paired group-aware uncertainty estimates for comparable predictions when appropriate. Preserve class support and independence assumptions. A tiny difference within substantial sampling variation should be described as inconclusive rather than a definitive superiority claim.

Set practical development goals before running a final measurement. Goals may target lower false-negative burden and stronger realistic-noise results while preserving an acceptable false-positive rate. If a goal fails, report the result, diagnose the cause, and keep a conservative release status.

No zero-error observation establishes zero population risk. For example, zero false positives on a small ordinary set requires its denominator and uncertainty interval. Calibration and deployment-prevalence changes also affect how precision transfers to real use.

A replacement model must pass export validation, platform parity, regression checks, and actual resource budgets where devices are available. A model that is better in Python but changes behavior after export is not a verified upgrade.

If no candidate beats the baseline on the agreed trade-off, preserve the baseline and ship the independent app, policy, and verification improvements. Document the research honestly. Do not force a promotion to satisfy the word “optimized.”

## 43. Metric artifacts and reproducible reporting

Generate machine-readable confusion counts, class reports, threshold tables, calibration records, robustness slices, and resource measurements for each eligible run. Include model, protocol, dataset, split, normalizer, policy, code commit, and environment identifiers.

Create a baseline-versus-candidate table showing actual F1, macro-F1, accuracy, precision, recall, specificity, FPR, FNR, TP, FP, TN, FN, coverage, and resource cost. Use separate rows or panels for model-only and complete-system results. Mark unavailable measurements explicitly.

Required documentation includes ML_OPTIMIZATION_PLAN.md, HYPERPARAMETER_SEARCH.md, METRIC_DEFINITIONS.md, MODEL_COMPARISON.md, and updated evaluation/model-card records, or clearly equivalent consolidated documents. Add targeted tests for metric identities and confusion-matrix consistency.

The app and future website may display verified benchmark summaries with version, dataset context, support, and limitations. They must not display a decorative “100 percent protected” meter or invent a score for an unsupported input.

Freeze evaluation artifacts before generating publication charts and presentation tables. Generate these views from the same structured sources so the GitHub README, Android about screen, browser app, and faculty reports agree.

Separate research experiments from release promotion. Keep old reports immutable and keep new failed or incomplete runs discoverable in a sensible experiment register. A failed experiment should not be erased simply because a better candidate was found later.

## 44. Vercel feasibility and zero-cost conditions

Treat Vercel as the preferred conditional host for a browser companion. The user wants an app that is easy to open and demonstrate through a web link, as with NewsLens. The original Android requirements remain intact.

Vercel's published Hobby plan is free within included usage, and its current terms limit Hobby use to personal or non-commercial purposes. This makes an ordinary personal academic demonstration a plausible deployment candidate, subject to the actual account, content, usage, and current terms. Do not claim every university deployment or commercial project automatically qualifies.

Plugin availability and hosting entitlement are separate facts. The presence of Vercel tools does not prove that a particular connected team is on a free plan or that every integration is free. Check the account and target scope before a deployment action.

The required budget is zero for this implementation path. Do not activate Pro, start an expiring paid-plan trial as a substitute for free hosting, add a payment method, purchase a domain, create paid seats, enable a paid add-on, buy inference credits, or provision chargeable databases or storage.

Prefer static hosting with local browser inference. This avoids needing a continuously running Python model server or paid prediction API. Use the provider's included project subdomain if available; a custom domain is unnecessary.

Recheck current usage limits before publication and document the relevant ones. The Hobby documentation states that features can become unavailable after included usage is exhausted. Therefore free hosting is not a promise of unlimited traffic or permanent uninterrupted availability.

If a suitable free scope, account access, rights-compatible deployment, or technically workable browser implementation is unavailable, skip that deployment step, document the concrete reason, and finish the Android/GitHub work and reviewable web source. Do not ask the owner to pay or contact dataset authors as a workaround.

## 45. Browser application architecture

Add a web directory inside the existing ScamSentry repository. Build a responsive browser companion using a current compatible stable React, TypeScript, and Vite stack, with the existing design tokens and locally bundled fonts. Tailwind CSS or a small maintainable CSS system can implement the presentation. Pin versions after checking compatibility at implementation time.

Use the original dark navy-black identity, coordinated risk accents, clear hierarchy, and readable typography. Make the site feel like the same product as the Android app. Do not reuse NewsLens branding, dataset, model, claims, or accuracy merely because the owner wants a similarly convenient hosted experience.

Implement actual text analysis, URL inspection, observations, uncertainty, result explanations, fictional examples, model information, privacy information, and a GitHub/APK link. The page must be a functional tool, not only a marketing page with a dummy input box.

Run the exported linear model in a dedicated Web Worker using a tested TypeScript implementation. Load the approved versioned model bundle as a public static asset and validate schema, digests, class order, preprocessing contract, calibration, and policy compatibility. Use a public asset only after the rights and privacy review.

Keep analysis input in browser memory. A user's pasted message must not become a POST request, a URL query parameter, a route fragment, an analytics event, a build log, or a feedback payload. No user account, cloud database, or remote explanation API is required.

Use request identifiers and cancellation or stale-result rejection in the worker protocol. The user must remain able to edit and navigate while analysis runs. Distinguish model loading, processing, unsupported input, corrupt assets, and recoverable errors.

Build and test the production static output, normally Vite's dist directory. Initial app/model download requires connectivity. Cached offline behavior or an installable PWA is an additional tested capability, not an automatic consequence of static hosting.

## 46. Python, Kotlin, and browser parity

Extend deployment verification to three implementations: the Python reference, the native Kotlin engine, and the browser TypeScript engine. Preserve all current Python–Kotlin tests and add safe end-to-end fixtures for the browser.

Specify Unicode code-point handling deliberately. JavaScript string indexing uses UTF-16 units; use a tested code-point-aware approach where the feature contract requires it. Match normalization, ASCII case handling, the fixed whitespace set, identifier minimization, feature extraction, counts, TF-IDF, normalization, coefficients, temperature, and class order.

Do not assume identically named regex flags have identical meaning across languages. Unicode word boundaries, letter/digit categories, and regular-expression engines can differ. Use explicit contracts and fixtures for the actual supported behavior.

Compare intermediate values and final outcomes. Validate normalized text, active features, weighted-vector norms, logits, calibrated estimates, observations, URL extraction, quality flags, limitations, and product category. Apply justified numeric tolerances and inspect threshold-adjacent differences.

The browser must use the exact approved bundle associated with its reported benchmark. Generate a shared manifest rather than copying numeric arrays manually. If a v2 model cannot yet be exported reliably, a clearly identified baseline can support the browser while replacement research continues.

Test cache and update consistency. A service worker or CDN must not combine an old model with a new incompatible normalizer or policy. Use versioned or content-addressed assets, explicit compatibility checks, and a controlled reload path. Private input must not be stored in static caches.

Report browser CPU timings separately from Android and Python timings. Device, browser, build, input set, cold download, parsing, warm inference, and complete-workflow latency must be identified. Equivalent predictions do not prove equivalent runtime or OCR quality.

## 47. Browser screenshot support

Browser screenshot recognition is a separate platform implementation. Android ML Kit is not a drop-in browser dependency. Preserve the Android OCR system and implement browser OCR only through a compatible locally executed option.

Tesseract.js is a candidate because its official project supports browser execution through WebAssembly and publishes an Apache-2.0 licence. Review the exact JavaScript package, worker, WASM core, language-data file, and notices before bundling. A package licence does not automatically document the provenance of every language asset.

Use supported English OCR initially. Where compatible assets are used from a repository such as tessdata_fast, inspect their actual licence and runtime compatibility. Prefer self-hosted pinned worker/model resources over undocumented remote asset downloads. Do not promise that an OCR engine's many supported languages validate the ScamSentry classifier in those languages.

Read user-selected files locally with explicit type, byte, dimension, orientation, and allocation limits. Provide reviewable extracted text before classification. Include cancel, replace-image, edit-text, and clear actions. Clean up object URLs, images, worker tasks, and temporary buffers according to their actual lifecycle.

Load large OCR resources lazily after the user chooses screenshot analysis. Reuse a worker where safe and terminate it when appropriate. Measure mobile-browser memory and responsiveness. Do not make the initial text-analysis page wait for every OCR resource.

If browser OCR cannot satisfy the free deployment, compatibility, rights, or quality conditions, ship a useful browser text/URL tool with honest screenshot limitations while keeping the Android screenshot workflow. Do not show an apparently working screenshot button that returns fabricated extracted text.

A PWA may cache public OCR assets for later use if implemented and tested. Do not claim first-use offline OCR until the required resources are actually available without a network request. Keep the difference between initial download and later offline use clear.

## 48. Copyright, permissions, and privacy

The user's no-email requirement remains binding. Use a dataset or asset only when the published terms and reviewed files support the intended workflow without an individual permission request. If a source requires mailing a university, researcher, or other owner, exclude it from the default implementation and find an eligible alternative.

An attribution requirement is not the same as a permission-email requirement. Provide required credit, licence links, relevant notices, and descriptions of changes. Preserve separate code, dataset, model-derived-asset, font, and dependency terms.

Free hosting does not grant rights to a dataset or model. Conversely, an appropriate published data licence does not make every hosting plan suitable. Review the source rights and hosting conditions independently.

For CC BY 4.0 material, follow the applicable attribution and reuse conditions and remember that privacy or other third-party rights can still matter. Do not promise that every record is cleared merely because a publisher page displays a licence badge.

Review the current provider content terms, including rights concerning uploaded assets and AI-training provisions. Upload only source and public assets suitable for that use. Keep real user messages and screenshots in the browser so they are not submitted as hosting or inference content. If a material incompatibility remains unresolved, skip that deployment route instead of seeking an individual permission exception.

Do not host raw training corpora merely to make a demonstration convenient. Publish reproducibility scripts, manifests, notices, safe fictional examples, and approved model assets. Inspect vocabulary and metadata for unintended sensitive fragments; model parameters are not a universal privacy guarantee.

Apply the same care to browser dependencies and OCR files. Do not use unlicensed visual assets, copied bank interfaces, paid fonts, or a proprietary pretrained checkpoint whose terms are unclear.

Do not add message analytics, session-replay tools, cloud crash payloads, or a “send us this message” feature by default. Inspect actual browser network requests during an analysis to verify that input content stays local. Ordinary host access logs and public asset requests should be described accurately without claiming zero network activity.

## 49. GitHub and conditional web deployment

Keep the exact repository requirement DevenGaikwad/ScamSentry and all original publication instructions. Add web source, tests, public approved assets, and browser documentation within that repository. Do not create a second differently named repository merely for hosting.

Add documentation such as WEB_ARCHITECTURE.md, WEB_PRIVACY.md, WEB_DEPLOYMENT.md, ZERO_COST_DEPLOYMENT_CHECKLIST.md, and cross-platform parity reports, or clearly equivalent consolidated files. Keep the Android installation and SDK instructions available.

Extend CI with browser dependency installation from a lockfile, type checking, lint, meaningful unit tests, worker parity, production build, and browser workflow tests where available. Browser test artifacts must use fictional inputs. Do not require private datasets to run public UI and inference-contract checks.

Use the Vercel integration if its capabilities and account access support the no-cost deployment. Check current account/team plan, project root, build command, output directory, source branch, and deployment settings before mutation. Do not move or downgrade unrelated paid projects.

Configure the web subdirectory as the deployment root and use the verified build/output settings. Keep client-side routing compatible with deep-link refresh. Use appropriate security headers and permit only the worker/WASM behavior actually required; avoid broad unsafe script permissions as a shortcut.

Conditional public web deployment is part of this requested addition once the zero-cost, rights, account, and technical conditions pass. No separate permission email should be introduced. If provider access requires a genuine user sign-in or an unavailable capability, identify that specific blocker, keep a concrete build ready, and continue independent authorized work.

Verify the returned live URL, actual deployed commit, public access, model identifier, functional text analysis, stale-result handling, responsive layout, notices, and input privacy. Do not invent a preferred subdomain or report a local preview as a successful public deployment.

If the conditions fail, retain the functioning Android/GitHub project and supply the browser source and static build instructions. Record why the optional host was skipped. The owner has explicitly accepted that outcome rather than paying or requesting dataset permissions.

## 50. Added release gates

The final gate report must include the additional criteria below, with pass, fail, blocked, or not applicable and an evidence path:

| Gate | Required evidence |
|---|---|
| Metric correctness | Tested confusion counts, averages, denominators, and zero-division handling |
| ML optimization | Recorded candidate/search protocol and actual comparison results |
| Error trade-off | Recall/FNR and precision/FPR examined together |
| Statistical interpretation | Supports, uncertainty, and comparable evaluation units |
| Model promotion | Documented quality/resource decision; no forced replacement |
| Historical preservation | Original baseline reports and old test history remain traceable |
| Three-platform parity | Python, Kotlin, and browser contracts checked where implemented |
| Browser functionality | Real text/URL workflow and controlled errors |
| Browser privacy | No input text/image transmitted during local analysis |
| Browser OCR | Actual executed status and asset rights; honest limitation if deferred |
| Free hosting eligibility | Current account/plan, usage, and terms reviewed |
| No-cost deployment | No paid plan, trial dependency, domain purchase, or chargeable add-on |
| Rights without permission requests | Eligible sources and asset notices; excluded ambiguous/permission-only material |
| Remote publication | Correct GitHub repository and verified live URL if deployed |
| Preservation of original prompt | Original v2 content retained in the complete final specification |

Do not require a numerically perfect classifier to mark independent application work complete. Do require truthful evidence and a useful supported operating point. If a metric goal is unmet, the report should explain it and preserve the research-preview scope.

Benchmark summaries in the deployed app must be generated from the correct frozen model/protocol artifacts. A build must never silently substitute a toy model or made-up score when a download, parsing step, or worker fails.

## 51. Final execution instruction

Execute the entire original v2 prompt and this additive v2.1 extension. Preserve all original audit findings, safeguards, Android work, GitHub requirements, and verification. Make ML optimization explicit across F1, macro-F1, accuracy, precision, recall, specificity, TP, TN, FP, FN, false-positive rate, false-negative rate, calibration, robustness, and coverage.

Use well-designed data, feature, hyperparameter, and threshold experiments to improve the useful trade-off. Report actual measurements. Treat correctness and diligence as mandatory; do not promise 100 percent real-world prediction or fabricate success.

Add the requested link-accessible browser companion where feasible. Prefer a static React/TypeScript/Vite application with local worker inference on an eligible free Vercel Hobby scope. Preserve the Android application and the single ScamSentry repository. Keep user messages local, avoid paid dependencies, and exclude sources that require permission emails.

Complete independent work even when a device or hosting capability is unavailable. Publish the authorized reviewed repository, verify any conditional deployment, and provide a concrete handoff with versions, test results, model evidence, costs/plan status, notices, URLs, and remaining limitations.

**This is the final additive instruction. The original prompt above remains part of the deliverable in full.**

## Appendix F. References for the added ML and hosting requirements

These references were checked on 16 November 2026. Recheck living pricing, terms, and package documentation at implementation time. Proposed architecture, tests, and optimization requirements are original project specifications; no reference constitutes a guarantee of model quality or free unlimited service.

| Reference | Purpose |
|---|---|
| [Scikit-learn metrics and scoring](https://scikit-learn.org/stable/modules/model_evaluation.html) | Metric implementation and averaging conventions |
| [Scikit-learn parameter search](https://scikit-learn.org/stable/modules/grid_search.html) | Reproducible hyperparameter-search tooling |
| [Vercel pricing](https://vercel.com/pricing) | Current Hobby price and plan comparison |
| [Vercel Hobby documentation](https://vercel.com/docs/plans/hobby) | Included usage and behavior at limits |
| [Vercel Terms of Service](https://vercel.com/legal/terms) | Personal/non-commercial Hobby conditions and submitted-content terms |
| [Vite on Vercel](https://vercel.com/docs/frameworks/frontend/vite) | Static frontend build/deployment guidance |
| [Vite static deployment](https://vite.dev/guide/static-deploy) | Production build output and deployment workflow |
| [Tesseract.js](https://github.com/naptha/tesseract.js) | Browser OCR candidate, package scope, and licence |
| [tessdata_fast](https://github.com/tesseract-ocr/tessdata_fast) | Candidate OCR language assets requiring compatibility review |
| [Creative Commons Attribution 4.0](https://creativecommons.org/licenses/by/4.0/) | Reuse conditions and separate-rights limitations |

**End of complete v2.1 master prompt, including all preserved v2 content.**


