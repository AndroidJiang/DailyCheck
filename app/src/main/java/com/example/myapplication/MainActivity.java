package com.example.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.example.myapplication.fragment.AboutFragment;
import com.example.myapplication.fragment.HomeFragment;
import com.example.myapplication.fragment.HabitManageFragment;
import com.example.myapplication.utils.DateUtils;
import com.example.myapplication.utils.SPUtils;
import com.example.myapplication.model.Habit;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView tvToolbarTitle;
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigation;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 检查并重置数据
        checkAndResetData();

        initViews();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        // 隐藏默认标题，使用自定义居中标题
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        
        // 创建居中的标题TextView
        tvToolbarTitle = new TextView(this);
        tvToolbarTitle.setTextSize(20);
        tvToolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
        tvToolbarTitle.setTypeface(null, Typeface.BOLD);
        tvToolbarTitle.setText("打卡");
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        toolbar.addView(tvToolbarTitle, layoutParams);

        viewPager = findViewById(R.id.viewPager);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        setupViewPager();
        setupBottomNavigation();
    }

    private void setupViewPager() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "打卡");
        adapter.addFragment(new HabitManageFragment(), "统计");
        adapter.addFragment(new AboutFragment(), "关于");
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                bottomNavigation.getMenu().getItem(position).setChecked(true);
                updateToolbarTitle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                viewPager.setCurrentItem(0);
                return true;
            } else if (itemId == R.id.navigation_stats) {
                viewPager.setCurrentItem(1);
                return true;
            } else if (itemId == R.id.navigation_about) {
                viewPager.setCurrentItem(2);
                return true;
            }
            return false;
        });
    }

    private void updateToolbarTitle(int position) {
        if (tvToolbarTitle != null) {
            tvToolbarTitle.setText(adapter.getPageTitle(position));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            Intent intent = new Intent(this, AddHabitActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkAndResetData() {
        String lastResetDate = SPUtils.getLastResetDate(this);
        String todayDate = DateUtils.getTodayDate();

        if (!DateUtils.isToday(lastResetDate)) {
            // 需要重置数据
            List<Habit> habits = SPUtils.getHabits(this);
            for (Habit habit : habits) {
                habit.resetCount();
            }
            SPUtils.saveHabits(this, habits);
            SPUtils.saveLastResetDate(this, todayDate);
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
}

