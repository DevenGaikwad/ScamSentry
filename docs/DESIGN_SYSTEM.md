# Quiet security studio

The identity uses a protective lens mark, deep navy surfaces and restrained semantic accents. App icon and small UI symbols are original vectors/drawing code. Space Grotesk headings and Inter body text are bundled with their SIL OFL notices; no runtime font request is used.

| Token | Value | Purpose |
|---|---|---|
| Background | #080C14 | Application foundation |
| Surface | #101722 | Main cards and navigation |
| Raised | #172131 | Result emphasis |
| Border | #26354B | Card separation |
| Main text | #F2F5FA | Essential content |
| Supporting text | #B6C2D3 | Secondary information |
| Action | #70A7FF | Primary action and navigation |
| Concern | #FF6B7D | Higher-risk indicators |
| Caution | #FFD166 | Needs checking/unable states |
| Lower observed | #66D6A0 | Lower indicators; never verified-safe |
| Interpretation | #B89BFF | Model and educational material |

Spacing uses 4/8/12/16/20/24/32 dp. Cards use 20 dp radii, action controls 14 dp, tags 8 dp. Body is 16 sp with 25 sp line-height; secondary text is 14 sp/22 sp. Text wraps without fixed-height result cards. Controls target at least 48 dp; the main action is 54 dp minimum. The editor is scrollable with IME insets. Compact layouts use labelled bottom navigation; larger layouts add a narrow explanatory sidebar and preserve the main workflow.

Risk color always has a text label and description. Decorative marks are removed from screen-reader traversal. Input labels, result headings, polite result/error announcements and explicit settings labels provide semantics. Continuous decorative motion and fake scan delays are absent. Actual large-font, TalkBack and compact-device checks must be recorded separately; design intent alone does not pass accessibility.

Contrast script `scripts/check_contrast.py` records text on actual primary surfaces and background text on blue buttons. Subtle borders are decorative separators, not the sole control indicator. Disabled controls use Material semantics. Real screenshots must come from a running packaged app and use fictional content. No image-generated app screen is substituted for runtime evidence.
