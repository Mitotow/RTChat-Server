plugins {
    kotlin("jvm") version "1.9.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("org.mariadb.jdbc:mariadb-java-client:2.2.0")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("org.slf4j:slf4j-simple:2.1.0-alpha1")
    implementation("io.github.oshai:kotlin-logging-jvm:6.0.3")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}