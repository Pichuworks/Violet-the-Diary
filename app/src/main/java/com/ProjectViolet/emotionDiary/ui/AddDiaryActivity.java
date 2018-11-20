package com.ProjectViolet.emotionDiary.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ProjectViolet.emotionDiary.R;
import com.ProjectViolet.emotionDiary.db.DiaryDatabaseHelper;
import com.ProjectViolet.emotionDiary.utils.AppManager;
import com.ProjectViolet.emotionDiary.utils.DiaryApplication;
import com.ProjectViolet.emotionDiary.utils.GetDate;
import com.ProjectViolet.emotionDiary.utils.StatusBarCompat;
import com.ProjectViolet.emotionDiary.widget.LinedEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.trity.floatingactionbutton.FloatingActionButton;
import cc.trity.floatingactionbutton.FloatingActionsMenu;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ProjectViolet.emotionDiary.R;
import com.ProjectViolet.emotionDiary.bean.DiaryBean;
import com.ProjectViolet.emotionDiary.event.StartUpdateDiaryEvent;
import com.ProjectViolet.emotionDiary.utils.DiaryApplication;
import com.ProjectViolet.emotionDiary.utils.GetDate;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Text;

import android.os.Bundle;
import android.util.Log;

import static java.lang.Thread.sleep;

import static java.lang.Thread.sleep;

public class AddDiaryActivity extends AppCompatActivity {

    @Bind(R.id.add_diary_tv_date)
    TextView mAddDiaryTvDate;
    @Bind(R.id.add_diary_et_title)
    EditText mAddDiaryEtTitle;
    @Bind(R.id.add_diary_et_content)
    LinedEditText mAddDiaryEtContent;
    @Bind(R.id.add_diary_fab_back)
    FloatingActionButton mAddDiaryFabBack;
    @Bind(R.id.add_diary_fab_add)
    FloatingActionButton mAddDiaryFabAdd;

    @Bind(R.id.right_labels)
    FloatingActionsMenu mRightLabels;
    @Bind(R.id.common_tv_title)
    TextView mCommonTvTitle;
    @Bind(R.id.common_title_ll)
    LinearLayout mCommonTitleLl;
    @Bind(R.id.common_iv_back)
    ImageView mCommonIvBack;
    @Bind(R.id.common_iv_test)
    ImageView mCommonIvTest;

    private DiaryDatabaseHelper mHelper;
    public DiaryApplication app;
    private String baseURL = "http://47.100.0.222:2000/emotion/get?text=";
    private String tab_result = "";

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AddDiaryActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String title, String content) {
        Intent intent = new Intent(context, AddDiaryActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        mAddDiaryEtTitle.setText(intent.getStringExtra("title"));
        StatusBarCompat.compat(this, Color.parseColor("#161414"));

        app = (DiaryApplication) getApplication();

        mCommonTvTitle.setText("添加日记");
        mAddDiaryTvDate.setText("今天，" + GetDate.getDate());
        mAddDiaryEtContent.setText(intent.getStringExtra("content"));
        mHelper = new DiaryDatabaseHelper(this, "Diary.db", null, 1);
    }


    @OnClick({R.id.common_iv_back, R.id.add_diary_et_title, R.id.add_diary_et_content, R.id.add_diary_fab_back, R.id.add_diary_fab_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_iv_back:
                Intent intent = getIntent();
                String args= intent.getExtras().get("username").toString();
                Intent activity_change= new Intent(this,  com.ProjectViolet.emotionDiary.ui.MainActivity.class);    //切换 Activityanother至MainActivity
                Bundle bundle = new Bundle();// 创建Bundle对象
                bundle.putString("username",args );//  放入data值为int型
                app.setName(args);
                activity_change.putExtras(bundle);// 将Bundle对象放入到Intent上
                Log.d("neko","neko1");
                startActivity(activity_change);//  开始跳转
                finish();


                //MainActivity.startActivity(this);
            case R.id.add_diary_et_title:
                break;
            case R.id.add_diary_et_content:
                break;
            case R.id.add_diary_fab_back:
                String date = GetDate.getDate().toString();
                String tag = String.valueOf(System.currentTimeMillis());
                String title = mAddDiaryEtTitle.getText().toString() + "";
                String content = mAddDiaryEtContent.getText().toString() + "";
                if (!title.equals("") || !content.equals("")) {
                    SQLiteDatabase db = mHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();

                    Intent intent2 = getIntent();
                    String args2= intent2.getExtras().get("username").toString();
//                    int a = Integer.parseInt(args2);
                    String a = args2;
                    app = (DiaryApplication) getApplication(); //获得我们的应用程序MyApplication
                    values.put("userid",app.getName());

                    // String args= intent.getExtras().get("username").toString();
                    // int a = Integer.parseInt(args); // username

                    String url = baseURL + title + "%20" + content;
                    url = url.replaceAll(" ", "%20");
                    url = url.replaceAll("\\n", "%20");
                    final HttpGet httpGet = new HttpGet(url);
                    final HttpClient httpClient = new DefaultHttpClient();

                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            // 发送请求
                            try
                            {
                                HttpResponse response = httpClient.execute(httpGet);
                                tab_result = showResponseResult(response);   // 显示返回结果
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    // 睡眠同步 又不是不能用
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    values.put("date", date);
                    values.put("title", title);
                    values.put("content", content);
                    values.put("tag", tag);
                    values.put("analysedResult", tab_result);

                    Log.d("fuckyou--AddDiary", "content: " + content + " tab_result: " + tab_result);

                    db.insert("Diary", null, values);
                    values.clear();
                }
                Intent intent3 = getIntent();
                String args3= intent3.getExtras().get("username").toString();
                Intent activity_change3= new Intent(this,  com.ProjectViolet.emotionDiary.ui.MainActivity.class);    //切换 Activityanother至MainActivity
                Bundle bundle3 = new Bundle();// 创建Bundle对象
                bundle3.putString("username",args3 );//  放入data值为int型
                app.setName(args3);
                activity_change3.putExtras(bundle3);// 将Bundle对象放入到Intent上
                Log.d("neko","neko3");
                startActivity(activity_change3);//  开始跳转
                finish();

               // MainActivity.startActivity(this);
                break;
            case R.id.add_diary_fab_add:
                final String dateBack = GetDate.getDate().toString();
                final String titleBack = mAddDiaryEtTitle.getText().toString();
                final String contentBack = mAddDiaryEtContent.getText().toString();
                if(!titleBack.isEmpty() || !contentBack.isEmpty()){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("是否保存日记内容？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = getIntent();
                            String args= intent.getExtras().get("username").toString();
                            // int a = Integer.parseInt(args); // username

                            String url = baseURL + titleBack + "%20" + contentBack;
                            url = url.replaceAll(" ", "%20");
                            url = url.replaceAll("\\n", "%20");
                            final HttpGet httpGet = new HttpGet(url);
                            final HttpClient httpClient = new DefaultHttpClient();

                            new Thread(new Runnable(){
                                @Override
                                public void run() {
                                    // 发送请求
                                    try
                                    {
                                        HttpResponse response = httpClient.execute(httpGet);
                                        tab_result = showResponseResult(response);   // 显示返回结果
                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();

                            // 睡眠同步 又不是不能用
                            try {
                                sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                            SQLiteDatabase db = mHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("date", dateBack);
                            values.put("title", titleBack);
                            values.put("content", contentBack);
                            // values.put("userid",a);
                            values.put("userid", args);
                            values.put("analysedResult", tab_result);
                            Log.d("fuckyou--AddDiary2", "content: " + contentBack + " tab_result: " + tab_result);
                            db.insert("Diary", null, values);
                            values.clear();

                            Intent intent4 = getIntent();
                            String args4= intent4.getExtras().get("username").toString();
                            Intent activity_change4= new Intent(AddDiaryActivity.this,  com.ProjectViolet.emotionDiary.ui.MainActivity.class);    //切换 Activityanother至MainActivity
                            Bundle bundle4 = new Bundle();// 创建Bundle对象
                            bundle4.putString("username",args4 );//  放入data值为int型
                            activity_change4.putExtras(bundle4);// 将Bundle对象放入到Intent上
                            Log.d("neko","neko4");
                            startActivity(activity_change4);//  开始跳转

                           // MainActivity.startActivity(AddDiaryActivity.this);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent4 = getIntent();
                            String args4= intent4.getExtras().get("username").toString();
                            Intent activity_change4= new Intent(AddDiaryActivity.this,  com.ProjectViolet.emotionDiary.ui.MainActivity.class);    //切换 Activityanother至MainActivity
                            Bundle bundle4 = new Bundle();// 创建Bundle对象
                            bundle4.putString("username",args4 );//  放入data值为int型
                            app.setName(args4);
                            activity_change4.putExtras(bundle4);// 将Bundle对象放入到Intent上
                            Log.d("neko","neko5");
                            startActivity(activity_change4);//  开始跳转
                            finish();
                          //  MainActivity.startActivity(AddDiaryActivity.this);
                        }
                    }).show();
                }else{
                    Intent intent4 = getIntent();
                    String args4= intent4.getExtras().get("username").toString();
                    Intent activity_change4= new Intent(this,  com.ProjectViolet.emotionDiary.ui.MainActivity.class);    //切换 Activityanother至MainActivity
                    Bundle bundle4 = new Bundle();// 创建Bundle对象
                    bundle4.putString("username",args4 );//  放入data值为int型
                    app.setName(args4);
                    activity_change4.putExtras(bundle4);// 将Bundle对象放入到Intent上
                    Log.d("neko","neko6");
                    startActivity(activity_change4);//  开始跳转
                    finish();
                    //MainActivity.startActivity(this);
                }
                break;
        }
    }

    private String showResponseResult(HttpResponse response)
    {
        if (null == response)
        {
            return "";
        }

        HttpEntity httpEntity = response.getEntity();
        try
        {
            InputStream inputStream = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            String result = "";
            String line = "";
            while (null != (line = reader.readLine()))
            {
                result += line;
            }
            System.out.println(result);
            Log.d("Analysis Result",result);
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return tab_result;
    }

    @Override
    public void onBackPressed() {
        app = (DiaryApplication) getApplication();
        super.onBackPressed();
        Log.d("nekoneko", "nekoneko");
        Intent intent4 = getIntent();
        String args4= intent4.getExtras().get("username").toString();
        Intent activity_change4= new Intent(this,  com.ProjectViolet.emotionDiary.ui.MainActivity.class);    //切换 Activityanother至MainActivity
        Bundle bundle4 = new Bundle();// 创建Bundle对象
        bundle4.putString("username",args4 );//  放入data值为int型
        app.setName(args4);
        activity_change4.putExtras(bundle4);// 将Bundle对象放入到Intent上
        Log.d("neko","neko7");
        startActivity(activity_change4);//  开始跳转
        finish();
        //MainActivity.startActivity(this);
    }
}











