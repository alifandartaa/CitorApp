package com.example.citorapp.home.pelayanan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.citorapp.R
import com.example.citorapp.databinding.ActivityPelayananBinding

class PelayananActivity : AppCompatActivity() {

    private lateinit var bindingPelayanan: ActivityPelayananBinding
    private var list: ArrayList<PelayananModel> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingPelayanan = ActivityPelayananBinding.inflate(layoutInflater)
        setContentView(bindingPelayanan.root)


        bindingPelayanan.rvLocation.setHasFixedSize(true)
        list.addAll(PelayananData.listCuci)

        bindingPelayanan.rvLocation.layoutManager = LinearLayoutManager(this)
        val adapterSuci = PelayananAdapter(list)

        bindingPelayanan.rvLocation.adapter = adapterSuci
    }
}