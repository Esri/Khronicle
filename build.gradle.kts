import java.util.Properties
import java.io.FileInputStream
import java.io.IOException

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.jetbrains.kotlin.android)
  alias(libs.plugins.jreleaser)
  `maven-publish`
}

// Load file "keystore.properties" where we keep our keys
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()

try {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
} catch (ignored: IOException) {
    if (project.hasProperty("centralUsername")) keystoreProperties["centralUsername"] = property("centralUsername")
    if (project.hasProperty("centralPassword")) keystoreProperties["centralPassword"] = property("centralPassword")
}

android {
  namespace = "com.esri.logger.khronicle"
  compileSdk = 35

  defaultConfig {
    minSdk = 24
    group = "com.esri.logger"

    // The version must be of the form "X.Y.Z[-b][-SNAPSHOT]"
    version =
      if (project.hasProperty("VERSION")) project.property("VERSION").toString()
      else "0.0.1-SNAPSHOT"

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

  publishing {
    singleVariant("release") {}
  }
}

tasks.withType<Test> {
  testLogging {
    events("passed", "skipped", "failed", "standardOut", "standardError")
    exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    showCauses = true
    showExceptions = true
    showStackTraces = true
  }
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

if (keystoreProperties.containsKey("centralUsername") && keystoreProperties.containsKey("centralPassword")) {
    afterEvaluate {
        publishing {
            publications {
                create<MavenPublication>("release") {
                    from(components["release"])

                    pom {
                        name = "Khronicle"
                        packaging = "aar"
                        description = "An SLF4J backend for Android."
                        url = "https://github.com/esri/khronicle"

                        scm {
                        connection = "scm:git:https://github.com/esri/khronicle"
                        developerConnection = "scm:git:https://github.com/esri/khronicle"
                        url = "https://github.com/esri/khronicle"
                        }

                        licenses {
                            license {
                                name = "Apache License 2.0"
                                url = "https://spdx.org/licenses/Apache-2.0.html"
                            }
                        }

                        developers {
                            developer {
                                id = "award"
                                name = "Adam Ward"
                                email = "award@esri.com"
                            }
                            developer {
                                id = "jonasvautherin"
                                name = "Jonas Vautherin"
                                email = "jvautherin@esri.com"
                            }
                        }
                    }
                }
            }
            repositories {
                maven {
                   url = uri(layout.buildDirectory.dir("target/staging-deploy"))
                }
            }
        }
    }

    jreleaser {
        signing {
            setActive("ALWAYS")
            armored.set(true)
            setMode("COMMAND")

            command {
                keyName.set("C8B1511EF991537875A649517D5F7A7C9542C299")
            }
        }
        deploy {
            release {
                github {
                    skipRelease = true
                    skipTag = true
                }
            }
            maven {
                mavenCentral {
                    create("sonatype") {
                        verifyPom = false
                        setActive("RELEASE")
                        username = keystoreProperties["centralUsername"] as String
                        password = keystoreProperties["centralPassword"] as String
                        url = "https://central.sonatype.com/api/v1/publisher"
                        stagingRepository("build/target/staging-deploy")
                    }
                }
                nexus2 {
                    create("snapshot-deploy") {
                        verifyPom = false
                        setActive("SNAPSHOT")
                        snapshotUrl.set("https://central.sonatype.com/repository/maven-snapshots")
                        url = "https://central.sonatype.com/repository/maven-snapshots"
                        applyMavenCentralRules = true
                        snapshotSupported = true
                        username = keystoreProperties["centralUsername"] as String
                        password = keystoreProperties["centralPassword"] as String
                        stagingRepository("build/target/staging-deploy")
                    }
                }
            }
        }
    }
}

