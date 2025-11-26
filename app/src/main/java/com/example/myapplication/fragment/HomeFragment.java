package com.example.myapplication.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.myapplication.model.Habit;
import com.example.myapplication.utils.DateUtils;
import com.example.myapplication.utils.SPUtils;
import java.util.ArrayList;
import java.util.List;

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
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_checkin, null);
        
        TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
        TextView tvMessage = dialogView.findViewById(R.id.tvMessage);
        android.widget.Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        android.widget.Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);
        
        tvMessage.setText("确认完成一次「" + habit.getTitle() + "」吗？");
        
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        
        btnConfirm.setOnClickListener(v -> {
            habit.incrementCount();
            // 记录打卡日期
            String today = DateUtils.getTodayDate();
            habit.addCheckInDate(today);
            
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

