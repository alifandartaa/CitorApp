package com.citor.app.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.citor.app.databinding.ItemNotificationsBinding

class NotificationsAdapter : RecyclerView.Adapter<NotificationsAdapter.NotificationItemViewHolder>() {

    private var listNotificationItem = ArrayList<NotifEntity>()

    fun setListNotificationItem(listNotificationItem: List<NotifEntity>?) {
        if (listNotificationItem == null) return
        this.listNotificationItem.clear()
        this.listNotificationItem.addAll(listNotificationItem)
    }

    class NotificationItemViewHolder(private val binding: ItemNotificationsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notificationItem: NotifEntity) {
            with(binding) {
                tvNotifications.text = notificationItem.message
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationItemViewHolder {
        val itemNotificationsBinding = ItemNotificationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationItemViewHolder(itemNotificationsBinding)
    }

    override fun onBindViewHolder(holder: NotificationItemViewHolder, position: Int) {
        val notificationItem = listNotificationItem[position]
        holder.bind(notificationItem)
    }

    override fun getItemCount(): Int {
        return listNotificationItem.size
    }

}