package com.ProjectViolet.emotionDiary.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ProjectViolet.emotionDiary.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @Date 2018-07-05.
 * 数据库工具类
 */
public class SqliteHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "my_db";
    private final static int DB_VERSION = 1;


    public SqliteHelper(Context context) {
        this(context, DB_NAME, null, DB_VERSION);
    }

    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //客户表
        String sql_user = "create table tb_user(" +
                "id  INTEGER PRIMARY KEY AUTOINCREMENT,username CHAR(50)," +
                "password CHAR(50)," +
                "name CHAR(20)," +
                "phone CHAR(11)," +
                "sex  INT DEFAULT 1," +
                "type INT DEFAULT 1 )";
        db.execSQL(sql_user);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //用户表
        db.execSQL("DROP TABLE IF EXISTS tb_user");
        //客户表
        String sql_user = "create table tb_user(" +
                "id  INTEGER PRIMARY KEY AUTOINCREMENT,username CHAR(50)," +
                "password CHAR(50)," +
                "name CHAR(20)," +
                "phone CHAR(11)," +
                "sex  INT DEFAULT 1," +
                "type INT DEFAULT 1 )";
        db.execSQL(sql_user);


    }

    /**
     * 插入用户信息
     *
     * @param helper
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public static int saveUser(SqliteHelper helper, String username, String password, int type) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from tb_user where username = '" + username + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            //账号已注册
            return 1;
        }
        db.beginTransaction();
        sql = "insert into tb_user(username,password,type) values('" + username + "','" + password + "'," + type + ")";
        db.execSQL(sql);
        db.setTransactionSuccessful();
        db.endTransaction();
        return 0;
    }


    /**
     * 登录
     *
     * @param helper
     * @param username
     * @param password
     * @return 是否成功
     */
    public static User login(SqliteHelper helper, String username, String password) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from tb_user where username = '" + username + "' and password = '" + password + "'";
        Cursor cursor = db.rawQuery(sql, null);
        boolean result = cursor.moveToFirst();
        if (result) {
            User user = new User();
            user.setId(cursor.getInt(0));
            user.setUsername(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            user.setName(cursor.getString(3));
            user.setPhone(cursor.getString(4));
            user.setSex(cursor.getInt(5));
            user.setType(cursor.getInt(6));
            cursor.close();
            return user;
        } else {
            cursor.close();
            return null;
        }


    }


    /**
     * 查询客户列表
     *
     * @param msh
     * @return
     */
    public static List<User> getUsers(SqliteHelper msh, int id) {

        List<User> list = new ArrayList<>();
        SQLiteDatabase db = msh.getWritableDatabase();
        String sql = "select * from tb_user where type !=0 and id != " + id + " order by id desc";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(0));
                user.setUsername(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                user.setName(cursor.getString(3));
                user.setPhone(cursor.getString(4));
                user.setSex(cursor.getInt(5));
                user.setType(cursor.getInt(6));
                list.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }


    /*
     * 根据id删除
     */
    public static void deleteUserbyId(SqliteHelper helper, int userId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        db.execSQL("Delete from tb_user where id=" + userId);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 根据用户名修改
     *
     * @param msh
     * @param user
     */
    public static void updateUserByUsername(SqliteHelper msh, User user) {


        String sql = "update tb_user set name = '" + user.getName() + "',sex=" + user.getSex() + ",phone='" + user.getPhone() + "' where username = '" + user.getUsername() + "'";
        SQLiteDatabase db = msh.getWritableDatabase();
        db.beginTransaction();

        db.execSQL(sql);
        db.setTransactionSuccessful();
        db.endTransaction();
    }


    /**
     * 修改用户身份
     *
     * @param msh
     * @param type 1:客户  2：普通管理员
     */
    public static void updateUserTypeById(SqliteHelper msh, int id, int type) {


        String sql = "update tb_user set type = " + type + " where id=" + id;
        SQLiteDatabase db = msh.getWritableDatabase();
        db.beginTransaction();

        db.execSQL(sql);
        db.setTransactionSuccessful();
        db.endTransaction();
    }
}
