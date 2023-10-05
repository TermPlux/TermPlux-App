package io.ecosed.droid;

interface EcosedDroid {
    String getFrameworkVersion(); // 获取版本
    String getShizukuVersion(); // 获取Shizuku版本
    String getChineseCale(); // 获取农历
    String getOnePoem(); // 获取随机心灵鸡汤
    boolean isWatch(); // 判断是否是手表
    boolean isUseDynamicColors(); // 是否使用动态颜色
    void openDesktopSettings(); // 打开任务栏设置
    void openEcosedSettings(); // 打开框架设置
}