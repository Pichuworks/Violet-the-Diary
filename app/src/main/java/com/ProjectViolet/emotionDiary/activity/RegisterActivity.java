package com.ProjectViolet.emotionDiary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ProjectViolet.emotionDiary.R;
import com.ProjectViolet.emotionDiary.util.SqliteHelper;


/**
 * @author
 * @time 2018-07-03 10:50
 * @类描述：注册
 * @变更记录: QQ:2354500192
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CODE_FLAG = 0x10;
    private EditText et_username;
    private EditText et_password;
    private EditText et_password_re;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();


    }

    private void initView() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_password_re = (EditText) findViewById(R.id.et_password_re);
        btn_register = (Button) findViewById(R.id.btn_register);

        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                submit();
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

        String password_re = et_password_re.getText().toString().trim();
        if (!password_re.equals(password)) {
            Toast.makeText(this, "两次密码不相同", Toast.LENGTH_SHORT).show();
            return;
        }

        int type = 1;

        int i = SqliteHelper.saveUser(new SqliteHelper(this), username, password, type);
        if (i == 1) {
            Toast.makeText(this, "此账号已注册", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, NextActivity.class);
        intent.putExtra("username", username);
        startActivityForResult(intent, CODE_FLAG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CODE_FLAG) {
            Toast.makeText(this, "注册成功！", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}
