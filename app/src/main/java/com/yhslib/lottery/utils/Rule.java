package com.yhslib.lottery.utils;

import android.database.Cursor;

import com.yhslib.lottery.sqlittle.DiaryDAO;

/**
 * Created by jerryzheng on 2018/5/18.
 */

public class Rule {
    public static final String PKTEN = "北京PK拾";
    public static final String SHISHICAI = "重庆时时彩";
    public static final String TABLE_NAME_REMIND = "remind";
    public static final String ID = "_id";
    public static final String REMIND_NAME = "name";
    public static final String REMIND_STATE = "state";
    public static final String REMIND_COUNT = "count";
    public static final String REMIND_TYPE = "remind_type";

    public static final String TABLE_NAME_SHISHICAI = "shishicaiHistory";//表名
    public static final String TABLE_NAME_PKTEN = "pktenHistory";//表名
    public static final String LOTTERY_ID = "lottery_id";//期号
    public static final String LOTTERY_VALUES = "lotteryValues";//开奖结果
    public static final String LOTTERY_TIME = "lotteryTime";//开奖时间

    public static final String TABLE_NAME_RECORD = "record";//表名
    public static final String RECORD_ID = "record_id";//最后一期id
    public static final String RECORD_NAME = "record_name";//彩票名
    public static final String RECORD_TYPE = "record_type";//记录类型
    public static final String RECORD_VALUE = "record_value";//具体号码
    public static final String RECORD_POSITION = "record_position";//第几个球
    public static final String RECORD_COUNT = "record_count";//连走几期
    public static final String RECORD_TIME = "record_time";//跳出时间

    public static final String TABLE_NAME_SET_RECORD = "set_record";//表名
    public static final String SET_RECORD_SHISHICAI_BIGSMALL = "set_record_shishicai_bigsmall";//时时彩记录大小的阈值
    public static final String SET_RECORD_SHISHICAI_EVENODD = "set_record_shishicai_evenodd";//时时彩记录奇偶的阈值
    public static final String SET_RECORD_PKTEN_BIGSMALL = "set_record_pkten_bigsmall";//PK10记录大小的阈值
    public static final String SET_RECORD_PKTEN_EVENODD = "set_record_pkten_evenodd";//pk10记录奇偶的阈值
    public static final String REMIND_TYPE_BIGSMALL = "大小";
    public static final String REMIND_TYPE_SINGLEPAIR = "单双";

    public static boolean isSuitRuleToAlram(String lotteryName, String type, int count, DiaryDAO diary) {
        Cursor cursorLottery;
        int[][] countLottery;
        int smallMin, smallMax, bigMin, bigMax;
        int lotteryLong = 0;
        if (lotteryName.equals(PKTEN)) {
            lotteryLong = 10;
            smallMin = 1;
            smallMax = 5;
            bigMin = 6;
            bigMax = 10;
            cursorLottery = diary.getRecordsOfLotteryById(TABLE_NAME_PKTEN, "%%");
            countLottery = new int[2][10];//第0位是连续的期数，第1位是标记变量。标记变量为-1时，说明这一位已经断开，不再连续
        } else {
            lotteryLong = 10;
            smallMin = 0;
            smallMax = 4;
            bigMin = 5;
            bigMax = 9;
            cursorLottery = diary.getRecordsOfLotteryById(TABLE_NAME_SHISHICAI, "%%");
            countLottery = new int[2][5];//第0位是连续的期数，第1位是标记变量。标记变量为-1时，说明这一位已经断开，不再连续
        }
        if (cursorLottery.moveToFirst()) {
            String[] firstValues = cursorLottery.getString(cursorLottery.getColumnIndex(LOTTERY_VALUES)).split(" ");
            int max = 0;
            while (cursorLottery.moveToNext() && max < 30) {//最多遍历前面的31个记录
                max++;
                String[] values = cursorLottery.getString(cursorLottery.getColumnIndex(LOTTERY_VALUES)).split(" ");
                for (int i = 0; i < lotteryLong; i++) {
                    if (type.equals(REMIND_TYPE_SINGLEPAIR)) {
                        if (Integer.valueOf(firstValues[i]) % 2 == Integer.valueOf(values[i]) % 2 && countLottery[1][i] != -1) {
                            countLottery[0][i]++;//如果和新开奖信息是一样的,切标记变量为0，那么连续期数+1，注意1代表连续2期，以此类推
                        } else {
                            countLottery[1][i] = -1;//如果和第一期不一样，将标记变量设为-1，下次不再对这一位数遍历
                        }
                    } else {
                        int x = Integer.valueOf(firstValues[i]);
                        if (smallMin <= x && x <= smallMax) {//如果是小
                            int y = Integer.valueOf(values[i]);
                            if (smallMin <= y && y <= smallMax && countLottery[1][i] != -1) {
                                countLottery[0][i]++;//如果和新开奖信息是一样的,切标记变量为0，那么连续期数+1，注意1代表连续2期，以此类推
                            } else {
                                countLottery[1][i] = -1;//如果和第一期不一样，将标记变量设为-1，下次不再对这一位数遍历
                            }
                        } else {//如果是大
                            int y = Integer.valueOf(values[i]);
                            if (bigMin <= y && y <= bigMax && countLottery[1][i] != -1) {
                                countLottery[0][i]++;//如果和新开奖信息是一样的,切标记变量为0，那么连续期数+1，注意1代表连续2期，以此类推
                            } else {
                                countLottery[1][i] = -1;//如果和第一期不一样，将标记变量设为-1，下次不再对这一位数遍历
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < lotteryLong; i++) {
                if (count == countLottery[0][i] + 1) {
                    return true;
                }
            }
        }
        cursorLottery.close();
        return false;
    }

    public static void doSaveRecord(String lotteryName, String type, int count, DiaryDAO diary) {
        String resultId,resultTime;// 每个元素分别代表id跳出彩票的id,values连续内容,position所在位置,count连续多数,time跳出的时间
        int  resultPosition, resultCount;
        String[] resultValues;
        Cursor cursorLottery;
        int[][] countLottery;
        int smallMin, smallMax, bigMin, bigMax;
        int lotteryLong = 0;
        if (lotteryName.equals(PKTEN)) {
            lotteryLong = 10;
            smallMin = 1;
            smallMax = 5;
            bigMin = 6;
            bigMax = 10;
            cursorLottery = diary.getRecordsOfLotteryById(TABLE_NAME_PKTEN, "%%");
            countLottery = new int[2][10];//第0位是连续的期数，第1位是标记变量。标记变量为-1时，说明这一位已经断开，不再连续
        } else {
            lotteryLong = 5;
            smallMin = 0;
            smallMax = 4;
            bigMin = 5;
            bigMax = 9;
            cursorLottery = diary.getRecordsOfLotteryById(TABLE_NAME_SHISHICAI, "%%");
            countLottery = new int[2][5];//第0位是连续的期数，第1位是标记变量。标记变量为-1时，说明这一位已经断开，不再连续
        }
        if (cursorLottery.moveToFirst()) {
            String[] firstValues = cursorLottery.getString(cursorLottery.getColumnIndex(LOTTERY_VALUES)).split(" ");
            resultId = cursorLottery.getString(cursorLottery.getColumnIndex(LOTTERY_ID));
            resultTime = cursorLottery.getString(cursorLottery.getColumnIndex(LOTTERY_TIME));
            resultValues = cursorLottery.getString(cursorLottery.getColumnIndex(LOTTERY_VALUES)).split(" ");
            if (cursorLottery.moveToNext()) {
                String[] secondValues = cursorLottery.getString(cursorLottery.getColumnIndex(LOTTERY_VALUES)).split(" ");
                for (int i = 0; i < resultValues.length; i++) {
                    resultValues[i] = resultValues[i] + " " + secondValues[i];
                }
                int max = 0;
                while (cursorLottery.moveToNext() && max < 30) {//最多遍历前面的31个记录
                    max++;
                    String[] values = cursorLottery.getString(cursorLottery.getColumnIndex(LOTTERY_VALUES)).split(" ");
                    for (int i = 0; i < lotteryLong; i++) {
                        if (type.equals(REMIND_TYPE_SINGLEPAIR)) {
                            if (Integer.valueOf(secondValues[i]) % 2 == Integer.valueOf(values[i]) % 2 && countLottery[1][i] != -1) {
                                countLottery[0][i]++;//如果和新开奖信息是一样的,切标记变量为0，那么连续期数+1，注意1代表连续2期，以此类推
                                resultValues[i] = resultValues[i] + " " + values[i];
                            } else {
                                countLottery[1][i] = -1;//如果和第一期不一样，将标记变量设为-1，下次不再对这一位数遍历
                            }
                        } else {
                            int x = Integer.valueOf(secondValues[i]);
                            if (smallMin <= x && x <= smallMax) {//如果是小
                                int y = Integer.valueOf(values[i]);
                                if (smallMin <= y && y <= smallMax && countLottery[1][i] != -1) {
                                    countLottery[0][i]++;//如果和新开奖信息是一样的,切标记变量为0，那么连续期数+1，注意1代表连续2期，以此类推
                                    resultValues[i] = resultValues[i] + " " + values[i];
                                } else {
                                    countLottery[1][i] = -1;//如果和第一期不一样，将标记变量设为-1，下次不再对这一位数遍历
                                    resultValues[i] = resultValues[i] + " " + values[i];
                                }
                            } else {//如果是大
                                int y = Integer.valueOf(values[i]);
                                if (bigMin <= y && y <= bigMax && countLottery[1][i] != -1) {
                                    countLottery[0][i]++;//如果和新开奖信息是一样的,切标记变量为0，那么连续期数+1，注意1代表连续2期，以此类推
                                    resultValues[i] = resultValues[i] + " " + values[i];
                                } else {
                                    countLottery[1][i] = -1;//如果和第一期不一样，将标记变量设为-1，下次不再对这一位数遍历
                                    resultValues[i] = resultValues[i] + " " + values[i];
                                }
                            }
                        }
                    }
                }
                for (int i = 0; i < lotteryLong; i++) {
                    if (count <= countLottery[0][i] + 1 && !firstValues[i].equals(secondValues[i])) {//如果连续的期数超过设定阈值，切已经发生了跳出，就返回这次事件的信息
//                        String name,int id,String type, String values,int position,int count,String time
                        resultPosition = i + 1;
                        resultCount = (countLottery[0][i] + 1);
                        Cursor cursor = diary.getRecordById("%%");
                        boolean isHave = false;
                        if (cursor.moveToFirst()) {
                            do {
//                                public boolean insertRecord(String name,int id,String type, String values,int position,int count,String time) {
                                //遍历Cursor对象
                                String nameHave = cursor.getString(cursor.getColumnIndex(Rule.RECORD_NAME));
                                String idHave = cursor.getString(cursor.getColumnIndex(Rule.RECORD_ID));
                                String typeHave = cursor.getString(cursor.getColumnIndex(Rule.RECORD_TYPE));
                                String positionHave = cursor.getString(cursor.getColumnIndex(Rule.RECORD_POSITION));
                                if (lotteryName.equals(nameHave) && idHave.equals(resultId) && typeHave.equals(type) && positionHave.equals(resultPosition + "")) {//防止重复插入相同的数据
                                    isHave = true;
                                }
                            } while (cursor.moveToNext());
                        }
                        cursor.close();
                        if (!isHave) {
                            diary.insertRecord(lotteryName, resultId, type, resultValues[i], resultPosition, resultCount, resultTime);
                        }
                    }
                }
            }
        }
        cursorLottery.close();
    }

    public static void saveRecord(DiaryDAO diaryDAO){
        Cursor cursor = diaryDAO.getRecordSet();
        cursor.moveToFirst();
        int shishicaiBigSmallCount= Integer.parseInt(cursor.getString(cursor.getColumnIndex(Rule.SET_RECORD_SHISHICAI_BIGSMALL)));
        int shishicaiEvenOddCount= Integer.parseInt(cursor.getString(cursor.getColumnIndex(Rule.SET_RECORD_SHISHICAI_EVENODD)));
        int pktenBigSmallCount= Integer.parseInt(cursor.getString(cursor.getColumnIndex(Rule.SET_RECORD_PKTEN_BIGSMALL)));
        int pktenEvenOddCount= Integer.parseInt(cursor.getString(cursor.getColumnIndex(Rule.SET_RECORD_PKTEN_EVENODD)));
        Rule.doSaveRecord(Rule.PKTEN,Rule.REMIND_TYPE_BIGSMALL,pktenBigSmallCount,diaryDAO);
        Rule.doSaveRecord(Rule.PKTEN,Rule.REMIND_TYPE_SINGLEPAIR,pktenEvenOddCount,diaryDAO);
        Rule.doSaveRecord(Rule.SHISHICAI,Rule.REMIND_TYPE_BIGSMALL,shishicaiBigSmallCount,diaryDAO);
        Rule.doSaveRecord(Rule.SHISHICAI,Rule.REMIND_TYPE_SINGLEPAIR,shishicaiEvenOddCount,diaryDAO);
    }
}
