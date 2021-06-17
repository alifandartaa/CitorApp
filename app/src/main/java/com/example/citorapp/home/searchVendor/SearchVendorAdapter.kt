package com.example.citorapp.home.searchVendor

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.citorapp.databinding.ItemVendorBinding
import com.example.citorapp.home.detailVendor.DetailVendorActivity

class SearchVendorAdapter : RecyclerView.Adapter<SearchVendorAdapter.VendorItemHolder>() {

    private var listVendorItem = ArrayList<VendorItemEntity>()

    fun setListVendorItem(listVendorItem: List<VendorItemEntity>?) {
        if (listVendorItem == null) return
        this.listVendorItem.clear()
        this.listVendorItem.addAll(listVendorItem)
    }

    class VendorItemHolder(private val binding: ItemVendorBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(vendorItem: VendorItemEntity) {
            with(binding) {
                tvName.text = vendorItem.name
                tvAddress.text = vendorItem.address
                if (vendorItem.status) {
                    tvStatus.text = "BUKA HARI INI"
                } else {
                    tvStatus.text = "TUTUP"
                }
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailVendorActivity::class.java)
                    itemView.context.startActivity(intent)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendorItemHolder {
        val itemVendorBinding = ItemVendorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VendorItemHolder(itemVendorBinding)
    }

    override fun onBindViewHolder(holder: VendorItemHolder, position: Int) {
        val vendorItem = listVendorItem[position]
        holder.bind(vendorItem)
    }

    override fun getItemCount(): Int {
        return listVendorItem.size
    }


}