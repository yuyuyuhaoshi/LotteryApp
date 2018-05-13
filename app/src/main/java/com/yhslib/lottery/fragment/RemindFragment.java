package com.yhslib.lottery.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.yhslib.lottery.R;
import com.yhslib.lottery.activity.MainActivity;
import com.yhslib.lottery.utils.CustomDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RemindFragment extends Fragment {

    private View view;
    private ListView listView;
    private SimpleAdapter adapter;
    private CustomDialog dialog;

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
        String[] from = {"icon", "title", "state", "count"};
        int[] to = {R.id.icon, R.id.title, R.id.state, R.id.count};
        adapter = new SimpleAdapter(getActivity(), getData(), R.layout.list_remind, from, to);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                remind_dialog();
            }
        });
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                MenuInflater menuInflater = getActivity().getMenuInflater();
                menuInflater.inflate(R.menu.menu_list, menu);
            }
        });
    }

    private void findView() {
        listView = view.findViewById(R.id.listView);
    }

    private void remind_dialog() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.cancel_btn:
                        dialog.dismiss();
                        break;
                    case R.id.ok_btn:
                        Toast.makeText(getActivity(), "点击了确定", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        break;
                }
            }
        };

        CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
        dialog = builder
                .style(R.style.Dialog)
                .heightDimenRes(R.dimen.dialog_height)
                .widthDimenRes(R.dimen.dialog_width)
                .cancelTouchout(false)
                .view(R.layout.dialog_remind)
                .addViewOnclick(R.id.cancel_btn, listener)
                .addViewOnclick(R.id.ok_btn, listener)
                .build();
        dialog.show();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                Toast.makeText(getActivity(), "点击了删除", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    public static ArrayList<Map<String, Object>> getData() {
        ArrayList<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("icon", R.mipmap.pkten);
        map.put("title", "北京PK拾");
        map.put("state", "开启中");
        map.put("count", "连走" + "8" + "期");
        data.add(map);
        map = new HashMap<>();
        map.put("icon", R.mipmap.pkten);
        map.put("title", "北京PK拾");
        map.put("state", "已关闭");
        map.put("count", "连走" + "9" + "期");
        data.add(map);
        map = new HashMap<>();
        map.put("icon", R.mipmap.shishicai);
        map.put("title", "重庆时时彩");
        map.put("state", "已关闭");
        map.put("count", "连走" + "9" + "期");
        data.add(map);
        return data;
    }

}
