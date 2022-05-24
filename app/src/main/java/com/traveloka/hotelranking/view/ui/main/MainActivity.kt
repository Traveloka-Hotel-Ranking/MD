package com.traveloka.hotelranking.view.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TableLayout
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.tabs.TabLayout
import com.traveloka.hotelranking.R
import com.traveloka.hotelranking.databinding.ActivityMainBinding
import com.traveloka.hotelranking.view.ui.login.LoginActivity
import com.traveloka.hotelranking.view.ui.main.adapter.DummyData
import com.traveloka.hotelranking.view.ui.main.adapter.OnBoardingAdapter
import com.traveloka.hotelranking.view.ui.register.RegisterActivity

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var adapter: OnBoardingAdapter
    private val dataDummy = DummyData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        actionBar?.hide()
        supportActionBar?.hide()
        adapter = OnBoardingAdapter(this)
        adapter.setBoardingList(dataDummy.listData)

        binding.run {
            vpBoarding.adapter = adapter
            tbBoarding.setupWithViewPager(vpBoarding)

            btnLogin.setOnClickListener {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
            }

            btnRegister.setOnClickListener {
                val intent = Intent(this@MainActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }
}