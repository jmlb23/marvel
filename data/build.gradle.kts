import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.5.20"
    id("com.codingfeline.buildkonfig")
}

group = "com.github.jmlb23"
version = "1.0"

kotlin {
    ios {
        binaries {
            framework {
                baseName = "Data"
            }
        }
    }

    jvm()


    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":domain"))
                implementation(kotlin("stdlib"))
                implementation("io.ktor:ktor-client-cio:1.6.0")
                implementation("io.ktor:ktor-client-serialization:1.6.0")
                implementation("io.insert-koin:koin-core:3.1.2")
                implementation("io.github.reactivecircus.cache4k:cache4k:0.3.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1-native-mt") {
                    version {
                        strictly("1.5.1-native-mt")
                    }
                }
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.2.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test:1.5.21")
                implementation("org.jetbrains.kotlin:kotlin-test-common:1.5.21")
                implementation("org.jetbrains.kotlin:kotlin-test-annotations-common:1.5.21")
                implementation("io.insert-koin:koin-test:3.1.2")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-ios:1.6.0")
            }
        }
        val jvmMain by getting

    }

}

buildkonfig {
    packageName = "com.github.jmlb23.marvel"

    defaultConfigs {
        buildConfigField(
            com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            "URL_BASE",
            "https://gateway.marvel.com:443/v1/public"
        )
        buildConfigField(
            com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            "API_KEY",
            project.findProperty("API_KEY").toString()
        )
        buildConfigField(
            com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            "API_KEY_PRIVATE",
            project.findProperty("API_KEY_PRIVATE").toString()
        )
    }

    defaultConfigs("dev") {
        buildConfigField(
            com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            "URL_BASE",
            "https://gateway.marvel.com:443/v1/public"
        )
        buildConfigField(
            com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            "API_KEY",
            project.findProperty("API_KEY").toString()
        )
        buildConfigField(
            com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            "API_KEY_PRIVATE",
            project.findProperty("API_KEY_PRIVATE").toString()
        )
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
