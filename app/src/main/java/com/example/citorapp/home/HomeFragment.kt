package com.example.citorapp.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.citorapp.databinding.FragmentHomeBinding
import com.example.citorapp.home.pelayanan.PelayananActivity

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

        binding.btnSearch.setOnClickListener(){
            val intent = Intent(this@HomeFragment.requireContext(), PelayananActivity::class.java)
            startActivity(intent)
        }

        binding.btnService.setOnClickListener(){

        }

        binding.btnRating.setOnClickListener(){

        }

        binding.btnLocation.setOnClickListener(){

        }

        binding.btnVoucher.setOnClickListener(){

        }
        return binding.root


    }
}