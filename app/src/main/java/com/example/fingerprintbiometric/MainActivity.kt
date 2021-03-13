package com.example.fingerprintbiometric

import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity()
{

    private lateinit var context: Context
    private var cancellationSignal: CancellationSignal? = null
    private val anthendcation: BiometricPrompt.AuthenticationCallback
    get() = @RequiresApi(Build.VERSION_CODES.P)
    object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
            super.onAuthenticationError(errorCode, errString)

            startNotfiyUser("AuthenticationError: $errString");
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
            super.onAuthenticationSucceeded(result)
            startNotfiyUser("AuthenticationSucceeded")
            startActivity(Intent(this@MainActivity, SecuritKeyActivity::class.java))


        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkFingerPrint()

        var press = findViewById(R.id.press) as Button

        press.setOnClickListener {
            val biometricPrompt: BiometricPrompt = BiometricPrompt.Builder(this)
                .setTitle("Finger Print Biometric")
                .setSubtitle("Authendaction Required ")
                .setDescription("this app used fimger print make sure you register your finger print")
                .setNegativeButton("Cancel", this.mainExecutor, DialogInterface.OnClickListener{ dialog, which ->
                    startNotfiyUser("Authendaction cancelled it ")

                    }).build()
           biometricPrompt.authenticate(getCancelationSignalFromUser(), mainExecutor, anthendcation)
        }
    }

   private fun getCancelationSignalFromUser(): CancellationSignal
   {
       cancellationSignal = CancellationSignal()
       cancellationSignal?.setOnCancelListener {
           startNotfiyUser("the anuthdcation cancel it by user ")
       }
       return cancellationSignal as CancellationSignal
   }
    private fun startNotfiyUser (message: String)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun checkFingerPrint (): Boolean
    {
        val kaygaurdManger: KeyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!kaygaurdManger.isKeyguardSecure) {
            startNotfiyUser("Fingerprint authntication has not been enabled in settings, please try again ");
            return false
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED)

        {
            startNotfiyUser("Fingerprint authntication has not  enabled ");

            return false
        }

        return if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT))
        {
            true
        }
        else true
    }

    }
