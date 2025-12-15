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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
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

        // 设置瀑布流布局，一行两列
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
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
        NumberPicker startHourPicker = dialogView.findViewById(R.id.startHourPicker);
        NumberPicker startMinutePicker = dialogView.findViewById(R.id.startMinutePicker);
        NumberPicker endHourPicker = dialogView.findViewById(R.id.endHourPicker);
        NumberPicker endMinutePicker = dialogView.findViewById(R.id.endMinutePicker);
        EditText etNote = dialogView.findViewById(R.id.etNote);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm);
        
        tvTitle.setText(habit.getTitle());
        tvMessage.setText("确认完成一次打卡吗？");
        
        // 获取当前时间
        Calendar now = Calendar.getInstance();
        int currentHour = now.get(Calendar.HOUR_OF_DAY);
        int currentMinute = now.get(Calendar.MINUTE);
        
        // 设置开始时间选择器（0-23小时，0-59分钟）
        startHourPicker.setMinValue(0);
        startHourPicker.setMaxValue(23);
        startHourPicker.setFormatter(value -> String.format(Locale.getDefault(), "%02d", value));
        startHourPicker.setValue(currentHour);
        
        startMinutePicker.setMinValue(0);
        startMinutePicker.setMaxValue(59);
        startMinutePicker.setFormatter(value -> String.format(Locale.getDefault(), "%02d", value));
        startMinutePicker.setValue(currentMinute);
        
        // 设置结束时间选择器
        endHourPicker.setMinValue(0);
        endHourPicker.setMaxValue(23);
        endHourPicker.setFormatter(value -> String.format(Locale.getDefault(), "%02d", value));
        endHourPicker.setValue(currentHour);
        
        endMinutePicker.setMinValue(0);
        endMinutePicker.setMaxValue(59);
        endMinutePicker.setFormatter(value -> String.format(Locale.getDefault(), "%02d", value));
        endMinutePicker.setValue(currentMinute);
        
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        
        btnConfirm.setOnClickListener(v -> {
            // 获取开始时间
            int startHour = startHourPicker.getValue();
            int startMinute = startMinutePicker.getValue();
            
            // 获取结束时间
            int endHour = endHourPicker.getValue();
            int endMinute = endMinutePicker.getValue();
            
            // 构建开始时间戳
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.set(Calendar.HOUR_OF_DAY, startHour);
            startCalendar.set(Calendar.MINUTE, startMinute);
            startCalendar.set(Calendar.SECOND, 0);
            startCalendar.set(Calendar.MILLISECOND, 0);
            long startTimestamp = startCalendar.getTimeInMillis();
            
            // 构建结束时间戳
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.set(Calendar.HOUR_OF_DAY, endHour);
            endCalendar.set(Calendar.MINUTE, endMinute);
            endCalendar.set(Calendar.SECOND, 0);
            endCalendar.set(Calendar.MILLISECOND, 0);
            long endTimestamp = endCalendar.getTimeInMillis();
            
            // 验证时间
            if (endTimestamp < startTimestamp) {
                Toast.makeText(getContext(), "⚠️ 结束时间不能早于开始时间", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // 获取备注内容
            String note = etNote.getText().toString().trim();
            
            // 创建打卡记录，使用开始和结束时间
            CheckInRecord record = new CheckInRecord(startTimestamp, endTimestamp, note);
            
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

