# Dataset eligibility and data sufficiency

The machine-readable source registry is `ml/manifests/sources.json`; the publisher licence evidence and archive checks are preserved beside it. Sources were checked on 16 November 2026. Code licensing does not relicense source data.

| Source | Status | Use |
|---|---|---|
| [Mishra and Soni, Mendeley V1](https://data.mendeley.com/datasets/f45bkkt8pr/1), DOI 10.17632/f45bkkt8pr.1 | Eligible for documented minimized research workflow | Primary three-class corpus. Publisher CC BY 4.0. Archive contains only CSV, no contradictory notice. |
| [UCI SMS Spam Collection](https://archive.ics.uci.edu/dataset/228/sms+spam+collection), DOI 10.24432/C5CC84 | Conditional; not trained | Publisher CC BY 4.0, older archive readme includes separate terms and an appreciated notification request. 5,200 exact normalized overlaps among 5,574 parsed records. |
| [PhiUSIIL](https://archive.ics.uci.edu/dataset/967/phiusiil+phishing+url+dataset) | Excluded from this release | Learned URL research is deferred; current eligibility not rechecked. |
| [Super SMS](https://github.com/smspamresearch/spstudy) | Excluded from this release | No added data necessary for baseline; data rights not established. |

Primary CSV parsing verifies 5,971 rows, including case variants of spam and smishing. Case-normalized source totals match 4,844 ham, 489 spam and 638 smishing. The audit removes 130 records in 51 label-conflicting connected groups and 101 redundant exact normalized records. The remaining 5,740 records form 5,411 designated groups (largest retained group: 14). Saved memberships yield train 3,444, development 861, calibration 574 and final test 861 records. These counts come from actual parsed files, not a hard-coded dataset fallback.

Messages are minimized for numeric strings, email addresses and URL strings; vocabulary fitting uses the training partition only. Known public source content can still include personal names and third-party text. Raw/processed records and private error examples are excluded from Git and the release archive. No user inbox data is collected. Original fictional demos use reserved domains.

Sufficiency decision: this corpus supports a reproducible historical English short-message research baseline. It does not establish current Indian campaign coverage, Marathi/Hindi/Hinglish accuracy, source-independent generalization, temporal generalization, real-user prevalence calibration or OCR robustness. Collection dates at record level remain unknown. Larger duplicate corpora do not fix those limits. A future expansion should target independently licensed, campaign-diverse legitimate banking and modern payment/impersonation examples, with reviewed labels and fresh evaluation.

Attribution: Mishra, Sandhya; Soni, Devpriya (2022), “SMS PHISHING DATASET FOR MACHINE LEARNING AND PATTERN RECOGNITION”, Mendeley Data, V1. [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/). Changes include identifier minimization, normalization, deduplication, template grouping, conflict quarantine and partitioning. No researcher, university, bank or publisher endorsement is implied. Published permissions do not eliminate every possible privacy or third-party-right concern.

Aggregate length, script/quality heuristic, source URL presence and designated-group-size distributions are in `ml/artifacts/eda.json`. This post-hoc metadata audit did not fit models or tune final-test decisions.
