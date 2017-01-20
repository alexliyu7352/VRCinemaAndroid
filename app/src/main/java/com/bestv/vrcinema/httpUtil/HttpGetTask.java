package com.bestv.vrcinema.httpUtil;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by xujunyang on 17/1/3.
 */

public class HttpGetTask extends AsyncTask<String, Void, String> {
    private OnTaskDoneListener onTaskDoneListener;

    public HttpGetTask(OnTaskDoneListener onTaskDoneListener){
        this.onTaskDoneListener = onTaskDoneListener;
    }

    @Override
    protected String doInBackground(String... params){
        try{
            URL url = new URL(params[0]);
            HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("Content-length", "0");
            httpConnection.setUseCaches(false);
            httpConnection.setConnectTimeout(10000);
            httpConnection.setReadTimeout(10000);

            httpConnection.connect();

            int responseCode = httpConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = br.readLine()) != null){
                    sb.append(line+"\n");
                }
                br.close();
                return sb.toString();
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s){
        super.onPostExecute(s);

        if(onTaskDoneListener != null && s != null){
            onTaskDoneListener.onTaskDone(s);
        }else{
            onTaskDoneListener.onError();
        }
    }
}
