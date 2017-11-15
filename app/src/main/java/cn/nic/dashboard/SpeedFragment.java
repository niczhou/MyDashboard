package cn.nic.dashboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by nic on 2017/11/2.
 */

public class SpeedFragment extends Fragment{
    private SpeedView speedView;
    private TextView tv_speed;

    private long acctTime=0;
    private int acctValue=0;
    private int v_car=0;
    private int a_car=0;

    private Handler handler;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver broadcastReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.fragment_speed,container,false);
        speedView= (SpeedView) view.findViewById(R.id.speedView);
        tv_speed= (TextView) view.findViewById(R.id.tv_speed);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:

                        break;
                    case 2:
                        speedView.setValue(msg.arg1);
                        tv_speed.setText(Integer.toString(msg.arg1));
//                        tv_speed.setText(Integer.toString(msg.arg2));
                        break;
                }
            }
        };
        new SpeedThread().start();
    }

    @Override
    public void onResume() {
        super.onResume();
        localBroadcastManager=LocalBroadcastManager.getInstance(getActivity());
        broadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle=intent.getExtras();
                if(bundle!=null){
                    acctTime=bundle.getLong("acctTime");
                    acctValue=bundle.getInt("acctValue");
                }
            }
        };
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("cn.nic.accelerator");
        localBroadcastManager.registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }

    class SpeedThread extends Thread{
        static final int FRICTION=6;

        @Override
        public void run() {
//            super.run();
            while (true) {
                try {
                    Thread.sleep(250);
                    a_car=acctValue-FRICTION;
//                        Log.d("nicTag",Long.toString(System.currentTimeMillis() - startTime_accel));
                    v_car=(int)(speedView.getValue()+a_car*(System.currentTimeMillis() - acctTime)/10000);
                    if (v_car<0) {
                        v_car=0;
                    } else if (v_car >260) {
                        v_car=260;
                    }
                    Message msg=new Message();
                    msg.what=2;
                    msg.arg1= v_car;
                    msg.arg2=(int)a_car;
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
