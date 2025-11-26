package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.model.Habit;
import java.util.List;

public class HabitListAdapter extends RecyclerView.Adapter<HabitListAdapter.ViewHolder> {
    private List<Habit> habits;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Habit habit);
    }

    public HabitListAdapter(List<Habit> habits, OnItemClickListener listener) {
        this.habits = habits;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_habit_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Habit habit = habits.get(position);
        holder.tvTitle.setText(habit.getTitle());
        holder.tvInfo.setText("目标: " + habit.getTargetCount() + "次/天 | 已打卡 " + habit.getCheckInDates().size() + " 天");
        
        // 如果今日已完成，显示完成印章；否则显示普通图标
        if (habit.isCompleted()) {
            holder.ivIcon.setImageResource(R.drawable.ic_completed_stamp);
        } else {
            holder.ivIcon.setImageResource(R.drawable.ic_daka);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(habit);
            }
        });
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    public void updateData(List<Habit> newHabits) {
        this.habits = newHabits;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvInfo;

        ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvInfo = itemView.findViewById(R.id.tvInfo);
        }
    }
}

