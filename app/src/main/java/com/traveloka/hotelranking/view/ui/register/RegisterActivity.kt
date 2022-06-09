package com.traveloka.hotelranking.view.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.NavUtils
import androidx.core.content.ContextCompat
import com.jakewharton.rxbinding2.widget.RxTextView
import com.traveloka.hotelranking.R
import com.traveloka.hotelranking.data.Resource
import com.traveloka.hotelranking.databinding.ActivityRegisterBinding
import com.traveloka.hotelranking.view.ui.login.LoginActivity
import com.traveloka.hotelranking.view.ui.main.MainActivity
import io.reactivex.Observable
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModel()

    private var favCountry: String? = null
    private var favFood: String? = null
    private var favMovie: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        playAnimation()
        inputLayoutValidate()
        setupAction()
    }

    private fun setupAction() {
        binding.mbRegister.setOnClickListener {

            val name = binding.name.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val phone = binding.phone.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val favorite = binding.security.text.toString().trim()
            val spinner = binding.spinnerSecurity.selectedItem.toString()

            when {
                name.isEmpty() -> {
                    binding.nameRegist.error = getString(R.string.name_required)
                }
                phone.isEmpty() -> {
                    binding.numberRegist.error = getString(R.string.mobile_number_required)
                }
                favorite.isEmpty() -> {
                    binding.layoutSecurity.error = getString(R.string.required)
                }
                spinner == "Select Question :" -> {
                    AlertDialog.Builder(this@RegisterActivity).apply {
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

                    registerViewModel.registerUser(
                        name,
                        email,
                        phone,
                        password,
                        favCountry,
                        favFood,
                        favMovie
                    )
                        .observe(this@RegisterActivity) { result ->
                            if (result is Resource.Loading) {
                                showLoading(true)
                            } else if (result is Resource.Error) {
                                showLoading(false)
                                Toast.makeText(
                                    this@RegisterActivity, result.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                                favCountry = null
                                favFood = null
                                favMovie = null
                            } else if (result is Resource.Success) {
                                showLoading(false)
                                if (result.data != null) {
                                    AlertDialog.Builder(this@RegisterActivity).apply {
                                        setMessage(result.data.message)
                                        setPositiveButton("Login") { _, _ ->
                                            finish()
                                            startActivity(
                                                Intent(
                                                    this@RegisterActivity,
                                                    LoginActivity::class.java
                                                ),
                                                ActivityOptionsCompat.makeSceneTransitionAnimation(
                                                    this@RegisterActivity
                                                )
                                                    .toBundle()
                                            )
                                        }
                                        create()
                                        show()
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

        binding.loginNow.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(
                intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this@RegisterActivity).toBundle()
            )
        }
    }

    @SuppressLint("CheckResult")
    private fun inputLayoutValidate() {
        binding.mbRegister.isEnabled = false
        binding.mbRegister.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))

        val nameStream = RxTextView.textChanges(binding.name)
            .skipInitialValue()
            .map { name ->
                name.isEmpty()
            }
        nameStream.subscribe {
            showNameExistAlert(it)
        }

        val emailStream = RxTextView.textChanges(binding.email)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        emailStream.subscribe {
            showEmailExistAlert(it)
        }

        val phoneStream = RxTextView.textChanges(binding.phone)
            .skipInitialValue()
            .map { phone ->
                phone.isEmpty()
            }
        phoneStream.subscribe {
            showPhoneNumberExistAlert(it)
        }

        val spinnerStream = RxTextView.textChanges(binding.security)
            .skipInitialValue()
            .map { spinner ->
                spinner.isEmpty()
            }
        spinnerStream.subscribe {
            showSpinnerAlert(it)
        }

        val passwordStream = RxTextView.textChanges(binding.password)
            .skipInitialValue()
            .map { password ->
                password.length < 8
            }
        passwordStream.subscribe {
            showPasswordMinimalAlert(it)
        }

        val passwordConfirmationStream = Observable.merge(
            RxTextView.textChanges(binding.password)
                .map { password ->
                    password.toString() != binding.confirmPassword.text.toString()
                },
            RxTextView.textChanges(binding.confirmPassword)
                .map { confirmPassword ->
                    confirmPassword.toString() != binding.password.text.toString()
                }
        )
        passwordConfirmationStream.subscribe {
            showPasswordConfirmationAlert(it)
        }

        val invalidFieldsStream = Observable.combineLatest(
            emailStream,
            passwordStream,
            passwordConfirmationStream
        ) { emailInvalid: Boolean, passwordInvalid: Boolean, passwordConfirmationInvalid: Boolean ->
            !emailInvalid && !passwordInvalid && !passwordConfirmationInvalid
        }
        invalidFieldsStream.subscribe { isValid ->
            if (isValid) {
                binding.mbRegister.isEnabled = true
                binding.mbRegister.setBackgroundColor(ContextCompat.getColor(this, R.color.orange))
            } else {
                binding.mbRegister.isEnabled = false
                binding.mbRegister.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.light_gray
                    )
                )
            }
        }
    }

    private fun showNameExistAlert(isNotValid: Boolean) {
        binding.nameRegist.error = if (isNotValid) getString(R.string.name_required) else null
    }

    private fun showEmailExistAlert(isNotValid: Boolean) {
        binding.emailRegist.error = if (isNotValid) getString(R.string.invalid_email) else null
    }

    private fun showPhoneNumberExistAlert(isNotValid: Boolean) {
        binding.numberRegist.error =
            if (isNotValid) getString(R.string.mobile_number_required) else null
    }

    private fun showPasswordMinimalAlert(isNotValid: Boolean) {
        binding.passRegist.error = if (isNotValid) getString(R.string.password_length) else null
    }

    private fun showPasswordConfirmationAlert(isNotValid: Boolean) {
        binding.passConfirmRegist.error =
            if (isNotValid) getString(R.string.password_doesnt_match) else null
    }

    private fun showSpinnerAlert(isNotValid: Boolean) {
        binding.layoutSecurity.error = if (isNotValid) getString(R.string.required) else null
    }

    private fun setupActionBar() {
        supportActionBar?.elevation = 0F
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.title = getString(R.string.register)
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
        val nameRegister = ObjectAnimator.ofFloat(binding.nameRegist, View.ALPHA, 1f)
            .setDuration(ANIMATION_DURATION.toLong())
        val emailRegister = ObjectAnimator.ofFloat(binding.emailRegist, View.ALPHA, 1f)
            .setDuration(ANIMATION_DURATION.toLong())
        val numberRegister = ObjectAnimator.ofFloat(binding.numberRegist, View.ALPHA, 1f)
            .setDuration(ANIMATION_DURATION.toLong())
        val passwordReg = ObjectAnimator.ofFloat(binding.passRegist, View.ALPHA, 1f)
            .setDuration(ANIMATION_DURATION.toLong())
        val confirmPasswordReg = ObjectAnimator.ofFloat(binding.passConfirmRegist, View.ALPHA, 1f)
            .setDuration(ANIMATION_DURATION.toLong())
        val textNote = ObjectAnimator.ofFloat(binding.tvAdditionalSecurity, View.ALPHA, 1f)
            .setDuration(ANIMATION_DURATION.toLong())
        val spinner = ObjectAnimator.ofFloat(binding.spinnerSecurity, View.ALPHA, 1f)
            .setDuration(ANIMATION_DURATION.toLong())
        val btnRegister = ObjectAnimator.ofFloat(binding.mbRegister, View.ALPHA, 1f)
            .setDuration(ANIMATION_DURATION.toLong())
        val security = ObjectAnimator.ofFloat(binding.layoutSecurity, View.ALPHA, 1f)
            .setDuration(ANIMATION_DURATION.toLong())
        val travAcc = ObjectAnimator.ofFloat(binding.travAcc, View.ALPHA, 1f)
            .setDuration(ANIMATION_DURATION.toLong())
        val loginNow = ObjectAnimator.ofFloat(binding.loginNow, View.ALPHA, 1f)
            .setDuration(ANIMATION_DURATION.toLong())

        val together1 = AnimatorSet().apply {
            playTogether(textNote, spinner, security)
        }
        val together2 = AnimatorSet().apply {
            playTogether(travAcc, loginNow)
        }

        AnimatorSet().apply {
            playSequentially(
                nameRegister, emailRegister, numberRegister, passwordReg, confirmPasswordReg,
                together1, btnRegister, together2
            )
            start()
        }
    }

    companion object {
        private const val ANIMATION_DURATION = 200
    }
}