package com.bestv.vrcinema.model;

/**
 * Created by xujunyang on 17/1/12.
 */

public class WelcomeInfo {
    public static String OPENTAG = "open";
    public static String PLAYTAG = "play_video";

    //	"title":"有奖用户访谈",
//	"content":"参与调研填问卷",
//	"button":"立即参加",
//	"buttonNote":"",
//	"url":"https://sojump.com/jq/10635395.aspx"
    public String title = "";
    public String content = "";
    public String btnStr = "";
    public String btnURL = "";

    //ad
    public boolean showAd = false;
    public String adType = "";  //open_native
    public String imgURL = "";
    public int timeout = 3;
    public String adValue = "";  // 外链URL
    public int btnType = 0;

    //play movie
    public String videoCode = "";
    public String videoType = "2D";
    public String videoURL = "http://ott.vod.bestvcdn.com.cn/gslb/program/FDN/FDNB2308359/prime.m3u8?_BitRate=700";
    public String videoName = "巴霍巴利王：开端";

    public boolean loadImageSuccess = false;

    private static WelcomeInfo instance;
    public static synchronized WelcomeInfo getInstance(){
        if(instance == null){
            instance = new WelcomeInfo();
        }
        return instance;
    }
}
