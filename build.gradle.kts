import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.2.10"
    application
}

group = "me.cdh"
version = "1.0"

repositories {
    mavenCentral()
}

application {
    mainClass = "me.cdh.MainKt"
    mainModule = "me.cdh"
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.10.2")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

tasks.register<Copy>("copyResourcesToClass") {
    from(layout.buildDirectory.dir("resources"))
    into(layout.buildDirectory.dir("classes/kotlin"))
}
tasks.register<Copy>("copyDependencies") {
    from(configurations.runtimeClasspath)
    into(layout.buildDirectory.dir("libs/lib"))
}
tasks.build{
    mustRunAfter("copyDependencies")
    finalizedBy("copyResourcesToClass")
}

tasks.jar {
    dependsOn("copyDependencies")
    manifest {
        attributes(
            "Main-Class" to "me.cdh.MainKt",
            "Class-Path" to configurations.runtimeClasspath.get().joinToString(" ") { "lib/" + it.name }
        )
    }
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        destinationDirectory.set(layout.buildDirectory.dir("classes/kotlin/main"))
    }
}

tasks.withType<JavaCompile> {
    destinationDirectory.set(layout.buildDirectory.dir("classes/kotlin/main"))
}

tasks.named("compileJava", JavaCompile::class.java) {
    options.compilerArgumentProviders.add(CommandLineArgumentProvider {
        listOf("--patch-module", "me.cdh=${sourceSets["main"].output.asPath}")
    })
}