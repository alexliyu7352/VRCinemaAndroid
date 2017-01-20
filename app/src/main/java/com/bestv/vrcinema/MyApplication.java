package com.bestv.vrcinema;


import android.app.Application;
import android.content.Context;

/**
 * Created by xujunyang on 16/10/9.
 */

public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        // Simply add the handler, and that's it! No need to add any code
        // to every activity. Everything is contained in MyLifecycleHandler
        // with just a few lines of code. Now *that's* nice.
        super.onCreate();

        registerActivityLifecycleCallbacks(new MyLifecycleHandler());

        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
