package com.citor.app.home.detailBooking

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.citor.app.R
import com.citor.app.databinding.ItemBookingBinding
import com.citor.app.home.payment.FixPaymentActivity
import com.citor.app.home.searchVendor.VendorItemEntity

class BookingAdapter(private val listInfo: ArrayList<String>) : RecyclerView.Adapter<BookingAdapter.BookingItemViewHolder>() {

    private var listBookingItem = ArrayList<VendorItemEntity>()

    fun setListBookingItem(listBookingItem: ArrayList<VendorItemEntity>) {
        if (listBookingItem == null) return
        this.listBookingItem.clear()
        this.listBookingItem.addAll(listBookingItem)
    }

    class BookingItemViewHolder(private val binding: ItemBookingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listInfo: ArrayList<String>, bookingItem: VendorItemEntity) {
            with(binding) {
                tvJamBuka.text = bookingItem.jam_buka

                when (bookingItem.status) {
                    "tersedia" -> {
                        val vendorId = listInfo[0]
                        val service = listInfo[1]
                        val price = listInfo[2]
                        itemView.setOnClickListener {
                            val intent = Intent(itemView.context, FixPaymentActivity::class.java)
                                .apply {
                                    putExtra(FixPaymentActivity.vendorId, vendorId)
                                    putExtra(FixPaymentActivity.service, service)
                                    putExtra(FixPaymentActivity.price, price)
                                    putExtra(FixPaymentActivity.timeService, bookingItem.jam_buka)
                                }
                            itemView.context.startActivity(intent)
                        }
                    }
                    "kunci" -> {
                        cvItemBooking.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.md_grey_600))
                        cvItemBooking.isEnabled = false
                    }
                    "penuh" -> {
                        cvItemBooking.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.redClose))
                        cvItemBooking.isEnabled = false
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingItemViewHolder {
        val itemBookingBinding = ItemBookingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookingItemViewHolder(itemBookingBinding)
    }

    override fun onBindViewHolder(holder: BookingItemViewHolder, position: Int) {
        val bookingItem = listBookingItem[position]
        val infoList = listInfo
        holder.bind(infoList, bookingItem)
    }

    override fun getItemCount(): Int {
        return listBookingItem.size
    }
}