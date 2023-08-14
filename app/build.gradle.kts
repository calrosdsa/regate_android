import com.android.build.api.dsl.Packaging
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask


plugins {
//    id("com.android.application")
//    id("kotlin-android")
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.cacheFixPlugin)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.serialization)
    alias(libs.plugins.gms.googleServices)
//    id("org.gradle.android.cache-fix")
//    id("dagger.hilt.android.plugin")
//    id("com.google.devtools.ksp")
//    kotlin("kapt")
}

android {
    namespace = "app.regate"
    compileSdk = 33

    defaultConfig {
        applicationId = "app.regate"
        minSdk = 23
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

    }



    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("release/app-debug.jks")
            storePassword = "12ab34cd56ef"
            keyAlias = "UploadDebug"
            keyPassword = "12ab34cd56ef"
        }

        create("release") {
//            if (useReleaseKeystore) {
                storeFile = rootProject.file("release/app-release.jks")
                storePassword = "12ab34cd56ef"
                keyAlias = "regate"
                keyPassword = "12ab34cd56ef"
            }
//        }
    }

    buildTypes {
        debug{
            signingConfig = signingConfigs["debug"]
        }
        release {
            isMinifyEnabled = true
//            isShrinkResources = true
            signingConfig = signingConfigs["release"]
            proguardFiles("proguard-rules.pro")
        }
    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_17
//        targetCompatibility = JavaVersion.VERSION_17
//    }
//    kotlinOptions {
//        jvmTarget = "17"
//    }
    buildFeatures {
        compose = true
        dataBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = LibVersion.composeCompilerVersion
    }
    fun Packaging.() {
        resources {
            excludes += setOf("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}


room {
    schemaLocationDir.set(file("$projectDir/schemas"))
}

object LibVersion {
    const val composeVersion = "2023.03.00"
    const val composeCompilerVersion = "1.4.4"
    const val navigationCompose = "2.5.3"
    const val roomVersion = "2.5.1"
    const val retrofitVersion = "2.9.0"
    const val moshiVersion = "1.14.0"
    const val coilVersion = "2.2.2"
    const val flowerVersion = "3.1.0"
}

tasks.withType<KotlinCompilationTask<*>> {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi")
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:${LibVersion.composeVersion}")
    val room_version = "2.5.1"
    implementation(projects.ui.bottom.reserva)
    implementation(projects.ui.bottom.auth)
    implementation(projects.ui.bottom.map)

    implementation(projects.common.resources)
    implementation(projects.common.compose)

    implementation(projects.domain)

    implementation(projects.core.base)
//    implementation(projects.core.logging)
    implementation(projects.core.preferences)
//    implementation(projects.data.api)
    implementation(projects.data.auth)
    implementation(projects.data.repository.account)
    implementation(projects.data.repository.establecimiento)
    implementation(projects.data.repository.instalacion)
    implementation(projects.data.repository.reserva)
    implementation(projects.data.repository.sala)
    implementation(projects.data.repository.users)
    implementation(projects.data.repository.labels)
    implementation(projects.data.repository.grupo)
    implementation(projects.data.repository.coin)

    implementation(projects.data.dbRoom)

//    implementation(projects.ui.map)
    implementation(projects.ui.welcome)
    implementation(projects.ui.auth)

    implementation(projects.ui.account.profile)
    implementation(projects.ui.account.reservas)
    implementation(projects.ui.account.coin)
    implementation(projects.ui.account.billing)
    implementation(projects.ui.account.inbox)
    implementation(projects.ui.account.favoritos)
    implementation(projects.ui.media)

    implementation(projects.ui.inicio.home)
    implementation(projects.ui.inicio.servicios)
    implementation(projects.ui.inicio.discover)
    implementation(projects.ui.inicio.grupos)
    implementation(projects.ui.inicio.actividades)

    implementation(projects.ui.entidad.complejo)
    implementation(projects.ui.entidad.instalacion)
    implementation(projects.ui.entidad.reservar)
    implementation(projects.ui.entidad.salas)
    implementation(projects.ui.entidad.actividades)

    implementation(projects.ui.settings)
    implementation(projects.ui.grupo.chat)
    implementation(projects.ui.grupo.sala)
    implementation(projects.ui.grupo.detail)
    implementation(projects.ui.grupo.createsala)
    implementation(projects.ui.grupo.creategrupo)

    implementation(projects.ui.review)

    implementation(projects.tasks.android)


    implementation("androidx.room:room-ktx:$room_version")

    implementation("com.mapbox.maps:android:10.14.0"){
        exclude("group_name","module_name")
    }



    implementation(libs.play.service.auth)

    implementation("androidx.core:core-ktx:1.9.0")
//    implementation("androidx.activity:activity-compose:1.7.0")
    implementation("androidx.activity:activity-compose:1.7.2")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")
//    implementation(composeBom)
//    implementation("androidx.compose.material3:material3")
    implementation(libs.compose.material3)

    implementation("androidx.compose.ui:ui-tooling-preview")
//    implementation("androidx.navigation:navigation-compose:${LibVersion.navigationCompose}")
    implementation(libs.androidx.navigation.compose)

    implementation(libs.okhttp.loggingInterceptor)


    implementation("com.google.accompanist:accompanist-systemuicontroller:0.30.0")
    implementation(libs.accompanist.navigation.animation)
    implementation(libs.accompanist.navigation.material)
    implementation(libs.compose.material.iconsext)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.websocket)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(libs.coil.coil)

    implementation(libs.kotlininject.runtime)
    ksp(libs.kotlininject.compiler)

    implementation(platform(libs.firabase.bom))
    implementation(libs.google.firebase.messaging)
    implementation(libs.google.firebase.firestore)

    implementation("com.google.android.gms:play-services-location:21.0.1")


    implementation(libs.androidx.work.runtime)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // UI Tests
    androidTestImplementation(composeBom)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    // Android Studio Preview support
    debugImplementation("androidx.compose.ui:ui-tooling")
}

