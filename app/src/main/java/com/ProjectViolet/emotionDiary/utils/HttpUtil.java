package com.ProjectViolet.emotionDiary.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
    public static void sendHttpRequest(final String address,final HttpCallback listener) {
        new Thread(new Runnable() {//开启子线程
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");//设置请求方式
                    connection.setConnectTimeout(8000);//设置链接超时时间
                    connection.setReadTimeout(8000);//设置读取超时时间
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new
                            InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    listener.onFinish(response.toString());//接口回掉
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError(e);//接口回掉
                } finally {
                    if (connection != null) {
                        connection.disconnect();//关闭连接
                    }
                }
            }
        }).start();

    }
}
