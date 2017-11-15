package cn.nic.dashboard;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

/**
 * Created by nic on 2017/11/1.
 */

public class ControllerFragment extends Fragment implements View.OnClickListener{

    private Button btn_setting_left;
    private Button btn_setting_right;
    private Button btn_setting_up;
    private Button btn_setting_down;
    private Button btn_setting_ok;
    private SeekBar sb_accelerator;
    private Button btn_lamp_belt;
    private Button btn_lamp_handbrake;
    private Button btn_lamp_highbeam;
    private Button btn_lamp_belt2;
    private Button btn_lamp_temper;
    private Button btn_lamp_light;
    private Button btn_lamp_foglamp;

    private Bundle bundle;
    private Intent intent;
    private LocalBroadcastManager localBroadcastManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.fragment_controller,container,false);
        btn_setting_left= (Button) view.findViewById(R.id.setting_left);
        btn_setting_right=(Button)view.findViewById(R.id.setting_right);
        btn_setting_up= (Button) view.findViewById(R.id.setting_up);
        btn_setting_down= (Button) view.findViewById(R.id.setting_down);
        btn_setting_ok= (Button) view.findViewById(R.id.setting_ok);
        btn_lamp_belt= (Button) view.findViewById(R.id.btn_lamp_belt);
        btn_lamp_handbrake= (Button) view.findViewById(R.id.btn_lamp_handbrake);
        btn_lamp_highbeam= (Button) view.findViewById(R.id.btn_lamp_highbeam);
        sb_accelerator= (SeekBar) view.findViewById(R.id.sb_accelerator);
        btn_lamp_belt2= (Button) view.findViewById(R.id.btn_lamp_belt2);
        btn_lamp_temper= (Button) view.findViewById(R.id.btn_lamp_temper);
        btn_lamp_light= (Button) view.findViewById(R.id.btn_lamp_light);
        btn_lamp_foglamp= (Button) view.findViewById(R.id.btn_lamp_foglamp);

        btn_setting_left.setOnClickListener(this);
        btn_setting_right.setOnClickListener(this);
        btn_setting_up.setOnClickListener(this);
        btn_setting_down.setOnClickListener(this);
        btn_setting_ok.setOnClickListener(this);
        btn_lamp_belt.setOnClickListener(this);
        btn_lamp_highbeam.setOnClickListener(this);
        btn_lamp_handbrake.setOnClickListener(this);
        btn_lamp_belt2.setOnClickListener(this);
        btn_lamp_temper.setOnClickListener(this);
        btn_lamp_light.setOnClickListener(this);
        btn_lamp_foglamp.setOnClickListener(this);

        localBroadcastManager=LocalBroadcastManager.getInstance(getActivity());
        intent=new Intent();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sb_accelerator.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(bundle==null){
                    bundle=new Bundle();
                }
                bundle.putInt("acctValue",sb_accelerator.getProgress());
                intent.putExtras(bundle);
                intent.setAction("cn.nic.accelerator");
                localBroadcastManager.sendBroadcast(intent);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(bundle==null){
                    bundle=new Bundle();
                }
                bundle.putLong("acctTime",System.currentTimeMillis());
                intent.putExtras(bundle);
                intent.setAction("cn.nic.accelerator");
                localBroadcastManager.sendBroadcast(intent);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.setProgress(0);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        switch (v.getId()) {
            case R.id.setting_left:
            case R.id.setting_right:
            case R.id.setting_up:
            case R.id.setting_down:
            case R.id.setting_ok:
                intent.setAction("cn.nic.ctrl");
                switch (v.getId()) {
                    case R.id.setting_left:
                        bundle.putString("ctrl", "left");
                        break;
                    case R.id.setting_right:
                        bundle.putString("ctrl", "right");
                        break;
                    case R.id.setting_up:
                        bundle.putString("ctrl", "up");
                        break;
                    case R.id.setting_down:
                        bundle.putString("ctrl", "down");
                        break;
                    case R.id.setting_ok:
                        bundle.putString("ctrl", "ok");
                        break;
                }
                intent.putExtras(bundle);
                localBroadcastManager.sendBroadcast(intent);
                break;
            case R.id.btn_lamp_belt:
            case R.id.btn_lamp_handbrake:
            case R.id.btn_lamp_highbeam:
            case R.id.btn_lamp_belt2:
            case R.id.btn_lamp_temper:
            case R.id.btn_lamp_light:
            case R.id.btn_lamp_foglamp:
                intent.setAction("cn.nic.lamp");
                switch (v.getId()) {
                    case R.id.btn_lamp_belt:
                        bundle.putString("lamp", "belt");
                        break;
                    case R.id.btn_lamp_handbrake:
                        bundle.putString("lamp", "handbrake");
                        break;
                    case R.id.btn_lamp_highbeam:
                        bundle.putString("lamp", "highbeam");
                        break;
                    case R.id.btn_lamp_belt2:
                        bundle.putString("lamp", "belt2");
                        break;
                    case R.id.btn_lamp_temper:
                        bundle.putString("lamp", "temper");
                        break;
                    case R.id.btn_lamp_light:
                        bundle.putString("lamp", "light");
                        break;
                    case R.id.btn_lamp_foglamp:
                        bundle.putString("lamp", "foglamp");
                        break;
                }
                intent.putExtras(bundle);
                localBroadcastManager.sendBroadcast(intent);
                break;
            default:
                break;
        }
    }
}
