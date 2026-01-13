package com.example.myapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TestJumpActivity extends AppCompatActivity {
    private EditText etGameId;
    private Button btnJumpDomoko;
    private Button btnJumpCloud;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_jump);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etGameId = findViewById(R.id.etGameId);
        btnJumpDomoko = findViewById(R.id.btnJumpDomoko);
        btnJumpCloud = findViewById(R.id.btnJumpCloud);
        tvResult = findViewById(R.id.tvResult);

        // 设置默认游戏ID用于测试
        etGameId.setText("5176657");
    }
    public void jumpToTargetApp() {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(
                    "com.domoko.thumb",
                    "com.carlos.tvthumb.activity.LunchBridgeActivity"
            ));
            String jsonParams = "{\"actionType\":\"9\",\"contentId\":5181282,\"gfrom\":\"1\"}";
            intent.putExtra("Uri", jsonParams);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            // 处理异常，比如目标应用未安装
            Toast.makeText(this, "无法启动目标应用", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupListeners() {
        // 拉起新版大拇哥-游戏
        btnJumpDomoko.setOnClickListener(v -> {
            String gameId = etGameId.getText().toString().trim();
            if (TextUtils.isEmpty(gameId)) {
                Toast.makeText(this, "请输入游戏ID", Toast.LENGTH_SHORT).show();
                return;
            }
            jumpToDomoko(gameId);
//            jumpToTargetApp();  //上海数生小度推荐位联调
        });

        // 拉起新版云游戏-游戏
        btnJumpCloud.setOnClickListener(v -> {
            String gameId = etGameId.getText().toString().trim();
            if (TextUtils.isEmpty(gameId)) {
                Toast.makeText(this, "请输入游戏ID", Toast.LENGTH_SHORT).show();
                return;
            }
            jumpToCloud(gameId);
        });
    }

    /**
     * 跳转到新版大拇哥-游戏
     * @param contentId 游戏内容ID
     */
    private void jumpToDomoko(String contentId) {
        String scheme = "domokonew://carlos.tvthumb.cn/jumpTo?data={\"actionType\":\"9\",\"contentId\":" + contentId + ",\"gfrom\":\"1\"}";
        
        tvResult.setText("尝试跳转大拇哥游戏\nScheme: " + scheme);
        
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(scheme));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            
            Toast.makeText(this, "正在跳转大拇哥游戏...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            tvResult.setText("跳转失败: " + e.getMessage());
            Toast.makeText(this, "跳转失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 跳转到新版云游戏-游戏
     * @param gameId 游戏ID
     */
    private void jumpToCloud(String gameId) {
        String scheme = "newegame://cn.egame.terminal.cloud5g?EGAdParam={\"actionType\":\"2\",\"gameId\":" + gameId + ",\"gfrom\":\"1\"}";
        
        tvResult.setText("尝试跳转云游戏\nScheme: " + scheme);
        
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(scheme));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            
            Toast.makeText(this, "正在跳转云游戏...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            tvResult.setText("跳转失败: " + e.getMessage());
            Toast.makeText(this, "跳转失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}

