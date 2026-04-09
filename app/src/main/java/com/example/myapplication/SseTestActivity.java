package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * SSE (Server-Sent Events) 流式接收测试页面
 *
 * 演示如何用 OkHttp 接收服务端的 SSE 流式推送数据，
 * 每收到一个字符就追加显示到 TextView 上，模拟逐字打印效果。
 */
public class SseTestActivity extends AppCompatActivity {

    private static final String TAG = "SseTestActivity";
    
    // ===== 后端地址配置 =====
    // 【模拟器调试】使用 10.0.2.2（模拟器访问宿主机的特殊IP）
    // 【真机调试】使用电脑局域网IP（当前为 10.102.33.178）
    
    // 模拟器版本：
    // private static final String SSE_URL = "http://10.0.2.2:8080/api/sse/stream";
    
    // 真机版本（当前电脑IP）：
    private static final String SSE_URL = "http://10.102.33.178:8080/api/sse/stream";

    private TextView tvOutput;
    private TextView tvStatus;
    private Button btnStart;
    private Button btnClear;

    private final OkHttpClient client = new OkHttpClient();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    // 当前正在进行的请求，用于取消
    private Call currentCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sse_test);

        tvOutput = findViewById(R.id.tvOutput);
        tvStatus = findViewById(R.id.tvStatus);
        btnStart = findViewById(R.id.btnStart);
        btnClear = findViewById(R.id.btnClear);

        btnStart.setOnClickListener(v -> startSseStream());

        btnClear.setOnClickListener(v -> {
            tvOutput.setText("");
            tvStatus.setText("已清空，可重新请求");
        });
    }

    /**
     * 发起 SSE 请求，流式读取服务端推送的数据
     */
    private void startSseStream() {
        Log.i(TAG, "========== 准备发起 SSE 请求 ==========");
        Log.i(TAG, "URL: " + SSE_URL);
        
        // 取消上一次未完成的请求
        if (currentCall != null && !currentCall.isCanceled()) {
            currentCall.cancel();
            Log.i(TAG, "已取消上一次请求");
        }

        tvOutput.setText("");
        setStatus("正在连接...");
        btnStart.setEnabled(false);

        Request request = new Request.Builder()
                .url(SSE_URL)
                // SSE 标准请求头，告诉服务端接受事件流
                .addHeader("Accept", "text/event-stream")
                .addHeader("Cache-Control", "no-cache")
                .build();
        
        Log.i(TAG, "请求头:");
        Log.i(TAG, "  Accept: text/event-stream");
        Log.i(TAG, "  Cache-Control: no-cache");

        currentCall = client.newCall(request);
        currentCall.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "========== SSE 连接失败 ==========", e);
                mainHandler.post(() -> {
                    setStatus("连接失败：" + e.getMessage());
                    btnStart.setEnabled(true);
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "========== SSE 连接成功 ==========");
                Log.i(TAG, "Response Code: " + response.code());
                Log.i(TAG, "Content-Type: " + response.header("Content-Type"));
                
                if (!response.isSuccessful()) {
                    mainHandler.post(() -> {
                        setStatus("请求失败，HTTP " + response.code());
                        btnStart.setEnabled(true);
                    });
                    return;
                }

                mainHandler.post(() -> setStatus("已连接，正在接收数据..."));

                ResponseBody body = response.body();
                if (body == null) {
                    Log.e(TAG, "响应 body 为空");
                    mainHandler.post(() -> {
                        setStatus("响应 body 为空");
                        btnStart.setEnabled(true);
                    });
                    return;
                }

                // 流式读取：逐行读取 SSE 格式的数据
                try (InputStream is = body.byteStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {

                    String line;
                    int lineNumber = 0;
                    
                    Log.i(TAG, "========== 开始接收 SSE 数据流 ==========");
                    
                    while ((line = reader.readLine()) != null) {
                        lineNumber++;
                        
                        // 打印每一行原始数据（包括空行）
                        if (line.isEmpty()) {
                            Log.d(TAG, "Line " + lineNumber + ": [空行 - SSE事件分隔符]");
                        } else {
                            Log.d(TAG, "Line " + lineNumber + ": \"" + line + "\"");
                        }
                        
                        // SSE 数据格式：每行以 "data: " 开头
                        if (line.startsWith("data:")) {
                            // 提取 data: 后面的内容
                            String data = line.substring("data:".length()).trim();
                            
                            Log.i(TAG, "  ↳ 解析出的内容: \"" + data + "\"");

                            if ("[DONE]".equals(data)) {
                                // 收到结束标志
                                Log.i(TAG, "========== 收到结束标志 [DONE] ==========");
                                mainHandler.post(() -> {
                                    setStatus("流式接收完成 ✓");
                                    btnStart.setEnabled(true);
                                });
                                break;
                            }

                            // 追加到界面上
                            final String displayData = data;
                            mainHandler.post(() -> tvOutput.append(displayData));

                        }
                        // 空行是 SSE 事件分隔符，忽略即可
                    }
                    
                    Log.i(TAG, "========== SSE 数据流结束，共接收 " + lineNumber + " 行 ==========");

                } catch (IOException e) {
                    Log.e(TAG, "读取数据出错", e);
                    if (!call.isCanceled()) {
                        mainHandler.post(() -> {
                            setStatus("读取数据出错：" + e.getMessage());
                            btnStart.setEnabled(true);
                        });
                    }
                }
            }
        });
    }

    private void setStatus(String status) {
        tvStatus.setText("状态：" + status);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (currentCall != null) {
            currentCall.cancel();
        }
    }
}
