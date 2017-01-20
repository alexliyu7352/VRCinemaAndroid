package com.bestv.vrcinema.util;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by xujunyang on 16/10/10.
 */

public class BrightnessHelper {
    public static void setBrightness(Context context, int brightness){
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
    }

    // 自动模式下,无法正确获取系统亮度
    public static int getBrightness(Context context){
        try{
            return Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        }catch (Settings.SettingNotFoundException e){
            return 0;
        }
    }

    public static boolean isAutoBrightness(Context context){
        boolean autoBrightness = false;
        try{
            int brightnessMode = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
            autoBrightness = (brightnessMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        }catch (Settings.SettingNotFoundException e){
            e.printStackTrace();
        }

        return autoBrightness;
    }

    // 根据亮度值修改当前window亮度
    public static void changeAppBrightness(Context context, int brightness){
        Window window = ((Activity) context).getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if(brightness == -1){
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        }else{
            lp.screenBrightness = (brightness <=0 ? 1: brightness) / 255.0f;
        }
        window.setAttributes(lp);
    }
}
