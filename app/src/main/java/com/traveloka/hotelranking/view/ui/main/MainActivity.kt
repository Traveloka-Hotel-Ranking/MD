package com.traveloka.hotelranking.view.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.traveloka.hotelranking.databinding.ActivityMainBinding
import com.traveloka.hotelranking.view.ui.home.HomeActivity
import com.traveloka.hotelranking.view.ui.login.LoginActivity
import com.traveloka.hotelranking.view.ui.login.LoginViewModel
import com.traveloka.hotelranking.view.ui.register.RegisterActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupActionBar()
        setupAction()
    }

    private fun setupViewModel() {
        loginViewModel.getUser().observe(this) { pref ->
            if (pref.checkLogin) {
                startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                finish()
            }
        }
    }

    private fun setupActionBar() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(
                intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity).toBundle()
            )
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(
                intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity).toBundle()
            )
        }
    }
}