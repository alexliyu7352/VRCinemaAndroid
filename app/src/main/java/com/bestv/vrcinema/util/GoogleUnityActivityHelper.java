package com.bestv.vrcinema.util;

import android.app.Activity;
import android.content.Intent;

import com.bestv.vrcinema.model.CurrentMovieInfo;
import com.google.unity.GoogleUnityActivity;
import com.unity3d.player.UnityPlayer;

import static com.unity3d.player.UnityPlayer.currentActivity;

/**
 * Created by xujunyang on 17/1/11.
 */

public class GoogleUnityActivityHelper {
    public static void startCardboard(Activity activity){
        Intent intent = new Intent(activity, GoogleUnityActivity.class);

        intent.putExtra("videoCode", CurrentMovieInfo.getVideoCode());
        intent.putExtra("videoType", CurrentMovieInfo.getVideoType());
        intent.putExtra("videoURL", CurrentMovieInfo.getVideoURL());
        intent.putExtra("videoName", CurrentMovieInfo.getVideoName());
        intent.putExtra("episodeFlag", CurrentMovieInfo.getEpisodeNum() > 1);

        activity.startActivity(intent);
    }

    public static String getVideoCode(){
        Intent intent = UnityPlayer.currentActivity.getIntent();
        return intent.getStringExtra("videoCode");
    }

    public static String getVideoType(){
        Intent intent = UnityPlayer.currentActivity.getIntent();
        return intent.getStringExtra("videoType");
    }

    public static String getVideoURL(){
        Intent intent = UnityPlayer.currentActivity.getIntent();
        return intent.getStringExtra("videoURL");
    }

    public static String getVideoName(){
        Intent intent = UnityPlayer.currentActivity.getIntent();
        return intent.getStringExtra("videoName");
    }

    public static boolean getEpisodeFlag(){
        Intent intent = UnityPlayer.currentActivity.getIntent();
        return intent.getBooleanExtra("episodeFlag", false);
    }

}
