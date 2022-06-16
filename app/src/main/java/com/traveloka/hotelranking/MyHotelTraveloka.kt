package com.traveloka.hotelranking

import android.app.Application
import com.traveloka.hotelranking.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MyHotelTraveloka : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidContext(this@MyHotelTraveloka)
            modules(
                listOf(
                    networkModule,
                    databaseModule,
                    viewModelModule,
                    repositoryModule,
                    dataPreferenceModule,
                )
            )
        }
    }
}