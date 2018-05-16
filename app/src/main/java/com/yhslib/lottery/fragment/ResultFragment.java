package com.yhslib.lottery.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yhslib.lottery.R;
import com.yhslib.lottery.config.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class ResultFragment extends Fragment {
    private final String TAG = "ResultFragment";
    private final int REFRESH_NEXT_TIME_MESSAGE = 999;
    private final int REFRESH_DATA_MESSAGE = 988;
    private final int REFRESH_NEXT_TIME_DELAY = 1000; // 一秒
    private final int REFRESH_DATA_DELAY = 30000;

    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView beijingTimeTxt;
    private TextView beijingPeriodTxt;
    private TextView beijingResultTxt;
    private TextView beijingSumTxt;
    private TextView beijingSizeTxt;
    private TextView beijingSingleOrDoubleTxt;
    private TextView beijingNextTime;

    private TextView shiShiCaiTimeTxt;
    private TextView shiShiCaiPeriodTxt;
    private TextView shiShiCaiResultTxt;
    private TextView shiShiCaiSumTxt;
    private TextView shiShiCaiSizeTxt;
    private TextView shiShiCaiSingleOrDoubleTxt;
    private TextView shiShiCaiNextTime;

    private MyHandler myHandler;

    public static ResultFragment newInstance() {
        Bundle args = new Bundle();
        ResultFragment fragment = new ResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_result, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myHandler = new MyHandler();
        findView();
        init();
        fetchPkTenData();
        fetchShiShiCaiData();
        Message messageNextTime = myHandler.obtainMessage(REFRESH_NEXT_TIME_MESSAGE);
        if (messageNextTime != null) {
            myHandler.sendMessage(messageNextTime);
        }
        Message messageRefreshData = myHandler.obtainMessage(REFRESH_DATA_MESSAGE);
        if (messageRefreshData != null) {
            myHandler.sendMessage(messageRefreshData);
        }
    }

    private void findView() {
        swipeRefreshLayout = view.findViewById(R.id.swipe);

        beijingTimeTxt = view.findViewById(R.id.beijing_time);
        beijingPeriodTxt = view.findViewById(R.id.beijing_period);
        beijingResultTxt = view.findViewById(R.id.beijing_result);
        beijingSumTxt = view.findViewById(R.id.beijing_sum);
        beijingSizeTxt = view.findViewById(R.id.beijing_size);
        beijingSingleOrDoubleTxt = view.findViewById(R.id.beijing_singleOrDouble);
        beijingNextTime = view.findViewById(R.id.beijing_next_time);

        shiShiCaiTimeTxt = view.findViewById(R.id.chongqing_time);
        shiShiCaiPeriodTxt = view.findViewById(R.id.chongqing_period);
        shiShiCaiResultTxt = view.findViewById(R.id.chongqing_result);
        shiShiCaiSumTxt = view.findViewById(R.id.chongqing_sum);
        shiShiCaiSizeTxt = view.findViewById(R.id.chongqing_size);
        shiShiCaiSingleOrDoubleTxt = view.findViewById(R.id.chongqing_singleOrDouble);
        shiShiCaiNextTime = view.findViewById(R.id.chongqing_next_time);
    }

    private void init() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPkTenData();
                fetchShiShiCaiData();
            }

        });
    }


    private class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_NEXT_TIME_MESSAGE:
                    String countDownTimeOfBeijing = formatBeijingNextTime();
                    beijingNextTime.setText(countDownTimeOfBeijing);
                    String countDownTimeOfShiShiCai = formatShiShiCaiNextTime();
                    shiShiCaiNextTime.setText(countDownTimeOfShiShiCai);
                    sendEmptyMessageDelayed(REFRESH_NEXT_TIME_MESSAGE, REFRESH_NEXT_TIME_DELAY);
                    break;
                case REFRESH_DATA_MESSAGE:
                    fetchPkTenData();
                    fetchShiShiCaiData();
                    sendEmptyMessageDelayed(REFRESH_DATA_MESSAGE, REFRESH_DATA_DELAY);
            }
        }
    }

    private String formatBeijingNextTime() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);

        int targetHour;
        int targetMinute;
        int targetSecond;

        int countDownHour;
        int countDownMinute;
        int countDownSecond;

        if (hour > 0 && hour < 9) {
            // 每天0点-9点
            targetHour = 9;
            targetMinute = 6;
            targetSecond = 59;

            countDownHour = targetHour - hour;
            countDownMinute = targetMinute - minute;
            countDownSecond = targetSecond - second;
            return countDownHour + ":" + countDownMinute + ":" + formatSecond(countDownSecond);
        }
        if (hour > 23 && minute > 56) {
            // 23:56 - 24:00
            return "今天开奖已结束";
        }

        targetMinute = ((minute - 1) / 5) * 5 + 5;
        targetSecond = 59;
        // Log.d(TAG, targetMinute + ":" + targetSecond);
        // Log.d(TAG, minute + ":" + second);
        countDownMinute = targetMinute - minute;
        countDownSecond = targetSecond - second;

        return countDownMinute + ":" + formatSecond(countDownSecond);
    }

    private String formatShiShiCaiNextTime() {
        // 现在的时间
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);

        // 下一期的开奖时间
        int targetHour;
        int targetMinute;
        int targetSecond;

        // 倒计时
        int countDownHour;
        int countDownMinute;
        int countDownSecond;

        if ((hour >= 0 && hour < 3) || (hour >= 22 && hour < 24)) {
            // 0-3点 22-24点 五分钟一期
            targetMinute = ((minute) / 5) * 5 + 5 - 1;
            targetSecond = 59;

            countDownMinute = targetMinute - minute;
            countDownSecond = targetSecond - second;
            return countDownMinute + ":" + formatSecond(countDownSecond);
        }
        if (hour >= 3 && hour < 10) {
            // 每天0点-9点
            // 等待10点整开奖
            targetHour = 9;
            targetMinute = 59;
            targetSecond = 59;

            countDownHour = targetHour - hour;
            countDownMinute = targetMinute - minute;
            countDownSecond = targetSecond - second;

            return countDownHour + ":" + countDownMinute + ":" + formatSecond(countDownSecond);
        }
        if (hour >= 10 && hour <= 21) {
            // 每天10点-21:59:59点
            // 每十分钟开奖一次
            targetMinute = ((minute) / 10) * 10 + 10 - 1;
            targetSecond = 59;
            countDownMinute = targetMinute - minute;
            countDownSecond = targetSecond - second;

            return countDownMinute + ":" + formatSecond(countDownSecond);

        }
        return "";
    }

    private String formatSecond(int i) {
        String second;
        if (i < 10) {
            second = "0" + i;
        } else {
            second = i + "";
        }
        return second;
    }


    private void fetchPkTenData() {
        String url = Url.PkTenNewestResult;
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                HashMap<String, Object> hm = parseJsonArray(response).get(0);
                String period = hm.get("period").toString();
                String result = hm.get("result").toString();
                String time = hm.get("time").toString();
                String[] timeArray = time.split("T");
                time = timeArray[0].substring(5) + " " + timeArray[1];
                beijingTimeTxt.setText(time);
                beijingPeriodTxt.setText(period);
                beijingResultTxt.setText(result);

                String[] resultArray = result.split(" ");
                int sum = 0;
                for (String i : resultArray) {
                    sum += Integer.parseInt(i);
                }
                beijingSumTxt.setText(sum + "");
                if (sum <= 100 / 2) {
                    beijingSizeTxt.setText("小");
                } else {
                    beijingSizeTxt.setText("大");
                }
                if (sum % 2 == 1) {
                    beijingSingleOrDoubleTxt.setText("单");
                } else {
                    beijingSingleOrDoubleTxt.setText("双");
                }
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                //Log.d(TAG, "刷新完毕");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                beijingResultTxt.setText("暂无数据,请稍后刷新");
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(stringRequest);
    }

    private void fetchShiShiCaiData() {
        String url = Url.ShiShiCaiNewestResult;
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                HashMap<String, Object> hm = parseJsonArray(response).get(0);
                // Log.d(TAG, parseJsonArray(response).toString());
                String period = hm.get("period").toString();
                period = period.substring(period.length() - 3);
                String result = hm.get("result").toString();
                String time = hm.get("time").toString();
                String[] timeArray = time.split("T");
                time = timeArray[0].substring(5) + " " + timeArray[1];
                shiShiCaiTimeTxt.setText(time);
                shiShiCaiPeriodTxt.setText(period);
                shiShiCaiResultTxt.setText(result);

                String[] resultArray = result.split(" ");
                int sum = 0;
                for (String i : resultArray) {
                    sum += Integer.parseInt(i);
                }
                shiShiCaiSumTxt.setText(sum + "");
                if (sum <= (45 + 1) / 2) {
                    shiShiCaiSizeTxt.setText("小");
                } else {
                    shiShiCaiSizeTxt.setText("大");
                }
                if (sum % 2 == 1) {
                    shiShiCaiSingleOrDoubleTxt.setText("单");
                } else {
                    shiShiCaiSingleOrDoubleTxt.setText("双");
                }
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                shiShiCaiResultTxt.setText("暂无数据,请稍后刷新");
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(stringRequest);
    }

    private ArrayList<HashMap<String, Object>> parseJsonArray(String responseStr) {
        ArrayList<HashMap<String, Object>> resultList = new ArrayList<>();
        try {
            JSONObject jsonobject = new JSONObject(responseStr);

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("period", jsonobject.getLong("period"));
            hashMap.put("time", jsonobject.getString("time"));
            hashMap.put("result", jsonobject.getString("result"));
            resultList.add(hashMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
