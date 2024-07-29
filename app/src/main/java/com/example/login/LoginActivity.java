package com.example.login;

import android.content.Intent;
import android.content.SharedPreferences;
import  android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import db.UserDbHelper;
import entity.UserInfo;

public class LoginActivity extends AppCompatActivity {
    private EditText et_username;
    private EditText et_password;
    private CheckBox checkbox;
    //定义一个全局变量，获取登录成功的密码
    private  boolean is_login;
    //shareprefrence在登录中判断是否被点击

    private SharedPreferences msharepreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //初始化控件
        et_username=findViewById(R.id.et_username);
        et_password=findViewById(R.id.et_password);
 
        //初始化记住密码控件
        checkbox =findViewById(R.id.checkbox);

        //获取msharepreferences实例
        msharepreferences=getSharedPreferences("user",MODE_PRIVATE);

        //判断是否勾选了记住密码
        is_login=msharepreferences.getBoolean("is_login",false);
        if (is_login){
            String username=msharepreferences.getString("username",null);
            String password=msharepreferences.getString("password",null);
            et_username.setText(username);
            et_password.setText(password);
            checkbox.setChecked(true);
        }


        //点击注册
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到注册界面（activity的跳转都使用intent）
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);

            }


        });
        //登录按钮
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username =et_username.getText().toString();
                String password =et_password.getText().toString();
                if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "用户名或密码错误，请重试", Toast.LENGTH_SHORT).show();
                }else {
                    UserInfo login = UserDbHelper.getInstance(LoginActivity.this).login(username);
                    //闪退，登录没有信息，做条件判断
                    if(login!=null){
                        if(username.equals(login.getUsername())&&password.equals(login.getPassword())){

                            SharedPreferences.Editor edit = msharepreferences.edit();
                            edit.putBoolean("is_login",is_login);
                            edit.putString("username",username);
                            edit.putString("password",password);
                            //这里一定要提交，不能少
                            edit.commit();

                            //登录成功
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginActivity.this, "用户名或密码错误，请重试", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(LoginActivity.this, "账号暂未注册 ", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });
    //checkbox的点击事件
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
             is_login=isChecked;
            }
        });



    }

}