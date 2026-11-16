"""Canonical end-to-end domain contract, shared with Kotlin and TypeScript."""
from .contracts import MAX_CODEPOINTS, normalize, observations, supported
from .url_v2 import extract
SCHEMA=2
POLICY='ordinal_v2'
LIMITS={
 'model_unavailable':'The validated message model is unavailable. Only local observations are available.',
 'unsupported_scope':'English messages are the research scope. Script checks do not establish language support; Marathi, Hindi and Hinglish are not validated.',
 'short_ocr':'Very little OCR text was available. Replace the image or enter a complete message.',
 'ocr_review':'OCR characters can be mistaken. Review the extracted text before relying on observations.',
 'weak_features':'Too few usable model features were found for reliable statistical interpretation.',
 'repetitive_input':'Highly repetitive text is outside the evaluated message scope.',
 'standalone_url':'Standalone address: lexical observations only; message estimates are not website-risk probabilities.',
 'link_limit':'Only the first 40 addresses were inspected. Shorten the input to inspect every destination.',
 'research_scope':'This historical research model cannot verify the sender or guarantee legitimacy.',
 'lexical_only':'Links were inspected as text. No destination or reputation service was contacted.',
 'invalid_input':'Enter a valid nonempty excerpt of at most 4,000 Unicode code points.',
}

def analyze(text, model, source='text'):
    base=dict(schema_version=SCHEMA,policy_version=POLICY,observation_version='local_context_v2',url_version='lexical_v2',normalizer_version='nfkc_ascii_ws_v1',model_id=model.bundle['model_id'] if model else 'unavailable')
    def result(risk,estimate,obs,addresses,flags,standalone=False,truncated=False):
        return dict(**base,risk=risk,estimate=estimate,observations=obs,urls=addresses,quality_flags=flags,limitations=[LIMITS[x] for x in flags],standalone_url=standalone,links_truncated=truncated)
    try:
        if not text.strip() or len(text)>MAX_CODEPOINTS or any((ord(c)<32 and c not in '\t\n\r') or 0xd800<=ord(c)<=0xdfff for c in text): raise ValueError('input')
        normalized=normalize(text)
    except (ValueError,TypeError): return result('unable',None,[],[],['invalid_input','research_scope'])
    obs=observations(text); addresses,truncated=extract(text)
    standalone=len(addresses)==1 and text.strip()==addresses[0]['original']
    scope=supported(text)
    estimate=model.predict(text) if model is not None and scope and not standalone else None
    flags=[]
    if model is None and not standalone: flags.append('model_unavailable')
    if not scope and not standalone: flags.append('unsupported_scope')
    if source=='ocr': flags.append('short_ocr' if len(text.strip())<20 else 'ocr_review')
    if estimate is not None and estimate['active_count']<4: flags.append('weak_features')
    if len(normalized)>30 and len(set(normalized.replace(' ','')))<4: flags.append('repetitive_input')
    if standalone: flags.append('standalone_url')
    if truncated: flags.append('link_limit')
    flags.append('research_scope')
    if addresses: flags.append('lexical_only')
    if any(o['code']=='secret_request' for o in obs): risk='higher'
    elif standalone: risk='checking' if addresses[0]['valid'] else 'unable'
    elif any(f in flags for f in ['model_unavailable','unsupported_scope','short_ocr','weak_features','repetitive_input','link_limit']): risk='unable'
    else:
        p=estimate['probabilities']; ordered=sorted(p,reverse=True)
        policy=model.bundle['policy']
        if any(u['codes'] for u in addresses) or ordered[0]<policy['confidence'] or ordered[0]-ordered[1]<policy['margin']: risk='checking'
        elif p.index(max(p))==2: risk='higher'
        elif p.index(max(p))==1 or any(o['severity']=='caution' for o in obs): risk='checking'
        else: risk='lower'
    return result(risk,estimate,obs,addresses,flags,standalone,truncated)
