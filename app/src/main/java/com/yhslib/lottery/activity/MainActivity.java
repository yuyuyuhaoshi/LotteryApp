package com.yhslib.lottery.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yhslib.lottery.R;
import com.yhslib.lottery.fragment.RecordFragment;
import com.yhslib.lottery.fragment.RemindFragment;
import com.yhslib.lottery.fragment.ResultFragment;
import com.yhslib.lottery.fragment.TrendFragment;
import com.yhslib.lottery.utils.CustomDialog;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private Fragment[] fragments;
    private BottomNavigationView navigation;
    private ActionBar actionBar;
    private Button trend_lottery_select_btn;
    private Button result_lottery_select_btn;
    private TextView add_remind_btn;
    private CustomDialog dialog;

    private static final int LOTTERY_BEIJING = 0;
    private static final int LOTTERY_CHONGQING = 1;
    private int trend_lottery_type = LOTTERY_BEIJING;
    private int result_lottery_type = LOTTERY_BEIJING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
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
                    if (actionBar != null) {
                        result_lottery_select_btn = actionBar.getCustomView().findViewById(R.id.result_lottery_select_btn);
                        if (result_lottery_type == LOTTERY_BEIJING) {
                            result_lottery_select_btn.setText(getString(R.string.lottery_beijing));
                        } else {
                            result_lottery_select_btn.setText(getString(R.string.lottery_chongqing));
                        }
                        result_lottery_select_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (result_lottery_type == LOTTERY_BEIJING) {
                                    Toast.makeText(MainActivity.this, "切换成重庆时时彩", Toast.LENGTH_SHORT).show();
                                    result_lottery_type = LOTTERY_CHONGQING;
                                    result_lottery_select_btn.setText(getString(R.string.lottery_chongqing));
                                } else {
                                    Toast.makeText(MainActivity.this, "切换成北京PK拾", Toast.LENGTH_SHORT).show();
                                    result_lottery_type = LOTTERY_BEIJING;
                                    result_lottery_select_btn.setText(getString(R.string.lottery_beijing));
                                }
                            }
                        });
                    }
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
                                    Toast.makeText(MainActivity.this, "切换成重庆时时彩", Toast.LENGTH_SHORT).show();
                                    trend_lottery_type = LOTTERY_CHONGQING;
                                    trend_lottery_select_btn.setText(getString(R.string.lottery_chongqing));
                                } else {
                                    Toast.makeText(MainActivity.this, "切换成北京PK拾", Toast.LENGTH_SHORT).show();
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

    private void add_remind_dialog() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.cancel_btn:
                        dialog.dismiss();
                        break;
                    case R.id.ok_btn:
                        Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
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
    }

}
