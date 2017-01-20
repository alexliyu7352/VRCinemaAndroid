package com.bestv.vrcinema.httpReport;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xujunyang on 17/1/15.
 * implement http get
 */

public class HttpGetImp {
    OkHttpClient client = new OkHttpClient();

    public void run(String url)throws IOException{
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HttpGetImp", "onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("HttpGetImp", "onResponse");
            }
        });
    }
}
