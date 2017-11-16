package cn.nic.dashboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nic on 2017/11/8.
 */

public class CenterFragment extends Fragment {
    private ViewPager vp_container;
    private TextView tv_connect;
    private TextView tv_display;
    private TextView tv_setting;
    private TextView getTv_display;

    private View view;
    private List<TextView> tvTabList;
    private List<Fragment> fragmentList;
    private ConnectFragment connectFragment;
    private DisplayFragment displayFragment;
    private SettingFragment settingFragment;
    private TimeSettingFragment timeSettingFragment;

    private DynamicFragmentPagerAdapter fpAdapter;

    private FragmentManager fragmentManager;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver broadcastReceiver;

    private int currentTab=1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        view=inflater.inflate(R.layout.fragment_center,container,false);

        initViews();
        initList();
        fragmentManager=getFragmentManager();
        fpAdapter=new DynamicFragmentPagerAdapter(fragmentManager,fragmentList);
        vp_container.setAdapter(fpAdapter);
        vp_container.setCurrentItem(currentTab);
        toggleTab(currentTab);

        vp_container.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentTab=position;
                toggleTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    private void initViews(){
        tv_connect= (TextView) view.findViewById(R.id.tv_connect);
        tv_display=(TextView) view.findViewById(R.id.tv_display);
        tv_setting=(TextView) view.findViewById(R.id.tv_setting);
        vp_container=(ViewPager)view.findViewById(R.id.vp_container);
    }
    private void initList(){
        connectFragment=new ConnectFragment();
        displayFragment =new DisplayFragment();
        settingFragment=new SettingFragment();
        fragmentList=new ArrayList<Fragment>();
        fragmentList.add(settingFragment);
        fragmentList.add(displayFragment);
        fragmentList.add(connectFragment);

        tvTabList=new ArrayList<>();
        tvTabList.add(tv_setting);
        tvTabList.add(tv_display);
        tvTabList.add(tv_connect);
    }

    private void toggleTab(int tabID){
        for(int i=0;i<tvTabList.size();i++){
            tvTabList.get(i).setTextColor(getResources().getColor(R.color.colorTabUnselected));
            tvTabList.get(i).setBackgroundColor(getResources().getColor(R.color.colorTabBackUnelected));
        }
        tvTabList.get(tabID).setTextColor(getResources().getColor(R.color.colorTabSelected));
        tvTabList.get(tabID).setBackgroundColor(getResources().getColor(R.color.colorTabBackSelected));
    }

    @Override
    public void onResume() {
        super.onResume();
        localBroadcastManager=LocalBroadcastManager.getInstance(getActivity());
        broadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle infoBundle=intent.getExtras();
                if(infoBundle.containsKey("ctrl")) {
                    switch (infoBundle.getString("ctrl")) {
                        case "left":
                            currentTab = (fragmentList.size() + currentTab - 1) % fragmentList.size();
                            break;
                        case "right":
                            currentTab = (currentTab + 1) % fragmentList.size();
                            break;
                        case "ok":
                            switch (currentTab){
                                case 0:
                                    switch (settingFragment.getCurrentOption()) {
                                        case 0:
                                            if (fpAdapter.getIsAtSetting()) {
                                                fpAdapter.setFlag(true);
                                                timeSettingFragment = new TimeSettingFragment();
                                                fpAdapter.replaceFragment(currentTab, timeSettingFragment);
                                            } else {
                                                fpAdapter.setFlag(true);
                                                fpAdapter.replaceFragment(currentTab, settingFragment);
                                            }
                                            break;
                                        case 1:
                                            if (fpAdapter.getIsAtSetting()) {
                                                fpAdapter.setFlag(true);
                                                VehicleSettingFragment vehicleSettingFragment = new VehicleSettingFragment();
                                                fpAdapter.replaceFragment(currentTab, vehicleSettingFragment);
                                            } else {
                                                fpAdapter.setFlag(true);
                                                fpAdapter.replaceFragment(currentTab, settingFragment);
                                            }
                                            break;

                                    };
                                    break;
                                case 1:

                                    break;
                                case 2:

                                    break;
                            }
                            break;
                    }
                }
                vp_container.setCurrentItem(currentTab);
            }
        };
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("cn.nic.ctrl");
        localBroadcastManager.registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }

}
