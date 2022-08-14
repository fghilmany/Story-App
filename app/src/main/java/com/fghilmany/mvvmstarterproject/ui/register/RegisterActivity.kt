package com.fghilmany.mvvmstarterproject.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fghilmany.mvvmstarterproject.R
import com.fghilmany.mvvmstarterproject.core.data.Resource
import com.fghilmany.mvvmstarterproject.databinding.ActivityRegisterBinding
import com.fghilmany.mvvmstarterproject.ui.custom.CustomTextInputEditText
import org.koin.android.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            etName.globalChange()
            etPassword.globalChange()
            etEmail.globalChange()
            btLogin.setOnClickListener {
                viewModel.setRegisterParam(
                    etName.text.toString(),
                    etEmail.text.toString(),
                    etPassword.text.toString()
                )
                viewModel.doRegister().observe(this@RegisterActivity) {
                    when (it) {
                        is Resource.Loading -> {
                            viewLoading.root.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            viewLoading.root.visibility = View.GONE
                            Toast.makeText(
                                this@RegisterActivity,
                                resources.getString(R.string.success_register),
                                Toast.LENGTH_SHORT
                            ).show()
                            onBackPressed()
                        }
                        is Resource.Error -> {
                            viewLoading.root.visibility = View.GONE
                            Toast.makeText(this@RegisterActivity, it.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }

        playAnimation()

    }

    private fun playAnimation() {
        with(binding) {
            val image = ObjectAnimator.ofFloat(imageLogo, View.ALPHA, 1f).setDuration(500)
            val name = ObjectAnimator.ofFloat(tilName, View.ALPHA, 1f).setDuration(500)
            val email = ObjectAnimator.ofFloat(tilEmail, View.ALPHA, 1f).setDuration(500)
            val password = ObjectAnimator.ofFloat(tilPassword, View.ALPHA, 1f).setDuration(500)
            val buttonLogin = ObjectAnimator.ofFloat(btLogin, View.ALPHA, 1f).setDuration(500)

            AnimatorSet().apply {
                playSequentially(image, name, email, password, buttonLogin)
                start()
            }
        }
    }

    private fun CustomTextInputEditText.globalChange() {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                with(binding) {
                    btLogin.isEnabled =
                        etEmail.isValid == true && etPassword.isValid == true && etName.isValid == true
                }
            }

        })
    }
}