package com.tufelmalik.tourapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tufelmalik.tourapp.databinding.ActivityDetailedBinding

class DetailedActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityDetailedBinding.inflate(layoutInflater)
    }
    private lateinit var db : DatabaseReference
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar?.hide()

        val placeName = intent.getStringExtra("name")
        val placeImage = intent.getStringExtra("img")
        val placeDescription = intent.getStringExtra("des")
        var placeCityName = intent.getStringExtra("cityName")

        binding.apply {
            txtPlaceNameDetail.text = placeName
            txtDesDet.text = placeDescription
            Glide.with(this@DetailedActivity).load(placeImage).into(binding.placeImageDet)
        }

        binding.atcbtn.setOnClickListener {
            saveDataToDB()

        }

        binding.btnback.setOnClickListener {
            onBackPressed()
        }
    }

    private fun saveDataToDB() {
        val placeName = intent.getStringExtra("name")
        val placeImage = intent.getStringExtra("img")
        val placeDescription = intent.getStringExtra("des")
        val placeCityName = intent.getStringExtra("cityName")

        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        val currentUserId = auth.currentUser!!.uid.toString()
        val history = CityModel(placeName,placeImage,placeDescription,placeCityName)
        db.child("WatchList").child(currentUserId).child(placeName!!).setValue(history)
            .addOnSuccessListener {
                Toast.makeText(this@DetailedActivity,placeName +" Added in WatchList", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this@DetailedActivity,"Somthing went wrong !!!",Toast.LENGTH_SHORT).show()
            }
    }
}