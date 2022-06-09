

object Versions {
    const val timber = "4.7.1"
    const val gson = "2.8.7"
    const val retrofit = "2.9.0"
    const val okHttp = "4.9.1"
    const val koin = "3.2.0-beta-1"
    const val glide = "4.12.0"
    const val coroutine = "1.3.9"
    const val viewModel = "2.4.0"
    const val activityKtx = "1.4.0"
    const val paging = "3.1.0"
    const val swipeRefresh = "1.1.0"
    const val gMap = "18.0.2"
    const val coil = "2.0.0-rc03"
    const val mockito = "3.12.4"
    const val coreTesting = "2.1.0"
    const val room = "2.4.0-rc01"
    const val shimmer_version = "0.5.0"
    const val sdp = "1.0.6"

}

object Libs {

    //Timber log
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    //network
    const val gson = "com.google.code.gson:gson:${Versions.gson}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitGsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val okHttp = "com.squareup.okhttp3:okhttp:${Versions.okHttp}"
    const val okHttpLogging = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttp}"

    //koin DI
    const val koinCore = "io.insert-koin:koin-core:${Versions.koin}"
    const val koinAndroid = "io.insert-koin:koin-android:${Versions.koin}"

    //glide
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"

    //coroutine
    const val coroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutine}"
    const val coroutineCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutine}"

    //view model
    const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.viewModel}"
    const val activityKtx = "androidx.activity:activity-ktx:${Versions.activityKtx}"

    //paging
    const val paging ="androidx.paging:paging-runtime:${Versions.paging}"

    //swipe refresh
    const val swipeRefresh = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefresh}"

    //google maps
    const val gMap = "com.google.android.gms:play-services-maps:${Versions.gMap}"

    //coil
    const val coil = "io.coil-kt:coil:${Versions.coil}"

    //TTD : Testing
    const val mockitoCore = "org.mockito:mockito-core:${Versions.mockito}"
    const val mockitoInLine = "org.mockito:mockito-inline:${Versions.mockito}"

    //special test
    const val coreTesting = "androidx.arch.core:core-testing:${Versions.coreTesting}"

    // room
    const val room = "androidx.room:room-ktx:${Versions.room}"
    const val roomKapt = "androidx.room:room-ktx:${Versions.room}"
    const val pagingRoom = "androidx.room:room-paging:${Versions.room}"


    //shimmer

    const val shimmer = "com.facebook.shimmer:shimmer:${Versions.shimmer_version}"

    //intuit
    const val sdp = "com.intuit.sdp:sdp-android:${Versions.sdp}"


}