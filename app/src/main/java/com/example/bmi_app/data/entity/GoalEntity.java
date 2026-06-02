package com.example.bmi_app.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "goals")
public class GoalEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String goalType;
    public float targetValue;
    public float currentValue;
    public String targetDate;
    public long createdAt;
}
