/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.cacheFixPlugin)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.navigation)
}

android {
    namespace = "app.regate.map"

    buildFeatures {
//        viewBinding = true
        dataBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composecompiler.get()
    }
}

dependencies {
    implementation(projects.ui.entidad.actividades)
    implementation(projects.ui.entidad.salas)
    implementation(projects.ui.entidad.reservar)
    implementation(projects.ui.entidad.complejo)

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

    implementation(projects.data.dbRoom)



    api(platform(libs.compose.bom))
    implementation(libs.compose.ui.ui)
    implementation(libs.compose.ui.uitextfonts)
    implementation(libs.compose.foundation.foundation)
    implementation(libs.compose.foundation.layout)
    implementation(libs.compose.material.material)
    implementation(libs.compose.material.iconsext)
    implementation(libs.compose.material3)
    implementation(libs.compose.animation.animation)
    implementation(libs.compose.ui.tooling)
    implementation(projects.core.base)
    implementation(projects.common.resources)
    implementation(projects.data.repository)
    implementation(projects.domain)
//    implementation(projects.data.api)
//    implementation(projects.common.ui.view)
//    implementation(projects.core.powercontroller)
    implementation(projects.core.preferences)

    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("me.tatarka.injectedvmprovider:injectedvmprovider-fragment-ktx:3.0.0")
//    implementation("me.tatarka.injectedvmprovider:injectedvmprovider-fragment-ktx:3.0.0")
    implementation("com.mapbox.maps:android:10.14.0")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")
//    implementation("com.google.android.material:material:1.10.0-alpha04")

    implementation("androidx.appcompat:appcompat:1.6.1")

//    implementation("androidx.activity:activity:1.7.2")
    implementation(libs.androidx.activity.activity)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.coil.coil)
//    kapt("com.android.databinding:compiler:3.1.4")
//    kapt("com.android.databinding:compiler:3.1.4")

    ksp(libs.kotlininject.compiler)
}
