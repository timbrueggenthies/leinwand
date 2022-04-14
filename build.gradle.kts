allprojects {
    version = "0.1.0-alpha01"
    group = "de.brueggenthies.leinwand"
}

tasks.create<Delete>("delete") {
    delete(rootProject.buildDir)
}