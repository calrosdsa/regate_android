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
    namespace = "app.regate.grupo.chat"
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
    implementation(projects.data.db)

    implementation("io.github.dokar3:sheets-m3:0.4.4")
    implementation(libs.ktor.client.core)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)

    implementation(libs.compose.foundation.foundation)
    implementation(libs.compose.foundation.layout)
//    implementation(libs.compose.material.material)
    implementation(libs.compose.material.iconsext)
    implementation(libs.compose.material3)
    implementation(libs.compose.animation.animation)
    implementation(libs.compose.ui.tooling)

    implementation(libs.component.swipe)


    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    ksp(libs.kotlininject.compiler)

    lintChecks(libs.slack.lint.compose)
}
