package com.tufelmalik.tourapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CityListAdapter(
    private val context: Context,
    private val placeList : List<CityModel>,
    private val who : String
    ): RecyclerView.Adapter<CityListAdapter.CityViewHolder>()
{
    class CityViewHolder (binding: View) : RecyclerView.ViewHolder(binding) {
        val placeName: TextView = binding.findViewById(R.id.tvTitle)
        val placImage: ImageView = binding.findViewById(R.id.ivPlaceImage)
        val cbCart: CheckBox = binding.findViewById(R.id.idCBCart_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityListAdapter.CityViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.city_list_layout, parent, false)
        return CityViewHolder(layout)
    }

    override fun onBindViewHolder(holder: CityListAdapter.CityViewHolder, position: Int) {
        val data = placeList[position]
        holder.placeName.text = data.placeName
        Glide.with(context).load(data.placeImage).into(holder.placImage)
        if(who.equals("list")) {
            holder.cbCart.visibility = View.GONE
        }else{
            holder.cbCart.visibility = View.VISIBLE
            holder.cbCart.isChecked = true
        }
        holder.cbCart.setOnCheckedChangeListener { _, isChecked ->
            removeCartItem(position)
        }

        //  setonClick listner
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailedActivity::class.java)
            intent.putExtra("img", data.placeImage)
            intent.putExtra("name", data.placeName)
            intent.putExtra("des", data.placeDescription)
            intent.putExtra("cityName", data.cityName)
            context.startActivity(intent)
        }
    }

    private fun removeCartItem(position: Int) {
        val data = placeList[position]
        val db = FirebaseDatabase.getInstance().reference
        val auth = FirebaseAuth.getInstance()
        val currentUserId = auth.currentUser!!.uid.toString()
        db.child("WatchList").child(currentUserId).child(data.placeName!!).removeValue()
            .addOnSuccessListener {
            }
            .addOnFailureListener {
                Toast.makeText(context,"Somthing went wrong !!!", Toast.LENGTH_SHORT).show()
            }
    }
    override fun getItemCount() = placeList.size
}

