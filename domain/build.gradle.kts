import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
      kotlin("multiplatform")
}

group = "com.github.jmlb23"
version = "1.0"

repositories {
    mavenCentral()
}

kotlin {
    ios {
        binaries {
            framework {
                baseName = "Domain"
            }
        }
    }

    jvm()


    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }
    }


}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
