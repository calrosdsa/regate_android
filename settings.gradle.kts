import org.gradle.internal.impldep.org.jsoup.safety.Safelist.basic

//pluginManagement {
//    repositories {
//        gradlePluginPortal()
//        google()
//        mavenCentral()
//    }
//}
//dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
//    repositories {
//        google()
//        mavenCentral()
//    }
//}
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven {  url = uri("https://jitpack.io") }
    }
}

plugins {
    id("com.gradle.enterprise") version "3.12.6"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishAlways()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
// https://docs.gradle.org/7.6/userguide/configuration_cache.html#config_cache:stable
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

rootProject.name = "regate"

include(
    ":core:base",
//    ":core:logging",
    ":core:preferences",
    ":ui:auth",
    ":ui:welcome",
    ":ui:search",
//    ":ui:map",
    ":ui:inicio:home",
    ":ui:inicio:discover",
    ":ui:inicio:actividades",
    ":ui:inicio:servicios",
    ":ui:inicio:grupos",

    ":ui:entidad:complejo",
    ":ui:entidad:instalacion",
    ":ui:entidad:reservar",
    ":ui:entidad:salas",
    ":ui:entidad:photos",
    ":ui:entidad:actividades",

    ":ui:bottom:reserva",
    ":ui:bottom:auth",
    ":ui:bottom:map",
//    ":ui:detail",

    ":ui:grupo:sala",
    ":ui:grupo:chat",
    ":ui:grupo:creategrupo",
    ":ui:grupo:chatsala",
    ":ui:grupo:createsala",
    ":ui:grupo:detail",

    ":ui:account:profile",
    ":ui:account:reservas",
    ":ui:account:favoritos",
    ":ui:account:inbox",
    ":ui:account:coin",
    ":ui:account:billing",

    ":ui:system",
    ":ui:settings",
    ":ui:media",
    ":ui:review",

    ":app",
    ":common:resources",
    ":common:compose",
    ":common:view",
    ":domain",
    ":data:db",
    ":data:db-room",
//    ":data:api",
    ":data:legacy",
    ":data:auth",
//    ":data:auth_app",
    ":data:models",

    ":data:repository:account",
    ":data:repository:establecimiento",
    ":data:repository:instalacion",
    ":data:repository:reserva",
    ":data:repository:sala",
    ":data:repository:labels",
    ":data:repository:users",
    ":data:repository:grupo",
    ":data:repository:coin",
    ":data:repository:system",
    ":data:repository:conversation",
    ":data:repository:search",

    ":tasks:android",
    ":tasks:api",
    )


//
//
//plugins {
//    id("com.android.application")
//    id("org.jetbrains.kotlin.android")
//    id("com.google.devtools.ksp")
//    id("org.jetbrains.kotlin.kapt")
//}
//
//android {
//    namespace = "com.app.expendedora"
//    compileSdk = 34
//
//    defaultConfig {
//        applicationId = "com.app.expendedora"
//        minSdk = 21
//        targetSdk = 34
//        versionCode = 1
//        versionName = "1.0"
//
//        testInstrumentationRunner= "androidx.test.runner.AndroidJUnitRunner"
//
////        javaCompileOptions {
////            annotationProcessorOptions {
////                arguments["room.schemaLocation"] =
////                    "$projectDir/schemas"
////            }
////        }
//        ksp {
//            arg("room.schemaLocation", "$projectDir/schemas")
//        }
//    }
//
//    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles("proguard-rules.pro")
//        }
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_17
//        targetCompatibility = JavaVersion.VERSION_17
//    }
//    kotlinOptions {
//        jvmTarget = "17"
//    }
//    buildFeatures {
//        viewBinding = true
//        compose = true
//    }
//
//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.4.3"
//    }
//}
//
////room {
////    schemaLocationDir.set(file("$projectDir/schemas"))
////}
//
//
//dependencies {
//
//    implementation("androidx.core:core-ktx:1.8.0")
//    implementation("androidx.appcompat:appcompat:1.6.1")
//    implementation("com.google.android.material:material:1.5.0")
//    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
//    implementation("androidx.navigation:navigation-fragment-ktx:2.7.4")
//    implementation("androidx.navigation:navigation-ui-ktx:2.7.4")
//    implementation("androidx.compose.ui:ui-android:1.5.4")
//    implementation(files("libs/tcn_springboard-release.aar"))
//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//
//    api(platform("androidx.compose:compose-bom:2023.10.01"))
////    implementation(composeBom)
////    androidTestImplementation composeBom
//
//
//    implementation("androidx.compose.material3:material3")
//    // or Material Design 2
//    implementation("androidx.compose.material:material")
//    // or skip Material Design and build directly on top of foundational components
//    implementation("androidx.compose.foundation:foundation")
//    // or only import the main APIs for the underlying toolkit systems,
//    // such as input and measurement/layout
//    implementation("androidx.compose.ui:ui")
//
//    // Android Studio Preview support
//    implementation("androidx.compose.ui:ui-tooling-preview")
//    debugImplementation("androidx.compose.ui:ui-tooling")
//
//    // UI Tests
//    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
//    debugImplementation("androidx.compose.ui:ui-test-manifest")
//
//    // Optional - Included automatically by material, only add when you need
//    // the icons but not the material library (e.g. when using Material3 or a
//    // custom design system based on Foundation)
//    implementation("androidx.compose.material:material-icons-core")
//    // Optional - Add full set of material icons
//    implementation("androidx.compose.material:material-icons-extended")
//
//
//    implementation("me.tatarka.inject:kotlin-inject-runtime:0.6.1")
//    ksp("me.tatarka.inject:kotlin-inject-compiler-ksp:0.6.1")
//
//    //room
//    implementation("androidx.room:room-runtime:2.6.0-alpha01")
//    implementation("androidx.room:room-ktx:2.6.0-alpha01")
//    ksp("androidx.room:room-compiler:2.6.0-alpha01")
//    implementation("androidx.room:room-runtime:2.6.0-alpha01")
//    implementation("androidx.room:room-paging:2.6.0-alpha01")
//
//
//
//}