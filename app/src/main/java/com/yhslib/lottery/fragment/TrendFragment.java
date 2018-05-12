package com.yhslib.lottery.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.yhslib.lottery.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;


public class TrendFragment extends Fragment {

    private View view;
    private TabLayout tabLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<String[]> contentList;
    private ListView listView;
    private SimpleAdapter adapter;
    private LinearLayout header_view;

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
        findView();
        init();
        setData();
    }

    private void setData() {
//        View v = LayoutInflater.from(getActivity()).inflate(R.layout.view_trend_header_pkshi, null);
//        header_view.addView(v);
//        contentList = new ArrayList<>();
//        contentList.add(new String[]{"101", "单", "双", "单", "单", "双", "单", "单", "单", "单", "单"});
//        contentList.add(new String[]{"101", "单", "单", "单", "单", "单", "单", "双", "单", "单", "单"});
//        contentList.add(new String[]{"101", "单", "单", "单", "单", "单", "单", "单", "单", "单", "单"});
//        contentList.add(new String[]{"101", "双", "单", "单", "单", "单", "单", "单", "单", "双", "单"});
//        contentList.add(new String[]{"101", "单", "单", "单", "单", "双", "单", "双", "单", "单", "单"});
//        contentList.add(new String[]{"101", "双", "双", "单", "单", "单", "单", "单", "单", "单", "单"});
//        contentList.add(new String[]{"101", "单", "单", "单", "单", "单", "单", "单", "单", "单", "单"});
//        contentList.add(new String[]{"101", "单", "单", "单", "单", "单", "双", "单", "单", "单", "单"});
//        contentList.add(new String[]{"101", "单", "单", "单", "双", "单", "单", "单", "单", "单", "单"});
//        contentList.add(new String[]{"101", "单", "单", "双", "双", "单", "单", "单", "单", "单", "单"});
//        contentList.add(new String[]{"101", "单", "单", "单", "单", "单", "单", "单", "单", "单", "单"});
//        contentList.add(new String[]{"101", "单", "单", "单", "单", "单", "单", "单", "单", "单", "单"});
//        contentList.add(new String[]{"101", "单", "单", "单", "单", "单", "单", "单", "单", "单", "单"});
//        contentList.add(new String[]{"101", "单", "单", "单", "单", "双", "单", "单", "单", "单", "单"});
//        contentList.add(new String[]{"101", "单", "单", "单", "单", "双", "双", "单", "单", "单", "单"});
//        contentList.add(new String[]{"101", "单", "单", "单", "单", "单", "单", "单", "单", "单", "单"});
//        contentList.add(new String[]{"101", "单", "单", "单", "单", "单", "单", "单", "单", "单", "单"});
//        contentList.add(new String[]{"101", "单", "双", "单", "单", "单", "单", "单", "单", "单", "单"});
//        contentList.add(new String[]{"101", "单", "单", "单", "单", "单", "单", "单", "单", "单", "单"});
//        contentList.add(new String[]{"101", "单", "单", "单", "单", "双", "单", "单", "单", "单", "单"});
//        contentList.add(new String[]{"101", "双", "单", "单", "单", "单", "双", "单", "单", "单", "单"});
//        contentList.add(new String[]{"101", "单", "单", "单", "单", "单", "单", "单", "单", "单", "单"});
//        contentList.add(new String[]{"101", "单", "单", "单", "单", "单", "单", "单", "单", "单", "单"});
//        contentList.add(new String[]{"101", "单", "单", "单", "单", "单", "单", "单", "单", "单", "单"});
//        contentList.add(new String[]{"101", "单", "单", "单", "单", "单", "单", "单", "单", "单", "单"});
//        contentList.add(new String[]{"101", "单", "单", "双", "单", "单", "单", "单", "单", "单", "单"});
//        contentList.add(new String[]{"101", "单", "双", "单", "单", "单", "单", "单", "单", "单", "单"});
//        contentList.add(new String[]{"101", "单", "单", "单", "单", "单", "单", "单", "单", "单", "单"});
//        contentList.add(new String[]{"101", "单", "单", "单", "单", "单", "双", "双", "单", "单", "单"});
//        contentList.add(new String[]{"101", "单", "双", "单", "单", "单", "单", "单", "单", "单", "单"});
//        ArrayList<Map<String, Object>> data = new ArrayList<>();
//        for (int i = 0; i < contentList.size(); i++) {
//            String[] row = contentList.get(i);
//            Map<String, Object> map = new HashMap<>();
//            map.put("no", row[0]);
//            map.put("data1", row[1]);
//            map.put("data2", row[2]);
//            map.put("data3", row[3]);
//            map.put("data4", row[4]);
//            map.put("data5", row[5]);
//            map.put("data6", row[6]);
//            map.put("data7", row[7]);
//            map.put("data8", row[8]);
//            map.put("data9", row[9]);
//            map.put("data10", row[10]);
//            data.add(map);
//        }
//        String[] from = {"no", "data1", "data2", "data3", "data4", "data5", "data6", "data7", "data8", "data9", "data10"};
//        int[] to = {R.id.no, R.id.data1, R.id.data2, R.id.data3, R.id.data4, R.id.data5, R.id.data6, R.id.data7, R.id.data8, R.id.data9, R.id.data10};
//        adapter = new SimpleAdapter(getActivity(), data, R.layout.list_trend_pkten, from, to) {
//            @Override
//            public int getItemViewType(int position) {
//                return (position % 2);
//            }
//
//            @Override
//            public int getViewTypeCount() {
//                return 2;
//            }
//
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                View v;
//                v = super.getView(position, convertView, parent);
//                if (v != null && position % 2 == 0) {
//                    v.setBackgroundResource(R.color.color_trend_bg);
//                }
//                for (int j = 0; j < ((LinearLayout) v).getChildCount(); j++) {
//                    TextView textView = (TextView) ((LinearLayout) v).getChildAt(j);
//                    if (textView.getText().equals("单") || textView.getText().equals("小")) {
//                        textView.setTextColor(getResources().getColor(R.color.color_trend_text1));
//                    } else if (textView.getText().equals("双") || textView.getText().equals("大")) {
//                        textView.setTextColor(getResources().getColor(R.color.color_trend_text2));
//                    }
//                }
//                return v;
//            }
//        };
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.view_trend_header_shishicai, null);
        header_view.addView(v);
        contentList = new ArrayList<>();
        contentList.add(new String[]{"101", "单", "双", "单", "单", "双"});
        contentList.add(new String[]{"101", "单", "单", "单", "单", "单"});
        contentList.add(new String[]{"101", "单", "单", "单", "单", "单"});
        contentList.add(new String[]{"101", "双", "单", "单", "单", "单"});
        contentList.add(new String[]{"101", "单", "单", "单", "单", "双"});
        contentList.add(new String[]{"101", "双", "双", "单", "单", "单"});
        contentList.add(new String[]{"101", "单", "单", "单", "单", "单"});
        contentList.add(new String[]{"101", "单", "单", "单", "单", "单"});
        contentList.add(new String[]{"101", "单", "单", "单", "双", "单"});
        contentList.add(new String[]{"101", "单", "单", "双", "双", "单"});
        contentList.add(new String[]{"101", "单", "单", "单", "单", "单"});
        contentList.add(new String[]{"101", "单", "双", "单", "单", "双"});
        contentList.add(new String[]{"101", "单", "单", "单", "单", "单"});
        contentList.add(new String[]{"101", "单", "单", "单", "单", "单"});
        contentList.add(new String[]{"101", "双", "单", "单", "单", "单"});
        contentList.add(new String[]{"101", "单", "单", "单", "单", "双"});
        contentList.add(new String[]{"101", "双", "双", "单", "单", "单"});
        contentList.add(new String[]{"101", "单", "单", "单", "单", "单"});
        contentList.add(new String[]{"101", "单", "单", "单", "单", "单"});
        contentList.add(new String[]{"101", "单", "单", "单", "双", "单"});
        contentList.add(new String[]{"101", "单", "单", "双", "双", "单"});
        contentList.add(new String[]{"101", "单", "单", "单", "单", "单"});
        contentList.add(new String[]{"101", "单", "双", "单", "单", "双"});
        contentList.add(new String[]{"101", "单", "单", "单", "单", "单"});
        contentList.add(new String[]{"101", "单", "单", "单", "单", "单"});
        contentList.add(new String[]{"101", "双", "单", "单", "单", "单"});
        contentList.add(new String[]{"101", "单", "单", "单", "单", "双"});
        contentList.add(new String[]{"101", "双", "双", "单", "单", "单"});
        contentList.add(new String[]{"101", "单", "单", "单", "单", "单"});
        contentList.add(new String[]{"101", "单", "单", "单", "单", "单"});
        ArrayList<Map<String, Object>> data = new ArrayList<>();
        for (int i = 0; i < contentList.size(); i++) {
            String[] row = contentList.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("no", row[0]);
            map.put("data1", row[1]);
            map.put("data2", row[2]);
            map.put("data3", row[3]);
            map.put("data4", row[4]);
            map.put("data5", row[5]);
            data.add(map);
        }
        String[] from = {"no", "data1", "data2", "data3", "data4", "data5"};
        int[] to = {R.id.no, R.id.data1, R.id.data2, R.id.data3, R.id.data4, R.id.data5};
        adapter = new SimpleAdapter(getActivity(), data, R.layout.list_trend_shishicai, from, to) {
            @Override
            public int getItemViewType(int position) {
                return (position % 2);
            }

            @Override
            public int getViewTypeCount() {
                return 2;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v;
                v = super.getView(position, convertView, parent);
                if (v != null && position % 2 == 0) {
                    v.setBackgroundResource(R.color.color_trend_bg);
                }
                for (int j = 0; j < ((LinearLayout) v).getChildCount(); j++) {
                    TextView textView = (TextView) ((LinearLayout) v).getChildAt(j);
                    if (textView.getText().equals("单") || textView.getText().equals("小")) {
                        textView.setTextColor(getResources().getColor(R.color.color_trend_text1));
                    } else if (textView.getText().equals("双") || textView.getText().equals("大")) {
                        textView.setTextColor(getResources().getColor(R.color.color_trend_text2));
                    }
                }
                return v;
            }
        };
        listView.setAdapter(adapter);
    }

    private void init() {
        tabLayout.addTab(tabLayout.newTab().setText("大小"));
        tabLayout.addTab(tabLayout.newTab().setText("单双"));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        Toast.makeText(getActivity(), "点击了大小", Toast.LENGTH_SHORT).show();
                        break;

                    case 1:
                        Toast.makeText(getActivity(), "点击了单双", Toast.LENGTH_SHORT).show();
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
                mHandler.sendEmptyMessageDelayed(111, 2000);
            }
        });
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 111:
                    Toast.makeText(getActivity(), "刷新结束", Toast.LENGTH_SHORT).show();
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    break;

            }
        }

        ;
    };

    private void findView() {
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        tabLayout = view.findViewById(R.id.tabLayout);
        listView = view.findViewById(R.id.listView);
        header_view = view.findViewById(R.id.view);
    }

}
