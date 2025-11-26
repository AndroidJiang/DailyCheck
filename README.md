# 习惯打卡 Android 应用

一个简单实用的习惯养成打卡应用，帮助您培养良好习惯。

## ✨ 功能特点

### 核心功能
- ✅ **无需登录** - 所有数据保存在本地，无需网络连接
- 📱 **双Tab设计** - 今日打卡 + 习惯管理
- 🎯 **灵活目标** - 自定义每日完成次数（1-99次）
- 🎨 **个性图标** - 12种图标可选，个性化您的习惯
- 📅 **打卡日历** - 查看最近30天的打卡记录
- 🔄 **自动重置** - 每天第二次打开应用自动重置计数

### 界面特色
- 🌈 **现代化UI** - Material Design风格
- 📊 **直观展示** - 一行两列卡片布局
- 🎉 **完成反馈** - 达成目标时的祝贺提示和视觉反馈
- 📈 **进度可视化** - 实时显示完成度（如 2/3）

## 📱 使用说明

### 第一个Tab：今日打卡
1. **查看习惯列表** - 以卡片形式展示所有习惯，显示图标、名称和完成进度
2. **打卡操作** - 点击习惯卡片，弹出确认对话框，确认后计数+1
3. **完成提示** - 完成当日目标后，卡片背景变为绿色，再次点击显示祝贺信息
4. **编辑习惯** - 长按习惯卡片可以编辑
5. **添加习惯** - 点击右下角"+"按钮

### 第二个Tab：习惯管理
1. **查看所有习惯** - 列表形式展示，显示目标次数和累计打卡天数
2. **查看详情** - 点击任意习惯进入详情页
3. **打卡日历** - 以日历形式查看最近30天的打卡记录
   - 🟢 绿色：已打卡
   - 🟡 黄色：今天
   - ⚪ 灰色：未打卡
4. **删除习惯** - 在详情页点击"删除习惯"按钮

### 添加/编辑习惯
1. **选择图标** - 点击图标区域，从12个预设图标中选择
2. **填写名称** - 输入习惯名称（如：喝水、运动、阅读）
3. **设置目标** - 使用 ➖ ➕ 按钮调整每日目标次数
4. **保存** - 点击"创建习惯"或"保存"按钮

## 🔧 技术实现

### 架构
- **数据存储**：SharedPreferences + Gson
- **UI框架**：Material Design Components
- **布局**：RecyclerView + GridLayoutManager/LinearLayoutManager
- **导航**：TabLayout + ViewPager

### 数据结构
```java
Habit {
    long id;                    // 唯一标识
    String title;               // 习惯名称
    int targetCount;            // 每日目标次数
    int currentCount;           // 当前完成次数
    int iconResId;              // 图标资源ID
    List<String> checkInDates;  // 打卡日期列表
}
```

### 主要类
- `MainActivity` - 主界面，TabLayout导航
- `HomeFragment` - 今日打卡页面
- `HabitManageFragment` - 习惯管理页面
- `AddHabitActivity` - 添加/编辑习惯
- `HabitDetailActivity` - 习惯详情和日历
- `SPUtils` - 数据持久化工具
- `DateUtils` - 日期处理工具

## 📦 依赖项

```gradle
implementation 'androidx.appcompat:appcompat:1.1.0'
implementation 'com.google.android.material:material:1.1.0'
implementation 'androidx.recyclerview:recyclerview:1.1.0'
implementation 'androidx.cardview:cardview:1.0.0'
implementation 'androidx.coordinatorlayout:coordinatorlayout:1.1.0'
implementation 'com.google.code.gson:gson:2.8.6'
```

## 🚀 构建和运行

1. 使用 Android Studio 打开项目
2. 等待 Gradle 同步完成
3. 连接 Android 设备或启动模拟器
4. 点击 Run 按钮

### 系统要求
- minSdkVersion: 21 (Android 5.0+)
- targetSdkVersion: 28 (Android 9.0)
- compileSdkVersion: 28

## 📝 数据说明

### 本地存储
所有数据保存在 SharedPreferences 中：
- **habits** - 习惯列表（JSON格式）
- **last_reset_date** - 最后重置日期

### 数据重置机制
- 每次启动应用时检查日期
- 如果不是今天，将所有习惯的 currentCount 重置为 0
- checkInDates 历史记录保持不变

## 🎯 未来计划

- [ ] 添加更多图标选择
- [ ] 支持自定义颜色主题
- [ ] 添加统计图表功能
- [ ] 支持习惯排序和分类
- [ ] 添加提醒通知功能
- [ ] 数据导出/导入功能

## 📄 许可证

本项目仅供学习和个人使用。

---

**开发者**: 习惯打卡团队
**版本**: 1.0
**更新日期**: 2025-11-26

