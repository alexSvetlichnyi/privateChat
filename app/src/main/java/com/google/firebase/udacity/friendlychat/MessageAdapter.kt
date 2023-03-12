package com.google.firebase.udacity.friendlychat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.ItemViewHolder>() {

    private var items = mutableListOf<FriendlyMessage>()

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val viewModel = ItemViewModel(items[position])
        val binding = holder.dataBinder
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val dataBinding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), R.layout.item_message, parent, false)
        return ItemViewHolder(dataBinding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addNewMessage(friendlyMessage: FriendlyMessage) {
        items.add(friendlyMessage)
        notifyItemInserted(items.size - 1)
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    class ItemViewHolder(var dataBinder: ViewDataBinding) : RecyclerView.ViewHolder(dataBinder.root)
}