plugins {
    alias(libs.plugins.android.application)
}


android {
    signingConfigs {
        getByName("debug") {
            storeFile = file("/Users/jeffrexxx/AndroidStudioProjects/login/my-release-key.keystore")
            storePassword = "123456"
            keyAlias = "my-key-alias"
            keyPassword = "123456"
        }
        create("release") {
        }
    }
    namespace = "com.example.login"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.login"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.google.android.material:material:1.4.0")
    // 添加 Facebook Rebound 依赖
    implementation("com.facebook.rebound:rebound:0.3.8")
    //权限申请
    api ("com.yanzhenjie:permission:2.0.0-rc11")
    implementation("com.github.bumptech.glide:glide:4.14.2")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    api ("com.tencent.mm.opensdk:wechat-sdk-android:+")
}
