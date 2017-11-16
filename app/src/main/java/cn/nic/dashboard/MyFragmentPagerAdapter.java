package cn.nic.dashboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by nic on 2017/11/1.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private FragmentManager fm;
    private boolean flag=false;

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm=fm;
    }

    public MyFragmentPagerAdapter(FragmentManager fm,List<Fragment> fragmentList) {
        super(fm);
        this.fm=fm;
        this.fragmentList=fragmentList;
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
    public int getItemPosition(Object object) {
        if(flag) {
            return POSITION_NONE;
        }else {
//            return super.getItemPosition(object);
            return POSITION_UNCHANGED;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if(flag) {
            Fragment f = (Fragment) super.instantiateItem(container, position);
            String fTag=f.getTag();
//            Log.d("nicdebug",fTag);
//            need to commit update UI;
            fm.beginTransaction().remove(f).commit();
            f = fragmentList.get(position);
            fm.beginTransaction().attach(f).commit();
            fm.beginTransaction().add(container.getId(),f, fTag).commit();
            toggleFlag();
            return  f;
        }else {
            return super.instantiateItem(container, position);
        }
    }

    public void replaceFragment(int position, Fragment fragment) {
        fragmentList.set(position,fragment);
        notifyDataSetChanged();

    }

    public void toggleFlag() {
        flag=(flag?false:true);
    }
}
