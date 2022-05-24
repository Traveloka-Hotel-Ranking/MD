package com.traveloka.hotelranking.view.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.traveloka.hotelranking.databinding.ActivityDetailHotelBinding

class DetailHotelActivity : AppCompatActivity() {

    private lateinit var binding:ActivityDetailHotelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHotelBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}