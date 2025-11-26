package com.example.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.myapplication.model.Habit;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SPUtils {
    private static final String SP_NAME = "habit_data";
    private static final String KEY_HABITS = "habits";
    private static final String KEY_LAST_RESET_DATE = "last_reset_date";
    
    private static SharedPreferences getSP(Context context) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    // 保存习惯列表
    public static void saveHabits(Context context, List<Habit> habits) {
        Gson gson = new Gson();
        String json = gson.toJson(habits);
        getSP(context).edit().putString(KEY_HABITS, json).apply();
    }

    // 获取习惯列表
    public static List<Habit> getHabits(Context context) {
        String json = getSP(context).getString(KEY_HABITS, "");
        if (json.isEmpty()) {
            return new ArrayList<>();
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Habit>>(){}.getType();
        List<Habit> habits = gson.fromJson(json, type);
        // 确保每个习惯都有checkInDates列表
        for (Habit habit : habits) {
            if (habit.getCheckInDates() == null) {
                habit.setCheckInDates(new ArrayList<String>());
            }
        }
        return habits;
    }

    // 保存最后重置日期
    public static void saveLastResetDate(Context context, String date) {
        getSP(context).edit().putString(KEY_LAST_RESET_DATE, date).apply();
    }

    // 获取最后重置日期
    public static String getLastResetDate(Context context) {
        return getSP(context).getString(KEY_LAST_RESET_DATE, "");
    }

    // 添加习惯
    public static void addHabit(Context context, Habit habit) {
        List<Habit> habits = getHabits(context);
        habits.add(habit);
        saveHabits(context, habits);
    }

    // 更新习惯
    public static void updateHabit(Context context, Habit updatedHabit) {
        List<Habit> habits = getHabits(context);
        for (int i = 0; i < habits.size(); i++) {
            if (habits.get(i).getId() == updatedHabit.getId()) {
                habits.set(i, updatedHabit);
                break;
            }
        }
        saveHabits(context, habits);
    }

    // 删除习惯
    public static void deleteHabit(Context context, long habitId) {
        List<Habit> habits = getHabits(context);
        for (int i = 0; i < habits.size(); i++) {
            if (habits.get(i).getId() == habitId) {
                habits.remove(i);
                break;
            }
        }
        saveHabits(context, habits);
    }

    // 根据ID获取习惯
    public static Habit getHabitById(Context context, long habitId) {
        List<Habit> habits = getHabits(context);
        for (Habit habit : habits) {
            if (habit.getId() == habitId) {
                return habit;
            }
        }
        return null;
    }
}

