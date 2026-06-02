package com.example.bmi_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bmi_app.data.AppDatabase;
import com.example.bmi_app.data.entity.BmiRecordEntity;
import com.example.bmi_app.data.entity.CheckInRecordEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {

    private LinearLayout historyList;
    private TextView tvEmpty;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        db = AppDatabase.getInstance(this);
        initViews();
        loadHistory();
    }

    private void initViews() {
        historyList = findViewById(R.id.history_list);
        tvEmpty = findViewById(R.id.tv_empty);

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
        loadHistory();
    }

    private void loadHistory() {
        historyList.removeAllViews();
        tvEmpty.setVisibility(View.GONE);

        List<Object> allRecords = new ArrayList<>();

        List<BmiRecordEntity> bmiRecords = db.bmiRecordDao().getAllRecords();
        if (bmiRecords != null) {
            allRecords.addAll(bmiRecords);
        }

        List<CheckInRecordEntity> checkInRecords = db.checkInRecordDao().getAllRecords();
        if (checkInRecords != null) {
            allRecords.addAll(checkInRecords);
        }

        if (allRecords.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            historyList.addView(tvEmpty);
            return;
        }

        Collections.sort(allRecords, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                long time1 = 0;
                long time2 = 0;

                if (o1 instanceof BmiRecordEntity) {
                    time1 = Long.MAX_VALUE;
                } else if (o1 instanceof CheckInRecordEntity) {
                    time1 = ((CheckInRecordEntity) o1).timestamp;
                }

                if (o2 instanceof BmiRecordEntity) {
                    time2 = Long.MAX_VALUE;
                } else if (o2 instanceof CheckInRecordEntity) {
                    time2 = ((CheckInRecordEntity) o2).timestamp;
                }

                return Long.compare(time2, time1);
            }
        });

        for (Object record : allRecords) {
            View itemView = LayoutInflater.from(this).inflate(R.layout.item_history, historyList, false);
            TextView tvRecordType = itemView.findViewById(R.id.tv_record_type);
            TextView tvRecordDate = itemView.findViewById(R.id.tv_record_date);
            TextView tvRecordDetail = itemView.findViewById(R.id.tv_record_detail);

            if (record instanceof BmiRecordEntity) {
                BmiRecordEntity bmi = (BmiRecordEntity) record;
                tvRecordType.setText("BMI记录");
                tvRecordDate.setText(bmi.data);
                tvRecordDetail.setText(String.format(Locale.getDefault(), "身高: %.1fcm, 体重: %.1fkg, BMI: %.1f (%s)",
                        bmi.height, bmi.weight, bmi.bmi, bmi.level));
            } else if (record instanceof CheckInRecordEntity) {
                CheckInRecordEntity checkin = (CheckInRecordEntity) record;
                tvRecordType.setText("运动打卡");
                tvRecordDate.setText(checkin.date);
                tvRecordDetail.setText(String.format(Locale.getDefault(), "运动时长: %d分钟", checkin.minutes));
            }

            historyList.addView(itemView);
        }
    }
}