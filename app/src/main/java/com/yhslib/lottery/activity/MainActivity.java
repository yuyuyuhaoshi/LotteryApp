package com.yhslib.lottery.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yhslib.lottery.R;
import com.yhslib.lottery.fragment.RecordFragment;
import com.yhslib.lottery.fragment.RemindFragment;
import com.yhslib.lottery.fragment.ResultFragment;
import com.yhslib.lottery.fragment.TrendFragment;
import com.yhslib.lottery.sqlittle.DiaryDAO;
import com.yhslib.lottery.utils.CustomDialog;
import com.yhslib.lottery.utils.Rule;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {
    static final String REMIND_NAME = "name";
    static final String REMIND_STATE = "state";
    static final String REMIND_COUNT = "count";
    static final String PKTEN ="北京PK拾";
    static final String SHISHICAI="重庆时时彩";
    static final String REMIND_TYPE = "remind_type";
    static final String REMIND_TYPE_BIGSMALL = "大小";
    static final String REMIND_TYPE_SINGLEPAIR = "单双";
    private ViewPager viewPager;
    public Fragment[] fragments;
    private BottomNavigationView navigation;
    private ActionBar actionBar;
    private Button trend_lottery_select_btn;
    private Button result_lottery_select_btn;
    private TextView add_remind_btn;
    private TextView set_record_btn;
    private CustomDialog dialog;
    private DiaryDAO remindDAo;
    private static final int LOTTERY_BEIJING = 0;
    private static final int LOTTERY_CHONGQING = 1;
    private int trend_lottery_type = LOTTERY_BEIJING;
    private int result_lottery_type = LOTTERY_BEIJING;
    Window window;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
//        window = this.getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        setBarColor();
        init();
    }

    private void init() {
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.actionbar_result);
        }
        fragments = new Fragment[4];
        fragments[0] = ResultFragment.newInstance();
        fragments[1] = TrendFragment.newInstance();
        fragments[2] = RecordFragment.newInstance();
        fragments[3] = RemindFragment.newInstance();
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myFragmentPagerAdapter);
        viewPager.addOnPageChangeListener(onPageChangeListener);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        remindDAo=new DiaryDAO(this);
        disableShiftMode(navigation);
    }

    private void findView() {
        navigation = findViewById(R.id.navigation);
        viewPager = findViewById(R.id.viewPager);
        actionBar = getSupportActionBar();
    }


    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_result:
                    viewPager.setCurrentItem(0);
                    actionBar.setCustomView(R.layout.actionbar_result);
                    return true;
                case R.id.navigation_trend:
                    viewPager.setCurrentItem(1);
                    actionBar.setCustomView(R.layout.actionbar_trend);
                    if (actionBar != null) {
                        trend_lottery_select_btn = actionBar.getCustomView().findViewById(R.id.trend_lottery_select_btn);
                        if (trend_lottery_type == LOTTERY_BEIJING) {
                            trend_lottery_select_btn.setText(getString(R.string.lottery_beijing));
                        } else {
                            trend_lottery_select_btn.setText(getString(R.string.lottery_chongqing));
                        }
                        trend_lottery_select_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (trend_lottery_type == LOTTERY_BEIJING) {
                                    ((TrendFragment)fragments[1]).cutLotteryType();
                                    trend_lottery_type = LOTTERY_CHONGQING;
                                    trend_lottery_select_btn.setText(getString(R.string.lottery_chongqing));
                                } else {
                                    ((TrendFragment)fragments[1]).cutLotteryType();
                                    trend_lottery_type = LOTTERY_BEIJING;
                                    trend_lottery_select_btn.setText(getString(R.string.lottery_beijing));
                                }
                            }
                        });
                    }
                    return true;
                case R.id.navigation_record:
                    viewPager.setCurrentItem(2);
                    actionBar.setCustomView(R.layout.actionbar_record);
                    if (actionBar != null) {
                        set_record_btn = actionBar.getCustomView().findViewById(R.id.add_record_btn);
                        set_record_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                set_record_dialog();
                            }
                        });
                        actionBar.getCustomView().findViewById(R.id.screen).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                set_record_screen();
                            }
                        });
                    }
                    return true;
                case R.id.navigation_remind:
                    viewPager.setCurrentItem(3);
                    actionBar.setCustomView(R.layout.actionbar_remind);
                    if (actionBar != null) {
                        add_remind_btn = actionBar.getCustomView().findViewById(R.id.add_remind_btn);
                        add_remind_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                add_remind_dialog();
                            }
                        });
                    }
                    return true;
            }
            return false;
        }
    };



    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    navigation.setSelectedItemId(R.id.navigation_result);
                    break;
                case 1:
                    navigation.setSelectedItemId(R.id.navigation_trend);
                    break;
                case 2:
                    navigation.setSelectedItemId(R.id.navigation_record);
                    break;
                case 3:
                    navigation.setSelectedItemId(R.id.navigation_remind);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        exit_dialog();
    }

    private void exit_dialog() {
        new AlertDialog.Builder(this).setMessage("您确定要退出吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
            }
        }).setNegativeButton("取消", null).show();
    }


    //在提醒界面，选择加好可添加新提醒
    //数据存入数据库，表名为remind
    private void add_remind_dialog() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editTextCount;
                RadioButton radioPkten,radioOn,radioTypeBigSmall;
                radioPkten=dialog.findViewById(R.id.radio_pkten);
                radioOn=dialog.findViewById(R.id.radio_on);
                radioTypeBigSmall=dialog.findViewById(R.id.radio_big_small);
                editTextCount=dialog.findViewById(R.id.edit_count);
                String name,type;
                boolean state ;
                int count;
                switch (view.getId()) {
                    case R.id.cancel_btn:
                        dialog.dismiss();
                        break;
                    case R.id.ok_btn:
                        if (radioPkten.isChecked()){
                            name =PKTEN;
                        }else {
                            name=SHISHICAI;
                        }
                        if (radioOn.isChecked()){
                            state = true;
                        }else {
                            state = false;
                        }
                        if (radioTypeBigSmall.isChecked()){
                            type=REMIND_TYPE_BIGSMALL;
                        }else {
                            type=REMIND_TYPE_SINGLEPAIR;
                        }
                        try {
                            count = Integer.parseInt(editTextCount.getText().toString());
                            remindDAo.insertRecordOfRemind(name,state,count,type);
                            dialog.dismiss();
                            ((RemindFragment)fragments[3]).init();
                            Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            Toast.makeText(MainActivity.this, "输入的内容不合法", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        };

        CustomDialog.Builder builder = new CustomDialog.Builder(this);
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
        RadioButton radioShishicai,radioOn,radioTypeBigSmall;
        radioShishicai=dialog.findViewById(R.id.radio_shishicai);
        radioOn=dialog.findViewById(R.id.radio_on);
        radioTypeBigSmall=dialog.findViewById(R.id.radio_big_small);
        radioTypeBigSmall.setChecked(true);
        radioShishicai.setChecked(true);
        radioOn.setChecked(true);
    }

    private void set_record_dialog() {
        View.OnClickListener listener = new View.OnClickListener() {
            @SuppressLint("CutPasteId")
            @Override
            public void onClick(View view) {
                EditText shishicaiBigSmallCount=dialog.findViewById(R.id.edit_shishicai_daxiao_count);
                EditText shishicaiEvenOddCount=dialog.findViewById(R.id.edit_shishicai_danshuang_count);
                EditText pktenBigSmallCount=dialog.findViewById(R.id.edit_pkten_daxiao_count);
                EditText pktenEvenOddCount=dialog.findViewById(R.id.edit_pkten_danshuang_count);
                switch (view.getId()) {
                    case R.id.cancel_btn:
                        dialog.dismiss();
                        break;
                    case R.id.ok_btn:
                        remindDAo.updateRecordSet(shishicaiBigSmallCount.getText().toString(),shishicaiEvenOddCount.getText().toString(),
                                pktenBigSmallCount.getText().toString(),pktenEvenOddCount.getText().toString());
                        dialog.dismiss();
                        Cursor cursor =remindDAo.getRecordsOfLotteryById(Rule.TABLE_NAME_PKTEN,"%%");
                        Cursor cursor2 =remindDAo.getRecordsOfLotteryById(Rule.TABLE_NAME_SHISHICAI,"%%");
                        int count=0;
                        if (cursor.moveToFirst()){
                            do {
                                count++;
                                remindDAo.updateLotteryHistoryIsRead(Rule.PKTEN,cursor.getString(cursor.getColumnIndex(Rule.ID)),0);
                            }while (cursor.moveToNext()&&count<50);
                        }
                        count=0;
                        if (cursor2.moveToFirst()){
                            do {
                                count++;
                                remindDAo.updateLotteryHistoryIsRead(Rule.SHISHICAI,cursor2.getString(cursor2.getColumnIndex(Rule.ID)),0);
                            }while (cursor2.moveToNext()&&count<50);
                        }
                        ((RecordFragment)fragments[2]).init();
                        Rule.saveRecord(remindDAo);
                        break;
                }
            }
        };

        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        dialog = builder
                .style(R.style.Dialog)
                .heightDimenRes(R.dimen.dialog_record_height)
                .widthDimenRes(R.dimen.dialog_width)
                .cancelTouchout(false)
                .view(R.layout.dialog_record)
                .addViewOnclick(R.id.cancel_btn, listener)
                .addViewOnclick(R.id.ok_btn, listener)
                .build();
        dialog.show();
        EditText shishicaiBigSmallCount=dialog.findViewById(R.id.edit_shishicai_daxiao_count);
        EditText shishicaiEvenOddCount=dialog.findViewById(R.id.edit_shishicai_danshuang_count);
        EditText pktenBigSmallCount=dialog.findViewById(R.id.edit_pkten_daxiao_count);
        EditText pktenEvenOddCount=dialog.findViewById(R.id.edit_pkten_danshuang_count);
        Cursor cursor =remindDAo.getRecordSet();
        cursor.moveToFirst();
        shishicaiBigSmallCount.setText(cursor.getString(cursor.getColumnIndex(Rule.SET_RECORD_SHISHICAI_BIGSMALL)));
        shishicaiEvenOddCount.setText(cursor.getString(cursor.getColumnIndex(Rule.SET_RECORD_SHISHICAI_EVENODD)));
        pktenBigSmallCount.setText(cursor.getString(cursor.getColumnIndex(Rule.SET_RECORD_PKTEN_BIGSMALL)));
        pktenEvenOddCount.setText(cursor.getString(cursor.getColumnIndex(Rule.SET_RECORD_PKTEN_EVENODD)));
    }

    private void set_record_screen() {
        View.OnClickListener listener = new View.OnClickListener() {
            @SuppressLint("CutPasteId")
            @Override
            public void onClick(View view) {
                EditText editText ;
                editText = dialog.findViewById(R.id.edit_screen_count);
                switch (view.getId()) {
                    case R.id.cancel_btn:
                        dialog.dismiss();
                        break;
                    case R.id.ok_btn:
                        dialog.dismiss();
                        try {
                            ((RecordFragment)fragments[2]).min= Integer.parseInt(editText.getText().toString());
                        }catch (Exception e){
                            Toast.makeText(MainActivity.this, "输入的内容不合法", Toast.LENGTH_SHORT).show();
                        }
                        ((RecordFragment)fragments[2]).init();
                        Rule.saveRecord(remindDAo);
                        break;
                }
            }
        };

        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        dialog = builder
                .style(R.style.Dialog)
                .heightDimenRes(R.dimen.dialog_screen_height)
                .widthDimenRes(R.dimen.dialog_width)
                .cancelTouchout(false)
                .view(R.layout.dialog_record_screen)
                .addViewOnclick(R.id.cancel_btn, listener)
                .addViewOnclick(R.id.ok_btn, listener)
                .build();
        dialog.show();
    }

    public void setBarColor() {
        ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
        View statusBarView = new View(window.getContext());
        int statusBarHeight = getStatusBarHeight(window.getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
        params.gravity = Gravity.TOP;
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        decorViewGroup.addView(statusBarView);
    }

    private static int getStatusBarHeight(Context context) {//获取状态栏高度
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }


}
