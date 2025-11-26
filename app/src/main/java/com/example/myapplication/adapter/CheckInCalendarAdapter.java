package com.example.myapplication.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.utils.DateUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CheckInCalendarAdapter extends RecyclerView.Adapter<CheckInCalendarAdapter.ViewHolder> {
    private List<CalendarDay> days;
    private List<String> checkInDates;
    private Calendar currentMonth;
    private OnDateClickListener listener;

    public interface OnDateClickListener {
        void onDateClick(String date);
    }

    public CheckInCalendarAdapter(List<String> checkInDates, Calendar currentMonth, OnDateClickListener listener) {
        this.checkInDates = checkInDates;
        this.currentMonth = currentMonth;
        this.listener = listener;
        this.days = generateMonthCalendar(currentMonth);
    }

    public CheckInCalendarAdapter(List<String> checkInDates, Calendar currentMonth) {
        this(checkInDates, currentMonth, null);
    }

    private List<CalendarDay> generateMonthCalendar(Calendar currentMonth) {
        List<CalendarDay> daysList = new ArrayList<>();
        
        // 克隆日历，避免修改原对象
        Calendar cal = (Calendar) currentMonth.clone();
        
        // 设置为当月第一天
        cal.set(Calendar.DAY_OF_MONTH, 1);
        
        // 获取当月第一天是星期几（0=周日, 1=周一, ...）
        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        
        // 添加空白天数
        for (int i = 0; i < firstDayOfWeek; i++) {
            daysList.add(new CalendarDay("", false, false, false));
        }
        
        // 获取当月天数
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = DateUtils.getTodayDate();
        
        // 添加当月所有日期
        for (int day = 1; day <= maxDay; day++) {
            cal.set(Calendar.DAY_OF_MONTH, day);
            String dateStr = sdf.format(cal.getTime());
            
            boolean isChecked = checkInDates.contains(dateStr);
            boolean isToday = dateStr.equals(today);
            boolean isCurrentMonth = true;
            
            daysList.add(new CalendarDay(String.valueOf(day), isChecked, isToday, isCurrentMonth));
        }
        
        return daysList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_calendar_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CalendarDay day = days.get(position);
        holder.tvDay.setText(day.dayNum);
        
        // 添加点击事件
        if (day.isCurrentMonth && !day.dayNum.isEmpty()) {
            holder.itemView.setOnClickListener(v -> {
                if (listener != null && day.isChecked) {
                    // 构造日期字符串
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Calendar cal = (Calendar) currentMonth.clone();
                    cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day.dayNum));
                    String dateStr = sdf.format(cal.getTime());
                    listener.onDateClick(dateStr);
                }
            });
        } else {
            holder.itemView.setOnClickListener(null);
        }

        if (!day.isCurrentMonth || day.dayNum.isEmpty()) {
            // 空白日期
            holder.tvDay.setTextColor(Color.TRANSPARENT);
            holder.tvDay.setBackground(null);
            holder.tvDay.setTypeface(null, Typeface.NORMAL);
        } else if (day.isChecked) {
            // 已打卡 - 绿色圆形背景 + 白色文字
            holder.tvDay.setBackgroundResource(R.drawable.bg_calendar_checked);
            holder.tvDay.setTextColor(Color.WHITE);
            holder.tvDay.setTypeface(null, Typeface.BOLD);
        } else if (day.isToday) {
            // 今天 - 蓝色圆形背景 + 白色文字
            holder.tvDay.setBackgroundResource(R.drawable.bg_calendar_today);
            holder.tvDay.setTextColor(Color.WHITE);
            holder.tvDay.setTypeface(null, Typeface.BOLD);
        } else {
            // 普通日期 - 无背景，灰色文字
            holder.tvDay.setBackground(null);
            holder.tvDay.setTextColor(Color.parseColor("#757575"));
            holder.tvDay.setTypeface(null, Typeface.NORMAL);
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout cardView;
        TextView tvDay;

        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            tvDay = itemView.findViewById(R.id.tvDay);
        }
    }

    static class CalendarDay {
        String dayNum;
        boolean isChecked;
        boolean isToday;
        boolean isCurrentMonth;

        CalendarDay(String dayNum, boolean isChecked, boolean isToday, boolean isCurrentMonth) {
            this.dayNum = dayNum;
            this.isChecked = isChecked;
            this.isToday = isToday;
            this.isCurrentMonth = isCurrentMonth;
        }
    }
}

