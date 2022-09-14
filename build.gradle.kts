allprojects {
    group = "de.brueggenthies.leinwand"
}

tasks.create<Delete>("delete") {
    delete(rootProject.buildDir)
}