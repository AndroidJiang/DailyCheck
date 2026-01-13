package com.example.myapplication.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.contrarywind.view.WheelView;
import com.contrarywind.adapter.WheelAdapter;
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
import com.example.myapplication.utils.MMKVUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        habits = MMKVUtils.getHabits();
        
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

        // 创建自定义对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_checkin_simple, null);
        
        TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
        WheelView wheelStartHour = dialogView.findViewById(R.id.wheelStartHour);
        WheelView wheelStartMinute = dialogView.findViewById(R.id.wheelStartMinute);
        WheelView wheelEndHour = dialogView.findViewById(R.id.wheelEndHour);
        WheelView wheelEndMinute = dialogView.findViewById(R.id.wheelEndMinute);
        EditText etNote = dialogView.findViewById(R.id.etNote);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm);
        
        tvTitle.setText(habit.getTitle());
        
        // 获取当前时间
        Calendar now = Calendar.getInstance();
        int currentHour = now.get(Calendar.HOUR_OF_DAY);
        int currentMinute = now.get(Calendar.MINUTE);
        
        // 准备小时数据 (0-23)
        List<String> hours = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hours.add(String.format(Locale.getDefault(), "%02d", i));
        }
        
        // 准备分钟数据 (0-59)
        List<String> minutes = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            minutes.add(String.format(Locale.getDefault(), "%02d", i));
        }
        
        // 配置开始时间小时选择器
        wheelStartHour.setCyclic(true);
        wheelStartHour.setAdapter(new WheelAdapter<String>() {
            @Override
            public int getItemsCount() {
                return hours.size();
            }
            
            @Override
            public String getItem(int index) {
                return hours.get(index);
            }
            
            @Override
            public int indexOf(String o) {
                return hours.indexOf(o);
            }
        });
        wheelStartHour.setCurrentItem(currentHour);
        
        // 配置开始时间分钟选择器
        wheelStartMinute.setCyclic(true);
        wheelStartMinute.setAdapter(new WheelAdapter<String>() {
            @Override
            public int getItemsCount() {
                return minutes.size();
            }
            
            @Override
            public String getItem(int index) {
                return minutes.get(index);
            }
            
            @Override
            public int indexOf(String o) {
                return minutes.indexOf(o);
            }
        });
        wheelStartMinute.setCurrentItem(currentMinute);
        
        // 配置结束时间小时选择器
        wheelEndHour.setCyclic(true);
        wheelEndHour.setAdapter(new WheelAdapter<String>() {
            @Override
            public int getItemsCount() {
                return hours.size();
            }
            
            @Override
            public String getItem(int index) {
                return hours.get(index);
            }
            
            @Override
            public int indexOf(String o) {
                return hours.indexOf(o);
            }
        });
        wheelEndHour.setCurrentItem(currentHour);
        
        // 配置结束时间分钟选择器
        wheelEndMinute.setCyclic(true);
        wheelEndMinute.setAdapter(new WheelAdapter<String>() {
            @Override
            public int getItemsCount() {
                return minutes.size();
            }
            
            @Override
            public String getItem(int index) {
                return minutes.get(index);
            }
            
            @Override
            public int indexOf(String o) {
                return minutes.indexOf(o);
            }
        });
        wheelEndMinute.setCurrentItem(currentMinute);
        
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        
        btnConfirm.setOnClickListener(v -> {
            // 获取选择的时间
            int startHour = wheelStartHour.getCurrentItem();
            int startMinute = wheelStartMinute.getCurrentItem();
            int endHour = wheelEndHour.getCurrentItem();
            int endMinute = wheelEndMinute.getCurrentItem();
            
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
            
            // 添加打卡记录到当前习惯对象
            String today = DateUtils.getTodayDate();
            habit.addCheckInRecord(record);
            
            // 增加计数
            habit.incrementCount();
            
            // 同步保存到 MMKV（写入即持久化，性能更好）
            boolean saveSuccess = MMKVUtils.updateHabit(habit);
            
            if (!saveSuccess) {
                Toast.makeText(getContext(), "⚠️ 保存失败，请重试", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // 更新内存中的习惯列表
            habits.set(position, habit);
            
            // 刷新界面
            adapter.notifyItemChanged(position);
            
            // 显示提示
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
            habits = MMKVUtils.getHabits();
            adapter.updateData(habits);
        }
    }
}

