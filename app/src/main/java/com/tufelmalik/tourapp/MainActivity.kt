package com.tufelmalik.tourapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.tufelmalik.tourapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val actionBar = supportActionBar
        actionBar?.hide()
        binding.btnAdmin.setOnClickListener {
            startActivity(Intent(this@MainActivity,AdminActivity::class.java))
        }
        binding.btnWatchList.setOnClickListener {
            startActivity(Intent(this@MainActivity,WatchListActivity::class.java))
        }

        binding.txtAhmdebad.setOnClickListener {
            replaceActivity("ahmedabad")
        }
        binding.txtChennai.setOnClickListener {
            replaceActivity("chennai")
        }
        binding.txtHyderabad.setOnClickListener {
            replaceActivity("hyderabad")
        }
        binding.txtMumbai.setOnClickListener {
            replaceActivity("mumbai")
        }
        binding.txtDelhi.setOnClickListener {
            replaceActivity("delhi")
        }
        binding.txtKolkata.setOnClickListener {
            replaceActivity("kolkata")
        }






    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun replaceActivity(cityName: String) {
        var intent = Intent(this@MainActivity,CityListActivity::class.java)
        intent.putExtra("city__name",cityName)
        startActivity(intent)
    }
}