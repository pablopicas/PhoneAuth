package com.decode.carwash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.decode.carwash.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    val auth = FirebaseAuth.getInstance()
    private var vaificationId: String? = null

    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityLoginBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        dialog = AlertDialog.Builder(this).setView(R.layout.loading_layout)
            .setCancelable(false)
            .create()

        binding.sendOtp.setOnClickListener {

            if (binding.userNumber.text!!.isEmpty()) {

                binding.userNumber.error = "Please Enter Your Number"

            } else {
                sendOTP(binding.userNumber.text.toString())

            }

        }

        binding.varifyOtp.setOnClickListener {

            if (binding.userOtp.text!!.isEmpty()) {

                binding.userOtp.error = "Please Enter Your OTP"

            } else {

                varifyOtp(binding.userOtp.text.toString())

            }

        }
    }

    private fun sendOTP(number: String) {

        dialog.show()

//        binding.sendOtp.showLoadingButton()
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

//                binding.sendOtp.showNormalButton()
                signInWithPhoneAuthCredential(credential)

            }

            override fun onVerificationFailed(e: FirebaseException) {
            }

            override fun onCodeSent(

                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken

            ) {
                dialog.dismiss()
                this@LoginActivity.vaificationId = verificationId

//                binding.sendOtp.showNormalButton()

                binding.numberLayout.visibility = GONE
                binding.otpLayout.visibility = VISIBLE

            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91$number")       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    private fun varifyOtp(otp: String) {

        dialog.show()

//        binding.sendOtp.showLoadingButton()
        val credential = PhoneAuthProvider.getCredential(vaificationId!!, otp)

        signInWithPhoneAuthCredential(credential)

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
//                binding.sendOtp.showNormalButton()
                if (task.isSuccessful) {

                    checkUserExist(binding.userNumber.text.toString())

//                    startActivity(Intent(this, MainActivity::class.java))
//                    finish()
                } else {

                    dialog.dismiss()

                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()

                }
            }
    }

    private fun checkUserExist(number: String) {

        FirebaseDatabase.getInstance().getReference("user").child(number)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    dialog.dismiss()
                    if (snapshot.exists()){

                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()

                    }else{

                        startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                        finish()

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    dialog.dismiss()
                    Toast.makeText(this@LoginActivity, error.message, Toast.LENGTH_SHORT).show()
                }


            })

    }

}
