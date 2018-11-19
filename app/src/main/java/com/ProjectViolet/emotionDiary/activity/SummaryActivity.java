package com.ProjectViolet.emotionDiary.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.app.AppCompatActivity;

import com.ProjectViolet.emotionDiary.R;
import com.ProjectViolet.emotionDiary.adapter.SummaryAdapter;
import com.ProjectViolet.emotionDiary.bean.User;
import com.ProjectViolet.emotionDiary.db.DiaryDatabaseHelper;
import com.ProjectViolet.emotionDiary.util.SqliteHelper;
import com.ProjectViolet.emotionDiary.utils.DiaryApplication;
import com.ProjectViolet.emotionDiary.utils.GetDate;
import com.ProjectViolet.emotionDiary.utils.UserDiaryStatus;
import com.ProjectViolet.emotionDiary.utils.UserSummary;
import com.vdurmont.emoji.EmojiParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class SummaryActivity extends AppCompatActivity {
    private List<UserSummary> userSummaries = new ArrayList<UserSummary>();
    private List<UserDiaryStatus> userDiaryStatusList = new ArrayList<UserDiaryStatus>();
    private DiaryDatabaseHelper mHelper;
    private DiaryApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_summary);
        initSummary();
        initUserSummary();
        SummaryAdapter adapter = new SummaryAdapter(SummaryActivity.this, R.layout.item_listview_summary, userSummaries);
        ListView listView = (ListView) findViewById(R.id.summary_list);
        listView.setAdapter(adapter);
    }

    private void initSummary() {
        mHelper = new DiaryDatabaseHelper(this, "Diary.db", null, 1);
        app = (DiaryApplication) getApplication(); //获得我们的应用程序MyApplication

        SQLiteDatabase sqLiteDatabase = mHelper.getWritableDatabase();
        //Cursor cursor = sqLiteDatabase.query("Diary", new String[] {"userid", "analysedResult"}, null, new String[]{app.getName()}, null, null, null);
        Cursor cursor = sqLiteDatabase.rawQuery("select * from Diary", null);
        if (cursor.moveToFirst()) {
            do {
                int findFlag = 0;
                int tmpUser = 0;
                ArrayList<String> nowEmoji = new ArrayList<String>();
                String tmpName = cursor.getString(cursor.getColumnIndex("userid"));
                String tmpEmoji = cursor.getString(cursor.getColumnIndex("analysedResult"));
                String tmpContent = cursor.getString(cursor.getColumnIndex("content"));

                Log.d("fuckyou", "name: " + tmpName + " emoji: " + tmpEmoji + " context: " + tmpContent);
                char[] debugfuck = tmpEmoji.toCharArray();
                /*
                for(int i=0; i<debugfuck.length; i++) {
                    Log.d("fuckyou--charray", String.valueOf(debugfuck[i]));
                }
                */


                for(int i=0; i<userDiaryStatusList.size(); i++) {
                    findFlag = 0;
                    if(userDiaryStatusList.get(i).getUserID().equals(tmpName)) {
                        findFlag = 1;
                        tmpUser = i;
                        nowEmoji = userDiaryStatusList.get(i).getUserDiaryStatus();
                        break;
                    }
                }
                if(findFlag == 1) {
                    nowEmoji.add(tmpEmoji);
                    UserDiaryStatus userDiaryStatus = new UserDiaryStatus(tmpName, nowEmoji);
                    userDiaryStatusList.set(tmpUser, userDiaryStatus);

                    Log.d("fuckyou", "set: " + tmpName);
                }
                else {
                    nowEmoji.add(tmpEmoji);
                    UserDiaryStatus userDiaryStatus = new UserDiaryStatus(tmpName, nowEmoji);
                    userDiaryStatusList.add(userDiaryStatus);

                    Log.d("fuckyou", "add: " + tmpName);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void initUserSummary() {

        /*
        // Debug Start
        for(int i=0; i<userDiaryStatusList.size(); i++) {
            UserSummary neko = new UserSummary(userDiaryStatusList.get(i).getUserID(), userDiaryStatusList.get(i).getUserID(), "情感趋势：" + userDiaryStatusList.get(i).getUserDiaryStatus().get(0));
            userSummaries.add(neko);
            String fuckyou = userDiaryStatusList.get(i).getUserDiaryStatus().get(0);
            Log.d("fuckyou", neko.getUserID() + " " + fuckyou);
        }
        // Debug End
        */


        for(int i=0; i<userDiaryStatusList.size(); i++) {
            // 获取每个人的Emoji列表
            Log.d("fuckyou", "-----");
            ArrayList<String> emojiArray = new ArrayList<>();

            for(int j=0; j<userDiaryStatusList.get(i).getUserDiaryStatus().size(); j++) {
                String rawEmoji = userDiaryStatusList.get(i).getUserDiaryStatus().get(j);
                String emojiResult = EmojiParser.parseToAliases(rawEmoji);
                String[] emojiList = emojiResult.split(" ");
                // Log.d("fuckyou--emoji", userDiaryStatusList.get(i).getUserID());
                for(int k=0; k<emojiList.length; k++) {
                    emojiArray.add(emojiList[k]);
                }
            }

            String calcEmoji = new String();
            Random rand = new Random();
            int loopArgc = 10;

            if(emojiArray.size() < loopArgc) loopArgc = emojiArray.size();

            for(int j=0; j<loopArgc; j++) {
                int randEmoji = rand.nextInt(emojiArray.size());
                calcEmoji = calcEmoji + " " + emojiArray.get(randEmoji);
            }

            calcEmoji = EmojiParser.parseToUnicode(calcEmoji);
            UserSummary neko = new UserSummary(userDiaryStatusList.get(i).getUserID(), userDiaryStatusList.get(i).getUserID(), "当前情感趋势：" + calcEmoji);
            userSummaries.add(neko);

        }

    }
}
