package jjun.jjunapp.programdrs.ArduinoPlay;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Locale;

import jjun.jjunapp.programdrs.Activity.MainActivity;
import jjun.jjunapp.programdrs.Communication.BluetoothService;
import jjun.jjunapp.programdrs.Communication.DeviceListActivity;
import jjun.jjunapp.programdrs.Communication.Protocol.DRS_SerialProtocol.DRS_Constants;
import jjun.jjunapp.programdrs.Communication.Protocol.DRS_SerialProtocol.DRS_SerialProtocol;
import jjun.jjunapp.programdrs.R;


/**
 * Created by jjunj on 2017-09-21.
 */

public class ArduinoPlayActivity extends AppCompatActivity {

    private static final String TAG = ArduinoPlayActivity.class.getClass().getSimpleName();

    // Bluetooth
    private static final int MODE_REQUEST = 1;
    public static final boolean D = true;

    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CONNECT_DEVICE = 1;

    private static final int COMMAND_LAYOUT_SIZE = 0;
    private static final int ARDUINOPLAY_REQUEST = 6;
    private static final int MESSAGE_STATE_CHANGE = 7;

    private static final int DRS_PROTOCOL = 101;

    ////////////////////

    private ArduinoPlayView arduinoPlayView;
    private BluetoothService mBluetoothService;
    private ArduinoPlayManager mPlayManager;
    private DRS_SerialProtocol drs;
    private TextToSpeech tts;

    private LinearLayout command_window;
    private TextView play_command_text;
    private ImageView controller;
    private ImageView bluetooth;
    private ScrollView scrollview;
    private TextView play_command_text2 ;


    private int command_layout_width , command_layout_height;
    private int currentScroll = 0;

    private String myBtAddress;
    private static final int horizontal = 8;
    private static final int vertical = 25;
    private int[] iconX_position = new int[horizontal]; // arrayOf Icon_X position
    private int[] iconY_position = new int[vertical]; // arrayOf Icon_Y position

    private boolean arduinoOn = false;
    private int currrentBtConnection = BluetoothService.STATE_NONE;

    private int BT_Connecting_tries = 0 ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_arduinoplay);

        command_window= (LinearLayout)findViewById(R.id.arduino_play_window);

        arduinoPlayView = new ArduinoPlayView(ArduinoPlayActivity.this,mHandler,iconX_position,iconY_position);
        arduinoPlayView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,8000));
        command_window.addView(arduinoPlayView);
        command_window.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                command_window.getViewTreeObserver().removeOnGlobalFocusChangeListener(this);
            }
        });
        command_window.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                float layout_width = command_window.getWidth();
                float layout_height = command_window.getHeight();

                mHandler.obtainMessage(COMMAND_LAYOUT_SIZE, (int) layout_width, (int) layout_height).sendToTarget();
            }
        });

        controller = (ImageView)findViewById(R.id.controller);
        controller.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    mPlayManager.executeController();
                    mPlayManager.bluetoothConnected(myBtAddress);
                }
                return true;
            }
        });

        bluetooth = (ImageView)findViewById(R.id.bluetooth);
        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currrentBtConnection == BluetoothService.STATE_CONNECTED){
                    mBluetoothService.stop();
                    arduinoPlayView.invalidate();
                    bluetooth.setImageDrawable(getResources().getDrawable(R.mipmap.bluetooth));
                    Toast.makeText(getApplicationContext(),"블루투스 연결이 해제되어 화면을 종료합니다.",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        scrollview = (ScrollView)findViewById(R.id.scrollView);

        play_command_text2 = (TextView)findViewById(R.id.play_command_text);

        mBluetoothService = new BluetoothService(ArduinoPlayActivity.this,mHandler,"DRS");
        mPlayManager = new ArduinoPlayManager(this,this,mHandler);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                tts.setLanguage(Locale.KOREA);
            }
        });

        Intent btintent = getIntent();
        myBtAddress = btintent.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

        // connect bluetooth
        mBluetoothService.getDeviceInfo(btintent);

    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch(msg.what){
                case COMMAND_LAYOUT_SIZE:
                    command_layout_width = msg.arg1;
                    command_layout_height = msg.arg2;

                    Log.d(TAG,"W : " + String.valueOf(command_layout_width));
                    Log.d(TAG,"H : " + String.valueOf(command_layout_height));


                    for (int i = 0; i < horizontal; i++)
                        iconX_position[i] = command_layout_width / (horizontal-1) * i;

                    for (int j = 0; j < vertical; j++)
                        iconY_position[j] = command_layout_height / (vertical-1) * j;

                    arduinoPlayView.setPosition(iconX_position,iconY_position);


                    break;

                case MESSAGE_STATE_CHANGE:
                    if (D) Log.i(TAG, "MESSAGE_STATE_CHANGE:" + msg.arg1);

                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            currrentBtConnection = msg.arg1;
                            TextView play_command_text = (TextView)findViewById(R.id.play_command_text);
                            play_command_text.setText(myBtAddress);
                            bluetooth.setImageDrawable(getResources().getDrawable(R.mipmap.bluetooth_cnt));

                            // send request message to Arduino for getting data stored in Arduino EEPROM
                            Log.d(TAG,"send request for getting command in Arduino");
                            drs = new DRS_SerialProtocol(DRS_Constants.DRS_CODING, mHandler ,mBluetoothService);
                            drs.make_send_DRS(DRS_Constants.REQUEST_COMMAND);

                            try{
                                Thread.sleep(50);
                            }catch (InterruptedException e){};

                            break;

                        case BluetoothService.STATE_CONNECTING:
//                            Toast.makeText(getApplicationContext(), "연결중....", Toast.LENGTH_LONG).show();
                            break;

                        case BluetoothService.STATE_FAIL:
//                            TextView play_command_text2 = (TextView)findViewById(R.id.play_command_text);
//                            play_command_text2.setText("블루투스 연결에 실패하였습니다.");
//                            mBluetoothService.stop();


                            if(BT_Connecting_tries < 2) {
                                BT_Connecting_tries ++;
                                play_command_text2.setText("블루투스 연결을 실패하였습니다. (" + BT_Connecting_tries + " / 3)");
                                if (mBluetoothService != null) {
                                    mBluetoothService.stop();

                                    if(mBluetoothService != null) {
                                        mBluetoothService = null;
                                        mBluetoothService = new BluetoothService(ArduinoPlayActivity.this, mHandler, "DRS");
                                        try{
                                            Thread.sleep(300);
                                        }catch (InterruptedException e){};
                                    }
                                    else{
                                        BT_Connecting_tries = 3;
                                    }
                                }

                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(mBluetoothService != null) {
                                            mBluetoothService = null;

                                            mBluetoothService = new BluetoothService(ArduinoPlayActivity.this, mHandler, "DRS");
                                            play_command_text2.setText("블루투스 연결을 다시 시도합니다.");
                                            Intent btintent = new Intent();
                                            btintent.putExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS, myBtAddress);
                                            mBluetoothService.getDeviceInfo(btintent);
                                        }
                                        else{
                                            BT_Connecting_tries = 3;
                                        }
                                    }
                                },1500);
                            }
                            else{
                                play_command_text2.setText("연결을 실패하여 업로드를 종료합니다.");
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                },1500);

                            }
                            break;

                        case BluetoothService.STATE_LISTEN :
                            if(currrentBtConnection == BluetoothService.STATE_CONNECTED){
                                String btfailed = "블루투스 연결이 해제되어 플레이를 종료합니다.";
                                tts.speak(btfailed, TextToSpeech.QUEUE_FLUSH,null);
                                bluetooth.setImageDrawable(getResources().getDrawable(R.mipmap.bluetooth));
                                mBluetoothService.stop();
                                finish();
                            }
                            currrentBtConnection = msg.arg1;
                            break ;

                        case BluetoothService.STATE_NONE :
                            currrentBtConnection = msg.arg1;
                            break ;
                    }
                    break;


                case DRS_PROTOCOL :
                    int level_round = msg.arg1;
                    int command = msg.arg2;
                    int[] payload =(int[])msg.obj;
                    if(level_round == DRS_Constants.DRS_CODING){

                        switch(command){
                            case DRS_Constants.REQUEST_COMMAND :
                                if(payload.length > 0) {
                                    int payload_index = 0;
                                    for(int i=0; i<payload.length; i++){
                                        Log.d(TAG,"payload : " + payload[i]);
                                    }
                                    for (int i = 0; i < payload.length / 5; i++) {
                                        int command_temp = payload[payload_index++];
                                        int value1 = read16((byte) payload[payload_index++], (byte) payload[payload_index++]);
                                        int value2 = read16((byte) payload[payload_index++], (byte) payload[payload_index++]);
                                        Log.d(TAG, "command " + i + " : command " + command_temp + "/ value1 " + value1 + "/ value2 " + value2);
                                        arduinoPlayView.addCommand(command_temp, value1, value2);
                                    }
                                    arduinoPlayView.invalidate();
                                }
                                else{
                                    mPlayManager.executeController();
                                    mPlayManager.bluetoothConnected(myBtAddress);
                                }


                                break;

                            case DRS_Constants.REQUEST_CURRENT_COMMAND :
                                Log.d("PLAY","Current OK");
                                int running_index = read8((byte)payload[0]) -1;
                                float current_vbat = (float)(read16((byte)payload[1], (byte)payload[2])) / 50;

                                Log.d(TAG,"running_index : " + running_index);
                                Log.d(TAG,"current" + current_vbat + "V");


                                if(running_index >= arduinoPlayView.getCommand_icon().size()){
                                    running_index = arduinoPlayView.getCommand_icon().size()-1;
                                    arduinoOn = false;
                                }

                                if (running_index != -1) {
                                    float x = arduinoPlayView.getCommand_icon().get(running_index).getX();
                                    float y = arduinoPlayView.getCommand_icon().get(running_index).getY();

                                    scrollview.setSmoothScrollingEnabled(true);
                                    scrollview.smoothScrollTo((int)x,(int)y - arduinoPlayView.getCommand_icon().get(running_index).getDisplayed_icon().getHeight());

                                }

                                arduinoPlayView.setCurrent_index(running_index);

                                arduinoPlayView.invalidate();
                                break;

                            case DRS_Constants.REQUEST_END :
                                String end = "동작이 완료되었습니다.";
                                tts.speak(end,TextToSpeech.QUEUE_FLUSH,null);
                                arduinoOn = false;
                                break;

                            case DRS_Constants.REQUEST_START :
                                String start = "동작을 시작합니다.";
                                tts.speak(start,TextToSpeech.QUEUE_FLUSH,null);
                                arduinoOn = true;
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        while(arduinoOn){
                                            drs.make_send_DRS(DRS_Constants.REQUEST_CURRENT_COMMAND);
                                            try {
                                                Thread.sleep(250);
                                            }catch (InterruptedException e){};
//                                        arduinoPlayView.invalidate();
                                        }
                                    }
                                }).start();
                                break;
                        }
                    }
                    break;

                case ARDUINOPLAY_REQUEST :
                    Log.d("PLAY","write data");
                    switch(msg.arg1){
                        case ArduinoPlayManager.DISMISSDIALOG :
                            mBluetoothService.stop();
                            mPlayManager = null;
                            break;

                        case ArduinoPlayManager.WRITE_DATA :
                            if(msg.arg2 == DRS_Constants.REQUEST_START){

                            }
                            else if(msg.arg2 == DRS_Constants.REQUEST_END){

                            }
                            drs.make_send_DRS(msg.arg2);
                            Log.d("PLAY","write data");

                            break;

                    }
                    break ;


            }
        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        AudioManager mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN )
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI);

        else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP )
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);

        else if(keyCode == KeyEvent.KEYCODE_BACK){
            mBluetoothService.stop();
            finish();
        }
        return true;
    }


    public int read8(byte int_8_1){
        return int_8_1 & 0xff;
    }

    public int read16(byte int_16_1, byte int_16_2){
        return ((int_16_1 & 0xff) + (int_16_2 << 8));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(drs != null)
            drs.make_send_DRS(DRS_Constants.REQUEST_END);
        arduinoOn = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mBluetoothService != null)
            mBluetoothService.stop();
    }
}
