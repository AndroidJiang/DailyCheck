package com.example.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.myapplication.model.Habit;
import com.example.myapplication.utils.SPUtils;

public class AddHabitActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText etTitle;
    private TextView tvCount;
    private ImageButton btnMinus, btnPlus;
    private Button btnSave;
    private int targetCount = 1;
    private long habitId = -1;
    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);
        
        initViews();
        loadIntentData();
        setupListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        etTitle = findViewById(R.id.etTitle);
        tvCount = findViewById(R.id.tvCount);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        btnSave = findViewById(R.id.btnSave);

        updateCountDisplay();
    }

    private void loadIntentData() {
        habitId = getIntent().getLongExtra("habit_id", -1);
        if (habitId != -1) {
            isEdit = true;
            String title = getIntent().getStringExtra("habit_title");
            targetCount = getIntent().getIntExtra("habit_target", 1);
            
            etTitle.setText(title);
            updateCountDisplay();
            btnSave.setText("保存");
            toolbar.setTitle("编辑习惯");
        } else {
            toolbar.setTitle("添加习惯");
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupListeners() {
        btnMinus.setOnClickListener(v -> {
            if (targetCount > 1) {
                targetCount--;
                updateCountDisplay();
            }
        });

        btnPlus.setOnClickListener(v -> {
            if (targetCount < 99) {
                targetCount++;
                updateCountDisplay();
            }
        });

        btnSave.setOnClickListener(v -> saveHabit());
    }

    private void updateCountDisplay() {
        tvCount.setText(String.valueOf(targetCount));
    }

    private void saveHabit() {
        String title = etTitle.getText().toString().trim();
        
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "请输入习惯名称", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isEdit) {
            // 编辑模式：更新现有习惯
            Habit habit = SPUtils.getHabitById(this, habitId);
            if (habit != null) {
                habit.setTitle(title);
                habit.setTargetCount(targetCount);
                habit.setIconResId(R.drawable.ic_daka);
                SPUtils.updateHabit(this, habit);
                Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
            }
        } else {
            // 新建模式
            Habit habit = new Habit(title, targetCount, R.drawable.ic_daka);
            SPUtils.addHabit(this, habit);
            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}

