plugins {
    kotlin("jvm") version "2.0.21"
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "mc.fuckoka"
version = "1.0.2"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib")
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("com.zaxxer:HikariCP:6.2.1")
    compileOnly("com.mysql:mysql-connector-j:9.2.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/fuckokamc/DBConnector")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            artifactId = "db-connector"
            from(components["java"])
        }
    }
}
