import argparse
from .common import ROOT, read_json


def validate_sources():
    entries = read_json(ROOT / "manifests/sources.json")
    if not entries:
        raise ValueError("No registered sources")
    for source in entries:
        for field in ("source_id", "landing_page", "published_license", "eligibility_status", "attribution"):
            if not source.get(field):
                raise ValueError(f"{source.get('source_id')}: missing {field}")
        if source["eligibility_status"] == "eligible":
            for field in ("download_sha256", "file_review", "checked_at"):
                if not source.get(field):
                    raise ValueError(f"Eligible source missing {field}")
    return entries


def main():
    p = argparse.ArgumentParser(description="Validate dataset eligibility records; never downloads implicitly")
    p.add_argument("action", choices=["validate"])
    p.parse_args()
    entries = validate_sources()
    print(f"Validated {len(entries)} source records")


if __name__ == "__main__":
    main()
