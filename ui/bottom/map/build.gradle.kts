/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 limitations under the License.
 */


plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.cacheFixPlugin)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "app.regate.bottom.auth"
//    compileSdk = 33
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composecompiler.get()
    }
}

dependencies {
    implementation(projects.common.resources)
    implementation(projects.common.compose)
    implementation(projects.domain)

    implementation(libs.play.service.auth)

    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation("com.mapbox.maps:android:10.14.0")


    // For registerForActivityResult
    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.navigation.compose)

    implementation(libs.compose.foundation.foundation)
    implementation(libs.compose.foundation.layout)
//    implementation(libs.compose.material.material)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.iconsext)

//    implementation("androidx.compose.material3:material3:1.0.1")
    implementation(libs.compose.animation.animation)
    implementation(libs.compose.ui.tooling)

    implementation(libs.coil.compose)

    ksp(libs.kotlininject.compiler)

    lintChecks(libs.slack.lint.compose)

//    implementation(libs.kotlin.d)
}
