# Threat model and selected MASVS review

This is an internal defensive review, not OWASP certification. Assets include text/images in memory, optional usage metadata, model integrity, release identity, and the user's understanding of a result. An adversary can supply text, URLs, shared intents, malformed images or replacement artifacts. Physical access to an unlocked phone remains a residual risk.

| Threat | Control | Verification and residual limit |
|---|---|---|
| Input denial of service | 4,000 codepoint analysis cap, 16,000 UTF-16 editor/share bound, 40 extracted links maximum | Boundary unit tests; real-device stress pending |
| Image allocation abuse | 12 MB stream limit, ≤40 MP, ≤16,000 side, ≤2,048 working side, MIME/decoder check | Source review; malformed-provider/device instrumentation pending |
| Deceptive host display | Literal host extraction, user-info/IDN observations, selectable non-clickable text | JVM/Python fixtures, including complete bare-host boundaries across unfamiliar TLDs; no DNS, redirects or WebView |
| Unsolicited external execution | No URL-open action; share input is plain reviewable text | Manifest/source inspection; device intent tests pending |
| Model corruption | SHA-256 comparison, schema/class/shape/numeric limits, controlled unavailable state | Tamper regression tests; checksum cannot authenticate a jointly replaced file/manifest |
| Wrong result on new input | Cancellation and monotonic input versions | State code review; lifecycle/race instrumentation pending |
| Stored messages exposed | Metadata-only history, explicit save, backups excluded | Entity schema/source review; OEM transfer behavior pending |
| Accidental export | Fixed redacted summary, exact preview, narrow FileProvider | Export regression test; recipient app retains its copy |
| Sensitive diagnostics | No message/URL logging or remote analytics | Source scan; dependency behavior needs package/device review |
| Misleading reassurance | Lower-observed wording, no safety percentage, unavailable/uncertain states | Policy tests and real UI review; model remains fallible |
| Dataset leakage | Conflict quarantine, duplicate/template grouping, train-only IDF, frozen test | Executable split assertions; unknown campaigns can evade grouping |
| Adversarial language/OCR | Script/quality limitations, editable OCR text, no broad robustness claim | Perturbation report exposes character-substitution failure |
| Dependency compromise | Exact catalog, official repositories, wrapper digest, release inventory | Coordinate and integrity checks; vulnerability scanning may remain pending |

Selected MASVS areas: STORAGE (minimization, deletion and backup), PLATFORM (intents/providers/permissions), CODE (bounds and schema validation), PRIVACY (explicit processing and export), CRYPTO (no raw-message encryption needed because no raw payload is persisted; Keystore field-encryption is future scope), NETWORK (no analysis networking; verify merged permissions). Authentication is not applicable to a local account-free product.

Only controlled fictional fixtures and owned/authorized device environments are used. No live phishing site is opened or resolved. New cloud sync, remote feeds, analytics or model-update features would require a new trust-boundary review.
