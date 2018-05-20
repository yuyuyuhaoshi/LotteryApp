package com.yhslib.lottery.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yhslib.lottery.Application.MyApplication;
import com.yhslib.lottery.R;
import com.yhslib.lottery.config.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;


public class TrendFragment extends Fragment {
    private String TAG = "TrendFragment";
    private String data;
    private final int REFRESH_DATA_MESSAGE = 546;
    private final int REFRESH_DATA_DELAY = 30000;
    private final int TABLE_ITEM_COUNT = 0;
    private final int TABLE_ITEM_SIZE = 1;
    private final int TABLE_ITEM_SINGLE_OR_DOUBLE = 2;
    private int selectTableItem = 0;

    private View view;
    private TabLayout tabLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private SimpleAdapter adapter;
    private LinearLayout header_view;

    private static final int LOTTERY_BEIJING = 0;
    private static final int LOTTERY_CHONGQING = 1;
    private int trend_lottery_type = LOTTERY_BEIJING;


    private MyHandler myHandler;

    public static TrendFragment newInstance() {
        Bundle args = new Bundle();
        TrendFragment fragment = new TrendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trend, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myHandler = new MyHandler();
        findView();
        init();
        fetchPkTenData();
        Message messageRefreshData = myHandler.obtainMessage(REFRESH_DATA_MESSAGE);
        if (messageRefreshData != null) {
            myHandler.sendMessage(messageRefreshData);
        }
    }

    private void findView() {
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        tabLayout = view.findViewById(R.id.tabLayout);
        listView = view.findViewById(R.id.listView);
        header_view = view.findViewById(R.id.view);
    }

    private void init() {
        tabLayout.addTab(tabLayout.newTab().setText("号码"));
        tabLayout.addTab(tabLayout.newTab().setText("大小"));
        tabLayout.addTab(tabLayout.newTab().setText("单双"));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        selectTableItem = TABLE_ITEM_COUNT;
                        // TODO 根据所选北京PK10 还是 重庆时时彩刷新数据 if 判断
                        if (trend_lottery_type == LOTTERY_BEIJING) {
                            setPkTenAdapter(data);
                        } else {
                            setShiShiCaiAdapter(data);
                        }
                        break;
                    case 1:
                        selectTableItem = TABLE_ITEM_SIZE;
                        // TODO 根据所选北京PK10 还是 重庆时时彩刷新数据 if 判断
                        if (trend_lottery_type == LOTTERY_BEIJING) {
                            setPkTenAdapter(data);
                        } else {
                            setShiShiCaiAdapter(data);
                        }
                        break;
                    case 2:
                        selectTableItem = TABLE_ITEM_SINGLE_OR_DOUBLE;
                        // TODO 根据所选北京PK10 还是 重庆时时彩刷新数据 if 判断
                        if (trend_lottery_type == LOTTERY_BEIJING) {
                            setPkTenAdapter(data);
                        } else {
                            setShiShiCaiAdapter(data);
                        }
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // TODO 根据所选北京PK10 还是 重庆时时彩刷新数据 if 判断
                fetchPkTenData();
                fetchShiShiCaiData();

            }
        });
    }

    public void cutLotteryType() {
        tabLayout.getTabAt(0).select();
        if (trend_lottery_type == LOTTERY_BEIJING) {
            trend_lottery_type = LOTTERY_CHONGQING;
            fetchShiShiCaiData();
            // setShiShiCaiAdapter(data);

        } else {
            trend_lottery_type = LOTTERY_BEIJING;
            fetchPkTenData();
            // setPkTenAdapter(data);
        }
    }


    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_DATA_MESSAGE:

                    fetchPkTenData();

                    fetchShiShiCaiData();

                    sendEmptyMessageDelayed(REFRESH_DATA_MESSAGE, REFRESH_DATA_DELAY);
                    break;
                default:
                    break;
            }
        }
    }

    private void fetchPkTenData() {
        String url = Url.PkTenList;
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                data = response;
                // Log.d(TAG, parseJsonArray(data).toString());
                if (trend_lottery_type == LOTTERY_BEIJING) {
                    setPkTenAdapter(data);
                }
                // Toast.makeText(getActivity(), "刷新成功", Toast.LENGTH_SHORT).show();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "数据刷新失败", Toast.LENGTH_SHORT).show();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        RequestQueue queue = MyApplication.getRequestQueue();
        queue.add(stringRequest);
    }

    private void fetchShiShiCaiData() {
        String url = Url.ShiShiCaiList;
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                data = response;
                if (trend_lottery_type == LOTTERY_CHONGQING) {
                    setShiShiCaiAdapter(response);
                }

                // Toast.makeText(getActivity(), "刷新成功", Toast.LENGTH_SHORT).show();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "数据刷新失败", Toast.LENGTH_SHORT).show();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        RequestQueue queue = MyApplication.getRequestQueue();
        queue.add(stringRequest);
    }

    private void setPkTenAdapter(String data) {
        ArrayList<HashMap<String, Object>> hm = parseJsonArray(data);
        String[] from = {"period", "data1", "data2", "data3", "data4", "data5", "data6", "data7", "data8", "data9", "data10"};
        int[] to = {R.id.no, R.id.data1, R.id.data2, R.id.data3, R.id.data4, R.id.data5, R.id.data6, R.id.data7, R.id.data8, R.id.data9, R.id.data10};

        adapter = new SimpleAdapter(getActivity(), hm, R.layout.list_trend_pkten, from, to);
        listView.setAdapter(adapter);
    }

    private void setShiShiCaiAdapter(String data) {
        ArrayList<HashMap<String, Object>> hm = parseJsonArray(data);
        String[] from = {"period", "data1", "data2", "data3", "data4", "data5"};
        int[] to = {R.id.no, R.id.data1, R.id.data2, R.id.data3, R.id.data4, R.id.data5};

        adapter = new SimpleAdapter(getActivity(), hm, R.layout.list_trend_shishicai, from, to);
        listView.setAdapter(adapter);
    }

    private ArrayList<HashMap<String, Object>> parseJsonArray(String responseStr) {
        ArrayList<HashMap<String, Object>> resultList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(responseStr);
            JSONArray resultsArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject dataObject = (JSONObject) resultsArray.opt(i);
                HashMap<String, Object> hashMap = new HashMap<>();
                if (trend_lottery_type == LOTTERY_BEIJING) {
                    hashMap.put("period", dataObject.getString("period"));
                } else {
                    String period = dataObject.getString("period") + "";
                    period = period.substring(period.length() - 3);
                    hashMap.put("period", period);
                }

                hashMap.put("result", dataObject.getString("result"));
                String s = dataObject.getString("result");
                String[] resultArray = s.split(" ");

                for (int j = 0; j < resultArray.length; j++) {
                    if (selectTableItem == 0) {
                        hashMap.put("data" + (j + 1), resultArray[j]);
                    } else if (selectTableItem == 1) {
                        // TODO 重庆时时彩 数值范围与北京Pk10不同 多加个判断
                        if (trend_lottery_type == LOTTERY_BEIJING) {
                            hashMap.put("data" + (j + 1), Integer.parseInt(resultArray[j]) >= 6 ? '大' : '小');
                        } else {
                            hashMap.put("data" + (j + 1), Integer.parseInt(resultArray[j]) >= 5 ? '大' : '小');
                        }
                    } else if (selectTableItem == 2) {
                        hashMap.put("data" + (j + 1), Integer.parseInt(resultArray[j]) % 2 == 0 ? '双' : '单');
                    }

                }
                resultList.add(hashMap);
            }
        } catch (JSONException e) {
        }
        return resultList;
    }
}
