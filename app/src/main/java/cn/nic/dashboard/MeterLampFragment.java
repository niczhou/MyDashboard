package cn.nic.dashboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by nic on 2017/11/13.
 */

public class MeterLampFragment extends Fragment {
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver broadcastReceiver;

    private ImageView iv_lamp_belt;
    private ImageView iv_lamp_handbrake;
    private ImageView iv_lamp_highbeam;
    private ImageView iv_lamp_belt2;
    private ImageView iv_lamp_temper;
    private ImageView iv_lamp_light;
    private ImageView iv_lamp_foglamp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.fragment_meterlamp,container,false);
        iv_lamp_belt= (ImageView) view.findViewById(R.id.iv_lamp_belt);
        iv_lamp_handbrake= (ImageView) view.findViewById(R.id.iv_lamp_handbrake);
        iv_lamp_highbeam= (ImageView) view.findViewById(R.id.iv_lamp_highbeam);
        iv_lamp_belt2= (ImageView) view.findViewById(R.id.iv_lamp_belt2);
        iv_lamp_temper= (ImageView) view.findViewById(R.id.iv_lamp_temper);
        iv_lamp_light= (ImageView) view.findViewById(R.id.iv_lamp_light);
        iv_lamp_foglamp= (ImageView) view.findViewById(R.id.iv_lamp_foglamp);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        localBroadcastManager=LocalBroadcastManager.getInstance(getActivity());
        broadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle=intent.getExtras();
//                Log.d("nicdebug",bundle.getString("lamp"));
                if(bundle!=null && bundle.containsKey("lamp")) {
                    switch (bundle.getString("lamp")) {
                        case "belt":
                            toggleLamp(iv_lamp_belt);
                            break;
                        case "handbrake":
                            toggleLamp(iv_lamp_handbrake);
                            break;
                        case "highbeam":
                            toggleLamp(iv_lamp_highbeam);
                            break;
                        case "belt2":
                            toggleLamp(iv_lamp_belt2);
                            break;
                        case "temper":
                            toggleLamp(iv_lamp_temper);
                            break;
                        case "light":
                            toggleLamp(iv_lamp_light);
                            break;
                    }
                }
            }
        };
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("cn.nic.lamp");
        localBroadcastManager.registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }

        private void toggleLamp(View view){
            if(view.getVisibility()==View.VISIBLE){
                view.setVisibility(View.INVISIBLE);
            }else{
                view.setVisibility(View.VISIBLE);
            }
        }
}
