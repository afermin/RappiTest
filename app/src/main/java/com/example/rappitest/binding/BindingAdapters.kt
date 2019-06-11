package com.example.rappitest.binding

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide


/**
 * Data Binding adapters specific to the app.
 */
object BindingAdapters {
    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }
}


@BindingAdapter("scrollListener")
fun RecyclerView.setScrollListener(listener: RecyclerView.OnScrollListener) {
    clearOnScrollListeners()
    addOnScrollListener(listener)
}
