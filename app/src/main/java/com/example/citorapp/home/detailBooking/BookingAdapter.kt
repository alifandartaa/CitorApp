package com.example.citorapp.home.detailBooking

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.citorapp.R
import com.example.citorapp.databinding.ItemBookingBinding
import com.example.citorapp.home.payment.FixPaymentActivity

class BookingAdapter : RecyclerView.Adapter<BookingAdapter.BookingItemViewHolder>() {

    private var listBookingItem = ArrayList<BookingEntity>()

    fun setListBookingItem(listBookingItem: List<BookingEntity>?) {
        if (listBookingItem == null) return
        this.listBookingItem.clear()
        this.listBookingItem.addAll(listBookingItem)
    }

    class BookingItemViewHolder(private val binding: ItemBookingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bookingItem: BookingEntity) {
            with(binding) {
                tvJamBuka.text = bookingItem.jamBuka
                if (!bookingItem.status) {
                    cvItemBooking.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.redClose))
                }
                itemView.setOnClickListener {
                    if (bookingItem.status) {
                        val intent = Intent(itemView.context, FixPaymentActivity::class.java)
                        itemView.context.startActivity(intent)
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
        holder.bind(bookingItem)
    }

    override fun getItemCount(): Int {
        return listBookingItem.size
    }
}