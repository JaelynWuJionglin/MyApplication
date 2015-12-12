package com.example.myapplication;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import Fragment.uitil.Fragment1;
import Fragment.uitil.Fragment2;
import Fragment.uitil.Fragment3;
import adapter.uiutl.MyFragmentPagerAdapter;
import adapter.uiutl.MyFragmentPagerAdapter2;

public class MainActivity4 extends FragmentActivity {

    private List<Fragment> viewList;
    private List<String> tabList;
    private ViewPager viewPager;
    private PagerTabStrip tabStrip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        /*
        * 通过View对象最为ViewPager的数据源
        * */
        View view1 = View.inflate(this, R.layout.view1, null);
        View view2 = View.inflate(this, R.layout.view2, null);
        View view3 = View.inflate(this, R.layout.view3, null);

        tabList = new ArrayList<String>();
        tabList.add("第一页");
        tabList.add("第二页");
        tabList.add("第三页");
        tabStrip = (PagerTabStrip) findViewById(R.id.tab);
        tabStrip.setBackgroundColor(Color.WHITE);
        tabStrip.setTextColor(Color.CYAN);
        tabStrip.setDrawFullUnderline(false);
        tabStrip.setTabIndicatorColor(Color.GREEN);

        /*
        * 通过Framgent作为ViewPager的数据源
        * */
        viewList = new ArrayList<Fragment>();
        viewList.add(new Fragment1());
        viewList.add(new Fragment2());
        viewList.add(new Fragment3());

        //初始化ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //创建适配器
        //MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),viewList,tabList);
        MyFragmentPagerAdapter2 adapter = new MyFragmentPagerAdapter2(getSupportFragmentManager(),viewList,tabList);
        //加载适配器
        viewPager.setAdapter(adapter);
    }
}