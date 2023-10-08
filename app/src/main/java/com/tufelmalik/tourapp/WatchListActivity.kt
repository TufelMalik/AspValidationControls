package com.tufelmalik.tourapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tufelmalik.tourapp.databinding.ActivityWatchListBinding

class WatchListActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityWatchListBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val actionBar = supportActionBar
        actionBar?.hide()
        binding.btnBackWatchList.setOnClickListener { onBackPressed() }
        getDataFromDB()


    }


    private fun getDataFromDB() {
        binding.watchListProgressBar.visibility = View.VISIBLE
        val db = FirebaseDatabase.getInstance()
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val historyRef = db.getReference("WatchList").child(currentUserId)

        historyRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val proList = mutableListOf<CityModel>()
                for (childSnapshot in snapshot.children) {
                    val productModel = childSnapshot.getValue(CityModel::class.java)
                    if (productModel != null) {
                        proList.add(productModel)
                    }
                    setRecyclerView(proList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                binding.watchListProgressBar.visibility = View.GONE
            }
        })


    }

    private fun setRecyclerView(proList: MutableList<CityModel>) {
        binding.watchListProgressBar.visibility = View.GONE
        binding.watchRecyclerView.layoutManager = LinearLayoutManager(this@WatchListActivity)
        binding.watchRecyclerView.adapter =  CityListAdapter(this@WatchListActivity, proList ,"muzammil")
    }
}