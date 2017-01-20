package com.bestv.vrcinema;


import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.bestv.vrcinema.util.BrightnessHelper;
import com.bestv.vrcinema.util.PermissionUtil;

/**
 * Created by xujunyang on 16/10/9.
 */

public class MyLifecycleHandler implements Application.ActivityLifecycleCallbacks {
    // I use four separate variables here. You can, of course, just use two and
    // increment/decrement them instead of using four and incrementing them all.
    private int resumed;
    private int paused;
    private int started;
    private int stopped;

    private boolean canWriteSettings;
    private boolean autoBrightness;
    private int brightnessLevel;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ++resumed;

        if(resumed == (paused+1)){
            try{
                if(PermissionUtil.checkWriteSettingsPermission(activity)){
                    // 0: SCREEN_BRIGHTNESS_MODE_MANUAL 1: SCREEN_BRIGHTNESS_MODE_AUTOMATIC
                    autoBrightness = BrightnessHelper.isAutoBrightness(activity);
                    Log.d("test", "brightness mode: " + autoBrightness + " ");
                    if(autoBrightness) {
                        BrightnessHelper.changeAppBrightness(activity, 200);
                    }else{
                        brightnessLevel = BrightnessHelper.getBrightness(activity);
                        Log.d("test", "brightness level: " + brightnessLevel + " ");

                        BrightnessHelper.changeAppBrightness(activity, brightnessLevel);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        android.util.Log.w("test", "application is in resume");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ++paused;

        android.util.Log.w("test", "application is in pause");
        android.util.Log.w("test", "application is in foreground: " + (resumed > paused));
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;
        android.util.Log.w("test", "application is in start");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;
        android.util.Log.w("test", "application is in stop");
        android.util.Log.w("test", "application is visible: " + (started > stopped));
    }

    // If you want a static function you can use to check if your application is
    // foreground/background, you can use the following:
    /*
    // Replace the four variables above with these four
    private static int resumed;
    private static int paused;
    private static int started;
    private static int stopped;

    // And these two public static functions
    public static boolean isApplicationVisible() {
        return started > stopped;
    }

    public static boolean isApplicationInForeground() {
        return resumed > paused;
    }
    */
}
