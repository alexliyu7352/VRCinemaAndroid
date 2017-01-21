package com.bestv.vrcinema.jsonUtil;

import android.graphics.Bitmap;
import android.view.View;

import com.bestv.vrcinema.model.MovieInfo;
import com.bestv.vrcinema.model.RecommendInfoSingleton;
import com.bestv.vrcinema.model.SearchMsgInfo;
import com.bestv.vrcinema.model.SearchResultInfo;
import com.bestv.vrcinema.model.TouchRecommendInfo;
import com.bestv.vrcinema.model.WelcomeInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by xujunyang on 17/1/3.
 */

public class JsonParser {
    public static String successCode = "SUCCESS";
    public static TouchRecommendInfo parseTouchRecommend(String data){
        TouchRecommendInfo touchRecommendInfo = null;
        try{
            JSONObject jsonObj = new JSONObject(data);
            String code = jsonObj.getString("code");
            if(code.equals(successCode)){
                touchRecommendInfo = new TouchRecommendInfo();
                touchRecommendInfo.setHasNext(jsonObj.getBoolean("hasNextPage"));
                touchRecommendInfo.setPageIndex(jsonObj.getInt("pageIndex"));

                JSONArray jsonArray = jsonObj.getJSONArray("categoryItemVOs");
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject element = jsonArray.getJSONObject(i);
                    MovieInfo movieInfo = new MovieInfo();
                    movieInfo.setName(element.getString("name"));
                    movieInfo.setCode(element.getString("code"));
                    movieInfo.setType(element.getString("type"));
                    movieInfo.setPlayUrl(element.getString("playUrl"));
                    movieInfo.setHorizontalPic(element.getString("horizontalPic"));
                    movieInfo.setVerticalPic(element.getString("verticalPic"));
                    movieInfo.setDuration(element.getInt("length")/60);
                    movieInfo.setRating(element.getDouble("rating"));
                    movieInfo.setShowDetail(element.getString("showDetail"));
                    movieInfo.setYear(element.optString("year"));
                    movieInfo.setEpisode(element.optInt("episode"));
                    movieInfo.setGenre(element.getString("genre"));
                    movieInfo.setActor(element.getString("actor"));
                    movieInfo.setDirector(element.getString("director"));
                    movieInfo.setDescription(element.getString("description"));

                    touchRecommendInfo.addMovieInfo(movieInfo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return touchRecommendInfo;
    }

    public static SearchResultInfo parseSearchResult(String data){
        SearchResultInfo searchResultInfo = null;
        try{
            JSONObject jsonObj = new JSONObject(data);
            String code = jsonObj.getString("code");
            if(code.equals(successCode)){
                searchResultInfo = new SearchResultInfo();
                searchResultInfo.setHasNext(jsonObj.getBoolean("hasNextPage"));
                searchResultInfo.setPageIndex(jsonObj.getInt("pageIndex"));

                JSONArray jsonArray = jsonObj.getJSONArray("categoryItemVOs");
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject element = jsonArray.getJSONObject(i);
                    MovieInfo movieInfo = new MovieInfo();
                    movieInfo.setName(element.getString("name"));
                    movieInfo.setCode(element.getString("code"));
                    movieInfo.setType(element.getString("type"));
                    movieInfo.setPlayUrl(element.getString("playUrl"));
                    movieInfo.setHorizontalPic(element.getString("horizontalPic"));
                    movieInfo.setVerticalPic(element.getString("verticalPic"));
                    movieInfo.setRating(element.getDouble("rating"));
                    movieInfo.setShowDetail(element.getString("showDetail"));
                    movieInfo.setYear(element.optString("year"));
                    movieInfo.setEpisode(element.optInt("episode"));
                    movieInfo.setGenre(element.getString("genre"));

                    searchResultInfo.addMovieInfo(movieInfo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return searchResultInfo;
    }

    public static void parseWelcomInfo(String data){
        try{
            JSONObject jsonObj = new JSONObject(data);
            int rc = jsonObj.getInt("rc");
            if(rc == 0){
                JSONObject body = jsonObj.getJSONObject("body");
                WelcomeInfo.getInstance().setTitle(body.getString("title"));
                WelcomeInfo.getInstance().setContent(body.getString("content"));
                WelcomeInfo.getInstance().setBtnStr(body.getString("button"));
                WelcomeInfo.getInstance().setBtnURL(body.getString("url"));

                try {
                    JSONObject boot_ad = body.getJSONObject("boot_ad");
                    WelcomeInfo.getInstance().setAdType(boot_ad.getString("type"));
                    WelcomeInfo.getInstance().setAdImgURL(boot_ad.getString("ad_img_url"));
                    WelcomeInfo.getInstance().setAdBtnType(boot_ad.getInt("button_type"));
                    WelcomeInfo.getInstance().setAdTimeout(boot_ad.getInt("timeout"));

                    if(WelcomeInfo.getInstance().getAdType().contains(WelcomeInfo.OPENTAG)){
                        WelcomeInfo.getInstance().setAdValue(boot_ad.getString("value"));
                    }else if(WelcomeInfo.getInstance().getAdType().contains(WelcomeInfo.PLAYTAG)){
                        JSONObject videoInfo = boot_ad.getJSONObject("value");
                        // for video
                        WelcomeInfo.getInstance().setVideoCode(videoInfo.getString("video_code"));
                        WelcomeInfo.getInstance().setVideoType(videoInfo.getString("video_type"));
                        WelcomeInfo.getInstance().setVideoName(videoInfo.getString("video_name"));
                        WelcomeInfo.getInstance().setVideoURL(videoInfo.getString("video_url"));
                    }

                    // 显示开机广告
                    WelcomeInfo.getInstance().setShowAd(true);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static SearchMsgInfo parseSearchHintResult(String data) {
        SearchMsgInfo searchMsgInfo = null;
        try{
            JSONObject jsonObj = new JSONObject(data);
            String code = jsonObj.getString("code");
            if(code.equals(successCode)){
                searchMsgInfo = new SearchMsgInfo();
                searchMsgInfo.setSearchMsg(jsonObj.getString("searchMessage"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return searchMsgInfo;
    }
}
