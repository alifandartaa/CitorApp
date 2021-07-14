package com.citor.app.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.citor.app.R
import com.citor.app.databinding.FragmentHomeBinding
import com.citor.app.home.searchVendor.SearchVendorActivity
import com.citor.app.utils.Constants
import com.citor.app.utils.MySharedPreferences
import com.google.firebase.messaging.FirebaseMessaging
import org.imaginativeworld.whynotimagecarousel.CarouselItem
import org.imaginativeworld.whynotimagecarousel.ImageCarousel

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var myPreferences: MySharedPreferences

    private var bindingHome: FragmentHomeBinding? = null
    private val binding get() = bindingHome!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        bindingHome = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myPreferences = MySharedPreferences(requireContext())
        FirebaseMessaging.getInstance().subscribeToTopic("notif")

        val userName = myPreferences.getValue(Constants.USER_NAMA).toString()
        val point = myPreferences.getValue(Constants.USER_POIN).toString()

        binding.tvName.text = userName
        binding.tvPoint.text = point

        firstImageSlider()

        secondImageSlider()


        binding.btnService.setOnClickListener() {
            val intent = Intent(this@HomeFragment.requireContext(), SearchVendorActivity::class.java)
            startActivity(intent)
        }

        binding.btnRating.setOnClickListener() {

        }

        binding.btnLocation.setOnClickListener() {

        }

        binding.btnVoucher.setOnClickListener() {

        }
    }

    private fun firstImageSlider() {
        //first image slider
        val firstCarousel: ImageCarousel = requireView().findViewById(R.id.carousel_view_first)
        val firstList = mutableListOf<CarouselItem>()
        //first image
        firstList.add(
            CarouselItem(
                imageDrawable = R.drawable.img_top_ads
            )
        )
        //second image
        firstList.add(
            CarouselItem(
                imageDrawable = R.drawable.img_main_service
            )
        )
        //third image slider
        firstList.add(
            CarouselItem(
                imageDrawable = R.drawable.img_washing_reguler
            )
        )
        //fourth image slider
        firstList.add(
            CarouselItem(
                imageDrawable = R.drawable.img_washing_snow
            )
        )
        //fifth image slider
        firstList.add(
            CarouselItem(
                imageDrawable = R.drawable.img_washing_service
            )
        )
        firstCarousel.addData(firstList)
    }

    private fun secondImageSlider() {
        //second image slider
        val secondCarousel: ImageCarousel = requireView().findViewById(R.id.carousel_view_second)
        val secondList = mutableListOf<CarouselItem>()
        //first image
        secondList.add(
            CarouselItem(
                imageDrawable = R.drawable.img_top_ads
            )
        )
        //second image
        secondList.add(
            CarouselItem(
                imageDrawable = R.drawable.img_main_service
            )
        )
        //third image slider
        secondList.add(
            CarouselItem(
                imageDrawable = R.drawable.img_washing_reguler
            )
        )
        //fourth image slider
        secondList.add(
            CarouselItem(
                imageDrawable = R.drawable.img_washing_snow
            )
        )
        //fifth image slider
        secondList.add(
            CarouselItem(
                imageDrawable = R.drawable.img_washing_service
            )
        )
        secondCarousel.addData(secondList)
    }
}