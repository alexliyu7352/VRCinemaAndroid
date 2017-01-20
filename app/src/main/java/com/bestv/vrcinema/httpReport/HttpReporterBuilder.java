package com.bestv.vrcinema.httpReport;

import com.bestv.vrcinema.MyApplication;
import com.bestv.vrcinema.constant.ConstantInterface;
import com.fun.crash.FunDevice;

/**
 * Created by xujunyang on 17/1/15.
 */

public class HttpReporterBuilder {
    public static String play = "touchplay";
    public static String recommendPlay = "1";
    public static String searchPlay = "2";
    public static String setting = "setting";
    public static String search = "search";
    public static String adTag = "adevent";
    public static String buildTemporaryReport(String key, String value){
        String str = null;
        str = String.format("/vr/temporary?version=1&dev=%d&mac=%s&ver=%s&kver=%s&key=%s&value=%s",
                0xCD,
                FunDevice.Network.getMacAddress(MyApplication.getContext()),
                ConstantInterface.VERSION,
                "6.0.0.0",
                key,
                value);

        return str;
    }
}
