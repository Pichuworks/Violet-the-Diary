package com.ProjectViolet.emotionDiary.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ProjectViolet.emotionDiary.R;
import com.ProjectViolet.emotionDiary.bean.User;
import com.ProjectViolet.emotionDiary.util.SqliteHelper;


public class MeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_type;
    private TextView tv_username;
    private EditText et_name;
    private RadioButton rb_man;
    private RadioButton rb_woman;
    private RadioGroup rg_sex;
    private EditText et_phone;
    private Button btn_submit;
    private int sex = 1;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        initView();
        initData();
    }

    private void initData() {
        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        username = sp.getString("username", "");
        String phone = sp.getString("phone", "");
        String name = sp.getString("name", "");
        int sex = sp.getInt("sex", 1);
        int type = sp.getInt("type", 1);
        String sType = type == 0 ? "超级管理员" : (type == 1 ? "客户" : "普通管理员");
        tv_type.setText("身份：" + sType);
        tv_username.setText("账号：" + username);
        et_name.setText(name);
        et_phone.setText(phone);
        if (sex == 1) {
            rb_man.setChecked(true);
            rb_woman.setChecked(false);
        } else {
            rb_man.setChecked(false);
            rb_woman.setChecked(true);
        }

    }

    private void initView() {
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_username = (TextView) findViewById(R.id.tv_username);
        et_name = (EditText) findViewById(R.id.et_name);
        rb_man = (RadioButton) findViewById(R.id.rb_man);
        rb_woman = (RadioButton) findViewById(R.id.rb_woman);
        rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
        et_phone = (EditText) findViewById(R.id.et_phone);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(this);
        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_man) {
                    sex = 1;
                } else {
                    sex = 2;
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                submit();
                break;
        }
    }

    private void submit() {
        String name = et_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String phone = et_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "电话不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setSex(sex);
        user.setPhone(phone);
        user.setName(name);
        SqliteHelper.updateUserByUsername(new SqliteHelper(this), user);
        Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();

        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("name", name);
        edit.putString("phone", phone);
        edit.putInt("sex", sex);
        edit.commit();

    }
}
