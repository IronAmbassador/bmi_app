package com.example.bmi_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private float bmi = 0f;
    private TextView tvTodo;
    private TextView tvStatExercise; // 本周运动
    private TextView tvTodayMain;    // 主页今日运动（如果有）

    private TextView tvGreeting;

    // 原来的BMI保留
    private final ActivityResultLauncher<Intent> bmiLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    bmi = result.getData().getFloatExtra("bmi_val", 0f);
                    updateBmiDisplay();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTodo = findViewById(R.id.tvTodo);
        tvStatExercise = findViewById(R.id.tvStatExercise);

        tvGreeting = findViewById(R.id.tv_greeting);

        bindClickEvents();
    }

    // ========== 每次回到主页都刷新数据 ==========
    @Override
    protected void onResume() {
        super.onResume();
        updateBmiDisplay();
        updateTodayTotalFromSP(); // 主页回显今日运动

        // 读取昵称显示欢迎语
        SharedPreferences sp = getSharedPreferences("campusfit", MODE_PRIVATE);
        String nickname = sp.getString("nickname", "");
        if (nickname.isEmpty()) {
            tvGreeting.setText("你好，同学 👋");
        } else {
            tvGreeting.setText("你好，" + nickname + " 👋");
        }
    }

    // ========== 主页显示今日运动总数 ==========
    private void updateTodayTotalFromSP() {
        SharedPreferences sp = getSharedPreferences("campusfit", MODE_PRIVATE);
        int today = sp.getInt("today_total", 0);

        // 如果你主页有 今日运动 控件，用这个
        // tvTodayMain.setText(today + " 分钟");

        // 如果没有，直接显示在本周运动里（最稳妥）
        tvStatExercise.setText(today + " 分钟");
    }

    private void updateBmiDisplay() {
        tvTodo.setText(String.format("%.2f", bmi));
    }

    private void bindClickEvents() {
        // BMI
        findViewById(R.id.layoutBmi).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BmiActivity.class);
            bmiLauncher.launch(intent);
        });

        // 运动打卡（普通跳转）
        findViewById(R.id.layoutCheckin).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CheckInActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.layoutGoal).setOnClickListener(v ->
                startActivity(new Intent(this, GoalActivity.class)));
        findViewById(R.id.layoutStats).setOnClickListener(v ->
                startActivity(new Intent(this, StatsActivity.class)));
        findViewById(R.id.btnHistory).setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));
        findViewById(R.id.btnSettings).setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));
        findViewById(R.id.tvContact).setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:10086"));
            startActivity(i);
        });
    }
}