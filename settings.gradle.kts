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
    ":ui:signup",
    ":ui:map",
    ":ui:inicio:home",
    ":ui:inicio:discover",
    ":ui:inicio:actividades",
    ":ui:inicio:servicios",
    ":ui:inicio:grupos",

    ":ui:entidad:complejo",
    ":ui:entidad:instalacion",
    ":ui:entidad:reservar",
    ":ui:entidad:salas",
    ":ui:entidad:actividades",

    ":ui:bottom:reserva",
    ":ui:bottom:auth",
    ":ui:bottom:map",
//    ":ui:detail",

    ":ui:grupo:sala",
    ":ui:grupo:chat",
    ":ui:grupo:creategrupo",
    ":ui:grupo:createsala",
    ":ui:grupo:detail",

    ":ui:account:profile",
    ":ui:account:reservas",

    ":ui:settings",
    ":ui:inbox",
    ":ui:favoritos",

    ":app",
    ":common:resources",
    ":common:compose",
    ":common:view",
    ":domain",
    ":data:db",
    ":data:db-room",
    ":data:api",
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


    ":tasks:android",
    ":tasks:api",
    )