package com.citor.app.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.citor.app.databinding.ItemHistoryBinding

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryItemViewHolder>() {

    private var listHistoryItem = ArrayList<HistoryEntity>()

    fun setListHistoryItem(listHistoryItem: List<HistoryEntity>?) {
        if (listHistoryItem == null) return
        this.listHistoryItem.clear()
        this.listHistoryItem.addAll(listHistoryItem)
    }

    class HistoryItemViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(historyItem: HistoryEntity) {
            with(binding) {
                if (historyItem.status == "berjalan") {
                    tvHistory.text = "Pencucian motor pada ${historyItem.nama_mitra} sedang berjalan"
                } else if (historyItem.status == "selesai") {
                    tvHistory.text = "Pencucian motor pada ${historyItem.nama_mitra} telah selesai"
                }
            }
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