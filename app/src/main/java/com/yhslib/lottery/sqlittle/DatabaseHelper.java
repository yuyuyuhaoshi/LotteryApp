package com.yhslib.lottery.sqlittle;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.yhslib.lottery.utils.Rule;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "lottery";
    private static final String DATABASE_NAME = "dbForLottery.db";
    private static final int DATABASE_VERSION = 8;

    DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "Create DatabaseHelper Object Version=" + DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        createTableDiary(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d(TAG, "onUpgrade=" + newVersion);
        dropTableDiary(db);
        createTableDiary(db);
        Log.d(TAG, "onUpgrade success");
    }

    void createTableDiary(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + Rule.TABLE_NAME_REMIND + " (" + Rule.ID + " INTEGER PRIMARY KEY,"
                + Rule.REMIND_NAME + " TEXT NOT NULL, " + Rule.REMIND_STATE + " TEXT NOT NULL, " + Rule.REMIND_COUNT + " TEXT NOT NULL, " + Rule.REMIND_TYPE + " TEXT NOT NULL "+ ");";
        db.execSQL(sql);

        sql= "CREATE TABLE " + Rule.TABLE_NAME_SHISHICAI + " (" + Rule.ID + " INTEGER PRIMARY KEY,"+ Rule.LOTTERY_ID + " TEXT NOT NULL, "
                + Rule.LOTTERY_VALUES + " TEXT NOT NULL, " + Rule.LOTTERY_TIME + " TEXT NOT NULL " +  ");";
        db.execSQL(sql);

        sql= "CREATE TABLE " + Rule.TABLE_NAME_PKTEN + " (" + Rule.ID + " INTEGER PRIMARY KEY,"+ Rule.LOTTERY_ID + " TEXT NOT NULL, "
                + Rule.LOTTERY_VALUES + " TEXT NOT NULL, " + Rule.LOTTERY_TIME + " TEXT NOT NULL " +  ");";
        db.execSQL(sql);

        sql= "CREATE TABLE " + Rule.TABLE_NAME_RECORD + " (" + Rule.ID + " INTEGER PRIMARY KEY,"+ Rule.RECORD_ID + " INTEGER NOT NULL, "
                + Rule.RECORD_NAME + " TEXT NOT NULL, " + Rule.RECORD_TYPE + " TEXT NOT NULL, "
                + Rule.RECORD_VALUE + " TEXT NOT NULL, " + Rule.RECORD_POSITION + " TEXT NOT NULL, "
                + Rule.RECORD_COUNT + " TEXT NOT NULL, " + Rule.RECORD_TIME + " TEXT NOT NULL " +");";
        db.execSQL(sql);

        sql= "CREATE TABLE " + Rule.TABLE_NAME_SET_RECORD + " (" + Rule.ID + " INTEGER PRIMARY KEY,"+ Rule.SET_RECORD_PKTEN_BIGSMALL + "  TEXT NOT NULL, "
                + Rule.SET_RECORD_PKTEN_EVENODD + " TEXT NOT NULL, " + Rule.SET_RECORD_SHISHICAI_BIGSMALL + " TEXT NOT NULL, "
                + Rule.SET_RECORD_SHISHICAI_EVENODD + " TEXT NOT NULL "  +");";
        db.execSQL(sql);

        Log.d(TAG, " CreateDB SUCCESS, SQL=" + sql);
    }

    void dropTableDiary(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + Rule.TABLE_NAME_REMIND);
    }

}
