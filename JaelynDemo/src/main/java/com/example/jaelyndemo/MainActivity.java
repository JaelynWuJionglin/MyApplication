package com.example.jaelyndemo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import ZidingyiView.Topbar;

public class MainActivity extends Activity {

    EditText et_userName,et_userPassword;
    CheckBox checkBox;
    Button button_Signin,button_cancel;
    SharedPreferences spf;
    Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Topbar topbar = (Topbar) findViewById(R.id.topbar);
        topbar.setOnTopbarClickLstener(new Topbar.topbarClickLstener() {
            @Override
            public void leftCilick() {
                Toast.makeText(MainActivity.this,"leftButtons-返回",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void rightCilick() {
                Toast.makeText(MainActivity.this,"rightButtons-确定",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void wiperSwitchCilick(boolean checkState) {
                Toast.makeText(MainActivity.this,checkState+"",Toast.LENGTH_SHORT).show();
            }
        });
        topbar.bMsetChecked(true);
        //topbar.setLeftIsvisable(false);


        //==============================================

        et_userName = (EditText) findViewById(R.id.et_userName);
        et_userPassword = (EditText) findViewById(R.id.et_userPassword);
        button_Signin = (Button) findViewById(R.id.button_Signin);
        button_cancel = (Button) findViewById(R.id.button_cancel);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

        spf = getSharedPreferences("UserInfo",MODE_PRIVATE);
        editor = spf.edit();
        String name = spf.getString("userName","");
        if(name.equals("")){
            checkBox.setChecked(false);
        }else {
            checkBox.setChecked(true);
            et_userName.setText(name);
        }

    }
    public void doClick(View v){
        switch (v.getId()){
            case R.id.button_Signin:
                String name = et_userName.getText().toString().trim();
                String pass = et_userPassword.getText().toString().trim();
                if("admin".equals(name)&&"123456".equals(pass)){
                    if(checkBox.isChecked()){
                        editor.putString("userName",name);
                        editor.commit();
                    }else {
                        editor.remove("userName");
                        editor.commit();
                    }
                    Toast.makeText(this,"登陆成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this,"用户名或密码错误，禁止登录",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button_cancel:
                finish();
                break;
        }

    }
}
