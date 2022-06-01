package com.traveloka.hotelranking.view.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.NavUtils
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.jakewharton.rxbinding2.widget.RxTextView
import com.traveloka.hotelranking.R
import com.traveloka.hotelranking.databinding.ActivityRegisterBinding
import com.traveloka.hotelranking.model.RegisterViewModel
import com.traveloka.hotelranking.view.ui.login.LoginActivity
import com.traveloka.hotelranking.view.ui.main.MainActivity
import io.reactivex.Observable
import io.reactivex.functions.Function3

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel>()

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
            registerViewModel.isLoading.observe(this) {
                showLoading(it)
            }

            val name = binding.name.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val phone = binding.phone.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val confirmPass = binding.confirmPassword.text.toString().trim()

            when {
                name.isEmpty() -> {
                    binding.nameRegist.error = getString(R.string.name_required)
                }
                email.isEmpty() -> {
                    binding.emailRegist.error = getString(R.string.invalid_email)
                }
                phone.isEmpty() -> {
                    binding.numberRegist.error = getString(R.string.mobile_number_required)
                }
                password.isEmpty() -> {
                    binding.passRegist.error = getString(R.string.password_required)
                }
                confirmPass.isEmpty() -> {
                    binding.passConfirmRegist.error = getString(R.string.password_required)
                }
                else -> {
                    registerViewModel.registerUser(name, email, phone, password)

                    registerViewModel.messageSuccessResponse.observe(this) { status ->
                        status?.let {
                            AlertDialog.Builder(this@RegisterActivity).apply {
                                setMessage(registerViewModel.messageSuccessResponse.value)
                                setPositiveButton("Login") { _, _ ->
                                    finish()
                                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java),
                                    ActivityOptionsCompat.makeSceneTransitionAnimation(this@RegisterActivity).toBundle())
                                }
                                create()
                                show()
                            }
                        }
                    }

                    registerViewModel.messageResponse.observe(this) { status ->
                        status?.let {
                            Toast.makeText(
                                this@RegisterActivity, registerViewModel.messageResponse.value,
                                Toast.LENGTH_SHORT
                            ).show()
                            registerViewModel.messageResponse.value = null
                        }
                    }
                }
            }
        }

        binding.loginNow.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this@RegisterActivity).toBundle())
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
        emailStream.subscribe{
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

        val passwordStream = RxTextView.textChanges(binding.password)
            .skipInitialValue()
            .map { password ->
                password.length < 8
            }
        passwordStream.subscribe{
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
        passwordConfirmationStream.subscribe{
            showPasswordConfirmationAlert(it)
        }

        val invalidFieldsStream = Observable.combineLatest(
            emailStream,
            passwordStream,
            passwordConfirmationStream,
            Function3 { emailInvalid: Boolean, passwordInvalid: Boolean, passwordConfirmationInvalid: Boolean ->
                !emailInvalid && !passwordInvalid && !passwordConfirmationInvalid
            }
        )
        invalidFieldsStream.subscribe { isValid ->
            if (isValid) {
                binding.mbRegister.isEnabled = true
                binding.mbRegister.setBackgroundColor(ContextCompat.getColor(this, R.color.orange))
            } else {
                binding.mbRegister.isEnabled = false
                binding.mbRegister.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
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
        binding.numberRegist.error = if (isNotValid) getString(R.string.mobile_number_required) else null
    }

    private fun showPasswordMinimalAlert(isNotValid: Boolean) {
        binding.passRegist.error = if (isNotValid) getString(R.string.password_length) else null
    }

    private fun showPasswordConfirmationAlert(isNotValid: Boolean) {
        binding.passConfirmRegist.error = if (isNotValid) getString(R.string.password_doesnt_match) else null
    }

    private fun setupActionBar() {
        supportActionBar?.elevation = 0F
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {
        val nameRegister = ObjectAnimator.ofFloat(binding.nameRegist, View.ALPHA, 1f).setDuration(300)
        val emailRegister = ObjectAnimator.ofFloat(binding.emailRegist, View.ALPHA, 1f).setDuration(300)
        val numberRegister = ObjectAnimator.ofFloat(binding.numberRegist, View.ALPHA, 1f).setDuration(300)
        val passwordReg = ObjectAnimator.ofFloat(binding.passRegist, View.ALPHA, 1f).setDuration(300)
        val confirmPasswordReg = ObjectAnimator.ofFloat(binding.passConfirmRegist, View.ALPHA, 1f).setDuration(300)
        val btnRegister = ObjectAnimator.ofFloat(binding.mbRegister, View.ALPHA, 1f).setDuration(300)
        val btnRegisterGoogle = ObjectAnimator.ofFloat(binding.mbRegisterGoogle, View.ALPHA, 1f).setDuration(300)
        val travAcc = ObjectAnimator.ofFloat(binding.travAcc, View.ALPHA, 1f).setDuration(300)
        val loginNow = ObjectAnimator.ofFloat(binding.loginNow, View.ALPHA, 1f).setDuration(300)

        val together = AnimatorSet().apply {
            playTogether(travAcc, loginNow)
        }

        AnimatorSet().apply {
            playSequentially(nameRegister, emailRegister, numberRegister, passwordReg, confirmPasswordReg,
                btnRegister, btnRegisterGoogle, together)
            start()
        }
    }
}