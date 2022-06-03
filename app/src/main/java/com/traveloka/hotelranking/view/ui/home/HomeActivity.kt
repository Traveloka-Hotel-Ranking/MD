package com.traveloka.hotelranking.view.ui.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.traveloka.hotelranking.R
import com.traveloka.hotelranking.databinding.ActivityHomeBinding
import com.traveloka.hotelranking.model.LoginViewModel
import com.traveloka.hotelranking.model.UserPreference

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
    }

    private fun setupActionBar() {
        supportActionBar?.elevation = 0F
        this.title = getString(R.string.accommodations)
    }
}