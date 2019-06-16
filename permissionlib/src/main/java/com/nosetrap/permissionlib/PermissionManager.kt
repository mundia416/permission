package com.nosetrap.permissionlib

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import androidx.annotation.RequiresApi


/**
 * main class to deal with permissions. it can be used in two ways,either instantiate it or access
 * te methods statically
 */
class PermissionManager(private val activity: Activity) {

    fun canDrawOverlays(): Boolean{
        return canDrawOverlays(activity)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun requestDrawOverlays(requestCode: Int){
        requestDrawOverlays(activity,requestCode)
    }

    fun requestAccServicePermission(requestCode: Int){
        requestAccServicePermission(activity, requestCode)
    }

    fun isAccServicePermissionGranted(accClassCanonicalName: String): Boolean{
        return isAccServicePermissionGranted(activity,accClassCanonicalName)
    }

    fun isGranted(permission: String): Boolean{
        return isGranted(activity,permission)
    }




companion object {

    /**
     * check if a permission is granted
     */
    fun isGranted(context: Context,permission: String): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.checkSelfPermission(permission) == PERMISSION_GRANTED
        }else{
            true
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun requestDrawOverlays(activity: Activity, requestCode: Int){
        activity.startActivityForResult( Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + activity.packageName)),
                requestCode)
    }

    /**
     * request accessiblity service permission
     */
    fun requestAccServicePermission(activity: Activity,requestCode: Int){
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        activity.startActivityForResult(intent, requestCode)

    }

    /**
     * is accessiblity service Permission granted
     * @param accClassCanonicalName canonical name of the accessiblity class retrieved with AccessiblityClass::class.java.canonicalName
     */
    fun isAccServicePermissionGranted(context:Context, accClassCanonicalName: String): Boolean{
                val accServiceId = context.packageName+"/"+ accClassCanonicalName

                var enabled = 0
                try{
                    enabled = Settings.Secure.getInt(context.applicationContext.contentResolver,Settings.Secure.ACCESSIBILITY_ENABLED)

                }catch (e: Settings.SettingNotFoundException){

                }

                val colonSplitter: TextUtils.SimpleStringSplitter = TextUtils.SimpleStringSplitter(':')

                if(enabled == 1){
                    val settingsValue = Settings.Secure.getString(context.applicationContext.contentResolver,
                            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)

                    if(settingsValue != null){
                        colonSplitter.setString(settingsValue)

                        while(colonSplitter.hasNext()){
                            val accService = colonSplitter.next()

                            if(accService == accServiceId){
                                return true
                            }
                        }
                    }
                }

                return false
            }

    fun canDrawOverlays(context: Context): Boolean {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else {
            true
        }
        //todo android 26 has a bug which is fixed by a security patch which needs to be downloaded
        //todo to update the OS. figure out a workaround to this bug. right now the correct value
        //todo of this on android 26 can only be retrieved if the app is restarted
        /* if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                return false
            }else{
                //there is a bug with android 26 so here is a workaround to check the permission
                //the workaround is to add an invisible overlay
                try{
                    val windowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager

                    //window manager can be null
                    if(windowManager == null) return false

                    val invisibleView = View(activity)
                    val params = WindowManager.LayoutParams(WRAP_CONTENT, WRAP_CONTENT,
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) TYPE_APPLICATION_OVERLAY
                                    or FLAG_LAYOUT_NO_LIMITS
                            else TYPE_SYSTEM_ERROR, FLAG_NOT_TOUCH_MODAL or
                            FLAG_NOT_FOCUSABLE or FLAG_LAYOUT_NO_LIMITS,
                            PixelFormat.TRANSLUCENT)
                invisibleView.layoutParams = params
                    windowManager.addView(invisibleView,params)
                    windowManager.removeView(invisibleView)
                    return true
                }catch(e: Exception){
                   e.printStackTrace()
                }
                return false
            }
        }*/
    }
}


}