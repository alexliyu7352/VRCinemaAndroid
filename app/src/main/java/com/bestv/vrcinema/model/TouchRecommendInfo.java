package com.bestv.vrcinema.model;

import java.util.ArrayList;

/**
 * Created by xujunyang on 17/1/3.
 */

public class TouchRecommendInfo {
    private boolean hasNext = false;
    private  int pageIndex = 1;
    private ArrayList<MovieInfo> movieInfos;

    public TouchRecommendInfo(){
        movieInfos = new ArrayList<MovieInfo>();
    }

    public void setHasNext(boolean hasNext){
        this.hasNext = hasNext;
    }

    public boolean getHasNext(){
        return hasNext;
    }

    public void setPageIndex(int pageIndex){
        this.pageIndex = pageIndex;
    }

    public int getPageIndex(){
        return pageIndex;
    }

    public void addMovieInfo(MovieInfo movieInfo){
        movieInfos.add(movieInfo);
    }

    public ArrayList<MovieInfo> getMovieInfos(){
        return movieInfos;
    }

    public int getItemsCount(){
        return movieInfos.size();
    }

    public MovieInfo getMovieInfo(int index){
        return movieInfos.get(index);
    }

    public void clearInfo(){movieInfos.clear();}
}
