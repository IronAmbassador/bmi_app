package com.example.bmi_app.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.bmi_app.data.dao.BmiRecordDao;
import com.example.bmi_app.data.dao.CheckInRecordDao;
import com.example.bmi_app.data.dao.GoalDao;
import com.example.bmi_app.data.entity.BmiRecordEntity;
import com.example.bmi_app.data.entity.CheckInRecordEntity;
import com.example.bmi_app.data.entity.GoalEntity;

@Database(
        entities = {BmiRecordEntity.class, GoalEntity.class, CheckInRecordEntity.class},
        version = 2,
        exportSchema = false

)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instance;
    public abstract BmiRecordDao bmiRecordDao();
    public abstract GoalDao goalDao();
    public abstract CheckInRecordDao checkInRecordDao();
    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "campusfit.db"
                    ).fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}