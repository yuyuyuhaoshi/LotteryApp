package com.yhslib.lottery.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.yhslib.lottery.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RemindFragment extends Fragment {

    private View view;
    private ListView listView;
    private SimpleAdapter adapter;

    public static RemindFragment newInstance() {
        Bundle args = new Bundle();
        RemindFragment fragment = new RemindFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_remind, container, false);
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
        adapter = new SimpleAdapter(getActivity(), getData(), R.layout.list_remind, from, to);
        listView.setAdapter(adapter);

    }

    private void findView() {
        listView = view.findViewById(R.id.listView);
    }

    public static ArrayList<Map<String, Object>> getData() {
        ArrayList<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("title", "Remind1");
        data.add(map);
        map = new HashMap<>();
        map.put("title", "Remind2");
        data.add(map);
        map = new HashMap<>();
        map.put("title", "Remind3");
        data.add(map);
        return data;
    }

}
