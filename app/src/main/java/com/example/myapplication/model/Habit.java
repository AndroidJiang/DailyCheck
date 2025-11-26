package com.example.myapplication.model;

import java.util.ArrayList;
import java.util.List;

public class Habit {
    private long id;
    private String title;
    private int targetCount;  // 需要完成的次数
    private int currentCount; // 当前完成次数
    private int iconResId;    // 图标资源ID
    private List<String> checkInDates; // 打卡日期记录列表

    public Habit() {
        this.id = System.currentTimeMillis();
        this.checkInDates = new ArrayList<>();
    }

    public Habit(String title, int targetCount, int iconResId) {
        this.id = System.currentTimeMillis();
        this.title = title;
        this.targetCount = targetCount;
        this.currentCount = 0;
        this.iconResId = iconResId;
        this.checkInDates = new ArrayList<>();
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

    // 添加打卡日期
    public void addCheckInDate(String date) {
        if (!checkInDates.contains(date)) {
            checkInDates.add(date);
        }
    }

    // 检查某天是否打卡
    public boolean hasCheckedIn(String date) {
        return checkInDates.contains(date);
    }
}

