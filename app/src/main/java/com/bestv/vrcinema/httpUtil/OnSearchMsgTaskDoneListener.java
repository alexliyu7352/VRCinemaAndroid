package com.bestv.vrcinema.httpUtil;

import android.app.Activity;
import android.os.Build;
import android.util.Log;

import com.bestv.vrcinema.SearchContentActivity;
import com.bestv.vrcinema.jsonUtil.JsonParser;
import com.bestv.vrcinema.model.RecommendInfoSingleton;
import com.bestv.vrcinema.model.SearchMsgInfo;
import com.bestv.vrcinema.model.SearchResultInfo;

import java.lang.ref.WeakReference;

/**
 * Created by xujunyang on 17/1/16.
 */

public class OnSearchMsgTaskDoneListener implements OnTaskDoneListener {
    public OnSearchMsgTaskDoneListener(){
    }

    public void onTaskDone(String responseData){
        Log.d("OnSearchMsgTask", "onTaskDone " + responseData);
        SearchMsgInfo searchMsgInfo = JsonParser.parseSearchHintResult(responseData);
        if(searchMsgInfo != null){
            RecommendInfoSingleton.getInstance().updateSearchMsg(searchMsgInfo);
        }
    }

    public void onError(){
        Log.d("OnSearchMsgTask", "onError");
    }
}
