package com.ProjectViolet.emotionDiary.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ProjectViolet.emotionDiary.R;
import com.ProjectViolet.emotionDiary.bean.DiaryBean;
import com.ProjectViolet.emotionDiary.db.DiaryDatabaseHelper;
import com.ProjectViolet.emotionDiary.event.StartUpdateDiaryEvent;
import com.ProjectViolet.emotionDiary.utils.AppManager;
import com.ProjectViolet.emotionDiary.utils.DiaryApplication;
import com.ProjectViolet.emotionDiary.utils.GetDate;
import com.ProjectViolet.emotionDiary.utils.SpHelper;
import com.ProjectViolet.emotionDiary.utils.StatusBarCompat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.common_iv_back)
    ImageView mCommonIvBack;
    @Bind(R.id.common_tv_title)
    TextView mCommonTvTitle;
    @Bind(R.id.common_iv_test)
    ImageView mCommonIvTest;
    @Bind(R.id.common_title_ll)
    LinearLayout mCommonTitleLl;
    @Bind(R.id.main_iv_circle)
    ImageView mMainIvCircle;
    @Bind(R.id.main_tv_date)
    TextView mMainTvDate;
    @Bind(R.id.main_tv_content)
    TextView mMainTvContent;
    @Bind(R.id.item_ll_control)
    LinearLayout mItemLlControl;

    @Bind(R.id.main_rv_show_diary)
    RecyclerView mMainRvShowDiary;
    @Bind(R.id.main_fab_enter_edit)
    FloatingActionButton mMainFabEnterEdit;
    @Bind(R.id.main_rl_main)
    RelativeLayout mMainRlMain;
    @Bind(R.id.item_first)
    LinearLayout mItemFirst;
    @Bind(R.id.main_ll_main)
    LinearLayout mMainLlMain;
    private List<DiaryBean> mDiaryBeanList;

    private DiaryDatabaseHelper mHelper;

    private static String IS_WRITE = "true";

    private int mEditPosition = -1;
    public DiaryApplication app;


    /**
     * 标识今天是否已经写了日记
     */
    private boolean isWrite = false;
    private static TextView mTvTest;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        StatusBarCompat.compat(this, Color.parseColor("#161414"));
        mHelper = new DiaryDatabaseHelper(this, "Diary.db", null, 1);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        EventBus.getDefault().register(this);
        SpHelper spHelper = SpHelper.getInstance(this);
        getDiaryBeanList();
        initTitle();
        mMainRvShowDiary.setLayoutManager(new LinearLayoutManager(this));
        mMainRvShowDiary.setAdapter(new DiaryAdapter(this, mDiaryBeanList));
        mTvTest = new TextView(this);
        mTvTest.setText("hello world");

    }

    private void initTitle() {
        mMainTvDate.setText("今天，" + GetDate.getDate());
        mCommonTvTitle.setText("日记");
        mCommonIvBack.setVisibility(View.INVISIBLE);
        mCommonIvTest.setVisibility(View.INVISIBLE);

    }

    private List<DiaryBean> getDiaryBeanList() {

        mDiaryBeanList = new ArrayList<>();
        List<DiaryBean> diaryList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = mHelper.getWritableDatabase();
        //Cursor cursor = sqLiteDatabase.query("Diary", null, null, null, null, null, null);
        Intent intent = getIntent();
//        String args= intent.getExtras().get("username").toString();

        app = (DiaryApplication) getApplication(); //获得我们的应用程序MyApplication
        Cursor cursor = sqLiteDatabase.query("Diary", null, "userid=?", new String[]{app.getName()}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String dateSystem = GetDate.getDate().toString();
                if (date.equals(dateSystem)) {
                    mMainLlMain.removeView(mItemFirst);
                    break;
                }
            } while (cursor.moveToNext());
        }


        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String tag = cursor.getString(cursor.getColumnIndex("tag"));
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String analysedResult = cursor.getString(cursor.getColumnIndex("analysedResult"));

                diaryList.add(new DiaryBean(date, title, content, tag, id, analysedResult));
            } while (cursor.moveToNext());
        }
        cursor.close();

        for (int i = mDiaryBeanList.size() - 1; i >= 0; i--) {
            diaryList.add(mDiaryBeanList.get(i));
        }

        mDiaryBeanList = diaryList;
        return mDiaryBeanList;
    }

    @Subscribe
    public void startUpdateDiaryActivity(StartUpdateDiaryEvent event) {
        String title = mDiaryBeanList.get(event.getPosition()).getTitle();
        String content = mDiaryBeanList.get(event.getPosition()).getContent();
        String tag = mDiaryBeanList.get(event.getPosition()).getTag();

        UpdateDiaryActivity.startActivity(this, title, content, tag);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.main_fab_enter_edit)
    public void onClick() {
        Intent intent = getIntent();
//        String args= intent.getExtras().get("username").toString();

        app = (DiaryApplication) getApplication(); //获得我们的应用程序MyApplication

        Intent activity_change= new Intent(this,  com.ProjectViolet.emotionDiary.ui.AddDiaryActivity.class);    //切换 Activityanother至MainActivity
        Bundle bundle = new Bundle();// 创建Bundle对象
        bundle.putString("username",app.getName() );//  放入data值为int型
        activity_change.putExtras(bundle);// 将Bundle对象放入到Intent上
        startActivity(activity_change);//  开始跳转

      //  AddDiaryActivity.startActivity(this);
    }

    /***
    @OnClick({R.id.item_rl_analysis, R.id.main_fab_enter_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_fab_enter_edit:
                AddDiaryActivity.startActivity(this);
            case R.id.item_rl_analysis:
                Toast.makeText(MainActivity.this,"dianji",Toast.LENGTH_LONG).show();
                break;
            }
        }
     ****/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppManager.getAppManager().AppExit(this);
    }
}