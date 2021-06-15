package com.example.citorapp.home.pelayanan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.citorapp.R

class PelayananAdapter(private val listCuci: ArrayList<PelayananModel>): RecyclerView.Adapter<PelayananAdapter.listCuciHolderView>(){

    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PelayananAdapter.listCuciHolderView {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_tempat, parent, false)
        return listCuciHolderView(view)
    }

    override fun onBindViewHolder(holder: PelayananAdapter.listCuciHolderView, position: Int) {
        val list = listCuci[position]

        holder.tvNama.text = list.name
        holder.tvAlamat.text = list.address
        holder.tvStatus.text = list.status

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listCuci[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int {
        return listCuci.size
    }

    inner class listCuciHolderView(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvNama: TextView = itemView.findViewById(R.id.tv_name)
        var tvAlamat: TextView = itemView.findViewById(R.id.tv_address)
        var tvStatus: TextView = itemView.findViewById(R.id.tv_status)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: PelayananModel)
    }
}