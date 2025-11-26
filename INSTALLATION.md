# 安装和运行指南

## 📋 系统要求

### 开发环境
- **操作系统**: Windows 10/11, macOS 10.14+, Linux (Ubuntu 18.04+)
- **Android Studio**: 4.0 或更高版本
- **JDK**: Java 8 或更高版本
- **Gradle**: 6.0+ (通常由 Android Studio 管理)

### 测试设备
- **Android 版本**: 5.0 (API 21) 或更高
- **屏幕尺寸**: 建议 5 英寸以上
- **存储空间**: 至少 20MB 可用空间

## 🚀 安装步骤

### 方法1: 使用 Android Studio（推荐）

#### 1. 打开项目
```bash
1. 启动 Android Studio
2. 选择 "Open an Existing Project"
3. 导航到 D:\MyApplication
4. 点击 "OK"
```

#### 2. 等待 Gradle 同步
```
- Android Studio 会自动下载依赖
- 查看底部状态栏确认同步完成
- 如有错误，点击 "Sync Now" 重试
```

#### 3. 配置运行设备

**选项A: 使用真实设备**
```bash
1. 启用开发者选项
   - 设置 → 关于手机 → 连续点击"版本号"7次
2. 启用 USB 调试
   - 设置 → 开发者选项 → USB调试（开启）
3. 连接设备到电脑
4. 在 Android Studio 设备列表中选择您的设备
```

**选项B: 使用模拟器**
```bash
1. 打开 AVD Manager
   - Tools → AVD Manager
2. 创建虚拟设备
   - Create Virtual Device
   - 选择设备型号（推荐 Pixel 3）
   - 选择系统镜像（推荐 API 28）
   - 完成创建
3. 启动模拟器
```

#### 4. 运行应用
```bash
1. 在工具栏中点击 "Run" 按钮 (绿色三角形)
   - 或使用快捷键: Shift + F10 (Windows/Linux), Control + R (macOS)
2. 等待应用编译和安装
3. 应用会自动启动
```

### 方法2: 命令行构建

#### 1. 进入项目目录
```bash
cd D:\MyApplication
```

#### 2. 构建 APK
```bash
# Windows
.\gradlew.bat assembleDebug

# macOS/Linux
./gradlew assembleDebug
```

#### 3. 安装到设备
```bash
# Windows
.\gradlew.bat installDebug

# macOS/Linux
./gradlew installDebug
```

#### 4. 查找 APK 文件
```
位置: app/build/outputs/apk/debug/app-debug.apk
```

## 🔧 常见问题解决

### 问题1: Gradle 同步失败

**原因**: 网络问题或依赖下载失败

**解决方案**:
```bash
1. 检查网络连接
2. 配置代理（如需要）
   - File → Settings → Appearance & Behavior → System Settings → HTTP Proxy
3. 清理并重新构建
   - Build → Clean Project
   - Build → Rebuild Project
```

### 问题2: 设备未识别

**原因**: USB 驱动未安装或 USB 调试未开启

**解决方案**:
```bash
1. 重新安装 USB 驱动
2. 确认 USB 调试已开启
3. 尝试更换 USB 线缆
4. 重启 adb
   - 在终端运行: adb kill-server && adb start-server
```

### 问题3: 编译错误

**原因**: SDK 版本不匹配或依赖冲突

**解决方案**:
```bash
1. 确认 SDK 已安装
   - Tools → SDK Manager
   - 安装 API 28
2. 更新 Gradle 插件
3. Invalidate Caches
   - File → Invalidate Caches / Restart
```

### 问题4: 应用闪退

**原因**: 代码错误或兼容性问题

**解决方案**:
```bash
1. 查看 Logcat 日志
   - View → Tool Windows → Logcat
2. 检查设备 Android 版本
3. 清除应用数据后重试
```

## 📱 安装后首次运行

### 1. 应用权限
本应用不需要任何特殊权限：
- ✅ 无需网络权限
- ✅ 无需位置权限
- ✅ 无需相机权限
- ✅ 无需通讯录权限

### 2. 初始化设置
首次运行时：
1. 应用会自动初始化本地数据库
2. 显示空的习惯列表
3. 可以立即开始添加习惯

### 3. 快速测试
建议进行以下测试：
```bash
1. 点击右下角 "+" 创建第一个习惯
2. 输入习惯名称（如：喝水）
3. 设置目标为 3 次
4. 点击保存
5. 在首页点击习惯卡片进行打卡
6. 切换到"习惯管理"Tab 查看列表
7. 点击习惯进入详情页查看日历
```

## 🔄 更新应用

### 从源码更新
```bash
1. 拉取最新代码
2. 在 Android Studio 中打开项目
3. Sync Gradle
4. 重新运行应用
```

### 安装新版本 APK
```bash
1. 构建新的 APK
2. 传输到设备
3. 直接安装（会覆盖旧版本）
4. 数据会自动保留
```

## 🗑️ 卸载应用

### 通过设备卸载
```bash
1. 设置 → 应用 → 习惯打卡
2. 点击"卸载"
3. 确认卸载
```

### 通过 adb 卸载
```bash
adb uninstall com.example.myapplication
```

⚠️ **注意**: 卸载后所有本地数据将被清除，无法恢复！

## 📊 版本检查

### 查看当前版本
```bash
位置: app/build.gradle
```

```gradle
defaultConfig {
    versionCode 1
    versionName "1.0"
}
```

## 🔐 签名配置（发布版本）

### 生成签名密钥
```bash
keytool -genkey -v -keystore habit-tracker.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias habit-tracker
```

### 配置签名
在 `app/build.gradle` 中添加：
```gradle
android {
    signingConfigs {
        release {
            storeFile file("habit-tracker.jks")
            storePassword "your-password"
            keyAlias "habit-tracker"
            keyPassword "your-password"
        }
    }
    buildTypes {
        release {
            signingConfig signingConfigs.release
        }
    }
}
```

### 构建发布版本
```bash
.\gradlew.bat assembleRelease
```

## 📦 APK 分发

### 方法1: 直接分享 APK
```bash
1. 构建 APK
2. 找到文件: app/build/outputs/apk/release/app-release.apk
3. 通过网盘、邮件等方式分享
4. 用户下载后直接安装
```

### 方法2: 发布到应用商店
```bash
1. 注册开发者账号
2. 准备应用图标和截图
3. 填写应用信息
4. 上传签名后的 APK
5. 提交审核
```

## 💾 数据备份

### 手动备份
```bash
1. 使用 adb 导出数据
adb backup -f backup.ab com.example.myapplication

2. 恢复数据
adb restore backup.ab
```

### 注意事项
- 数据存储在 SharedPreferences 中
- 位置: /data/data/com.example.myapplication/shared_prefs/
- 卸载应用会删除数据

## 🐛 调试模式

### 启用详细日志
```java
// 在需要调试的地方添加
Log.d("HabitTracker", "Debug message");
```

### 查看日志
```bash
adb logcat | grep HabitTracker
```

## 📞 获取帮助

如果遇到问题：
1. 查看本文档的"常见问题"部分
2. 查看 README.md 了解应用功能
3. 查看 QUICKSTART.md 学习使用方法
4. 检查 Logcat 日志寻找错误信息

## ✅ 安装检查清单

安装完成后，请确认：
- [ ] 应用能正常启动
- [ ] 可以创建习惯
- [ ] 可以打卡
- [ ] Tab 切换正常
- [ ] 图标选择正常
- [ ] 习惯详情页正常
- [ ] 删除功能正常
- [ ] 数据持久化正常（重启应用数据还在）

---

**安装完成！开始使用习惯打卡应用，养成好习惯吧！** 💪

