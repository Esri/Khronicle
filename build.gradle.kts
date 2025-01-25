plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.jetbrains.kotlin.android)
}

android {
  namespace = "com.esri.logger"
  compileSdk = 35

  defaultConfig {
    minSdk = 24
    group = "com.esri"
    version = "0.0.1"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions { jvmTarget = "17" }
  testOptions { unitTests { isIncludeAndroidResources = true } }
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  implementation(libs.slf4j.api)
  implementation(libs.kotlin.reflect)

  testImplementation(libs.junit)
  testImplementation(libs.robolectric)
  testImplementation(libs.io.mockk)

  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
}
