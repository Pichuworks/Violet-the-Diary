package com.ProjectViolet.emotionDiary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DiaryDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_USER = "create table DiaryUser("
            + "id integer primary key autoincrement, "
            + "username text, "
            + "password text, "
            + "regTime text,"
            + "privilege integer)";  // priv == 0 -> user, priv == 1 -> admin

    public static final String CREATE_DIARY = "create table Diary("
            + "id integer primary key autoincrement, "
            + "date text, "
            + "title text, "
            + "tag text, "
            + "userid text, "
            + "analysedFlag integer, "
            + "analysedResult text, "
            + "content text)";

    public static final String CREATE_TEXT_SENTIMENT = "create table DiaryTextSentiment("
            + "id integer primary key autoincrement, "
            + "userID integer, "
            + "diaryID integer, "
            + "resultArray text, "
            + "updateTime text)";

    public static final String CREATE_SENTIMENT_ANALYSIS = "create table DiarySentimentAnalysis("
            + "id integer primary key autoincrement, "
            + "userID integer, "
            + "featureVector text, "
            + "classifier text)";

    private Context mContext;
    public DiaryDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_DIARY);
        db.execSQL(CREATE_TEXT_SENTIMENT);
        db.execSQL(CREATE_SENTIMENT_ANALYSIS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Diary");
        onCreate(db);
    }
}
