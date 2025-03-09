package com.example.healthmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.healthmanagement.Adapter.MainViewPagerAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ViewPager viewPager;
    private ArrayList<ImageView> viewlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    /**
     * 控件初始化
     */
    private void initView() {
        viewPager = findViewById(R.id.main_pager);
        viewlist = new ArrayList<ImageView>();
        viewlist.add(findViewById(R.id.main));
        viewlist.add(findViewById(R.id.mine));
        MainViewPagerAdapter mAdapter = new MainViewPagerAdapter(getSupportFragmentManager(),0);
        viewPager.setAdapter(mAdapter);
        SetViewClick(0);
        findViewById(R.id.main).setOnClickListener(this);
        findViewById(R.id.mine).setOnClickListener(this);
        //viewpager的滑动监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            //滑动一次都会调用这个方法
            @Override
            public void onPageSelected(int position) {
                SetViewClick(position);//设置底部选项卡
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    //设置选中状态
    private  void SetViewClick(int i) {
        ResetSelected();
        viewlist.get(i).setSelected(true);  //设置为选中状态
        viewPager.setCurrentItem(i);  // 设置对应的Fragment

    }
    //重置底部都为未选中状态
    private void ResetSelected() {
        for (int i = 0; i < viewlist.size(); i++) {
            viewlist.get(i).setSelected(false);  //设置为正常状态
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //点击第一个图标
            case R.id.main:
                SetViewClick(0);
                break;
            //点击第二个图标
            case R.id.mine:
                SetViewClick(1);
                break;
        }
    }
}