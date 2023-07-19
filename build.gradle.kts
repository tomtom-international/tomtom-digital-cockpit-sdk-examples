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

import com.tomtom.ivi.buildsrc.environment.ProjectAbis
import com.tomtom.ivi.buildsrc.extensions.android
import com.tomtom.ivi.buildsrc.extensions.androidApplication
import com.tomtom.ivi.buildsrc.extensions.getGradleProperty
import com.tomtom.ivi.buildsrc.extensions.kotlinOptions
import com.tomtom.ivi.platform.gradle.api.common.dependencies.IviDependencySource
import com.tomtom.ivi.platform.gradle.api.framework.config.ivi
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
    id("com.tomtom.tools.android.extractstringsources") apply false
}

val jvmVersion = JavaVersion.toVersion(iviDependencies.versions.jvm.get())

// Make a single directory where to store all test results.
val testOutputDirectory: File by extra {
    val testRootDir: File by extra(File(rootProject.projectDir, "IviTest"))
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
    testRootDir.resolve(LocalDateTime.now().format(formatter))
}

ivi {
    dependencySource =
        IviDependencySource.ArtifactRepository(libraries.versions.iviPlatform.get())
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

subprojects {
    val isApplicationProject by extra(getGradleProperty("isApplicationProject", false))

    val iviDependencies = rootProject.iviDependencies
    val versions = rootProject.iviDependencies.versions

    when {
        isApplicationProject -> apply(plugin = "com.android.application")
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
        compileSdk = versions.compileSdk.get().toInt()
        buildToolsVersion = versions.buildTools.get()

        defaultConfig {
            minSdk = versions.minSdk.get().toInt()
            // AutomotiveUI has enabled flavorized publication of their modules, because of
            // this, it is now needed on the integrator side to specify which flavor to use.
            missingDimensionStrategy("engine", "navkit1")
            // TTDC uses flavorized publication of modules, because of
            // this, it is now needed on the integrator side to specify which flavor to use.
            missingDimensionStrategy("navigationEngine", "navkit1")
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        if (isApplicationProject) {
            androidApplication {
                defaultConfig.targetSdk = versions.targetSdk.get().toInt()
                // Use hardcoded product versions, or pass them from CI, or adopt a solution for
                // dynamic version codes. See the recommendations from Android Gradle Plugin docs:
                //  - https://developer.android.com/studio/publish/versioning
                //  - https://developer.android.com/build/gradle-tips#configure-dynamic-version-codes
                defaultConfig.versionCode = 1
                defaultConfig.versionName = "1.0"
            }
        }

        compileOptions {
            sourceCompatibility = jvmVersion
            targetCompatibility = jvmVersion
        }

        kotlinOptions {
            @Suppress("UnstableApiUsage")
            jvmTarget = versions.jvm.get()
        }

        lint {
            abortOnError = false
            checkReleaseBuilds = false
        }

        packagingOptions {
            jniLibs {
                // For NavKit, pick the first binary found when there are multiple.
                pickFirsts += "lib/**/*.so"
                // NOTE: Do not strip any binaries: they should already come stripped from the
                // release artifacts; and since we don't use an NDK, they cannot be stripped
                // anyway.
                keepDebugSymbols.add("**/*.so")
            }
            resources {
                excludes.addAll(
                    listOf(
                        "META-INF/INDEX.LIST",
                        "META-INF/LICENSE.md",
                        "META-INF/LICENSE-notice.md",
                        "META-INF/NOTICE.md",
                    )
                )
                pickFirsts += "META-INF/io.netty.versions.properties"
            }
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
    }
}
