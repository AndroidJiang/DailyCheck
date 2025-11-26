package com.example.myapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.adapter.CheckInCalendarAdapter;
import com.example.myapplication.model.Habit;
import com.example.myapplication.utils.SPUtils;
import com.google.android.material.button.MaterialButton;
import java.util.Calendar;
import java.util.List;

public class HabitDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Habit habit;
    private TextView tvTitle;
    private TextView tvTarget;
    private TextView tvTotalDays;
    private TextView tvMonth;
    private ImageView ivIcon;
    private ImageButton btnPrevMonth, btnNextMonth;
    private RecyclerView recyclerView;
    private MaterialButton btnDelete;
    private CheckInCalendarAdapter adapter;
    private Calendar currentCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_detail);

        long habitId = getIntent().getLongExtra("habit_id", -1);
        if (habitId == -1) {
            finish();
            return;
        }

        habit = SPUtils.getHabitById(this, habitId);
        if (habit == null) {
            finish();
            return;
        }

        initViews();
        loadData();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        tvTitle = findViewById(R.id.tvTitle);
        tvTarget = findViewById(R.id.tvTarget);
        tvTotalDays = findViewById(R.id.tvTotalDays);
        tvMonth = findViewById(R.id.tvMonth);
        ivIcon = findViewById(R.id.ivIcon);
        btnPrevMonth = findViewById(R.id.btnPrevMonth);
        btnNextMonth = findViewById(R.id.btnNextMonth);
        recyclerView = findViewById(R.id.recyclerViewCalendar);
        btnDelete = findViewById(R.id.btnDelete);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 7));
        
        currentCalendar = Calendar.getInstance();
        
        btnPrevMonth.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, -1);
            updateCalendar();
        });
        
        btnNextMonth.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, 1);
            updateCalendar();
        });
        
        btnDelete.setOnClickListener(v -> showDeleteDialog());
    }

    private void loadData() {
        toolbar.setTitle(habit.getTitle());
        tvTitle.setText(habit.getTitle());
        tvTarget.setText("每日目标：" + habit.getTargetCount() + " 次");
        tvTotalDays.setText("已打卡天数：" + habit.getCheckInDates().size() + " 天");
        ivIcon.setImageResource(habit.getIconResId());

        updateCalendar();
    }
    
    private void updateCalendar() {
        // 更新月份显示
        int year = currentCalendar.get(Calendar.YEAR);
        int month = currentCalendar.get(Calendar.MONTH) + 1;
        tvMonth.setText(year + "年" + month + "月");
        
        // 显示打卡日历
        List<String> checkInDates = habit.getCheckInDates();
        adapter = new CheckInCalendarAdapter(checkInDates, currentCalendar);
        recyclerView.setAdapter(adapter);
    }

    private void showDeleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle("删除确认")
                .setMessage("确定要删除「" + habit.getTitle() + "」吗？所有打卡记录将被清除。")
                .setPositiveButton("确定", (dialog, which) -> {
                    SPUtils.deleteHabit(this, habit.getId());
                    Toast.makeText(this, "已删除", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

