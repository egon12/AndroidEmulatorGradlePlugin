plugins {
    `kotlin-dsl`
    groovy
    `maven-publish`
}

dependencies {
    implementation(gradleApi())
    implementation(localGroovy())
}

repositories {
    mavenCentral()
}

dependencies {
    testCompile("junit:junit:4.12")
}

group = "org.egon12"
version = "0.1"

publishing {
    repositories {
        maven {
            url = uri("$buildDir/repo")
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
