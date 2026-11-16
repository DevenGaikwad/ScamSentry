"""Inspect actual signed APK contents and native ELF load alignment."""
from pathlib import Path
import argparse
import hashlib
import json
import struct
import zipfile

def inspect(path):
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
        return dict(file=path.name,bytes=path.stat().st_size,model_sha256=hashlib.sha256(z.read(models[0])).hexdigest(),abis=sorted({a['file'].split('/')[1] for a in libraries}),libraries=libraries,alignment_scope='ELF PT_LOAD only; ZIP checks and actual 16 KB page runtime are separate gates')

def main():
    p=argparse.ArgumentParser();p.add_argument('--input',type=Path,default=Path('release/install'));p.add_argument('--output',type=Path,default=Path('release/apk_inspection.json'));a=p.parse_args()
    results=[inspect(p) for p in sorted(a.input.glob('*.apk'))]
    if not results:raise ValueError('No APKs inspected')
    a.output.write_text(json.dumps(results,indent=2)+'\n');print(f'Inspected {len(results)} actual APKs')
if __name__=='__main__':main()
