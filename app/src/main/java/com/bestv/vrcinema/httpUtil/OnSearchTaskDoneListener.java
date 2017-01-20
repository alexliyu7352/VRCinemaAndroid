package com.bestv.vrcinema.httpUtil;

import android.app.Activity;
import android.os.Build;
import android.util.Log;

import com.bestv.vrcinema.MainActivity;
import com.bestv.vrcinema.SearchContentActivity;
import com.bestv.vrcinema.jsonUtil.JsonParser;
import com.bestv.vrcinema.model.RecommendInfoSingleton;
import com.bestv.vrcinema.model.SearchResultInfo;
import com.bestv.vrcinema.model.TouchRecommendInfo;

import java.lang.ref.WeakReference;

/**
 * Created by xujunyang on 17/1/10.
 */

public class OnSearchTaskDoneListener implements OnTaskDoneListener {
    private WeakReference<Activity> mActivityRef;

    public OnSearchTaskDoneListener(Activity activity){
        mActivityRef = new WeakReference<Activity>(activity);
    }

    public void onTaskDone(String responseData){
        Log.d("OnSearchTask", "onTaskDone " + responseData);
        SearchResultInfo searchResultInfo = JsonParser.parseSearchResult(responseData);
        if(searchResultInfo != null){
            Activity activity = mActivityRef != null ? mActivityRef.get() : null;
            if (activity != null) {
                boolean secureOnUIStaff = !activity.isFinishing();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    secureOnUIStaff = secureOnUIStaff && !activity.isDestroyed();
                }

                if (secureOnUIStaff && activity instanceof SearchContentActivity) {
                    ((SearchContentActivity) activity).initSearchResult(searchResultInfo);
                }
            }
        }
    }

    public void onError(){
        Log.d("OnSearchTask", "onError");
    }
}
