package com.bestv.vrcinema.constant;

/**
 * Created by xujunyang on 17/1/9.
 */

public interface ConstantInterface {
    String HTTPHOST = "http://vr.ott.bestv.com.cn/";  //vr.ott.bestv.com.cn  54.223.43.38
    String RECOMMENDURL = HTTPHOST + "VRServer/API/V1/TouchRecommend?pageIndex=1";
    String SearchRecommend = HTTPHOST + "VRServer/API/V1/SearchRecommend";
//    String SEARCHURL = "http://54.223.43.38/VRServer/API/V1/Search?keyword=中国&pageIndex=1";
    String SEARCHURL = HTTPHOST +  "VRServer/API/V1/Search?pageIndex=1";
    String WELCOMEURL = HTTPHOST + "VRServer/API/V1/WelcomePage";


    int REQUEST_CODE_WRITE_SETTINGS = 2;

    String VERSION = "0.9.6.0";  //请求版本号

    String ShowedVersion = "V0.9.6";//显示版本号

}
