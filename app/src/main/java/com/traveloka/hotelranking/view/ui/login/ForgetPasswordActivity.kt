package com.traveloka.hotelranking.view.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.jakewharton.rxbinding2.widget.RxTextView
import com.traveloka.hotelranking.R
import com.traveloka.hotelranking.data.Resource
import com.traveloka.hotelranking.databinding.ActivityForgetPasswordBinding
import com.traveloka.hotelranking.databinding.NewPasswordLayoutBinding
import com.traveloka.hotelranking.model.UserForgetPasswordModel
import io.reactivex.Observable
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgetPasswordBinding
    private lateinit var binding2: NewPasswordLayoutBinding
    private val forgetPasswordViewModel: ForgetPasswordViewModel by viewModel()
    private lateinit var userForgetPasswordModel: UserForgetPasswordModel

    private var favCountry: String? = null
    private var favFood: String? = null
    private var favMovie: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupAction()
    }

    private fun setupAction() {
        binding.email.addTextChangedListener {
            binding.layoutEmail.error = null
        }
        binding.security.addTextChangedListener {
            binding.layoutSecurity.error = null
        }

        binding.mbSend.setOnClickListener {

            val email = binding.email.text.toString().trim()
            val favorite = binding.security.text.toString().trim()
            val spinner = binding.spinnerSecurity.selectedItem.toString()

            when {
                email.isEmpty() -> {
                    binding.layoutEmail.error = getString(R.string.required)
                }
                favorite.isEmpty() -> {
                    binding.layoutSecurity.error = getString(R.string.required)
                }
                spinner == "Select Question :" -> {
                    AlertDialog.Builder(this).apply {
                        setMessage("Please Select the question!")
                        setNegativeButton("Close") { _, _ -> }
                        create()
                        show()
                    }
                }
                else -> {
                    if (spinner == "What's your Favorite Country?") {
                        favCountry = favorite
                    }
                    if (spinner == "What's your Favorite Food?") {
                        favFood = favorite
                    }
                    if (spinner == "What's your Favorite Movie?") {
                        favMovie = favorite
                    }

                    forgetPasswordViewModel.forgetPassword(email, favCountry, favFood, favMovie)
                        .observe(this@ForgetPasswordActivity) { result ->
                            if (result is Resource.Loading) {
                                showLoading(true)
                            } else if (result is Resource.Error) {
                                showLoading(false)
                                Toast.makeText(
                                    this@ForgetPasswordActivity, result.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                                favCountry = null
                                favFood = null
                                favMovie = null
                            } else if (result is Resource.Success) {
                                showLoading(false)
                                if (result.data != null) {
                                    val emailReset = result.data.email
                                    val accessTokenPassword = result.data.accessTokenPassword
                                    forgetPasswordViewModel.saveForgetPassword(
                                        UserForgetPasswordModel(emailReset, accessTokenPassword)
                                    )

                                    binding2 = NewPasswordLayoutBinding.inflate(layoutInflater)
                                    setContentView(binding2.root)

                                    inputLayoutValidate()
                                    forgetPasswordViewModel.getUserForgetPassword()
                                        .observe(this) { userForget ->
                                            this.userForgetPasswordModel = userForget
                                            this.title = userForget.email

                                            binding2.mbUpdate.setOnClickListener {
                                                val emailReset = userForget.email
                                                val tokenReset = userForget.accessTokenReset
                                                val newPass =
                                                    binding2.newPass.text.toString().trim()

                                                forgetPasswordViewModel.resetPassword(
                                                    tokenReset,
                                                    emailReset,
                                                    newPass
                                                ).observe(this@ForgetPasswordActivity) { result ->
                                                    if (result is Resource.Loading) {
                                                        showLoadingForgetPassword(true)
                                                    } else if (result is Resource.Error) {
                                                        showLoadingForgetPassword(false)
                                                        Toast.makeText(
                                                            this@ForgetPasswordActivity,
                                                            result.message.toString(),
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    } else if (result is Resource.Success) {
                                                        showLoadingForgetPassword(false)
                                                        if (result.data != null) {
                                                            AlertDialog.Builder(this@ForgetPasswordActivity)
                                                                .apply {
                                                                    setMessage(result.data.message)
                                                                    setPositiveButton("Login") { _, _ ->
                                                                        finish()
                                                                        startActivity(
                                                                            Intent(
                                                                                this@ForgetPasswordActivity,
                                                                                LoginActivity::class.java
                                                                            ),
                                                                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                                                                this@ForgetPasswordActivity
                                                                            )
                                                                                .toBundle()
                                                                        )
                                                                    }
                                                                    create()
                                                                    show()
                                                                }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                }
                                favCountry = null
                                favFood = null
                                favMovie = null
                            }
                        }
                }
            }
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
        this.title = getString(R.string.forget_pass)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                super.onOptionsItemSelected(item)
            }
            else -> true
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showLoadingForgetPassword(isLoading: Boolean) {
        binding2.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}