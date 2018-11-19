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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.lang.Thread.sleep;

/**
 * Created by 李 on 2017/1/26.
 */
public class UpdateDiaryActivity extends AppCompatActivity {

    @Bind(R.id.update_diary_tv_date)
    TextView mUpdateDiaryTvDate;
    @Bind(R.id.update_diary_et_title)
    EditText mUpdateDiaryEtTitle;
    @Bind(R.id.update_diary_et_content)
    LinedEditText mUpdateDiaryEtContent;
    @Bind(R.id.update_diary_fab_back)
    FloatingActionButton mUpdateDiaryFabBack;
    @Bind(R.id.update_diary_fab_add)
    FloatingActionButton mUpdateDiaryFabAdd;
    @Bind(R.id.update_diary_fab_delete)
    FloatingActionButton mUpdateDiaryFabDelete;
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
    @Bind(R.id.update_diary_tv_tag)
    TextView mTvTag;


    private DiaryDatabaseHelper mHelper;
    private DiaryApplication app;
    private String baseURL = "http://47.100.0.222:2000/emotion/get?text=";
    private String tab_result = "";

    public static void startActivity(Context context, String title, String content, String tag) {
        Intent intent = new Intent(context, UpdateDiaryActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("tag", tag);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_diary);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        mHelper = new DiaryDatabaseHelper(this, "Diary.db", null, 1);
        initTitle();
        StatusBarCompat.compat(this, Color.parseColor("#161414"));

        Intent intent = getIntent();
        mUpdateDiaryTvDate.setText("今天，" + GetDate.getDate());
        mUpdateDiaryEtTitle.setText(intent.getStringExtra("title"));
        mUpdateDiaryEtContent.setText(intent.getStringExtra("content"));
        mTvTag.setText(intent.getStringExtra("tag"));
    }

    private void initTitle() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        mCommonTvTitle.setText("修改日记");
    }

    @OnClick({R.id.common_iv_back, R.id.update_diary_tv_date, R.id.update_diary_et_title, R.id.update_diary_et_content, R.id.update_diary_fab_back, R.id.update_diary_fab_add, R.id.update_diary_fab_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_iv_back:
                app = (DiaryApplication) getApplication(); //获得我们的应用程序MyApplication
                Intent activity_change4= new Intent(this, com.ProjectViolet.emotionDiary.ui.MainActivity.class);    //切换 Activityanother至MainActivity
                Bundle bundle4 = new Bundle();// 创建Bundle对象
                bundle4.putString("username", app.getName());//  放入data值为int型
                activity_change4.putExtras(bundle4);// 将Bundle对象放入到Intent上
                startActivity(activity_change4);//  开始跳转
               // MainActivity.startActivity(this);
            case R.id.update_diary_tv_date:
                break;
            case R.id.update_diary_et_title:
                break;
            case R.id.update_diary_et_content:
                break;
            case R.id.update_diary_fab_back:
                android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("确定要删除该日记吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

//                      String title = mUpdateDiaryEtTitle.getText().toString();
                        String title = mUpdateDiaryEtTitle.getText().toString();
                        String content = mUpdateDiaryEtContent.getText().toString();
                        String tag = mTvTag.getText().toString();

                        SQLiteDatabase dbDelete = mHelper.getWritableDatabase();
                        dbDelete.delete("Diary", "content=?", new String[]{content});

                        app = (DiaryApplication) getApplication(); //获得我们的应用程序MyApplication

                        Intent activity_change4= new Intent(UpdateDiaryActivity.this, com.ProjectViolet.emotionDiary.ui.MainActivity.class);    //切换 Activityanother至MainActivity
                        Bundle bundle4 = new Bundle();// 创建Bundle对象
                        bundle4.putString("username",app.getName() );//  放入data值为int型
                        activity_change4.putExtras(bundle4);// 将Bundle对象放入到Intent上
                        startActivity(activity_change4);//  开始跳转


                        //MainActivity.startActivity(UpdateDiaryActivity.this);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
                break;
            case R.id.update_diary_fab_add:
                SQLiteDatabase dbUpdate = mHelper.getWritableDatabase();
                ContentValues valuesUpdate = new ContentValues();
                String title = mUpdateDiaryEtTitle.getText().toString();
                String content = mUpdateDiaryEtContent.getText().toString();

                valuesUpdate.put("title", title);
                valuesUpdate.put("content", content);

                // 更新emoji
                // 在这里更新数据
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
                valuesUpdate.put("analysedResult", tab_result);

                dbUpdate.update("Diary", valuesUpdate, "title = ?", new String[]{title});
                dbUpdate.update("Diary", valuesUpdate, "content = ?", new String[]{content});
                dbUpdate.update("Diary", valuesUpdate, "analysedResult = ?", new String[]{tab_result});

                MainActivity.startActivity(this);
                break;
            case R.id.update_diary_fab_delete:

                MainActivity.startActivity(this);

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

    @OnClick(R.id.common_tv_title)
    public void onClick() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.startActivity(this);
    }
}