# EnableContactsGroups Android Xposed Module

EnableContactsGroups is an Android Xposed module that enables contacts groups support for non-Google/Exchange contacts providers on Android devices. The module hooks into Android's contacts framework to enable group membership editing for external account types.

Always reference these instructions first and fallback to search or bash commands only when you encounter unexpected information that does not match the info here.

## Working Effectively

### Environment Setup
- Install JDK 11: The project requires Java Development Kit 11, not newer versions
  - Ubuntu/Debian: `sudo apt-get update && sudo apt-get install -y openjdk-11-jdk`
  - Set JAVA_HOME: `export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64`
  - Add to PATH: `export PATH=/usr/lib/jvm/java-11-openjdk-amd64/bin:$PATH`
- Android SDK with Build Tools 28.0.3 and Android API 28 is required
- **NETWORK REQUIREMENT**: Access to dl.google.com and jcenter() repositories is mandatory for dependency resolution

### Build Commands
Bootstrap and build the project:
- `chmod +x gradlew` -- Make gradlew executable
- **CRITICAL**: Ensure network access to dl.google.com and jcenter() - builds will fail without internet access to Maven repositories
- `./gradlew clean` -- Clean previous builds, takes 30-60 seconds
- `./gradlew build` -- **NEVER CANCEL: Build takes 2-5 minutes including dependency downloads. Set timeout to 10+ minutes on first build.**
- `./gradlew assembleRelease` -- Build release APK, takes 1-3 minutes. **NEVER CANCEL: Set timeout to 5+ minutes.**

**Build Failure Warning**: If you see "dl.google.com: No address associated with hostname" or similar network errors, the build environment lacks required internet access. This is a hard dependency - the project cannot build without downloading Android Gradle Plugin 4.1.3 and other dependencies.

### Test Commands
- `./gradlew test` -- Run unit tests, takes 30-60 seconds. **NEVER CANCEL: Set timeout to 3+ minutes.**
- `./gradlew connectedAndroidTest` -- Run instrumented tests on connected device/emulator, takes 2-5 minutes. **NEVER CANCEL: Set timeout to 10+ minutes.**

### Build Troubleshooting
- If build fails with "Unsupported class file major version 61", ensure you're using JDK 11, not JDK 17+:
  - `export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64`
  - `export PATH=/usr/lib/jvm/java-11-openjdk-amd64/bin:$PATH`
- If build fails with "dl.google.com: No address associated with hostname", network access to Google's Maven repository is blocked - this is a hard requirement
- If build fails with "Could not resolve all artifacts for configuration", dependencies cannot be downloaded - network access required
- If Gradle daemon issues occur, run `./gradlew --stop` then retry (stops running daemons)
- If "No cached version available for offline mode", dependencies must be downloaded online first

## Validation

### Manual Testing Requirements
**CRITICAL**: This Xposed module cannot be functionally tested without:
1. A rooted Android device with Xposed Framework installed
2. Installing the built APK as an Xposed module
3. Rebooting the device to activate the module
4. Testing contacts group functionality with non-Google/Exchange accounts

### Build Validation Steps
Always perform these validation steps after making changes:
- **Build validation**: `./gradlew build` must complete successfully
- **Unit test validation**: `./gradlew test` must pass
- **APK generation**: Verify `app/build/outputs/apk/` contains generated APK files
- **Code quality**: The project uses CodeClimate with PMD and Checkstyle (configured in .codeclimate.yml)

### CI Compatibility
- Always ensure changes are compatible with the GitHub Actions workflow (.github/workflows/android.yml)
- The CI runs on Ubuntu with JDK 11 and executes `./gradlew build`
- Legacy Travis CI configuration exists (.travis.yml) using Oracle JDK 8 and build-tools-28.0.3 but may not be active

## Project Structure

### Key Files and Directories
```
EnableContactsGroups/
├── app/                           # Main Android application module
│   ├── build.gradle              # App-level build configuration
│   ├── src/main/
│   │   ├── java/.../EnableContactsGroups.java  # **CORE MODULE**: Main Xposed hook implementation
│   │   ├── AndroidManifest.xml   # Xposed module metadata and permissions
│   │   └── res/                  # Android resources (minimal - just app name)
│   ├── src/test/                 # Unit tests (JUnit)
│   └── src/androidTest/          # Instrumented tests (Android JUnit)
├── build.gradle                  # Project-level build configuration
├── settings.gradle               # Gradle project settings
├── gradle.properties             # Gradle build properties
└── .github/workflows/android.yml # CI/CD pipeline
```

### Critical Code Location
- **app/src/main/java/name/iavael/xposed/enablecontactsgroups/EnableContactsGroups.java**: The entire module functionality is in this single 20-line file. It hooks the `isGroupMembershipEditable` method in `ExternalAccountType` class to always return true, specifically targeting the package "com.android.contacts.common.model.account".

## Build Configuration Details

### Build Configuration Details

### Dependencies and Versions
- Android Gradle Plugin: 4.1.3
- Gradle: 6.8 (managed by gradle wrapper)
- Compile SDK: 28 (Android 9.0)
- Min SDK: 18 (Android 4.3) 
- Target SDK: 28 (Android 9.0)
- Version Code: 2, Version Name: "0.2"
- Xposed API: 82 (provided dependency)
- Support Library: 28.0.0
- JUnit: 4.13.2 (test dependency)
- Espresso: 3.0.2 (instrumented test dependency)

### Build Outputs
- Debug APK: `app/build/outputs/apk/debug/app-debug.apk`
- Release APK: `app/build/outputs/apk/release/app-release.apk`
- The APK size is typically very small (< 50KB) due to minimal functionality

## Common Tasks

### Making Changes to the Module
1. **Always build first**: Run `./gradlew build` to ensure current state is buildable
2. **Edit the hook logic**: Modify `app/src/main/java/.../EnableContactsGroups.java`
3. **Test changes**: Run `./gradlew test` for unit tests
4. **Build and verify**: Run `./gradlew build` to ensure changes compile
5. **Generate APK**: Run `./gradlew assembleRelease` for distribution

### Adding New Hooks
- Follow the existing pattern in EnableContactsGroups.java
- Use `findAndHookMethod` with appropriate class, method name, and replacement logic
- Target classes within the Android contacts framework
- Always test the package name filter in `handleLoadPackage` (currently targets "com.android.contacts.common.model.account")
- The current hook replaces `isGroupMembershipEditable` in `ExternalAccountType` to always return `true`

### Repository Commands Reference
```bash
# Repository root listing
ls -la
# Expected output includes: README.md, LICENSE, build.gradle, settings.gradle, gradle/, app/, .github/

# Key configuration files
cat build.gradle        # Project build configuration
cat app/build.gradle    # App module configuration
cat .github/workflows/android.yml  # CI pipeline

# Source code structure  
find app/src -name "*.java"
# Expected output:
# app/src/test/java/name/iavael/xposed/enablecontactsgroups/ExampleUnitTest.java
# app/src/androidTest/java/name/iavael/xposed/enablecontactsgroups/ExampleInstrumentedTest.java
# app/src/main/java/name/iavael/xposed/enablecontactsgroups/EnableContactsGroups.java

# Main module line count (should be 19 via wc -l, but actually 20 lines of code)
wc -l app/src/main/java/name/iavael/xposed/enablecontactsgroups/EnableContactsGroups.java

# Gradle daemon management
./gradlew --stop        # Stops all Gradle daemons if experiencing issues
```

## Expected Build Times and Resource Usage
- **First build**: 3-10 minutes (includes dependency downloads) - **NEVER CANCEL, SET 15+ MINUTE TIMEOUT**
- **Incremental builds**: 30-90 seconds - **NEVER CANCEL, SET 5+ MINUTE TIMEOUT**  
- **Clean builds**: 1-3 minutes - **NEVER CANCEL, SET 5+ MINUTE TIMEOUT**
- **Unit tests**: 10-60 seconds - **NEVER CANCEL, SET 3+ MINUTE TIMEOUT**
- **Memory usage**: Gradle daemon uses ~500MB-1GB RAM
- **Disk usage**: Build artifacts use ~50-100MB in app/build/