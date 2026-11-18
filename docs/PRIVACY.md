# Native privacy and retention

The Android app requests no inbox, contacts, camera, broad storage or network access. A user explicitly types, pastes or shares text, or chooses an image through the system picker. The merged APK permission set is inspected separately; ML Kit and AndroidX dependencies do not imply that the app has network permission.

Draft text, screenshots, estimates and analyzed input stay in memory. Rotation preserves the active ViewModel draft; process death discards it. New input invalidates pending analysis by version. OCR ownership spans decode, all EXIF transforms, native recognition completion and dispatcher return; cancelled/stale drafts close their bitmap rather than becoming a result.

DataStore holds the onboarding, consent, theme and decorative-motion preferences. These values contain no message content. History begins disabled. Saving remains manual after enabling it. Room stores only a result ID, time, category, derived count summary, model/policy version and input source. Original text, addresses and screenshots are omitted. A transaction inserts and prunes to 200 rows; schema 2→3 migration prunes older stored rows. Disabling history clears saved summaries and the setting/save operations are serialized. No saved history enters training.

Reports are redacted and previewed before sharing. Each export has an independent filename, retaining up to 20 recent redacted cache files. Files older than one hour are eligible for cleanup during export or app startup; this is not a guarantee of deletion at an exact wall-clock deadline. Previously shared copies belong to the destination app. Export failures remain visible.

Backups and data extraction are disabled in app configuration. A rooted device, compromised OS or a user-chosen sharing destination is outside this storage boundary. Public model parameters can contain character fragments; identifier minimization is not differential privacy. Raw/processed training messages, executable checkpoints and signing keys are excluded from source and release packages.

The browser companion has its own [privacy boundary](WEB_PRIVACY.md). Initial downloads and normal host logs exist, while input processing remains local. Functional CI request assertions, Android manifest inspection, first-use offline recognition and physical-device diagnostics are separate evidence gates; consult the current gate report.
