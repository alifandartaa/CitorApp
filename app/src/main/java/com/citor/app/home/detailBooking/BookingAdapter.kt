package com.citor.app.home.detailBooking

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.citor.app.R
import com.citor.app.databinding.ItemBookingBinding
import com.citor.app.home.payment.FixPaymentActivity
import com.citor.app.home.searchVendor.VendorItemEntity
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class BookingAdapter(private val listInfo: ArrayList<String>, private val vendorNameOrdered: String) :
    RecyclerView.Adapter<BookingAdapter.BookingItemViewHolder>() {

    private var listBookingItem = ArrayList<VendorItemEntity>()

    fun setListBookingItem(listBookingItem: ArrayList<VendorItemEntity>) {
        if (listBookingItem == null) return
        this.listBookingItem.clear()
        this.listBookingItem.addAll(listBookingItem)
    }

    class BookingItemViewHolder(private val binding: ItemBookingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listInfo: ArrayList<String>, vendorNameOrdered: String, bookingItem: VendorItemEntity) {
            with(binding) {
                tvJamBuka.text = bookingItem.jam_buka

                //get currentTime
                val calendar: Calendar = Calendar.getInstance()
                val hour24hrs: Int = calendar.get(Calendar.HOUR_OF_DAY)
                val minutes: Int = calendar.get(Calendar.MINUTE)
                println("Pukul :  $hour24hrs.$minutes")
//                tvJamBuka.text = "$hour24hrs.$minutes"

                when (bookingItem.status) {
                    "tersedia" -> {
                        val vendorId = listInfo[0]
                        val service = listInfo[1]
                        val price = listInfo[2]
                        itemView.setOnClickListener {

                            val dfTimeNow = "$hour24hrs.$minutes".toDouble()
//                            var bookTime = bookingItem.jam_buka.subSequence(0, 5).toString()
                            val waktu = "15.00"//nanti dirubah dari bookingitem.jamBuka
                            val waktu1 = "15.00 - 15.30"//dirubah dari akhir range bookingItem.jamBuka

                            var newWaktu = 0.0
                            val dfBookTimeEnd = waktu1.subSequence(8,13).toString().toDouble()
                            newWaktu = if(waktu.subSequence(3,5) != "00") {
                                waktu.toDouble()
                            }else{
                                waktu.toDouble()-1+0.6
                            }
                            val dfBookTime = newWaktu
                            val diffTime = (newWaktu - dfTimeNow).toString().subSequence(2,4)
                            //cek kondisi pesan kurang dari 30mnt
                            if (dfTimeNow < dfBookTimeEnd) {
                                if(dfTimeNow < dfBookTime-0.3) {
                                    val intent = Intent(itemView.context, FixPaymentActivity::class.java)
                                        .apply {
                                            putExtra(FixPaymentActivity.vendorName, vendorNameOrdered)
                                            putExtra(FixPaymentActivity.vendorId, vendorId)
                                            putExtra(FixPaymentActivity.service, service)
                                            putExtra(FixPaymentActivity.price, price)
                                            putExtra(FixPaymentActivity.timeService, bookingItem.jam_buka)
                                            putExtra(FixPaymentActivity.idJamBuka, bookingItem.idjam_buka)
                                        }
                                    itemView.context.startActivity(intent)
                                }else{
                                    Toast.makeText(itemView.context, "Anda Terlambat $diffTime menit", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(itemView.context, "Anda Melebihi Waktu Pemesanan ", Toast.LENGTH_SHORT).show()
                            }
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
        val vendorName = vendorNameOrdered
        holder.bind(infoList, vendorName, bookingItem)
    }

    override fun getItemCount(): Int {
        return listBookingItem.size
    }


}