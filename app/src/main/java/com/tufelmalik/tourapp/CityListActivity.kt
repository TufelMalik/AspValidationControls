package com.tufelmalik.tourapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tufelmalik.tourapp.databinding.ActivityCityListBinding

class CityListActivity : AppCompatActivity() {
    val binding by lazy{
        ActivityCityListBinding.inflate(layoutInflater)
    }
    private var places : String?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar?.hide()
        places = intent.getStringExtra("city__name")!!.lowercase()
        binding.txtHeadCity.setText(places+"'s Places")
        getDataFromFirebase(places!!)
    }

    private fun getDataFromFirebase(places: String) {
        try {

            binding.cityListProgressBar.visibility = View.VISIBLE
            val databaseRef = FirebaseDatabase.getInstance().getReference("Places").child(places)

            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val proList = mutableListOf<CityModel>() // Initialize an empty list

                    for (i in snapshot.children) {
                        val cityModel = i.getValue(CityModel::class.java)
                        if (cityModel != null) {
                            proList.add(cityModel)
                        }
                    }
                    setRecyclerView(proList)
                }

                override fun onCancelled(error: DatabaseError) {
                    binding.cityListProgressBar.visibility = View.GONE
                    Toast.makeText(this@CityListActivity, error.message, Toast.LENGTH_SHORT).show()
                }
            })

        }catch (e : Exception){
            binding.cityListProgressBar.visibility = View.GONE
            Log.d("name",e.message.toString())
            Toast.makeText(this@CityListActivity,e.message.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    private fun setRecyclerView(proList: MutableList<CityModel>) {
        binding.cityListProgressBar.visibility = View.GONE
        binding.cityListRecyclerView.layoutManager = LinearLayoutManager(this@CityListActivity)
        binding.cityListRecyclerView.adapter =  CityListAdapter(this@CityListActivity, proList,"list" )

    }
}