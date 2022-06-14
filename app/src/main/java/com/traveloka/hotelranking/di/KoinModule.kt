package com.traveloka.hotelranking.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.traveloka.hotelranking.BuildConfig
import com.traveloka.hotelranking.data.HotelRepository
import com.traveloka.hotelranking.data.remote.network.ApiService
import com.traveloka.hotelranking.view.ui.login.ForgetPasswordViewModel
import com.traveloka.hotelranking.view.ui.register.RegisterViewModel
import com.traveloka.hotelranking.model.UserPreference
import com.traveloka.hotelranking.view.ui.home.HomeViewModel
import com.traveloka.hotelranking.view.ui.login.LoginViewModel
import com.traveloka.hotelranking.view.ui.maps.MapsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single {
        val loggingInterceptor =
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ChuckerInterceptor.Builder(get()).build())
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://34.128.85.45:8080/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val viewModelModule = module {
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { ForgetPasswordViewModel(get()) }
    viewModel {HomeViewModel(get(), get())}
    viewModel {MapsViewModel(get(), get())}
}

val repositoryModule = module {
    single { HotelRepository(get()) }
}

val dataPreferenceModule = module {
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(androidContext(),"settings")),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { androidContext().preferencesDataStoreFile("settings") }
        )
    }
    single { UserPreference(get()) }
}