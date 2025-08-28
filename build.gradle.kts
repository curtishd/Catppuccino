import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.2.0"
}

group = "me.cdh"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

tasks.register<Copy>("copyDependencies") {
    into(layout.buildDirectory.dir("libs"))
    from(configurations.runtimeClasspath)
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        destinationDirectory.set(file("$buildDir/classes/kotlin/main"))
    }
}

tasks.withType<JavaCompile> {
    destinationDirectory.set(file("$buildDir/classes/kotlin/main"))
}

tasks.named("compileJava", JavaCompile::class.java) {
    options.compilerArgumentProviders.add(CommandLineArgumentProvider {
        listOf("--patch-module", "me.cdh=${sourceSets["main"].output.asPath}")
    })
}