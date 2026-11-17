# JSON model is accessed through explicit fields; no reflective model mapping.
-keepattributes SourceFile,LineNumberTable
# ML Kit discovers these manifest-named registrars through public no-arg constructors.
# Preserve that reflective entry point while allowing the rest of the SDK to shrink.
-keep class com.google.mlkit.common.internal.CommonComponentRegistrar { public <init>(); }
-keep class com.google.mlkit.vision.text.internal.TextRegistrar { public <init>(); }
-keep class com.google.mlkit.vision.common.internal.VisionCommonRegistrar { public <init>(); }
