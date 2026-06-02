package com.example.bmi_app.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "check_in_records")
public class CheckInRecordEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String date;
    public int minutes;
    public String note;
    public long timestamp;
}