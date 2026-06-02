package com.example.bmi_app.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bmi_app.data.entity.GoalEntity;

import java.util.List;

@Dao
public interface GoalDao {
    @Insert
    long insert(GoalEntity goal);

    @Update
    void update(GoalEntity goal);

    @Query("SELECT * FROM goals ORDER BY id DESC")
    List<GoalEntity> getAllGoals();

    @Query("SELECT * FROM goals WHERE goalType = :type LIMIT 1")
    GoalEntity getGoalByType(String type);

    @Query("DELETE FROM goals WHERE id = :id")
    void deleteById(long id);
}
