subprojects {
    group = "de.brueggenthies.leinwand"

    val ktlintConfig by configurations.creating

    dependencies {
        ktlintConfig(rootProject.libs.ktlint)
        ktlintConfig(rootProject.libs.ktlint.composeRules)
    }

    val outputDir = "${project.buildDir}/reports/ktlint/"
    val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))

    tasks.create<JavaExec>("ktlintCheck") {
        inputs.files(inputFiles)
        outputs.dir(outputDir)
        group = "verification"
        description = "Check Kotlin code style."
        classpath = ktlintConfig
        mainClass.set("com.pinterest.ktlint.Main")
        args = listOf("src/**/*.kt")
    }

    tasks.create<JavaExec>("ktlintFormat") {
        inputs.files(inputFiles)
        outputs.dir(outputDir)
        group = "formatting"
        description = "Fix Kotlin code style deviations."
        classpath = ktlintConfig
        mainClass.set("com.pinterest.ktlint.Main")
        args = listOf("-F", "src/**/*.kt")
    }
}

tasks.create<Delete>("delete") {
    delete(rootProject.buildDir)
}