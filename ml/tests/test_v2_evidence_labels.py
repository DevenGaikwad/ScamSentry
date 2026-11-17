"""Public evidence minimizes credential-like fixture IDs without weakening gates."""
import importlib.util
import json
from pathlib import Path
import xml.etree.ElementTree as ET
import pytest

SPEC=importlib.util.spec_from_file_location("release_evidence",Path(__file__).resolve().parents[2]/"scripts/create_release_evidence.py")
EVIDENCE=importlib.util.module_from_spec(SPEC)
SPEC.loader.exec_module(EVIDENCE)

def test_public_evidence_redacts_credential_fixture_labels_and_keeps_counts(tmp_path):
    labels=[
        "fixture_token["+"gh"+"p_"+"A"*40+"]",
        "fixture_key["+"-----BEGIN "+"PRIVATE KEY-----"+"]",
    ]
    root=ET.Element("testsuite")
    for label in labels:
        ET.SubElement(root,"testcase",classname="fictional.Privacy",name=label)
    file=tmp_path/"results.xml"
    ET.ElementTree(root).write(file)
    report=EVIDENCE.junit([file],2)
    serialized=json.dumps(report)
    assert report["tests"]==2 and report["failed"]==0 and report["skipped"]==0
    assert report["labels_redacted"]==2
    assert all(label not in serialized for label in labels)
    assert all("redacted-fixture-marker" in item["name"] for item in report["cases"])
    assert all(item["class_name"]=="fictional.Privacy" for item in report["cases"])

def test_credential_label_redaction_cannot_hide_failed_or_skipped_cases(tmp_path):
    for status in ["failure","skipped"]:
        root=ET.Element("testsuite")
        case=ET.SubElement(root,"testcase",classname="fictional.Privacy",name="fixture["+"gh"+"p_"+"B"*40+"]")
        ET.SubElement(case,status).text="Synthetic failure or skip"
        file=tmp_path/(status+".xml")
        ET.ElementTree(root).write(file)
        with pytest.raises(ValueError,match="Failed or skipped"):
            EVIDENCE.junit([file],1)
