# Release artifacts are generated

The source archive contains an `install` folder when APK build checks succeed. Debug artifacts use a development signing key and are for owner-controlled testing. A minified QA artifact, if present, is explicitly test-signed; it is not a production signing identity. No keystore or password is included.

Check the generated `SHA256SUMS.txt`, `ARTIFACT_INVENTORY.json`, `RELEASE_NOTES.md` and gate report before installation. See `docs/SETUP_WINDOWS.md` and `docs/DEVICE_CHECKLIST.md`. A debug build and a compiled unsigned release do not by themselves pass fresh-offline physical-device acceptance.
