package com.yhslib.lottery.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.yhslib.lottery.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ResultFragment extends Fragment {

    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private SimpleAdapter adapter;

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
        findView();
        init();
    }

    private void init() {
        String[] from = {"icon", "title", "period", "time", "result", "sum", "size", "singleOrDouble"};
        int[] to = {R.id.icon, R.id.title, R.id.period, R.id.time, R.id.result, R.id.sum, R.id.size, R.id.singleOrDouble};
        adapter = new SimpleAdapter(getActivity(), getData(), R.layout.list_lottery_result, from, to);
        listView.setAdapter(adapter);
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
    };

    private void findView() {
        listView = view.findViewById(R.id.listView);
        swipeRefreshLayout = view.findViewById(R.id.swipe);
    }

    public static ArrayList<Map<String, Object>> getData() {
        ArrayList<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("icon", R.mipmap.pkten);
        map.put("title", "北京PK拾");
        map.put("period", "第" + "681585" + "期");
        map.put("time", "22:20:41");
        map.put("result", "1 2 3 4 5 6 7 8 9 10");
        map.put("sum", "20");
        map.put("size", "大");
        map.put("singleOrDouble", "单");
        data.add(map);
        map = new HashMap<>();
        map.put("icon", R.mipmap.shishicai);
        map.put("title", "重庆时时彩");
        map.put("period", "第" + "681585" + "期");
        map.put("time", "22:20:41");
        map.put("result", "1 2 3 4 5");
        map.put("sum", "20");
        map.put("size", "大");
        map.put("singleOrDouble", "单");
        data.add(map);
        return data;
    }
}
