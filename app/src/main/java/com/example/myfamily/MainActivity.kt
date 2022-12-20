package com.example.myfamily

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    //array of permissions
    val permission = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.CAMERA,
        Manifest.permission.READ_CONTACTS
    )
    val permissionCode = 47
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //as soon as main_activity open , ask for permissions that is required
        askForPermission()

        //bottom nav-bar
        bottom_navbar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> inflateFragment(HomeFragment.newInstance())
                R.id.nav_dashboard -> inflateFragment(MapsFragment())
                R.id.nav_gaurd -> inflateFragment(GuardFragment.newInstance())
                R.id.nav_profile -> inflateFragment(ProfileFragment.newInstance())

            }
            true
        }
        //home fragment will be set as default
        bottom_navbar.selectedItemId = R.id.nav_home
    }

    private fun askForPermission() {
        //ActivityCompat is a class that calls requestPermissions function. It will popup a dialog for giving the grant to access the location
        ActivityCompat.requestPermissions(this, permission, permissionCode)
    }

    //this is when we grant some permission after that what should happen like we granted the permission for camera
    // then after that open the camera that will happen in this function
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionCode) {
            if (allPermissionGranted()) {
                //openCamera()
            } else {
                Toast.makeText(this, "Permission is not granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openCamera() {
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        startActivity(intent)
    }

    //check if all permission granted or not that we have given in array of permissions
    private fun allPermissionGranted(): Boolean {
        for (item in permission) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    item
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }
    //function for inflating fragment
    private fun inflateFragment(newInstance: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_contaier, newInstance)
        transaction.commit()
    }
}