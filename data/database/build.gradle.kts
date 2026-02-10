import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.sqldelight)
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.kindaboii.journal.data.database")
            generateAsync.set(true)
            dependency(project(":features:entries:schema"))
        }
    }
}

kotlin {
    applyDefaultHierarchyTemplate()

    androidLibrary {
        namespace = "com.kindaboii.journal.data.database"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "AppDatabase"
            isStatic = true
        }
    }

    jvm()

    js {
        browser()
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting
        val jsMain by getting
        val androidMain by getting
        val jvmMain by getting

        val iosMain by getting

        val nonJsMain by creating {
            dependsOn(commonMain)
        }

        androidMain.dependsOn(nonJsMain)
        iosMain.dependsOn(nonJsMain)
        jvmMain.dependsOn(nonJsMain)

        commonMain.dependencies {
            implementation(libs.coroutines.extensions)
            implementation(libs.kotlinx.serialization.json)
            implementation(project(":features:entries:schema"))
            implementation(libs.sqldelight.runtime)
            implementation(libs.koin.core)
        }

        nonJsMain.dependencies {
            implementation(libs.powersync.core)
            implementation(libs.powersync.integration.sqldelight)
            implementation(libs.powersync.connector.supabase)
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

            implementation(npm("@cashapp/sqldelight-sqljs-worker", "2.2.1"))
            implementation(npm("sql.js", "1.13.0"))
            implementation(devNpm("copy-webpack-plugin", "13.0.1"))
        }
    }

}
