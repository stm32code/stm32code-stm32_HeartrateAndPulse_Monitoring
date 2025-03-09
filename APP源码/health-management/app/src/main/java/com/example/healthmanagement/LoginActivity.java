package com.example.healthmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.example.healthmanagement.Adapter.LoginViewPagerAdapter;
import com.example.healthmanagement.Util.Constant_HM;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String[] titles = new String[]{"登 录","注 册"};
    private ViewPager viewPager;
    public static Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 112:
                        Intent i = new Intent();
                        i.putExtra("result", msg.obj.toString());
                        setResult(RESULT_OK,i);
                        Constant_HM.LOGIN_STATE = true;
                        finish();
                        break;
                }
            }
        };
    }

    /**
     * 获取控件
     */
    private void initView() {
        /***
         * 绑定控件
         */
        viewPager = findViewById(R.id.login_view);
        /**
         * 初始化Tablelayout
         */
        TabLayout tb = findViewById(R.id.login_tab);
        for(int i=0;i<titles.length;i++){
            tb.addTab(tb.newTab().setText(titles[i])); //tablelayout添加item
        }
        //实例化适配器
        LoginViewPagerAdapter mAdapter = new LoginViewPagerAdapter(getSupportFragmentManager(),0);
        viewPager.setAdapter(mAdapter);
        tb.setupWithViewPager(viewPager);
        for(int i=0;i<titles.length;i++){
            tb.getTabAt(i).setText(titles[i]);
        }
        /**
         * 绑定监听
         */
        findViewById(R.id.back).setOnClickListener(this);
    }

    /**
     * 控件监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
}