"""Inspect actual signed APK contents and native ELF load alignment."""
from pathlib import Path
import argparse
import hashlib
import json
import struct
import os
import subprocess
import zipfile

from release_identity import release_identity

ROOT=Path(__file__).resolve().parents[1]
def inspect(path,sdk):
    aapt=sdk/'build-tools/36.0.0'/('aapt2.exe' if os.name=='nt' else 'aapt2')
    badging=subprocess.check_output([str(aapt),'dump','badging',str(path)],text=True)
    permissions=subprocess.check_output([str(aapt),'dump','permissions',str(path)],text=True)
    version, code = release_identity()
    if not all(s in badging for s in ["package: name='in.deven.scamsentry'",f"versionCode='{code}'",f"versionName='{version}'","minSdkVersion:'26'","targetSdkVersion:'36'"]):raise ValueError('APK manifest version/API mismatch')
    if any(s in permissions for s in ['android.permission.INTERNET','android.permission.READ_SMS','android.permission.READ_CONTACTS','android.permission.CAMERA']):raise ValueError('Unexpected sensitive/network permission')
    with zipfile.ZipFile(path) as z:
        models=[n for n in z.namelist() if n=='assets/model/model.json']
        if len(models)!=1:raise ValueError('Missing packaged model')
        libraries=[]
        for name in z.namelist():
            if name.endswith('.so') and name.startswith('lib/'):
                raw=z.read(name)
                if raw[:4]!=b'\x7fELF':raise ValueError('Invalid ELF')
                bits=raw[4];endian='<' if raw[5]==1 else '>'
                if bits==2:
                    off=struct.unpack_from(endian+'Q',raw,32)[0];entsize,count=struct.unpack_from(endian+'HH',raw,54);align=[]
                    for i in range(count):
                        if struct.unpack_from(endian+'I',raw,off+i*entsize)[0]==1:align.append(struct.unpack_from(endian+'Q',raw,off+i*entsize+48)[0])
                else:
                    off=struct.unpack_from(endian+'I',raw,28)[0];entsize,count=struct.unpack_from(endian+'HH',raw,42);align=[]
                    for i in range(count):
                        if struct.unpack_from(endian+'I',raw,off+i*entsize)[0]==1:align.append(struct.unpack_from(endian+'I',raw,off+i*entsize+28)[0])
                libraries.append(dict(file=name,elf_bits=64 if bits==2 else 32,load_alignment=align,supports_16k_load_alignment=all(a>=16384 for a in align)))
        removed = {"android.permission.READ_MEDIA_IMAGES", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}
        if any(permission in permissions for permission in removed):raise ValueError("Unexpected broad storage access")
        for notice in ("archivo-OFL.txt", "geistmono-OFL.txt", "fonts.json"):
            if f"assets/third_party/{notice}" not in z.namelist():raise ValueError("Missing font provenance")
        for font in ("archivo_black", "geist_mono"):
            if not any(name.endswith(f"/{font}.ttf") for name in z.namelist()):
                # R8/resource optimization can rename the compiled font path. Inspect the resource table too.
                resources = subprocess.check_output([str(aapt), "dump", "resources", str(path)], text=True)
                if f"font/{font}" not in resources:raise ValueError("Missing compiled editorial font")
        model_sha=hashlib.sha256(z.read(models[0])).hexdigest()
        if model_sha!=hashlib.sha256((ROOT/'ml/artifacts/bundle/model.json').read_bytes()).hexdigest():raise ValueError('Packaged model mismatch')
        if any(x['elf_bits']==64 and not x['supports_16k_load_alignment'] for x in libraries):raise ValueError('64-bit ELF load alignment below 16 KB')
        return dict(manifest=[line for line in badging.splitlines() if line.startswith(('package:','minSdkVersion:','targetSdkVersion:','native-code:'))],permissions=permissions.strip().splitlines(),file=path.name,bytes=path.stat().st_size,model_sha256=hashlib.sha256(z.read(models[0])).hexdigest(),abis=sorted({a['file'].split('/')[1] for a in libraries}),libraries=libraries,alignment_scope='ELF PT_LOAD only; ZIP checks and actual 16 KB page runtime are separate gates')

def main():
    p=argparse.ArgumentParser();p.add_argument('--sdk',type=Path,default=Path(os.environ.get('ANDROID_HOME','/missing-sdk')));p.add_argument('--input',type=Path,default=Path('release/install'));p.add_argument('--output',type=Path,default=Path('release/apk_inspection.json'));a=p.parse_args()
    results=[inspect(p,a.sdk) for p in sorted(a.input.glob('*.apk'))]
    by_variant = {name: [item for item in results if f"_{name}_TEST-SIGNED.apk" in item["file"]]
                  for name in ("arm64-v8a", "x86_64", "universal")}
    if len(results) != 3 or any(len(items) != 1 for items in by_variant.values()):
        raise ValueError("Expected one APK per release variant")
    for abi in ("arm64-v8a", "x86_64"):
        if by_variant[abi][0]["abis"] != [abi]:
            raise ValueError(f"Split APK ABI mismatch: {abi}")
    required_abis = {"arm64-v8a", "x86_64"}
    if not required_abis <= set(by_variant["universal"][0]["abis"]):
        raise ValueError("Universal APK is missing a supported 64-bit ABI")

    a.output.write_text(json.dumps(results,indent=2)+'\n');print(f'Inspected {len(results)} actual APKs')
if __name__=='__main__':main()
