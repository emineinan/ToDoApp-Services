package com.example.todoapp.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentToDoBinding
import com.example.todoapp.databinding.ToDoOverlayViewBinding

class ToDoOverlayView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.to_do_overlay_view, this)
    }

}