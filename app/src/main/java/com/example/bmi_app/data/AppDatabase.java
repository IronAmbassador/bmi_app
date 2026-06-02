package com.example.bmi_app.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.bmi_app.data.dao.BmiRecordDao;
import com.example.bmi_app.data.entity.BmiRecordEntity;

@Database(
        entities = {BmiRecordEntity.class},
        version = 1,
        exportSchema = false

)
public abstract class AppDatabase extends RoomDatabase {
    // 声明数据库的表格操作：bmiRecordDao方法，返回的是BmiRecordDao接口
    private static volatile AppDatabase instance;// 声明
    public abstract BmiRecordDao bmiRecordDao();
    public static AppDatabase getInstance(Context context) {
        if (instance == null) { // 首次调用getInstance时,instance为空
            // 实例化
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "campusfit.db"
            ).allowMainThreadQueries().build();
        }
        return instance;
    }
}

// 单例模式开发：在AppDatabase中进行实例化，然后给线程提供一个实例对象，并且永远是同一个
