package com.example.myapplication.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Habit {
    private long id;
    private String title;
    private int targetCount;  // 需要完成的次数
    private int currentCount; // 当前完成次数
    private int iconResId;    // 图标资源ID
    private List<String> checkInDates; // 打卡日期记录列表（旧版兼容）
    private Map<String, List<CheckInRecord>> checkInRecords; // 详细打卡记录 Key: 日期(yyyy-MM-dd), Value: 当天的打卡记录列表

    public Habit() {
        this.id = System.currentTimeMillis();
        this.checkInDates = new ArrayList<>();
        this.checkInRecords = new HashMap<>();
    }

    public Habit(String title, int targetCount, int iconResId) {
        this.id = System.currentTimeMillis();
        this.title = title;
        this.targetCount = targetCount;
        this.currentCount = 0;
        this.iconResId = iconResId;
        this.checkInDates = new ArrayList<>();
        this.checkInRecords = new HashMap<>();
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(int targetCount) {
        this.targetCount = targetCount;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public List<String> getCheckInDates() {
        if (checkInDates == null) {
            checkInDates = new ArrayList<>();
        }
        return checkInDates;
    }

    public void setCheckInDates(List<String> checkInDates) {
        this.checkInDates = checkInDates;
    }

    public Map<String, List<CheckInRecord>> getCheckInRecords() {
        if (checkInRecords == null) {
            checkInRecords = new HashMap<>();
        }
        return checkInRecords;
    }

    public void setCheckInRecords(Map<String, List<CheckInRecord>> checkInRecords) {
        this.checkInRecords = checkInRecords;
    }

    public boolean isCompleted() {
        return currentCount >= targetCount;
    }

    public void incrementCount() {
        if (currentCount < targetCount) {
            currentCount++;
        }
    }

    public void resetCount() {
        currentCount = 0;
    }

    public String getProgress() {
        return currentCount + "/" + targetCount;
    }

    // 添加打卡日期（旧版兼容）
    public void addCheckInDate(String date) {
        if (!checkInDates.contains(date)) {
            checkInDates.add(date);
        }
    }

    // 检查某天是否打卡
    public boolean hasCheckedIn(String date) {
        return checkInDates.contains(date) || 
               (checkInRecords != null && checkInRecords.containsKey(date) && 
                !checkInRecords.get(date).isEmpty());
    }

    /**
     * 添加一次打卡记录
     */
    public void addCheckInRecord(CheckInRecord record) {
        if (checkInRecords == null) {
            checkInRecords = new HashMap<>();
        }
        String date = record.getDate();
        List<CheckInRecord> records = checkInRecords.get(date);
        if (records == null) {
            records = new ArrayList<>();
            checkInRecords.put(date, records);
        }
        records.add(record);
        
        // 同时更新旧版日期列表（兼容）
        addCheckInDate(date);
    }

    /**
     * 获取今天的打卡记录列表
     */
    public List<CheckInRecord> getTodayCheckInRecords() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = sdf.format(new Date());
        return getCheckInRecordsByDate(today);
    }

    /**
     * 获取指定日期的打卡记录列表
     */
    public List<CheckInRecord> getCheckInRecordsByDate(String date) {
        if (checkInRecords == null) {
            return new ArrayList<>();
        }
        List<CheckInRecord> records = checkInRecords.get(date);
        return records != null ? records : new ArrayList<>();
    }

    /**
     * 获取今天最后一次打卡记录
     */
    public CheckInRecord getTodayLastCheckIn() {
        List<CheckInRecord> records = getTodayCheckInRecords();
        if (records.isEmpty()) {
            return null;
        }
        return records.get(records.size() - 1);
    }
}


