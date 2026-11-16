# Physical-device acceptance procedure

Use an owner-controlled phone. Record model, Android/API level, RAM/chipset, build variant, APK checksum, bundled model digest, date and connectivity condition. Emulator results must be labelled emulator. Never report desktop or TCG emulator times as phone measurements.

| Case | Procedure | Required observation |
|---|---|---|
| Fresh offline install | Uninstall/clear prior test state, transfer verified APK, disable Wi-Fi/mobile data, launch | Bundled text and Latin OCR work without initialization download |
| Cold and warm sharing | Share fictional text while app closed, then while editing | Correct reviewable input/source, no automatic URL opening or saving |
| New input race | Begin OCR/inference, then share a new fictional message | Old request cannot overwrite current-version result |
| Screenshot review | Pick original demo PNG; edit extracted word/link | User corrections drive analysis; source image not copied to app storage |
| Malformed/provider failure | Pick controlled malformed/oversized fixture or revoke provider access | Clear recoverable error, responsive main UI, bounded memory |
| Rotation and process death | Rotate editor/OCR; background, then kill process | Rotation retains draft safely; process restart discards it and requires re-selection |
| History and deletion | Confirm default off, enable, save, restart, delete, clear, disable | Only approved derived metadata retained; no raw text/image/host saved. Rapid save/disable/delete cannot restore deleted summaries or show a stale saved marker |
| History migration | Install/construct v1 fixture then apply v2 | Old summary/model/policy preserved; input method marked unknown |
| Export | Inspect preview containing fictional identifier-rich input | Exact shared report omits raw message, URLs and screenshot; narrow temporary URI access |
| Missing/corrupt model | Use controlled test artifact | Unable/limited state, no invented probability or lower-risk fallback |
| Large font and small display | Main flow at 1.3×/1.5×/2× fonts and compact size | No clipped warnings, hidden actions, broken hostname inspection or keyboard overlap |
| TalkBack | Navigate editor, results, URLs and settings | Useful labels/heading/traversal, main result announced, decorative marks omitted |
| Merged release package | Inspect permissions/exported components and install minified test build | Main share activity and protected profile receiver justified; debug preview/test activities absent |
| Backup/transfer | Authorized backup/restore/transfer of own test profile | Document actual OEM behavior; no raw-payload claim based only on manifest |
| Phone latency | 10 cold starts, 30 warm text samples, 10 screenshots | p50/p95, input set, thermal conditions, build and hardware, no best-case-only report |

Battery percentages and universal compatibility are not inferred from a short demo. No persistent inbox scanner is implemented. SDK diagnostics and observed network behavior are recorded separately from core local inference.
