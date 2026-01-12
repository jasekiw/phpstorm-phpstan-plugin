[![official JetBrains project](https://jb.gg/badges/official.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)

# PHPStan plugin for PhpStorm
This plugin adds dedicated functionality for [PHPStan](https://phpstan.org/) to [PhpStorm](https://www.jetbrains.com/phpstorm/). The plugin is under active development and will be bundled into PhpStorm.

# Features

**PHPStan as a first-class quality tool**

- On-the-fly file highlighting and ability to batch-run inspections
- Ability to run via remote interpreters such as Docker, WSL, and others
- Automatic detection of PHPStan settings from composer.json
- Manual configuration of PHPStan settings in the corresponding inspection options

# Building

## Prerequisites

- **Java 21+** - Required by the IntelliJ Platform Gradle Plugin 2.x  
  (The build will auto-provision JDK 21 via Foojay if not available)
- **PHPStorm** - A local installation is **required** for building

## Build Instructions

This plugin uses internal PHPStorm APIs that are not available through the standard plugin SDK. You **must** have a local PHPStorm installation to build this plugin.

### Step 1: Configure Local PHPStorm Path

Copy the example configuration file:
```bash
cp gradle.properties.local.example gradle.properties.local
```

Edit `gradle.properties.local` and set your PHPStorm path:
```properties
# Windows
platformLocalPath=C:/Users/YourUsername/AppData/Local/Programs/PhpStorm

# macOS
platformLocalPath=/Applications/PhpStorm.app/Contents

# Linux
platformLocalPath=/opt/PhpStorm
```

### Step 2: Build the Plugin

```bash
# On Unix/macOS
./gradlew buildPlugin -x test

# On Windows
.\gradlew.bat buildPlugin -x test
```

The plugin zip will be in `build/distributions/`.

## Running the Plugin

To run PHPStorm with the plugin installed for testing:

```bash
./gradlew runIde
```

## Note for External Contributors

This plugin is primarily developed within JetBrains' internal infrastructure using the `BUILD.bazel` configuration. The Gradle build system provided here is for external contributors and may have limitations:

1. A local PHPStorm installation is required (internal APIs are not published)
2. Tests may not compile due to missing test fixture dependencies
3. The Kotlin version must match your PHPStorm installation

If you encounter issues, ensure your PHPStorm version matches the `platformVersion` in `gradle.properties`.