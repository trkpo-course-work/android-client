import org.gradle.api.artifacts.dsl.DependencyHandler

/**
 * To define plugins
 */
object BuildPlugins {
    val android by lazy { "com.android.tools.build:gradle:${Versions.gradlePlugin}" }
    val kotlin by lazy { "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}" }
}

/**
 * To define dependencies
 */
object Deps {
    private val timber by lazy { "com.jakewharton.timber:timber:${Versions.timber}" }
    private val appCompat by lazy { "androidx.appcompat:appcompat:${Versions.appCompat}" }
    private val materialDesign by lazy { "com.google.android.material:material:${Versions.material}" }
    private val constraintLayout by lazy { "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}" }
    private val junit by lazy { "junit:junit:${Versions.jUnit}" }
    private val junitExt by lazy { "androidx.test.ext:junit:${Versions.junitExt}" }
    private val espresso by lazy { "androidx.test.espresso:espresso-core:${Versions.espresso}" }
    private val kotlinStdLib by lazy { "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}" }
    private val coreKtx by lazy { "androidx.core:core-ktx:${Versions.androidx}" }
    private val livedataKtx by lazy { "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleKtx}" }
    private val viewModelKtx by lazy { "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleKtx}" }
    private val fragmentKtx by lazy { "androidx.navigation:navigation-fragment-ktx:${Versions.navigationKtx}" }
    private val uiKtx by lazy { "androidx.navigation:navigation-ui-ktx:${Versions.navigationKtx}" }
    private val legacySupport by lazy { "androidx.legacy:legacy-support-v4:1.0.0" }
    private val glide by lazy { "com.github.bumptech.glide:glide:4.12.0" }
    private val coordinator by lazy { "androidx.coordinatorlayout:coordinatorlayout:${Versions.coordinator}"}
    val daggerDep by lazy { "com.google.dagger:dagger:${Versions.daggerVersion}" }
    val glideKapt by lazy { "com.github.bumptech.glide:compiler:4.12.0" }
    val daggerKapt by lazy { "com.google.dagger:dagger-compiler:${Versions.daggerVersion}" }
    private val circleImageView by lazy { "de.hdodenhof:circleimageview:${Versions.circleImageView}" }

    val appLibraries = arrayListOf<String>().apply {
        add(kotlinStdLib)
        add(coreKtx)
        add(appCompat)
        add(constraintLayout)
        add(livedataKtx)
        add(viewModelKtx)
        add(fragmentKtx)
        add(uiKtx)
        add(materialDesign)
        add(timber)
        add(legacySupport)
        add(glide)
        add(circleImageView)
        add(coordinator)
    }

    val androidTestLibraries = arrayListOf<String>().apply {
        add(junitExt)
        add(espresso)
    }

    val testLibraries = arrayListOf<String>().apply {
        add(junit)
    }
}

//util functions for adding the different type dependencies from build.gradle file
fun DependencyHandler.kapt(list: List<String>) {
    list.forEach { dependency ->
        add("kapt", dependency)
    }
}

fun DependencyHandler.implementation(list: List<String>) {
    list.forEach { dependency ->
        add("implementation", dependency)
    }
}

fun DependencyHandler.androidTestImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("androidTestImplementation", dependency)
    }
}

fun DependencyHandler.testImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("testImplementation", dependency)
    }
}