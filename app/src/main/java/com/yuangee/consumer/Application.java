package com.yuangee.consumer;

import android.support.multidex.MultiDexApplication;

import com.baidu.mapapi.SDKInitializer;
import com.tencent.bugly.crashreport.CrashReport;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by developerLzh on 2017/4/19.
 */

public class Application extends MultiDexApplication {

    private static Application context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        initBugly();
        initBaidu();
        initJpush();
    }

    /**
     * bugly初始化
     */
    private void initBugly() {
        CrashReport.initCrashReport(getApplicationContext(), Config.BUGLY_ID, false);
    }

    /**
     * 百度地图初始化
     */
    private void initBaidu() {
        SDKInitializer.initialize(getApplicationContext());
    }

    /**
     * 初始化JPUSH
     */
    private void initJpush(){
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

}
