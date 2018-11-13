package com.ProjectViolet.emotionDiary.utils;

public interface HttpCallback {
    //获取成功调用
    void onFinish(String response);
    //获取失败调用
    void onError(Exception e);
}
