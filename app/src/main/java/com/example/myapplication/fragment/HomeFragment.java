package com.example.myapplication.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.AddHabitActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.HabitAdapter;
import com.example.myapplication.model.CheckInRecord;
import com.example.myapplication.model.Habit;
import com.example.myapplication.utils.DateUtils;
import com.example.myapplication.utils.SPUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private HabitAdapter adapter;
    private List<Habit> habits;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
        loadData();
        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);

        // 设置GridLayoutManager，一行两列
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void loadData() {
        habits = SPUtils.getHabits(getContext());
        
        adapter = new HabitAdapter(habits, new HabitAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Habit habit, int position) {
                showCheckInDialog(habit, position);
            }

            @Override
            public void onItemLongClick(Habit habit, int position) {
                showEditDialog(habit);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void showCheckInDialog(Habit habit, int position) {
        if (habit.isCompleted()) {
            Toast.makeText(getContext(), "🎉 今日目标已完成！", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_checkin_with_note, null);
        
        TextView tvTitle = dialogView.findViewById(R.id.dialog_title);
        TextView tvMessage = dialogView.findViewById(R.id.dialog_message);
        NumberPicker hourPicker = dialogView.findViewById(R.id.hourPicker);
        NumberPicker minutePicker = dialogView.findViewById(R.id.minutePicker);
        EditText etNote = dialogView.findViewById(R.id.etNote);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm);
        
        tvTitle.setText(habit.getTitle());
        tvMessage.setText("确认完成一次打卡吗？");
        
        // 设置小时选择器（0-23）
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);
        hourPicker.setFormatter(value -> String.format(Locale.getDefault(), "%02d", value));
        
        // 设置分钟选择器（0-59）
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        minutePicker.setFormatter(value -> String.format(Locale.getDefault(), "%02d", value));
        
        // 默认设置为当前时间
        Calendar now = Calendar.getInstance();
        hourPicker.setValue(now.get(Calendar.HOUR_OF_DAY));
        minutePicker.setValue(now.get(Calendar.MINUTE));
        
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        
        btnConfirm.setOnClickListener(v -> {
            // 获取选择的时间
            int selectedHour = hourPicker.getValue();
            int selectedMinute = minutePicker.getValue();
            
            // 根据用户选择的时间构建timestamp
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
            calendar.set(Calendar.MINUTE, selectedMinute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            long customTimestamp = calendar.getTimeInMillis();
            
            // 获取备注内容
            String note = etNote.getText().toString().trim();
            
            // 创建打卡记录，使用用户选择的时间作为timestamp
            CheckInRecord record = new CheckInRecord(customTimestamp, note);
            
            // 添加打卡记录
            String today = DateUtils.getTodayDate();
            List<CheckInRecord> recordsToday = habit.getCheckInRecords().get(today);
            if (recordsToday == null) {
                recordsToday = new ArrayList<>();
                habit.getCheckInRecords().put(today, recordsToday);
            }
            recordsToday.add(record);
            
            habit.incrementCount();
            
            // 保存更新
            SPUtils.updateHabit(getContext(), habit);
            adapter.notifyItemChanged(position);

            if (habit.isCompleted()) {
                Toast.makeText(getContext(), "🎉 恭喜完成今日目标！继续保持！", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "✅ 打卡成功！", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });
        
        dialog.show();
    }

    private void showEditDialog(Habit habit) {
        Intent intent = new Intent(getActivity(), AddHabitActivity.class);
        intent.putExtra("habit_id", habit.getId());
        intent.putExtra("habit_title", habit.getTitle());
        intent.putExtra("habit_target", habit.getTargetCount());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        // 刷新数据
        if (adapter != null) {
            habits = SPUtils.getHabits(getContext());
            adapter.updateData(habits);
        }
    }
}

