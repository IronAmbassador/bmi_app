package com.example.bmi_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CheckInActivity extends AppCompatActivity {

    private int selectedMinutes = 25;
    private Button btn15, btn25, btn30, btn45, btn60, btnTimerStart;
    private TextView tvSelectedDurationLabel, tvTimerDisplay, tvBack;
    private TextView tvTodayActual;
    private int todayTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
        bindViews();
        loadTodayTotal();
        updateDisplay();
        setButtonListeners();
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
        tvBack.setOnClickListener(v -> finish());
    }

    private void setButtonListeners() {
        btn15.setOnClickListener(v -> { resetAllButtons(); setButtonSelected(btn15); selectedMinutes = 15; updateDisplay(); });
        btn25.setOnClickListener(v -> { resetAllButtons(); setButtonSelected(btn25); selectedMinutes = 25; updateDisplay(); });
        btn30.setOnClickListener(v -> { resetAllButtons(); setButtonSelected(btn30); selectedMinutes = 30; updateDisplay(); });
        btn45.setOnClickListener(v -> { resetAllButtons(); setButtonSelected(btn45); selectedMinutes = 45; updateDisplay(); });
        btn60.setOnClickListener(v -> { resetAllButtons(); setButtonSelected(btn60); selectedMinutes = 60; updateDisplay(); });

        // ========== 修复：打卡不退出，只累加 ==========
        btnTimerStart.setOnClickListener(v -> {
            todayTotal += selectedMinutes;
            saveTodayTotal(todayTotal);
            updateTodayDisplay();

            Toast.makeText(this, "已记录 " + selectedMinutes + " 分钟", Toast.LENGTH_SHORT).show();
        });
    }

    // 读取今日运动总数
    private void loadTodayTotal() {
        SharedPreferences sp = getSharedPreferences("campusfit", MODE_PRIVATE);
        todayTotal = sp.getInt("today_total", 0);
        updateTodayDisplay();
    }

    // 保存到本地
    private void saveTodayTotal(int value) {
        SharedPreferences sp = getSharedPreferences("campusfit", MODE_PRIVATE);
        sp.edit().putInt("today_total", value).apply();
    }

    // 更新页面显示
    private void updateTodayDisplay() {
        tvTodayActual.setText(String.valueOf(todayTotal));
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
        button.setTextColor(android.R.color.white);
    }

    private void updateDisplay() {
        tvSelectedDurationLabel.setText(selectedMinutes + " 分钟");
        tvTimerDisplay.setText(String.format("%02d:00", selectedMinutes));
    }
}
