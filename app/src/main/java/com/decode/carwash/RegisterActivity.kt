package com.decode.carwash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.core.content.ContentProviderCompat.requireContext
import com.decode.carwash.databinding.ActivityRegisterBinding
import com.decode.carwash.databinding.FragmentFirstBinding

class RegisterActivity : AppCompatActivity() {

//    lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {

//        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
//        setContentView(binding.root)

        supportActionBar!!.hide()

        setContentView(R.layout.activity_register)


//        val gender = resources.getStringArray(R.array.gender)
//        val gender = arrayOf("Select Gender","Male","Female")
//        val arrayAdapter = ArrayAdapter(this, R.layout.spinner_list, gender)
//        binding.spMonths.setAdapter(arrayAdapter)


    }
}