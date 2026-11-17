# Controlled OCR rendering check

Actual bundled Latin ML Kit execution on the API 35 x86_64 KVM emulator in [run 44000057342](https://github.com/DevenGaikwad/ScamSentry/actions/runs/44000057342), source `4d54d2af860e4f72ef1b1d9879312c697781cdea`. The debug suite executed this check successfully even though another case failed. See `assets/evidence/v2/ocr_rendering_run11.json` for observed counts.

Six PNG renderings share **one fictional English message**, with 107 reference characters and 13 words. Canvas text uses system sans/serif fonts at pixel sizes below; salt noise uses seed 42 and approximately 2% selected pixels. Other slices vary image dimensions, font, foreground/background contrast or dark appearance. These are controlled sensitivity checks, not independent messages or population accuracy. No real private screenshot is used.

| Slice | Image pixels | Font pixels | Character edits | CER | Word edits | WER | Observed category |
|---|---:|---:|---:|---:|---:|---:|---|
| clean_sans | 720×190 | 20 | 0 / 107 | 0.00% | 0 / 13 | 0.00% | higher |
| small_sans | 480×140 | 14 | 0 / 107 | 0.00% | 0 / 13 | 0.00% | higher |
| large_serif | 1000×240 | 28 | 2 / 107 | 1.87% | 2 / 13 | 15.38% | higher |
| low_contrast | 720×200 | 22 | 0 / 107 | 0.00% | 0 / 13 | 0.00% | higher |
| dark | 720×200 | 22 | 0 / 107 | 0.00% | 0 / 13 | 0.00% | higher |
| salt_noise | 720×190 | 20 | 2 / 107 | 1.87% | 2 / 13 | 15.38% | higher |

CER/WER are Levenshtein edit counts divided by reference character/word counts after lowercasing and whitespace normalization. A correct category does not imply a correct transcription: serif and salt-noise renderings each had two character and two word edits, while the concrete-request category remained higher. The reference category is higher for all six slices, not a set of independently labelled model trials. Every extraction retains the review warning.

This does not recalibrate ML Kit, estimate OCR certainty or establish classifier accuracy. The classifier's severe text-substitution experiment remains a separate known weakness. All eight EXIF pixel layouts are verified in their own test, and OCR consumes that transformed bitmap. Physical-phone memory, assistive correction and broader screenshot evaluation remain open.
