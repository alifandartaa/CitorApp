package com.citor.app.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.citor.app.databinding.FragmentHistoryBinding
import com.citor.app.utils.Constants
import com.citor.app.utils.MySharedPreferences

class HistoryFragment : Fragment() {

    private lateinit var historyBinding: FragmentHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var myPreferences: MySharedPreferences

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
        historyBinding.chipCatProgress.isChecked = true
        myPreferences = MySharedPreferences(requireActivity())
        val idUser = myPreferences.getValue(Constants.USER_ID).toString()
        val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()
        if (activity != null) {
            historyAdapter = HistoryAdapter()
            setupListHistory(idUser, tokenAuth)
        }
    }

    private fun setupListHistory(idUser: String, tokenAuth: String) {
        val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(HistoryViewModel::class.java)

        viewModel.loadListHistoryFromDatabase(idUser, tokenAuth)

        viewModel.getListDataHistory().observe(requireActivity(), { historyItems ->

            val listProgress = ArrayList<HistoryEntity>()
            val listDone = ArrayList<HistoryEntity>()
            if (historyItems != null) {
                historyItems.forEach {
                    if (it.status == "berjalan") {
                        listProgress.add(it)
                    } else if (it.status == "selesai") {
                        listDone.add(it)
                    }
                }

                historyAdapter.setListHistoryItem(listProgress)
                historyAdapter.notifyDataSetChanged()
                with(historyBinding.rvHistory) {
                    layoutManager = LinearLayoutManager(activity)
                    itemAnimator = DefaultItemAnimator()
                    setHasFixedSize(true)
                    adapter = historyAdapter
                }

                historyBinding.cgStatusHistory.setOnCheckedChangeListener { group, checkedId ->
                    when (checkedId) {
                        historyBinding.chipCatProgress.id -> {
                            historyAdapter.setListHistoryItem(listProgress)
                            historyAdapter.notifyDataSetChanged()
                        }
                        historyBinding.chipCatDone.id -> {
                            historyAdapter.setListHistoryItem(listDone)
                            historyAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        })


    }


}