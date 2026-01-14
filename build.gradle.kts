import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import java.util.Properties

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("org.jetbrains.intellij.platform") version "2.2.1"
}

// Load local properties if they exist
val localProperties = Properties()
val localPropertiesFile = file("gradle.properties.local")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}

fun getProperty(name: String): String? {
    return localProperties.getProperty(name) ?: providers.gradleProperty(name).orNull
}

group = providers.gradleProperty("pluginGroup").get()
version = providers.gradleProperty("pluginVersion").get()

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(providers.gradleProperty("javaVersion").get()))
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(providers.gradleProperty("javaVersion").get()))
    }
}

sourceSets {
    main {
        java.srcDirs("src")
        resources.srcDirs("resources")
    }
    test {
        java.srcDirs("tests")
        resources.srcDirs("testData")
    }
}

// Check if a local PHPStorm path is specified
val platformLocalPath: String? = getProperty("platformLocalPath")

dependencies {
    intellijPlatform {
        // Use local installation if specified, otherwise download
        if (platformLocalPath != null) {
            local(platformLocalPath)
        } else {
            create(providers.gradleProperty("platformType").get(), providers.gradleProperty("platformVersion").get())
        }
        
        // PHP plugin - required dependency
        bundledPlugin("com.jetbrains.php")
        
        // Remote interpreter plugin - optional dependency  
        bundledPlugin("org.jetbrains.plugins.phpstorm-remote-interpreter")
        
        pluginVerifier()
        zipSigner()
        
        testFramework(TestFrameworkType.Platform)
    }
    
    testImplementation("junit:junit:4.13.2")
}

intellijPlatform {
    pluginConfiguration {
        id = "com.github.jasekiw.phpstan"
        name = providers.gradleProperty("pluginName")
        version = providers.gradleProperty("pluginVersion")
        
        ideaVersion {
            sinceBuild = "253"
            untilBuild = provider { null }
        }
    }
    
    signing {
        certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
        privateKey = providers.environmentVariable("PRIVATE_KEY")
        password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
    }
    
    publishing {
        token = providers.environmentVariable("PUBLISH_TOKEN")
    }
    
    pluginVerification {
        ides {
            recommended()
        }
    }
}

tasks {
    wrapper {
        gradleVersion = "8.12"
    }
    
    buildSearchableOptions {
        enabled = false
    }
    
    // Also disable prepareJarSearchableOptions since buildSearchableOptions is disabled
    named("prepareJarSearchableOptions") {
        enabled = false
    }
    
    // Compile Java after Kotlin to allow Java to see Kotlin classes
    compileJava {
        dependsOn(compileKotlin)
    }
    
    compileTestJava {
        dependsOn(compileTestKotlin)
    }
    
    test {
        useJUnit()
        systemProperty("idea.home.path", intellijPlatform.sandboxContainer.get().asFile.absolutePath)
    }
}
