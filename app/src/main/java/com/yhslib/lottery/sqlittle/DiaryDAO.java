package com.yhslib.lottery.sqlittle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yhslib.lottery.utils.Rule;

public class DiaryDAO {

	private static final String TAG="xyz";
	private DatabaseHelper mOpenHelper;

//	static final String TABLE_NAME_REMIND = "remind";
//	static final String ID = "_id";
//	static final String REMIND_NAME = "name";
//	static final String REMIND_STATE = "state";
//	static final String REMIND_COUNT = "count";
//    static final String REMIND_TYPE = "remind_type";
//	static final String TABLE_NAME_SHISHICAI = "shishicaiHistory";//表名
//	static final String TABLE_NAME_PKTEN = "pktenHistory";//表名
//	static final String LOTTERY_ID = "_id";//期号
//	static final String LOTTERY_VALUES = "lotteryValues";//开奖结果
//	static final String LOTTERY_TIME = "lotteryTime";//开奖时间
//
//	static final String TABLE_NAME_RECORD= "record";//表名
//	static final String RECORD_ID = "record_id";//最后一期id
//	static final String RECORD_NAME= "record_name";//彩票名
//	static final String RECORD_TYPE= "record_type";//记录类型
//	static final String RECORD_VALUE= "record_value";//具体号码
//	static final String RECORD_POSITION= "record_position";//第几个球
//	static final String RECORD_COUNT= "record_count";//连走几期
//	static final String RECORD_TIME= "record_time";//跳出时间
//
//	static final String TABLE_NAME_SET_RECORD= "set_record";//表名
//	static final String SET_RECORD_SHISHICAI_BIGSMALL= "set_record_shishicai_bigsmall";//时时彩记录大小的阈值
//	static final String SET_RECORD_SHISHICAI_EVENODD= "set_record_shishicai_evenodd";//时时彩记录奇偶的阈值
//	static final String SET_RECORD_PKTEN_BIGSMALL= "set_record_pkten_bigsmall";//PK10记录大小的阈值
//	static final String SET_RECORD_PKTEN_EVENODD= "set_record_pkten_evenodd";//pk10记录奇偶的阈值

	public DiaryDAO(Context context){
		mOpenHelper=new DatabaseHelper(context);
	}

	/*
	 * 重新建立数据表
	 */
	public  boolean  createTable() {
		Log.d(TAG, "CreateTable before getWritableDatabase");
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		Log.d(TAG, "CreateTable after getWritableDatabase");

		try {

			mOpenHelper.dropTableDiary(db);
			mOpenHelper.createTableDiary(db);
			Log.d(TAG, "Create DB Table");
			return true;

		} catch (SQLException e) {

			Log.d(TAG, e.getMessage());
			return false;
		}
	}

	/*
	 * 删除数据表
	 */
	public  boolean  dropTable() {
		Log.d(TAG, "drop table before getWritableDatabase");
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		Log.d(TAG, "drop table after getWritableDatabase");
		try {
			mOpenHelper.dropTableDiary(db);
			return true;

		} catch (SQLException e) {

			Log.d(TAG, e.getMessage());
			return false;
		}
	}

	/*
	 * 插入新的提醒数据
	 * @参数,name提醒的彩票类型， state提醒是否开启， count连走个数
	 */
	public boolean insertRecordOfRemind(String name,Boolean state,int count,String type) {
		Log.d(TAG, "insertItem before getWritableDatabase");
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(Rule.REMIND_NAME, name);
		cv.put(Rule.REMIND_STATE, state);
		cv.put(Rule.REMIND_COUNT, count);
		cv.put(Rule.REMIND_TYPE, type);
		return db.insert(Rule.TABLE_NAME_REMIND, null, cv)!=-1;
	}

	/*
	 * 插入新的开奖记录
	 * @参数,tableName 表名，id期号，values开奖号码，time开奖时间
	 */
	public boolean insertLotteryHistory(String tableName,int id,String values,String time) {
		Log.d(TAG, "insertItem before getWritableDatabase");
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(Rule.LOTTERY_ID, id);
		cv.put(Rule.LOTTERY_VALUES, values);
		cv.put(Rule.LOTTERY_TIME, time);
		return db.insert(tableName, null, cv)!=-1;
	}

	/*
	 * 插入新的自定义事件记录
	 * @参数,name 彩票名，id跳出期号，type类型，value具体内容，position第几个球，count连走了几期，time跳出时间
	 */
	public boolean insertRecord(String name,int id,String type, String values,int position,int count,String time) {
		Log.d(TAG, "insertItem before getWritableDatabase");
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(Rule.RECORD_NAME, name);
		cv.put(Rule.RECORD_ID, id);
		cv.put(Rule.RECORD_TYPE, type);
		cv.put(Rule.RECORD_VALUE, values);
		cv.put(Rule.RECORD_POSITION, position);
		cv.put(Rule.RECORD_COUNT, count);
		cv.put(Rule.RECORD_TIME, time);
		return db.insert(Rule.TABLE_NAME_RECORD, null, cv)!=-1;
	}

	/*
	 * 获取指定id的记录record的Cursor，读取全部使用%通配符。
	 */

	public Cursor getRecordById(String id) {
		String[] arg={id};
		Cursor cur = null;
		try {
			Log.d(TAG, "showItems before getReadableDatabase");
			SQLiteDatabase db = mOpenHelper.getReadableDatabase();
			Log.d(TAG, "showItems after getReadableDatabase");
			String col[] = { Rule.ID,Rule.RECORD_NAME, Rule.RECORD_ID,Rule.RECORD_TYPE,Rule.RECORD_VALUE,Rule.RECORD_POSITION,Rule.RECORD_COUNT,Rule.RECORD_TIME };
			cur = db.query(Rule.TABLE_NAME_RECORD, col, "_id like ?", arg, null, null, null);
			return cur;
		} catch (SQLException e) {
			Log.d(TAG, e.getMessage());
		}
		return cur;
	}
	/*
	 * 删除提醒
	 */
	public  boolean  deleteRecordOfRemind(int id) {
		try {
			Log.d(TAG, "deleteItem before getWritableDatabase");
			SQLiteDatabase db = mOpenHelper.getWritableDatabase();
			Log.d(TAG, "deleteItem before getWritableDatabase");
			db.delete(Rule.TABLE_NAME_REMIND, " _id = '"+id+" ' ", null);
			return true;

		} catch (SQLException e) {
			Log.d(TAG, e.getMessage());
			return false;

		}
	}

	/*
	 * 删除提醒
	 */
	public  boolean  deleteRecord(int id) {
		try {
			Log.d(TAG, "deleteItem before getWritableDatabase");
			SQLiteDatabase db = mOpenHelper.getWritableDatabase();
			Log.d(TAG, "deleteItem before getWritableDatabase");
			db.delete(Rule.TABLE_NAME_RECORD, " _id = '"+id+" ' ", null);
			return true;
		} catch (SQLException e) {
			Log.d(TAG, e.getMessage());
			return false;

		}
	}


	/*
	 * 获取指定id的记录Remind的Cursor，读取全部使用%通配符。
	 */

	public Cursor getRecordsOfRmindById(String tableName,String id) {
		String[] arg={id};
		Cursor cur = null;
		try {
			Log.d(TAG, "showItems before getReadableDatabase");
			SQLiteDatabase db = mOpenHelper.getReadableDatabase();
			Log.d(TAG, "showItems after getReadableDatabase");
			String col[] = { "_id",Rule.REMIND_NAME, Rule.REMIND_STATE,Rule.REMIND_COUNT,Rule.REMIND_TYPE };
			cur = db.query(tableName, col, "_id like ?", arg, null, null, null);
			return cur;
		} catch (SQLException e) {

			Log.d(TAG, e.getMessage());
		}
		return cur;
	}

	/*
	 * 获取指定id的记录彩票的Cursor，读取全部使用%通配符。
	 */

	public Cursor getRecordsOfLotteryById(String tableName,String id) {
		String[] arg={id};
		Cursor cur = null;
		try {
			Log.d(TAG, "showItems before getReadableDatabase");
			SQLiteDatabase db = mOpenHelper.getReadableDatabase();
			Log.d(TAG, "showItems after getReadableDatabase");
			String col[] = { "_id",Rule.LOTTERY_ID, Rule.LOTTERY_VALUES,Rule.LOTTERY_TIME };
			cur = db.query(tableName, col, "_id like ?", arg, null, null, null);
			return cur;
		} catch (SQLException e) {

			Log.d(TAG, e.getMessage());
		}
		return cur;
	}

	public boolean updateRemind(String id,String name,Boolean state,int count,String type){
		String[] arg={id};
		try {
			Log.d(TAG, "Do update before getWritableDatabase");
			SQLiteDatabase db = mOpenHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put(Rule.REMIND_NAME, name);
			cv.put(Rule.REMIND_STATE, state);
			cv.put(Rule.REMIND_COUNT,count);
			cv.put(Rule.REMIND_TYPE,type);
			int x = db.update(Rule.TABLE_NAME_REMIND, cv, "_id=?",arg);
			if (x>0){
				return true;
			}
			return false;
		} catch (SQLException e) {
			Log.d(TAG, e.getMessage());
			return false;
		}
	}

	private boolean insertRecordSet(SQLiteDatabase db){
		Log.d(TAG, "Do insert before getWritableDatabase");
		ContentValues cv = new ContentValues();
		cv.put(Rule.SET_RECORD_PKTEN_BIGSMALL, 99);
		cv.put(Rule.SET_RECORD_PKTEN_EVENODD, 99);
		cv.put(Rule.SET_RECORD_SHISHICAI_BIGSMALL,99);
		cv.put(Rule.SET_RECORD_SHISHICAI_EVENODD,99);
		return db.insert(Rule.TABLE_NAME_SET_RECORD, null, cv)!=-1;
	}

	/*
	 * 获取指定id的记录彩票的Cursor，读取全部使用%通配符。
	 */

	public Cursor getRecordSet() {
		Cursor cur = null;
		try {
			Log.d(TAG, "showItems before getReadableDatabase");
			SQLiteDatabase db = mOpenHelper.getReadableDatabase();
			Log.d(TAG, "showItems after getReadableDatabase");
			String col[] = {Rule.SET_RECORD_PKTEN_BIGSMALL,Rule.SET_RECORD_PKTEN_EVENODD, Rule.SET_RECORD_SHISHICAI_BIGSMALL,Rule.SET_RECORD_SHISHICAI_EVENODD };
			cur = db.query(Rule.TABLE_NAME_SET_RECORD, col, null, null,null, null, null);
			if (cur.getCount()==0){//如果设置里面是空的，那么初始化设置99
				insertRecordSet(db);
				cur = db.query(Rule.TABLE_NAME_SET_RECORD, col, null, null,null, null, null);
			}
			return cur;
		} catch (SQLException e) {
			Log.d(TAG, e.getMessage());
		}
		return cur;
	}

	public boolean updateRecordSet(String shishicaiBigSmall,String shishicaiEvenOdd,String pkTenBigSmall,String pkTenEvenOdd){
		try {
			Log.d(TAG, "Do update before getWritableDatabase");
			SQLiteDatabase db = mOpenHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put(Rule.SET_RECORD_PKTEN_BIGSMALL, pkTenBigSmall);
			cv.put(Rule.SET_RECORD_PKTEN_EVENODD, pkTenEvenOdd);
			cv.put(Rule.SET_RECORD_SHISHICAI_BIGSMALL,shishicaiBigSmall);
			cv.put(Rule.SET_RECORD_SHISHICAI_EVENODD,shishicaiEvenOdd);
			int x = db.update(Rule.TABLE_NAME_SET_RECORD, cv, null,null);
			return x > 0;
		} catch (SQLException e) {
			Log.d(TAG, e.getMessage());
			return false;
		}
	}
}
