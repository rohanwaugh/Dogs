package com.android.dogs.view

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.android.dogs.R
import com.android.dogs.utils.PERMISSION_SEND_SMS
import kotlinx.android.synthetic.main.activity_main.*

/* This is MainActivity of the application. */
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(this, R.id.fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    /* This function will check if application has access to send SMS.
    *  If not then it will show Request Permission Rational to the user.
    *  If user click Yes on rational then he will be shown Request Permission popup
    * */
    fun checkSmsPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.SEND_SMS
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.alert_title))
                    .setMessage(getString(R.string.alert_message))
                    .setPositiveButton(getString(R.string.alert_yes_button_text)) { _: DialogInterface, _: Int ->
                        requestSmsPermission()
                    }
                    .setNegativeButton(getString(R.string.alert_no_button_text)) { _: DialogInterface, _: Int ->
                        notifyDetailFragment(false)
                    }
                    .show()
            } else {
                requestSmsPermission()
            }
        } else {
            notifyDetailFragment(true)
        }

    }

    /* This will show User request Permission dialog. */
    private fun requestSmsPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS),
            PERMISSION_SEND_SMS)
    }

    /* This is request callback method. Here we can determine whether user
    *  has granted permission or not.
    * */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){

            PERMISSION_SEND_SMS ->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    notifyDetailFragment(true)
                }else{
                    notifyDetailFragment(false)
                }
            }
        }
    }

    /* This function will notify DetailsFragment weather user has granted permission
    *  or not.
    * */
    private fun notifyDetailFragment(permissionGranted: Boolean) {
        val activeFragment = fragment.childFragmentManager.primaryNavigationFragment
        if(activeFragment is DetailsFragment){
            activeFragment.onPermissionResult(permissionGranted)
        }
    }

}
