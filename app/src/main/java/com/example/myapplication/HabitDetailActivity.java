package com.example.myapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.adapter.CheckInCalendarAdapter;
import com.example.myapplication.adapter.CheckInRecordAdapter;
import com.example.myapplication.model.CheckInRecord;
import com.example.myapplication.model.Habit;
import com.example.myapplication.utils.SPUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
        adapter = new CheckInCalendarAdapter(checkInDates, currentCalendar, new CheckInCalendarAdapter.OnDateClickListener() {
            @Override
            public void onDateClick(String date) {
                showCheckInRecordsDialog(date);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    /**
     * 显示指定日期的打卡记录弹窗
     */
    private void showCheckInRecordsDialog(String date) {
        List<CheckInRecord> records = habit.getCheckInRecordsByDate(date);
        
        if (records == null || records.isEmpty()) {
            Toast.makeText(this, "该日期暂无打卡记录", Toast.LENGTH_SHORT).show();
            return;
        }

        // 创建底部弹窗
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_check_in_records, null);
        
        TextView tvDate = dialogView.findViewById(R.id.tvDate);
        TextView tvCount = dialogView.findViewById(R.id.tvCount);
        RecyclerView rvRecords = dialogView.findViewById(R.id.rvCheckInRecords);
        Button btnClose = dialogView.findViewById(R.id.btnClose);
        
        // 格式化日期显示
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        try {
            tvDate.setText(outputFormat.format(inputFormat.parse(date)));
        } catch (Exception e) {
            tvDate.setText(date);
        }
        
        tvCount.setText("共打卡 " + records.size() + " 次 ✓");
        
        // 设置记录列表
        rvRecords.setLayoutManager(new LinearLayoutManager(this));
        CheckInRecordAdapter recordAdapter = new CheckInRecordAdapter(records);
        rvRecords.setAdapter(recordAdapter);
        
        btnClose.setOnClickListener(v -> dialog.dismiss());
        
        dialog.setContentView(dialogView);
        dialog.show();
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

