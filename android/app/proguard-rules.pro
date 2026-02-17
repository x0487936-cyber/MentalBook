# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep model classes
-keep class com.mentalbook.app.** { *; }

# Keep Android classes
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

