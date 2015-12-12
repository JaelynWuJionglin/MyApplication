package adapter.uiutl;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Fragment管理的ViewPager的适配器
 * FragmentStatePagerAdapter适配器有销毁操作
 * Created by Jaelyn on 2015/12/13.
 */
public class MyFragmentPagerAdapter2 extends FragmentStatePagerAdapter{
    private List<Fragment> fragmentList;
    private List<String> list;
    public MyFragmentPagerAdapter2(FragmentManager fm, List<Fragment> fragmentList, List<String> list) {
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





    /*
    * 以下方法不需要改写
    * */


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
