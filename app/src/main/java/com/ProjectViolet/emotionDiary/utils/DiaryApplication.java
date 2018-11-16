package com.ProjectViolet.emotionDiary.utils;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by developerHaoz on 2017/5/9.
 */

public class DiaryApplication extends Application {

    private String name;
    private int diaryid;

    @Override
    public void onCreate() {
        super.onCreate();
        setName(NAME); //初始化全局变量
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getdiaryid() {
        return diaryid;
    }
    public void setdiaryid(int diaryid) {
        this.diaryid = diaryid;
    }

    private static final String NAME = "admin";

}
