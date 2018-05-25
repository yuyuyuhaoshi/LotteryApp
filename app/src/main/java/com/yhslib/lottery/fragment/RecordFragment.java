package com.yhslib.lottery.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.yhslib.lottery.R;
import com.yhslib.lottery.sqlittle.DiaryDAO;
import com.yhslib.lottery.utils.Rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RecordFragment extends Fragment {
    private View view;
    private ListView listViewRecord;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SimpleAdapter adapter;
    private DiaryDAO diaryDAO;
    private boolean isThisFragment = false;
    public static int min = 0;

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

    public void init() {
        String[] from = {"_id", "icon", "title", "type", "position", "result", "id", "time"};
        int[] to = {R.id.id_record, R.id.icon, R.id.title, R.id.no, R.id.continued, R.id.result, R.id.period, R.id.time};
        diaryDAO = new DiaryDAO(getActivity());
        adapter = new SimpleAdapter(getActivity(), getData(min), R.layout.list_record, from, to);
        listViewRecord.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.sendEmptyMessageDelayed(111, 2000);
            }
        });
        listViewRecord.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                isThisFragment = true;
                MenuInflater menuInflater = getActivity().getMenuInflater();
                menuInflater.inflate(R.menu.menu_list, menu);
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (isThisFragment) {//判断当前fragment是否可见，防止错乱
            isThisFragment = false;
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int position = (int) info.id;
            int fetch = 0;
            if (listViewRecord.getLastVisiblePosition() >= listViewRecord.getChildCount())//get到的child只能是屏幕显示的，如第100个child，在屏幕里面当前是第2个，那么应当是第二个child而非100
            {
                fetch = listViewRecord.getChildCount() - 1 - (listViewRecord.getLastVisiblePosition() - position);
            } else {
                fetch = position;
            }
            TextView textView = listViewRecord.getChildAt(fetch).findViewById(R.id.id_record);
            int id = Integer.parseInt(textView.getText().toString());
            switch (item.getItemId()) {
                case R.id.delete:
                    Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                    diaryDAO.deleteRecord(id);
                    init();
                    break;
            }
        }
        return super.onContextItemSelected(item);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 111:
                    init();
                    Toast.makeText(getActivity(), "记录刷新结束", Toast.LENGTH_SHORT).show();
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    break;

            }
        }

        ;
    };


    private void findView() {
        listViewRecord = view.findViewById(R.id.listViewRecord);
        swipeRefreshLayout = view.findViewById(R.id.swipe);
    }

    //* @参数,name 彩票名，id跳出期号，type类型，value具体内容，position第几个球，count连走了几期，time跳出时间
    public ArrayList<Map<String, Object>> getData(int min) {
        String[] from = {"_id", "icon", "title", "type", "position", "result", "id", "time"};
        //"icon"图标, "title"彩票名字, "type"类型, "position"发生位置, "result"具体内容, "id"跳出id, "time"//跳出时间
        ArrayList<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> map = new HashMap<>(); //这个new HashMap<>()不可以省略，否则会报空指针
        Cursor cursor = diaryDAO.getRecordById("%%");
        if (cursor.moveToFirst()) {
            do {
                //遍历Cursor对象
                String count = cursor.getString(cursor.getColumnIndex(Rule.RECORD_COUNT));
                if (Integer.parseInt(count) >= min) {
                    String name = cursor.getString(cursor.getColumnIndex(Rule.RECORD_NAME));
                    String id = cursor.getString(cursor.getColumnIndex(Rule.RECORD_ID));
                    String type = cursor.getString(cursor.getColumnIndex(Rule.RECORD_TYPE));
                    String values = cursor.getString(cursor.getColumnIndex(Rule.RECORD_VALUE));
                    String position = cursor.getString(cursor.getColumnIndex(Rule.RECORD_POSITION));
                    String time = cursor.getString(cursor.getColumnIndex(Rule.RECORD_TIME));
                    String[] value = values.split(" ");
                    int x = Integer.parseInt(value[0]);
                    if (type.equals(Rule.REMIND_TYPE_BIGSMALL)) {
                        if (name.equals(Rule.PKTEN)) {
                            if (x > 5) {
                                type = "小";
                            } else {
                                type = "大";
                            }
                        } else {
                            if (x > 4) {
                                type = "小";
                            } else {
                                type = "大";
                            }
                        }

                    }
                    if (type.equals(Rule.REMIND_TYPE_SINGLEPAIR)) {
                        if (x % 2 != 0) {
                            type = "双";
                        } else {
                            type = "单";
                        }
                    }

                    map = new HashMap<>();
                    map.put("_id", cursor.getString(cursor.getColumnIndex(Rule.ID)));
                    if (name.equals(Rule.PKTEN)) {
                        map.put("icon", R.mipmap.pkten);
                        map.put("title", "北京PK拾");
                    } else {
                        map.put("icon", R.mipmap.shishicai);
                        map.put("title", "重庆时时彩");
                    }
                    map.put("id", "跳出期号：" + id);
                    map.put("type", "连走" + count + "个" + type);
                    map.put("result", values);
                    map.put("position", "第" + position + "位");
                    map.put("time", time);
                    data.add(map);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

//    public void saveRecord(){
//        Cursor cursor = diaryDAO.getRecordSet();
//        cursor.moveToFirst();
//        int shishicaiBigSmallCount= Integer.parseInt(cursor.getString(cursor.getColumnIndex(Rule.SET_RECORD_SHISHICAI_BIGSMALL)));
//        int shishicaiEvenOddCount= Integer.parseInt(cursor.getString(cursor.getColumnIndex(Rule.SET_RECORD_SHISHICAI_EVENODD)));
//        int pktenBigSmallCount= Integer.parseInt(cursor.getString(cursor.getColumnIndex(Rule.SET_RECORD_PKTEN_BIGSMALL)));
//        int pktenEvenOddCount= Integer.parseInt(cursor.getString(cursor.getColumnIndex(Rule.SET_RECORD_PKTEN_EVENODD)));
//        Rule.doSaveRecord(Rule.PKTEN,Rule.REMIND_TYPE_BIGSMALL,pktenBigSmallCount,diaryDAO);
//        Rule.doSaveRecord(Rule.PKTEN,Rule.REMIND_TYPE_SINGLEPAIR,pktenEvenOddCount,diaryDAO);
//        Rule.doSaveRecord(Rule.SHISHICAI,Rule.REMIND_TYPE_BIGSMALL,shishicaiBigSmallCount,diaryDAO);
//        Rule.doSaveRecord(Rule.SHISHICAI,Rule.REMIND_TYPE_SINGLEPAIR,shishicaiEvenOddCount,diaryDAO);
//        init();
//    }

}
