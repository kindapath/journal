plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)

    alias(libs.plugins.kotlinSerialization)

    alias(libs.plugins.sqldelight)
}

sqldelight {
    databases {
        create("EntryDatabase") {
            packageName.set("com.kindaboii.journal")
            generateAsync.set(true)
        }
    }
}

kotlin {
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
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
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

            implementation(libs.coroutines.extensions)

            implementation(project(":common:ui"))
            implementation(project(":features:entries:api"))
        }

        androidMain.dependencies {
            implementation(libs.android.driver)
        }

        iosMain.dependencies {
            implementation(libs.native.driver)
        }

        jvmMain.dependencies {
            implementation(libs.sqlite.driver)
        }

        jsMain.dependencies {
            implementation(libs.web.worker.driver)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}
