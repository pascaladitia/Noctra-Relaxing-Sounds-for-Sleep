@file:Suppress("DEPRECATION")

import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
    alias(libs.plugins.buildKonfig)
    alias(libs.plugins.kotlinCocoapods)
}

kotlin {
    androidTarget {
        compilerOptions { jvmTarget = JvmTarget.JVM_17 }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        version = "1.0.0"
        summary = "Shared noctra UI and Firebase auth layer"
        homepage = "https://example.com/TODO-noctra-homepage"
        ios.deploymentTarget = "15.0"

        pod("GoogleSignIn")
        pod("Google-Mobile-Ads-SDK")
    }

    sourceSets {
        val commonMain by getting {
            resources.srcDir("src/commonMain/composeResources")
        }

        commonMain.dependencies {
            api(libs.compose.runtime)
            api(libs.compose.ui)
            api(libs.compose.foundation)
            api(libs.compose.resources)
            api(libs.compose.ui.tooling.preview)
            api(libs.compose.material3)

            implementation(libs.material.icons.extended)
            implementation(libs.kermit)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.serialization.json)
            implementation(libs.ktor.client.logging)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime)
            implementation(libs.androidx.navigation.compose)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.annotations)
            implementation(libs.coil)
            implementation(libs.coil.network.ktor)
            implementation(libs.multiplatformSettings)
            implementation(libs.room.runtime)

            implementation(libs.moko.permission)
            implementation(libs.moko.permission.camera)
            implementation(libs.moko.permission.location)
            implementation(libs.moko.permission.notifications)
            implementation(libs.paging.compose.common)
            implementation(libs.paging.common)
            implementation(libs.constraintlayout.compose.multiplatform)
            implementation(libs.peekaboo.ui)
            implementation(libs.peekaboo.image.picker)
            implementation(libs.sqlite.bundled)
            implementation(libs.compose.webview.multiplatform)
            implementation(libs.composeIcons.featherIcons)
            implementation(libs.compose.multiplatform.media.player)
            implementation(libs.firebase.app)
            implementation(libs.firebase.auth)
            implementation(libs.firebase.config)
            implementation(libs.firebase.firestore)
            implementation(libs.firebase.messaging)
            implementation(libs.compottie)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.compose.ui.test)
            implementation(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.androidx.ui.tooling)
            implementation(libs.androidx.ui.tooling.android)
            implementation(libs.androidx.ui.tooling.preview)
            implementation(libs.androidx.customview)
            implementation(libs.androidx.customview.poolingcontainer)
            implementation(libs.androidx.preference.ktx)
            implementation(libs.androidx.appcompat)
            implementation(libs.room.runtime.android)
            implementation(libs.play.services.auth)
            implementation(libs.play.services.location)
            implementation(libs.play.services.ads)
            implementation(libs.androidx.media)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

    }

    targets
        .withType<KotlinNativeTarget>()
        .matching { it.konanTarget.family.isAppleFamily }
        .configureEach {
            binaries {
                framework {
                    baseName = "SharedUI"
                    binaryOption("bundleId", "com.pascal.noctra.sharedUI")
                    isStatic = true
                }
            }
        }
}

android {
    namespace = "com.pascal.noctra"
    compileSdk = 36
    flavorDimensions += "environment"
    productFlavors {
        create("dev") {
            dimension = "environment"
        }
        create("prod") {
            dimension = "environment"
        }
    }
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    debugImplementation(libs.compose.ui.tooling)
}

buildkonfig {
    packageName = "com.pascal.noctra"

    defaultConfigs {
        buildConfigField(FieldSpec.Type.STRING, "BASE_URL", "https://www.sample.com")

        buildConfigField(FieldSpec.Type.STRING, "ADMOB_APP_OPEN_ID", "ca-app-pub-3940256099942544/9257395921")
        buildConfigField(FieldSpec.Type.STRING, "ADMOB_BANNER_ID", "ca-app-pub-3940256099942544/6300978111")
        buildConfigField(FieldSpec.Type.STRING, "ADMOB_INTERSTITIAL_ID", "ca-app-pub-3940256099942544/1033173712")
    }
    defaultConfigs("dev") {
        buildConfigField(FieldSpec.Type.STRING, "BASE_URL", "https://www.sample.com")

        buildConfigField(FieldSpec.Type.STRING, "ADMOB_APP_OPEN_ID", "ca-app-pub-3940256099942544/9257395921")
        buildConfigField(FieldSpec.Type.STRING, "ADMOB_BANNER_ID", "ca-app-pub-3940256099942544/6300978111")
        buildConfigField(FieldSpec.Type.STRING, "ADMOB_INTERSTITIAL_ID", "ca-app-pub-3940256099942544/1033173712")
    }
    defaultConfigs("prod") {
        buildConfigField(FieldSpec.Type.STRING, "BASE_URL", "https://www.sample.com")

        buildConfigField(FieldSpec.Type.STRING, "ADMOB_APP_OPEN_ID", "ca-app-pub-3940256099942544/9257395921")
        buildConfigField(FieldSpec.Type.STRING, "ADMOB_BANNER_ID", "ca-app-pub-3940256099942544/6300978111")
        buildConfigField(FieldSpec.Type.STRING, "ADMOB_INTERSTITIAL_ID", "ca-app-pub-3940256099942544/1033173712")
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    with(libs.room.compiler) {
        add("kspAndroid", this)
        add("kspIosArm64", this)
        add("kspIosX64", this)
        add("kspIosSimulatorArm64", this)
    }
}
