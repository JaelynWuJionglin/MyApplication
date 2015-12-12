package com.example.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import adapter.uiutl.MyPagerAdapter;


/**
 * Created by Jaelyn on 2015/12/13.
 */
public class MainActivity2 extends Activity {

    private List<View> viewList;
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
        View view1 = View.inflate(this,R.layout.view1,null);
        View view2 = View.inflate(this,R.layout.view2,null);
        View view3 = View.inflate(this,R.layout.view3,null);

        tabList = new ArrayList<String>();
        tabList.add("第一页");
        tabList.add("第二页");
        tabList.add("第三页");
        tabStrip = (PagerTabStrip) findViewById(R.id.tab);
        tabStrip.setBackgroundColor(Color.WHITE);
        tabStrip.setTextColor(Color.CYAN);
        tabStrip.setDrawFullUnderline(false);
        tabStrip.setTabIndicatorColor(Color.GREEN);

        viewList = new ArrayList<View>();
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);

        //初始化ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //创建适配器
        MyPagerAdapter adapter = new MyPagerAdapter(viewList,tabList);
        //加载适配器
        viewPager.setAdapter(adapter);

    }

}
