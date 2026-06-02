package com.example.bmi_app;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bmi_app.data.AppDatabase;
import com.example.bmi_app.data.entity.BmiRecordEntity;
import com.example.bmi_app.data.entity.CheckInRecordEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StatsActivity extends AppCompatActivity {

    private TextView tvBmiCount;
    private TextView tvLatestBmi;
    private TextView tvCheckinCount;
    private TextView tvTotalMinutes;
    private TextView tvTodayExercise;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        db = AppDatabase.getInstance(this);
        initViews();
        loadStats();
    }

    private void initViews() {
        tvBmiCount = findViewById(R.id.tv_bmi_count);
        tvLatestBmi = findViewById(R.id.tv_latest_bmi);
        tvCheckinCount = findViewById(R.id.tv_checkin_count);
        tvTotalMinutes = findViewById(R.id.tv_total_minutes);
        tvTodayExercise = findViewById(R.id.tv_today_exercise);

        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStats();
    }

    private void loadStats() {
        loadBmiStats();
        loadCheckinStats();
        loadTodayStats();
    }

    private void loadBmiStats() {
        List<BmiRecordEntity> records = db.bmiRecordDao().getAllRecords();
        if (records != null && !records.isEmpty()) {
            tvBmiCount.setText(String.valueOf(records.size()));
            BmiRecordEntity latest = records.get(0);
            tvLatestBmi.setText(String.format(Locale.getDefault(), "%.1f", latest.bmi));
        } else {
            tvBmiCount.setText("0");
            tvLatestBmi.setText("--");
        }
    }

    private void loadCheckinStats() {
        List<CheckInRecordEntity> records = db.checkInRecordDao().getAllRecords();
        if (records != null) {
            tvCheckinCount.setText(String.valueOf(records.size()));
            Integer totalMinutes = db.checkInRecordDao().getTotalMinutes();
            if (totalMinutes != null) {
                tvTotalMinutes.setText(String.format(Locale.getDefault(), "%d 分钟", totalMinutes));
            } else {
                tvTotalMinutes.setText("0 分钟");
            }
        } else {
            tvCheckinCount.setText("0");
            tvTotalMinutes.setText("0 分钟");
        }
    }

    private void loadTodayStats() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = sdf.format(new Date());
        Integer todayMinutes = db.checkInRecordDao().getTotalMinutesByDate(today);
        if (todayMinutes != null) {
            tvTodayExercise.setText(String.format(Locale.getDefault(), "今日已运动: %d 分钟", todayMinutes));
        } else {
            tvTodayExercise.setText("今日已运动: 0 分钟");
        }
    }
}