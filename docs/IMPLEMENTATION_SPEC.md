# ScamSentry — Comprehensive Implementation Master Prompt

**Owner:** Deven Sachin Gaikwad  
**Academic setting:** Third-year B.Tech Electronics and Communication Engineering, AI/ML specialization; Mobile App Development and Cybersecurity  
**Document version:** 1.0  
**Prepared:** 16 November 2026  
**Primary deliverable requested by this document:** A reproducible, useful, visually polished Android project with defensible machine-learning evidence and a reviewable GitHub release.

## How to use this document

Give this complete document to an implementation agent in a fresh project workspace. Ask it to execute the prompt, inspect the environment, and begin Phase 0. This document is an implementation specification, not a claim that the application, trained model, experiment results, repository, or APK already exists. Read the entire specification before selecting dependencies or downloading datasets. Preserve the requirements in repository documents so work can continue across interrupted sessions.

The source register near the end records official references checked during preparation. Recheck licences, release compatibility, and platform requirements when implementation begins. Requirements, proposed budgets, and target metrics in this prompt are design decisions. They must never be presented as measured performance. Every completed claim must point to an actual artifact or reproducible observation.

---

## Navigation

- [1. Your role, responsibility, and working standard](#1-your-role-responsibility-and-working-standard)
- [2. Project mission and useful user outcomes](#2-project-mission-and-useful-user-outcomes)
- [3. Execution authority and boundaries](#3-execution-authority-and-boundaries)
- [4. Mandatory release scope](#4-mandatory-release-scope)
- [5. Growth scope and explicit priorities](#5-growth-scope-and-explicit-priorities)
- [6. Dataset and asset rights workflow](#6-dataset-and-asset-rights-workflow)
- [7. Initial dataset inventory](#7-initial-dataset-inventory)
- [8. Adding data when existing data is insufficient](#8-adding-data-when-existing-data-is-insufficient)
- [9. Privacy inspection and data minimization](#9-privacy-inspection-and-data-minimization)
- [10. Canonical records and provenance schema](#10-canonical-records-and-provenance-schema)
- [11. Label taxonomy and task separation](#11-label-taxonomy-and-task-separation)
- [12. Exact duplicates, templates, and campaign grouping](#12-exact-duplicates-templates-and-campaign-grouping)
- [13. Partitioning and experimental isolation](#13-partitioning-and-experimental-isolation)
- [14. Text normalization and feature contracts](#14-text-normalization-and-feature-contracts)
- [15. Exploratory analysis and the data sufficiency decision](#15-exploratory-analysis-and-the-data-sufficiency-decision)
- [16. Baselines and the first deployable classifier](#16-baselines-and-the-first-deployable-classifier)
- [17. Modern model candidates and evidence-based selection](#17-modern-model-candidates-and-evidence-based-selection)
- [18. Training configuration and compute discipline](#18-training-configuration-and-compute-discipline)
- [19. Calibration, uncertainty, and abstention](#19-calibration-uncertainty-and-abstention)
- [20. Decision policy and risk categories](#20-decision-policy-and-risk-categories)
- [21. Explainability and faithful explanations](#21-explainability-and-faithful-explanations)
- [22. Safe offline URL analysis](#22-safe-offline-url-analysis)
- [23. Learned URL model and feature availability](#23-learned-url-model-and-feature-availability)
- [24. Screenshot import and OCR review](#24-screenshot-import-and-ocr-review)
- [25. Robustness evaluation and controlled perturbations](#25-robustness-evaluation-and-controlled-perturbations)
- [26. Evaluation metrics and statistical interpretation](#26-evaluation-metrics-and-statistical-interpretation)
- [27. Model export and artifact contract](#27-model-export-and-artifact-contract)
- [28. Cross-language prediction parity](#28-cross-language-prediction-parity)
- [29. Runtime budgets and phone measurements](#29-runtime-budgets-and-phone-measurements)
- [30. Native Android stack and dependency policy](#30-native-android-stack-and-dependency-policy)
- [31. Architecture and module boundaries](#31-architecture-and-module-boundaries)
- [32. Domain models and result schema](#32-domain-models-and-result-schema)
- [33. Navigation and interaction structure](#33-navigation-and-interaction-structure)
- [34. Visual identity and aesthetic direction](#34-visual-identity-and-aesthetic-direction)
- [35. Color, typography, spacing, and motion tokens](#35-color-typography-spacing-and-motion-tokens)
- [36. Screen specifications: onboarding and Analyze home](#36-screen-specifications-onboarding-and-analyze-home)
- [37. Screen specifications: editor, OCR review, and processing](#37-screen-specifications-editor-ocr-review-and-processing)
- [38. Screen specifications: results and evidence](#38-screen-specifications-results-and-evidence)
- [39. Screen specifications: History, Learn, settings, and model information](#39-screen-specifications-history-learn-settings-and-model-information)
- [40. Accessibility and inclusive interaction](#40-accessibility-and-inclusive-interaction)
- [41. Local storage, encryption, backup, and deletion](#41-local-storage-encryption-backup-and-deletion)
- [42. Permissions, intents, and platform attack surface](#42-permissions-intents-and-platform-attack-surface)
- [43. Threat model and security verification](#43-threat-model-and-security-verification)
- [44. Dependency security, build provenance, and model integrity](#44-dependency-security-build-provenance-and-model-integrity)
- [45. Error handling, observability, and privacy-safe diagnostics](#45-error-handling-observability-and-privacy-safe-diagnostics)
- [46. Unit and integration test strategy](#46-unit-and-integration-test-strategy)
- [47. UI, lifecycle, and device verification](#47-ui-lifecycle-and-device-verification)
- [48. ML acceptance gates and failure decisions](#48-ml-acceptance-gates-and-failure-decisions)
- [49. Repository structure and maintainable source organization](#49-repository-structure-and-maintainable-source-organization)
- [50. GitHub workflow and continuous integration](#50-github-workflow-and-continuous-integration)
- [51. Windows development and setup experience](#51-windows-development-and-setup-experience)
- [52. APK packaging, signing, and release artifacts](#52-apk-packaging-signing-and-release-artifacts)
- [53. Project website and optional browser demonstration](#53-project-website-and-optional-browser-demonstration)
- [54. Academic reports for the two subjects](#54-academic-reports-for-the-two-subjects)
- [55. Faculty demonstration and evaluation evidence](#55-faculty-demonstration-and-evaluation-evidence)
- [56. Pilot study and user feedback](#56-pilot-study-and-user-feedback)
- [57. Milestones, schedule, and scope control](#57-milestones-schedule-and-scope-control)
- [58. Resume and continuation protocol](#58-resume-and-continuation-protocol)
- [59. Public communication, authorship, and résumé claims](#59-public-communication-authorship-and-résumé-claims)
- [60. Definition of done and final handoff](#60-definition-of-done-and-final-handoff)
- [Appendix A. Copy-ready implementation launch instruction](#appendix-a-copy-ready-implementation-launch-instruction)
- [Appendix B. Concrete repository contract](#appendix-b-concrete-repository-contract)
- [Appendix C. Source and experiment manifest examples](#appendix-c-source-and-experiment-manifest-examples)
- [Appendix D. Reference inference mathematics and parity checklist](#appendix-d-reference-inference-mathematics-and-parity-checklist)
- [Appendix E. Concrete acceptance scenarios](#appendix-e-concrete-acceptance-scenarios)
- [Appendix F. Milestone deliverables and practical exit criteria](#appendix-f-milestone-deliverables-and-practical-exit-criteria)
- [Appendix G. Required command interface and validation behavior](#appendix-g-required-command-interface-and-validation-behavior)
- [Appendix H. Official source register and freshness rules](#appendix-h-official-source-register-and-freshness-rules)
- [Appendix I. Decision records and release-review questions](#appendix-i-decision-records-and-release-review-questions)
- [Appendix J. First actions and final implementation instruction](#appendix-j-first-actions-and-final-implementation-instruction)

## 1. Your role, responsibility, and working standard

Act as the responsible lead engineer for ScamSentry, combining Android development, applied machine learning, data stewardship, mobile security, product design, and academic communication. Build a complete product incrementally. Make routine engineering decisions independently, record the decisions, and continue until the authorized deliverables are concrete and reviewable. Do not stop after producing an architecture proposal, attractive mock screens, a notebook, or a list of recommendations.

Use careful judgment instead of interpreting words such as “perfect” as an instruction to invent results. The intended standard is a reliable, understandable application with transparent limits, excellent presentation, meaningful tests, and a credible path to improvement. No classifier can prove every message legitimate or detect every future scam. Convert that reality into useful product behavior: explicit uncertainty, clear observations, practical next steps, and evaluation that exposes failure cases.

The owner should understand and defend the work in a faculty demonstration and viva. Explain important decisions in straightforward language, followed by technical detail where useful. Keep a concise architecture decision record for choices that affect data rights, model behavior, storage, deployment, or release quality. Prefer a small number of well-understood components over a stack whose maintenance exceeds the semester.

Maintain an evidence-first development process. Link a feature to its acceptance criteria, implementation, verification, and screenshot or test result. Treat generated code as code requiring review. Read existing files before editing, preserve user changes, and avoid replacing working implementations with skeletons. Distinguish blocked work from failed work and completed work. Where a tool or device is unavailable, prepare the corresponding test procedure and report that limitation accurately.

Produce original source code and honest project documentation. Attribute external datasets, libraries, icons, fonts, research, and adapted examples. The project should show the owner's engineering contribution through integration, experiments, evaluation design, privacy choices, and deployment quality.

## 2. Project mission and useful user outcomes

Create ScamSentry, an offline-first Android assistant for examining suspicious messages and links. The user can paste text, share text from another app, or select a screenshot. ScamSentry extracts relevant content, lets the user correct it, analyzes supported inputs locally, and explains observable risk signals in plain language.

The application must support a complete decision workflow. A user should understand what was analyzed, whether OCR changed the input, what the model estimates, which rules were triggered, what remains uncertain, and what reasonable action to take. Useful actions include independently verifying a sender through a known channel, avoiding an unexpected payment request, and checking a domain carefully. Keep advice educational and proportional to the evidence.

Focus the first validated release on English text. The interface may be designed for later localization, but language support is earned through labelled data and evaluation. Hindi, Marathi, Hinglish, and mixed-script text must not inherit an English model's validation claim. Unsupported or ambiguous inputs should lead to a limited-analysis result with an explanation and an option to edit.

Use risk categories that distinguish likely ordinary content, unsolicited promotion, and indicators consistent with smishing. Preserve the difference between advertising and fraud. Users should never be told that a message is definitively safe because no suspicious pattern was found. Similarly, a familiar bank name, HTTPS, or an official-looking screenshot must not be treated as proof of authenticity.

The project should strengthen both courses. Mobile App Development evidence should cover native interaction, lifecycle behavior, responsive layouts, persistence, accessibility, and packaging. Cybersecurity evidence should cover threat modelling, phishing mechanisms, least privilege, adversarial robustness, attack-surface reduction, and security verification. A shared codebase can support separate reports and demonstrations if the relevant faculty permit cross-course reuse.

## 3. Execution authority and boundaries

This document authorizes local implementation, local experiments using eligible data, reversible repository preparation, test execution, and preparation of release artifacts. It does not itself authorize sending emails, uploading private messages, purchasing services, collecting classmates' inboxes, changing an unrelated repository, or publishing through another person's account. Follow the authorization available in the actual execution session for external repository creation, pushes, releases, and hosting.

Do not make a permission email part of the default workflow. Use sources whose published terms cover the intended use. If a candidate requires institution-specific permission, research-only access approval, or correspondence with an author, exclude it from the default plan and find a suitable alternative. Record the exclusion and continue with independent work. A useful academic project must remain buildable without waiting for replies from a university.

There is no routine copyright-permission email for rights already granted under a suitable published licence, provided its conditions are followed. This does not remove attribution, provenance, privacy, or third-party rights checks. If a source's files contradict its landing-page licence, quarantine that source rather than guessing. Keep the application functional with the verified sources already available.

Do not request confirmation for ordinary color choices, component names, refactoring, baseline experiments, or local bug fixes. Use the defaults in this document. Ask only for a genuine missing decision that materially affects authorized external actions, course requirements, hardware access, or an irreversible operation. Prepare everything reviewable before asking for that decision.

Never overwrite personal files, expose credentials, commit signing keys, or run destructive cleanup against an uncertain path. Inspect the current working tree before modifying Git state. If the workspace already contains ScamSentry, resume from its recorded progress and verify the current implementation. Do not restart merely because a new session begins.

## 4. Mandatory release scope

The first release must contain a real Android application, a reproducible model-development pipeline, a privacy-conscious data workflow, and documentation that explains the product. The essential inputs are manually pasted text, text received through Android sharing, and screenshots selected explicitly by the user. A standalone URL entered into the same editor should receive the supported URL analysis.

Required app screens are onboarding, home, input editor, screenshot review, analysis progress, result details, optional local history, educational guidance, settings, and model/about information. Each screen needs functional actions, empty states, error states, and accessibility semantics. The APK must work with the core models already bundled after installation.

Required analysis behavior includes a message classifier, a transparent rule engine, uncertainty handling, and explanations that distinguish model evidence from observed patterns. URL analysis starts with syntax and lexical observations. A learned URL classifier becomes a release feature only after its feature extractor, calibration, evaluation, and Android parity checks pass.

Required engineering artifacts include dataset manifests, licence records, split definitions, training configurations, evaluation reports, model metadata, app tests, security review notes, setup instructions, a faculty demo script, and release checksums. Include screenshots captured from the actual application using fictional demonstration content.

The core must not depend on a cloud language model, payment account, subscription API, remote database, login, constantly running background service, or internet connection at analysis time. Those dependencies would increase setup burden and complicate the user's stated privacy and permission goals. A project website may explain the application and distribute approved artifacts without receiving user messages.

Complete and verify the core before adding optional research features. A small, measured, well-presented project is preferable to an unfinished collection of ambitious modules. The release definition is based on user-visible functionality and reproducibility, not the number of frameworks installed.

## 5. Growth scope and explicit priorities

Organize development into three levels. Level A is the semester release: English text analysis, screenshot OCR, Android sharing, local inference, optional history, explanations, evaluated baselines, and a public-ready repository. Level B adds a validated URL model, QR decoding with review, stronger robustness experiments, model comparison screens, and more detailed accessibility work. Level C covers multilingual research, fresh-data collection, optional intelligence integrations, and deployment beyond the academic demonstration.

Do not implement every level simultaneously. Level A should produce an installable vertical slice early. Each Level B feature must have an independent acceptance gate and should be removable without breaking core analysis. Level C belongs in a clearly labelled roadmap until data, time, and evidence justify it.

Prioritize features using expected user benefit, security effect, evaluation burden, dependency cost, and semester effort. A correction screen for OCR has higher priority than a decorative animated threat map. An honest uncertainty state has higher priority than a confidence dial that displays impressive but uncalibrated percentages. A reproducible APK build has higher priority than a second frontend.

Growth should build on stable interfaces. Define a model-engine abstraction, a versioned result schema, a source manifest, and theme tokens. These permit replacing a model, adding a language, or revising presentation without rewriting the entire application. Avoid speculative microservices and elaborate plug-in systems that have no immediate user.

Account creation, cloud sync, automatic inbox surveillance, remote threat feeds, and monetization remain outside the initial release. They can be evaluated later with separate privacy, licensing, security, and platform decisions. Document why each extension is useful, what evidence it needs, and what additional maintenance it creates.

Use a roadmap that includes entry criteria, expected experiments, and exit criteria. Roadmap entries must not appear as completed capabilities in README badges, screenshots, reports, or résumé text. The owner should be able to state exactly what the released version does today.

## 6. Dataset and asset rights workflow

Create a source registry before training. For every dataset, record its original publisher, exact version, persistent identifier when available, landing page, download location, access date, stated licence, applicable notices, provenance notes, permitted intended use, and unresolved issues. Keep an auditable local copy of the licence text or a reference with an integrity digest where lawful.

Use a traffic-light eligibility field internally: eligible, conditional, or excluded. Eligible means the published terms have been checked for the intended workflow and no unresolved conflicting terms have been found. It is not a legal guarantee about every underlying record. Conditional material must remain outside training and releases until its specific condition is resolved. Excluded sources must have a short reason.

For CC BY 4.0 material, preserve attribution, licence links, relevant supplied notices, and descriptions of changes. Avoid language implying endorsement by researchers, universities, banks, or government agencies. Separate the project's own code licence from data and third-party asset licences. A repository-level MIT licence must not imply that an external dataset has been relicensed as MIT.

Review raw records for unnecessary personal information and third-party content before using public examples. Published copyright permissions do not automatically resolve privacy concerns. Use original fictional examples in app demonstrations whenever possible. Retain a change log for redaction, normalization, filtering, and label mapping.

Evaluate pretrained checkpoints, fonts, icons, and copied code under their own terms. A model card saying “open” is insufficient without the actual checkpoint licence and any important restrictions. Avoid assets whose provenance is unclear. Prefer downloadable fonts with clear redistribution terms and small, attributed vector icon sets.

At release, generate a concise attribution page in the app and comprehensive third-party notices in the repository. Make the provenance process easy for a faculty reviewer to inspect. Do not hide material exclusions or pending licence questions inside an inaccessible notebook.

## 7. Initial dataset inventory

Start with the Mendeley “SMS PHISHING DATASET FOR MACHINE LEARNING AND PATTERN RECOGNITION,” version 1, DOI 10.17632/f45bkkt8pr.1. Its publisher page lists CC BY 4.0 and describes 5,971 messages: 4,844 ham, 489 spam, and 638 smishing. Treat those as publisher-described counts, then calculate actual parsed counts from the downloaded files. Verify the archive, schema, label spelling, encodings, duplicates, and licence consistency before using it. Reference [S02].

Use the UCI SMS Spam Collection as a possible auxiliary spam-detection benchmark. Its UCI page lists CC BY 4.0. It contains ordinary-message and spam labels, which do not establish that every spam item is fraudulent. Verify the actual file count rather than hard-coding a count from secondary descriptions. Record its known mixed-source provenance and possible overlap with other SMS collections. Reference [S03].

Use the UCI PhiUSIIL Phishing URL dataset as the initial candidate for URL research. Its page lists CC BY 4.0 and describes features derived from URLs and webpages. An offline app cannot assume webpage-derived variables will be available from a pasted address. Build a separate lexical feature pipeline from the raw URL and explicitly reject unavailable or target-leaking fields. Reference [S04].

Do not concatenate these datasets and call the result a universal scam corpus. Their units, labels, collection dates, source populations, and risks differ. Build separate tasks where necessary. The primary message task should use a defensible label taxonomy. Auxiliary spam labels may support a separate head or benchmark, but must not silently become smishing labels.

Expect these sources to be older than current scam campaigns. Their usefulness is as a reproducible starting point, not evidence of comprehensive protection against present-day Indian fraud. Include a fresh, independently authored challenge set and, if available through a lawful process, a separately reviewed contemporary evaluation source.

## 8. Adding data when existing data is insufficient

Expand data in response to a diagnosed weakness, not an arbitrary target row count. First determine whether the limitation is too few independent smishing campaigns, sparse legitimate banking messages, unsupported language, OCR corruption, misleading URLs, domain shift, or label ambiguity. More near-duplicates will not solve those problems.

For each new candidate, apply the same source-registry process before downloading large files. Prefer original research deposits or clearly licensed author repositories. A Kaggle or Hugging Face mirror may aid discovery, but the original source and its terms must be traceable. Do not treat a hosting platform's licence badge as clearance of content copied from elsewhere.

Investigate larger SMS corpora only as candidates. For example, the research repository listed in [S05] can be inspected for its exact data terms and source composition. It is not preapproved by this prompt. If no explicit data licence is found, omit its records from the default training pipeline. Code licensing and data licensing must be assessed separately.

Allow three expansion routes: another clearly licensed compatible corpus, original human-authored scenarios, or a separately authorized donation study with explicit consent and privacy review. The third route is optional and must not become a dependency for the semester project. Do not request inbox exports from classmates as a shortcut.

Synthetic data can enrich training variants and demonstrations, but label it explicitly and track its generation process. It must not be used as the only test evidence for real-world claims. Split by parent scenario before creating variants so paraphrases cannot leak across partitions. Audit whether synthetic text introduces formulaic cues that make the task artificially easy.

Maintain an expansion decision log with expected benefit, overlap estimate, licence result, label compatibility, and measured effect on the frozen development protocol. Keep a smaller reproducible pipeline if additional data does not improve independent performance. Report diminishing returns honestly.

## 9. Privacy inspection and data minimization

Perform a local privacy review before feature engineering. Identify patterns resembling phone numbers, email addresses, account numbers, authentication codes, personal names, addresses, tracking tokens, and URL parameters containing identifiers. Use automated detection to prioritize review, then inspect representative samples because simple patterns cannot reliably distinguish every identifier from ordinary text.

Preserve only information needed for the learning task. Where feasible, replace unnecessary specific values with typed placeholders or consistent local transformations. Keep structural features needed for detection, such as the presence of an address or numeric pattern, while reducing avoidable exposure. Assess whether each transformation changes the label meaning or destroys a relevant attack signal.

Do not publish a catalogue of real private records as an error-analysis appendix. Use local row identifiers, aggregate statistics, and carefully written summaries. If an example is essential, inspect it individually and prefer a fictional equivalent. Treat salted hashes as pseudonymous identifiers rather than claiming they are automatically anonymous.

Store raw downloads separately from processed learning tables. Exclude raw and sensitive processed data from Git by default. Avoid putting messages into filenames, experiment names, shell history, issue descriptions, or CI logs. A notebook's rendered output can leak data even if the underlying CSV is ignored, so audit notebook outputs before committing.

Record the privacy review's scope and limitations. A positive review does not prove that every record is free of all rights concerns. It means the documented checks were executed and identified issues were handled consistently. If a dataset's provenance or sensitive content prevents a reasonable academic workflow, exclude it.

For app users, analysis must be ephemeral unless they deliberately save a result. Raw screenshots should be discarded after processing by default. Clarify whether local history saves text, a redacted summary, or only derived metadata. Never use a user's analysis history as training data automatically.

## 10. Canonical records and provenance schema

Define a canonical message record with a stable local record identifier, source identifier, source version, original row reference, raw-text location, processed text, original label, normalized task label, language status, collection-date status, duplicate-group identifier, template-group identifier, privacy-review status, and eligibility flags. Keep unavailable metadata explicitly unknown instead of fabricating it.

Separate immutable source facts from derived fields. Source labels and source row references must remain recoverable. Derived label mappings, normalization versions, grouping algorithms, and privacy transformations need version identifiers so an experiment can be reproduced. A change in preprocessing must create a new processed-data artifact or digest.

Store canonical tables in a format suitable for scripts, such as Parquet with a documented schema. Provide a lightweight CSV export for faculty inspection using only non-sensitive columns and fictional examples. Define encoding, null handling, enum values, timestamp interpretation, and allowed lengths. Validate schemas before training.

Use separate canonical schemas for URLs and OCR robustness fixtures. A URL record needs its label source, parsed host status, grouping domain, lexical feature version, and any timing limitations. An OCR fixture needs the parent scenario, rendering configuration, image transformation parameters, expected text, and known ambiguity. Do not force incompatible units into one table.

Generate a dataset summary showing input rows, parse failures, quarantined records, unique exact texts, template groups, class counts, and final partition counts. Each reduction should have a reason. When several sources overlap, retain provenance associations even if a single learning record represents the duplicate group.

The data pipeline must fail visibly when required columns are missing, labels are unknown, row counts collapse unexpectedly, or files change without a manifest update. Silent fallback to a tiny toy dataset is unacceptable. Development fixtures may support app work, but every report must identify them as fixtures.

## 11. Label taxonomy and task separation

Use the primary message taxonomy ordinary, promotional-or-spam, and smishing-like, mapped transparently from compatible source labels. Keep original source terms in the manifest. “Ordinary” describes the dataset annotation, not a guarantee that a real sender is trustworthy. “Smishing-like” describes evidence consistent with the trained task, not a legal finding of fraud.

Create an explicit mapping table for every source. A two-class ham/spam dataset can support binary spam evaluation or an auxiliary learning objective. Its spam records must not be assigned to the smishing class without adequate independent annotation. Avoid treating promotional content as confirmed non-fraudulent simply because the source used the word spam.

Define annotation guidelines for original scenarios. Include impersonation, credential solicitation, payment redirection, implausible rewards, delivery-fee bait, urgent account suspension, and harmless transactional messages. Annotators should distinguish contextual suspicion from proof. A legitimate OTP notification and a request to reveal an OTP have materially different labels.

For manually annotated material, use independent review where practical, an adjudication process, and a disagreement log. When independent reviewers are unavailable, disclose single-annotator limitations and keep that set separate from the main benchmark. Do not invent inter-rater agreement numbers.

Maintain uncertainty labels in data where the evidence does not support a confident category. Such records can be excluded from supervised training, used for abstention research, or evaluated separately with careful documentation. Do not force ambiguity into an arbitrary class to increase sample size.

Keep message classification and URL classification distinct. A message may be suspicious without a URL, and a benign-looking message may contain a problematic address. Combine their outputs in the product through an explicit policy layer rather than pretending that one task's probabilities measure the other. The result schema must preserve the origin of every signal.

## 12. Exact duplicates, templates, and campaign grouping

Create a global duplicate analysis across all candidate message sources before partitioning. Exact grouping should include a raw digest and a conservative normalized-text digest. Keep the normalization procedure versioned because aggressive replacement can merge unrelated messages. Review a sample of merged groups.

Add template or near-duplicate grouping to capture messages whose wording differs only by names, amounts, codes, phone numbers, or domains. Use a documented similarity method such as token shingles with approximate candidate retrieval and a verified threshold. Report how thresholds were selected without using test labels to tune them.

Group synthetic variants under their parent scenario. Where known, group campaign variants and repeated sender templates. For URLs, group by registrable domain using a versioned public suffix source and inspect whether related domains or templated paths require additional grouping. Record limitations when campaign identity is unknown.

Build a graph of relevant duplicate relationships and assign whole connected components to a partition. Watch for overly broad components caused by generic templates; review component-size distributions and prevent a single mistaken edge from collapsing most of the corpus. Grouping quality affects the evaluation and needs its own spot checks.

Publish only safe aggregate overlap statistics. State how many unique groups each added source contributes, not merely its advertised row count. If a source duplicates the initial collection extensively, it may add little independent evidence even if it makes the training table appear much larger.

Do not claim an external holdout if its messages or close templates occur in training. Remove cross-source leakage using a preregistered rule or label the comparison as overlapping-source evaluation. All leakage checks should be executable assertions with saved counts, so a later dataset update cannot silently invalidate the reported results.

## 13. Partitioning and experimental isolation

Use a group-aware partition plan established before model tuning. A practical starting allocation is approximately 60 percent training, 15 percent development, 10 percent calibration, and 15 percent final test by records, while prioritizing whole-group isolation and sufficient minority-class groups. Exact percentages may vary because groups have unequal sizes.

Fit vocabularies, IDF statistics, scalers, feature selection, neural tokenizers trained from scratch, and sampling procedures only on the training partition or appropriate inner folds. Calibration data fits a specified calibration transformation. Development data supports model selection and decision-policy tuning. The final test remains locked until the pipeline and policy are frozen.

Within training, use group-aware cross-validation with stratification where feasible. Reduce fold count if too few independent smishing groups exist. Report group counts alongside record counts. A split that technically preserves classes but contains only a handful of independent attack templates requires cautious interpretation.

Save partition membership using stable identifiers and a manifest digest. Assert no exact, template, parent-scenario, or designated domain group crosses partitions. Use a separate external or time-based holdout only if its independence can actually be established. Unknown timestamps cannot support a temporal-generalization claim.

Choose and record how candidate calibration is fitted and how development thresholds are selected. Avoid fitting a calibrator on test predictions or repeatedly using test results to change the model. Development reuse is allowed for tuning but should be disclosed. If the final test prompts a substantive change, create a new evaluation version and obtain a fresh independent holdout where possible.

Produce a split report before the first serious experiment. Include class distribution, source distribution, language coverage, text lengths, and group sizes. If a partition is unrepresentative, diagnose the issue before training. Reproducibility should depend on saved memberships, not only on a random seed.

## 14. Text normalization and feature contracts

Maintain two text representations: the original input for user review and a versioned model representation. Do not destructively overwrite the user's text. Preserve offsets or a mapping strategy when explanations highlight portions of the original message. If normalization makes exact highlighting ambiguous, show a textual observation instead of an incorrect span.

Specify Unicode normalization, case handling, whitespace rules, line-break behavior, control-character policy, maximum input size, and truncation strategy. Choose transformations through development experiments. Retain an observation channel for zero-width characters, suspicious mixed scripts, and unusual punctuation so cleaning does not erase potentially useful evidence.

Avoid indiscriminate stemming, stop-word removal, and URL deletion. Small words and punctuation can matter in short messages. Compare simple character features with word features before adding more preprocessing. If amounts, phone-like sequences, or URLs are replaced, evaluate whether typed placeholders preserve the distinction between legitimate notifications and credential requests.

Treat tokenization as a deployable interface. Python and Kotlin must agree on character boundaries, whitespace collapse, normalization, n-gram construction, weighting, and class order. Unicode code points and UTF-16 code units differ; define the intended behavior and test emoji, accented text, combining marks, and surrogate pairs.

Set a bounded input policy, for example a configurable cap around 4,000 Unicode code points for the initial short-message task. Measure the actual corpus before finalizing that cap. Explain truncation to the user and avoid silently discarding the part containing a link or important request. Larger pasted content may need chunking or a limited-support response.

Version this contract independently of model weights. A preprocessing change can invalidate model calibration and parity tests even when the coefficient file is unchanged. Include representative golden examples and expected normalized outputs in both Python and Android test suites.

## 15. Exploratory analysis and the data sufficiency decision

Produce exploratory analysis that answers concrete engineering questions. Show class and source distributions, independent group counts, length distributions, vocabulary coverage, URL presence, script coverage, duplicate rates, label conflicts, and privacy-filter effects. Keep charts readable and captions factual. Do not dump dozens of plots that do not influence a decision.

Inspect source-specific shortcuts. A classifier may learn formatting, collection artifacts, artificial prefixes, or recurring domains instead of scam behavior. Compare performance by source and after removing obvious artifacts. Evaluate whether one class is mostly drawn from one source, which can make apparent discrimination a source-recognition task.

Use learning curves based on independent groups to decide whether more data is likely to help. Report mean and variability across suitable development splits, with minority-class support. If performance saturates while external errors remain high, seek broader coverage rather than more copies of the same campaign.

Create an error taxonomy before expanding the model. Categories should include legitimate urgency, promotional rewards, quoted scam discussions, unsupported languages, OCR mistakes, obfuscated domains, very short text, and mixed requests. Review development errors using privacy-safe summaries. Do not tune on the final test's individual messages.

Assess label quality. Investigate contradictory duplicate labels and suspiciously perfect performance. A disagreement may reflect incompatible definitions rather than a model failure. Resolve systematic mapping errors before training a larger architecture. Keep unresolved records quarantined or in a dedicated ambiguity set.

Finish this phase with a data sufficiency memo. State what the available data can support, what it cannot establish, what additional data would address a specific gap, and which sources were excluded. Continue building the app with a clearly labelled experimental model if appropriate, but do not claim broad real-world validation from an old, narrow benchmark.

## 16. Baselines and the first deployable classifier

Establish simple baselines before training a neural network. Include a majority-class baseline, a transparent rule-only baseline, a word-feature linear classifier, and a character-feature linear classifier. The majority baseline makes class imbalance visible. The rule baseline establishes what can be achieved through observable patterns without machine learning.

Use regularized multinomial logistic regression as the preferred first deployable message model. Compare it with a linear support-vector classifier using a properly fitted calibration approach and, if useful, a Naive Bayes baseline. Select parameters through group-aware development experiments. Do not declare a winner based only on training accuracy or one fortunate split.

For the portable linear path, prefer a precisely specified character n-gram analyzer with TF-IDF weighting and bounded vocabulary. Start with candidate n-gram ranges such as three through five characters, minimum document frequency of one or two, and vocabulary budgets around 20,000 to 60,000 entries. These are search candidates, not mandatory final settings. Document memory, export size, and quality for the chosen configuration.

Keep feature extraction consistent across desktop and Android. A small, well-tested Kotlin inference engine for sparse TF-IDF and linear scores may be simpler than converting unsupported text preprocessing operators into a neural runtime. Use direct export only when coefficient interpretation, class order, calibration, and preprocessing can be verified completely.

Provide an experiment table containing macro-F1, smishing precision and recall, ordinary-message false-positive rate, calibration measures, model size, and projected deployment complexity. Store raw aggregate metrics and configuration digests in machine-readable files. Label runtime measurements from desktop separately from measurements collected on a phone.

The first successful milestone is a trained model that can be exported and produces matching predictions in Python and Kotlin on golden examples. It does not need to be the final champion. Use that milestone to build the real app while research comparisons continue.

## 17. Modern model candidates and evidence-based selection

Evaluate a compact neural candidate after the baseline pipeline is trustworthy. A character-level convolutional network trained from scratch is a practical option because its architecture, preprocessing, and training data can be controlled. Keep sequence length, vocabulary, convolution widths, pooling, and parameter count modest enough for the owner's hardware and an ordinary Android phone.

A compact pretrained language encoder may be investigated when its exact checkpoint licence, provenance, export path, and device requirements are acceptable. Inspect the specific checkpoint rather than assuming all models in a family share the same terms. Record whether pretraining data and tokenization introduce additional constraints or uncertainty.

Limit the research search to a small number of justified families. For example, compare the strongest linear baseline, one compact character neural network, and one eligible compact encoder. A dozen barely tuned models will consume time without necessarily improving the application. Use an explicit experiment budget and a stopping rule.

For each candidate, evaluate both normal and perturbed inputs, calibration, support for uncertainty, and device deployment cost. A tiny improvement in an in-domain score may not justify much slower analysis, larger downloads, complicated tokenization, or reduced explainability. Select from the quality, latency, size, and maintenance trade-off rather than architecture prestige.

If distillation is explored, use only eligible training data and an appropriately permitted teacher. Mark teacher-generated labels as derived supervision. Do not use a remote commercial assistant to process real user messages silently or treat its decisions as verified ground truth. Compare the student against the original baseline using the same locked protocol.

Record the final champion selection in a model decision document. Include why rejected candidates lost and whether differences are meaningful relative to evaluation uncertainty. The released model can legitimately be a linear classifier if it delivers the best demonstrated product behavior.

## 18. Training configuration and compute discipline

Make training script-driven. Notebooks may support exploration, but the final pipeline must run from versioned command-line entry points and configuration files. A fresh environment should be able to validate sources, prepare records, create the frozen partitions, train baselines, evaluate candidates, and export the selected model without manual cell ordering.

Record the Python version, dependency lock, operating system, CPU, available RAM, accelerator availability, random seeds, dataset digests, code commit, and configuration. Reproducibility on CPU should be available for the baseline. Do not require expensive cloud hardware merely to reproduce the core project.

Use bounded hyperparameter searches. Start with a coarse grid or randomized search informed by model structure, then refine only when the development results justify it. Track the number of runs, wall time, and reasons for additional experiments. Avoid uncontrolled overnight tuning that repeatedly optimizes the same small development set.

For neural training, implement early stopping using the designated development protocol, checkpointing, gradient sanity checks, deterministic settings where feasible, and resumable optimizer state. Record known nondeterminism. Save the best checkpoint selected by the agreed metric rather than the last epoch or the highest training accuracy.

Fit class weighting or resampling only within training. Never oversample before partitioning. Preserve real evaluation prevalence and report class support. Synthetic augmentation must remain in training unless it is part of a separately labelled robustness evaluation. A balanced training set does not mean the deployed model's posterior estimates are automatically calibrated for real usage.

Generate a run summary after each experiment. Include status, duration, metrics, warnings, artifact paths, and whether the run is eligible for champion selection. Failed runs should not disappear from the record when they explain a decision. Keep experiment logs free of sensitive raw text.

## 19. Calibration, uncertainty, and abstention

Treat model scores and decision confidence separately. A softmax output or calibrated class probability is an estimate under the evaluation distribution; it is not proof about an individual message. The user interface must communicate this distinction without overwhelming the user with statistical terminology.

Compare calibration approaches suitable for the model and sample size. A simple scalar temperature for multinomial logits may be easier to export and less prone to overfitting than a flexible calibration map on a small calibration set. For binary models, an appropriately fitted sigmoid transformation may be practical. Use the dedicated calibration partition or a documented cross-fitting strategy.

Measure log loss, an explicitly defined Brier score, reliability curves, and a consistently defined calibration-error summary. State binning choices and sample counts. Calibration can appear good on a narrow benchmark and fail under distribution shift, so include source-wise and perturbation-wise observations.

Implement abstention through an explicit policy using available indicators such as prediction margin, entropy, input quality, supported language, and signal disagreement. Fit thresholds using development evidence. These indicators do not constitute a complete out-of-distribution detector, and the documentation must not claim otherwise.

An uncertain result should remain useful. Explain whether the difficulty comes from unclear text, insufficient context, mixed signals, or unsupported language. Offer editing or independent verification guidance. Do not color an abstained result green or hide it behind a reassuring percentage.

Report selective performance: coverage, error among accepted predictions, and error among abstained examples. A model that becomes accurate only by refusing most inputs must show that reduced coverage clearly. Store policy thresholds and the calibration version in the model bundle so desktop evaluation and Android behavior remain aligned.

## 20. Decision policy and risk categories

Define a deterministic policy layer that consumes message-model output, URL observations, optional URL-model output, OCR quality flags, language support status, and rule-engine observations. Keep the policy versioned independently from model weights. This allows an audit of why a result was displayed.

Use product states such as “Higher risk,” “Needs checking,” “Lower observed risk,” and “Unable to assess reliably.” Adapt the exact wording through usability feedback. Preserve the underlying three-class model output internally when available, but do not force the user to interpret technical training labels.

Separate priority from probability. A request to reveal an OTP can warrant a strong educational warning even when the statistical model is uncertain. Show that warning as an observed rule with its reason. Do not fabricate a mathematically calibrated overall percentage by averaging unrelated model probabilities and arbitrary rule points.

If a numerical composite score is used for research, define it explicitly as a policy index and evaluate it as a separate system. Prefer an ordinal presentation in the app unless the combined probability has a defensible training and calibration process. Never display “99 percent safe.”

Specify conflict handling. For example, a low message-model score plus a suspicious URL should lead to a result explaining the disagreement, not a hidden override that loses evidence. Unsupported language can limit the message interpretation while still permitting safe syntax-level URL observations.

Lock thresholds and policy rules before final testing. Test the complete system in addition to isolated classifiers. Include legitimate urgent messages, promotional offers, quoted scam examples, and ordinary messages containing unusual links. Document the cost trade-off between missed smishing indicators, nuisance warnings, and abstention.

## 21. Explainability and faithful explanations

Build explanations from observable evidence and model-compatible attribution. Each explanation item needs a source type, severity, concise title, plain-language detail, and an optional text span. Distinguish “The message asks for a one-time code” from “These text features increased the model's smishing score.”

For a linear model, compute contributions from active feature values and relevant class-weight differences. Explain that overlapping character fragments are statistical cues rather than complete semantic reasoning. Aggregate fragments carefully and avoid turning a sequence of n-grams into a confident claim about sender intent.

Use a curated rule engine for interpretable patterns such as credential requests, payment requests, urgency, unusual link structure, and mismatched display text. Keep rules configurable and covered by positive, negative, and boundary examples. A single word such as “urgent” must not independently prove fraud.

If a neural model is selected, use a lightweight attribution method only if it is stable enough and its latency is acceptable. Otherwise present model-level classification with transparent rule observations. Do not claim that template-generated prose reveals a neural network's internal reasoning.

Avoid remote generative explanations in the core app. Local templates can produce clear, consistent descriptions without uploading private content or inventing unsupported facts. Never write statements such as “This sender is a criminal” or “This bank has confirmed the message” without an actual authoritative basis.

Evaluate explanation usefulness through a small task-based study. Ask whether users can identify the concern and choose an appropriate next step. Track confusing explanations and revise them. Explanations should help the user reason, not merely decorate a prediction card with technical-looking labels.

## 22. Safe offline URL analysis

Treat every supplied URL as untrusted input. The core app must parse and examine the string without fetching it, resolving it through external services, loading it in a WebView, following redirects, or initiating a network request. Offline analysis can inspect syntax and lexical patterns but cannot establish current website reputation.

Support addresses with and without a scheme through an explicit parsing policy. Distinguish an email address, a domain-like token, a plain string, and a valid HTTP or HTTPS URL. Reject dangerous or irrelevant schemes for analysis actions. Never invoke a supplied intent URI or executable scheme automatically.

Extract and display the actual host carefully. Handle user-info sections, ports, percent encoding, trailing punctuation, IP literals, internationalized domain names, and multiple URLs. Use a versioned parsing implementation and golden tests that cover deceptive separators. Preserve the original string alongside a normalized display form.

Explain the limits of HTTPS, shorteners, long URLs, subdomains, and lookalike text. These are signals with context, not individually conclusive evidence. A long address can be legitimate; a short address can be malicious. An address containing a brand name outside the registrable domain must not be presented as belonging to that brand.

For any optional “open externally” action, require a deliberate user choice and show the destination host first. It should be absent from the high-risk primary action. Copying a suspicious URL may be useful, but make copying explicit and avoid automatic clipboard writes.

Use reserved example domains or non-resolving test names for fixtures and screenshots. Security tests should exercise parsing behavior without visiting live malicious pages. The cybersecurity contribution is rigorous defensive analysis, not executing suspicious content.

## 23. Learned URL model and feature availability

Build the URL classifier as a separate optional module with its own data manifest, training protocol, class order, calibration, metrics, and deployment artifact. Do not reuse message-model thresholds or interpret URL confidence as the probability that the entire message is fraudulent.

Create a feature-availability matrix before training on PhiUSIIL. Mark each original variable as locally computable, requiring an external lookup, requiring webpage content, requiring a training reference corpus, or prohibited because of target leakage. The production lexical model should be trained on features genuinely reproducible from a pasted URL and bundled reference assets.

Prefer recomputing lexical features directly from raw addresses. This prevents accidental reliance on precomputed columns whose generation procedure is unavailable. Candidate features include lengths, character composition, host structure, digit patterns, and carefully defined encodings. Evaluate whether those features generalize beyond source-specific collection quirks.

Group evaluation by registrable domain and inspect related-template leakage. A random row split can make domain memorization look like generalization. If collection dates are reliable, add a separately specified temporal test. Unknown or approximate dates must not be turned into precise chronological claims.

Compare a simple logistic model and a modest tree-based candidate on the same development protocol. Consider export complexity, feature parity, memory, and calibration. A tree ensemble may be suitable for tabular lexical features, but size and inference cost must still be measured.

If a credible lexical model cannot be validated within the semester, ship rule-based URL observations and label learned URL detection as research work. That is a valid release decision. Never train on webpage-derived features and then substitute zeros for those unavailable features in the phone app while claiming equivalent accuracy.

## 24. Screenshot import and OCR review

Implement screenshot analysis with user-selected images through the Android photo picker or another current platform-supported picker appropriate to the minimum SDK. Broad gallery access should not be required for selecting a single image. The source image remains untrusted content and must be decoded with explicit bounds.

Use a bundled OCR model for the supported script so first-use text recognition does not depend on a model download. ML Kit's Android documentation describes bundled and download-based deployment options; verify the chosen dependency and behavior at implementation time [S06]. On-device OCR capability does not by itself establish that every SDK makes no network connections, so inspect the final dependency configuration and test observed network behavior.

Before allocation, inspect image dimensions, declared size when available, actual stream limits, orientation, and MIME consistency. Downsample large images to a documented working range and close streams promptly. Defend against malformed files, unsupported formats, excessive dimensions, and content-provider failures without crashing or freezing the interface.

Always show extracted text in an editable review screen before final classification. Let the user crop or replace the screenshot when extraction is poor. Preserve punctuation and link structure where possible. Do not claim a universal OCR confidence percentage if the selected API does not provide an appropriate calibrated measure.

Use simple quality indicators such as empty extraction, implausible character patterns, unusually fragmented URLs, or very little text. Label these as quality heuristics. A confidently recognized wrong character can still change a domain, so the app should explain that OCR errors can affect link analysis.

Remove temporary image copies after the processing lifecycle unless the user explicitly saves them. Do not put source screenshots into logs, crash reports, model-improvement uploads, or demo galleries. When the app is interrupted, recover to a sensible editor state without persisting sensitive images unnecessarily.

## 25. Robustness evaluation and controlled perturbations

Build a dedicated robustness suite after freezing the primary data protocol. It should test whether expected behavior survives realistic changes rather than merely measuring ordinary benchmark accuracy. Each perturbation must have a parent record, transformation version, severity, and intended semantic effect.

Include spacing changes, punctuation variations, case changes, Unicode lookalikes, inserted zero-width characters, ordinary spelling mistakes, paraphrases, added benign context, and common OCR confusions. Some transformations preserve the original meaning while others may alter it. Review the latter and do not automatically inherit labels when meaning changes.

For screenshot tests, render fictional or eligible held-out text in several phone-like layouts and fonts, then apply resizing, compression, blur, and contrast changes. Evaluate text extraction separately from classification. Report OCR errors, classification on clean text, classification on extracted text, and the benefit of user corrections.

Partition parent scenarios before generating variants. Keep variants of test messages out of training. If augmented training is used, compare against an unaugmented baseline and evaluate on distinct transformations or independently authored variations. Avoid reporting gains on the exact transformation templates used for training as broad robustness.

Include negative controls: ordinary messages with urgency, legitimate promotional offers, a discussion quoting scam language, and educational text containing suspicious words. These expose false alarms caused by keyword dependence. Track the rate at which explanations incorrectly imply malicious intent.

Present robustness results by perturbation family and severity. State the number of independent parent groups. A single aggregate robustness score can hide a catastrophic failure on domain text. Record failures and decide whether to improve normalization, widen abstention, revise rules, or narrow the supported scope.

## 26. Evaluation metrics and statistical interpretation

Report macro-F1 and per-class precision, recall, F1, and support for the message classifier. Include a confusion matrix with readable class names. Overall accuracy is supplementary because the ordinary-message class can dominate. For the URL classifier, report its own binary metrics and group-aware evaluation details.

Define the ordinary-message false-positive rate precisely. Specify whether a positive means a smishing warning, any non-ordinary prediction, or a high-risk policy result. Report both relevant definitions when useful. Similarly, distinguish recall for the smishing class from recall for all unsolicited content.

For probability-based evaluation, report PR-AUC or average precision with the positive class and implementation identified, ROC-AUC where appropriate, log loss, and calibration measures. Do not label average precision and trapezoidal PR-AUC as interchangeable without explanation. Save the metric configuration so calculations remain consistent.

Compute uncertainty intervals using a method appropriate to the grouping and sample size. Group bootstrap is preferable to pretending near-duplicate rows are independent. State resampling unit, repetitions, seed, and interval method. For low false-positive counts, include a binomial interval or another justified estimate and make small-sample limitations visible.

Predeclare aspirational product targets such as strong smishing precision, useful recall, and a low ordinary-message warning rate. Choose exact targets after inspecting data support and expected user costs. Targets are development goals, not promised results. If no model meets them, retain honest experimental positioning and a conservative policy.

Evaluate the complete app pipeline on representative inputs. A strong text classifier can still fail because OCR, truncation, parsing, or a policy override behaves incorrectly. Provide component metrics and end-to-end metrics with denominators. Every published number must identify model version, split, sample size, and measurement environment.

## 27. Model export and artifact contract

Create a versioned model bundle containing the model parameters, feature configuration, normalization contract, class order, calibration transform, policy thresholds, training-source references, evaluation summary, and integrity digests. The bundle must be self-describing enough that the Android app can reject unsupported formats.

For a direct linear export, include vocabulary entries with stable feature identifiers, IDF values, coefficient arrays, intercepts, and numeric representation. Define whether scores use multinomial softmax or calibrated one-versus-rest probabilities. These formulations are not interchangeable. Export the exact trained decision function.

Avoid relying on arbitrary Python object deserialization in the phone app. Pickle or joblib artifacts can remain local training outputs if useful, but must not be loaded from untrusted sources or distributed as a mobile inference interface. Use a documented non-executable format with bounds checking.

For a neural candidate, choose one runtime after testing conversion and operator support. ONNX Runtime Android is a reasonable route for an eligible PyTorch model; LiteRT is another route when the model and converter support it [S11, S12]. Do not bundle both by default. Validate the specific runtime configuration rather than assuming that export success implies equivalent predictions.

Quantization is an experiment with a separate artifact identity. Measure quality, calibration, latency, memory, and class-level regressions after quantization. Recalibrate or revise thresholds through the development protocol if necessary. Never attach unquantized metrics to a quantized APK.

Generate a bundle manifest and validation command. A corrupted coefficient file, unknown normalizer version, missing class name, invalid array shape, or mismatched digest must produce a controlled model-unavailable state. It must never silently return a lower-risk result.

## 28. Cross-language prediction parity

Build golden fixtures before implementing the full Android analysis interface. Each fixture should contain a fictional or otherwise safe input, its normalized representation, active feature identifiers or selected intermediate checks, raw logits, calibrated outputs, final policy state, and model-bundle version.

Compare Python reference inference with Kotlin inference across ordinary text, empty input, whitespace variations, Unicode cases, repeated fragments, short inputs, maximum-length input, long URLs, and conflicting signals. Test both typical and boundary examples. Randomized non-sensitive strings can supplement manually designed fixtures.

Set numeric tolerances based on representation. For example, float32 logits may justify a small absolute tolerance that is different from float64 reference calculations. Record the chosen tolerance and inspect differences near decision thresholds. Require agreement in user-facing outcomes for the designated release fixtures.

The direct TF-IDF implementation must reproduce term counts, optional sublinear scaling, IDF multiplication, L2 normalization, coefficient indexing, and calibration exactly. If the Python training pipeline concatenates separately normalized feature blocks, Android must reproduce that structure. Do not accidentally normalize the concatenated vector a second time.

For neural deployment, compare token IDs, attention masks if used, input shape, padding, truncation, output class order, and postprocessing. Conversion can change unsupported operations or numeric behavior. Identify the first divergent stage instead of widening tolerances until tests pass.

Store parity reports with the release artifact. These checks are a central technical contribution because they show that the phone actually runs the evaluated model. An attractive demo that calls a different model, uses placeholder scores, or applies undocumented preprocessing does not meet the project standard.

## 29. Runtime budgets and phone measurements

Set provisional performance budgets, then revise them using real measurements. Suggested initial goals include warm text analysis within a few hundred milliseconds for a short message, OCR completion within a few seconds for a typical screenshot, responsive editing throughout processing, and acceptable installation size on an ordinary student phone. These are targets only.

Document the actual device model, Android version, RAM, chipset when available, build type, model bundle, thermal conditions, and input set. Distinguish debug and release performance. Measure cold initialization, warm inference, full-pipeline latency, p50 and p95, peak memory where feasible, and artifact size.

Use repeated runs with a written procedure. Exclude installation and first-run setup only if they are measured separately. Avoid a single best-case timing captured from a warm emulator. Do not convert desktop measurements into claims about phone latency.

Keep expensive processing off the main thread. Bound concurrency, reuse the loaded model safely, release image buffers, and cancel obsolete work. Rapid repeated taps, incoming shares during processing, navigation changes, and app backgrounding must not create unbounded tasks or stale results.

Measure battery behavior only through a defensible method. A short demo does not support a precise battery-drain percentage. For the initial release, demonstrate that analysis is user-initiated and no persistent scanner runs in the background. If deeper battery measurement is unavailable, state that limit.

Use performance findings to guide architecture. If a compact linear model is fast and reliable, avoid adding a heavy runtime merely for branding. If OCR dominates latency, optimize image handling and review flow rather than spending days shaving microseconds from a negligible classifier stage.

## 30. Native Android stack and dependency policy

Use Kotlin, Jetpack Compose, Material 3, lifecycle-aware ViewModels, Coroutines and Flow, Room for optional structured local history, and DataStore for simple settings. Use the current supported navigation and adaptive-layout libraries appropriate to the verified stable dependency set. Add dependency injection only in a form the team can understand and maintain.

Resolve compatible stable versions when implementation starts. Use the official Compose BOM and verify compatibility among the Android Gradle Plugin, Gradle wrapper, Kotlin, Compose compiler plugin, JDK, SDK levels, and KSP when applicable. Record exact versions in a version catalog and dependency manifest. Do not use dynamic version selectors.

Material 3 Expressive ideas can inform shape, hierarchy, and motion. Check actual component stability before adopting an API. The official release page distinguishes stable and prerelease versions [S08]. Do not introduce an alpha dependency simply to describe the frontend as current. Achieve a modern identity through design tokens and interaction quality.

For fonts, use Space Grotesk for selected headings and Inter for body text if their bundled files and notices are verified. A restrained monospace face may be used for hostnames and technical metadata, with its own licence check. Avoid loading fonts from the network at runtime. Limit font weights to control APK size.

Prefer a small set of bundled vector icons derived from a clearly licensed source. Do not add an enormous icon library for a handful of symbols. Keep navigation, data, inference, and design dependencies proportionate to the project.

Create a dependency policy covering version pinning, release notes, vulnerability review, licence notes, and update testing. A modern app is one whose stack is supported and coherent, not one that continually replaces working libraries without evidence.

## 31. Architecture and module boundaries

Use a clean but modest architecture with presentation, domain, and data responsibilities. The app should expose a small analysis use case that coordinates input validation, text preparation, model inference, rule observations, URL handling, and policy composition. Keep these components testable without launching the entire UI.

A practical structure can begin with the application module and a small number of reusable core modules or packages: analysis, model runtime, data, and design. Add feature modules only when boundaries improve build speed, ownership, or testability. Do not create a separate Gradle module for every screen.

Represent UI state as immutable data. The ViewModel receives user events and emits states such as editing, extracting, reviewing, analyzing, success, limited support, and recoverable error. Keep platform handles and large bitmaps out of long-lived state objects. Separate user input from derived results so edits invalidate stale analysis predictably.

Repositories mediate optional history and model metadata. Inference should not depend on the history database, a logged-in user, or a network client. A persistence failure must not prevent a user from analyzing text. Dependency injection should make fake engines and repositories easy to supply in tests.

Define concurrency ownership. A single analysis request has an identifier and a cancellable lifecycle. Results must be applied only if they correspond to the current input version. A new share intent or edit can cancel or supersede an older request without displaying its result over newer text.

Document architecture with a compact diagram and short explanations of data flow and trust boundaries. Avoid decorative diagrams that omit the difficult interfaces. The owner should be able to explain where sensitive data exists, when it is deleted, and how the tested model reaches the screen.

## 32. Domain models and result schema

Define explicit domain types for input, observations, model estimates, quality flags, policy decisions, and saved records. Avoid passing arbitrary maps between features. The schema should support serialization for local storage and safe exports while keeping internal implementation details out of ordinary user screens.

An AnalysisInput contains a request identifier, input version, source type, text, optional local image reference, and creation time. An AnalysisResult contains the analysis identifier, engine versions, supported-scope status, model estimates when available, observations, URLs, final policy category, explanatory limitations, and completion time.

Each observation includes a stable code, source type, severity, user-facing title, detail, and optional validated span. A span must specify its indexing convention. Never assume UTF-16 offsets from an Android editor match Python code-point offsets. Validate spans before rendering and fall back to a non-highlighted explanation if necessary.

Model estimates need class identifiers and explicit calibration metadata. Do not force probabilities into a result when the engine is unavailable or the input is unsupported. Represent absent estimates as absent, with a reason. Zero is a numerical estimate and must not be used as a generic error value.

Keep saved history separate from live results. A history record may contain only a user-approved summary and encrypted text, depending on settings. Version the serialized schema and provide migrations or clear compatibility behavior. A model upgrade should not silently rewrite an old analysis as though it had always used the new model.

Design the result schema so tests can compare meaningful fields deterministically. Exclude timestamps and random identifiers from golden prediction comparisons where appropriate. Preserve model, normalizer, and policy identifiers in all technical exports so later explanations can be traced to the correct release.

## 33. Navigation and interaction structure

Use three primary destinations: Analyze, History, and Learn. Put settings and model information in an accessible top-level action or secondary menu. The Analyze destination should expose the main input methods immediately instead of requiring the user to choose a technical analysis mode first.

On compact phones, use a clear bottom navigation bar with labels. On larger screens, adapt to a navigation rail or suitable two-pane layout when the verified library set supports it. Preserve destination state and scroll position where sensible. Do not stretch a phone layout into an unreadably wide tablet screen.

The main interaction sequence is input, review where necessary, analysis, and results. Users must be able to return to editing without losing their text unexpectedly. Editing a message invalidates the previous result until reanalysis. Make this visible so an old warning cannot be mistaken for a result on new content.

Support incoming Android share intents from a cold start and while the app is already open. Shared content should land in a reviewable editor. Do not run irreversible actions on receipt. If an analysis is in progress, handle the new input explicitly through replacement or a simple confirmation about preserving the current draft, rather than mixing contents.

Define back behavior for every transient state. Cancelling OCR should return to input selection or the editor. Leaving results should not force repeated analysis. A process restart should recover safely according to the documented privacy policy, even if that means asking the user to select the screenshot again.

Provide navigation tests for the complete workflow and major interruptions. Verify edge-to-edge insets, keyboard visibility, system back behavior, and state restoration. Navigation quality is part of the Mobile App Development contribution, not an optional polish task.

## 34. Visual identity and aesthetic direction

Create an original “quiet security studio” identity: dark navy-black foundations, crisp typography, purposeful color, subtle technical texture, and generous spacing. Draw inspiration from the owner's earlier dark NoC dashboard through precision, grids, and coordinated accents, while giving ScamSentry its own mobile-focused composition.

Avoid a generic hacker aesthetic with raining code, flashing danger lights, excessive neon, decorative world maps, or permanently animated radar. The product should feel understandable to an ordinary student or family member. Technical credibility should come from clear evidence and excellent interaction rather than visual noise.

Use a restrained background grid only in hero or empty-state areas. Its opacity should be low enough that it never competes with text. Cards should use solid elevated surfaces with carefully chosen borders. A small blue-to-purple glow can support the primary analysis area, but risk states should remain visually distinct.

Give each accent a semantic role: blue for main actions and navigation, purple for model or explanation features, red for higher-risk concerns, amber for caution and uncertainty, and green for lower observed risk or successful completion. Never make green imply guaranteed legitimacy. Pair every risk color with text and an icon.

Design the app icon as an original geometric mark suggesting careful inspection or a protective lens. It should remain recognizable at small sizes and in monochrome themed-icon form. Do not imitate an established security company's mark or use a university emblem as an endorsement cue.

Create a short visual rationale and token reference. The visual system should make screenshots coherent without turning each screen into the same stack of glowing cards. Use one dominant action per state and clear hierarchy between the result, supporting evidence, and secondary metadata.

## 35. Color, typography, spacing, and motion tokens

Begin with these proposed dark-theme tokens: background #080C14, surface #101722, raised surface #172131, border #26354B, main text #F2F5FA, supporting text #B6C2D3, action blue #70A7FF, higher-risk red #FF6B7D, caution amber #FFD166, lower-risk green #66D6A0, and explanatory purple #B89BFF. Verify contrast in actual combinations before freezing them.

Use Space Grotesk sparingly for prominent headings and Inter for body, labels, and controls. Keep body text around 16 sp with comfortable line height, then refine through device review. Avoid tiny uppercase labels and overuse of monospace. Hostnames may use a readable monospace treatment if it improves inspection.

Define a spacing scale based on small consistent increments, such as 4, 8, 12, 16, 24, and 32 dp. Choose a coherent radius scale for cards, chips, and controls. Touch targets should meet the platform's accessible minimum, with adequate separation between destructive and ordinary actions.

Motion should communicate state changes. Use short transitions for result appearance, expanding evidence, and navigation. Respect reduced-motion settings and avoid continuous animations while the app waits for user input. Never extend a fast inference with a fake multi-second scan just to appear sophisticated.

Implement theme tokens centrally. Risk category colors must come from semantic tokens rather than hard-coded values scattered across screens. Create a high-contrast review mode or appropriate accessible variant if the initial dark scheme causes problems. A light theme can be added if time permits, but the primary dark theme must be complete first.

Produce contrast checks, typography previews, and real-device screenshots at multiple font scales. Visual quality is achieved through verified composition, not merely by choosing attractive hexadecimal values.

## 36. Screen specifications: onboarding and Analyze home

Onboarding should be brief and skippable after the essential scope explanation. Explain that the user chooses what to analyze, that the core analysis runs locally, and that results are indicators requiring judgment. Describe whether the selected build includes any SDK-related communication in the privacy information.

The Analyze home screen should have a concise title, a short privacy/status line supported by the actual implementation, and a prominent message input area. Provide clear actions for pasting text, selecting a screenshot, and trying a fictional example. If QR support is absent, do not show a disabled camera-scanner button as though it were available.

Show a compact mode cue when the input contains a URL, but keep the workflow unified. The user should not need to understand separate classifiers. Provide a character counter and clear behavior for oversized input. Keep error messages close to the relevant control.

Use an empty-state example to teach the interaction. The example should be fictional, use reserved domains, and identify itself as a demo. Do not prefill a high-risk result or imply that the device has already been scanned. A recent-history preview can appear only if history is enabled and actual records exist.

The home screen's primary action becomes enabled when a supported input is present. Avoid ambiguous disabled buttons without explanation. The keyboard must not cover the action, and content must remain scrollable on a small device with enlarged fonts.

Acceptance includes successful first-launch analysis without an account, understanding of the input methods in a small usability task, graceful handling of an empty clipboard, and no permission request unrelated to the chosen action. The screen should be visually strong enough for the README hero while remaining an efficient daily interaction.

## 37. Screen specifications: editor, OCR review, and processing

The editor should display the exact text that will be analyzed, with source information such as pasted, shared, or extracted from a screenshot. Provide clear edit, clear, and analyze controls. Treat clipboard access as user-initiated and avoid reading the clipboard repeatedly in the background.

For screenshot review, display a safe image preview and extracted text in a layout that fits the device. Offer replace-image and edit-text actions. A crop workflow may be added when it improves extraction and can be implemented reliably. Preserve the user's corrections through screen rotation where consistent with the privacy policy.

Show quality flags using concise language: “Some text may be missing” or “Check the extracted link carefully.” Do not invent a certainty score. Highlight that a changed character in a URL may alter the destination, and allow analysis after the user reviews it.

The processing state should identify the actual current step, such as reading image text or analyzing the message. A bounded progress indicator is appropriate when total progress is unknown. Do not display fake percentages or pretend to contact threat databases when the core is offline.

Provide cancellation. A cancelled request must not save history, show a stale result, or keep unnecessary image buffers alive. A failed request should return the user to an editable state with a useful recovery action. Preserve text where safe instead of forcing repeated entry.

Test rapid taps, cancellation at several stages, orientation change, an incoming share during processing, and app backgrounding. The result must correspond to the current input version. This screen sequence should demonstrate disciplined state management in the faculty viva.

## 38. Screen specifications: results and evidence

Lead the results screen with an understandable category and a short explanation of its meaning. Follow with the most useful evidence, then practical next steps. Technical metadata belongs in an expandable section. Avoid leading with a large number that looks more authoritative than the evidence supports.

Show observed patterns separately from model interpretation. For example, a credential-request observation can appear as a concrete finding, while the model section states that the text resembles the smishing examples used in training. If the input is unsupported or OCR is unreliable, make that limitation prominent.

Display URLs as non-clickable text by default, emphasizing the actual host. Show normalized and original forms where necessary for inspection. Explain suspicious formatting without requiring the user to know URL syntax. Multiple addresses should remain individually identifiable.

Offer useful actions: edit and reanalyze, save locally when enabled, copy a carefully selected explanation, and read relevant guidance. Exporting or sharing a report must be deliberate and privacy-aware. A redacted preview should show what will leave the app.

Lower observed risk results should explain that the analysis found fewer relevant indicators within the supported scope. They must not say “verified safe,” “bank approved,” or “guaranteed legitimate.” An uncertain result should have equal design quality and a clear action path instead of looking like a broken classifier.

Acceptance includes comprehension tests, correct evidence ordering, accessible announcements, no accidental URL opening, and consistent rendering for long text, no URLs, multiple URLs, missing model output, and contradictory signals. All screenshots must use actual app states and fictional content.

## 39. Screen specifications: History, Learn, settings, and model information

History is optional and disabled by default unless a documented product decision establishes an equally privacy-conscious alternative. Its empty state should explain how saving works. Saved entries need a timestamp, result category, short user-approved summary, and model version, with clear delete controls.

Provide per-entry deletion and clear-all behavior. If saving raw text is available, explain it separately from saving derived metadata. Do not imply that clearing app history removes exported reports or screenshots stored by other applications. Avoid elaborate charts that suggest a user's personal scam exposure has been scientifically measured.

The Learn section should contain concise educational cards about impersonation, credential requests, payment pressure, link inspection, and independent verification. Clearly identify fictional examples. Include both malicious-looking and legitimate-looking cases so users learn contextual judgment rather than a few keywords.

Settings should cover history, retention where implemented, theme or reduced motion where relevant, privacy information, attribution, and model details. Do not include toggles for features that do not exist. Changing a privacy setting should have immediate and documented effects.

Model information should show the actual bundled version, supported languages and tasks, data-source links, evaluation context, measured metrics where available, and limitations. Distinguish benchmark results from live protection claims. Make technical detail accessible without crowding the main analysis flow.

Accept these screens only when their controls work and their content matches the release configuration. A privacy page copied from an unrelated cloud app is unacceptable. The owner should be able to show exactly where data is stored and what each deletion action does.

## 40. Accessibility and inclusive interaction

Accessibility is a release requirement. Use semantic labels, meaningful roles, appropriate traversal order, and text descriptions for risk indicators. Do not rely on red versus green alone. Screen readers should announce the main result and available recovery actions without reading decorative grids or repeated icons.

Test at large system font scales and on a compact display. Layouts must reflow without clipped warnings, hidden buttons, or overlapping text. Prefer flexible height and wrapping over fixed-height cards for content that can expand. Long domains need a readable inspection strategy that does not conceal important characters.

Use sufficient text and non-text contrast in the actual rendered design. Verify disabled states, selected navigation, outlines, focus rings, and error messages. A token that works on the background may fail on an accent-filled button, so check real pairings rather than only the palette list.

Support keyboard and switch-style navigation where feasible, including predictable focus after processing or errors. Respect platform motion settings and avoid flashing or rapidly pulsing risk indicators. Haptic feedback should be optional, brief, and never the only way a result is communicated.

Keep language plain and respectful. Explain smishing in ordinary terms before using the technical term extensively. Avoid shaming people for being deceived. The app should support careful decisions, including the legitimate choice to seek help or verify through a trusted channel.

Document the accessibility checks executed, device and tool used, defects found, and fixes made. The official Compose accessibility guidance is a reference [S13], but the project must demonstrate its own verification. Do not claim formal accessibility certification from a small internal review.

## 41. Local storage, encryption, backup, and deletion

Use app-private storage for transient processing and a Room database for optional saved history. Room does not encrypt its contents automatically. If the app saves raw messages, encrypt sensitive payload fields using an authenticated encryption design with a key protected through Android Keystore, or choose a separately reviewed database-encryption solution. Document the actual implementation.

For a field-encryption design, use a standard authenticated mode such as AES-GCM through supported platform APIs, a fresh unique nonce for each encryption, and explicit metadata describing the format version. Do not invent cryptographic algorithms or derive an encryption key from a hard-coded app constant. Keep searchable metadata minimal and recognize that timestamps and categories can still reveal information.

Set backup and device-transfer rules deliberately. Exclude sensitive database files, encrypted payloads where restoration would be unusable or inappropriate, temporary images, and key-dependent artifacts according to the platform behavior being targeted. Test actual restore or transfer behavior where possible. Do not assume one manifest flag proves that every transfer path is excluded.

Handle key invalidation, database migration, corrupted records, and storage-full conditions safely. A history failure must not turn analysis into a lower-risk result or crash the whole app. Explain when a saved entry cannot be opened and provide a controlled deletion option.

Delete temporary images and analysis buffers promptly. Clear-all should remove the application's saved records and relevant temporary files. Do not promise forensic secure erasure from flash storage. If cryptographic erasure is used, explain its scope and key lifecycle accurately.

Test the difference between history off, saving summaries, saving raw text, exporting a report, and clearing local data. The privacy document must reflect these distinctions. A user should not need to understand database internals to control what ScamSentry retains.

## 42. Permissions, intents, and platform attack surface

Keep the permission set minimal. The core should not request SMS inbox access, call logs, contacts, location, accessibility-service privileges, notification-listener access, or broad storage access. A user-selected screenshot and user-initiated text share should be sufficient for the initial workflows.

Camera permission is optional only if a camera-based feature is actually implemented. If QR decoding uses a selected image, no live-camera permission is needed. Every permission needs a documented feature, request point, denial behavior, and test. Remove unused permissions introduced through dependencies where possible and inspect the merged manifest.

Handle incoming intents defensively. Validate action, MIME type, extras, URI scheme, size, and read permissions. Do not trust a content provider's filename or declared type. Avoid resolving arbitrary paths from external input. Export only the components required for sharing and document why they are exposed.

Use a narrowly configured FileProvider or current secure sharing equivalent for deliberate exports [S15]. Grant temporary access only to the intended export file. Do not expose the entire private directory. Never use broad file URIs or world-readable storage as a convenience.

Avoid WebView rendering of user-supplied HTML or screenshot-derived content. Display plain text with safe escaping and bounded formatting. A pasted URL must not trigger an external intent automatically. Protect against repeated intent delivery causing duplicate saved records or unexpected navigation.

Test the merged release manifest and exported components, not only the source manifest. Dependencies and build variants can change the effective attack surface. Include these results in the cybersecurity report, with clear distinction between checked controls and untested assumptions.

## 43. Threat model and security verification

Create a threat model covering assets, actors, trust boundaries, entry points, abuse cases, and mitigations. Assets include user text, screenshots, local history, model integrity, release authenticity, and the user's understanding of the result. Attackers may control a message, URL, image file, share intent, or tampered model artifact.

Include input-based denial of service, deceptive URL rendering, content-provider abuse, accidental exports, sensitive logs, malicious dependency behavior, database exposure, backup leakage, model corruption, and misleading reassurance. Consider physical access to an unlocked device and the limits of app-level protections.

Use selected OWASP MASVS areas as a review framework [S14]. Map applicable storage, cryptography, platform, code, privacy, and network controls to actual tests. Mark non-applicable areas with reasons. Do not claim OWASP certification or full compliance from a self-authored checklist.

For each important threat, record impact, likelihood reasoning, mitigation, verification method, residual limitation, and responsible component. Keep the document connected to the code. An attack tree or compact data-flow diagram is useful only when it helps reviewers inspect a real decision.

Security testing must use owned devices, local fixtures, and authorized environments. There is no need to visit live phishing pages or attack third-party services to demonstrate defensive value. Parser stress tests and controlled malformed-image cases can provide substantial evidence without external harm.

Review the threat model after major architecture changes. Adding a backend, analytics SDK, remote model download, or cloud explanation changes trust boundaries and requires fresh assessment. Preserve the original offline core as a simpler, useful configuration even if later research adds optional services.

## 44. Dependency security, build provenance, and model integrity

Lock dependencies and retain a dependency inventory for Android, Python, and any optional tooling. Review licences and known vulnerabilities using appropriate available tools. A clean scan is a point-in-time observation, not proof of complete security. Record tool versions, scan dates, and unresolved findings.

Use official package repositories and verify dependency coordinates. Avoid copying arbitrary binaries from blog posts or repackaged model archives. Pin CI actions to reviewed immutable references where feasible and apply minimal workflow permissions. Keep automated changes reviewable.

The model bundle should include digests for each asset and compatibility metadata. Bundled assets inherit some protection from APK signing, but the app still needs schema checks and controlled failure behavior. A checksum is an integrity comparison, not an authenticity proof if an attacker can replace both the file and checksum.

Remote model updates are outside the first release. If implemented later, require an authenticated manifest, signature verification with a trusted key, version and rollback policy, atomic installation, compatibility checks, and a known-good fallback. Never load executable scripts from a model download.

Inspect exported vocabularies and fixtures for privacy. Character n-grams and model parameters can contain or reveal training-derived information; bundling weights does not automatically eliminate every disclosure risk. Avoid raw records in model metadata, debug examples, and vocabulary annotations. Report any limits of the privacy review.

Generate a release inventory connecting source commit, dependency lock, model digest, APK digest, build procedure, and evaluation report. This linkage is valuable academic evidence because a reviewer can see which software and model produced the demonstrated behavior.

## 45. Error handling, observability, and privacy-safe diagnostics

Define typed failures for unsupported input, oversized content, image decoding, OCR unavailable, model initialization, invalid bundle, inference, storage, cancellation, and export. Do not route all failures through a generic “Something went wrong” message. Give a short explanation and a recovery action appropriate to the failure.

A model failure must lead to an unavailable or limited-analysis state. It must never default to a green result, zero risk, or a random prediction. If rule observations remain available, identify them explicitly as partial analysis and explain that model analysis did not complete.

Keep operational logs free of message text, screenshots, full URLs with identifiers, tokens, and encrypted payload material. Use request identifiers, component names, timing summaries, error codes, and non-sensitive lengths. Debug builds can contain more diagnostics only when deliberate fixtures are used and their scope is documented.

Do not add remote crash reporting or analytics by default. If local diagnostics can be exported, show a preview and ensure redaction is tested. A developer should be able to reproduce common failures using fixtures without asking a user to send private content.

Monitor state correctness as well as exceptions. Examples include the wrong result attached to a new input, an old history record displayed after deletion, a spinner that never stops, or an accessibility announcement repeating indefinitely. These are product failures even if no crash occurs.

Create a troubleshooting guide organized around visible symptoms. Include model not loaded, screenshot text missing, share action absent, build incompatibility, device connection failure, and storage issues. Keep commands accurate for the user's operating system and avoid asking the user to reinstall everything before diagnosing the actual cause.

## 46. Unit and integration test strategy

Write tests that verify meaningful contracts and failure behavior. The important units are normalization, n-gram extraction, TF-IDF math, model scoring, calibration, policy decisions, URL parsing, explanation spans, retention settings, and export redaction. Avoid tests that merely repeat implementation details without checking observable behavior.

Use table-driven fixtures for common boundaries. Include empty and whitespace-only text, very short messages, maximum-length input, repeated characters, multiple URLs, unusual Unicode, malformed host syntax, and conflicting observations. Expected results should follow the written contract.

Integration tests should exercise analysis orchestration with real preprocessing and model inference, then controlled fakes for unavailable components. Verify that a storage failure does not break inference, a cancelled request does not save a result, and model corruption produces a controlled unavailable state.

Test database migrations and encrypted-payload recovery where implemented. Verify settings changes, deletion, key loss, and schema-version handling. Include a migration fixture for at least one realistic version change rather than assuming no future release will modify storage.

For data and ML code, test source validation, label mapping, duplicate grouping, partition isolation, train-only fitting, artifact schemas, metric calculation, and model export. Use small fictional fixtures for CI and reserve actual licensed data for appropriate controlled jobs.

Organize test reports by risk and user workflow. Coverage percentage can be supplementary, but it does not replace evidence that the difficult contracts work. When a defect is found, add a focused regression test if it protects important behavior without creating excessive maintenance.

## 47. UI, lifecycle, and device verification

Create UI tests for first launch, text analysis, screenshot review, uncertain results, model failure, history saving, deletion, settings, and navigation. Assert visible behavior and accessibility semantics. Use stable semantic identifiers instead of brittle coordinate-based scripts where the framework permits.

Verify lifecycle behavior manually or through instrumentation: rotation during editing, backgrounding during OCR, process death, cold-start sharing, warm-start sharing, rapid navigation, and repeated analysis. Document whether drafts are intentionally retained or discarded for privacy. Both behavior and messaging must match that choice.

Test several display configurations, including a compact phone, a typical phone, enlarged fonts, dark mode, keyboard-visible input, and a larger-screen layout if supported. An emulator can cover many configurations, but at least one real Android device is needed for credible runtime and sharing evidence.

Test fresh installation with network unavailable after the APK is transferred. Verify that the bundled classifier and bundled OCR resources actually work on first launch in that condition. If any feature requires prior initialization, document it honestly and revise the offline claim.

Capture actual screenshots after visual review. Inspect for clipping, excessive empty space, inconsistent risk colors, unreadable hostnames, overlapping system bars, and false placeholder metrics. Fix observed defects before producing README media.

Keep a device matrix with test status, build version, date, device details, and limitations. If only one physical phone is available, say so and complement it with emulators. Do not fabricate cross-device validation or claim universal compatibility.

## 48. ML acceptance gates and failure decisions

Define release gates as a combination of correctness, evaluation integrity, and product usefulness. Absolute gates include valid data eligibility records, no known partition leakage under the declared grouping policy, reproducible preprocessing, successful export validation, and desktop-to-Android parity for the release fixtures.

Performance goals need enough independent examples to interpret them. Specify targets for smishing precision, recall, ordinary-message warnings, abstention coverage, and latency as development objectives. Include the denominator and uncertainty interval when reporting whether a target was met.

Do not promise 99 percent or 100 percent accuracy. A result can be impressive and still fail an important class or source. Evaluate calibration, robustness, and external coverage alongside headline scores. Suspiciously high scores should trigger a leakage and shortcut-learning review.

If the model fails to reach a useful operating point, diagnose data quality and scope before escalating architecture size. Options include revising labels, adding independent data, narrowing supported inputs, increasing abstention, or releasing the model as an explicitly experimental assistant. Do not disguise a rules-only fallback as a trained model.

A release with unavailable learned URL detection can still succeed if its supported message workflow is complete and the URL observations are accurately described. A release with a corrupt bundled model or fabricated metrics cannot succeed. Distinguish optional research incompleteness from failures of core integrity.

Produce a gate report showing pass, fail, blocked, or not applicable, with evidence paths. A blocked physical-device benchmark must not be marked passed because desktop tests succeeded. The owner should have a concrete remaining-action list rather than a vague statement that everything is production-ready.

## 49. Repository structure and maintainable source organization

Create a single repository with a clear Android root, an ML workspace, documentation, scripts, and carefully reviewed media. Suggested top-level directories are android, ml, docs, scripts, and assets. Keep raw data, generated caches, local environment files, and signing material outside tracked source.

Within ml, separate source ingestion, schema validation, privacy review, grouping, partitioning, feature extraction, training, calibration, evaluation, robustness testing, and export. Small reusable modules are easier to inspect than one enormous notebook. Keep experiment configurations alongside their documented purpose.

Within android, retain a standard Gradle project that opens directly in Android Studio. Use a version catalog, a checked-in wrapper, appropriate formatting, and build variants with explicit behavior. Avoid a generated project that only works through an undocumented local script.

Create documents for architecture, data sources, data privacy, model card, evaluation protocol, evaluation results, threat model, test matrix, design system, Windows setup, faculty demonstration, limitations, roadmap, and project state. Consolidate small related documents if the repository becomes difficult to navigate, but preserve the information.

Use assets for original brand vectors and safe demonstration media. Keep generated APKs and large trained artifacts in suitable release packaging rather than ordinary source history when repository size would suffer. If a small model is tracked for reproducibility, document the size and licence decision.

Provide a root README that gives a visitor a useful first impression in under a minute: purpose, real screenshot, supported behavior, installation, measured evidence, privacy design, and links to technical documentation. Deeper detail belongs in linked documents rather than an unreadable wall of badges.

## 50. GitHub workflow and continuous integration

Initialize or inspect Git carefully and preserve existing work. Use meaningful commits that describe functional increments. Do not manufacture a long historical commit sequence to imply work occurred over months. The repository history should reflect actual implementation.

Create CI jobs for Python linting and tests, data-contract fixtures, model-export schema checks, Kotlin or Android checks appropriate to the project, and a debug APK build. Add instrumentation jobs only where infrastructure is available and reliable. A workflow that is always skipped must not be advertised as a passing test suite.

Keep CI free of private messages and permission-restricted data. Small fictional fixtures should exercise ingestion and prediction contracts. Full training can be a manual or scheduled development workflow only if the actual execution session authorizes scheduling and the resources are suitable. This prompt does not create a recurring task.

Use minimal workflow token permissions. Separate untrusted pull-request checks from release jobs that can access credentials or publish artifacts. Do not expose secrets to forked contributions or run unreviewed scripts with broad permissions.

Dependency automation can be configured conservatively if desired. Group compatible updates, avoid noisy untested upgrades, and require checks before merging. Do not remove attribution or falsify contributor history to make ownership appear different. Demonstrate the owner's contribution through clear code and documentation.

Before any public push or release, review tracked files and generated artifacts for secrets, personal data, restricted data, misleading claims, and incomplete licences. Follow the authorization provided in the execution session for external publication. Local readiness checks should be completed before a final publication decision is needed.

## 51. Windows development and setup experience

Target a setup path that works for the owner's Windows 11 laptop without requiring unnecessary operating-system changes. Use Android Studio, its supported JDK configuration, the Android SDK, a compatible Gradle wrapper, Git, and a Python virtual environment. Inspect installed versions before recommending new installations.

Provide a concise prerequisites table with verified versions and official download links. Distinguish required tools from optional ones. Do not assume the Node.js, MySQL, Vivado, or ESP-IDF installations from other projects are needed for this Android application. Keep this project's environment isolated.

Write PowerShell instructions with accurate quoting and paths. Avoid scripts that depend on administrator rights when ordinary user installation is sufficient. A bootstrap script should check prerequisites, create project-local directories, and explain missing components without changing unrelated system settings.

Explain physical-phone testing: developer options, USB debugging, driver considerations where relevant, device authorization, and selecting the correct run target. Include an emulator route for UI development and a real-device route for final performance evidence. Never tell the user to accept an unexpected debugging prompt without verifying the connected computer.

Distinguish offline app operation from offline development builds. Gradle and Python dependencies generally need to be downloaded during setup. Prepare caches before a classroom demonstration if an offline rebuild is desired. Do not promise that a fresh machine can build without internet unless the necessary dependencies are lawfully packaged and tested.

Validate setup from a clean clone or a reasonably isolated directory. Record the actual steps and common failures. The final instructions should let a classmate reproduce the core project without reconstructing the original engineer's environment from scattered chat messages.

## 52. APK packaging, signing, and release artifacts

Produce a debug APK for easy local testing and a documented release-build path. A debug APK is installable but should be identified as a development artifact. A public release should use a deliberately managed signing identity and include the information needed to verify the artifact.

Keep keystores, passwords, and signing configuration secrets out of source control and logs. Provide an example configuration with placeholders that are clearly labelled. Do not create a permanent signing secret and publish it as a convenience for evaluators.

Verify the release build independently from the debug build. Minification, resource shrinking, native runtime packaging, and keep rules can change behavior. Test model loading, OCR, sharing, encrypted history where present, and exports using the actual release variant.

Package the APK, SHA-256 checksum file, release notes, model manifest, evaluation summary, and appropriate notices. Identify source commit and build procedure. If reproducible byte-identical builds are not established, do not claim them; a documented repeatable build process is still useful.

Prepare a concise install guide covering sideloading from a trusted project source, version identification, update compatibility, and uninstall behavior. Explain that GitHub distributes the artifact while analysis runs on the phone. GitHub Pages can host project information, but it is not an Android execution environment.

Inspect the installed app on a fresh profile or device state where feasible. Verify no onboarding shortcut, cached model download, or development-only asset is required for the advertised offline workflow. A release is accepted only when the packaged artifact demonstrates the claimed behavior.

## 53. Project website and optional browser demonstration

The initial public presentation can be a static project page with screenshots, features, architecture, results, privacy explanation, source links, and APK download information. Keep it lightweight and accessible. It should help visitors understand and install the Android app without requiring an account.

A browser demo is optional and must not replace the Android project. If added, prefer fictional examples or genuinely local browser inference with an independently verified implementation. Clearly identify whether inputs remain in the browser or are sent to a server. Do not describe a server-backed demo as offline.

Avoid introducing a second production stack unless it serves a concrete need. A polished README and static page may be sufficient for the user's GitHub goal. The semester effort should remain focused on the mobile workflow and evidence.

If a backend is later authorized, define a separate threat model, API schema, retention policy, rate limits, abuse controls, deployment procedure, and operating cost. Never assume that hosting a model API permits collecting users' text for training. Data reuse requires its own explicit process.

Any demo form must have tested input limits, error handling, and privacy communication. Avoid storing submitted messages in server logs, query strings, analytics events, or public issue reports. External explanations or reputation APIs require their own terms review.

Prepare the presentation assets even if external hosting is not authorized in the current session. The deliverable can remain fully reviewable locally. Do not request domain purchases, paid hosting, or publication approval before the actual app and page are concrete.

## 54. Academic reports for the two subjects

Prepare separate report outlines around the same implementation, subject to faculty rules on reuse. The Mobile App Development report should emphasize requirements, UI design, state management, Android lifecycle, platform integration, local storage, accessibility, testing, and packaging.

The Cybersecurity report should emphasize threat analysis, phishing mechanisms, data rights and privacy, model limitations, secure input handling, adversarial robustness, mobile security controls, and measured false alarms. Both reports should reference the same actual release and model version.

Use truthful academic context: Deven Sachin Gaikwad, the applicable course and department, and the institution details verified by the owner or authoritative course material. Do not invent teammates, roll numbers, guide approval, submission dates, signatures, or institutional endorsements.

Include a clear problem statement, objectives, scope, literature or related-work review, design, implementation, experiments, results, limitations, and future work. Separate implemented capabilities from proposed extensions. Use real screenshots and reproducible charts with captions that identify what they show.

Document the original contribution precisely. Suitable contributions include group-aware evaluation across overlapping corpora, an audited local inference pipeline, OCR-aware analysis, a faithful explanation policy, and an integrated privacy-conscious Android workflow. Do not claim invention of phishing detection or a standard classifier.

Make both reports useful for viva preparation. Include simple explanations of why spam differs from smishing, why calibration matters, why larger datasets can leak, why APK inference differs from notebook evaluation, and why uncertainty is a legitimate system output. The owner should understand the reasoning behind every major diagram and result.

## 55. Faculty demonstration and evaluation evidence

Prepare a reliable demonstration lasting approximately six to ten minutes, with a shorter fallback path. Begin with the useful problem, then show a fresh install or known release version, an ordinary message, a suspicious message, screenshot review, uncertainty handling, and optional local history controls.

Use fictional scenarios and reserved domains. State that examples demonstrate behavior rather than estimate general accuracy. Show the actual measured evaluation report separately, including denominators and limitations. A few successful live examples do not constitute validation.

Demonstrate offline operation with a documented connectivity condition after the APK is installed. Show that no account or inference server is required. If an SDK has behavior that prevents a broad “no network” claim, explain the tested scope accurately.

Include at least one difficult case, such as a legitimate urgent notification or a screenshot with a mistaken character. Show how editing, explanations, and uncertainty help the user. This makes the project more defensible than a sequence of only obvious scam messages.

Prepare a local fallback recording of the same release and safe inputs. A recording is a backup, not a substitute for claiming that a failed live feature worked. Keep screenshots, APK, model card, report, and setup guide in a clearly labelled faculty package.

Create a rubric mapping that ties requirements to evidence for both subjects. Use it to identify missing work before submission. Do not promise full marks; demonstrate completeness, originality, technical depth, and a clear understanding of limitations.

## 56. Pilot study and user feedback

Run a small voluntary usability study if time permits. Its purpose is to improve comprehension and workflow, not to prove population-wide scam prevention. Use fictional tasks and avoid asking participants to reveal private inbox contents.

Select tasks such as analyzing a pasted message, correcting OCR, identifying the actual host, interpreting an uncertain result, and deleting a saved entry. Record completion, errors, confusing language, and qualitative feedback. Keep the study protocol short enough for realistic participation.

Explain what participation involves and what observations will be recorded. Collect the minimum personal information necessary; often none is needed. Do not publish participant names, private comments, or identifiable recordings without an appropriate separate basis.

Measure whether users understand the distinction between lower observed risk and verified legitimacy. Test whether a warning helps them choose independent verification rather than panic or blindly trust the app. Review accessibility concerns and reading difficulty.

Report participant count, recruitment method, task conditions, app version, and limitations. A small convenience sample can guide design decisions but cannot establish that the app prevents a particular percentage of fraud.

Turn feedback into a documented revision cycle. Identify the issue, change made, and follow-up observation where possible. Include one or two concrete improvements in the project report so the study demonstrates product thinking rather than merely adding a satisfaction chart.

## 57. Milestones, schedule, and scope control

Plan approximately twelve development weeks, adaptable to the actual semester. Early work should resolve data rights, project structure, environment setup, and a minimal vertical slice. The owner should see a real app analyzing safe text within the first few milestones.

A reasonable order is: feasibility and source audit; data pipeline and baseline; Android shell and direct inference; OCR and sharing; explanation policy and history; model comparisons and robustness; security and accessibility; release packaging and academic evidence. Parallelizable tasks can be organized without assuming access to additional agents or teammates.

Each milestone needs entry criteria, deliverables, verification, and a short review. Do not let visual polishing consume all available time before an evaluated model exists. Conversely, do not postpone the interface until every model experiment is finished.

Keep a prioritized backlog with must-have, should-have, and research items. If a deadline approaches, freeze optional features and complete documentation, failure handling, and packaging. Scope reduction should preserve the useful core workflow.

Include time for integration defects. Model export, Android release builds, OCR behavior, and physical-device testing often reveal issues that notebooks and previews miss. Reserve a dedicated verification period instead of treating the final week as purely report writing.

Record actual progress and revised estimates honestly. A schedule is a planning aid, not a guarantee. If a phase is blocked, continue independent work that remains valid and document the next action needed to unblock it.

## 58. Resume and continuation protocol

Maintain a project-state document with the current commit, completed milestones, pending decisions, active blockers, important commands, environment details, model candidates, and next concrete actions. Update it at meaningful boundaries and before ending a long execution session.

When resuming, read the state document, inspect Git status, review recent changes, and verify the referenced artifacts. Do not trust a stale checklist over the actual workspace. Preserve user edits and do not repeat expensive completed experiments unless a dependency, data, or implementation change justifies it.

Save experiment checkpoints and manifests so interrupted training can resume where supported. Keep model selection separate from transient process state. A partially written artifact must not be mistaken for a valid model bundle; use atomic writes or explicit completion markers.

Use stable evidence paths and record commands with their working directories. An instruction such as “run the previous command” is insufficient for a new session. Keep logs concise enough that the next engineer can understand the current problem quickly.

If a tool is unavailable, record the specific missing capability and a safe continuation path. Do not fill the gap with fabricated screenshots, imagined test results, or a fake public URL. Prepared-but-unexecuted steps should remain marked pending.

End each execution session with what changed, what was verified, what remains uncertain, and the next step. Continue autonomously within the authorized scope instead of repeatedly asking whether to proceed with ordinary implementation work.

## 59. Public communication, authorship, and résumé claims

Write a professional README and release description that lead with the useful behavior. Explain supported inputs, local processing, model evidence, and installation. Avoid exaggerated labels such as perfect, unbreakable, government-grade, or guaranteed scam protection.

List actual contributions and actual measurements. A résumé bullet may describe a Kotlin application with on-device classification, OCR, secure optional history, and group-aware evaluation only after those capabilities exist. Insert measured values only from the release evidence.

Attribute datasets, libraries, and adapted material clearly. The owner can still demonstrate substantial original engineering while using standard tools. Do not obscure external contributions or imply that a university or dataset author endorses the product.

Separate benchmark metrics, robustness metrics, device measurements, and usability observations. They answer different questions. A high benchmark score should not become a claim that the app prevents the same proportion of financial losses.

Include limitations in a visible, concise place: supported language, dataset age and coverage, uncertain inputs, offline URL limits, OCR effects, and the absence of guaranteed authenticity verification. Practical limits should inform user decisions without overwhelming every screen.

Keep screenshots consistent with the actual release. Do not display fabricated user counts, attack totals, testimonials, app-store ratings, partner logos, or model accuracy. The project's credibility should come from transparent evidence and a complete workflow.

## 60. Definition of done and final handoff

The core project is complete when a clean setup can produce the documented application, the bundled model matches its evaluation artifact, the required input workflows function, uncertainty and errors are handled clearly, and sensitive data behavior matches the privacy documentation.

Required evidence includes source manifests and licence notices, partition-isolation checks, baseline comparisons, calibration and policy configuration, parity fixtures, actual test reports, at least one real-device verification record, release-build checks, and safe screenshots. Missing optional research features must be listed accurately.

The handoff should contain source code, installation instructions, reproducibility commands, APK or an explicitly documented build limitation, model card, evaluation report, security notes, design reference, faculty demo script, and subject-specific report outlines. Provide exact artifact locations and versions.

Before declaring completion, inspect the repository and release package as a new reviewer would. Verify links, command working directories, ignored data, placeholders, notices, screenshot accuracy, and the relationship between source commit, model bundle, and APK. Correct contradictions before delivery.

If external GitHub publication is authorized, perform it only after the package is ready and the privacy and rights checks have been executed. If publication is outside the current authorization, deliver the complete local release package and a concrete publication step. Do not send permission emails as a default substitute for using eligible sources.

The final report to the owner should be concise and candid: what was built, what was measured, where to install or inspect it, and any material remaining limitation. Do not call the application perfect. Make it a project the owner can explain, reproduce, improve, and proudly demonstrate.

---

## Appendix A. Copy-ready implementation launch instruction

Use the following instruction with this document when implementation is intended:

> Execute the complete ScamSentry specification in this document. Begin by inspecting the workspace and environment, preserving existing work, and creating an evidence-based implementation plan. Use the stated defaults for routine decisions. Build the Android application and reproducible ML pipeline incrementally, with real tests and artifacts. Keep the core offline and avoid permission-dependent datasets. Do not send emails or collect private inbox data. Verify source licences and dependency compatibility at execution time. Select the deployed model through measured quality, calibration, robustness, and phone performance. Continue through the authorized local implementation and release preparation, updating the project-state document at each milestone. Report actual results and unresolved limitations without inventing accuracy, screenshots, public links, or completed checks.

The implementation agent's first response should identify its working assumptions, inspect prerequisites, and start the first concrete task. It should not return only another proposal or ask the owner to repeat the details already present here. If the repository already exists, the first task is to resume it rather than generate a replacement project.

Use the owner identity and academic context only where relevant. Do not expose personal contact information by default. Repository ownership, publication destination, signing identity, and external hosting must follow the actual execution session's authorization. The local build should remain usable even when those external decisions are pending.

At each milestone, summarize the user-visible outcome and supporting evidence. A concise status update should explain what has been learned and what the next verification will resolve. Keep implementation details in the repository so the owner can inspect them without scrolling through an enormous chat transcript.

## Appendix B. Concrete repository contract

Implement a structure with these responsibilities. Paths are proposed contracts; if renamed for a sound reason, update every linked command and document consistently.

| Path | Responsibility | Required evidence |
|---|---|---|
| `README.md` | Product introduction, installation, screenshots, results | Links checked against release |
| `android/` | Native Android project | Opens and builds with documented tools |
| `android/gradle/libs.versions.toml` | Pinned dependency catalog | Compatible stable versions |
| `android/app/` | App entry, navigation, platform integration | Unit and device checks |
| `android/core-analysis/` or equivalent package | Model and policy orchestration | Contract and failure tests |
| `android/core-design/` or equivalent package | Theme and reusable UI components | Preview and accessibility review |
| `ml/pyproject.toml` | Python project and command definitions | Reproducible environment |
| `ml/configs/` | Training and evaluation configurations | Versioned run identifiers |
| `ml/src/scamsentry_ml/` | Script-driven ML implementation | Meaningful tests |
| `ml/tests/` | Safe fixtures and pipeline checks | CI report |
| `ml/manifests/` | Source and artifact metadata | Schema validation |
| `docs/DATA_SOURCES.md` | Sources, licences, provenance | Source eligibility review |
| `docs/MODEL_CARD.md` | Scope, training, evaluation, limits | Matches bundled model |
| `docs/EVALUATION_PROTOCOL.md` | Frozen split and metric definitions | Pre-test version recorded |
| `docs/EVALUATION_RESULTS.md` | Actual measurements and interpretation | Machine-readable inputs |
| `docs/THREAT_MODEL.md` | Trust boundaries and mitigations | Linked verification |
| `docs/PRIVACY.md` | Actual data handling | Matches app configuration |
| `docs/DESIGN_SYSTEM.md` | Tokens, typography, screen patterns | Real screenshots |
| `docs/SETUP_WINDOWS.md` | Windows installation and debugging | Clean-setup review |
| `docs/FACULTY_DEMO.md` | Demo and fallback procedure | Rehearsal record |
| `docs/PROJECT_STATE.md` | Resume context and next actions | Updated at milestones |
| `docs/DECISIONS/` | Important architecture decisions | Reasons and alternatives |
| `scripts/` | Validation, packaging, and utility entry points | Documented usage |
| `assets/demo/` | Fictional scenarios and safe media | Privacy review |
| `THIRD_PARTY_NOTICES.md` | Asset and dependency notices | Release review |
| `LICENSE` | Licence for the owner's eligible source code | Scope clearly stated |

Do not commit raw downloaded datasets, private processed tables, sensitive logs, local device captures, API keys, keystores, or huge experiment caches. Keep an example environment file only if environment variables are actually used. A project with no server should not accumulate unused cloud configuration.

A release artifact directory may be generated locally but should not become a substitute for clear source organization. Retain a manifest connecting each artifact to its source commit and model bundle. Use archive names that identify project, version, platform, and release status without claiming finality prematurely.

## Appendix C. Source and experiment manifest examples

Create schema-validated manifests. The following YAML illustrates required meaning; the implementation must replace unknown fields with verified values and must not treat this example as a completed audit.

```yaml
source_id: mendeley_smishing_v1
title: SMS PHISHING DATASET FOR MACHINE LEARNING AND PATTERN RECOGNITION
publisher: Mendeley Data
version: "1"
doi: 10.17632/f45bkkt8pr.1
landing_page: https://data.mendeley.com/datasets/f45bkkt8pr/1
published_license: CC-BY-4.0
license_url: https://creativecommons.org/licenses/by/4.0/
checked_at: null
download_sha256: null
actual_rows: null
actual_columns: []
eligibility_status: pending_file_review
intended_tasks:
  - message_three_class
source_labels:
  - ham
  - spam
  - smishing
privacy_review:
  status: pending
  report_path: null
overlap_review:
  status: pending
  report_path: null
notes:
  - Verify archive contents and any conflicting notices before use.
  - Preserve original labels and source row references.
```

The manifest should support a state transition from pending to eligible only after the relevant checks run. Do not initialize every source as eligible because its landing page appears promising. Add fields for privacy transformation version, source exclusions, and attribution text as implementation requires.

An experiment manifest should identify a run independently of human-readable filenames:

```yaml
run_id: baseline_char_lr_001
status: planned
task: message_three_class
code_commit: null
environment_lock_digest: null
source_manifest_digests: []
processed_dataset_digest: null
split_manifest_digest: null
normalizer_version: ascii_nfkc_v1
feature_version: char_ngram_tfidf_v1
model_family: multinomial_logistic_regression
seed: 42
fit_partition: train
selection_partition: development
calibration_partition: calibration
final_test_accessed: false
metrics_path: null
artifact_path: null
warnings: []
```

Seed 42 is a reproducibility convenience, not a source of model quality. Use additional seeds or suitable resampling when the experiment question requires variability estimates. Record whether repeated runs share partitions and which source of randomness changes.

A model-bundle manifest should include schema version, task, class order, normalizer, feature extractor, parameter files, calibration, policy version, supported scope, size, and digests. No field may imply a passed evaluation until that report exists. An experiment status of planned, running, failed, or completed must be distinct from release eligibility.

Every manifest validator should produce actionable errors. A missing checksum should be reported as missing, not replaced with a dummy string that looks valid. If a source changes, preserve the prior manifest and require a new processed-data identity. This makes later debugging possible without relying on memory.

## Appendix D. Reference inference mathematics and parity checklist

For the portable linear baseline, choose one exact feature contract and implement it in both languages. A suitable starting design uses a custom analyzer over a normalized Unicode-code-point sequence. Define normalization with NFKC, explicit ASCII lowercasing for A through Z, and a fixed documented whitespace set. Preserve unsupported scripts and flag language support separately rather than pretending ASCII processing validates every language.

The final normalizer may differ if development evidence supports it, but the version name and golden fixtures must change. Python's full case-folding and Kotlin's ordinary lowercasing are not automatically equivalent. Similarly, Python string iteration and Kotlin UTF-16 indexing require deliberate code-point handling. These details should be visible in tests.

For each configured n-gram length, count overlapping n-grams in the normalized sequence. Keep only vocabulary entries learned from training. If a feature occurs c times, use either raw term frequency c or a documented sublinear value of 1 + ln(c). Choose the option through development experiments and export it.

With smoothed IDF, one common definition is:

```text
idf(j) = ln((1 + N_train_documents) / (1 + document_frequency_train(j))) + 1
raw_weight(j) = term_frequency(j) * idf(j)
norm = sqrt(sum_j raw_weight(j)^2)
feature(j) = raw_weight(j) / norm, if norm > 0
feature(j) = 0 for an all-zero feature vector
logit(k) = intercept(k) + sum_j coefficient(k, j) * feature(j)
```

Use exactly the formula fitted during training. If the chosen library uses different smoothing, a different analyzer, multiple feature blocks, or another normalization arrangement, export that actual behavior. Do not copy these formulas mechanically onto an incompatible pipeline.

For multinomial logistic regression, convert logits through a stable softmax by subtracting the maximum logit before exponentiation. If scalar temperature calibration is selected, apply the fitted positive temperature in the documented position before softmax. A one-versus-rest calibrated classifier requires its own exported equations and cannot be treated as multinomial softmax.

Use sparse accumulation so only active features contribute to the dot product. Bound feature-map allocation by input size and vocabulary limits. Load immutable model parameters once, then reuse them safely. Avoid creating a dense 60,000-element vector for every small message if sparse inference is straightforward.

Test intermediate values as well as final categories. Required checks include normalized text, n-gram count samples, vocabulary identifiers, IDF values, vector norm, selected weighted features, raw logits, calibrated estimates, and policy outcome. If only final categories are checked, substantial numerical bugs can remain hidden on easy examples.

Include fixtures for repeated whitespace, tabs, line breaks, emoji, combining marks, full-width characters, isolated surrogate handling, empty features, repeated n-grams, and threshold-adjacent scores. Define how invalid input sequences are rejected or replaced. The goal is deterministic behavior, not a fragile approximation that works only on plain ASCII demonstrations.

For explanation contributions, choose a comparison such as the predicted class versus the ordinary class and compute active-feature contributions consistently. Include the intercept in technical diagnostics but do not present it as a highlighted word. Group overlapping fragments only through a documented method. A statistical contribution should never be rewritten into an unsupported statement about a sender's identity.

The final parity report should identify the reference environment, Android build, model digest, fixture digest, numeric tolerances, maximum observed difference, class agreement, and policy agreement. A change to normalization, coefficients, calibration, or thresholds must trigger the relevant parity checks before release.

## Appendix E. Concrete acceptance scenarios

Implement the following scenarios as a mix of automated tests, instrumentation, and documented manual checks. The expected behavior describes the product contract; it does not prescribe fabricated model probabilities. Use fictional messages and reserved domains. Classifier-dependent outcomes should be established through the frozen model and reported honestly.

### E01. Ordinary coordination message

Analyze a simple fictional message arranging a meeting or asking about class notes. The app should complete analysis, display whatever the validated model and policy support, and avoid warning solely because the message contains a time or number. Verify that the explanation does not invent a URL, credential request, or sender identity. Keep this case in usability testing because nuisance warnings on ordinary communication reduce trust.

### E02. Legitimate urgency

Use a fictional ordinary notice with a deadline, such as a reminder to bring a laboratory notebook before a scheduled session. Urgency may be observed, but it must not independently establish smishing. Evaluate the model and policy for false alarms. If the system warns, document the failure and inspect whether rules are overweighted. Do not relabel the scenario merely to make the model appear correct.

### E03. Credential disclosure request

Use an original fictional scenario asking the recipient to reveal an authentication secret. The app should identify the request through its supported observations and show proportionate guidance about independent verification and keeping secrets private. It must not claim that a particular real organization confirmed the attack. Verify that the result remains understandable without a numerical confidence display.

### E04. Promotional offer

Use a clearly fictional promotional message that resembles ordinary advertising without independently establishing fraud. The system should preserve the distinction between promotional content and smishing. Inspect which class labels and product state appear. A warning about unsolicited promotion may be appropriate, but the explanation must not turn a promotional annotation into proof of credential theft.

### E05. Quoted scam discussion

Analyze an educational sentence that quotes a suspicious phrase while discussing how to recognize it. This tests contextual limitations and false positives. The app may be uncertain, but it should not fabricate a personal threat to the user. Record whether the model relies too strongly on isolated scam-like fragments and whether explanation wording remains faithful to the actual input.

### E06. Standalone ordinary URL

Enter a reserved example address with a simple host and path. The app should recognize it as an address, show its actual host, and explain the limits of lexical analysis. It must not claim the site has been checked against live intelligence. Verify that no DNS lookup, network request, or automatic browser launch occurs as part of the core analysis.

### E07. User-info deception

Use a reserved-domain URL whose apparent familiar name occurs before an at-sign while the actual host is different. The parser must identify and emphasize the true host according to its documented policy. The observation should explain the structure in plain language. The app must not decide ownership based on a brand-like substring earlier in the string.

### E08. Internationalized host

Use a controlled fixture containing an internationalized domain representation and a corresponding encoded form. Verify consistent parsing, display, and warnings about potentially confusing characters when supported. Do not classify every non-ASCII domain as malicious. Test copy behavior and screen-reader output so users can inspect the destination without hidden normalization surprises.

### E09. Several URLs in one message

Provide a fictional message with two or more reserved addresses, including trailing punctuation. Each detected address should have an independent identity, parse result, and observation list. The aggregate result should explain meaningful disagreement rather than silently keeping only the first address. Ensure extraction does not swallow adjacent sentences or drop the final character of a hostname.

### E10. Unsupported scheme

Provide a non-HTTP scheme or intent-like string as plain text. The app should not invoke it. It may explain that the input is outside supported URL analysis and continue supported text observations if appropriate. Verify that no external component launches merely because the string appears in a shared message or OCR output.

### E11. Clean screenshot

Render a fictional message in a readable phone-style image. Select it through the picker on a fresh install. OCR should produce editable text and permit analysis without broad gallery permission. Compare expected text, extracted text, and final result. Record actual timing on the device and confirm that the source image is handled according to the retention policy.

### E12. OCR-altered link

Use a controlled screenshot where a character in a reserved URL is difficult to distinguish. The review screen should make correction possible. After editing, reanalysis must use the corrected input version, not a cached result from the original OCR output. This case should demonstrate why a review step is part of the product rather than an optional extra.

### E13. Empty or unreadable screenshot

Select an image containing no useful text or text below the supported quality. The app should show a clear extraction limitation with replace-image or manual-entry actions. It must not run a classifier on an empty string and display a reassuring result. Verify cancellation and temporary-file cleanup.

### E14. Oversized or malformed image

Supply a controlled oversized image or a safe malformed-file fixture. The decoder must enforce limits and return a recoverable error. The main thread should remain responsive. Verify that the app does not allocate unbounded memory, retain a partially decoded bitmap, or repeatedly retry the same unsupported input without user action.

### E15. Unsupported language

Analyze text outside the validated language scope. The result must state that message-level interpretation is limited. Supported syntax observations may remain available if clearly identified. Do not present an English benchmark score as evidence for this input. Check that the interface can still render the script properly and offer editing or independent verification guidance.

### E16. Obfuscated text

Apply a controlled spacing or Unicode perturbation to a held-out fictional scenario. Compare the clean and changed inputs. Record changes in prediction, abstention, and explanation. If normalization removes the perturbation, verify that the original text remains available and any relevant suspicious-format observation is preserved. This is a robustness measurement, not a guarantee of evasion resistance.

### E17. Cold-start sharing

Close the app, then share safe text into it from another application. The correct content should appear in a reviewable editor, with source indication and no automatic external action. Test repeated share delivery and lifecycle restoration. The app must not create duplicate history records merely because the activity receives the same intent again.

### E18. New input during analysis

Begin OCR or inference, then submit a different input through a share or edit action. The old request should be cancelled or superseded according to the written state policy. The eventual displayed result must correspond to the current input identifier. Test this race deliberately because it can produce misleading results without a crash.

### E19. History disabled

Analyze several fictional messages with history off. Restart the app and inspect its normal history interface. No saved entries should appear. Use appropriate local inspection to verify the documented storage behavior, including temporary artifacts. Do not equate an empty screen with proof that no sensitive data was retained elsewhere.

### E20. Save and delete

Enable the supported history mode, save an entry, reopen it, delete it, and clear all remaining records. Verify that the content and model version are correct and that deletion removes the app's saved record. Test encrypted payload access if implemented. Explain that previously exported files are outside this local deletion action.

### E21. Missing or corrupt model

Use a test build with a deliberately invalid bundle. The app must show model unavailability and preserve supported partial observations only if clearly labelled. It must not generate a default green result or use random probabilities. The error should identify a recovery action without exposing internal paths or sensitive diagnostics.

### E22. Fresh offline launch

Transfer and install the APK, disable network connectivity in a documented way, then launch and analyze text and a screenshot. Confirm the bundle and OCR resources are available immediately. Record the precise build and device. If a component requires an initialization download, revise the architecture or the offline claim before release.

### E23. Export redaction

Save or share an analysis report containing a fictional phone-like token and URL parameter. The preview must show exactly what will be exported. Test default redaction, explicit inclusion behavior if offered, temporary-file access, and cleanup. A report should not leak additional history, image metadata, internal paths, or diagnostic data.

### E24. Large fonts and assistive reading

Run the main workflow at enlarged font settings and with a screen reader. Verify result announcement order, readable links, accessible buttons, and a usable correction editor. Decorative icons and grids should not pollute reading order. Record observed problems and fixes rather than checking an accessibility box without testing.

### E25. Release-build consistency

Install the actual release variant and repeat the core text, OCR, sharing, history, and export cases. Confirm that minification and packaging have not broken the model runtime or resource loading. Compare the app's model identifier to the release manifest. Debug-build success alone cannot satisfy this acceptance scenario.

## Appendix F. Milestone deliverables and practical exit criteria

### Phase 0: Feasibility and environment

Inspect the workspace, available hardware, development tools, storage, and network access. Resolve the baseline dependency set and record exact versions. Create the source registry and verify the initial datasets' published licences. Deliver a short feasibility note, repository scaffold, environment checks, and a prioritized implementation backlog.

Exit when the Android shell builds, the Python test environment runs, the first source manifest is valid, and no hidden permission-email dependency remains in the core plan. If a source or tool is blocked, record the alternative and continue independent tasks. Do not wait for optional research resources before establishing the basic project.

### Phase 1: Data and baseline

Implement ingestion, schema checks, privacy review workflow, duplicate grouping, partitioning, and a script-driven baseline. Produce the data summary and frozen split manifest. Train a simple model and create the first evaluation report with correct labels and denominators.

Exit when partition isolation checks pass, preprocessing is trained only on allowed data, and the baseline can be exported into a documented bundle. Quality may still need improvement. A valid modest baseline is sufficient to begin app integration; a misleading high score is not.

### Phase 2: Native vertical slice

Connect the actual model bundle to a native editor and result screen. Implement typed states, cancellation, errors, and a first version of the explanation policy. Add golden parity fixtures and compare desktop and Kotlin outputs.

Exit when a user can install the app, analyze safe text locally, edit and reanalyze, and see controlled behavior for an unavailable model. The screens should already use the central visual tokens, but elaborate motion and optional history can wait.

### Phase 3: Full input workflow and privacy controls

Add sharing, screenshot selection, OCR review, optional history, retention settings, and deliberate report export. Verify permission denial, malformed images, lifecycle changes, and cleanup. Review the merged manifest and actual storage behavior.

Exit when every required input method reaches a correct result or a useful limitation state. Confirm that temporary content and saved history behave as documented. Capture early real screenshots for visual review, not final promotional media.

### Phase 4: Research comparisons and robustness

Compare the selected candidate families under the frozen development protocol. Evaluate calibration, selective performance, perturbations, source effects, and device budgets. Add the learned URL module only if its feature-availability and evaluation gates pass.

Exit when the champion decision is documented and the selected deployment artifact has its own valid metrics and parity report. If the baseline remains best, record that result clearly. Freeze the policy and bundle before final test access.

### Phase 5: Verification and release preparation

Run the meaningful automated suites, physical-device checks, accessibility review, threat-model verification, clean-setup review, and release-build tests. Fix high-impact defects. Generate final safe screenshots, notices, checksum files, and the connected artifact inventory.

Exit when the core gate report is complete and remaining limitations are explicit. Prepare the repository and release package for authorized publication. Optional features that have not passed should be removed from product claims rather than hidden behind misleading screenshots.

### Phase 6: Academic and portfolio handoff

Prepare the faculty demonstration, subject-specific report outlines, related-work discussion, architecture explanation, and viva notes. Rehearse the offline demonstration and its fallback. Check that all reported metrics match the released bundle and every screenshot corresponds to actual functionality.

Exit when another reviewer can understand the project, install it, reproduce the core experiments, and identify the owner's original contribution. Public GitHub publication, if authorized, should be a final delivery action supported by this prepared package.

## Appendix G. Required command interface and validation behavior

Implement a coherent command interface for the ML workspace. The following commands are proposed contracts. They must not be advertised as working until implemented and executed. Keep the same meaning even if a packaging tool changes the exact launcher.

```text
python -m scamsentry_ml.sources validate
python -m scamsentry_ml.data prepare --config configs/data.yaml
python -m scamsentry_ml.data audit --config configs/data.yaml
python -m scamsentry_ml.splits create --config configs/splits.yaml
python -m scamsentry_ml.splits verify --manifest artifacts/splits.json
python -m scamsentry_ml.train --config configs/baseline.yaml
python -m scamsentry_ml.evaluate --run artifacts/runs/baseline_char_lr_001
python -m scamsentry_ml.robustness --config configs/robustness.yaml
python -m scamsentry_ml.export --run artifacts/runs/baseline_char_lr_001
python -m scamsentry_ml.bundle validate --bundle artifacts/bundle
python -m pytest
```

Each command needs help text, input validation, a nonzero exit code on failure, and concise output identifying generated artifacts. Do not print raw message samples by default. Separate dry-run or validation behavior from downloading and modifying source data.

A final-test command should require an explicit frozen-protocol identifier or equivalent guard. It should record that the test was accessed, the exact bundle, and the report path. The guard is a reproducibility aid, not a security mechanism against a determined developer.

For Android, document the actual verified Gradle tasks. Typical tasks include local tests, lint, debug assembly, instrumentation, and release assembly, but exact names depend on the project. In Windows documentation, use the wrapper executable appropriate to PowerShell and quote paths correctly. Do not instruct the user to install a global Gradle version unnecessarily.

Create a project validation script that checks required manifests, source references, bundle schemas, forbidden tracked files, and release-document consistency. It should not silently rewrite evidence. A failing check must identify the specific problem and the next useful action.

Keep large model training outside routine smoke tests. CI should validate contracts with small safe fixtures and, where practical, a tiny reproducible training run. Full-quality experiments can remain separately documented development actions. This separation keeps ordinary code changes reviewable without pretending a fixture model provides production metrics.

## Appendix H. Official source register and freshness rules

The references below were checked while preparing this specification on 16 November 2026. They support the stated source licences, framework choices, and platform considerations. Most of this document consists of original project requirements and proposed engineering decisions; it is not a reproduction of those references.

Recheck the exact source version and relevant terms at implementation time. A link to a living documentation page is not a dependency lock. Save the resolved versions and important licence evidence in the repository's source registry. Where the linked page changes, record the new access date and the effect on the project.

| ID | Primary reference | Use in this project |
|---|---|---|
| S01 | [Creative Commons Attribution 4.0](https://creativecommons.org/licenses/by/4.0/) | Published reuse conditions and limits concerning other rights |
| S02 | [Mendeley SMS phishing dataset, version 1](https://data.mendeley.com/datasets/f45bkkt8pr/1) | Initial three-class message corpus and its stated licence |
| S03 | [UCI SMS Spam Collection](https://archive.ics.uci.edu/dataset/228/sms+spam+collection) | Auxiliary spam benchmark, source composition, stated licence |
| S04 | [UCI PhiUSIIL dataset](https://archive.ics.uci.edu/dataset/967/phiusiil+phishing+url+dataset) | Candidate URL corpus and feature-availability concerns |
| S05 | [Super SMS Dataset research repository](https://github.com/smspamresearch/spstudy) | Additional candidate to inspect; no automatic data approval |
| S06 | [ML Kit text recognition for Android](https://developers.google.com/ml-kit/vision/text-recognition/v2/android) | Bundled OCR setup, supported options, integration guidance |
| S07 | [Android architecture recommendations](https://developer.android.com/topic/architecture/recommendations) | Native architecture reference |
| S08 | [Compose Material 3 release notes](https://developer.android.com/jetpack/androidx/releases/compose-material3) | Distinguishing stable APIs from prerelease choices |
| S09 | [Compose BOM guidance](https://developer.android.com/develop/ui/compose/bom) | Compatible Compose dependency management |
| S10 | [Compose compiler and dependency setup](https://developer.android.com/develop/ui/compose/setup-compose-dependencies-and-compiler) | Compiler-plugin and build compatibility |
| S11 | [ONNX Runtime mobile documentation](https://onnxruntime.ai/docs/tutorials/mobile/) | Optional neural-model deployment path |
| S12 | [LiteRT overview](https://developers.google.com/edge/litert/overview) | Alternative eligible on-device inference path |
| S13 | [Compose accessibility](https://developer.android.com/develop/ui/compose/accessibility) | Semantics and accessible interaction reference |
| S14 | [OWASP MASVS](https://mas.owasp.org/MASVS/) | Selected mobile-security review areas |
| S15 | [Android secure file sharing](https://developer.android.com/training/secure-file-sharing) | Deliberate report export and temporary URI access |
| S16 | [Google Play SMS and call-log permissions](https://support.google.com/googleplay/android-developer/answer/10208820?hl=en) | Constraints relevant to optional future store distribution |
| S17 | [Google Play User Data policy](https://support.google.com/googleplay/android-developer/answer/10144311?hl=en) | Privacy and disclosure review for a future Play release |
| S18 | [Space Grotesk font licence](https://github.com/google/fonts/blob/main/ofl/spacegrotesk/OFL.txt) | Bundled heading font terms |
| S19 | [Inter font licence](https://github.com/google/fonts/blob/main/ofl/inter/OFL.txt) | Bundled body font terms |
| S20 | [Scikit-learn TF-IDF reference](https://scikit-learn.org/stable/modules/generated/sklearn.feature_extraction.text.TfidfVectorizer.html) | Feature-extraction implementation reference |
| S21 | [Scikit-learn calibration guidance](https://scikit-learn.org/stable/modules/calibration.html) | Calibration concepts and evaluation reference |
| S22 | [StratifiedGroupKFold reference](https://scikit-learn.org/stable/modules/generated/sklearn.model_selection.StratifiedGroupKFold.html) | Group-preserving model-selection reference |

The source registry must distinguish a publisher's stated licence from the project's completed file and privacy review. The initial dataset entries are promising because their publisher pages explicitly list CC BY 4.0. That finding does not justify a blanket claim that all datasets found online are unrestricted.

For a future Play Store release, revisit S16 and S17 at submission time. The project deliberately avoids direct inbox access in its initial scope, reducing dependence on restricted SMS permissions. A GitHub APK release and a Play Store listing have different distribution procedures. Neither route removes the underlying duties concerning data rights, truthful claims, and user privacy.

When a source cannot be accessed, report the access limitation and use verified alternatives. Do not invent the current licence, library version, or platform policy. This project should be able to proceed with a conservative baseline rather than waiting on an uncertain optional resource.

## Appendix I. Decision records and release-review questions

Create short decision records for choices that materially change the system. Each record should state the problem, available evidence, selected option, important alternatives, consequences, and verification. Avoid lengthy essays for trivial choices. The purpose is to make the project understandable to a reviewer who was not present during development.

Required decisions include the primary label taxonomy, treatment of auxiliary spam labels, duplicate-grouping approach, partition protocol, deployed model family, calibration method, abstention policy, local storage design, inference runtime, and core offline boundary. A design-system decision should explain the accent semantics and typography without pretending aesthetic preferences are scientific findings.

Use this review matrix before release:

| Review area | Question that must have an evidenced answer |
|---|---|
| Data rights | Are all training sources eligible for the intended workflow under recorded terms? |
| Privacy | Were unnecessary identifiers and public examples reviewed? |
| Labels | Can every task label be traced to a defensible mapping? |
| Independence | Are duplicate and designated template groups isolated across partitions? |
| Training | Were fitted preprocessing and sampling confined to training? |
| Calibration | Is the transformation fitted on the documented data? |
| Policy | Are thresholds and rule interactions versioned and frozen for final testing? |
| Deployment | Does the phone execute the evaluated model and preprocessing? |
| Offline behavior | Does a fresh installed build perform the stated core workflow without connectivity? |
| Failure behavior | Can model or OCR failures avoid misleading reassurance? |
| Storage | Do history settings, encryption, backup, and deletion match the documentation? |
| Platform | Are merged permissions and exported components justified? |
| Accessibility | Were actual large-font and assistive-use checks performed? |
| Performance | Do device measurements identify build, hardware, input set, and procedure? |
| Release | Do the APK, model, source commit, notices, and reports agree? |
| Academic claims | Are completed work and proposed extensions clearly separated? |

A failed review should lead to a concrete action: fix the defect, narrow a claim, remove an optional feature, obtain a genuinely necessary missing decision, or mark a limitation. It must not lead to changing a failed check into “passed with assumptions” without evidence.

Pay special attention to apparent shortcuts. Hiding raw data from Git does not remove its licence conditions. Bundling a model does not prove that training data cannot be inferred. Using HTTPS does not establish website legitimacy. A successful OCR extraction does not prove that every character is correct. A calibrated in-domain model is not automatically calibrated on unfamiliar campaigns.

When evidence is too limited for a confident numerical claim, provide counts and uncertainty instead. For example, zero observed false positives among a small number of ordinary examples does not establish a near-zero population rate. A transparent small study is more useful than an impressive percentage with no denominator.

## Appendix J. First actions and final implementation instruction

Begin execution with a small set of concrete actions. Inspect the working directory, Git status, project instructions, installed Android and Python tools, and available device access. Create the project-state file and a milestone checklist. Resolve the first compatible dependency set and verify the initial source pages.

Next, implement source manifests and safe fixtures, then the baseline data pipeline and Android shell. Work toward the earliest real vertical slice: a user types a fictional message, the app runs the exported model, and the result screen displays supported evidence. Use that slice to test the hardest interfaces early.

Continue through OCR review, Android sharing, history controls, model comparisons, robustness, security verification, visual refinement, and release preparation. Keep optional additions behind clear scope gates. Do not allow the pursuit of an ideal model to prevent delivery of a complete evaluated application.

Before each final-looking screenshot, report, or release note, verify the underlying feature and evidence. Before each public artifact, check privacy, attribution, source identity, and authorization. Before each claim of completion, inspect the actual repository and test reports.

The final implementation must satisfy the owner's intent: one strong, aesthetically considered, technically substantial project that can support Mobile App Development and Cybersecurity, be presented confidently on GitHub, and grow through evidence-based improvements. It should reduce repeated academic work through shared engineering while preserving honest course-specific analysis.

Start Phase 0 now when this document is supplied for implementation. Make reasonable routine decisions, preserve the evidence trail, and carry the authorized work through to a concrete handoff. Treat quality as measurable behavior, clear communication, and reproducibility. Never substitute claims of perfection for verified engineering.

**End of master prompt.**









