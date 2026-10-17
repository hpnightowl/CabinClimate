# Release (R8) rules for the Cluster Showcase. This build is minified AND obfuscated.
#
# protobuf-lite reads the fields of generated messages by name through reflection. R8 cannot see
# that, so without this rule it removes "unused" fields such as LayoutNode.layoutId_ and parsing the
# Figma .dcf fails: the cluster stays blank and nothing is reported (DesignCompose catches the error).
# This was the only rule needed; verified on a release build with demo/release-check.ps1.
-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite { <fields>; }
