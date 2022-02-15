package com.example.todoapp

import android.content.res.ColorStateList
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.setDivider(@DrawableRes drawableRes: Int) {
    val divider = DividerItemDecoration(
        this.context,
        DividerItemDecoration.VERTICAL
    )
    val drawable = ContextCompat.getDrawable(
        this.context,
        drawableRes
    )
    drawable?.let {
        divider.setDrawable(it)
        addItemDecoration(divider)
    }
}

@BindingAdapter("android:changeHeaderText")
fun changeHeaderText(view: TextView, isActive: Boolean) {
    if (isActive) {
        view.setText(R.string.header_active)
    } else {
        view.setText(R.string.header_done)
    }
}

@BindingAdapter("android:changeColor")
fun changeColor(view: TextView, isActive: Boolean) {
    if (isActive) {
        view.setTextColor(ContextCompat.getColor(view.context, R.color.lightGray))
    } else {
        view.setTextColor(ContextCompat.getColor(view.context, R.color.darkGray))
    }
}

@BindingAdapter("android:changeIconColor")
fun changeIconColor(view: ImageView, isActive: Boolean) {
    if (isActive) {
        view.setColorFilter(ContextCompat.getColor(view.context, R.color.darkGray))
    } else {
        view.setColorFilter(ContextCompat.getColor(view.context, R.color.lightGray))
        Toast.makeText(view.context, "Rengim değişti pasifim", Toast.LENGTH_SHORT).show()
    }
}

@BindingAdapter("android:changeTickIconColor")
fun changeTickIconColor(view: CheckBox, isActive: Boolean) {
    if (isActive) {
        view.buttonTintList =
            ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.green))
    } else {
        view.buttonTintList =
            ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.lightGray))
    }
}
