pluginManagement {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        mavenCentral()
    }

}
rootProject.name = "marvel"


include(":android")
include(":domain")
include(":data")
include("presentation")
