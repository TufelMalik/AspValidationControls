package com.tufelmalik.tourapp

import android.app.Activity
import android.content.ClipDescription
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.tufelmalik.tourapp.databinding.ActivityAdminBinding
import kotlin.random.Random

class AdminActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityAdminBinding.inflate(layoutInflater)
    }
    private lateinit var imgUri: Uri
    private var auth = FirebaseAuth.getInstance()
    private lateinit var db: FirebaseDatabase
    private lateinit var selectedCategory: String

    val categories = arrayOf(
        "kolkata",
        "delhi",
        "mumbai",
        "hyderabad",
        "chennai",
        "ahmedabad"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar?.hide()
        binding.imgSelectPlace.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        val adapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categories)
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.placeCategory.adapter = adapter

        binding.placeCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCategory = categories[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected
            }
        }
        binding.placeCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCategory = categories[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected
            }
        }

        db = FirebaseDatabase.getInstance() // Initialize the database

        binding.btnAddPlace.setOnClickListener {
            binding.progressBarAdmin.isVisible = true
            val placeName = binding.etPlaceName.text.toString()
            val placeDes = binding.etPlaceDes.text.toString()

            if (placeName.isNotEmpty() && placeDes.isNotEmpty() && imgUri != null) {
                saveData2Database(placeName,placeDes, imgUri, selectedCategory)
            } else {
                Toast.makeText(this@AdminActivity, "Please enter all details", Toast.LENGTH_SHORT).show()
            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (data!!.data != null) {
                imgUri = data.data!!
                binding.imgSelectPlace.setImageURI(imgUri)
            } else {
                Toast.makeText(this@AdminActivity, "No image selected", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun saveData2Database(
        placeName: String,
        placeDescription: String,
        imgUri: Uri,
        selectedCategory: String
    ){
        val auth = FirebaseAuth.getInstance()
        val randomNumber = Random.nextInt(1000) + 1 // Generate a random number between 1 and 1000
        val storage = FirebaseStorage.getInstance().reference
        val productsRef = storage.child("Places").child(selectedCategory).child(randomNumber.toString())

        productsRef.putFile(imgUri)
            .addOnSuccessListener { uploadTask ->
                productsRef.downloadUrl.addOnSuccessListener { uri ->
                    val product = CityModel(placeName,uri.toString(),placeDescription, selectedCategory)
                    val databaseRef = FirebaseDatabase.getInstance().getReference("Places").child(selectedCategory).child(randomNumber.toString())

                    databaseRef.setValue(product)
                        .addOnSuccessListener {
                            clearAll()
                            binding.progressBarAdmin.isVisible = false
                            Toast.makeText(this@AdminActivity, "Place Added Successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { exception ->
                            binding.progressBarAdmin.isVisible = false
                            Toast.makeText(this@AdminActivity, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {
                binding.progressBarAdmin.isVisible = false
            }


    }

    private fun clearAll() {
        binding.apply {
            imgSelectPlace.setImageResource(R.drawable.ic_add_photoes)
            etPlaceDes.text = null
            etPlaceName.text = null
        }
    }
}