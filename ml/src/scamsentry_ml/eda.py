"""Privacy-safe aggregate metadata audit; no model fitting or performance retuning."""
import argparse
from collections import Counter
import csv
import re
import numpy as np
from .common import ROOT, digest, read_json, write_json
from .contracts import supported


def main():
    parser=argparse.ArgumentParser(description=__doc__)
    parser.parse_args()
    source=read_json(ROOT/'manifests/sources.json')[0]
    path=ROOT/'data/raw/Dataset_5971.csv'
    if digest(path)!=source['download_sha256']:raise ValueError('Source checksum changed')
    with path.open(encoding='utf-8-sig',newline='') as f: raw=list(csv.DictReader(f))
    rows=read_json(ROOT/'data/processed/messages.json')
    result={'scope':'Post-hoc aggregate metadata audit only; no test predictions, labels or thresholds used for new tuning','raw_records':len(raw),'processed_records':len(rows),'raw_class_case_normalized':dict(Counter(r['LABEL'].lower() for r in raw)),'raw_URL_presence_by_source_label':{},'processed_by_label':{},'group_size_distribution':{},'record_collection_dates':'unknown; no temporal claim','privacy_limit':'No names or record examples published; regex minimization cannot guarantee removal of all identifiers.'}
    for label in ['ham','spam','smishing']:
        part=[r for r in raw if r['LABEL'].lower()==label]
        result['raw_URL_presence_by_source_label'][label]={'records':len(part),'with_http_or_www':sum(bool(re.search(r'https?://|www\.',r['TEXT'],re.I)) for r in part)}
    for label in range(3):
        part=[r for r in rows if r['label']==label];lengths=[len(r['text']) for r in part]
        result['processed_by_label'][str(label)]={'records':len(part),'groups':len({r['group_id'] for r in part}),'length_min_p50_p95_max':np.quantile(lengths,[0,.5,.95,1]).tolist(),'script_and_length_heuristic_supported':sum(supported(r['text']) for r in part)}
    sizes=Counter(r['group_id'] for r in rows)
    result['group_size_distribution']=dict(Counter(sizes.values()))
    write_json(ROOT/'artifacts/eda.json',result)
    print(f'Wrote aggregate metadata for {len(raw)} source and {len(rows)} processed rows; no message samples printed')


if __name__=='__main__':main()
