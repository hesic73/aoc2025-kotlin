plugins {
    kotlin("jvm") version "2.1.0"
    application
}

application {
    mainClass.set("Day02Kt")
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "9.2.1"
    }
}
