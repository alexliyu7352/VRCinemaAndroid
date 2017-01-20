package com.bestv.vrcinema.util;

import android.text.TextUtils;
import android.util.Base64;

import com.bestv.vr.utils.Global;
import com.bestv.vrcinema.MyApplication;
import com.bestv.vrcinema.constant.ConstantInterface;
import com.fun.crash.CrashReport;
import com.bestv.vr.utils.DeviceInfoUtil;
import com.fun.crash.FunDevice;

import org.json.JSONObject;

/**
 * Created by xujunyang on 17/1/12.
 */

public class QueryUtil {
    private static String queryParam = "";
    public static String getQueryParam(){
        if(!TextUtils.isEmpty(queryParam)){
            return queryParam;
        }

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", "");
            jsonObject.put("login_id", "");
            jsonObject.put("mac", FunDevice.Network.getMacAddress(MyApplication.getContext()));
            jsonObject.put("cid", "");
            jsonObject.put("channel", "");// DeviceInfoUtil.getChannelNumber());
            jsonObject.put("app_version", ConstantInterface.VERSION);
            jsonObject.put("os", "Android");
            jsonObject.put("os_version", DeviceInfoUtil.getOSVersion());
            jsonObject.put("manufacturer", DeviceInfoUtil.getDeviceModel());
            String json = jsonObject.toString();
            queryParam = Base64.encodeToString(json.getBytes(), Base64.NO_WRAP);
        }catch (Exception e){
            e.printStackTrace();
        }

        return  queryParam;
    }
}
