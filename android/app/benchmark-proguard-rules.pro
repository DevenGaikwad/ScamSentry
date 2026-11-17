# AndroidJUnitRunner runs in the target process and calls this shared library API.
# Dependency deduplication assumes the target retains it; R8 otherwise removes it.
# This QA-only entry point does not change the distributed release's keep rules.
-keep class androidx.tracing.Trace { public static *; }
