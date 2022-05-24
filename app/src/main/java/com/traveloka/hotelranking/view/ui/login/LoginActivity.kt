package com.traveloka.hotelranking.view.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.traveloka.hotelranking.R
import com.traveloka.hotelranking.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.elevation = 0F //remove shadow action bar
        this.title = getString(R.string.login) //title
    }
}