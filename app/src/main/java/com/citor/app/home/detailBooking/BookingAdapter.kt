package com.citor.app.home.detailBooking

import android.app.Activity
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
import com.citor.app.retrofit.DataService
import com.citor.app.retrofit.RetrofitClient
import com.citor.app.retrofit.response.DefaultResponse
import com.citor.app.retrofit.response.MitraResponse
import com.citor.app.utils.Constants
import com.citor.app.utils.MySharedPreferences
import dev.shreyaspatil.MaterialDialog.MaterialDialog
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class BookingAdapter(private val listInfo: ArrayList<String>) :
    RecyclerView.Adapter<BookingAdapter.BookingItemViewHolder>() {

    private var listBookingItem = ArrayList<VendorItemEntity>()

    fun setListBookingItem(listBookingItem: ArrayList<VendorItemEntity>) {
        if (listBookingItem == null) return
        this.listBookingItem.clear()
        this.listBookingItem.addAll(listBookingItem)
    }

    class BookingItemViewHolder(private val binding: ItemBookingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listInfo: ArrayList<String>, bookingItem: VendorItemEntity) {
            with(binding) {
                val mySharedPreferences = MySharedPreferences(itemView.context)
                val tokenAuth = mySharedPreferences.getValue(Constants.TokenAuth).toString()
                tvJamBuka.text = bookingItem.jam_buka

                //get currentTime
                val calendar: Calendar = Calendar.getInstance()
                val hour24hrs: Int = calendar.get(Calendar.HOUR_OF_DAY)
                val minutes: Int = calendar.get(Calendar.MINUTE)
                println("Pukul :  $hour24hrs.$minutes")
//                tvJamBuka.text = "$hour24hrs.$minutes"
                val vendorId = listInfo[0]
                val service = listInfo[1]
                val price = listInfo[2]
                val vendorNameOrdered = listInfo[3]
                val jamBuka = bookingItem.jam_buka
                val idJamBuka = bookingItem.idjam_buka

                val services = RetrofitClient().apiRequest().create(DataService::class.java)
                when (bookingItem.status) {
                    "tersedia" -> {
                        itemView.setOnClickListener {
                            val dfTimeNow = "$hour24hrs.$minutes".toFloat()

//                            var bookTime = bookingItem.jam_buka.subSequence(0, 5).toString()
                            var bookTime = 0.0f
                            bookTime = if (jamBuka.subSequence(3, 5) != "00") {
                                jamBuka.toFloat()
                            } else {
                                jamBuka.toFloat() - 1.0f + 0.6f
                            }

                            //convert bookTime ke format jam dan menit
                            val dfBookTime = bookTime

                            //initials val perbedaan waktu dalam menit
                            val diffTime = (dfBookTime - dfTimeNow).toString().subSequence(2, 4)

                            //cek kondisi pesan lebih dari waktu jamBuka
                            if (dfTimeNow < dfBookTime) {

                                //cek kondisi pesan kurang dari 30mnt
                                if (dfTimeNow < dfBookTime - 0.3) {
                                    services.getJamBukaStatus(vendorId, idJamBuka, "Bearer $tokenAuth").enqueue(object : Callback<MitraResponse> {
                                        override fun onResponse(call: Call<MitraResponse>, response: Response<MitraResponse>) {
                                            if (response.isSuccessful) {
                                                if (response.body()!!.data[0].status == "tersedia") {
                                                    val mDialog = MaterialDialog.Builder(itemView.context as Activity)
                                                        .setTitle(itemView.context.getString(R.string.title_modal_choose_time))
                                                        .setMessage(itemView.context.getString(R.string.message_modal_choose_time))
                                                        .setCancelable(true)
                                                        .setPositiveButton(
                                                            itemView.context.getString(R.string.yes)
                                                        ) { dialogInterface, _ ->
                                                            changeStatus(bookingItem.idjam_buka, "kunci", tokenAuth)
                                                            val intent = Intent(itemView.context, FixPaymentActivity::class.java)
                                                                .apply {
                                                                    putExtra(FixPaymentActivity.vendorName, vendorNameOrdered)
                                                                    putExtra(FixPaymentActivity.vendorId, vendorId)
                                                                    putExtra(FixPaymentActivity.service, service)
                                                                    putExtra(FixPaymentActivity.price, price)
                                                                    putExtra(FixPaymentActivity.timeService, jamBuka)
                                                                    putExtra(FixPaymentActivity.idJamBuka, idJamBuka)
                                                                }
                                                            itemView.context.startActivity(intent)
                                                            dialogInterface.dismiss()
                                                        }
                                                        .setNegativeButton(
                                                            itemView.context.getString(R.string.no)
                                                        ) { dialogInterface, _ ->
                                                            dialogInterface.dismiss()
                                                        }
                                                        .build()
                                                    // Show Dialog
                                                    mDialog.show()

                                                } else {
                                                    Toasty.warning(
                                                        itemView.context,
                                                        "Jam Yang Anda Pesan Sedang Dipesan Pelanggan Lain",
                                                        Toasty.LENGTH_LONG
                                                    ).show()
                                                }
                                            }
                                        }

                                        override fun onFailure(call: Call<MitraResponse>, t: Throwable) {
                                            Toasty.error(itemView.context, R.string.try_again, Toasty.LENGTH_LONG).show()
                                        }
                                    })

                                } else {
                                    Toast.makeText(itemView.context, "Anda Terlambat $diffTime menit", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toasty.error(itemView.context, "Anda Melebihi Waktu Pemesanan ", Toasty.LENGTH_SHORT).show()
                                cvItemBooking.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.md_grey_600))
                                cvItemBooking.isEnabled = false
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

        private fun changeStatus(idJamBuka: String, status: String, tokenAuth: String) {
            val service = RetrofitClient().apiRequest().create(DataService::class.java)
            service.changeStatus(idJamBuka, status, "Bearer $tokenAuth").enqueue(object : Callback<DefaultResponse> {
                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                    if (response.isSuccessful) {
                        if (response.body()!!.status == "success") {
                        }
                    }
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toasty.error(itemView.context, R.string.try_again, Toasty.LENGTH_LONG).show()
                }
            })
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

    override fun getItemViewType(position: Int): Int {
        return position
    }
}