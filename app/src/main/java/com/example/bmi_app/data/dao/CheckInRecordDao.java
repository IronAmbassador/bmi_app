package com.example.bmi_app.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.bmi_app.data.entity.CheckInRecordEntity;

import java.util.List;

@Dao
public interface CheckInRecordDao {
    @Insert
    long insert(CheckInRecordEntity record);

    @Query("SELECT * FROM check_in_records ORDER BY id DESC")
    List<CheckInRecordEntity> getAllRecords();

    @Query("SELECT * FROM check_in_records WHERE date = :date")
    List<CheckInRecordEntity> getRecordsByDate(String date);

    @Query("SELECT SUM(minutes) FROM check_in_records WHERE date = :date")
    Integer getTotalMinutesByDate(String date);

    @Query("SELECT SUM(minutes) FROM check_in_records")
    Integer getTotalMinutes();

    @Query("DELETE FROM check_in_records WHERE id = :id")
    void deleteById(long id);
}
