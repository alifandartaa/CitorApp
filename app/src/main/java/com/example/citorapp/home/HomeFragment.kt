package com.example.citorapp.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.citorapp.R
import com.example.citorapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private var bindingHome: FragmentHomeBinding? = null
    private val binding get() = bindingHome!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
//        val root = inflater.inflate(R.layout.fragment_home, container, false)
////        val textView: TextView = root.findViewById(R.id.text_home)
////        homeViewModel.text.observe(viewLifecycleOwner, Observer {
////            textView.text = it
////        })

        bindingHome = FragmentHomeBinding.inflate(inflater, container, false)

        binding.btnCari.setOnClickListener(){
            val intent = Intent(this@HomeFragment.requireContext(), PelayananActivity::class.java)
            startActivity(intent)
        }
        return binding.root


    }
}