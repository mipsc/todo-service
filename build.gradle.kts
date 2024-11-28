import com.google.cloud.tools.jib.api.buildplan.ImageFormat

plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("com.google.cloud.tools.jib")
}

group = "org.mzaza"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.mapstruct:mapstruct:${versions["mapstruct"]}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    annotationProcessor("org.mapstruct:mapstruct-processor:${versions["mapstruct"]}")
    annotationProcessor("org.projectlombok:lombok:${versions["lombok"]}")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:${versions["lombokMapstructBinding"]}")

    compileOnly("org.projectlombok:lombok-mapstruct-binding:${versions["lombokMapstructBinding"]}")
    compileOnly("org.projectlombok:lombok:${versions["lombok"]}")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-core:${versions["mockito"]}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${versions["junitJupiter"]}")
    testImplementation("org.testcontainers:testcontainers:${versions["testcontainers"]}")
    testImplementation("org.testcontainers:postgresql:${versions["testcontainers"]}")
    testImplementation("org.testcontainers:junit-jupiter:${versions["testcontainers"]}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

jib {
    from {
        image = "azul/zulu-openjdk:21.0.5"
    }
    to {
        image = "todo-service"
        tags = setOf("latest")
    }
    container {
        format = ImageFormat.OCI
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

sourceSets {
    test {
        java {
            srcDirs("src/test/java")
        }
    }
}




