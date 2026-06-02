package com.example.bmi_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bmi_app.data.AppDatabase;
import com.example.bmi_app.data.dao.BmiRecordDao;
import com.example.bmi_app.data.entity.BmiRecordEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BmiActivity extends AppCompatActivity {

    private TextView tvBack, tvHelp, tvStatus, tvResultTip;
    private EditText etHeight, etWeight;
    private Button btnCalculate, btnReset;
    private float bmi = 0.0f;
    private AppDatabase appDatabase;
    private BmiRecordDao bmiRecordDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        // 绑定控件
        tvBack = findViewById(R.id.tvBack);
        tvHelp = findViewById(R.id.tvHelp);
        tvStatus = findViewById(R.id.tvStatus);
        tvResultTip = findViewById(R.id.tvResultTip);
        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnReset = findViewById(R.id.btnReset);

        // 返回上一页
        tvBack.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("bmi_val", bmi);
            setResult(RESULT_OK, intent);
            finish();
        });

        // 帮助弹窗
        tvHelp.setOnClickListener(v -> showBmiHelpDialog());

        // 计算BMI
        btnCalculate.setOnClickListener(v -> calculateBmi());

        // 重置输入
        btnReset.setOnClickListener(v -> resetAll());

        // 数据库初始化
        appDatabase = AppDatabase.getInstance(this);
        bmiRecordDao = appDatabase.bmiRecordDao();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BmiRecordEntity record = bmiRecordDao.getLatestRecord();
        if (record != null) {
            tvStatus.setText(String.format(Locale.getDefault(), "%.1f", record.bmi));
        }
    }

    // BMI帮助弹窗
    private void showBmiHelpDialog() {
        new AlertDialog.Builder(this)
                .setTitle("BMI说明")
                .setMessage("BMI(身体质量指数)\n\n" +
                        "计算公式：\n体重(kg) ÷ 身高(m)²\n\n" +
                        "中国标准：\n" +
                        "偏瘦：BMI ＜ 18.5\n" +
                        "正常：18.5 ≤ BMI ＜ 24.0\n" +
                        "偏胖：24.0 ≤ BMI ＜ 28.0\n" +
                        "肥胖：BMI ≥ 28.0")
                .setPositiveButton("确定", null)
                .show();
    }

    // 计算BMI
    private void calculateBmi() {
        String heightStr = etHeight.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();

        if (heightStr.isEmpty() || weightStr.isEmpty()) {
            Toast.makeText(this, "请输入身高体重", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // 数据解析
            float height = Float.parseFloat(heightStr) / 100f;
            float weight = Float.parseFloat(weightStr);
            bmi = weight / (height * height);

            // 显示结果
            tvStatus.setText(String.format(Locale.getDefault(), "%.1f", bmi));
            String level = setBmiLevel(bmi);

            // 保存到数据库（完全匹配你的实体类）
            BmiRecordEntity record = new BmiRecordEntity();
            record.data = getCurrentDate(); // 自动填入当前日期
            record.height = Float.parseFloat(heightStr);
            record.weight = weight;
            record.bmi = bmi;
            record.level = level; // 保存健康等级

            bmiRecordDao.insert(record);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入有效数字", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
        }
    }

    // 获取当前日期
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    // 设置BMI等级并返回等级文字
    private String setBmiLevel(float bmi) {
        String level;
        if (bmi < 18.5f) {
            level = "偏瘦";
        } else if (bmi < 24f) {
            level = "正常";
        } else if (bmi < 28f) {
            level = "偏胖";
        } else {
            level = "肥胖";
        }
        tvResultTip.setText(level);
        return level;
    }

    // 重置所有输入和显示
    private void resetAll() {
        etHeight.setText("");
        etWeight.setText("");
        tvStatus.setText("--");
        tvResultTip.setText("请先完成计算");
    }
}