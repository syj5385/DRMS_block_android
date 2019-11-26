package jjun.jjunapp.programdrs.Upload;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.LinkAddress;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.WritePendingException;
import java.util.ArrayList;

import jjun.jjunapp.programdrs.Communication.BluetoothService;
import jjun.jjunapp.programdrs.Communication.DeviceListActivity;
import jjun.jjunapp.programdrs.Communication.Protocol.STK500.STK500v1;
import jjun.jjunapp.programdrs.R;

/**
 * Created by jjunj on 2017-10-25.
 */

public class UploadManager {

    // Macro
    private static final String TAG = "UplaodManager";
    public static final boolean D = true;

    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CONNECT_DEVICE = 1;

    private static final int MESSAGE_STATE_CHANGE = 7;
    private static final int MESSAGE_WRITE = 2;
    private static final int MESSAGE_READ = 3;
    private static final int UPDATE_STATE = 4;

    private static final int ORIENT_COMMAND = 20;
    private static final int RECEIVED_MESSAGE = 21;
    private static final int UPLOAD_PROGRESS = 22;
    private static final int UPLOAD_STATE = 23;

    public static final int UPLOAD_FAILED = -1;
    public static final int UPLOAD_SUCCESS = 1;
    public static final int UPLOAD_START = 3;
    public static final int UPLOAD_END =4;

    private byte orient_command;
    private boolean isUploading = false;
    private boolean uploadSuccess = false;
    private int stateOnIndex = 0;
    private boolean isInitializing = false;
    private boolean startUpload  = true;

    private Activity mActivity;
    private ArrayList<Integer> command_data;
    private String bt_address;

    private AlertDialog.Builder upload_dialog;
    private AlertDialog dialog;

    //Object
    private BluetoothService mBluetoothService;
    private StringBuffer mOutStringBuffer;

    private STK500v1 stk500;

    // View
    private ImageView[] state = new ImageView[3];
    private TextView information;
    private LinearLayout progresslayout ;
    private ProgressView progressBar;

    // upload data
    private String hexData = null;

    // Bitmap(arrow)
    private Bitmap arrowOn;
    private Bitmap arrowOff;
    private Bitmap notConnected;
    private BitmapDrawable arrowOnDrawable;
    private BitmapDrawable arrowOffDrawable;
    private BitmapDrawable notConnectedDrawable;


    public UploadManager(Activity mActivity, ArrayList<Integer> command_data, String bt_address) {

        this.mActivity = mActivity;
        this.command_data = command_data;
        this.bt_address = bt_address;

        Paint arrowOnPaint = new Paint();
        arrowOnPaint.setColor(mActivity.getResources().getColor(R.color.redColor));
        arrowOnPaint.setStrokeWidth(20);

        Paint arrowOffPaint = new Paint();
        arrowOffPaint.setColor(Color.GRAY);
        arrowOffPaint.setStrokeWidth(20);

        Paint notConnectedPaint = new Paint();
        notConnectedPaint.setColor(Color.BLACK);
        notConnectedPaint.setStrokeWidth(20);

        Bitmap temp = BitmapFactory.decodeResource(mActivity.getResources(),R.mipmap.move_right);
        arrowOn = Bitmap.createBitmap(temp.getWidth()/2,temp.getHeight()/2, Bitmap.Config.ARGB_8888);
        Canvas arrowOnCanvas = new Canvas(arrowOn);
        arrowOnCanvas.drawLine(0,arrowOnCanvas.getHeight()/2, arrowOnCanvas.getWidth()-arrowOnPaint.getStrokeWidth()/5,arrowOnCanvas.getHeight()/2,arrowOnPaint);
        arrowOnCanvas.drawLine(arrowOnCanvas.getWidth()/2,arrowOnPaint.getStrokeWidth()/2,arrowOnCanvas.getWidth()-arrowOnPaint.getStrokeWidth()/2,arrowOnCanvas.getHeight()/2,arrowOnPaint);
        arrowOnCanvas.drawLine(arrowOnCanvas.getWidth()/2,arrowOnCanvas.getHeight()-arrowOnPaint.getStrokeWidth()/2,arrowOnCanvas.getWidth()-arrowOnPaint.getStrokeWidth()/2,arrowOnCanvas.getHeight()/2,arrowOnPaint);
        arrowOnDrawable = new BitmapDrawable(mActivity.getResources(),arrowOn);

        arrowOff = Bitmap.createBitmap(temp.getWidth()/2,temp.getHeight()/2, Bitmap.Config.ARGB_8888);
        Canvas arrowOffCanvas = new Canvas(arrowOff);
        arrowOffCanvas.drawLine(0,arrowOffCanvas.getHeight()/2, arrowOffCanvas.getWidth()-arrowOffPaint.getStrokeWidth()/5,arrowOffCanvas.getHeight()/2,arrowOffPaint);
        arrowOffCanvas.drawLine(arrowOffCanvas.getWidth()/2,arrowOffPaint.getStrokeWidth()/2,arrowOffCanvas.getWidth()-arrowOffPaint.getStrokeWidth()/2,arrowOffCanvas.getHeight()/2,arrowOffPaint);
        arrowOffCanvas.drawLine(arrowOffCanvas.getWidth()/2,arrowOffCanvas.getHeight()-arrowOffPaint.getStrokeWidth()/2,arrowOffCanvas.getWidth()-arrowOffPaint.getStrokeWidth()/2,arrowOffCanvas.getHeight()/2,arrowOffPaint);
        arrowOffDrawable = new BitmapDrawable(mActivity.getResources(),arrowOff);

        notConnected = Bitmap.createBitmap(temp.getWidth()/2,temp.getHeight()/2, Bitmap.Config.ARGB_8888);
        Canvas notConnectedCanvas = new Canvas(notConnected);
        notConnectedCanvas.drawLine(0,0,notConnectedCanvas.getWidth(),notConnectedCanvas.getHeight(),notConnectedPaint);
        notConnectedCanvas.drawLine(notConnectedCanvas.getWidth(),0,0,notConnectedCanvas.getHeight(),notConnectedPaint);
        notConnectedDrawable = new BitmapDrawable(mActivity.getResources(),notConnected);

        requestUploadDialog();
        requestConnectClassicBT();

        hexData = makeHexStringFromHexFile();

    }

    private void requestUploadDialog(){
        upload_dialog = new AlertDialog.Builder(mActivity);

        ConstraintLayout upload_view = (ConstraintLayout) View.inflate(mActivity, R.layout.activity_firmware,null);
        upload_dialog.setCancelable(false);
        upload_dialog.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(!isUploading) {
                    dialogInterface.dismiss();
                }
                else{
                    Toast.makeText(mActivity,"업로드 중에는 종료할 수 없습니다.",Toast.LENGTH_SHORT).show();
                }

            }
        });

        state[0] = (ImageView)upload_view.findViewById(R.id.arrow0);
        state[1] = (ImageView)upload_view.findViewById(R.id.arrow1);
        state[2] = (ImageView)upload_view.findViewById(R.id.arrow2);
        for(int i=0; i<state.length;i++)
            state[i].setImageDrawable(notConnectedDrawable);
        information = (TextView)upload_view.findViewById(R.id.information);
        information.setText("블루투스를 연결중 입니다.");

        progresslayout = (LinearLayout)upload_view.findViewById(R.id.progress_layout);
        progressBar = new ProgressView(mActivity);
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        progresslayout.addView(progressBar);

        progressBar.invalidate();

        upload_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(stk500 != null){
                    stk500.running = false;
                    stk500 = null;
                }

                if(mBluetoothService != null) {
                    mBluetoothService.stop();
                    mBluetoothService = null;
                }
            }
        });
        upload_dialog.setView(upload_view);
        dialog = upload_dialog.create();
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(startUpload){
                    try {
                        Thread.sleep(100);
                    }catch (InterruptedException e){}
                }
                uploadManagerHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        });
                    }
                },2000);
            }
        }).start();
    }

    private void requestConnectClassicBT(){
        mBluetoothService = new BluetoothService(mActivity,uploadManagerHandler,"STK");

        Intent btintent = new Intent();
        btintent.putExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS,bt_address);
        mBluetoothService.getDeviceInfo(btintent);
    }

    private boolean requestUpload(){
        boolean success = true;
        if(mBluetoothService != null) {
            if (mBluetoothService.getState() == BluetoothService.STATE_CONNECTED) {
//            displayStateOfUpload();

                // make binaryFile
                if (stk500 == null) {
                    stk500 = new STK500v1(mActivity, mBluetoothService, uploadManagerHandler);
                }

                final byte[] binaryFile = new byte[hexData.length() / 2];
                for (int i = 0; i < hexData.length(); i += 2)
                    binaryFile[i / 2] = Integer.decode("0x" + hexData.substring(i, i + 2)).byteValue();

                int UnitOfByte = 256;

                // execute Uploading
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        uploadSuccess = stk500.programHexFile(binaryFile, 256, true, requestMakeCommandDataFromCommand(command_data));
                        if (uploadSuccess) {
                            Log.d(TAG, "Uploading Successful");
                            isUploading = false;
                            uploadManagerHandler.obtainMessage(UPLOAD_STATE, 1, -1).sendToTarget();
                        } else {
                            Log.e(TAG, "Failed to Uploading");
                        }
                    }
                }).start();

            } else {
                success = false;
                Log.e(TAG, "bluetooth communication is not connected");
                isUploading = false;
                for (int i = 0; i < state.length; i++)
                    state[i].setImageDrawable(notConnectedDrawable);

                information.setTextColor(Color.argb(255, 255, 61, 95));
                information.setText("블루투스 연결이 끊어졌습니다.");
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){}
                dialog.dismiss();
                return success;
            }
        }
        else {
            success =false;
        }
        return success;
    }

    private void displayStateOfUpload(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(isUploading){
                    uploadManagerHandler.obtainMessage(UPDATE_STATE,stateOnIndex++,-1).sendToTarget();
                    try{
                        Thread.sleep(100);
                    }catch (InterruptedException e){};
                    if(stateOnIndex > 2)
                        stateOnIndex = 0;

                }
            }
        }).start();
    }

    private boolean init_state = false;
    private void display_initializing(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(isInitializing){
                    try{
                        Thread.sleep(200);
                    }catch (InterruptedException e){};
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(init_state){
                                init_state = false;
                                for(int i=0; i<state.length; i++)
                                    state[i].setImageDrawable(arrowOffDrawable);
                            }
                            else{
                                init_state = true;
                                for(int i=0; i<state.length; i++)
                                    state[i].setImageDrawable(arrowOnDrawable);
                            }
                        }
                    });
                }
            }
        }).start();
    }

    private int BT_Connecting_tries = 0;


    private Handler uploadManagerHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (D) Log.i(TAG, "MESSAGE_STATE_CHANGE:" + msg.arg1);

                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            information.setTextColor(Color.BLUE);
                            information.setText("리셋버튼을 눌러주세요");
                            for(int i=0; i<state.length; i++)
                                state[i].setImageDrawable(arrowOffDrawable);
                            requestUpload();

                            break;

                        case BluetoothService.STATE_CONNECTING:
//                            Toast.makeText(getApplicationContext(), "연결중....", Toast.LENGTH_LONG).show();
                            break;

                        case BluetoothService.STATE_FAIL:
                            if(BT_Connecting_tries < 2) {
                                BT_Connecting_tries ++;
                                information.setText("블루투스 연결을 실패하였습니다. (" + BT_Connecting_tries + " / 3)");
                                if (mBluetoothService != null) {
                                    mBluetoothService.stop();

                                    if(mBluetoothService != null) {
                                        mBluetoothService = null;
                                        mBluetoothService = new BluetoothService(mActivity, uploadManagerHandler, "STK");
                                        try{
                                            Thread.sleep(300);
                                        }catch (InterruptedException e){};
                                    }
                                    else{
                                        BT_Connecting_tries = 3;
                                    }
                                }

                                uploadManagerHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(mBluetoothService != null) {
                                            mBluetoothService = null;

                                            mBluetoothService = new BluetoothService(mActivity,uploadManagerHandler,"STK");
                                            information.setText("블루투스 연결을 다시 시도합니다.");
                                            Intent btintent = new Intent();
                                            btintent.putExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS, bt_address);
                                            mBluetoothService.getDeviceInfo(btintent);
                                        }
                                        else{
                                            BT_Connecting_tries = 3;
                                        }
                                    }
                                },1500);
                            }
                            else{
                                information.setText("연결을 실패하여 업로드를 종료합니다.");
                                uploadManagerHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                    }
                                },1500);

                            }
                            break;

                        case BluetoothService.STATE_DISCONNECTED :
                            Log.e(TAG,"bluetooth disconnected");
                            if(!uploadSuccess) {
                                if(stk500 != null)
                                    stk500.running = false;
                                isUploading = false;
                                information.setTextColor(Color.argb(255, 255, 61, 95));
                                information.setText("블루투스 연결이 끊어져 종료합니다.");
                                for (int i = 0; i < state.length; i++)
                                    state[i].setImageDrawable(notConnectedDrawable);
                                uploadManagerHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                    }
                                },2000);
                            }


                            break;
                    }
                    break;

                case MESSAGE_READ :
                    byte[] data = (byte[])msg.obj;
                    stk500.stk500_handler.obtainMessage(RECEIVED_MESSAGE,(int)orient_command,-1,data).sendToTarget();
                    int index = 0;

                    break;

                case ORIENT_COMMAND :
                    orient_command = (byte)msg.arg1;
                    break;


                case UPLOAD_STATE :
                    switch(msg.arg1){
                        case UPLOAD_SUCCESS:     // Finished Uploading Firmware
                            isUploading = false;
                            startUpload = false;
                            try {
                                Thread.sleep(500);
                            }catch (InterruptedException e){}
                            stk500.running = false;
                            mBluetoothService.stop();
                            information.setText("업로드가 완료 되었습니다.");
                            for(int i=0; i<state.length; i++)
                                state[i].setImageDrawable(notConnectedDrawable);
                            break;

                        case UPLOAD_FAILED:    // Failed to Upload Firmware

                            isUploading = false;

                            break;

                        case 2 :    // Timeout to press Arduino Reset btn
                            Log.d(TAG,"Try Again to Upload");
                            requestUpload();
                            break;

                        case UPLOAD_START :
                            information.setTextColor(Color.BLUE);
                            information.setText("initializing...");
                            isInitializing = true;

                            display_initializing();
                            break;

                        case UPLOAD_END :
                            information.setText("업로드 종료 요청 중입니다.");
                            break;

                        default :
                            if(mBluetoothService.getState() == BluetoothService.STATE_CONNECTED) {
                                if (!isUploading) {
                                    isInitializing = false;
                                    isUploading = true;
                                    displayStateOfUpload();
                                }
                                int progress = msg.arg1 * 100 / msg.arg2;
                                float current_byte = (float) msg.arg1 / 1000;
                                float finished_byte = (float) msg.arg2 / 1000;

                                information.setText("업로드 중... ( " + progress + " % 완료 )");
                                progressBar.setProgress(progress);
                                progressBar.invalidate();
                            }

                            break;
                    }
                    break;

                case UPDATE_STATE :
                    Log.d(TAG,"state on : " + msg.arg1);
                    for(int i=0; i<state.length ;i++)
                        state[i].setImageDrawable(arrowOffDrawable);

                    state[msg.arg1].setImageDrawable(arrowOnDrawable);
                    break;
            }
        }
    };

    public class ProgressView extends View{

        private Paint non_execuetPaint ;
        private Paint executedPaint;
        private Bitmap progressDrone;
        private int progress;
        private float droneLocation =0 ;

        public ProgressView(Context context) {
            super(context);

            non_execuetPaint = new Paint();
            non_execuetPaint.setStrokeWidth(30);
            non_execuetPaint.setColor(Color.GRAY);

            executedPaint = new Paint();
            executedPaint.setStrokeWidth(30);
            executedPaint.setColor(mActivity.getResources().getColor(R.color.progressColor));

            progressDrone = BitmapFactory.decodeResource(mActivity.getResources(),R.drawable.drone);
            droneLocation = 0;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            Log.d(TAG,"progress : " + progress);

            float progressLength = canvas.getWidth()-(progressDrone.getWidth()*2);
            droneLocation = progressDrone.getWidth()/2 + (progressLength*progress)/100;

//            canvas.drawLine(progressDrone.getWidth()/2,canvas.getHeight()/2, canvas.getWidth()-progressDrone.getWidth()/2,canvas.getHeight()/2,non_execuetPaint);
            canvas.drawLine(progressDrone.getWidth(),canvas.getHeight()/2,droneLocation+progressDrone.getWidth()/2, canvas.getHeight()/2,executedPaint);
            canvas.drawLine(droneLocation+progressDrone.getWidth()/2,canvas.getHeight()/2,canvas.getWidth()-progressDrone.getWidth(),canvas.getHeight()/2,non_execuetPaint);

            // progress -> location

            canvas.drawBitmap(progressDrone,droneLocation ,canvas.getHeight()/2-progressDrone.getHeight()/2,null);

        }

        public void setProgress(int progress){
            this.progress = progress;
        }
    }



    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // make upload File

    private String makeHexStringFromHexFile(){
        InputStream inputStream = null;

        inputStream = mActivity.getResources().openRawResource(R.raw.firmware);

        StringBuffer buffer = new StringBuffer();
        byte[] b = new byte[1024];

        try {
            for (int n; (n =inputStream.read(b)) != -1; ) {
                buffer.append(new String(b, 0, n));
            }
        }catch (IOException e){}

        String str = buffer.toString();

        String hexData_temp = str.replaceAll(":","3A");
        hexData_temp = hexData_temp.replaceAll(System.getProperty("line.separator"),"");
        hexData_temp = hexData_temp.replaceAll("\\p{Space}","");

        return hexData_temp;
    }

    private byte[] requestMakeCommandDataFromCommand(ArrayList<Integer> command){
        int command_temp_size = command.size()*5/3;
        Log.d(TAG,"command size : " + command.size());
        Log.d(TAG,"command_temp_size : " + command_temp_size);
        Character[] command_temp = new Character[command_temp_size];
        int command_index =0;
        int command_temp_index=0;

        Log.d(TAG,"the number OF command = " + command.size()/3);
        for(int i=0;i<command.size();i++){
            Log.w(TAG,"data : " + command.get(i));
        }
        for(int i=0; i<command.size()/3;i++){
            command_temp[command_temp_index++] = (char) (command.get(command_index++) & 0xff );

            //value1
            command_temp[command_temp_index++] = (char)(command.get(command_index) & 0xff);
            command_temp[command_temp_index++] = (char)((command.get(command_index++)>>8) & 0xff);

            //value2
            command_temp[command_temp_index++] = (char)(command.get(command_index) & 0xff);
            command_temp[command_temp_index++] = (char)((command.get(command_index++)>>8)&0xff);
            Log.d(TAG,"i : "  + i);
        }


        return requestCommand(command_temp);
    }

    private byte[] requestCommand(Character[] command){
        byte[] data_set = new byte[command.length];

        for(int i=0; i<data_set.length; i++) {
            data_set[i] = (byte) (command[i] & 0xff);
        }

//        Log.d(TAG,"DataSize = " + String.valueOf(data_set.length));
//        Log.d(TAG,"data 0 : " + String.valueOf(data_set[0]));

        return data_set;

    }
}