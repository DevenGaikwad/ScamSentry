"""Research-only portable block contract; production schema 1 remains frozen."""
from collections import Counter
import math
import re
from .contracts import normalize,privacy_transform,ngrams

WORD_VERSION='ascii_words_2plus_ngrams_v1'
def words(text):
    return re.findall(r"[a-z]+(?:'[a-z]+)?",normalize(privacy_transform(text)))
def word_features(text,low=1,high=2):
    tokens=[w for w in words(text) if len(w)>=2]
    return [' '.join(tokens[i:i+n]) for n in range(low,high+1) for i in range(len(tokens)-n+1)]
def char_features(text):return ngrams(privacy_transform(text))

class PortableBlocks:
    def __init__(self,bundle):
        self.bundle=validate_blocks(bundle)
        self.maps=[{s:i for i,s in enumerate(b['vocabulary'])} for b in bundle['blocks']]
    def predict(self,text):
        weights={};offset=0;norms=[]
        for b,vocab in zip(self.bundle['blocks'],self.maps):
            features=char_features(text) if b['kind']=='char' else word_features(text,*b['ngram_range'])
            counts=Counter(vocab[g] for g in features if g in vocab)
            raw={i:(1+math.log(c))*b['idf'][i] for i,c in counts.items()}
            norm=math.sqrt(sum(v*v for v in raw.values()));norms.append(norm)
            if norm:weights.update({offset+i:v/norm*b['weight'] for i,v in raw.items()})
            offset+=len(vocab)
        logits=[bias+sum(row[i]*v for i,v in weights.items()) for row,bias in zip(self.bundle['coefficients'],self.bundle['intercept'])]
        exps=[math.exp((x-max(logits))/self.bundle['temperature']) for x in logits];total=sum(exps)
        return dict(normalized=normalize(privacy_transform(text)),logits=logits,probabilities=[x/total for x in exps],active_count=len(weights),norm=math.sqrt(sum(v*v for v in weights.values())),block_norms=norms)

def export_blocks(features,estimator,temperature,model_id):
    from .common import CLASSES
    blocks=[]
    for name,vec in features.transformer_list:
        blocks.append(dict(kind=name,feature_version='nfkc_ascii_ws_v1' if name=='char' else WORD_VERSION,ngram_range=[3,5] if name=='char' else [1,2],vocabulary=vec.get_feature_names_out().tolist(),idf=vec.idf_.tolist(),weight=features.transformer_weights.get(name,1),sublinear_tf=True,l2_normalize=True))
    return dict(schema_version=2,model_id=model_id,classes=CLASSES,normalizer='nfkc_ascii_ws_v1',privacy_transform='typed_tokens_v1',blocks=blocks,final_l2=False,coefficients=estimator.coef_.tolist(),intercept=estimator.intercept_.tolist(),temperature=temperature,policy=dict(version='ordinal_v1',confidence=.62,margin=.18),promotion_status='research_only')

def validate_blocks(bundle):
    from .common import CLASSES
    if bundle.get('schema_version')!=2 or bundle.get('classes')!=CLASSES or bundle.get('normalizer')!='nfkc_ascii_ws_v1' or bundle.get('privacy_transform')!='typed_tokens_v1' or bundle.get('final_l2') is not False: raise ValueError('Unsupported block contract')
    blocks=bundle.get('blocks',[])
    if not 1<=len(blocks)<=2 or len({b['kind'] for b in blocks})!=len(blocks):raise ValueError('Invalid blocks')
    n=0
    for b in blocks:
        expected='nfkc_ascii_ws_v1' if b['kind']=='char' else WORD_VERSION if b['kind']=='word' else None
        if expected is None or b['feature_version']!=expected or b['ngram_range']!=([3,5] if b['kind']=='char' else [1,2]) or b['sublinear_tf'] is not True or b['l2_normalize'] is not True:raise ValueError('Invalid feature block')
        vocab=b['vocabulary'];idf=b['idf']
        if not 1<=len(vocab)<=60000 or len(set(vocab))!=len(vocab) or any(not isinstance(t,str) or not t for t in vocab) or len(idf)!=len(vocab):raise ValueError('Invalid block vocabulary')
        if not isinstance(b['weight'],(int,float)) or isinstance(b['weight'],bool) or not math.isfinite(b['weight']) or b['weight']<=0 or any(not isinstance(x,(int,float)) or isinstance(x,bool) or not math.isfinite(x) or x<=0 for x in idf):raise ValueError('Invalid block parameters')
        n+=len(vocab)
    if len(bundle['coefficients'])!=3 or any(len(r)!=n for r in bundle['coefficients']) or len(bundle['intercept'])!=3:raise ValueError('Parameter shape mismatch')
    values=sum(bundle['coefficients'],[])+bundle['intercept']+[bundle['temperature']]
    if any(not isinstance(x,(int,float)) or isinstance(x,bool) or not math.isfinite(x) for x in values) or bundle['temperature']<=0:raise ValueError('Invalid numeric parameters')
    if bundle['policy']['version']!='ordinal_v1' or any(not isinstance(bundle['policy'][k],(int,float)) or isinstance(bundle['policy'][k],bool) or not math.isfinite(bundle['policy'][k]) or not 0<=bundle['policy'][k]<=1 for k in ['confidence','margin']):raise ValueError('Invalid policy')
    return bundle
