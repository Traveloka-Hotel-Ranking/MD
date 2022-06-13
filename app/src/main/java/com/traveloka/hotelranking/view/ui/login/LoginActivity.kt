package com.traveloka.hotelranking.view.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.NavUtils
import androidx.core.widget.addTextChangedListener
import com.traveloka.hotelranking.R
import com.traveloka.hotelranking.data.Resource
import com.traveloka.hotelranking.databinding.ActivityLoginBinding
import com.traveloka.hotelranking.model.UserModel
import com.traveloka.hotelranking.view.ui.home.HomeActivity
import com.traveloka.hotelranking.view.ui.main.MainActivity
import com.traveloka.hotelranking.view.ui.register.RegisterActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModel()
    private lateinit var userModel: UserModel

    private var email: String? = null
    private var phone: String? = null
    private var favCountry = "favCountry"
    private var favFood = "favFood"
    private var favMovie = "favMovie"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupActionBar()
        playAnimation()
        setupAction()
    }

    private fun setupAction() {
        binding.emailPhone.addTextChangedListener {
            binding.emailPhoneLogin.error = null
        }
        binding.password.addTextChangedListener {
            binding.passLogin.error = null
        }

        binding.mbLogin.setOnClickListener {
            val emailPhone = binding.emailPhone.text.toString().trim()
            val password = binding.password.text.toString().trim()

            when {
                Patterns.EMAIL_ADDRESS.matcher(emailPhone).matches() -> {
                    email = emailPhone
                    phone = null
                }
                else -> {
                    phone = emailPhone
                    email = null
                }
            }

            loginViewModel.loginUser(email, phone, password)
                .observe(this@LoginActivity) { result ->
                    if (result is Resource.Loading) {
                        showLoading(true)
                        binding.emailPhoneLogin.error = null
                        binding.passLogin.error = null

                    } else if (result is Resource.Error) {
                        showLoading(false)
                        when (val mMessage = result.message.toString()) {
                            "User Not Found." -> {
                                binding.emailPhoneLogin.error = mMessage
                            }
                            "Invalid Password!" -> {
                                binding.passLogin.error = mMessage
                            }
                        }
                        email = null
                        phone = null
                    } else if (result is Resource.Success) {
                        showLoading(false)
                        binding.emailPhoneLogin.error = null
                        binding.passLogin.error = null

                        favCountry = result.data?.favCountry.toString()
                        favFood = result.data?.favFood.toString()
                        favMovie = result.data?.favMovie.toString()

                        if (result.data != null) {
                            loginViewModel.login()
                            val name = result.data.name
                            val dataEmail = result.data.email
                            val dataPhone = result.data.phone
                            val accessToken = result.data.accessToken
                            loginViewModel.saveUser(
                                UserModel(
                                    name,
                                    dataEmail,
                                    dataPhone,
                                    favCountry,
                                    favFood,
                                    favMovie,
                                    accessToken,
                                    true
                                )
                            )
                            startActivity(
                                Intent(this@LoginActivity, HomeActivity::class.java),
                                ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity)
                                    .toBundle()
                            )
                        }
                        email = null
                        phone = null
                    }
                }
        }

        binding.forgetPass.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgetPasswordActivity::class.java)
            startActivity(
                intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity).toBundle()
            )
        }

        binding.registerNow.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(
                intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity).toBundle()
            )
        }
    }

    private fun setupActionBar() {
        supportActionBar?.elevation = 0F
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.title = getString(R.string.login)
    }

    private fun setupViewModel() {
        loginViewModel.getUser().observe(this) { user ->
            this.userModel = user
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
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
        val email = ObjectAnimator.ofFloat(binding.emailPhoneLogin, View.ALPHA, 1f)
            .setDuration(ANIMATION_DURATION.toLong())
        val password = ObjectAnimator.ofFloat(binding.passLogin, View.ALPHA, 1f)
            .setDuration(ANIMATION_DURATION.toLong())
        val btnLogin = ObjectAnimator.ofFloat(binding.mbLogin, View.ALPHA, 1f)
            .setDuration(ANIMATION_DURATION.toLong())
        val forgetPass = ObjectAnimator.ofFloat(binding.forgetPass, View.ALPHA, 1f)
            .setDuration(ANIMATION_DURATION.toLong())
        val travAcc = ObjectAnimator.ofFloat(binding.travAcc, View.ALPHA, 1f)
            .setDuration(ANIMATION_DURATION.toLong())
        val registerNow = ObjectAnimator.ofFloat(binding.registerNow, View.ALPHA, 1f)
            .setDuration(ANIMATION_DURATION.toLong())

        val together = AnimatorSet().apply {
            playTogether(travAcc, registerNow)
        }

        AnimatorSet().apply {
            playSequentially(email, password, btnLogin, forgetPass, together)
            start()
        }
    }

    companion object {
        private const val ANIMATION_DURATION = 200
    }
}