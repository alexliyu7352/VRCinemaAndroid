package com.bestv.vrcinema.httpReport;


/**
 * Created by xujunyang on 17/1/15.
 */

public class HttpReporterImpl {
    private final static String reportHost = "http://ottdata.bestv.com.cn";

    private static HttpReporterImpl instance;

    private HttpGetImp httpGetImp;

    private HttpReporterImpl(){
        httpGetImp = new HttpGetImp();
    }

    public static synchronized HttpReporterImpl getInstance(){
        if(instance == null){
            instance = new HttpReporterImpl();
        }
        return instance;
    }

    public void reportSth(String report){
        try{
            String record = reportHost + report;
            httpGetImp.run(record);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
