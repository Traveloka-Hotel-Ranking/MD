package com.traveloka.hotelranking.view.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.traveloka.hotelranking.R
import com.traveloka.hotelranking.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.elevation = 0F //remove shadow action bar
        this.title = getString(R.string.register) //title
    }
}