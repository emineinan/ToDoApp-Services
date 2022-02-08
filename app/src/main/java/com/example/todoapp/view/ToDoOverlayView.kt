package com.example.todoapp.view

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.todoapp.data.model.ToDoData
import com.example.todoapp.databinding.ToDoOverlayViewBinding
import com.example.todoapp.viewmodel.ToDoViewModel

class ToDoOverlayView(private val context: Context) {
    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val layoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private lateinit var binding: ToDoOverlayViewBinding
    private val toDoViewModel = ToDoViewModel(context as Application)

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

        binding.root.setListener {
            if (it) {
                enableKeyboard()
            } else {
                disableKeyboard()
            }
        }

        binding.buttonCancel.setOnClickListener {
            closeOverlay()
        }

        binding.buttonAdd.setOnClickListener {
            addTaskToDatabase()
        }

        return binding.root
    }

    private fun addTaskToDatabase() {
        val title = binding.editTextOverlayTitle.text.toString()
        val description = binding.editTextOverlayDescription.text.toString()

        checkInputFields(title, description)
        clearInputFields()
    }

    private fun checkInputFields(title: String, description: String) {
        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(context, "Please fill in the fields", Toast.LENGTH_LONG).show()
        } else {
            val newTask = ToDoData(0, true, title, description)
            toDoViewModel.insertData(newTask)
        }
    }

    private fun clearInputFields() {
        binding.editTextOverlayTitle.text.clear()
        binding.editTextOverlayDescription.text.clear()
    }

    private fun enableKeyboard() {
        if (windowParams.flags and WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE != 0) {
            windowParams.flags =
                windowParams.flags and WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE.inv()
            update()
        }
    }


    private fun disableKeyboard() {
        if (windowParams.flags and WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE == 0) {
            windowParams.flags = windowParams.flags or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            update()
        }
    }


    private fun update() {
        windowManager.updateViewLayout(binding.root, windowParams)
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