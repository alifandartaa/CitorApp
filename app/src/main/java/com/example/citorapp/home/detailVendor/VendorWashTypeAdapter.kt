package com.example.citorapp.home.detailVendor

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.citorapp.databinding.ItemWashTypeBinding
import com.example.citorapp.home.detailBooking.BookingActivity
import com.example.citorapp.home.searchVendor.VendorItemEntity
import java.text.DecimalFormat

class VendorWashTypeAdapter(private val vendorId: String) : RecyclerView.Adapter<VendorWashTypeAdapter.WashTypeHolder>() {

    private var listWashType = ArrayList<VendorItemEntity>()

    fun setListWashType(listWashType: List<VendorItemEntity>?) {
        if (listWashType == null) return
        this.listWashType.clear()
        this.listWashType.addAll(listWashType)
    }

    class WashTypeHolder(private val binding: ItemWashTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(id: String, washType: VendorItemEntity) {
            with(binding) {
                val numbering = DecimalFormat("#,###")
                tvNameType.text = washType.judul
                tvPrice.text = numbering.format(washType.harga.toInt())
                btnChooseType.setOnClickListener {
                    val intent = Intent(itemView.context, BookingActivity::class.java)
                        .apply {
                            putExtra(BookingActivity.vendorId, id)
                        }
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
        val id = vendorId
        holder.bind(id, washType)
    }

    override fun getItemCount(): Int {
        return listWashType.size
    }
}