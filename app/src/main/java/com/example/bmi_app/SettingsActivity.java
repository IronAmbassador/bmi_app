package com.example.bmi_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingsActivity extends AppCompatActivity {

    private ImageView ivAvatar;
    private EditText etName;
    private Switch switchBmi, switchExercise, switchWater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViewById(R.id.tvBack).setOnClickListener(v -> finish());
        initViews();
        loadSettings();

    }

    private void initViews() {
        ivAvatar = findViewById(R.id.ivSettingsAvatar);
        etName = findViewById(R.id.etSettingsName);
        switchBmi = findViewById(R.id.switchNotifyBmi);
        switchExercise = findViewById(R.id.switchNotifyExercise);
        switchWater = findViewById(R.id.switchNotifyWater);

        // 保存按钮
        findViewById(R.id.btnSaveSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });

        // 恢复默认按钮  ←←← 新增
        findViewById(R.id.btnResetSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetDefaultSettings();
            }
        });
    }

    private void loadSettings() { // 至少要在onCreate的initView之后调用
        // TODO 从 SharedPreferences 加载设置
        // 1.获取sp对象
        SharedPreferences sp = getSharedPreferences("campusfit", MODE_PRIVATE);
        // 2.getXXX读取数据
        String name = sp.getString("nickname", "");
        boolean notifyBmi = sp.getBoolean("notifyBmi", true);
        boolean notifyExercise = sp.getBoolean("notifyExercise", true);
        boolean notifyWater = sp.getBoolean("notifyWater", true);
        // 3.显示到网页上
        etName.setText(name);
        switchBmi.setChecked(notifyBmi);
        switchExercise.setChecked(notifyExercise);
        switchWater.setChecked(notifyWater);
    }

    private void saveSettings() {
        // TODO 保存设置到 SharedPreferences
        // 1.获取sp对象
        SharedPreferences sp = getSharedPreferences("campusfit", MODE_PRIVATE);
        // 2.获得sp对象的编辑器
        SharedPreferences.Editor editor = sp.edit();
        // 3.putXXX,写配置数据到sp文件中，前提：拿到页面的数据：获得页面配置的4个数据了
        String nickname = etName.getText().toString(); // 昵称
        boolean notifyBmi = switchBmi.isChecked(); // 如果按钮开启，返回true
        boolean notifyExercise = switchExercise.isChecked();
        boolean notifyWater = switchWater.isChecked();
        editor.putString("nickname", nickname);
        editor.putBoolean("notifyBmi", notifyBmi);
        editor.putBoolean("notifyExercise", notifyExercise);
        editor.putBoolean("notifyWater", notifyWater);
        // 4.提交修改
        editor.apply();
        // 5.成功提醒
        Toast.makeText(this, "保存成功", Toast.LENGTH_LONG).show();
    }

    // 新增：恢复默认设置
    private void resetDefaultSettings() {
        // 清空昵称
        etName.setText("");

        // 恢复默认开关状态
        switchBmi.setChecked(true);
        switchExercise.setChecked(true);
        switchWater.setChecked(false);

        // 保存默认值
        saveSettings();

        Toast.makeText(this, "已恢复默认设置", Toast.LENGTH_SHORT).show();
    }
}