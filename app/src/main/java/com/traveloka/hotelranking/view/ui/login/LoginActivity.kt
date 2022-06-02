package com.traveloka.hotelranking.view.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.NavUtils
import androidx.core.widget.addTextChangedListener
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.traveloka.hotelranking.R
import com.traveloka.hotelranking.databinding.ActivityLoginBinding
import com.traveloka.hotelranking.model.LoginViewModel
import com.traveloka.hotelranking.model.UserModel
import com.traveloka.hotelranking.model.UserPreference
import com.traveloka.hotelranking.model.ViewModelFactory
import com.traveloka.hotelranking.view.ui.home.HomeActivity
import com.traveloka.hotelranking.view.ui.main.MainActivity
import com.traveloka.hotelranking.view.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userModel: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel(applicationContext)
        setupActionBar()
        playAnimation()
        setupAction()
    }

    private fun setupAction() {
        binding.email.addTextChangedListener {
            binding.emailLogin.error = null
        }
        binding.password.addTextChangedListener {
            binding.passLogin.error = null
        }

        binding.mbLogin.setOnClickListener {
            loginViewModel.isLoading.observe(this) {
                showLoading(it)
            }

            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            loginViewModel.loginUser(email, password)

            loginViewModel.messageSuccessResponse.observe(this) { status ->
                status?.let {
                    Toast.makeText(this@LoginActivity,
                        loginViewModel.messageSuccessResponse.value,
                        Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java),
                        ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity).toBundle())
                    loginViewModel.messageSuccessResponse.value = null
                }
            }

            loginViewModel.messageResponse.observe(this) { status ->
                status?.let {
                    when (loginViewModel.messageResponse.value) {
                        "User Not Found." -> {
                            binding.emailLogin.error = loginViewModel.messageResponse.value
                        }
                        "Invalid Password!" -> {
                            binding.passLogin.error = loginViewModel.messageResponse.value
                        }
                        else -> {
                            binding.emailLogin.error = null
                            binding.passLogin.error = null
                        }
                    }
                    loginViewModel.messageResponse.value = null
                }
            }

        }

        binding.registerNow.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity).toBundle())
        }
    }

    private fun setupActionBar() {
        supportActionBar?.elevation = 0F
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.title = getString(R.string.login)
    }

    private fun setupViewModel(context: Context) {
        loginViewModel = ViewModelProvider(this,
        ViewModelFactory(UserPreference.getInstance(dataStore), context)
        )[LoginViewModel::class.java]

        loginViewModel.getUser().observe(this) { user ->
            this.userModel = user
        }
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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