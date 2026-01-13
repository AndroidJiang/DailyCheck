package com.example.myapplication.utils;

import com.example.myapplication.model.Habit;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mmkv.MMKV;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * MMKV 工具类 - 替代 SharedPreferences
 * 优势：
 * 1. 性能更好（快10倍+）
 * 2. 写入即持久化，不会丢失数据
 * 3. 支持数据恢复
 * 4. 文件损坏自动修复
 */
public class MMKVUtils {
    private static final String KEY_HABITS = "habits";
    private static final String KEY_LAST_RESET_DATE = "last_reset_date";
    
    private static MMKV getMMKV() {
        return MMKV.defaultMMKV();
    }

    // 保存习惯列表
    public static boolean saveHabits(List<Habit> habits) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(habits);
            return getMMKV().encode(KEY_HABITS, json);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 获取习惯列表
    public static List<Habit> getHabits() {
        try {
            String json = getMMKV().decodeString(KEY_HABITS, "");
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
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // 保存最后重置日期
    public static boolean saveLastResetDate(String date) {
        try {
            return getMMKV().encode(KEY_LAST_RESET_DATE, date);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 获取最后重置日期
    public static String getLastResetDate() {
        try {
            return getMMKV().decodeString(KEY_LAST_RESET_DATE, "");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    // 添加习惯
    public static boolean addHabit(Habit habit) {
        try {
            List<Habit> habits = getHabits();
            habits.add(habit);
            return saveHabits(habits);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 更新习惯
    public static boolean updateHabit(Habit updatedHabit) {
        try {
            List<Habit> habits = getHabits();
            boolean found = false;
            for (int i = 0; i < habits.size(); i++) {
                if (habits.get(i).getId() == updatedHabit.getId()) {
                    habits.set(i, updatedHabit);
                    found = true;
                    break;
                }
            }
            if (found) {
                return saveHabits(habits);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除习惯
    public static boolean deleteHabit(long habitId) {
        try {
            List<Habit> habits = getHabits();
            for (int i = 0; i < habits.size(); i++) {
                if (habits.get(i).getId() == habitId) {
                    habits.remove(i);
                    break;
                }
            }
            return saveHabits(habits);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 根据ID获取习惯
    public static Habit getHabitById(long habitId) {
        try {
            List<Habit> habits = getHabits();
            for (Habit habit : habits) {
                if (habit.getId() == habitId) {
                    return habit;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 从 SharedPreferences 迁移数据到 MMKV
     * 只在首次启动时调用一次
     */
    public static void migrateFromSP(android.content.Context context) {
        try {
            // 检查是否已经迁移过
            if (getMMKV().decodeBool("migrated_from_sp", false)) {
                return;
            }
            
            // 从 SP 读取旧数据
            List<Habit> oldHabits = SPUtils.getHabits(context);
            String oldResetDate = SPUtils.getLastResetDate(context);
            
            // 如果有旧数据，迁移到 MMKV
            if (oldHabits != null && !oldHabits.isEmpty()) {
                saveHabits(oldHabits);
            }
            
            if (oldResetDate != null && !oldResetDate.isEmpty()) {
                saveLastResetDate(oldResetDate);
            }
            
            // 标记已迁移
            getMMKV().encode("migrated_from_sp", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

