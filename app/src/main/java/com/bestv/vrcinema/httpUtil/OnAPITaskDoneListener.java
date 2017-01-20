package com.bestv.vrcinema.httpUtil;

import android.app.Activity;
import android.os.Build;
import android.util.Log;

import com.bestv.vrcinema.MainActivity;
import com.bestv.vrcinema.jsonUtil.JsonParser;
import com.bestv.vrcinema.model.RecommendInfoSingleton;
import com.bestv.vrcinema.model.TouchRecommendInfo;

import java.lang.ref.WeakReference;

/**
 * Created by xujunyang on 17/1/3.
 */

public class OnAPITaskDoneListener implements OnTaskDoneListener {
    private WeakReference<Activity> mActivityRef;

    public OnAPITaskDoneListener(Activity activity){
        mActivityRef = new WeakReference<Activity>(activity);
    }

    public void onTaskDone(String responseData){
        Log.d("OnAPITaskDoneListener", "onTaskDone " + responseData);
        TouchRecommendInfo touchRecommendInfo = JsonParser.parseTouchRecommend(responseData);
        if(touchRecommendInfo != null){
            RecommendInfoSingleton.getInstance().updateInfo(touchRecommendInfo);
            Activity activity = mActivityRef != null ? mActivityRef.get() : null;
            if (activity != null) {
                boolean secureOnUIStaff = !activity.isFinishing();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    secureOnUIStaff = secureOnUIStaff && !activity.isDestroyed();
                }

                if (secureOnUIStaff && activity instanceof MainActivity) {
                    ((MainActivity) activity).initContentFragment();
                }
            }
        }
    }
    public void onError(){
        Log.d("OnAPITaskDoneListener", "onError");
    }
}
