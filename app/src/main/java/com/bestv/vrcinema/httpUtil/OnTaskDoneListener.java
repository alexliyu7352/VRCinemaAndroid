package com.bestv.vrcinema.httpUtil;

/**
 * Created by xujunyang on 17/1/3.
 */

public interface OnTaskDoneListener {
    void onTaskDone(String responseData);
    void onError();
}
