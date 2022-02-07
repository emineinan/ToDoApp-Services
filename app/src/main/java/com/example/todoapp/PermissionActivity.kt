package com.example.todoapp

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.example.todoapp.databinding.ActivityPermissionBinding
import com.example.todoapp.service.INTENT_COMMAND_NOTE

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
                startFloatingService(INTENT_COMMAND_NOTE)
                finish()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}