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
                    context.getApplicationContext(), // 获取当前应用的上下文，或者说获取当前应用的包名
                    AppDatabase.class, // 希望构建器实例化的对象
                    "campusfit.db" // 制定要创建的数据库名称
            ).build(); // 让构建器给你创建一个数据库的实例
        }
        return instance;
    }
}

// 单例模式开发：在AppDatabase中进行实例化，然后给线程提供一个实例对象，并且永远是同一个
