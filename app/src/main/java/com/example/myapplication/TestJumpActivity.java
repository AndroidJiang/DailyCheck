package com.example.myapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TestJumpActivity extends AppCompatActivity {
    private EditText etDomokoScheme;
    private EditText etCloudScheme;
    private EditText etDomokoUri;
    private EditText etCloudUri;
    private Button btnJumpDomoko;
    private Button btnJumpCloud;
    private Button btnJumpDomokoUri;
    private Button btnJumpCloudUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_jump);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etDomokoScheme = findViewById(R.id.etDomokoScheme);
        etCloudScheme = findViewById(R.id.etCloudScheme);
        etDomokoUri = findViewById(R.id.etDomokoUri);
        etCloudUri = findViewById(R.id.etCloudUri);
        btnJumpDomoko = findViewById(R.id.btnJumpDomoko);
        btnJumpCloud = findViewById(R.id.btnJumpCloud);
        btnJumpDomokoUri = findViewById(R.id.btnJumpDomokoUri);
        btnJumpCloudUri = findViewById(R.id.btnJumpCloudUri);

        // 设置默认游戏ID用于测试
        etDomokoScheme.setText("domokonew://carlos.tvthumb.cn/jumpTo?data={\"actionType\":\"9\",\"contentId\":5181282,\"gfrom\":\"1\"}");
        etCloudScheme.setText("newegame://cn.egame.terminal.cloud5g?EGAdParam={\"actionType\":\"2\",\"gameId\":5177423,\"gfrom\":\"1\"}");
        etDomokoUri.setText("{\"actionType\":\"9\",\"contentId\":5181282}");
        etDomokoUri.setText("{\"actionType\":\"9\",\"contentId\":5181282}");
        etCloudUri.setText("{\"EGAdType\":\"6\",\"gameId\":5182006}");
    }


    private void setupListeners() {
        // 拉起新版大拇哥-游戏
        btnJumpDomoko.setOnClickListener(v -> {
            String url = etDomokoScheme.getText().toString().trim();
            jumpToDomoko(url);
        });

        // 拉起新版云游戏-游戏
        btnJumpCloud.setOnClickListener(v -> {
            String url = etCloudScheme.getText().toString().trim();
            jumpToCloud(url);
        });
        btnJumpDomokoUri.setOnClickListener(v -> {
            String url = etDomokoUri.getText().toString().trim();
            jumpToDomokoUri(url);  //上海数生小度推荐位联调
        });
        btnJumpCloudUri.setOnClickListener(v -> {
            String url = etCloudUri.getText().toString().trim();
            jumpToCloudUri(url);  //上海数生小度推荐位联调
        });
    }

    /**
     * 跳转到新版大拇哥-游戏
     */
    private void jumpToDomoko(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            Toast.makeText(this, "正在跳转大拇哥游戏...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "跳转失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 跳转到新版云游戏-游戏
     * @param gameId 游戏ID
     */
    private void jumpToCloud(String url) {


        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            
            Toast.makeText(this, "正在跳转云游戏...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "跳转失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public void jumpToDomokoUri(String url) {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(
                    "com.domoko.thumb",
                    "com.carlos.tvthumb.activity.LunchBridgeActivity"
            ));

            intent.putExtra("Uri", url);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            // 处理异常，比如目标应用未安装
            Toast.makeText(this, "无法启动目标应用", Toast.LENGTH_SHORT).show();
        }
    }
    public void jumpToCloudUri(String url) {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(
                    "cn.egame.terminal.cloud5g",
                    "cn.egame.terminal.cloud5g.ui.launch.LaunchActivity"
            ));
            intent.putExtra("Uri", url);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            // 处理异常，比如目标应用未安装
            Toast.makeText(this, "无法启动目标应用", Toast.LENGTH_SHORT).show();
        }
    }
}

