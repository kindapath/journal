plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)

    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    applyDefaultHierarchyTemplate()

    androidLibrary {
        namespace = "com.kindaboii.journal.features.entries.impl"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
        androidResources {
            enable = true
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "EntriesImpl"
            isStatic = true
        }
    }

    jvm()

    js {
        browser()
        binaries.executable()
    }

    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)

    sourceSets {
        val commonMain by getting
        val androidMain by getting
        val jvmMain by getting
        val jsMain by getting
        val iosMain by getting

        val nonJsMain by creating {
            dependsOn(commonMain)
        }

        androidMain.dependsOn(nonJsMain)
        jvmMain.dependsOn(nonJsMain)
        iosMain.dependsOn(nonJsMain)

        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)

            implementation(libs.jetbrains.navigation3.ui)

            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.cryptography.core)
            implementation(libs.cryptography.provider.optimal)

            implementation(libs.coroutines.extensions)

            implementation(project(":common:ui"))
            implementation(project(":common:network"))

            implementation(project(":data:database"))

            implementation(project(":features:entries:api"))
            implementation(project(":features:entries:schema"))
            implementation(project(":features:auth:api"))
            implementation(project(":features:profile:api"))
            implementation(project(":features:stats:api"))
        }

        nonJsMain.dependencies {
            implementation(libs.powersync.connector.supabase)
        }

        jvmMain.dependencies {
            implementation(libs.openhtmltopdf.pdfbox)
            implementation(libs.jsoup)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}
