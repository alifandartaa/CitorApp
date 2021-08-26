package com.citor.app.history

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewFragment
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.citor.app.MainActivity
import com.citor.app.R
import com.citor.app.databinding.ItemHistoryBinding
import com.citor.app.retrofit.DataService
import com.citor.app.retrofit.RetrofitClient
import com.citor.app.retrofit.response.DefaultResponse
import com.citor.app.utils.Constants
import com.citor.app.utils.MySharedPreferences
import com.google.android.material.bottomsheet.BottomSheetDialog
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryItemViewHolder>() {

    private var listHistoryItem = ArrayList<HistoryEntity>()

    fun setListHistoryItem(listHistoryItem: List<HistoryEntity>?) {
        if (listHistoryItem == null) return
        this.listHistoryItem.clear()
        this.listHistoryItem.addAll(listHistoryItem)
    }

    class HistoryItemViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var myPreferences: MySharedPreferences
        private val dialog = BottomSheetDialog(itemView.context)

        @SuppressLint("SetTextI18n")
        fun bind(historyItem: HistoryEntity) {
            myPreferences = MySharedPreferences(itemView.context)
            binding.tvTime.text = historyItem.timestamp
            if (historyItem.status == "selesai") {
                binding.tvHistory.text = "Pencucian motor pada ${historyItem.nama_mitra} telah selesai"
                binding.tvDetail.visibility = View.VISIBLE
                itemView.setOnClickListener {
                    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.setContentView(R.layout.bottom_sheet_history_detail)

                    dialog.findViewById<TextView>(R.id.tv_detail_kode)?.text = historyItem.kode_pemesanan
                    dialog.findViewById<TextView>(R.id.tv_detail_nama_mitra)?.text = historyItem.nama_mitra
                    dialog.findViewById<TextView>(R.id.tv_detail_layanan)?.text = historyItem.layanan
                    dialog.findViewById<TextView>(R.id.tv_detail_harga)?.text = "Rp ${historyItem.harga}"
                    dialog.findViewById<TextView>(R.id.tv_detail_timestamp)?.text = historyItem.timestamp

                    if (historyItem.rating == "rated") {
                        dialog.findViewById<LinearLayout>(R.id.ll_rating)?.visibility = View.GONE
                    } else if (historyItem.rating == "belum") {
                        dialog.findViewById<LinearLayout>(R.id.ll_rating)?.visibility = View.VISIBLE
                    }

                    val btnSimpan = dialog.findViewById<TextView>(R.id.btn_simpan)
                    val ratingNumber = dialog.findViewById<TextView>(R.id.rating_number)
                    val tvValueUlasan = dialog.findViewById<TextView>(R.id.tv_value_ulasan)
                    val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()

                    fun isDataFilled(): Boolean {
                        if (ratingNumber?.text.toString() == "0.0") {
                            Toasty.error(itemView.context, "Harap Beri Penilaian Terlebih Dahulu!", Toasty.LENGTH_LONG).show()
                            return false
                        }
                        return true
                    }

                    btnSimpan?.setOnClickListener {
                        val nilai = ratingNumber?.text.toString()
                        val ulasan = tvValueUlasan?.text.toString()
                        if (isDataFilled()) {
                            insertRating(historyItem.idpemesanan, nilai, ulasan, tokenAuth)
                        }
                    }
                    dialog.show()
                    dialog.findViewById<RatingBar>(R.id.ratingBar)?.onRatingBarChangeListener =
                        RatingBar.OnRatingBarChangeListener { p0, p1, p2 ->
                            ratingNumber?.text = p1.toString()
                        }
                }
            } else if (historyItem.status == "berjalan") {
                binding.tvHistory.text = "Pencucian motor pada ${historyItem.nama_mitra} sedang berjalan"
                itemView.isClickable = false
                binding.tvDetail.visibility = View.GONE
            }

        }

        private fun insertRating(idpemesanan: String, nilai: String, ulasan: String, tokenAuth: String) {
            val service = RetrofitClient().apiRequest().create(DataService::class.java)
            service.insertRating(idpemesanan, nilai, ulasan, "Bearer $tokenAuth").enqueue(object : Callback<DefaultResponse> {
                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                    if (response.isSuccessful) {
                        if (response.body()!!.status == "success") {
                            Toasty.success(itemView.context, "Terima Kasih Atas Penilaian Anda", Toasty.LENGTH_LONG).show()
                            itemView.setOnClickListener(null)
                            dialog.dismiss()
                            itemView.context.startActivity(Intent(itemView.context, MainActivity::class.java))
//                            val manager: FragmentManager = (itemView.context as AppCompatActivity).supportFragmentManager
//                            manager.findFragmentById(R.id.navigation_history)

//                            (itemView.context as MainActivity).supportFragmentManager.beginTransaction()
//                                .add(R.id.container, HistoryFragment(), "OptionsFragment").commit()
                        } else {
                            Toasty.error(itemView.context, R.string.try_again, Toasty.LENGTH_LONG).show()
                        }
                    } else {
                        Toasty.error(itemView.context, response.message(), Toasty.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toasty.error(itemView.context, R.string.try_again, Toasty.LENGTH_LONG).show()
                }

            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        val itemHistoryBinding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryItemViewHolder(itemHistoryBinding)
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        val historyItem = listHistoryItem[position]
        holder.bind(historyItem)
    }

    override fun getItemCount(): Int {
        return listHistoryItem.size
    }
}