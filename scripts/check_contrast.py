from pathlib import Path
import json

def luminance(hex_color):
    values=[int(hex_color[i:i+2],16)/255 for i in (1,3,5)]
    values=[c/12.92 if c<=.04045 else ((c+.055)/1.055)**2.4 for c in values]
    return sum(a*b for a,b in zip(values,[.2126,.7152,.0722]))

def ratio(a,b):
    a,b=sorted([luminance(a),luminance(b)])
    return (b+.05)/(a+.05)

root=Path(__file__).resolve().parents[1]
colors={'text':'#F2F5FA','supporting':'#B6C2D3','blue':'#70A7FF','red':'#FF6B7D','amber':'#FFD166','green':'#66D6A0','purple':'#B89BFF'}
result=[]
for background in ['#080C14','#101722','#172131']:
    for name,color in colors.items():
        r=ratio(color,background);result.append(dict(foreground=name,background=background,contrast=r,normal_text_pass=r>=4.5))
result.append(dict(foreground='dark button text',background='#70A7FF',contrast=ratio('#080C14','#70A7FF'),normal_text_pass=ratio('#080C14','#70A7FF')>=4.5))
p=root/'assets/evidence/contrast.json';p.parent.mkdir(parents=True,exist_ok=True);p.write_text(json.dumps(result,indent=2)+'\n')
assert all(r['normal_text_pass'] for r in result)
print(f'{len(result)} primary text pairings pass 4.5:1; device/accessibility review remains separate')
