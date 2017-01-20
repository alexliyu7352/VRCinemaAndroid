package com.bestv.vrcinema.httpUtil;

import android.app.Activity;
import android.os.Build;
import android.util.Log;

import com.bestv.vrcinema.SearchContentActivity;
import com.bestv.vrcinema.SplashActivity;
import com.bestv.vrcinema.jsonUtil.JsonParser;
import com.bestv.vrcinema.model.SearchResultInfo;

import java.lang.ref.WeakReference;

/**
 * Created by xujunyang on 17/1/12.
 */

public class OnWelcomeTaskDoneListener implements OnTaskDoneListener{
    private WeakReference<Activity> mActivityRef;

    public OnWelcomeTaskDoneListener(Activity activity){
        mActivityRef = new WeakReference<Activity>(activity);
    }

    public void onTaskDone(String responseData){
        Log.d("OnWelcomeTask", "onTaskDone " + responseData);
        JsonParser.parseWelcomInfo(responseData);
        Activity activity = mActivityRef != null ? mActivityRef.get() : null;
        if (activity != null) {
            boolean secureOnUIStaff = !activity.isFinishing();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                secureOnUIStaff = secureOnUIStaff && !activity.isDestroyed();
            }

            if (secureOnUIStaff && activity instanceof SplashActivity) {
                ((SplashActivity) activity).preloadAdImage();
            }
        }
    }

    public void onError(){
        Log.d("OnWelcomeTask", "onError");
    }
}
