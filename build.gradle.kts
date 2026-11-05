plugins {
    id("java")
    id("idea")
    id("com.gradleup.shadow") version "9.2.2"
}

group = providers.gradleProperty("project.build.group").get()
version = providers.gradleProperty("project.build.version").get()

java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))

repositories {
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
        content {
            includeModule("io.papermc.paper", "paper-api")
            includeGroup("net.md-5")
            includeGroup("net.kyori")
        }
    }

    maven {
        name = "minecraft"
        url = uri("https://libraries.minecraft.net")
        content {
            includeModule("com.mojang", "brigadier")
        }
    }

    mavenCentral()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.9-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")
    compileOnly("org.jetbrains:annotations:26.0.2-1")

    implementation("com.google.code.gson:gson:2.15.0")

    testImplementation("io.papermc.paper:paper-api:1.21.9-R0.1-SNAPSHOT")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks.processResources {
    inputs.properties(
        "pluginName" to rootProject.name,
        "pluginVersion" to version,
        "pluginPackage" to project.group
    )

    filesMatching("**/plugin.yml") {
        expand(
            "NAME" to rootProject.name,
            "VERSION" to version,
            "PACKAGE" to project.group
        )
    }
}

tasks.shadowJar {
    manifest {
        attributes["Main-Class"] = "${project.group}.${rootProject.name}"
    }

    relocate("com.google", "${project.group}.libs.google")

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    exclude(
        "META-INF/INDEX.LIST",
        "META-INF/*.SF",
        "META-INF/*.DSA",
        "META-INF/*.RSA",
        "**/module-info.class"
    )

    group = BasePlugin.BUILD_GROUP
    archiveClassifier = "shadow"
    archiveFileName = project.name + "-" + project.version + "-shaded.jar"
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}
