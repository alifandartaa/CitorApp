package com.citor.app.home.searchVendor

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.citor.app.databinding.ItemVendorBinding
import com.citor.app.home.detailVendor.DetailVendorActivity
import java.util.*
import kotlin.collections.ArrayList

class SearchVendorAdapter : RecyclerView.Adapter<SearchVendorAdapter.VendorItemHolder>(), Filterable {

    private var listVendorItem = ArrayList<VendorItemEntity>()
    private var listVendorItemFilter = ArrayList<VendorItemEntity>()


    fun setListVendorItem(listVendorItem: List<VendorItemEntity>?) {
        if (listVendorItem == null) return
        this.listVendorItem.clear()
        this.listVendorItem.addAll(listVendorItem)

        this.listVendorItemFilter = listVendorItem as ArrayList<VendorItemEntity>
        notifyDataSetChanged()
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
                    rlItemVendor.setBackgroundColor(Color.parseColor("#455A64"))
                    cvItemBooking.isEnabled = false
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

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val filterResults = FilterResults()

                if (constraint == null || constraint.length < 0) {
                    filterResults.count = listVendorItemFilter.size
                    filterResults.values = listVendorItemFilter
                } else {
                    val charSearch = constraint.toString()

                    val resultList = ArrayList<VendorItemEntity>()

                    for (row in listVendorItemFilter) {
                        if (row.nama_mitra.toLowerCase(Locale.getDefault()).contains(charSearch.toLowerCase(Locale.getDefault()))) {
                            resultList.add(row)
                        }
                    }
                    filterResults.count = resultList.size
                    filterResults.values = resultList
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listVendorItem = results?.values as ArrayList<VendorItemEntity>
                notifyDataSetChanged()
            }

        }
    }
}



