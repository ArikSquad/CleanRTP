pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/") {
            name = "papermc"
        }
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/") {
            name = "placeholderapi"
        }
        maven("https://repo.essentialsx.net/releases/") {
            name = "essentialsx"
        }
        maven("https://nexus.hc.to/content/repositories/pub_releases") {
            name = "vault"
        }
        maven("https://repo.dmulloy2.net/nexus/repository/public/") {
            name = "dmulloy2"
        }
        maven("https://repo.william278.net/releases") {
            name = "william278"
        }
        maven("https://jitpack.io") {
            name = "jitpack"
        }
    }
}

rootProject.name = "cleanrtp"
