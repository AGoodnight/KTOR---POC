plugins {
    kotlin("jvm") version "1.8.20"
    application
    id("io.ktor.plugin") version "2.3.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.ktor:ktor-client-logging:2.3.0")
    implementation("io.ktor:ktor-server-core:2.3.0")
    implementation("io.ktor:ktor-server-netty:2.3.0")
    implementation("io.ktor:ktor-serialization-jackson:2.3.0")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.0")
    implementation("io.ktor:ktor-server-cors:2.3.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.9.8")
    implementation("aws.sdk.kotlin:s3:0.18.0-beta")
    implementation("aws.sdk.kotlin:dynamodb:0.18.0-beta")
    implementation("aws.sdk.kotlin:iam:0.18.0-beta")
    implementation("aws.sdk.kotlin:cloudwatch:0.18.0-beta")
    implementation("aws.sdk.kotlin:cognitoidentityprovider:0.18.0-beta")
    implementation("aws.sdk.kotlin:sns:0.18.0-beta")
    implementation("aws.sdk.kotlin:pinpoint:0.18.0-beta")
    implementation("org.slf4j:slf4j-api:2.0.7")
    implementation("org.slf4j:slf4j-simple:2.0.7")
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
    implementation("com.github.f4b6a3:tsid-creator:5.2.4")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("MainKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=true")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}