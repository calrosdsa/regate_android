import com.android.build.gradle.BaseExtension
import com.diffplug.gradle.spotless.SpotlessExtension
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.cacheFixPlugin) apply false
    alias(libs.plugins.android.lint) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.gms.googleServices) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.navigation) apply false

}
allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url  = uri("https://jitpack.io") }
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            authentication {
                create<BasicAuthentication>("basic")
            }
            credentials {
                username = "mapbox"
                password = "sk.eyJ1Ijoiam1pcmFuZGEyMSIsImEiOiJjbGl5cjNhZTgwaG56M2hudTg3OXdpOWdxIn0.AmU2HzmZwhpwkhDuB9TXuA"
            }
        }
        mavenLocal()


        // Needed when using the 'dev' Compose Compiler
        // maven("https://androidx.dev/storage/compose-compiler/repository/")

        // Jetpack Compose SNAPSHOTs if needed
        // maven("https://androidx.dev/snapshots/builds/$composeSnapshot/artifacts/repository/")

        // Used for snapshots if needed
        // maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        // maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }


    apply(plugin = rootProject.libs.plugins.spotless.get().pluginId)
    configure<SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            targetExclude("$buildDir/**/*.kt")
            targetExclude("bin/**/*.kt")
            ktlint(libs.versions.ktlint.get())
            licenseHeaderFile(rootProject.file("spotless/copyright.txt"))
        }
        kotlinGradle {
            target("**/*.kts")
            targetExclude("$buildDir/**/*.kts")
            ktlint(libs.versions.ktlint.get())
            licenseHeaderFile(rootProject.file("spotless/copyright.txt"), "(^(?![\\/ ]\\*).*$)")
        }
    }

    // Configure Java to use our chosen language level. Kotlin will automatically
    // pick this up
    plugins.withType<JavaBasePlugin>().configureEach {
        extensions.configure<JavaPluginExtension> {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(11))
            }
        }
    }

    // Workaround for https://issuetracker.google.com/issues/268961156
    tasks.withType<com.android.build.gradle.internal.lint.AndroidLintTask>() {
        val kspTestTask = tasks.findByName("kspTestKotlin")
        if (kspTestTask != null) {
            dependsOn(kspTestTask)
        }
    }
    tasks.withType<com.android.build.gradle.internal.lint.AndroidLintAnalysisTask>() {
        val kspTestTask = tasks.findByName("kspTestKotlin")
        if (kspTestTask != null) {
            dependsOn(kspTestTask)
        }
    }

    tasks.withType<KotlinCompilationTask<*>>().configureEach {
        compilerOptions {
            // Treat all Kotlin warnings as errors
            allWarningsAsErrors.set(true)

            // Enable experimental coroutines APIs, including Flow
            freeCompilerArgs.addAll(
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
            )

            if (project.hasProperty("tivi.enableComposeCompilerReports")) {
                freeCompilerArgs.addAll(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
                            project.buildDir.absolutePath + "/compose_metrics",
                )
                freeCompilerArgs.addAll(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
                            project.buildDir.absolutePath + "/compose_metrics",
                )
            }
        }
    }

    // Configure kapt
    pluginManager.withPlugin(rootProject.libs.plugins.kotlin.kapt.get().pluginId) {
        extensions.getByType<KaptExtension>().correctErrorTypes = true
    }

    // Configure Android projects
    pluginManager.withPlugin("com.android.application") {
        configureAndroidProject()
    }
    pluginManager.withPlugin("com.android.library") {
        configureAndroidProject()
    }
    pluginManager.withPlugin("com.android.test") {
        configureAndroidProject()
    }
}



fun Project.configureAndroidProject() {
    extensions.configure<BaseExtension> {
        compileSdkVersion(libs.versions.compileSdk.get().toInt())

        defaultConfig {
            minSdk = libs.versions.minSdk.get().toInt()
            targetSdk = libs.versions.targetSdk.get().toInt()
        }

        // Can remove this once https://issuetracker.google.com/issues/260059413 is fixed.
        // See https://kotlinlang.org/docs/gradle-configure-project.html#gradle-java-toolchains-support
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11

            // https://developer.android.com/studio/write/java8-support
            isCoreLibraryDesugaringEnabled = true
        }
    }

    dependencies {
        // https://developer.android.com/studio/write/java8-support
        "coreLibraryDesugaring"(libs.tools.desugarjdklibs)
    }
}


