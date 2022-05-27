package com.traveloka.hotelranking.view.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.NavUtils
import com.traveloka.hotelranking.R
import com.traveloka.hotelranking.databinding.ActivityRegisterBinding
import com.traveloka.hotelranking.view.ui.login.LoginActivity
import com.traveloka.hotelranking.view.ui.main.MainActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        playAnimation()
        setupAction()
    }

    private fun setupAction() {
        binding.loginNow.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this@RegisterActivity).toBundle())
        }
    }

    private fun setupActionBar() {
        supportActionBar?.elevation = 0F
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //add back button on Action Bar
        this.title = getString(R.string.register)
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
        val nameRegister = ObjectAnimator.ofFloat(binding.nameRegist, View.ALPHA, 1f).setDuration(300)
        val emailRegister = ObjectAnimator.ofFloat(binding.emailRegist, View.ALPHA, 1f).setDuration(300)
        val numberRegister = ObjectAnimator.ofFloat(binding.numberRegist, View.ALPHA, 1f).setDuration(300)
        val passwordReg = ObjectAnimator.ofFloat(binding.passRegist, View.ALPHA, 1f).setDuration(300)
        val confirmmPasswordReg = ObjectAnimator.ofFloat(binding.passConfirmRegist, View.ALPHA, 1f).setDuration(300)
        val btnRegister = ObjectAnimator.ofFloat(binding.mbRegister, View.ALPHA, 1f).setDuration(300)
        val btnRegisterGoogle = ObjectAnimator.ofFloat(binding.mbRegisterGoogle, View.ALPHA, 1f).setDuration(300)
        val travAcc = ObjectAnimator.ofFloat(binding.travAcc, View.ALPHA, 1f).setDuration(300)
        val loginNow = ObjectAnimator.ofFloat(binding.loginNow, View.ALPHA, 1f).setDuration(300)

        val together = AnimatorSet().apply {
            playTogether(travAcc, loginNow)
        }

        AnimatorSet().apply {
            playSequentially(nameRegister, emailRegister, numberRegister, passwordReg, confirmmPasswordReg,
                btnRegister, btnRegisterGoogle, together)
            start()
        }
    }
}