package com.ProjectViolet.emotionDiary.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ProjectViolet.emotionDiary.R;
import com.ProjectViolet.emotionDiary.bean.User;
import com.ProjectViolet.emotionDiary.util.SqliteHelper;
import com.ProjectViolet.emotionDiary.utils.DiaryApplication;


/**
 * @time 2018-07-05 10:51
 * @类描述：
 * @变更记录:
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private Button btn_register;
    public DiaryApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();

        /******/
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        boolean isFirst = sp.getBoolean("isFirst", true);
        if (isFirst) {
            sp.edit().putBoolean("isFirst", false).commit();
            //程序第一次启动，插入一条管理员账号，账号密码 admin
            //0:超级管理员 1：客户 2：普通管理员
            SqliteHelper.saveUser(new SqliteHelper(this), "admin", "admin", 0);
        }

        /********/
    }

    private void initData() {
        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        String username = sp.getString("username", "");
        String password = sp.getString("password", "");
        et_username.setText(username);
        et_password.setText(password);


        app = (DiaryApplication) getApplication(); //获得我们的应用程序MyApplication
        Log.e("MyFirstActivityOriginal", app.getName());   //将我们放到进程中的全局变量拿出来，看是不是我们曾经设置的值
        app.setName(username);  //OK，现在我们开始修改了
    }

    private void initView() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);

        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                submit();
                break;
            case R.id.btn_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }
    }

    private void submit() {
        String username = et_username.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "账号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = SqliteHelper.login(new SqliteHelper(this), username, password);

        if (user != null) {
            SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            edit.putInt("id", user.getId());
            edit.putString("username", username);
            edit.putString("password", password);
            edit.putString("name", user.getName());
            edit.putString("phone", user.getPhone());
            edit.putInt("sex", user.getSex());
            edit.putInt("type", user.getType());
            edit.commit();
            /*****/
            if(user.getType()==1)
            {
                //startActivity(new Intent(this, com.ProjectViolet.emotionDiary.ui.MainActivity.class));
                app.setName(username);  //OK，现在我们开始修改了
                Intent activity_change= new Intent(this,  com.ProjectViolet.emotionDiary.ui.MainActivity.class);    //切换 Activityanother至MainActivity
                Bundle bundle = new Bundle();// 创建Bundle对象
                bundle.putString("username",username );
                activity_change.putExtras(bundle);// 将Bundle对象放入到Intent上
                startActivity(activity_change);//  开始跳转
                finish();


            }
            else if(user.getType()==0)
                startActivity(new Intent(this, com.ProjectViolet.emotionDiary.activity.MainActivity.class));
                finish();
            /*****/
        } else {
            Toast.makeText(this, "账号或密码错误", Toast.LENGTH_SHORT).show();
        }

    }
}
