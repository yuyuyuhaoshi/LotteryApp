package com.yhslib.lottery.sqlittle;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;


/**
 * My File Created by xyz on 2018/4/12.
 */

public class NotesLoader extends AsyncTaskLoader {
    static final String TABLE_NAME_REMIND = "remind";
    private DiaryDAO dao;
    public NotesLoader(Context context) {
        super(context);
        dao = new DiaryDAO(context);
    }
    @Override
    protected void onStartLoading() {
        Log.v("onStartLoading", "OK");
        forceLoad();    //强制加载
    }
    @Override
    public Cursor loadInBackground() {
        Log.v("loadInBackground","OK");

        return dao.getRecordsOfRmindById(TABLE_NAME_REMIND,"%%");
    }
    @Override
    protected void onStopLoading() {
        Log.v("onStopLoading","OK");

    }
}






