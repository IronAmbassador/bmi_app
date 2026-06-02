package com.example.bmi_app.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.bmi_app.data.entity.BmiRecordEntity;

import androidx.room.Query;
import java.util.List;

@Dao
public interface BmiRecordDao {

    // 1. 计算完 BMI 后，生成一条记录并插入数据库
    @Insert
    long insert(BmiRecordEntity record);

    // 2. 每次进入 Bmi 页面，查询最新的一条 BMI 记录
    @Query("SELECT * FROM bmi_records ORDER BY id DESC LIMIT 1")
    BmiRecordEntity getLatestRecord();

    // 3. 在 stats/history 页面查询所有 BMI 记录（用于统计或展示历史记录）
    @Query("SELECT * FROM bmi_records ORDER BY id DESC")
    List<BmiRecordEntity> getAllRecords();
}
