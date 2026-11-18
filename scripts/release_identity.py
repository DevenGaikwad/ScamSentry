"""Read the same Android preview identity used by the Gradle application."""
from pathlib import Path
import re

ROOT = Path(__file__).resolve().parents[1]


def release_identity():
    settings = dict(line.split("=", 1) for line in (ROOT / "android/release.properties").read_text().splitlines() if line)
    version = settings["versionName"]
    code = int(settings["versionCode"])
    if not re.fullmatch(r"[0-9]+\.[0-9]+\.[0-9]+-preview", version) or code < 1:
        raise ValueError("Invalid native preview identity")
    return version, code


if __name__ == "__main__":
    print(release_identity()[0])
