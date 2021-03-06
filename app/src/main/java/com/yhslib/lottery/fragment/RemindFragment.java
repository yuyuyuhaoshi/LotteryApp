package com.yhslib.lottery.fragment;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.yhslib.lottery.R;
import com.yhslib.lottery.activity.MainActivity;
import com.yhslib.lottery.sqlittle.DiaryDAO;
import com.yhslib.lottery.sqlittle.NotesLoader;
import com.yhslib.lottery.utils.CustomDialog;
import com.yhslib.lottery.utils.Rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RemindFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private View view;
    private ListView listView;
    private SimpleAdapter adapter;
    private CustomDialog dialog;
    public DiaryDAO diary;
    private SimpleCursorAdapter simpleCursorAdapter;
    private boolean isThisFragment = false;
    int countPkten = 999;
    int countShishicai = 999;

    public static RemindFragment newInstance() {
        Bundle args = new Bundle();
        RemindFragment fragment = new RemindFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_remind, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    public void init() {
        findView();
        diary = new DiaryDAO(getActivity());
        String[] from = {"_id", "icon", "title", "state", "count", "type"};
        int[] to = {R.id.id, R.id.icon, R.id.title, R.id.state, R.id.count, R.id.type};
        adapter = new SimpleAdapter(getActivity(), getData(), R.layout.list_remind, from, to);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int fetch = 0;
                if (listView.getLastVisiblePosition() >= listView.getChildCount())//get到的child只能是屏幕显示的，如第100个child，在屏幕里面当前是第2个，那么应当是第二个child而非100
                {
                    fetch = listView.getChildCount() - 1 - (listView.getLastVisiblePosition() - position);
                } else {
                    fetch = position;
                }
                TextView textView = listView.getChildAt(fetch).findViewById(R.id.id);
                int id1 = Integer.parseInt(textView.getText().toString());
                remind_dialog(id1);
//                diary.insertLotteryHistory(Rule.TABLE_NAME_PKTEN,100,"10 9 8 7 6 5 4 3 2 1","2016/10/18");
//                for (int i = 1; i < 100; i++) {
//                    diary.insertLotteryHistory(Rule.TABLE_NAME_PKTEN,i,"1 2 3 4 5 6 7 8 9 10","2016/10/18");
//                }
//                sendeMinder();
            }
        });

        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                isThisFragment = true;
                MenuInflater menuInflater = getActivity().getMenuInflater();
                menuInflater.inflate(R.menu.menu_list, menu);
            }
        });
    }


    private void findView() {
        listView = view.findViewById(R.id.listView);
    }

    //弹出修改提醒的对话框
    private void remind_dialog(int id) {
        final int finalId = id;
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editTextCount;
                RadioButton radioPkten, radioOn, radioTypeBigSmall;
                radioPkten = dialog.findViewById(R.id.radio_pkten);
                radioOn = dialog.findViewById(R.id.radio_on);
                radioTypeBigSmall = dialog.findViewById(R.id.radio_big_small);
                editTextCount = dialog.findViewById(R.id.edit_count);
                String name, type;
                boolean state;
                int count;
                switch (view.getId()) {
                    case R.id.cancel_btn:
                        dialog.dismiss();
                        break;
                    case R.id.ok_btn:
                        if (radioPkten.isChecked()) {
                            name = Rule.PKTEN;
                        } else {
                            name = Rule.SHISHICAI;
                        }
                        if (radioOn.isChecked()) {
                            state = true;
                        } else {
                            state = false;
                        }

                        if (radioTypeBigSmall.isChecked()) {
                            type = Rule.REMIND_TYPE_BIGSMALL;
                        } else {
                            type = Rule.REMIND_TYPE_SINGLEPAIR;
                        }
                        try {
                            count = Integer.parseInt(editTextCount.getText().toString());
                            diary.updateRemind(String.valueOf(finalId), name, state, count, type);
                            init();
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "输入的内容不合法", Toast.LENGTH_SHORT).show();
                        }
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
        Cursor cursor = diary.getRecordsOfRmindById(Rule.TABLE_NAME_REMIND, String.valueOf(id));
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex(Rule.REMIND_NAME));
        boolean state = cursor.getInt(cursor.getColumnIndex(Rule.REMIND_STATE)) > 0;
        int count = cursor.getInt(cursor.getColumnIndex(Rule.REMIND_COUNT));
        String type = cursor.getString(cursor.getColumnIndex(Rule.REMIND_TYPE));
        RadioButton radioShishicai, radioPkten, radioOn, radioOff, radioBigSmall, radioSinglePair;
        EditText editText;
        editText = dialog.findViewById(R.id.edit_count);
        radioShishicai = dialog.findViewById(R.id.radio_shishicai);
        radioPkten = dialog.findViewById(R.id.radio_pkten);
        radioOn = dialog.findViewById(R.id.radio_on);
        radioOff = dialog.findViewById(R.id.radio_off);
        radioBigSmall = dialog.findViewById(R.id.radio_big_small);
        radioSinglePair = dialog.findViewById(R.id.radio_single_pair);
        if (name.equals(Rule.SHISHICAI)) {
            radioShishicai.setChecked(true);
        } else {
            radioPkten.setChecked(true);
        }

        if (state) {
            radioOn.setChecked(true);
        } else {
            radioOff.setChecked(true);
        }
        if (type.equals(Rule.REMIND_TYPE_BIGSMALL)) {
            radioBigSmall.setChecked(true);
        } else {
            radioSinglePair.setChecked(true);
        }
        editText.setText(count + "");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (isThisFragment) {//判断当前fragment是否可见，防止错乱
            isThisFragment = false;
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int position = (int) info.id;
            int fetch = 0;
            if (listView.getLastVisiblePosition() >= listView.getChildCount())//get到的child只能是屏幕显示的，如第100个child，在屏幕里面当前是第2个，那么应当是第二个child而非100
            {
                fetch = listView.getChildCount() - 1 - (listView.getLastVisiblePosition() - position);
            } else {
                fetch = position;
            }
            TextView textView = listView.getChildAt(fetch).findViewById(R.id.id);
            int id = Integer.parseInt(textView.getText().toString());
            switch (item.getItemId()) {
                case R.id.delete:
                    Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                    diary.deleteRecordOfRemind(id);
                    init();
                    break;
            }
        }
        return super.onContextItemSelected(item);
    }

    public ArrayList<Map<String, Object>> getData() {
        ArrayList<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> map = new HashMap<>(); //这个new HashMap<>()不可以省略，否则会报空指针
        Cursor cursor = diary.getRecordsOfRmindById(Rule.TABLE_NAME_REMIND, "%%");
        if (cursor.moveToFirst()) {
            do {
                //遍历Cursor对象
                String name = cursor.getString(cursor.getColumnIndex(Rule.REMIND_NAME));
                boolean state = cursor.getInt(cursor.getColumnIndex(Rule.REMIND_STATE)) > 0;
                int count = cursor.getInt(cursor.getColumnIndex(Rule.REMIND_COUNT));
                String type = cursor.getString(cursor.getColumnIndex(Rule.REMIND_TYPE));
                map = new HashMap<>();
                map.put("_id", cursor.getInt(cursor.getColumnIndex(Rule.ID)));
                if (name.equals(Rule.PKTEN)) {
                    map.put("icon", R.mipmap.pkten);
                    map.put("title", "北京PK拾");
                    if (countPkten > count)
                        countPkten = count;
                } else {
                    map.put("icon", R.mipmap.shishicai);
                    map.put("title", "重庆时时彩");
                    if (countShishicai > count)
                        countShishicai = count;
                }

                if (state) {
                    map.put("state", "开启中");
                } else {
                    map.put("state", "已关闭");
                }
                map.put("count", "连走" + count + "期");
                map.put("type", type);
                data.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new NotesLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        simpleCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        simpleCursorAdapter.swapCursor(null);
    }

}
