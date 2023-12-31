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
//    alias(libs.plugins.ksp)
}

android {
    namespace = "app.regate.grupos"
    compileSdk = 33
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


    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // For registerForActivityResult
    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.compose.material.iconsext)

    implementation(libs.compose.foundation.foundation)
    implementation(libs.compose.foundation.layout)
//    implementation(libs.compose.material.material)
    implementation(libs.compose.material3)
    implementation(libs.compose.animation.animation)
    implementation(libs.compose.ui.tooling)

    implementation(libs.coil.compose)

    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
    implementation(libs.compose.material.material)

    implementation(libs.io.github.dokar3)




//    ksp(libs.kotlininject.compiler)

//    lintChecks(libs.slack.lint.compose)
}
