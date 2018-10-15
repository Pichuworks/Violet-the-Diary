package com.ProjectViolet.emotionDiary.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ProjectViolet.emotionDiary.R;
import com.ProjectViolet.emotionDiary.bean.User;
import com.ProjectViolet.emotionDiary.util.SqliteHelper;


public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_username;
    private EditText et_name;
    private AppCompatRadioButton rb_man;
    private AppCompatRadioButton rb_woman;
    private EditText et_phone;
    private Button btn_submit;
    private String userName;
    private RadioGroup rg_sex;
    private int sex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        initView();
        initData();
    }

    private void initData() {
        User user = (User) getIntent().getSerializableExtra("user");
        if (user != null) {
            userName = user.getUsername();
            tv_username.setText("账号：" + user.getUsername());
            et_name.setText(user.getName());
            et_phone.setText(user.getPhone());
            if (user.getSex() == 1) {
                rb_man.setChecked(true);
                rb_woman.setChecked(false);
            } else {
                rb_man.setChecked(false);
                rb_woman.setChecked(true);
            }
        }
    }

    private void initView() {
        tv_username = (TextView) findViewById(R.id.tv_username);
        et_name = (EditText) findViewById(R.id.et_name);
        rb_man = (AppCompatRadioButton) findViewById(R.id.rb_man);
        rb_woman = (AppCompatRadioButton) findViewById(R.id.rb_woman);
        et_phone = (EditText) findViewById(R.id.et_phone);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(this);
        rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
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
        user.setUsername(userName);
        user.setSex(sex);
        user.setPhone(phone);
        user.setName(name);
        SqliteHelper.updateUserByUsername(new SqliteHelper(this), user);
        Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
        finish();

    }
}
