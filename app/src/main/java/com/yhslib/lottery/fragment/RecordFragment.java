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


public class RecordFragment extends Fragment {

    private View view;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SimpleAdapter adapter;

    public static RecordFragment newInstance() {
        Bundle args = new Bundle();
        RecordFragment fragment = new RecordFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_record, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView();
        init();
    }

    private void init() {
        String[] from = {"icon", "title", "no", "continued", "result", "period", "time"};
        int[] to = {R.id.icon, R.id.title, R.id.no, R.id.continued, R.id.result, R.id.period, R.id.time};
        adapter = new SimpleAdapter(getActivity(), getData(), R.layout.list_record, from, to);
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

        ;
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
        map.put("no", "第" + "1" + "球");
        map.put("continued", "连走" + "8" + "期");
        map.put("result", "大大大大大大大大大大大大大大大");
        map.put("period", "第" + "xxxxxxx" + "期");
        map.put("time", "2018/10/22 22:22:22");
        data.add(map);
        map = new HashMap<>();
        map.put("icon", R.mipmap.shishicai);
        map.put("title", "重庆时时彩");
        map.put("no", "第" + "2" + "球");
        map.put("continued", "连走" + "10" + "期");
        map.put("result", "大大大大大大大大大大大大大大大");
        map.put("period", "第" + "xxxxxxx" + "期");
        map.put("time", "2018/10/22 22:22:22");
        data.add(map);
        return data;
    }

}
