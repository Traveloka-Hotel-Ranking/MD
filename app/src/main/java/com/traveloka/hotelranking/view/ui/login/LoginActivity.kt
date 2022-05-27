package com.traveloka.hotelranking.view.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.NavUtils
import com.traveloka.hotelranking.R
import com.traveloka.hotelranking.databinding.ActivityLoginBinding
import com.traveloka.hotelranking.view.ui.main.MainActivity
import com.traveloka.hotelranking.view.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        playAnimation()
        setupAction()
    }

    private fun setupAction() {
        binding.registerNow.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity).toBundle())
        }
    }

    private fun setupActionBar(){
        supportActionBar?.elevation = 0F
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //add back button on Action Bar
        this.title = getString(R.string.login)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home ->{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                super.onOptionsItemSelected(item)
            }
            else -> true
        }
    }

    override fun onBackPressed() {
        NavUtils.navigateUpFromSameTask(this)
    }

    private fun playAnimation() {
        val email = ObjectAnimator.ofFloat(binding.emailLogin, View.ALPHA, 1f).setDuration(300)
        val password = ObjectAnimator.ofFloat(binding.passLogin, View.ALPHA, 1f).setDuration(300)
        val btnLogin = ObjectAnimator.ofFloat(binding.mbLogin, View.ALPHA, 1f).setDuration(300)
        val forgetPass = ObjectAnimator.ofFloat(binding.forgetPass, View.ALPHA, 1f).setDuration(300)
        val btnLoginGoogle = ObjectAnimator.ofFloat(binding.mbLoginGoogle, View.ALPHA, 1f).setDuration(300)
        val travAcc = ObjectAnimator.ofFloat(binding.travAcc, View.ALPHA, 1f).setDuration(300)
        val registerNow = ObjectAnimator.ofFloat(binding.registerNow, View.ALPHA, 1f).setDuration(300)

        val together = AnimatorSet().apply {
            playTogether(travAcc, registerNow)
        }

        AnimatorSet().apply {
            playSequentially(email, password, btnLogin, forgetPass, btnLoginGoogle, together)
            start()
        }
    }

}