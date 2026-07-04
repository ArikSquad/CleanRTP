plugins {
    java
    id("com.gradleup.shadow") version "9.3.0"
}

group = "eu.mikart.cleanrtp"
version = "3.6.13"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.1.2.build.70-stable")
    compileOnly("net.milkbowl.vault:VaultAPI:1.7")
    compileOnly("net.dmulloy2:ProtocolLib:5.4.0")
    compileOnly("net.essentialsx:EssentialsX:2.20.1") {
        isTransitive = false
    }
    compileOnly("io.github.miniplaceholders:miniplaceholders-api:3.2.0")
    compileOnly("com.github.angeschossen:LandsAPI:6.28.11") {
        isTransitive = false
    }
    compileOnly("net.william278.huskclaims:huskclaims-bukkit:1.5.2") {
        isTransitive = false
    }
    compileOnly("net.william278.husktowns:husktowns-bukkit:3.0") {
        isTransitive = false
    }

    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")

    implementation("xyz.xenondevs:particle:1.8.4")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
    implementation("de.exlll:configlib-yaml:4.8.1")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(25)
    }

    processResources {
        filteringCharset = "UTF-8"
        filesMatching("paper-plugin.yml") {
            expand("version" to project.version)
        }
    }

    shadowJar {
        archiveClassifier.set("")
        relocate("com.tcoded.folialib", "me.superronancraft.betterrtp.lib.folialib")
        relocate("xyz.xenondevs.particle", "me.superronancraft.betterrtp.lib.particle")
        relocate("org.json.simple", "me.superronancraft.betterrtp.lib.jsonsimple")
        relocate("de.exlll.configlib", "eu.mikart.cleanrtp.lib.configlib")
    }

    build {
        dependsOn(shadowJar)
    }
}
