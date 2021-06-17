package com.example.citorapp.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.citorapp.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var historyBinding: FragmentHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        historyBinding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        return historyBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {
            historyAdapter = HistoryAdapter()
            setupListHistory()
        }
    }

    private fun setupListHistory() {
        val listDummy = ArrayList<HistoryEntity>()
        val data1 = HistoryEntity("Suka Makmur", "progress")
        val data2 = HistoryEntity("Damai Jaya", "progress")
        val data3 = HistoryEntity("Damai Jaya", "progress")
        val data4 = HistoryEntity("Damai Jaya", "progress")
        val data5 = HistoryEntity("Damai Jaya", "progress")
        val data6 = HistoryEntity("Damai Jaya", "progress")
        val data7 = HistoryEntity("Damai Jaya", "progress")
        val data8 = HistoryEntity("Damai Jaya", "progress")
        listDummy.add(data1)
        listDummy.add(data2)
        listDummy.add(data3)
        listDummy.add(data4)
        listDummy.add(data5)
        listDummy.add(data6)
        listDummy.add(data7)
        listDummy.add(data8)
        historyAdapter.setListHistoryItem(listDummy)
//        showLoading(false)
        historyAdapter.notifyDataSetChanged()

        with(historyBinding.rvHistory) {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
            setHasFixedSize(true)
            adapter = historyAdapter
        }
    }


}