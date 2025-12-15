package com.example.myapplication.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 打卡记录类
 */
public class CheckInRecord {
    private long startTimestamp; // 开始时间戳
    private long endTimestamp;   // 结束时间戳
    private String note;         // 备注（可选）

    public CheckInRecord() {
        this.startTimestamp = System.currentTimeMillis();
        this.endTimestamp = System.currentTimeMillis();
        this.note = "";
    }

    public CheckInRecord(long startTimestamp, long endTimestamp, String note) {
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.note = note != null ? note : "";
    }

    // 兼容旧版本：单一时间戳
    public CheckInRecord(long timestamp, String note) {
        this.startTimestamp = timestamp;
        this.endTimestamp = timestamp;
        this.note = note != null ? note : "";
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    // 兼容旧版本
    public long getTimestamp() {
        return startTimestamp;
    }

    public void setTimestamp(long timestamp) {
        this.startTimestamp = timestamp;
        this.endTimestamp = timestamp;
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
        return sdf.format(new Date(startTimestamp));
    }

    /**
     * 获取开始时间 格式：14:30
     */
    public String getStartTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date(startTimestamp));
    }

    /**
     * 获取结束时间 格式：15:30
     */
    public String getEndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date(endTimestamp));
    }

    /**
     * 获取时间段 格式：14:30-15:30
     */
    public String getTime() {
        if (startTimestamp == endTimestamp) {
            // 如果开始和结束时间相同，只显示一个时间
            return getStartTime();
        }
        return getStartTime() + "-" + getEndTime();
    }

    /**
     * 获取持续时长（分钟）
     */
    public long getDurationMinutes() {
        return (endTimestamp - startTimestamp) / (1000 * 60);
    }

    /**
     * 获取持续时长文本
     */
    public String getDurationText() {
        long minutes = getDurationMinutes();
        if (minutes == 0) {
            return "";
        }
        if (minutes < 60) {
            return minutes + "分钟";
        }
        long hours = minutes / 60;
        long remainMinutes = minutes % 60;
        if (remainMinutes == 0) {
            return hours + "小时";
        }
        return hours + "小时" + remainMinutes + "分钟";
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
                "startTimestamp=" + startTimestamp +
                ", endTimestamp=" + endTimestamp +
                ", date='" + getDate() + '\'' +
                ", time='" + getTime() + '\'' +
                ", duration='" + getDurationText() + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}

