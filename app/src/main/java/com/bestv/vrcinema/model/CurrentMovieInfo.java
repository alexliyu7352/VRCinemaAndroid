package com.bestv.vrcinema.model;

/**
 * Created by xujunyang on 17/1/11.
 * 利用静态变量来记录2D界面传到3D界面的播放参数
 */

public class CurrentMovieInfo {
    private static String videoCode="";
    private static String videoType="";
    private static String videoURL="";
    private static String videoName="";
    private static int    episodeNum = 0;

    public static String getVideoCode(){
        return videoCode;
    }

    public static String getVideoType(){
        return videoType;
    }

    public static String getVideoURL(){
        return videoURL;
    }

    public static String getVideoName(){
        return videoName;
    }

    public static int getEpisodeNum(){return episodeNum;}

    public static void updateInfo(String code, String type, String url, String name, int num){
        reset();
        videoCode = code;
        videoType = type;
        videoURL = url;
        videoName = name;
        episodeNum = num;
    }

    public static void reset(){
        videoCode = "";
        videoType = "";
        videoURL = "";
        videoName = "";
        episodeNum = 0;
    }
}
