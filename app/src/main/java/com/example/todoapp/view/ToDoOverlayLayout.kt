package com.example.todoapp.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout

class ToDoOverlayLayout : ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var listener: ((activate: Boolean) -> Unit)? = null

    fun setListener(listener: (activate: Boolean) -> Unit) {
        this.listener = listener
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) listener?.invoke(true)
        if (event.action == MotionEvent.ACTION_OUTSIDE) listener?.invoke(false)
        return super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) listener?.invoke(true)
        if (event.action == MotionEvent.ACTION_OUTSIDE) listener?.invoke(false)
        return super.onInterceptTouchEvent(event)
    }

}