package com.bestv.vrcinema.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.bestv.vrcinema.constant.ConstantInterface;

/**
 * Created by xujunyang on 16/10/9.
 */

public class PermissionUtil {
    public static boolean checkWriteSettingsPermission(Activity activity){
        if(Build.VERSION.SDK_INT >= 23){
            if(!Settings.System.canWrite(activity)){
                return false;
            }
        }

        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void permitWriteSettings(Activity activity){
        Log.i("writing", "onActivityResult write settings granted" );
        if(Build.VERSION.SDK_INT >= 23){
            if(!Settings.System.canWrite(activity)){
                Log.i("writing", "onActivityResult write settings granted2" );
                //打开设置界面让用户设置是否授权系统设置权限
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                        Uri.parse("package:" + activity.getPackageName()));
                activity.startActivityForResult(intent, ConstantInterface.REQUEST_CODE_WRITE_SETTINGS);
            }
        }
    }
}
