
plugins {
    id("kotlin")
    alias(libs.plugins.android.lint)
    alias(libs.plugins.ksp)
}

dependencies {
    api(projects.data.models)
//    api(projects.data.traktauth)
    implementation(projects.data.db)
    implementation(projects.core.preferences)
//    implementation(projects.data.auth)
    implementation(projects.data.legacy) // remove this eventually

//    api(projects.api.trakt)
//    api(projects.api.tmdb)

    api(libs.store)
    implementation(libs.kotlinx.atomicfu)

    implementation(libs.kotlininject.runtime)
    implementation(libs.ktor.client.core)
//    implementation(libs.ktor.client.auth)
    ksp(libs.kotlininject.compiler)
}
