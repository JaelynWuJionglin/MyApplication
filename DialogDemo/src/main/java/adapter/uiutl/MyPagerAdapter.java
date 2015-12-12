package adapter.uiutl;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Jaelyn on 2015/12/13.
 */
public class MyPagerAdapter extends PagerAdapter{

    private List<View> viewList;
    private List<String> tabList;

    public MyPagerAdapter(List<View> viewList,List<String> tabList) {
        this.viewList = viewList;
        this.tabList = tabList;
    }

    /*
    * 返回的事页卡的数量
    * */
    @Override
    public int getCount() {
        return viewList.size();
    }

    /*
    * View 是否来自于对象
    * */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /*
    * 实例化一个页卡
    * */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    /*
    * 销毁一个页卡
    * */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }

    /*
    * 标题栏
    * */
    @Override
    public CharSequence getPageTitle(int position) {
        return tabList.get(position);
    }
}
