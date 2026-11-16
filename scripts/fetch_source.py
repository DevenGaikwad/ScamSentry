"""Download only the registered primary CSV, verify both digests, never execute archive code."""
from pathlib import Path
import hashlib
import io
import json
import urllib.request
import zipfile

root=Path(__file__).resolve().parents[1]
source=json.loads((root/'ml/manifests/sources.json').read_text())[0]
if source['eligibility_status']!='eligible': raise SystemExit('Source is not eligible')
with urllib.request.urlopen(source['download_url'],timeout=90) as response:
    raw=response.read(2_000_001)
if len(raw)>2_000_000: raise SystemExit('Archive exceeds expected safety bound')
if hashlib.sha256(raw).hexdigest()!=source['archive_sha256']: raise SystemExit('Archive changed; stop and review source')
archive=zipfile.ZipFile(io.BytesIO(raw))
if archive.namelist()!=['Dataset_5971.csv']: raise SystemExit('Unexpected archive members')
if archive.getinfo('Dataset_5971.csv').file_size>2_000_000: raise SystemExit('Unexpected CSV size')
csv=archive.read('Dataset_5971.csv')
if hashlib.sha256(csv).hexdigest()!=source['download_sha256']: raise SystemExit('CSV digest mismatch')
path=root/'ml/data/raw/Dataset_5971.csv'
path.parent.mkdir(parents=True,exist_ok=True)
if path.exists() and path.read_bytes()!=csv: raise SystemExit('Existing source differs; not overwritten')
path.write_bytes(csv)
print('Verified primary source saved. No messages printed or redistributed.')
