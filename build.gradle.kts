// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
}
buildscript {
    val androidxCoreVersion by extra { "1.6.0" }
    val androidxAppCompatVersion by extra { "1.3.1" }

    repositories {
        google()
        mavenCentral()

    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.0.2")
        // 其他类路径依赖项

    }
}


