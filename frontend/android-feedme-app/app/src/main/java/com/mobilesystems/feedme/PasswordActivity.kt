package com.mobilesystems.feedme

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mobilesystems.feedme.ui.authentication.AuthViewModel
import com.mobilesystems.feedme.databinding.ActivityPasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*

@AndroidEntryPoint
class PasswordActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private var _binding: ActivityPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this@PasswordActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val emailEditText = binding.email
        val resetButton = binding.reset
        val loadingProgressBar = binding.loading
        resetButton.isEnabled = true

        binding.reset.setOnClickListener {
            val intent = Intent(this@PasswordActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}