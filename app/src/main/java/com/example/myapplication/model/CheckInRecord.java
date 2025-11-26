package com.example.myapplication.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 打卡记录类
 */
public class CheckInRecord {
    private long timestamp;      // 时间戳
    private String note;         // 备注（可选）

    public CheckInRecord() {
        this.timestamp = System.currentTimeMillis();
        this.note = "";
    }

    public CheckInRecord(long timestamp, String note) {
        this.timestamp = timestamp;
        this.note = note != null ? note : "";
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note != null ? note : "";
    }

    /**
     * 获取日期 格式：2025-11-26
     */
    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    /**
     * 获取时间 格式：14:30
     */
    public String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    /**
     * 获取时间（带秒） 格式：14:30:15
     */
    public String getTimeWithSeconds() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    /**
     * 判断是否有备注
     */
    public boolean hasNote() {
        return note != null && !note.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "CheckInRecord{" +
                "timestamp=" + timestamp +
                ", date='" + getDate() + '\'' +
                ", time='" + getTime() + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}

