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
    private val coordinator by lazy { "androidx.coordinatorlayout:coordinatorlayout:${Versions.coordinator}" }
    val daggerDep by lazy { "com.google.dagger:dagger:${Versions.daggerVersion}" }
    val glideKapt by lazy { "com.github.bumptech.glide:compiler:4.12.0" }
    val daggerKapt by lazy { "com.google.dagger:dagger-compiler:${Versions.daggerVersion}" }
    private val circleImageView by lazy { "de.hdodenhof:circleimageview:${Versions.circleImageView}" }
    private val gson by lazy { "com.google.code.gson:gson:${Versions.gson}" }
    private val okhttp by lazy { "com.squareup.okhttp3:okhttp:${Versions.okhttp}" }
    private val retrofit by lazy { "com.squareup.retrofit2:retrofit:${Versions.retrofit}" }
    private val eventBus by lazy { "org.greenrobot:eventbus:${Versions.eventBus}" }
    private val rxJava3Adapter by lazy { "com.github.akarnokd:rxjava3-retrofit-adapter:${Versions.rxJava3Adapter}" }
    private val rxJava3 by lazy { "io.reactivex.rxjava3:rxandroid:${Versions.rxJava3}" }
    private val gsonConverter by lazy { "com.squareup.retrofit2:converter-gson:${Versions.retrofit}" }
    private val security by lazy { "androidx.security:security-crypto:${Versions.security}" }
    private val rxKotlin by lazy { "io.reactivex.rxjava3:rxkotlin:${Versions.rxKotlin}" }

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
        add(eventBus)
        add(rxJava3)
        add(security)
        add(rxKotlin)
        add(retrofit)
    }

    val network = arrayListOf<String>().apply {
        add(gson)
        add(okhttp)
        add(rxJava3Adapter)
        add(gsonConverter)
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