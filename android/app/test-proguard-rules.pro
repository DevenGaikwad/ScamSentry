# The runner's class filter names this test through an external CLI argument.
# Preserve this reflective test entry point; production app rules are separate.
-keepattributes RuntimeVisibleAnnotations
-keep class in.deven.scamsentry.OcrIntegrationTest {
    public <init>();
    @org.junit.Test public void *(...);
}
