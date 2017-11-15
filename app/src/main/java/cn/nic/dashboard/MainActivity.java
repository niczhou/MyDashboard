package cn.nic.dashboard;

import android.content.BroadcastReceiver;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends FragmentActivity {

    private TextView tv_connect;
    private TextView tv_display;
    private TextView tv_setting;
    private ViewPager vp_container;
    private List<TextView> tvTabList;

    private List<Fragment> fragmentList;
    private ConnectFragment connectFragment;
    private DisplayFragment displayFragment;
    private SettingFragment settingFragment;
    private FragmentPagerAdapter fpAdapter;

    private int currentTab=1;

    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
