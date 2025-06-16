plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    id ("kotlin-kapt")
}

android {
    namespace = "com.univalle.dogapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.univalle.dogapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    // Animaciones con Lottie (JSON)
    implementation(libs.lottie)

    // Extensiones de Kotlin para funciones comunes de Android
    implementation(libs.androidx.core.ktx)

    // Compatibilidad con versiones antiguas de Android
    implementation(libs.androidx.appcompat)

    // Componentes del diseño Material (botones, inputs, etc.)
    implementation(libs.material)
    // Funcionalidades modernas para Activity
    implementation(libs.androidx.activity)
    // Layout flexible y potente
    implementation(libs.androidx.constraintlayout)

    // Pruebas unitarias
    testImplementation(libs.junit)
    // Pruebas instrumentadas (Android)
    androidTestImplementation(libs.androidx.junit)
    // Pruebas UI con Espresso
    androidTestImplementation(libs.androidx.espresso.core)

    // Huella digital y autenticación biométrica
    implementation(libs.androidx.biometric)

    // Navegación entre fragments y componentes UI
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.common)

    // Vista en forma de tarjeta (CardView)
    implementation(libs.androidx.cardview)

    // Lista eficiente y reutilizable (RecyclerView)
    implementation(libs.androidx.recyclerview)

    // Corrutinas para operaciones asincrónicas en Android
    implementation(libs.kotlinx.coroutines.android)

    // ViewModel para separar lógica de UI
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // Extensiones para Activity en Kotlin
    implementation(libs.androidx.activity.ktx)
    // Extensiones para Fragment en Kotlin
    implementation(libs.androidx.fragment.ktx)

    // LiveData para observar cambios en datos
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // Room (base de datos local)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    // Procesador de anotaciones de Room (con KSP)
    ksp(libs.androidx.room.compiler)

    // Botón de acción flotante personalizado (si no es parte de Material)
    implementation(libs.floatingactionbutton)

    // Cliente HTTP para APIs REST
    implementation(libs.retrofit)
    // Conversor JSON (Gson) para Retrofit
    implementation(libs.converter.gson)

    // Carga y visualización eficiente de imágenes
    implementation(libs.glide)
}
