package com.kylecorry.kravtrainer.ui
import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kylecorry.kravtrainer.R
import com.kylecorry.kravtrainer.doTransaction
import com.kylecorry.kravtrainer.infrastructure.PunchGestureRepo

class MainActivity : AppCompatActivity() {

    // CONFIGURATION
    private val fragmentMap: Map<Int, Fragment> = mapOf(
        Pair(
            R.id.action_training,
            TrainingSelectFragment()
        ),
        Pair(
            R.id.action_stats,
            StatsFragment()
        )
    )

    private val defaultFragmentId =
        R.id.action_training

    private val permissions = listOf(Manifest.permission.BLUETOOTH)

    // END CONFIGURATION

    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!hasPermissions()){
            getPermission()
        }

        PunchGestureRepo.load()

        bottomNavigation = findViewById(R.id.bottom_navigation)

        syncFragmentWithSelection(bottomNavigation.selectedItemId)

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            syncFragmentWithSelection(item.itemId)
            true
        }

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        bottomNavigation.selectedItemId = savedInstanceState.getInt("page", defaultFragmentId)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("page", bottomNavigation.selectedItemId)
    }

    private fun syncFragmentWithSelection(selection: Int){
        switchFragment(fragmentMap[selection] ?: fragmentMap[defaultFragmentId])
    }

    private fun switchFragment(fragment: Fragment?){
        if (fragment == null) return
        supportFragmentManager.doTransaction {
            this.replace(R.id.fragment_holder, fragment)
        }
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount

        if (count == 0) {
            super.onBackPressed()
            //additional code
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        val granted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
        if (granted){
            // Do nothing yet
        } else {
            Toast.makeText(this, "Not all permissions were granted, some features may be broken", Toast.LENGTH_LONG).show()
        }
    }

    private fun hasPermissions(): Boolean {
        for (permission in permissions){
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false
            }
        }

        return true
    }

    private fun getPermission(){
        ActivityCompat.requestPermissions(this,
            permissions.toTypedArray(),
            1
        )
    }

}
