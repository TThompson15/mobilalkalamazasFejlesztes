plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.gazorajelento"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.gazorajelento"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

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
    implementation("com.google.firebase:firebase-auth:XX.X.X")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation("com.google.firebase:firebase-auth")

    implementation("com.google.android.gms:play-services-auth:19.0.0")

    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("androidx.recyclerview:recyclerview-selection:1.1.0")
    implementation("com.github.bumptech.glide:glide:3.7.0")
    implementation("androidx.cardview:cardview:1.0.0")

    implementation("com.google.firebase:firebase-firestore:21.4.3")
    implementation("com.android.support:multidex:1.0.3")

}