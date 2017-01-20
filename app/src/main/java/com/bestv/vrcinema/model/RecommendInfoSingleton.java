package com.bestv.vrcinema.model;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by xujunyang on 17/1/3.
 */

public class RecommendInfoSingleton {
    private static RecommendInfoSingleton instance;

    private TouchRecommendInfo touchRecommendInfo;

    private SearchMsgInfo searchMsgInfo;

    private RecommendInfoSingleton(){
        touchRecommendInfo = new TouchRecommendInfo();
        searchMsgInfo = new SearchMsgInfo();
    }

    public static synchronized RecommendInfoSingleton getInstance(){
        if(instance == null){
            instance = new RecommendInfoSingleton();
        }
        return instance;
    }

    public void updateInfo(TouchRecommendInfo info){
        touchRecommendInfo.setHasNext(info.getHasNext());
        touchRecommendInfo.setPageIndex(info.getPageIndex());
        ArrayList<MovieInfo> temp = info.getMovieInfos();
        for(MovieInfo movieInfo : temp){
            touchRecommendInfo.addMovieInfo(movieInfo);
        }
        Log.d("RecommendInfoSingleton", "size: " + touchRecommendInfo.getMovieInfos().size());
    }

    public boolean getHasNext(){
        return touchRecommendInfo.getHasNext();
    }

    public int getPageIndex(){
        return touchRecommendInfo.getPageIndex();
    }

    public int getItemsCount() {return touchRecommendInfo.getItemsCount();}

    public MovieInfo getMovieInfo(int index){
        return touchRecommendInfo.getMovieInfo(index);
    }

    public void updateSearchMsg(SearchMsgInfo info){
        searchMsgInfo = info;
    }

    public String getSearchMessage(){
        return searchMsgInfo.getSearchMsg();
    }
}
