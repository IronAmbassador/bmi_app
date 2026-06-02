package com.example.bmi_app.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// 告诉编译器，这是一个实体类，帮我把这个实体类转换成数据库表格,表格名字是bmi_records
@Entity(tableName = "bmi_records")
public class BmiRecordEntity { // 实体类，每个实例对应一行bmi记录
    // 定义一个bmi记录
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String data; // 日期
    public float height; // 身高
    public float weight; // 体重
    public float bmi;
    public String level; // 健康状况
}
