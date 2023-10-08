package com.tufelmalik.tourapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.tufelmalik.tourapp.databinding.ActivityLoginBinding

class
LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    val database = FirebaseDatabase.getInstance().getReference("Users")
    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val actionBar = supportActionBar
        actionBar?.hide()

        binding.progressBarLogin.visibility = View.GONE
        binding.btnGotoReg.setOnClickListener {
            startActivity(Intent(this,RegistrationActivity::class.java))
        }
        binding.btnLogin.setOnClickListener {
            if (binding.etEmailLogin.text.isEmpty()) {
                binding.etEmailLogin.error = "Email Address"
            } else if (binding.etPassLogin.text.isEmpty()) {
                binding.etPassLogin.error = "Enter Password"
            }else {
                binding.progressBarLogin.visibility = View.VISIBLE
                loginTheUser()
            }
        }

        if(auth.currentUser != null){
            startActivity(Intent(this,MainActivity::class.java))
        }

    }

    private fun loginTheUser() {
        val email = binding.etEmailLogin.text.toString()
        val pass = binding.etPassLogin.text.toString()


        auth.signInWithEmailAndPassword(email,pass)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    binding.progressBarLogin.visibility =View.GONE
                    Toast.makeText(this@LoginActivity, "Login Successfully...", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .addOnFailureListener {
                binding.progressBarLogin.visibility = View.GONE
                Toast.makeText(this@LoginActivity, "You Don't have account\nPleas SignUp first !", Toast.LENGTH_SHORT).show()
            }
    }
 }
