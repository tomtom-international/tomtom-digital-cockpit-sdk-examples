/*
 * Copyright © 2020 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

import com.android.builder.core.BuilderConstants
import com.tomtom.ivi.buildsrc.environment.ProjectAbis
import com.tomtom.ivi.buildsrc.extensions.android
import com.tomtom.ivi.buildsrc.extensions.getGradleProperty
import com.tomtom.ivi.buildsrc.extensions.kotlinOptions
import com.tomtom.ivi.platform.gradle.api.common.dependencies.IviDependencySource
import com.tomtom.ivi.platform.gradle.api.framework.config.ivi
import com.tomtom.ivi.platform.gradle.api.tools.emulators.iviEmulators
import com.tomtom.ivi.platform.gradle.api.tools.version.iviAndroidVersionCode
import com.tomtom.ivi.platform.gradle.api.tools.version.iviVersion
import com.tomtom.navtest.android.android
import com.tomtom.navtest.android.androidRoot
import com.tomtom.navtest.extensions.navTest
import com.tomtom.navtest.extensions.navTestRoot
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    `kotlin-dsl`
    id("com.android.application") apply false
    id("com.android.library") apply false
    id("com.android.test") apply false
    id("com.google.devtools.ksp") apply false
    id("com.tomtom.ivi.platform.framework.config") apply true
    id("com.tomtom.ivi.platform.tools.emulators") apply true
    id("com.tomtom.ivi.platform.tools.version") apply true
    id("com.tomtom.navtest") apply true
    id("com.tomtom.navtest.android") apply true
    id("com.tomtom.navapp.emulators-plugin") apply false
    id("com.tomtom.tools.android.extractstringsources") apply false
}

val isRunningOnCi: Boolean by extra(
    (System.getenv("TF_BUILD") ?: System.getenv("BUILD_BUILDNUMBER"))
        .orEmpty()
        .isNotEmpty()
)

val jvmVersion = JavaVersion.toVersion(iviDependencies.versions.jvm.get())

// Make a single directory where to store all test results.
val testOutputDirectory: File by extra {
    val testRootDir: File by extra(File(rootProject.projectDir, "IviTest"))
    if (isRunningOnCi) {
        testRootDir
    } else {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        testRootDir.resolve(LocalDateTime.now().format(formatter))
    }
}

ivi {
    dependencySource =
        IviDependencySource.ArtifactRepository(libraries.versions.iviPlatform.get())
}

iviEmulators {
    findProperty("emulatorsDirectory")?.toString()?.let {
        emulatorsDirectory = File(it)
    }
    findProperty("multiDisplay")?.toString()?.toBoolean()?.let {
        enableMultiDisplay = it
    }
    findProperty("numberOfEmulators")?.toString()?.toInt()?.let {
        numberOfInstances = it
    }
    findProperty("emulatorImage")?.let {
        emulatorImage = it.toString()
    }
    minApiLevel = iviDependencies.versions.minSdk.get().toInt()
    outputDirectory = testOutputDirectory
    targetApiLevel = iviDependencies.versions.compileSdk.get().toInt()
}

// Set up global test options
tasks.withType<Test> {
    testLogging {
        // Logging exceptions verbosely helps on CI to immediately see the source of testing
        // errors, especially in case of crashes.
        exceptionFormat = TestExceptionFormat.FULL
        showExceptions = true
        showCauses = true
        showStackTraces = true
    }
}

// Set up the NavTest framework.
navTestRoot {
    // Specify where the report and artifacts of the tests will be archived
    outputDir.set(testOutputDirectory)

    androidRoot {
        deviceUsageReport {
            enabled.set(true)
        }
    }

    timeline {
        enabled.set(true)
    }

    suites {
        create("unit") {
            includeTags += "unit"
        }
        create("integration") {
            includeTags += "integration"
        }
        create("e2e") {
            includeTags += "e2e"
        }
    }
}

tasks.register<DefaultTask>("showAllDependencies") { }

allprojects {
    val project = this
    rootProject.tasks.named("showAllDependencies") {
        if (project.name == rootProject.name) {
            dependsOn("dependencies")
        } else {
            dependsOn(":${project.name}:dependencies")
        }
    }
}

subprojects {
    val isApplicationProject by extra(getGradleProperty("isApplicationProject", false))
    val isAndroidTestProject by extra(getGradleProperty("isAndroidTestProject", false))

    val iviDependencies = rootProject.iviDependencies
    val versions = rootProject.iviDependencies.versions

    when {
        isApplicationProject -> apply(plugin = "com.android.application")
        isAndroidTestProject -> apply(plugin = "com.android.test")
        else -> apply(plugin = "com.android.library")
    }

    apply(plugin = "kotlin-android")
    apply(plugin = "kotlin-parcelize")

    dependencies {
        implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

        constraints {
            // kotlin-reflect dependency is not constrained up by kotlin-bom, so we need to
            // constrain it explicitly.
            implementation(iviDependencies.kotlinReflect)
        }

        implementation(iviDependencies.androidxAnnotation)
        implementation(iviDependencies.androidxCoreKtx)
    }

    // Override some conflicting transitive dependencies which duplicate classes.
    configurations.all {
        exclude(group = "org.bouncycastle", module = "bcprov-jdk15to18")
        exclude(group = "org.bouncycastle", module = "bcutil-jdk15to18")

        resolutionStrategy {
            eachDependency {
                when (requested.group) {
                    "org.jetbrains.kotlin" ->
                        useVersion(versions.kotlin.get())
                    "org.jetbrains.kotlinx" -> {
                        when (requested.name) {
                            "kotlinx-coroutines-core",
                            "kotlinx-coroutines-core-jvm",
                            "kotlinx-coroutines-android",
                            "kotlinx-coroutines-test" ->
                                useVersion(versions.kotlinxCoroutines.get())
                            "kotlinx-serialization-json" ->
                                useVersion(versions.kotlinxSerialization.get())
                        }
                    }
                }
            }
            dependencySubstitution {
                substitute(module("org.hamcrest:hamcrest-core:1.3"))
                    .using(module("org.hamcrest:hamcrest:2.2"))
            }
        }
    }

    android {
        compileSdkVersion(versions.compileSdk.get().toInt())
        buildToolsVersion = versions.buildTools.get()

        defaultConfig {
            minSdk = versions.minSdk.get().toInt()
            targetSdk = versions.targetSdk.get().toInt()
            if (isApplicationProject) {
                versionCode = iviAndroidVersionCode
                versionName = iviVersion
            }
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        // TODO(IVI-8723): Force `androidx.core` to TomTom Digital Cockpit platform version:
        // TODO(IVI-8723): the latest sets compileSdk=33.
        configurations.all {
            resolutionStrategy.force("androidx.core:core:1.8.0")
            resolutionStrategy.force("androidx.core:core-ktx:1.8.0")
            resolutionStrategy.force("com.google.android.material:material:1.6.1")
        }

        compileOptions {
            sourceCompatibility = jvmVersion
            targetCompatibility = jvmVersion
        }

        kotlinOptions {
            @Suppress("UnstableApiUsage")
            jvmTarget = versions.jvm.get()
        }

        lintOptions {
            // lintConfig = File("lint.xml")
            isAbortOnError = false
            isCheckAllWarnings = true
            isCheckDependencies = false
            isWarningsAsErrors = true
            xmlOutput = File(buildDir, "reports/lint/report.xml")
            htmlOutput = File(buildDir, "reports/lint/report.html")

            disable(
                // New dependency version checking is done during development.
                "GradleDependency",
                "NewerVersionAvailable",
                // Use of synthetic accessors is accepted, assuming we'll be using multidexing.
                "SyntheticAccessor",
                // The product does require protected system permissions.
                "ProtectedPermissions",
                // Accessibility text is not useful in this product.
                "ContentDescription",
                // We don't need to be indexable by Google Search.
                "GoogleAppIndexingApiWarning",
                // 'supportsRtl' is defined in the product manifest.
                "RtlEnabled",
                // Ignore duplicate strings, which are most likely due to using the same
                // string in different contexts.
                "DuplicateStrings",
                // Do not check for style-related properties, as they're defined as attributes
                // and used in the styles, and thus depend on dependencies to be verified.
                // The main product module does enable these checks.
                "RequiredSize",
                "UnusedResources"
            )
        }

        packagingOptions {
            // For NavKit 2, pick the first binary found when there are multiple.
            pickFirsts.add("lib/**/*.so")
            // NOTE: Do not strip any binaries: they should already come stripped from the
            // release artifacts; and since we don't use an NDK, they cannot be stripped anyway.
            jniLibs.keepDebugSymbols.add("*.so")
            pickFirsts.add("META-INF/io.netty.versions.properties")
            resources.excludes.add("META-INF/INDEX.LIST")
            resources.excludes.add("META-INF/LICENSE.md")
            resources.excludes.add("META-INF/NOTICE.md")
        }

        // Split the output into multiple APKs based on their ABI.
        splits.abi {
            @Suppress("UnstableApiUsage")
            isEnable = true
            reset()
            include(*ProjectAbis.enabledAbis)
        }

        val projectSourceSets by extra(mutableSetOf<String>())
        sourceSets.all {
            val path = "src/$name/kotlin"
            java.srcDir(path)
            File(projectDir, path).takeIf { it.exists() }?.let {
                projectSourceSets += it.absolutePath
            }
        }

        apply(plugin = "com.tomtom.ivi.platform.tools.signing-config")

        apply(plugin = "com.tomtom.navtest")
        apply(plugin = "com.tomtom.navtest.android")

        navTest.android {
            // Applies for functional tests under "androidTest" directory.
            androidTest {
                enabled.set(true)
                // Tags are set by subprojects.

                // Allow specifying the test-class via command-line
                findProperty("testClass")?.let {
                    instrumentationArguments.className.set(it as String)
                }
            }

            pluginManager.withPlugin("com.tomtom.ivi.platform.framework.config.activity-test") {
                androidTest {
                    testTags.add("integration")
                    timeout.set(10 * 60)
                }
            }

            if (!isAndroidTestProject) {
                // Applies for unit tests under "test" directory.
                unit {
                    enabled.set(true)
                    testTags.add("unit")
                    variantFilter = { it.buildType == BuilderConstants.DEBUG }
                }
            }
        }
    }
}
