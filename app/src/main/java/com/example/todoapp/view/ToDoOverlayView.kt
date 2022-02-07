package com.example.todoapp.view

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.todoapp.databinding.ToDoOverlayViewBinding
import android.content.res.Resources
import android.widget.Toast

class ToDoOverlayView(private val context: Context) {
    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val layoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private lateinit var binding: ToDoOverlayViewBinding

    private val windowParams = WindowManager.LayoutParams(
        0,
        0,
        0,
        0,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        },
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
        PixelFormat.TRANSLUCENT
    )

    private fun calculateSizeAndPosition(
        params: WindowManager.LayoutParams,
        widthInDp: Int,
        heightInDp: Int
    ) {
        val displayMetrics = Resources.getSystem().displayMetrics
        params.gravity = Gravity.START or Gravity.TOP
        params.width = (widthInDp * displayMetrics.density).toInt()
        params.height = (heightInDp * displayMetrics.density).toInt()
        params.x = (displayMetrics.widthPixels - params.width) / 2
        params.y = (displayMetrics.heightPixels - params.height) / 2
    }

    private fun initWindowParams() {
        calculateSizeAndPosition(
            windowParams,
            300,
            300
        )
    }

    private fun initWindow(): ConstraintLayout {
        binding = ToDoOverlayViewBinding.inflate(layoutInflater)

        binding.buttonCancel.setOnClickListener {
            closeOverlay()
        }

        binding.buttonAdd.setOnClickListener {
            Toast.makeText(context, "Button is working", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    init {
        initWindowParams()
        initWindow()
    }

    fun openOverlay() {
        windowManager.addView(binding.root, windowParams)
    }

    private fun closeOverlay() {
        windowManager.removeView(binding.root)
    }
}