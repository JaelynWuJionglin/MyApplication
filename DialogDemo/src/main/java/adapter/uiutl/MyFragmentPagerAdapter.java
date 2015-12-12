package adapter.uiutl;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Fragment管理的ViewPager的适配器
 * FragmentPagerAdapter适配器不会有销毁操作，会一次性的加如所有页面，对于多页面的不选择
 * Created by Jaelyn on 2015/12/13.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter{
    private List<Fragment> fragmentList;
    private List<String> list;
    public MyFragmentPagerAdapter(FragmentManager fm,List<Fragment> fragmentList,List<String> list) {
        super(fm);
        this.fragmentList = fragmentList;
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position);
    }
}
