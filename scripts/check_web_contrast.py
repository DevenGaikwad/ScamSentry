"""Check the actual editorial CSS token pairings; no WCAG certification claim."""
from pathlib import Path
import json
import re

ROOT = Path(__file__).resolve().parents[1]
css = (ROOT / 'web/src/style.css').read_text()

def luminance(value):
    value = value.lstrip('#')
    if len(value) == 3:
        value = ''.join(c * 2 for c in value)
    channels = [int(value[i:i + 2], 16) / 255 for i in (0, 2, 4)]
    channels = [v / 12.92 if v <= .04045 else ((v + .055) / 1.055) ** 2.4 for v in channels]
    return sum(v * w for v, w in zip(channels, [.2126, .7152, .0722]))

def ratio(a, b):
    a, b = sorted([luminance(a), luminance(b)])
    return (b + .05) / (a + .05)

results = []
for theme, selector in [('light', ':root{'), ('dark', ':root[data-theme=dark]{')]:
    block = css.split(selector, 1)[1].split('}', 1)[0]
    tokens = dict(re.findall(r'--([a-z]+):(#[0-9a-fA-F]+)', block))
    for description, foreground, background, minimum in [
        ('body and labels', 'ink', 'paper', 4.5),
        ('supporting text and placeholder', 'muted', 'paper', 4.5),
        ('disabled button text', 'muted', 'soft', 4.5),
        ('primary button and inverse risk category', 'paper', 'ink', 4.5),
        ('input border against page', 'control', 'paper', 3),
        ('focus outline', 'ink', 'paper', 3),
        ('neutral result text', 'ink', 'soft', 4.5),
    ]:
        measured = ratio(tokens[foreground], tokens[background])
        results.append(dict(theme=theme, component=description, foreground=tokens[foreground], background=tokens[background], ratio=measured, minimum=minimum, passed=measured >= minimum))
assert all(item['passed'] for item in results), results
destination = ROOT / 'assets/evidence/v3/CONTRAST.json'
destination.write_text(json.dumps({'source': 'web/src/style.css tokens; actual browser/keyboard review remains separate', 'pairings': results}, indent=2) + '\n')
print(f'{len(results)} editorial text/control/focus token pairings passed their stated thresholds')
