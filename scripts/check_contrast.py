"""Check the actual native palette's normal text and focus contrast."""
from pathlib import Path
import json
import re


def luminance(color):
    rgb = [int(color[i:i + 2], 16) / 255 for i in (0, 2, 4)]
    values = [c / 12.92 if c <= .04045 else ((c + .055) / 1.055) ** 2.4 for c in rgb]
    return sum(c * weight for c, weight in zip(values, [.2126, .7152, .0722]))


def ratio(a, b):
    dark, light = sorted([luminance(a), luminance(b)])
    return (light + .05) / (dark + .05)


root = Path(__file__).resolve().parents[1]
theme = (root / 'android/app/src/main/java/in/deven/scamsentry/ui/Theme.kt').read_text()
result = []
for appearance in ('light', 'dark'):
    palette = theme.split(f'private val {appearance} = StudioPalette(', 1)[1].split('\n)', 1)[0]
    color = dict(re.findall(r'(\w+) = Color\(0xFF([0-9A-F]{6})\)', palette))
    for background in ('paper', 'surface', 'raised'):
        for name in ('ink', 'muted', 'danger', 'caution', 'positive', 'uncertain', 'control'):
            contrast = ratio(color[name], color[background])
            minimum = 3 if name == 'control' else 4.5
            result.append(dict(theme=appearance, foreground=name, surface=background, ratio=contrast, minimum=minimum,
                               passed=contrast >= minimum))
    contrast = ratio(color['paper'], color['ink'])
    result.append(dict(theme=appearance, foreground='button label', surface='primary button', ratio=contrast,
                       minimum=4.5, passed=contrast >= 4.5))
report = root / 'release/ANDROID_PALETTE_CONTRAST.json'
report.parent.mkdir(exist_ok=True)
report.write_text(json.dumps(result, indent=2) + '\n')
failures = [entry for entry in result if not entry['passed']]
if failures:
    raise SystemExit(f'Native contrast failed: {failures}')
print(f'{len(result)} native text and control color pairs meet their contrast target')
