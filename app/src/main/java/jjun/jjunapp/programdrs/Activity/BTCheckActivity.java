package jjun.jjunapp.programdrs.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import jjun.jjunapp.programdrs.Command.CommandCheck;
import jjun.jjunapp.programdrs.Command.CommandIcon;
import jjun.jjunapp.programdrs.Communication.BluetoothService;
import jjun.jjunapp.programdrs.Communication.DeviceListActivity;
import jjun.jjunapp.programdrs.R;

/**
 * Created by jjunj on 2017-10-23.
 */

public class BTCheckActivity extends AppCompatActivity{

    private static final int MODE_REQUEST = 1;
    public static final boolean D = true;

    private static final int MESSAGE_STATE_CHANGE = 7;

    private TextView monitor_text;
    private ProgressBar commandcheckProgress;
    private Handler myHandler;

    private BluetoothService mBluetoothService;
    private int ReconnectCount = 0;
    private String address = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commandcheck);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        address = getIntent().getStringExtra("BT");

        monitor_text = (TextView)findViewById(R.id.monitor_text);
        commandcheckProgress = (ProgressBar)findViewById(R.id.command_check_progress);

        monitor_text.setText("블루투스 연결 중입니다.");
        commandcheckProgress.setVisibility(View.VISIBLE);

        mBluetoothService = new BluetoothService(this,mHandler,"DRS");
        Intent btintent = new Intent();
        btintent.putExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS,address);
        mBluetoothService.getDeviceInfo(btintent);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (D) Log.i("BTCheck", "MESSAGE_STATE_CHANGE:" + msg.arg1);

                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            commandcheckProgress.setVisibility(View.GONE);
                            monitor_text.setText("블루투스 연결이 성공적으로 되었습니다.\n잠시 후 화면이 자동적으로 종료됩니다.\n잠시만 기다려 주세요.");
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(mBluetoothService != null) {
                                        mBluetoothService.stop();
                                        mBluetoothService = null;
                                        finish();
                                    }

                                }
                            },2000);
                            break;

                        case BluetoothService.STATE_CONNECTING:
//                            Toast.makeText(getApplicationContext(), "연결중....", Toast.LENGTH_LONG).show();
                            break;

                        case BluetoothService.STATE_FAIL:
                            if(ReconnectCount < 2) {
                                ReconnectCount++;
                                commandcheckProgress.setVisibility(View.GONE);
                                monitor_text.setText("블루투스 연결을 실패했습니다.\n 아두이노 전원을 다시 확인해주세요.\n곧 다시 연결을 시도합니다.\n["
                                        + ReconnectCount + " / 3]");
                                mBluetoothService.stop();

                                    mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    monitor_text.setText("블루투스 연결 중입니다.");
                                                    commandcheckProgress.setVisibility(View.VISIBLE);
                                                }
                                            });
                                            Intent btintent = new Intent();
                                            btintent.putExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS, address);
                                            mBluetoothService.getDeviceInfo(btintent);

                                        }
                                    }, 2000);
                            }
                            else{
                                monitor_text.setText("블루투스 연결을 실패했습니다.\n 아두이노 전원을 다시 확인해주세요.\nTest를 종료합니다.");
                                commandcheckProgress.setVisibility(View.GONE);
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                },2000);
                            }
                            break;

                    }
                    break;
            }
        }
    };



    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
