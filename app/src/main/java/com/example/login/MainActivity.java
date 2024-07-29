package com.example.login;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.login.fragment.HomeFragment;
import com.example.login.fragment.LifeFragment;
import com.example.login.fragment.MineFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private HomeFragment mHomeFragment;

    private LifeFragment mLifeFragment;
    private MineFragment mMineFragment;

    private BottomNavigationView mbottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //清晨日记的控件



        //初始化控件
        mbottomNavigationView = findViewById(R.id.bottomNavigationView);
        //mbottomNavigationView设计点击事件
        mbottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    selectedFragment(0);
                }else if(item.getItemId() == R.id.life) {
                    selectedFragment(1);
                } else {
                    selectedFragment(2);
                }

                return true;
            }
        });
        //默认首页选中
        selectedFragment(0);
    }


    //设置默认选中界面函数
    private void selectedFragment(int position){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(fragmentTransaction);

        if(position==0){
            if(mHomeFragment==null){
                mHomeFragment=new HomeFragment();
                fragmentTransaction.add(R.id.content,mHomeFragment);
            }else {
                 fragmentTransaction.show(mHomeFragment);
            }
        } else if (position==1) {
            if (mLifeFragment == null) {
                mLifeFragment = new LifeFragment();
                fragmentTransaction.add(R.id.content, mLifeFragment);
            } else {
                fragmentTransaction.show(mLifeFragment);
            }
        } else  {
            if (mMineFragment == null) {
                mMineFragment  = new MineFragment();
                fragmentTransaction.add(R.id.content, mMineFragment );
            } else {
                fragmentTransaction.show(mMineFragment);
            }

        }
        //一定一定要提交！！！
        fragmentTransaction.commit();
    }
    //当点击其他界面的时候，其他界面做隐藏
    private void hideFragment( FragmentTransaction fragmentTransaction){
        if(mHomeFragment!=null){
            fragmentTransaction.hide(mHomeFragment);

            if(mLifeFragment!=null) {
                fragmentTransaction.hide(mLifeFragment);
            }

            if(mMineFragment!=null) {
                fragmentTransaction.hide(mMineFragment);
            }
        }


    }
}