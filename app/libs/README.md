# Local Libraries

This directory contains JAR files that are required for compilation but not available in public Maven repositories.

## xposed-api-82.jar

This is a minimal stub implementation of the Xposed API (version 82) containing the classes needed for compilation:

- `de.robv.android.xposed.IXposedHookLoadPackage`
- `de.robv.android.xposed.XC_MethodReplacement`
- `de.robv.android.xposed.XposedHelpers`
- `de.robv.android.xposed.callbacks.XC_LoadPackage`

The actual Xposed framework functionality is provided at runtime by the Xposed Framework itself when installed on a device.

This stub JAR was created to enable compilation with modern Android Gradle Plugin versions when the official Xposed API is not available in public repositories.