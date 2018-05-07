package com.yhslib.lottery.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.yhslib.lottery.R;
import com.yhslib.lottery.fragment.ResultFragment;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private Fragment[] fragments;
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        findView();
    }

    private void init() {
        navigation = findViewById(R.id.navigation);
        viewPager = findViewById(R.id.viewPager);
    }

    private void findView() {
        fragments = new Fragment[4];
        fragments[0] = ResultFragment.newInstance();
        fragments[1] = ResultFragment.newInstance();
        fragments[2] = ResultFragment.newInstance();
        fragments[3] = ResultFragment.newInstance();
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myFragmentPagerAdapter);
        viewPager.addOnPageChangeListener(onPageChangeListener);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
                    return true;
                case R.id.navigation_trend:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_remind:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_record:
                    viewPager.setCurrentItem(3);
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
                    navigation.setSelectedItemId(R.id.navigation_remind);
                    break;
                case 3:
                    navigation.setSelectedItemId(R.id.navigation_record);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}
