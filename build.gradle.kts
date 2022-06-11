import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.0"
    application
    idea
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo1.maven.org/maven2/")
    }
}

sourceSets.main {
    java.srcDirs("src/core")
}

sourceSets.test {
    java.srcDirs("src/test")
}

application {
    mainClass.set("MainKt")
}

dependencies {
    testImplementation("org.testng:testng:7.4.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "16"
}

tasks.withType<Test> {
    useJUnitPlatform()
    this.testLogging {
        this.showStandardStreams = true
        this.events = setOf(org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED)
        this.exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}