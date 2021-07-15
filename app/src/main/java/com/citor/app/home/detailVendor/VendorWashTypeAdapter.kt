package com.citor.app.home.detailVendor

import android.content.Intent
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.citor.app.R
import com.citor.app.databinding.ItemWashTypeBinding
import com.citor.app.home.detailBooking.BookingActivity
import com.citor.app.home.searchVendor.VendorItemEntity
import java.text.DecimalFormat

class VendorWashTypeAdapter(private val vendorId: String, private val name: String) : RecyclerView.Adapter<VendorWashTypeAdapter.WashTypeHolder>() {

    private var listWashType = ArrayList<VendorItemEntity>()

    fun setListWashType(listWashType: List<VendorItemEntity>?) {
        if (listWashType == null) return
        this.listWashType.clear()
        this.listWashType.addAll(listWashType)
    }

    class WashTypeHolder(private val binding: ItemWashTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(id: String, vendorName: String, washType: VendorItemEntity) {
            with(binding) {
                val numbering = DecimalFormat("#,###")

                tvNameType.text = washType.judul

                if(washType.judul.subSequence(0, 10).contains("cuci salju", ignoreCase = true)){
                    Glide.with(itemView.context)
                        .asDrawable()
                        .load(R.drawable.img_washing_snow)
                        .into(imgWashType)
                }

                else if(washType.judul.subSequence(0, 12).contains("cuci reguler", ignoreCase = true)){
                    Glide.with(itemView.context)
                        .asDrawable()
                        .load(R.drawable.img_washing_reguler)
                        .into(imgWashType)
                }

                tvPrice.text = numbering.format(washType.harga.toInt())
                btnChooseType.setOnClickListener {
                    val intent = Intent(itemView.context, BookingActivity::class.java)
                        .apply {
                            putExtra(BookingActivity.vendorId, id)
                            putExtra(BookingActivity.service, washType.judul)
                            putExtra(BookingActivity.price, washType.harga)
                            putExtra(BookingActivity.vendorName, vendorName)
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
        val name = name
        holder.bind(id, name, washType)
    }

    override fun getItemCount(): Int {
        return listWashType.size
    }
}