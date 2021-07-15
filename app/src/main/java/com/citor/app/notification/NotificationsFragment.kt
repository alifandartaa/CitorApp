package com.citor.app.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.citor.app.databinding.FragmentNotificationsBinding
import com.citor.app.utils.Constants
import com.citor.app.utils.MySharedPreferences

class NotificationsFragment : Fragment() {

    private lateinit var notificationsBinding: FragmentNotificationsBinding
    private lateinit var notificationsAdapter: NotificationsAdapter
    private lateinit var myPreferences: MySharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        notificationsBinding = FragmentNotificationsBinding.inflate(layoutInflater, container, false)
        return notificationsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myPreferences = MySharedPreferences(requireActivity())
        val idUser = myPreferences.getValue(Constants.USER_ID).toString()
        val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()
        if (activity != null) {
            notificationsAdapter = NotificationsAdapter()
            setupListNotification(idUser, tokenAuth)
        }
    }

    private fun setupListNotification(idUser: String, tokenAuth: String) {

        val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(NotificationsViewModel::class.java)
        viewModel.loadListNotificationfromDatabase(idUser, tokenAuth)

        viewModel.getListDataNotification().observe(requireActivity(), {notificationItem ->

            val listNotif = ArrayList<NotifEntity>()

            if(notificationItem != null){
                notificationItem.forEach {
                    listNotif.add(it)
                }

                notificationsAdapter.setListNotificationItem(listNotif)
                notificationsAdapter.notifyDataSetChanged()

                with(notificationsBinding.rvNotifications) {
                    layoutManager = LinearLayoutManager(activity)
                    itemAnimator = DefaultItemAnimator()
                    setHasFixedSize(true)
                    adapter = notificationsAdapter
                }
            }
        })

    }
}