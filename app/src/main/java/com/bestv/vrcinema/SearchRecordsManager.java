package com.bestv.vrcinema;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bestv.vrcinema.model.RecordSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xujunyang on 17/1/9.
 */

public class SearchRecordsManager {
    RecordSQLiteOpenHelper recordsHelper;
    SQLiteDatabase recordsDb;

    public SearchRecordsManager(Context context){
        recordsHelper = new RecordSQLiteOpenHelper(context);
    }

    // 添加搜索记录
    public void addRecord(String record){
        if(!isHasRecord(record)){
            recordsDb = recordsHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", record);
            recordsDb.insert("records", null, values);
            recordsDb.close();
        }
    }

    // 判断是否含有该搜索记录
    public boolean isHasRecord(String record){
        boolean isHasRecord = false;
        recordsDb = recordsHelper.getReadableDatabase();
        Cursor cursor = recordsDb.query("records", null, null, null, null, null, null);
        while (cursor.moveToNext()){
            if(record.equals(cursor.getString(cursor.getColumnIndexOrThrow("name")))){
                isHasRecord = true;
            }
        }
        recordsDb.close();

        return isHasRecord;
    }

    // 获取全部搜索记录
    public List<String> getRecordsList(){
        List<String> recordsList = new ArrayList<>();
        recordsDb = recordsHelper.getReadableDatabase();
        Cursor cursor = recordsDb.query("records", null, null, null, null, null, null);
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            recordsList.add(name);
        }
        recordsDb.close();

        return recordsList;
    }

    // 清空搜索记录
    public void deleteAllRecords(){
        recordsDb = recordsHelper.getWritableDatabase();
        recordsDb.execSQL("delete from records");
        recordsDb.close();
    }
}
