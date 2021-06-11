package com.example.citorapp.home.detailBooking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.citorapp.R

class BookingAdapter (private val listBooking: ArrayList<BookingModel>) :
    RecyclerView.Adapter<BookingAdapter.AdapterViewHolder>() {
    private lateinit var onItemClickCallback: BookingAdapter.OnItemClickCallback

    //viewHolder
    inner class AdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvJamBuka: TextView = itemView.findViewById(R.id.tv_jamBuka)
    }

    //Callback
    fun setOnItemClickCallback(onItemClickCallback: BookingAdapter.OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    //onClickitem
    interface OnItemClickCallback {
        fun onItemClicked(data: BookingModel)
    }

    //3 komponen penting adapter
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AdapterViewHolder {
        val view: View =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_booking, viewGroup, false)
        return AdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        val booking = listBooking[position]
        holder.tvJamBuka.text = booking.jamBuka
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listBooking[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int {
        return listBooking.size
    }

}