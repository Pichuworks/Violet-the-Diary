package com.ProjectViolet.emotionDiary.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ProjectViolet.emotionDiary.R;
import com.ProjectViolet.emotionDiary.bean.User;
import com.ProjectViolet.emotionDiary.util.SqliteHelper;


public class NextActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_name;
    private AppCompatRadioButton rb_man;
    private AppCompatRadioButton rb_woman;
    private EditText et_phone;
    private Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        initView();
    }

    private void initView() {
        et_name = (EditText) findViewById(R.id.et_name);
        rb_man = (AppCompatRadioButton) findViewById(R.id.rb_man);
        rb_woman = (AppCompatRadioButton) findViewById(R.id.rb_woman);
        et_phone = (EditText) findViewById(R.id.et_phone);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(this);
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

        int sex = rb_man.isChecked() ? 1 : 2;

        String username = getIntent().getStringExtra("username");

        User user = new User();
        user.setUsername(username);
        user.setSex(sex);
        user.setPhone(phone);
        user.setName(name);
        SqliteHelper.updateUserByUsername(new SqliteHelper(this), user);

        setResult(RESULT_OK);
        //注册成功
        finish();
    }
}
