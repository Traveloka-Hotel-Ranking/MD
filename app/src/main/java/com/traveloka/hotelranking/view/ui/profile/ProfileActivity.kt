package com.traveloka.hotelranking.view.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.NavUtils
import androidx.core.content.ContextCompat
import com.jakewharton.rxbinding2.widget.RxTextView
import com.traveloka.hotelranking.R
import com.traveloka.hotelranking.data.Resource
import com.traveloka.hotelranking.databinding.ActivityProfileBinding
import com.traveloka.hotelranking.databinding.NewPasswordLayoutBinding
import com.traveloka.hotelranking.model.UserModel
import com.traveloka.hotelranking.view.ui.home.HomeActivity
import com.traveloka.hotelranking.view.ui.login.ForgetPasswordViewModel
import com.traveloka.hotelranking.view.ui.login.LoginViewModel
import com.traveloka.hotelranking.view.ui.main.MainActivity
import io.reactivex.Observable
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val loginViewModel: LoginViewModel by viewModel()
    private val forgetPasswordViewModel: ForgetPasswordViewModel by viewModel()
    private lateinit var userModel: UserModel

    private var email = "email"
    private var favCountry: String? = null
    private var favFood: String? = null
    private var favMovie: String? = null
    private var accessTokenReset: String? = null

    private lateinit var binding2: NewPasswordLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        loginViewModel.getUser().observe(this) { user ->
            this.userModel = user

            if (user.checkLogin) {
                email = user.email
                favCountry = user.favCountry
                favFood = user.favFood
                favMovie = user.favMovie

                binding.tvName.text = user.name
                binding.tvEmail.text = email
                binding.tvPhone.text = user.phone
            } else {
                startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
            }
        }
    }

    private fun setupAction() {
        binding.changePassword.setOnClickListener {
            forgetPasswordViewModel.forgetPassword(email, favCountry, favFood, favMovie)
                .observe(this@ProfileActivity)
                { result ->
                    if (result is Resource.Loading) {
                        showLoading(true)
                    } else if (result is Resource.Error) {
                        showLoading(false)
                        Toast.makeText(
                            this@ProfileActivity, result.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (result is Resource.Success) {
                        showLoading(false)
                        if (result.data != null) {
                            val emailReset = result.data.email
                            accessTokenReset = result.data.accessTokenPassword

                            binding2 = NewPasswordLayoutBinding.inflate(layoutInflater)
                            setContentView(binding2.root)

                            inputLayoutValidate()

                            binding2.mbUpdate.setOnClickListener {
                                val newPass = binding2.newPass.text.toString().trim()
                                forgetPasswordViewModel.resetPassword(
                                    accessTokenReset!!,
                                    emailReset,
                                    newPass
                                ).observe(this@ProfileActivity) { result ->
                                    if (result is Resource.Loading) {
                                        showLoadingForgetPassword(true)
                                    } else if (result is Resource.Error) {
                                        showLoadingForgetPassword(false)
                                        Toast.makeText(
                                            this@ProfileActivity, result.message.toString(),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else if (result is Resource.Success) {
                                        showLoadingForgetPassword(false)
                                        if (result.data != null) {
                                            Toast.makeText(
                                                this@ProfileActivity,
                                                result.data.message,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                        accessTokenReset = null
                                        finish()
                                    }
                                }
                            }
                        }
                    }
                }
        }

        binding.mbLogout.setOnClickListener {
            loginViewModel.logout()
        }
    }

    @SuppressLint("CheckResult")
    private fun inputLayoutValidate() {
        binding2.mbUpdate.isEnabled = false
        binding2.mbUpdate.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))

        val passwordStream = RxTextView.textChanges(binding2.newPass)
            .skipInitialValue()
            .map { password ->
                password.length < 8
            }
        passwordStream.subscribe {
            showPasswordMinimalAlert(it)
        }

        val passwordConfirmationStream = Observable.merge(
            RxTextView.textChanges(binding2.newPass)
                .map { password ->
                    password.toString() != binding2.confirmPass.text.toString()
                },
            RxTextView.textChanges(binding2.confirmPass)
                .map { confirmPassword ->
                    confirmPassword.toString() != binding2.newPass.text.toString()
                }
        )
        passwordConfirmationStream.subscribe {
            showPasswordConfirmationAlert(it)
        }

        val invalidFieldsStream = Observable.combineLatest(
            passwordStream,
            passwordConfirmationStream
        ) { passwordInvalid: Boolean, passwordConfirmationInvalid: Boolean ->
            !passwordInvalid && !passwordConfirmationInvalid
        }
        invalidFieldsStream.subscribe { isValid ->
            if (isValid) {
                binding2.mbUpdate.isEnabled = true
                binding2.mbUpdate.setBackgroundColor(ContextCompat.getColor(this, R.color.orange))
            } else {
                binding2.mbUpdate.isEnabled = false
                binding2.mbUpdate.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.light_gray
                    )
                )
            }
        }
    }

    private fun showPasswordMinimalAlert(isNotValid: Boolean) {
        binding2.layoutNewPass.error = if (isNotValid) getString(R.string.password_length) else null
    }

    private fun showPasswordConfirmationAlert(isNotValid: Boolean) {
        binding2.layoutConfirmPass.error =
            if (isNotValid) getString(R.string.password_doesnt_match) else null
    }

    private fun setupActionBar() {
        supportActionBar?.elevation = 0F
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.title = getString(R.string.profile)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(
                    intent,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this@ProfileActivity)
                        .toBundle()
                )
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

    private fun showLoadingForgetPassword(isLoading: Boolean) {
        binding2.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}