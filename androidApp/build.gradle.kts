import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.service)
}

android {
    namespace = "com.pascal.noctra"
    compileSdk = 36

    androidResources {
        generateLocaleConfig = true
    }

    defaultConfig {
        minSdk = 24
        targetSdk = 36

        applicationId = "com.pascal.noctra"
        versionCode = 2
        versionName = "1.0.0"
    }

    flavorDimensions += "environment"
    productFlavors {
        create("dev") {
            dimension = "environment"
        }
        create("prod") {
            dimension = "environment"
        }
    }

    signingConfigs {
        val releaseKeystorePath = providers.gradleProperty("ANDROID_KEYSTORE_PATH").orNull
            ?: System.getenv("ANDROID_KEYSTORE_PATH")
        if (!releaseKeystorePath.isNullOrBlank()) {
            create("release") {
                storeFile = file(releaseKeystorePath)
                storePassword = providers.gradleProperty("ANDROID_KEYSTORE_PASSWORD").orNull
                    ?: System.getenv("ANDROID_KEYSTORE_PASSWORD")
                keyAlias = providers.gradleProperty("ANDROID_KEY_ALIAS").orNull
                    ?: System.getenv("ANDROID_KEY_ALIAS")
                keyPassword = providers.gradleProperty("ANDROID_KEY_PASSWORD").orNull
                    ?: System.getenv("ANDROID_KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = true
            signingConfigs.findByName("release")?.let {
                signingConfig = it
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    applicationVariants.all {
        outputs.all {
            if (name.contains("release")) {
                (this as com.android.build.gradle.internal.api.BaseVariantOutputImpl)
                    .outputFileName = "noctra-$name-$versionName.apk"
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions { jvmTarget.set(JvmTarget.JVM_17) }
}

dependencies {
    implementation(project(":sharedUI"))
    implementation(libs.androidx.activityCompose)
    implementation(libs.koin.android)
    debugImplementation(libs.androidx.ui.tooling)
}
