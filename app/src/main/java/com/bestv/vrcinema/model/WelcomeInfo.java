package com.bestv.vrcinema.model;

/**
 * Created by xujunyang on 17/1/12.
 */

public class WelcomeInfo {
    public static String OPENTAG = "open";
    public static String PLAYTAG = "play_video";

    private String title = "";
    private String content = "";
    private String btnStr = "";
    private String btnURL = "";

    //ad info
    private boolean showAd = false;
    private String adType = "";  //open_native
    private String adImgURL = "";
    private int adTimeout = 3;
    private String adValue = "";  // 外链URL
    private int adBtnType = 0;

    //play movie
    private String videoCode = "";
    private String videoType = "2D";
    private String videoURL = "http://ott.vod.bestvcdn.com.cn/gslb/program/FDN/FDNB2308359/prime.m3u8?_BitRate=700";
    private String videoName = "巴霍巴利王：开端";

    private boolean loadImageSuccess = false;

    private static WelcomeInfo instance;
    public static synchronized WelcomeInfo getInstance(){
        if(instance == null){
            instance = new WelcomeInfo();
        }
        return instance;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBtnStr() {
        return btnStr;
    }

    public void setBtnStr(String btnStr) {
        this.btnStr = btnStr;
    }

    public String getBtnURL() {
        return btnURL;
    }

    public void setBtnURL(String btnURL) {
        this.btnURL = btnURL;
    }

    public boolean isShowAd() {
        return showAd;
    }

    public void setShowAd(boolean showAd) {
        this.showAd = showAd;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public String getAdImgURL() {
        return adImgURL;
    }

    public void setAdImgURL(String adImgURL) {
        this.adImgURL = adImgURL;
    }

    public int getAdTimeout() {
        return adTimeout;
    }

    public void setAdTimeout(int adTimeout) {
        this.adTimeout = adTimeout;
    }

    public String getAdValue() {
        return adValue;
    }

    public void setAdValue(String adValue) {
        this.adValue = adValue;
    }

    public int getAdBtnType() {
        return adBtnType;
    }

    public void setAdBtnType(int adBtnType) {
        this.adBtnType = adBtnType;
    }

    public String getVideoCode() {
        return videoCode;
    }

    public void setVideoCode(String videoCode) {
        this.videoCode = videoCode;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public boolean isLoadImageSuccess() {
        return loadImageSuccess;
    }

    public void setLoadImageSuccess(boolean loadImageSuccess) {
        this.loadImageSuccess = loadImageSuccess;
    }
}
