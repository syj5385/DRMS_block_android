package jjun.jjunapp.programdrs.ArduinoPlay;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import jjun.jjunapp.programdrs.Communication.Protocol.DRS_SerialProtocol.DRS_Constants;
import jjun.jjunapp.programdrs.R;


/**
 * Created by jjunj on 2017-09-20.
 */

public class ArduinoPlayManager {

    private static final int ARDUINOPLAY_REQUEST = 6;

    public static final int DISMISSDIALOG = 61;
    public static final int WRITE_DATA = 62;

    public static final int START = 100;

    private Context context;
    private Activity mActivity;
    private LayoutInflater inflater;
    private Handler mHandler;

    private AlertDialog.Builder startDialog;

    private LinearLayout dialogLayout;
    private ImageView play;
    private ImageView stop;
    private ImageView bluetooth;
    private TextView deviceaddress;

    public ArduinoPlayManager(Context context, Activity mActivity, Handler mHandler) {

        this.context = context;
        this.mActivity = mActivity;
        this.mHandler = mHandler;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//        mAdapter.addItem(new ArduinoControlItem("Controller",context.getResources().getDrawable(R.mipmap.ic_launcher),context.getResources().getDrawable(R.mipmap.ic_launcher)));

    }

    public void executeController(){
        OpenStartAlertDialog();
    }

    private void OpenStartAlertDialog(){
        startDialog = new AlertDialog.Builder(mActivity);

        dialogLayout = (LinearLayout)View.inflate(context,R.layout.arduinocontroller,null);
        dialogLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        bluetooth = (ImageView)dialogLayout.findViewById(R.id.bluetooth);
        play = (ImageView)dialogLayout.findViewById(R.id.arduino_play);
        stop = (ImageView)dialogLayout.findViewById(R.id.arduino_stop);
        deviceaddress = (TextView)dialogLayout.findViewById(R.id.address);

        startDialog.setView(dialogLayout).create();
        startDialog.show();

        startDialog.setOnDismissListener(null);
    }

    public void bluetoothConnected( String address){
        bluetooth.setImageDrawable(context.getResources().getDrawable(R.mipmap.bluetooth_cnt));
        deviceaddress.setText(address);
        play.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    play.setImageDrawable(context.getResources().getDrawable(R.drawable.arduino_play_on));
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP){

                    try{
                        Thread.sleep(25);
                    }catch (InterruptedException e){}
                    play.setImageDrawable(context.getResources().getDrawable(R.drawable.arduino_play));
                    Log.d("PLAY","send Message by Handler");
                    mHandler.obtainMessage(ARDUINOPLAY_REQUEST,WRITE_DATA, DRS_Constants.REQUEST_START).sendToTarget();

                }
                return true;
            }
        });

        stop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    stop.setImageDrawable(context.getResources().getDrawable(R.drawable.arduino_stop_on));

                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP){

                    try{
                        Thread.sleep(25);
                    }catch (InterruptedException e){}
                    stop.setImageDrawable(context.getResources().getDrawable(R.drawable.arduino_stop));

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mHandler.obtainMessage(ARDUINOPLAY_REQUEST,WRITE_DATA, DRS_Constants.REQUEST_END).sendToTarget();
                            try{
                                Thread.sleep(100);
                            }catch (InterruptedException e){};

                        }
                    }).start();

//
                }
                return true;
            }
        });
    }




}
