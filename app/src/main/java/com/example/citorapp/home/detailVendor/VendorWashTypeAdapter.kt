package com.example.citorapp.home.detailVendor

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.citorapp.databinding.ItemWashTypeBinding
import com.example.citorapp.home.detailBooking.BookingActivity

class VendorWashTypeAdapter : RecyclerView.Adapter<VendorWashTypeAdapter.WashTypeHolder>() {

    private var listWashType = ArrayList<WashTypeEntity>()

    fun setListWashType(listWashType: List<WashTypeEntity>?) {
        if (listWashType == null) return
        this.listWashType.clear()
        this.listWashType.addAll(listWashType)
    }

    class WashTypeHolder(private val binding: ItemWashTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(washType: WashTypeEntity) {
            with(binding) {
                tvNameType.text = washType.name
                btnChooseType.setOnClickListener {
                    val intent = Intent(itemView.context, BookingActivity::class.java)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WashTypeHolder {
        val itemWashTypeBinding =
            ItemWashTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WashTypeHolder(itemWashTypeBinding)
    }

    override fun onBindViewHolder(holder: WashTypeHolder, position: Int) {
        val washType = listWashType[position]
        holder.bind(washType)
    }

    override fun getItemCount(): Int {
        return listWashType.size
    }
}