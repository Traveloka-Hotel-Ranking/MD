package com.traveloka.hotelranking.view.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.traveloka.hotelranking.R
import com.traveloka.hotelranking.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.elevation = 0F //remove shadow action bar
        this.title = getString(R.string.accommodations) //title
    }
}