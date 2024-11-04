package com.example.tseaafricaapp

import android.content.Intent
import android.os.Bundle
import android.provider.Settings // Add this import to resolve the Settings reference
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.tseaafricaapp.databinding.ActivityBiometricsBinding
import java.util.concurrent.Executor

class Biometrics : AppCompatActivity() {
    lateinit var binding: ActivityBiometricsBinding
    private lateinit var executor: Executor
    private lateinit var biometricPromot: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBiometricsBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_biometrics)

        binding.imgFingure.setOnClickListener{
            checkDeviceHasBiometric()
        }

        executor = ContextCompat.getMainExecutor(this)
        biometricPromot = BiometricPrompt(this,executor,object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(this@Biometrics, "Authentication error: $errString", Toast.LENGTH_LONG).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(this@Biometrics, "Authentication Successful", Toast.LENGTH_LONG).show()

            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(this@Biometrics, "Authentication Failed", Toast.LENGTH_LONG).show()
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Sample Title")
            .setSubtitle("Sample SubTitle")
            .setNegativeButtonText("Sample Negatuve button text")
            .build()

        binding.bioLogin.setOnClickListener{
            biometricPromot.authenticate(promptInfo)
        }
    }

    fun checkDeviceHasBiometric() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("MY_APP_TAG", "App can authenticate using biometrics")
                binding.tvMsg.text = "App can authenticate using biometrics"
                binding.bioLogin.isEnabled = true
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.d("MY_APP_TAG", "Biometric features are currently unavailable")
                binding.tvMsg.text = "Biometric features are currently unavailable"
                binding.bioLogin.isEnabled = false
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                    )
                }
                binding.bioLogin.isEnabled = false
                startActivityForResult(enrollIntent, 100)
            }
        }
    }
}
