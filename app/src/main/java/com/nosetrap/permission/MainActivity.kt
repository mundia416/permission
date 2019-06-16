package com.nosetrap.permission

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.florent37.runtimepermission.RuntimePermission

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RuntimePermission(this).onAccepted {
            Log.d("sfs", "fsf")
        }.onDenied {
            Log.d("sfs", "fsf")
        }.onForeverDenied {
            Log.d("sfs", "fsf")
        }.ask { }

    }

    override fun onResume() {
        super.onResume()
        Log.d("sfs", "fsf")
    }
}
