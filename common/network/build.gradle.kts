plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
}

val generateApiConfig by tasks.registering(GeneratePropertiesConfigTask::class) {
    propertiesFile.set(rootProject.file("local.properties"))
    outputDirectory.set(layout.buildDirectory.dir("generated/apiconfig"))
    packageName.set("com.kindaboii.journal.network")
    objectName.set("ApiConfig")

    stringField("SUPABASE_URL", "supabase.url")
    stringField("SUPABASE_SCHEMA", "supabase.schema", default = "public")
    stringField("SUPABASE_CLIENT_API_KEY", "supabase.clientApiKey")
    stringField("POWERSYNC_URL", "powersync.url")
    stringField("POWERSYNC_DEV_TOKEN", "powersync.devToken", default = "")
    booleanField("SUPABASE_ANON_AUTH_ENABLED", "supabase.anonAuthEnabled")
    booleanField("DEBUG", "debug")
}

kotlin {
    androidLibrary {
        namespace = "com.kindaboii.journal.common.network"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "CommonNetwork"
            isStatic = true
        }
    }

    jvm()

    js {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain {
            kotlin.srcDir(generateApiConfig.flatMap { it.outputDirectory })
        }
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)

            api(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)

            implementation(libs.koin.core)
            api(libs.supabase.core)
            api(libs.supabase.postgrest)
            api(libs.supabase.realtime)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        jvmMain.dependencies {
            implementation(libs.ktor.client.cio)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        jsMain.dependencies {
            implementation(libs.ktor.client.js)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
