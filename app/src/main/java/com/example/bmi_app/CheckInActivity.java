package com.example.bmi_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bmi_app.data.AppDatabase;
import com.example.bmi_app.data.entity.CheckInRecordEntity;
import com.example.bmi_app.data.entity.GoalEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CheckInActivity extends AppCompatActivity {

    private int selectedMinutes = 25;
    private Button btn15, btn25, btn30, btn45, btn60, btnTimerStart;
    private TextView tvSelectedDurationLabel, tvTimerDisplay, tvBack;
    private TextView tvTodayActual, tvTodayGoalNum;
    private ProgressBar progressToday;
    
    private int todayTotal = 0;
    private int goalMinutes = 30;
    
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
        
        db = AppDatabase.getInstance(this);
        bindViews();
        loadTodayTotal();
        loadGoal();
        updateDisplay();
        setButtonListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTodayTotal();
        updateTodayDisplay();
    }

    private void bindViews() {
        tvBack = findViewById(R.id.tvBack);
        btn15 = findViewById(R.id.btnTimer15);
        btn25 = findViewById(R.id.btnTimer25);
        btn30 = findViewById(R.id.btnTimer30);
        btn45 = findViewById(R.id.btnTimer45);
        btn60 = findViewById(R.id.btnTimer60);
        btnTimerStart = findViewById(R.id.btnTimerStart);
        tvSelectedDurationLabel = findViewById(R.id.tvSelectedDurationLabel);
        tvTimerDisplay = findViewById(R.id.tvTimerDisplay);
        tvTodayActual = findViewById(R.id.tvTodayActual);
        tvTodayGoalNum = findViewById(R.id.tvTodayGoalNum);
        progressToday = findViewById(R.id.progressToday);
        
        tvBack.setOnClickListener(v -> finish());
    }

    private void setButtonListeners() {
        btn15.setOnClickListener(v -> { 
            resetAllButtons(); 
            setButtonSelected(btn15); 
            selectedMinutes = 15; 
            updateDisplay(); 
        });
        btn25.setOnClickListener(v -> { 
            resetAllButtons(); 
            setButtonSelected(btn25); 
            selectedMinutes = 25; 
            updateDisplay(); 
        });
        btn30.setOnClickListener(v -> { 
            resetAllButtons(); 
            setButtonSelected(btn30); 
            selectedMinutes = 30; 
            updateDisplay(); 
        });
        btn45.setOnClickListener(v -> { 
            resetAllButtons(); 
            setButtonSelected(btn45); 
            selectedMinutes = 45; 
            updateDisplay(); 
        });
        btn60.setOnClickListener(v -> { 
            resetAllButtons(); 
            setButtonSelected(btn60); 
            selectedMinutes = 60; 
            updateDisplay(); 
        });

        btnTimerStart.setOnClickListener(v -> {
            saveCheckIn();
        });
    }

    private void saveCheckIn() {
        CheckInRecordEntity record = new CheckInRecordEntity();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        record.date = sdf.format(new Date());
        record.minutes = selectedMinutes;
        record.timestamp = System.currentTimeMillis();
        record.note = "";
        
        db.checkInRecordDao().insert(record);
        
        todayTotal += selectedMinutes;
        saveTodayTotal(todayTotal);
        updateTodayDisplay();
        
        Toast.makeText(this, "已记录 " + selectedMinutes + " 分钟", Toast.LENGTH_SHORT).show();
    }

    private void loadTodayTotal() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = sdf.format(new Date());
        
        Integer total = db.checkInRecordDao().getTotalMinutesByDate(today);
        todayTotal = total != null ? total : 0;
        
        updateTodayDisplay();
    }

    private void loadGoal() {
        GoalEntity goal = db.goalDao().getGoalByType("EXERCISE");
        if (goal != null) {
            goalMinutes = (int) goal.targetValue;
        } else {
            goalMinutes = 30;
        }
        tvTodayGoalNum.setText(String.valueOf(goalMinutes));
        progressToday.setMax(goalMinutes);
    }

    private void saveTodayTotal(int value) {
        SharedPreferences sp = getSharedPreferences("campusfit", MODE_PRIVATE);
        sp.edit().putInt("today_total", value).apply();
    }

    private void updateTodayDisplay() {
        tvTodayActual.setText(String.valueOf(todayTotal));
        progressToday.setProgress(Math.min(todayTotal, goalMinutes));
    }

    private void resetAllButtons() {
        btn15.setBackgroundResource(R.drawable.btn_timer_normal);
        btn25.setBackgroundResource(R.drawable.btn_timer_normal);
        btn30.setBackgroundResource(R.drawable.btn_timer_normal);
        btn45.setBackgroundResource(R.drawable.btn_timer_normal);
        btn60.setBackgroundResource(R.drawable.btn_timer_normal);

        btn15.setTextColor(getColor(R.color.black));
        btn25.setTextColor(getColor(R.color.black));
        btn30.setTextColor(getColor(R.color.black));
        btn45.setTextColor(getColor(R.color.black));
        btn60.setTextColor(getColor(R.color.black));
    }

    private void setButtonSelected(Button button) {
        button.setBackgroundResource(R.drawable.btn_timer_selected);
        button.setTextColor(getColor(android.R.color.white));
    }

    private void updateDisplay() {
        tvSelectedDurationLabel.setText(selectedMinutes + " 分钟");
        tvTimerDisplay.setText(String.format(Locale.getDefault(), "%02d:00", selectedMinutes));
    }
}
