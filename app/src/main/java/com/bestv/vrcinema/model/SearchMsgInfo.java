package com.bestv.vrcinema.model;

import java.util.ArrayList;

/**
 * Created by xujunyang on 17/1/16.
 */

public class SearchMsgInfo {
    private String searchMsg = "";
    private ArrayList<String> searchKeywords;

    public SearchMsgInfo(){
        searchKeywords = new ArrayList<String>();
    }

    public void setSearchMsg(String msg){
        searchMsg = msg;
    }

    public String getSearchMsg(){
        return searchMsg;
    }

    public void addSearchKeyword(String keyword){
        searchKeywords.add(keyword);
    }
}
