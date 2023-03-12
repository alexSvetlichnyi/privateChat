package com.google.firebase.udacity.friendlychat

import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BaseObservable
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ItemViewModel(message: FriendlyMessage) : BaseObservable() {
    val imageVisibility = ObservableInt(View.GONE)
    val textVisibility = ObservableInt(View.VISIBLE)
    val backgroundColor = ObservableInt(View.VISIBLE)
    val isCurrentUser= ObservableBoolean()
    var imageUrl: String
    val messageText = ObservableField<String>()
    val messageTime = ObservableField<String>()
    val author = ObservableField<String>()

    init {
        textVisibility.set(if (message.photoUrl.isNullOrEmpty()) View.VISIBLE else View.GONE)
        imageVisibility.set(if (message.photoUrl != null) View.VISIBLE else View.GONE)
        backgroundColor.set(if (message.isCurrentUser) R.color.white else R.color.promo_green1)
        isCurrentUser.set(message.isCurrentUser)
        imageUrl = message.photoUrl ?: ""
        author.set(message.name)
        messageText.set(message.text)
        messageTime.set(message.time)
    }

    companion object {
        @BindingAdapter("imageUrl")
        @JvmStatic
        fun setImageSource(imageView: ImageView, imageUrl: String) {
            Glide.with(imageView).asBitmap().load(imageUrl).into(imageView)
        }

        /*@BindingAdapter("app:background")
        @JvmStatic
        fun setBackground(view: View, colorId: Int) {
            view.setBackgroundColor(ContextCompat.getColor(view.context, colorId))
        }

        @BindingAdapter("adapter")
        @JvmStatic
        fun setAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
            adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    super.onItemRangeInserted(positionStart, itemCount)
                    recyclerView.smoothScrollToPosition(positionStart)
                }
            })
            recyclerView.adapter = adapter
        }*/
    }
}