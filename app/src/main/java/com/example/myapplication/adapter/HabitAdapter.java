package com.example.myapplication.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.model.Habit;
import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.ViewHolder> {
    private List<Habit> habits;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Habit habit, int position);
        void onItemLongClick(Habit habit, int position);
    }

    public HabitAdapter(List<Habit> habits, OnItemClickListener listener) {
        this.habits = habits;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_habit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Habit habit = habits.get(position);
        holder.tvTitle.setText(habit.getTitle());
        
        // 设置进度文字：已完成数字（橙色大号20sp）+ "/" + 需要完成数字（灰色小号16sp）
        String progressStr = habit.getProgress(); // "0/1" 格式
        String[] parts = progressStr.split("/");
        
        if (parts.length == 2) {
            String currentCount = parts[0];
            String separator = "/";
            String targetCount = parts[1];
            String progressText = currentCount + separator + targetCount;
            
            SpannableString spannableString = new SpannableString(progressText);
            
            // 已完成数字：橙色 + 大号(20sp) + 粗体
            int orangeColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.theme_primary);
            spannableString.setSpan(new ForegroundColorSpan(orangeColor), 0, currentCount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new AbsoluteSizeSpan(20, true), 0, currentCount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, currentCount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            
            // 斜杠：灰色 + 小号(16sp)
            int grayColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.text_secondary);
            int slashStart = currentCount.length();
            int slashEnd = slashStart + separator.length();
            spannableString.setSpan(new ForegroundColorSpan(grayColor), slashStart, slashEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new AbsoluteSizeSpan(16, true), slashStart, slashEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            
            // 需要完成数字：灰色 + 小号(16sp)
            int targetStart = slashEnd;
            spannableString.setSpan(new ForegroundColorSpan(grayColor), targetStart, progressText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new AbsoluteSizeSpan(16, true), targetStart, progressText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            
            holder.tvProgress.setText(spannableString);
        } else {
            holder.tvProgress.setText(progressStr);
        }
        
        // 设置图标
        holder.ivIcon.setImageResource(habit.getIconResId());

        // 如果已完成，改变背景色
        if (habit.isCompleted()) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#E8F5E9"));
        } else {
            holder.cardView.setCardBackgroundColor(Color.WHITE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(habit, position);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onItemLongClick(habit, position);
            }
            return true;
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
        CardView cardView;
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvProgress;

        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvProgress = itemView.findViewById(R.id.tvProgress);
        }
    }
}

