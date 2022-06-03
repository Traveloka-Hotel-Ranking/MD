package com.traveloka.hotelranking

import android.app.Application
import com.traveloka.hotelranking.di.dataPreferenceModule
import com.traveloka.hotelranking.di.networkModule
import com.traveloka.hotelranking.di.repositoryModule
import com.traveloka.hotelranking.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyHotelTraveloka : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyHotelTraveloka)
            modules(
                listOf(
                    networkModule,
                    viewModelModule,
                    repositoryModule,
                    dataPreferenceModule
                )
            )
        }
    }
}