plugins {
    java
    id("com.diffplug.spotless").version("6.22.0")
}

repositories { mavenCentral() }

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // https://mvnrepository.com/artifact/com.mysql/mysql-connector-j
    implementation("com.mysql:mysql-connector-j:8.2.0")
}

spotless {
    java { googleJavaFormat("1.17.0").reorderImports(true).aosp() }
    kotlinGradle { ktfmt().kotlinlangStyle() }
}

tasks.named<Test>("test") { useJUnitPlatform() }
