package com.example.todoapp

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.Constants.Companion.INTENT_COMMAND_NOTE
import com.example.todoapp.databinding.ActivityPermissionBinding
import com.example.todoapp.util.drawOverOtherAppsEnabled
import com.example.todoapp.util.startToDoService

const val PERMISSION_REQUEST_CODE = 1

class PermissionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPermissionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonOpenSettings.setOnClickListener {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                finish()
            } else {
                requestPermission()
            }
        }
    }

    private fun showDialog(titleText: String, messageText: String) {
        with(AlertDialog.Builder(this)) {
            title = titleText
            setMessage(messageText)
            setPositiveButton(R.string.common_ok) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        try {
            startActivityForResult(intent, PERMISSION_REQUEST_CODE)
        } catch (e: Exception) {
            showDialog(
                getString(R.string.permission_error_title),
                getString(R.string.permission_error_text)
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Don't check for resultCode == Activity.RESULT_OK because the overlay activity
        // is closed with the back button and so the RESULT_CANCELLED is always received.
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (drawOverOtherAppsEnabled()) {
                // The permission has been granted.
                // Resend the last command - we have only one, so no additional logic needed.
                startToDoService(INTENT_COMMAND_NOTE)
                finish()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}