package com.example.citorapp.home.searchVendor

import android.content.Intent
import android.graphics.Color
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
                tvName.text = vendorItem.nama_mitra
                tvAddress.text = vendorItem.alamat_mitra
                if (vendorItem.statusBuka == "buka") {
                    tvStatus.text = "BUKA HARI INI"
                    itemView.setOnClickListener {
                        val intent = Intent(itemView.context, DetailVendorActivity::class.java)
                            .apply {
                                putExtra(DetailVendorActivity.vendorId, vendorItem.idmitra)
                                putExtra(DetailVendorActivity.vendorName, vendorItem.nama_mitra)
                                putExtra(DetailVendorActivity.vendorAddress, vendorItem.alamat_mitra)
                                putExtra(DetailVendorActivity.lat, vendorItem.lat)
                                putExtra(DetailVendorActivity.long, vendorItem.long)
                            }
                        itemView.context.startActivity(intent)
                    }
                } else {
                    tvStatus.text = "TUTUP"
                    rlItemVendor.setBackgroundColor(Color.parseColor("#466a7d"))
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