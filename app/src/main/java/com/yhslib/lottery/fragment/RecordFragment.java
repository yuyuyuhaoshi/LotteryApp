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
        String[] from = {"title"};
        int[] to = {R.id.test};
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
        map.put("title", "Record1");
        data.add(map);
        map = new HashMap<>();
        map.put("title", "Record2");
        data.add(map);
        map = new HashMap<>();
        map.put("title", "Record3");
        data.add(map);
        return data;
    }

}
