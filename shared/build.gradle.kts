import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    applyDefaultHierarchyTemplate()

    androidLibrary {
        namespace = "com.kindaboii.journal.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
    }

    iosArm64()
    iosSimulatorArm64()

    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().configureEach {
        if (name.startsWith("ios")) {
            binaries.framework {
                baseName = "JournalShared"
                isStatic = true
            }
        }
    }

    jvm()

    js {
        browser()
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting
        val androidMain by getting
        val iosMain by getting
        val jvmMain by getting
        val jsMain by getting

        val nonJsMain by creating {
            dependsOn(commonMain)
        }

        androidMain.dependsOn(nonJsMain)
        iosMain.dependsOn(nonJsMain)
        jvmMain.dependsOn(nonJsMain)

        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)

            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)

            implementation(libs.kotlinx.datetime)

            implementation(libs.jetbrains.navigation3.ui)

            implementation(project(":common:network"))
            implementation(project(":common:ui"))

            implementation(project(":data:database"))

            implementation(project(":features:entries:api"))
            implementation(project(":features:entries:impl"))
            implementation(project(":features:auth:api"))
            implementation(project(":features:auth:impl"))
            implementation(project(":features:profile:api"))
            implementation(project(":features:profile:impl"))
            implementation(project(":features:stats:api"))
            implementation(project(":features:stats:impl"))
        }

        nonJsMain.dependencies {
            implementation(libs.powersync.connector.supabase)
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}
