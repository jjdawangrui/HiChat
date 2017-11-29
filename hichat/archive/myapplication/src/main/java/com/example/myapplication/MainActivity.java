package com.example.myapplication;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;

import static com.example.myapplication.R.id.bt_register;

public class MainActivity extends AppCompatActivity {

    TextInputEditText etPwd;
    TextInputEditText etConfirmPwd;
    Button btRegister;
    TextInputEditText etUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println(getFilesDir()+"-----------");

        etPwd = (TextInputEditText) findViewById(R.id.et_pwd);
        etConfirmPwd = (TextInputEditText) findViewById(R.id.et_confirm_pwd);
        btRegister = (Button) findViewById(bt_register);
        etUsername = (TextInputEditText) findViewById(R.id.et_username);

        //监听软键盘的操作
        etUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_NEXT){
                    if(TextUtils.isEmpty(etUsername.getText().toString().trim())){
                        Toast.makeText(MainActivity.this, "用户名输入为空", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });


    }
}
