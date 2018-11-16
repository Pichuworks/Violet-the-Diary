package com.ProjectViolet.emotionDiary.activity;

import android.app.Application;
import android.content.SharedPreferences;

import com.ProjectViolet.emotionDiary.util.SqliteHelper;


/**
 * @Date 2018-07-06.
 */
public class MyApplication extends Application {



    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        boolean isFirst = sp.getBoolean("isFirst", true);
        if (isFirst) {
            sp.edit().putBoolean("isFirst", false).commit();
            //程序第一次启动，插入一条管理员账号，账号密码 admin
            //0:超级管理员 1：客户 2：普通管理员
            SqliteHelper.saveUser(new SqliteHelper(this), "admin", "admin", 0);

        }
    }


}
