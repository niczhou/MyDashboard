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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nic on 2017/11/1.
 */

public class SettingFragment extends Fragment {

    private List<TextView> tvOptionList;
    private TextView tv_option_0;
    private TextView tv_option_1;
    private TextView tv_option_2;
    private TextView tv_option_3;

    private int currentOption=0;

    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver broadcastReceiver;
    private FragmentManager fragmentManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.fragment_setting,container,false);
        tvOptionList=new ArrayList<>();
        tv_option_0= (TextView) view.findViewById(R.id.tv_option_0);
        tv_option_1= (TextView) view.findViewById(R.id.tv_option_1);
        tv_option_2= (TextView) view.findViewById(R.id.tv_option_2);
        tv_option_3= (TextView) view.findViewById(R.id.tv_option_3);
        tvOptionList.add(tv_option_0);
        tvOptionList.add(tv_option_1);
        tvOptionList.add(tv_option_2);
        tvOptionList.add(tv_option_3);

        toggleOption(currentOption);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentManager=getActivity().getSupportFragmentManager();
    }

    @Override
    public void onResume() {
        super.onResume();
        localBroadcastManager=LocalBroadcastManager.getInstance(getActivity());
        broadcastReceiver=new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle controlBundle=intent.getExtras();
                switch (controlBundle.getString("ctrl")){
                    case "up":
                        currentOption=(tvOptionList.size()+currentOption-1)%tvOptionList.size();
                        toggleOption(currentOption);
                        break;
                    case "down":
                        currentOption=(currentOption+1)%tvOptionList.size();
                        toggleOption(currentOption);
                        break;
                    case "ok":

                }
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

    private void toggleOption(int optionID){
        for(int i=0;i<tvOptionList.size();i++){
            tvOptionList.get(i).setTextColor(getResources().getColor(R.color.colorTabUnselected));
        }
        tvOptionList.get(optionID).setTextColor(getResources().getColor(R.color.colorTabSelected));
    }

}
