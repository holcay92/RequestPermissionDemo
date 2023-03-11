package com.example.requestpermissiondemo

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    // this is how you register for a permission
    private val cameraResultLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
                // Permission is granted. Continue the action or workflow in your app.

            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
                // Explain to the user that the feature is unavailable because the features requires a permission that the user has denied.
            }
        }
    private val cameraAndLocationResultLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach {
                val permission = it.key
                val isGranted = it.value
                if (isGranted) {
                    if (permission == Manifest.permission.CAMERA) {
                        Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
                    } else if (permission == Manifest.permission.ACCESS_FINE_LOCATION) {
                        Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    if (permission == Manifest.permission.CAMERA) {
                        Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
                    } else if (permission == Manifest.permission.ACCESS_FINE_LOCATION) {
                        Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonCameraPermission =
            findViewById<android.widget.Button>(R.id.btnCameraPermission)
        buttonCameraPermission.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(
                    Manifest.permission.CAMERA
                )
            ) {
                showRationaleDialog(
                    "Camera Permission",
                    "This app needs camera permission to take pictures."
                )
            } else {
                // this is how you request a permission
                cameraAndLocationResultLauncher.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )

            }
        }


    }

    private fun showRationaleDialog(
        title: String,
        message: String,
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                // Request the permission
                dialog.dismiss()
            }
        builder.create().show()
    }
}