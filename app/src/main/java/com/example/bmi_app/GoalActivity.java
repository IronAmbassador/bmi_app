package com.example.bmi_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bmi_app.data.AppDatabase;
import com.example.bmi_app.data.entity.GoalEntity;

public class GoalActivity extends AppCompatActivity {

    private EditText etTargetBmi;
    private EditText etTargetMinutes;
    private TextView tvCurrentBmiGoal;
    private TextView tvCurrentExerciseGoal;
    private Button btnSaveBmiGoal;
    private Button btnSaveExerciseGoal;
    
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        db = AppDatabase.getInstance(this);
        initViews();
        loadCurrentGoals();
    }

    private void initViews() {
        etTargetBmi = findViewById(R.id.et_target_bmi);
        etTargetMinutes = findViewById(R.id.et_target_minutes);
        tvCurrentBmiGoal = findViewById(R.id.tv_current_bmi_goal);
        tvCurrentExerciseGoal = findViewById(R.id.tv_current_exercise_goal);
        btnSaveBmiGoal = findViewById(R.id.btn_save_bmi_goal);
        btnSaveExerciseGoal = findViewById(R.id.btn_save_exercise_goal);

        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSaveBmiGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBmiGoal();
            }
        });

        btnSaveExerciseGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExerciseGoal();
            }
        });
    }

    private void saveBmiGoal() {
        String bmiStr = etTargetBmi.getText().toString().trim();
        if (bmiStr.isEmpty()) {
            Toast.makeText(this, "请输入目标BMI", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            float targetBmi = Float.parseFloat(bmiStr);
            if (targetBmi < 10 || targetBmi > 50) {
                Toast.makeText(this, "请输入合理的BMI值（10-50）", Toast.LENGTH_SHORT).show();
                return;
            }

            GoalEntity existingGoal = db.goalDao().getGoalByType("BMI");
            GoalEntity goal;
            if (existingGoal != null) {
                goal = existingGoal;
                goal.targetValue = targetBmi;
                db.goalDao().update(goal);
            } else {
                goal = new GoalEntity();
                goal.goalType = "BMI";
                goal.targetValue = targetBmi;
                goal.currentValue = 0;
                goal.createdAt = System.currentTimeMillis();
                db.goalDao().insert(goal);
            }

            Toast.makeText(this, "BMI目标保存成功", Toast.LENGTH_SHORT).show();
            loadCurrentGoals();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入有效的数字", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveExerciseGoal() {
        String minutesStr = etTargetMinutes.getText().toString().trim();
        if (minutesStr.isEmpty()) {
            Toast.makeText(this, "请输入每日运动目标", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int targetMinutes = Integer.parseInt(minutesStr);
            if (targetMinutes < 1 || targetMinutes > 300) {
                Toast.makeText(this, "请输入合理的运动时间（1-300分钟）", Toast.LENGTH_SHORT).show();
                return;
            }

            GoalEntity existingGoal = db.goalDao().getGoalByType("EXERCISE");
            GoalEntity goal;
            if (existingGoal != null) {
                goal = existingGoal;
                goal.targetValue = targetMinutes;
                db.goalDao().update(goal);
            } else {
                goal = new GoalEntity();
                goal.goalType = "EXERCISE";
                goal.targetValue = targetMinutes;
                goal.currentValue = 0;
                goal.createdAt = System.currentTimeMillis();
                db.goalDao().insert(goal);
            }

            Toast.makeText(this, "运动目标保存成功", Toast.LENGTH_SHORT).show();
            loadCurrentGoals();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入有效的数字", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadCurrentGoals() {
        GoalEntity bmiGoal = db.goalDao().getGoalByType("BMI");
        if (bmiGoal != null) {
            tvCurrentBmiGoal.setText(String.format("目标BMI: %.1f", bmiGoal.targetValue));
        } else {
            tvCurrentBmiGoal.setText("目标BMI: --");
        }

        GoalEntity exerciseGoal = db.goalDao().getGoalByType("EXERCISE");
        if (exerciseGoal != null) {
            tvCurrentExerciseGoal.setText(String.format("每日运动: %.0f 分钟", exerciseGoal.targetValue));
        } else {
            tvCurrentExerciseGoal.setText("每日运动: -- 分钟");
        }
    }
}
