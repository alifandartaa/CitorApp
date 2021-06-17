package com.example.citorapp.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.citorapp.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var notificationsBinding: FragmentNotificationsBinding
    private lateinit var notificationsAdapter: NotificationsAdapter

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

        if (activity != null) {
            notificationsAdapter = NotificationsAdapter()
            setupListNotification()
        }
    }

    private fun setupListNotification() {
        val listDummy = ArrayList<NotifEntity>()
        val data1 = NotifEntity("point", "10")
        val data2 = NotifEntity("point", "20")
        val data3 = NotifEntity("point", "30")
        val data4 = NotifEntity("point", "40")
        val data5 = NotifEntity("point", "50")
        val data6 = NotifEntity("point", "60")
        val data7 = NotifEntity("point", "70")
        val data8 = NotifEntity("point", "80")
        listDummy.add(data1)
        listDummy.add(data2)
        listDummy.add(data3)
        listDummy.add(data4)
        listDummy.add(data5)
        listDummy.add(data6)
        listDummy.add(data7)
        listDummy.add(data8)
        notificationsAdapter.setListNotificationItem(listDummy)
//        showLoading(false)
        notificationsAdapter.notifyDataSetChanged()

        with(notificationsBinding.rvNotifications) {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
            setHasFixedSize(true)
            adapter = notificationsAdapter
        }
    }
}