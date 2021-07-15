package com.citor.mitra.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.citor.mitra.ServiceDetailActivity
import com.citor.mitra.ServiceEntity
import com.citor.mitra.databinding.ItemServiceBinding


class AdapterRvService : RecyclerView.Adapter<AdapterRvService.ServiceItemViewHolder>() {

    private var listServiceItem = ArrayList<ServiceEntity>()

    fun setListServiceItem(listServiceItem: List<ServiceEntity>?) {
        if (listServiceItem == null) return
        this.listServiceItem.clear()
        this.listServiceItem.addAll(listServiceItem)
    }

    class ServiceItemViewHolder(private val binding: ItemServiceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(serviceItem: ServiceEntity) {
            with(binding) {
                tvClock.text = serviceItem.jam_buka
                tvUserName.text = serviceItem.nama
                cvDetail.setOnClickListener {
                    val intent = Intent(itemView.context, ServiceDetailActivity::class.java)
                        .apply {
                            putExtra(ServiceDetailActivity.name, serviceItem.nama)
                            putExtra(ServiceDetailActivity.timeService, serviceItem.jam_buka)
                            putExtra(ServiceDetailActivity.service, serviceItem.layanan)
                            putExtra(ServiceDetailActivity.price, serviceItem.harga)
                            putExtra(ServiceDetailActivity.orderId, serviceItem.kode_pemesanan)
                            putExtra(ServiceDetailActivity.paymentMethod, serviceItem.metode_pembayaran)
                            putExtra(ServiceDetailActivity.timestamp, serviceItem.timestamp)
                        }
                    itemView.context.startActivity(intent)
                    (itemView.context as Activity).finish()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceItemViewHolder {
        val itemServiceBinding = ItemServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceItemViewHolder(itemServiceBinding)
    }

    override fun onBindViewHolder(holder: ServiceItemViewHolder, position: Int) {
        val serviceItem = listServiceItem[position]
        holder.bind(serviceItem)
    }

    override fun getItemCount(): Int {
        return listServiceItem.size
    }
}