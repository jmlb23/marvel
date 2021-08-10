import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
}

group = "com.github.jmlb23"
version = "1.0"

kotlin {

    iosX64("ios") {
        binaries {
            framework {
                baseName = "presentation"
            }
        }
    }

    jvm()


    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":data"))
                api(project(":domain"))
                implementation("org.jetbrains.kotlin:kotlin-test:1.5.21")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1-native-mt")
                implementation("io.insert-koin:koin-core:3.1.2")
            }
        }
        val iosMain by getting {
            kotlin.srcDir("${buildDir.absolutePath}/generated/source/kaptKotlin/")
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("io.insert-koin:koin-test:3.1.2")
            }
        }
    }


}
val packForXcode by tasks.creating(Sync::class) {
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>("ios").binaries.getFramework(mode)
    val targetDir = File(buildDir, "xcode-frameworks")

    group = "build"
    dependsOn(framework.linkTask)
    inputs.property("mode", mode)

    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
