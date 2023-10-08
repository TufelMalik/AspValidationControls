package com.tufelmalik.tourapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.tufelmalik.tourapp.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegistrationBinding
    private val auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btngotoLogin.setOnClickListener {
            startActivity(Intent(this@RegistrationActivity,RegistrationActivity::class.java))
        }
        supportActionBar!!.hide()
        binding.btnReg.setOnClickListener {
            if (binding.etNameReg.text.isEmpty()) {
                binding.etNameReg.error = "Enter Full Name"
            } else if (binding.etEmailReg.text.isEmpty()) {
                binding.etPassReg.error = "Email Address"
            } else if (binding.etNameReg.text.isEmpty()) {
                binding.etPassReg.error = "Password"
            } else {
                binding.regProgressBar.visibility = View.VISIBLE
                registerTheUser()
            }
        }


    }

    private fun registerTheUser() {
        val email = binding.etEmailReg.text.toString()
        val pass = binding.etPassReg.text.toString()
        val name = binding.etNameReg.text.toString()
        auth.createUserWithEmailAndPassword(
            email,
            pass
        )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    addUserDataOnDB(name,email,pass)
                    binding.regProgressBar.visibility = View.GONE
                    startActivity(Intent(this@RegistrationActivity, MainActivity::class.java))
                } else {
                    binding.regProgressBar.visibility = View.GONE
                    Toast.makeText(this@RegistrationActivity, "Something Went Wrong !!!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                binding.regProgressBar.visibility = View.GONE
                Toast.makeText(this@RegistrationActivity, "Registration Failed.\nPleas try again later...: ${exception.message}", Toast.LENGTH_SHORT).show()
                Log.e("Firebase", "Failed to save data", exception)
            }

    }

    private fun addUserDataOnDB(name: String?, email: String, pass: String) {
        val database = FirebaseDatabase.getInstance().getReference("Users")

        val user = Users().apply {
            userAuth(auth.uid.toString(),name, email, pass)
        }

        database.child(auth.uid.toString()).setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this@RegistrationActivity, "Registration Successfully done.", Toast.LENGTH_SHORT).show()
                binding.regProgressBar.isVisible= false
            }
            .addOnFailureListener {
                Toast.makeText(this@RegistrationActivity, "Registration Failed.\nPlease try again later...", Toast.LENGTH_SHORT).show()
                binding.regProgressBar.isVisible= false
            }
    }


}