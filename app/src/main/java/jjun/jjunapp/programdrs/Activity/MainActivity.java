package jjun.jjunapp.programdrs.Activity;

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
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import jjun.jjunapp.programdrs.ArduinoPlay.ArduinoPlayActivity;
import jjun.jjunapp.programdrs.ArduinoPlay.ArduinoPlayManager;
import jjun.jjunapp.programdrs.Command.Command;
import jjun.jjunapp.programdrs.Command.CommandCheck;
import jjun.jjunapp.programdrs.Command.CommandWindowView;
import jjun.jjunapp.programdrs.Communication.BluetoothService;
import jjun.jjunapp.programdrs.Communication.DeviceListActivity;
import jjun.jjunapp.programdrs.CustomAdapter.CustomAdapter1.Custom1_Item;
import jjun.jjunapp.programdrs.CustomAdapter.CustomAdapter1.CustomAdapter1;
import jjun.jjunapp.programdrs.FileManagement.FileManagement;
import jjun.jjunapp.programdrs.R;
import jjun.jjunapp.programdrs.Sound.SoundManager;
import jjun.jjunapp.programdrs.Tutorial.Tutorial;
import jjun.jjunapp.programdrs.Upload.UploadManager;

public class MainActivity extends AppCompatActivity {

    // Bluetooth MACRO
    private static final String TAG = MainActivity.class.getClass().getSimpleName();

    // Bluetooth Variables
    private static final int MODE_REQUEST = 1;
    public static final boolean D = true;

    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    public static final int REQUEST_TUTORIAL_ACTIVITY = 3;
    public static final int REQUEST_TUTORIAL_FROM_INTRO = 4;
    public static final int REQUEST_UPLOAD_ACTIVITY = 5;
    public static final int REQUEST_BT_CHECK = 6;

    // Setting Macro
    private static final int COMMAND_LAYOUT_SIZE = 0; // MACRO for Handler
    private static final int ICON_ON_TOUCH_EVENT = 1;
    private static final int KEYBOARD_VALUE = 2;
    private static final int MOTOR_VALUE_UPDATE = 3;
    private static final int FILEMANAGEMENT = 4;
    private static final int CURRENT_MODE = 5;
    private static final int ARDUINOPLAY_REQUEST = 6;
    private static final int MESSAGE_STATE_CHANGE = 7;
    public static final int REMOVE_ALL_LAYOUT = 9;
    private static final int TUTORIAL = 10;
    private static final int COMMAND_CHECK = 11;

    // User View Definition
    private LinearLayout command_window;  // Main layout to put icons
    private LinearLayout left_menu_layout;   // Icon Box layout
    private LinearLayout app_menu_open;      // Main menu for application
    private LinearLayout menu_layout;   // Icon Menu
    private LinearLayout setting_layout;    // Icon setting (ex motor, timer etc.)
    private LinearLayout appmenu;
    private LinearLayout bottom_setting;    // Layout to set some value, that is motor speed, delay time, loop count

    public ScrollView command_scroll; // ScrollView of Command Layout

    private ImageView appmenu_open;     // Button to open the Application menu
    private ImageView[] number = new ImageView[12]; // ImageView to display keyboard

    private TextView command_text;
    private EditText keyboard_value;

    // User Variable
    private int command_layout_width , command_layout_height; // Size of Command Main Layout
    private int[] iconX_position = new int[6]; // arrayOf Icon_X position
    private int[] iconY_position = new int[26]; // arrayOf Icon_Y position
    private boolean ismenu_open = false;  // To check whether Main menu is open or not

    private String keyboard_number = "0";
    private ArrayList<Character> keyboard_array = new ArrayList<Character>();
    private boolean isKeyboard = false;
    private int concentration_index = 0;
    private String thisFileName = "";
    private boolean isSavedFile = false;
    private boolean isRequestUpload = false;
    private boolean isRequestFinish = false;
    private boolean isRequestTest = false;
    private boolean isfinishedWrite = false;

    // Object
    private Handler mHandler = new MyHandler();
    private TextToSpeech myTTS;
    private LayoutInflater inflater;

    private CustomAdapter1 motor_adapter = new CustomAdapter1(this);
    private CustomAdapter1 timer_adapter = new CustomAdapter1(this);
    private CustomAdapter1 jump_adapter = new CustomAdapter1(this);
    private CustomAdapter1 selectionBoxAdapter = new CustomAdapter1(this);
    private CommandWindowView command_view;  // main view / drawing Icon and Making ArrayList to manage data list
    private FileManagement mFileManagement;
    private BluetoothService mBluetoothService = null;
    private AlertDialog requestTemp ;
//    private ArrayList<CommandIcon> copy_command = new ArrayList<CommandIcon>();
    private SoundManager mSoundmanager;
    private ArduinoPlayManager mArduinoPlayManager;
    private Tutorial mTutorial;
    private CommandCheck check;

    //Bitmap (arrow)
    private Bitmap arrowOff;
    private Bitmap arrowOn;
    private Bitmap backIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"START Activity");

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initialize_UserViews();

        if(getIntent().getIntExtra(CheckPermissionActivity.REQUEST_TUTORIAL,0) == 1){
//            createRequestTutorialDialog();
            startActivityForResult(new Intent(this,IntroActivity.class),REQUEST_TUTORIAL_FROM_INTRO);
            overridePendingTransition(R.anim.fade,R.anim.hold);
        }



    }

    private void initialize_UserViews(){
        command_window = (LinearLayout)findViewById(R.id.command_window);
        left_menu_layout = (LinearLayout) findViewById(R.id.icon_box);
        menu_layout = (LinearLayout)findViewById(R.id.menu_layout);
        setting_layout = (LinearLayout)findViewById(R.id.setting_menu);
        app_menu_open = (LinearLayout)findViewById(R.id.appmenu_open);
        command_scroll = (ScrollView)findViewById(R.id.scrollView);
        command_scroll.setSmoothScrollingEnabled(true);

        appmenu = (LinearLayout)findViewById(R.id.appmenu) ;
        bottom_setting = (LinearLayout)findViewById(R.id.bottom_setting);

        // implementation

        appmenu_open = new ImageView(this);
        appmenu_open.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        appmenu_open.setImageDrawable(getResources().getDrawable(R.mipmap.appmenu));
        app_menu_open.addView(appmenu_open);

        app_menu_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(command_view.getCurrentMode() == CommandWindowView.NORMAL_MODE) {
                    Log.d(TAG,String.valueOf(ismenu_open));
                    if (!ismenu_open) {
                        removeAllViewOnCommandWindow();
                        mSoundmanager.play(0);
                        implementationAppMenuBox();
                        for (int i = 0; i < command_view.getCommand_icon().size(); i++) {
                            command_view.getCommand_icon().get(i).icon_unchecked();
                        }

                        command_view.invalidate();
                    } else {
                        removeAllViewOnCommandWindow();
                        command_text.setText(thisFileName);
                        mSoundmanager.play(0);
                        implementationInitIconBox();
                    }
                }
                else if(command_view.getCurrentMode() == CommandWindowView.TUTORIAL_MODE){
                    initializeCommandWindow();
                }
                else if(command_view.getCurrentMode() == CommandWindowView.COPY_MODE){
                    Toast.makeText(MainActivity.this,"copy mode에서는 클릭할 수 없습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        command_window.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                command_window.getViewTreeObserver().removeOnGlobalFocusChangeListener(this);
            }
        });
        command_window.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (command_layout_width == 0 && command_layout_height == 0){
                    float layout_width = command_window.getWidth();
                    float layout_height = command_window.getHeight();
                    mHandler.obtainMessage(COMMAND_LAYOUT_SIZE, (int) layout_width, (int) layout_height).sendToTarget();
                    if(command_view == null) {
                        command_view = new CommandWindowView(MainActivity.this,MainActivity.this, mHandler, iconX_position, iconY_position);
                        Log.d(TAG,"initialize commandView");
                        command_view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 8000));


                        if(!mFileManagement.isTempExist()){
                            command_view.addCommand(Command.START);
                        }
                        mFileManagement.initializeTempFile();
                        command_window.addView(command_view);
                    }
                }
            }
        });

        // IconBoxListAdapter Initialization
        selectionBoxAdapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.motor),"Motor\nBox"));
        selectionBoxAdapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.timer),"Timer\nBox"));
        selectionBoxAdapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.jump1_up),"Jump\nBox"));
        selectionBoxAdapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.start),"START"));
        selectionBoxAdapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.end),"END"));

        motor_adapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.motor3),"motor3"));
        motor_adapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.motor5),"motor5"));
        motor_adapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.motor6),"motor6"));
        motor_adapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.motor9),"motor9"));
        motor_adapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.motor10),"motor10"));
        motor_adapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.motor11),"motor11"));
        motor_adapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.motorall),"All Motor"));
        motor_adapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.stop),"Stop"));

        timer_adapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.timer_sec),"timer\n(sec)"));
        timer_adapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.timer_millis),"timer\n(millis)"));

        jump_adapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.jump1_up),"Jump1\nUp"));
        jump_adapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.jump1_down),"Jump1\ndown"));
        jump_adapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.jump2_up),"Jump2\nUp"));
        jump_adapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.jump2_down),"Jump2\ndown"));
        jump_adapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.jump3_up),"Jump3\nUp"));
        jump_adapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.jump3_down),"Jump3\ndown"));
        jump_adapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.jump4_up),"Jump4\nUp"));
        jump_adapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.jump4_down),"Jump4\ndown"));



//        mFileManagement = new FileManagement(this,mHandler);
//        thisFileName = mFileManagement.makeFileNameUsingData() +".drs";

        command_text = new TextView(MainActivity.this);
        command_text.setText("Command   - " + String.valueOf(thisFileName));
        command_text.setTextSize(20);
        command_text.setGravity(View.TEXT_ALIGNMENT_CENTER);
        command_text.setTextColor(Color.BLACK);

        appmenu.addView(command_text);

        mFileManagement = new FileManagement(this,mHandler);

        thisFileName = mFileManagement.makeFileNameUsingData()+".drs";
        mBluetoothService = new BluetoothService(MainActivity.this,mHandler,"DRS");

        mSoundmanager = new SoundManager(this);
        mArduinoPlayManager = new ArduinoPlayManager(this,this,mHandler);

        inflater = getLayoutInflater();


        new Thread(new Runnable() {
            @Override
            public void run() {
                while(command_view == null){
                    try{
                        Thread.sleep(3);
                    }catch (InterruptedException e){};

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        implementationInitIconBox();
                        command_text.setText(String.valueOf(thisFileName)); // command text initiailize;
                        command_text.setTextSize(20);
                        command_text.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        command_text.setTextColor(Color.BLACK);
                    }
                });
            }
        }).start();

        command_scroll.setSmoothScrollingEnabled(true);
    }

    // Handler Object to Receive Message
    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("Handler","msg.what = " + String.valueOf(msg.what));
            Log.d("Handler","msg.arg1 = " + String.valueOf(msg.arg1));
            switch (msg.what) {
                case COMMAND_LAYOUT_SIZE:
                    command_layout_width = msg.arg1;
                    command_layout_height = msg.arg2;

                    for (int i = 0; i < 6; i++)
                        iconX_position[i] = command_layout_width / 5 * i;

                    for (int j = 0; j < 26; j++)
                        iconY_position[j] = command_layout_height / 25 * j;

                    break;

                case ICON_ON_TOUCH_EVENT :
                    mHandler.obtainMessage(FILEMANAGEMENT,FileManagement.REQUEST_SAVE_TEMP,-1).sendToTarget();
                    switch(msg.arg1){
                        case CommandWindowView.INFLATE_CHILDREN_LAYOUT:
                            mSoundmanager.play(1);
                            isKeyboard = false;

                            removeAllViewOnCommandWindow();

                            concentration_index = msg.arg2;
                            int y = command_view.getCommand_icon().get(concentration_index).getY() -
                                    command_view.getCommand_icon().get(concentration_index).getDisplayed_icon().getHeight();
                            command_scroll.smoothScrollTo(0,y);
                            Log.w("Concentration_index","Concentration Index = " + String.valueOf(concentration_index));

                            int command_temp = command_view.getCommand_icon().get(msg.arg2).getCommand();
                            if((command_temp >= Command.MOTOR3 && command_temp < Command.STOP)){
                                ImplementationMotorSetting(concentration_index); // implementation Motor_setting Layout
                            }
                            else if(command_temp == Command.STOP){
                                implementationInitIconBox();
                                implementationExtraIconSetting(concentration_index);
//                                ImplementationMotorSetting(concentration_index);

                            }
                            else if(command_temp == Command.TIMER_MILLIS || command_temp == Command.TIMER_SEC){
                                ImplementationTimerSetting(concentration_index); // implementation timer_setting Layout
                            }
                            else if(command_temp == Command.START){
                                implementationInitIconBox();
                                implementationExtraIconSetting(concentration_index);

                            }

                            else if(command_temp == Command.END){
                                implementationInitIconBox();
                                implementationExtraIconSetting(concentration_index);
                            }

                            else if(command_temp >= Command.JUMP1_UP && command_temp <= Command.JUMP4_DOWN){
                                implementationExtraIconSetting(concentration_index);
                                if(command_temp % 2 == 0)
                                    ImplementationTimerSetting(concentration_index);
                                else {
                                    implementationInitIconBox();
                                    implementationExtraIconSetting(concentration_index);
                                }


                            }
                            bottom_setting.setVisibility(View.VISIBLE);
                            create_menu_and_update(concentration_index);
                            command_view.invalidate();
                            break;

                        case CommandWindowView.TOUCH_NO_ICON :
                            Log.d(TAG,"no touch sound");
                            mSoundmanager.play(1);
                            isKeyboard = false;
                            concentration_index = -1;
                            Log.w("Concentration_index","Concentration Index = " + String.valueOf(concentration_index));

                            bottom_setting.setVisibility(View.GONE);
                            for(int i=0; i<command_view.getCommand_icon().size();i++)
                                command_view.getCommand_icon().get(i).icon_unchecked();

                            Log.d("COMMANDVIEW","NOTOUCH");

                            command_view.invalidate();
                            implementationInitIconBox();
                            break;

                        case CommandWindowView.COPY_COMMAND_ITEM :
                            mSoundmanager.play(1);
                            break;
                    }

                    break;

                case KEYBOARD_VALUE :
                    String value = (String)msg.obj;
                    final int index = msg.arg1;

                    keyboard_value.setText(value);
                    command_view.invalidate();

                    Log.d(TAG,"number : "  + keyboard_number);

                    Log.d(TAG,"keyboardNumber  = " + keyboard_number);
                    Log.d(TAG,"keyboardarray : " + keyboard_array.size());
                    if(concentration_index != -1) {

                        if(command_view.getCommand_icon().get(concentration_index).getCommand()>= 12 &&
                                command_view.getCommand_icon().get(concentration_index).getCommand() < 19){
                            if(Integer.parseInt(keyboard_number) > 255){ // motor value range
                                Toast.makeText(MainActivity.this, "모터 속도 : 0 ~ 255",Toast.LENGTH_SHORT).show();
                                Log.d(TAG,"keyboardarray(check) : " + keyboard_array.size());

                                keyboard_array.remove(keyboard_array.size()-1);
                                char[] value_temp = new char[keyboard_array.size()];
                                for (int i = 0; i < keyboard_array.size(); i++)
                                    value_temp[i] = keyboard_array.get(i);

                                keyboard_number = new String(value_temp);

                                keyboard_value.setText(keyboard_number);
                                command_view.getCommand_icon().get(concentration_index).setValue(Integer.parseInt(keyboard_number));
                                value = keyboard_number;
                            }
                        }
                        if(command_view.getCommand_icon().get(concentration_index).getCommand() == 20){
                            if(Integer.parseInt(keyboard_number) > 10){ // motor value range
                                Toast.makeText(MainActivity.this, "Delay(sec) : 0 ~ 10",Toast.LENGTH_SHORT).show();
                                Log.d(TAG,"keyboardarray(check) : " + keyboard_array.size());

                                keyboard_array.remove(keyboard_array.size()-1);
                                char[] value_temp = new char[keyboard_array.size()];
                                for (int i = 0; i < keyboard_array.size(); i++)
                                    value_temp[i] = keyboard_array.get(i);

                                keyboard_number = new String(value_temp);

                                keyboard_value.setText(keyboard_number);
                                command_view.getCommand_icon().get(concentration_index).setValue(Integer.parseInt(keyboard_number));
                                value = keyboard_number;
                            }
                        }
                        if(command_view.getCommand_icon().get(concentration_index).getCommand() == 21){
                            if(Integer.parseInt(keyboard_number) > 1000){ // motor value range
                                Toast.makeText(MainActivity.this, "Delay(milli) : 0 ~ 1000",Toast.LENGTH_SHORT).show();
                                Log.d(TAG,"keyboardarray(check) : " + keyboard_array.size());

                                keyboard_array.remove(keyboard_array.size()-1);
                                char[] value_temp = new char[keyboard_array.size()];
                                for (int i = 0; i < keyboard_array.size(); i++)
                                    value_temp[i] = keyboard_array.get(i);

                                keyboard_number = new String(value_temp);

                                keyboard_value.setText(keyboard_number);
                                command_view.getCommand_icon().get(concentration_index).setValue(Integer.parseInt(keyboard_number));
                                value = keyboard_number;
                            }
                        }
                        if(command_view.getCommand_icon().get(concentration_index).getCommand() >= 22 &&
                                command_view.getCommand_icon().get(concentration_index).getCommand() >= 22 &&
                                command_view.getCommand_icon().get(concentration_index).getCommand() %2 == 0){
                            if(Integer.parseInt(keyboard_number) > 10){ // motor value range
                                Toast.makeText(MainActivity.this, "Loop : 0 ~ 10" +
                                        "",Toast.LENGTH_SHORT).show();
                                Log.d(TAG,"keyboardarray(check) : " + keyboard_array.size());

                                keyboard_array.remove(keyboard_array.size()-1);
                                char[] value_temp = new char[keyboard_array.size()];
                                for (int i = 0; i < keyboard_array.size(); i++)
                                    value_temp[i] = keyboard_array.get(i);

                                keyboard_number = new String(value_temp);
                                keyboard_value.setText(keyboard_number);
                                command_view.getCommand_icon().get(concentration_index).setValue(Integer.parseInt(keyboard_number));
                                value = keyboard_number;
                            }
                        }

                        if (command_view.getCommand_icon().get(index).getCommand() >= 12 && command_view.getCommand_icon().get(index).getCommand() < 19) {
                            TextView motor_setting = (TextView) findViewById(R.id.motor_speed);
                            if (motor_setting != null)
                                motor_setting.setText(value);
                        } else if (command_view.getCommand_icon().get(index).getCommand() >= 20 && command_view.getCommand_icon().get(index).getCommand() < 22) {
                            TextView timer_setting = (TextView) findViewById(R.id.delay_time);
                            if (timer_setting != null)
                                timer_setting.setText(value);
                        }

                    }
//
                    break;

                case FILEMANAGEMENT :
                    switch(msg.arg1){
                        case FileManagement.FINISHED_WRITE_DATA :
                            isfinishedWrite  = true;
                            if(isRequestUpload) {
                                isRequestUpload = false;
                                check = new CommandCheck(MainActivity.this,command_view.getCommand_icon(),mHandler);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        check.startCheckCommand();
                                    }
                                }).start();
                            }

                            if(isRequestFinish){
                                isRequestFinish = false;
                                mFileManagement.removeTempFile();
                                finish();
                            }
                            break;

                        case FileManagement.FINISHED_READ_DATA:
                            if(msg.arg2 == -1) {
                                ArrayList<ArrayList<Integer>> receivedfile = (ArrayList<ArrayList<Integer>>) msg.obj;
                                ArrayList<Integer> command = receivedfile.get(0);
                                ArrayList<Integer> value0 = receivedfile.get(1);
                                ArrayList<Integer> value1 = receivedfile.get(2);


                                if (command.size() == value0.size() && value0.size() == value1.size()) {
                                    command_view.removeAllCommand();
                                    for (int i = 0; i < command.size(); i++) {
                                        command_view.addCommand(command.get(i), value0.get(i), value1.get(i));
                                    }
                                    command_view.invalidate();
                                } else {
                                    Toast.makeText(MainActivity.this, "유효하지 않는 파일입니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else if(msg.arg2 == 0){
                                thisFileName = (String)msg.obj;
                                command_text.setText(thisFileName);
                            }
                            break;

                        case FileManagement.FAILED_READ_DATA :
                            Toast.makeText(MainActivity.this,"유효하지 않는 파일입니다.",Toast.LENGTH_SHORT).show();

                            break;

                        case FileManagement.REQUEST_SAVE_TEMP:
                            mFileManagement.writeCommandOnTempFile(command_view.getCommand_icon());
                            break;

                        case FileManagement.REQUEST_OPEN_TEMP:
                            final ArrayList<int[]> temp_command = (ArrayList<int[]>)msg.obj;
                            AlertDialog.Builder tempfile_ab = new AlertDialog.Builder(MainActivity.this);
                            tempfile_ab.setCancelable(false);
                            tempfile_ab.setTitle("파일 복구");
                            tempfile_ab.setCancelable(false);
                            tempfile_ab.setMessage("작성 중이던 파일을 복구하시겠습니까?\n\n - 이 파일은 예기치 못한 종료로 인한 임시파일입니다. \n - 파일을 보관하기 위해서는 반드시 저장해주세요.");
                            tempfile_ab.setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mSoundmanager.play(0);
                                    if(command_view.getCommand_icon().size() > 0){
                                        command_view.removeAllCommand();
                                    }

                                    Log.d(TAG,"size : " + temp_command.size());

                                    for(int j=0; j<temp_command.size(); j++){
                                        command_view.addCommand(temp_command.get(j)[0],temp_command.get(j)[1], temp_command.get(j)[2]);
                                    }
                                    Log.d(TAG,"size : " + temp_command.size());
                                    command_view.invalidate();
                                }
                            }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mSoundmanager.play(0);
                                    command_view.addCommand(Command.START);
                                    command_view.invalidate();
                                }
                            });
                            requestTemp = tempfile_ab.create();
                            requestTemp.show();
                    }
                    break;

                case MESSAGE_STATE_CHANGE:
                    if (D) Log.i(TAG, "MESSAGE_STATE_CHANGE:" + msg.arg1);

                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Toast.makeText(getApplicationContext(), "블루투스 연결에 성공하였습니다.\nDevice : "
                                    + mBluetoothService.address1, Toast.LENGTH_SHORT).show();
                            if(!isRequestTest)
                                mArduinoPlayManager.bluetoothConnected(mFileManagement.readBTAddress());
                            else{

                            }
                            break;

                        case BluetoothService.STATE_CONNECTING:
//                            Toast.makeText(getApplicationContext(), "연결중....", Toast.LENGTH_LONG).show();
                            break;

                        case BluetoothService.STATE_FAIL:
                            Toast.makeText(getApplicationContext(), "블루투스 연결에 실패하였습니다.", Toast.LENGTH_LONG).show();
                            mBluetoothService.stop();
                            break;

                    }
                    break;

                case ARDUINOPLAY_REQUEST :
                    switch(msg.arg1){
                        case ArduinoPlayManager.DISMISSDIALOG :
                            mBluetoothService.stop();
                            mArduinoPlayManager = null;
                            break;

                        case ArduinoPlayManager.WRITE_DATA :
                            if(msg.arg2 == ArduinoPlayManager.START){
                                byte[] data = new byte[1];
                                data[0] = 'Z';
                                mBluetoothService.write(data,MODE_REQUEST);
                                Log.d("BT","Writedata" + new String(data));
                            }

                            break;

                    }
                    break ;

                case REMOVE_ALL_LAYOUT :
                    removeAllViewOnCommandWindow();
                    break;

                case TUTORIAL :
                    Log.d(TAG,"Received message");

                    break;

                case COMMAND_CHECK :
                    mSoundmanager.play(0);
                    command_view.setCurrentMode(CommandWindowView.NORMAL_MODE);
                    command_text.setTextColor(Color.BLACK);
                    command_text.setText(thisFileName);
                    command_text.setOnClickListener(null);
                    command_view.invalidate();
                    left_menu_layout.setVisibility(View.VISIBLE);
                    implementationInitIconBox();
                    break;

            }
        }
    }

    // Implementation Layout box

    // Implementation Application Menu Box
    private void implementationAppMenuBox(){
        removeAllViewOnCommandWindow(); // Initialize Command Window

        ismenu_open = true;
        appmenu_open.setImageDrawable(getResources().getDrawable(R.mipmap.appmenu_on));

        inflater.inflate(R.layout.appmenu_layout,left_menu_layout,true);

        TextView list_topic = (TextView)findViewById(R.id.list_topic);
        list_topic.setText("Menu");

        ListView appmenu_list = (ListView)MainActivity.this.findViewById(R.id.file_list);
        CustomAdapter1 mAdapter = new CustomAdapter1(MainActivity.this);
        mAdapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.appmenu_new),"New"));
        mAdapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.appmenu_save),"Save"));
        mAdapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.appmenu_saveas),"Save as"));
        mAdapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.appmenu_myfile),"My File"));
        mAdapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.appmenu_play),"Play"));
        mAdapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.appmenu_upload),"Upload"));
        mAdapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.appmenu_bt),"Bluetooth\nSetting"));
        mAdapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.tutorial),"Tutorial"));
        appmenu_list.setAdapter(mAdapter);

        appmenu_list.setOnItemClickListener(appmenuItemClickListener);

    }


    // Implementation Icon Box
    private void implementationInitIconBox() {
        removeAllViewOnCommandWindow();

        inflater.inflate(R.layout.icon_box, left_menu_layout, true);
        ListView icon_box_list = (ListView) findViewById(R.id.selectionbox);
        final ImageView copy_icon = (ImageView) MainActivity.this.findViewById(R.id.copy_icon);
        final ImageView onview_icon = (ImageView) MainActivity.this.findViewById(R.id.onview);

        icon_box_list.setAdapter(selectionBoxAdapter);
        icon_box_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mSoundmanager.play(1);
                if(command_view.getCurrentMode() == CommandWindowView.NORMAL_MODE) {
                    switch (position) {
                        case 0:
                            implementationMotorBox();
                            break;

                        case 1:
                            implementationTimerBox();
                            break;

                        case 2:
                            implementationJumpBox();
                            break;

                        case 3:
                            command_view.addCommand(Command.START);
                            mHandler.obtainMessage(FILEMANAGEMENT,FileManagement.REQUEST_SAVE_TEMP,-1).sendToTarget();
                            break;

                        case 4:
                            command_view.addCommand(Command.END, 0, 0);
                            mHandler.obtainMessage(FILEMANAGEMENT,FileManagement.REQUEST_SAVE_TEMP,-1).sendToTarget();
                            break;
                    }
                }
            }
        });

        copy_icon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    mSoundmanager.play(1);
                    if (command_view.getCurrentMode() == CommandWindowView.NORMAL_MODE) {
                        command_view.getViewHandler().obtainMessage(CURRENT_MODE, CommandWindowView.COPY_MODE, -1).sendToTarget();
                        command_text.setText("복사할 항목을 선택하여 붙여넣기를 누르세요");
                        command_text.setTextSize(20);
                        command_text.setTextColor(getResources().getColor(R.color.lineColor));
//                        onview_icon.setVisibility(View.VISIBLE);
//                        copy_icon.setVisibility(View.GONE);


                    } else if (command_view.getCurrentMode() == CommandWindowView.COPY_MODE) {
                        command_view.getViewHandler().obtainMessage(CURRENT_MODE, CommandWindowView.NORMAL_MODE, -1).sendToTarget();
                        command_text.setText(thisFileName);
                        command_text.setTextSize(20);
                        command_text.setTextColor(Color.BLACK);
                        for (int i = 0; i < command_view.getCommand_icon().size(); i++) {
                            command_view.getCommand_icon().get(i).icon_unchecked();
                        }
                    }
                }
                command_view.invalidate();
                return true;
            }
        });

        onview_icon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    mSoundmanager.play(1);
                    int count = 0;
                    Log.d("COMMAND_COUNT", String.valueOf(command_view.getCommand_icon().size()));
                    for (int i = 0; i < command_view.getCommand_icon().size(); i++) {
                        if (command_view.getCommand_icon().get(i).getIconChecked()) {
                            command_view.addCommand(command_view.getCommand_icon().get(i).getCommand()
                                    , command_view.getCommand_icon().get(i).getValue(),command_view.getCommand_icon().get(i).getValue2());
                            command_view.getCommand_icon().get(i).icon_unchecked();
                            count++;
                        }
                    }
                    command_view.getViewHandler().obtainMessage(CURRENT_MODE, CommandWindowView.NORMAL_MODE, -1).sendToTarget();
                    command_text.setText("Command   - " + String.valueOf(thisFileName));
                    command_text.setTextSize(20);
                    command_text.setTextColor(Color.BLACK);
                    for (int i = 0; i < command_view.getCommand_icon().size(); i++) {
                        command_view.getCommand_icon().get(i).icon_unchecked();
                    }
                    command_view.invalidate();
                }
                return true;
            }
        });
    }

    private void implementationMotorBox(){
        removeAllViewOnCommandWindow();

        inflater.inflate(R.layout.selected_item,left_menu_layout,true);
        final ImageView selectedback = (ImageView)MainActivity.this.findViewById(R.id.motor_back);
        selectedback.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    selectedback.setImageDrawable(getResources().getDrawable(R.drawable.box_back_on));
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    mSoundmanager.play(1);
                    try{
                        Thread.sleep(25);
                    }catch (InterruptedException e){};
                    selectedback.setImageDrawable(getResources().getDrawable(R.drawable.box_back));

                    implementationInitIconBox();
                }
                return true;
            }
        });



        ListView motor_box = (ListView)MainActivity.this.findViewById(R.id.motor_box);
        motor_box.setAdapter(motor_adapter);
        motor_box.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mSoundmanager.play(1);
                switch(i){
                    case 0 :
                        command_view.addCommand(Command.MOTOR3);
                        break;

                    case 1:
                        command_view.addCommand(Command.MOTOR5);
                        break;

                    case 2 :
                        command_view.addCommand(Command.MOTOR6);
                        break;

                    case 3:
                        command_view.addCommand(Command.MOTOR9);
                        break;

                    case 4:
                        command_view.addCommand(Command.MOTOR10);
                        break;

                    case 5 :
                        command_view.addCommand(Command.MOTOR11);
                        break;

                    case 6 :
                        command_view.addCommand(Command.ALLMOTOR);
                        break;

                    case 7:
                        command_view.addCommand(Command.STOP);
                        break;
                }
                mHandler.obtainMessage(FILEMANAGEMENT,FileManagement.REQUEST_SAVE_TEMP,-1).sendToTarget();
            }
        });

    }

    private void implementationTimerBox(){
        removeAllViewOnCommandWindow();

        inflater.inflate(R.layout.selected_item,left_menu_layout,true);

        final ImageView timer_back = (ImageView)MainActivity.this.findViewById(R.id.motor_back);
        timer_back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    timer_back.setImageDrawable(getResources().getDrawable(R.drawable.box_back_on));
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    mSoundmanager.play(1);
                    try{
                        Thread.sleep(25);
                    }catch (InterruptedException e){};
                    timer_back.setImageDrawable(getResources().getDrawable(R.drawable.box_back));
                    implementationInitIconBox();
                }
                return true;
            }
        });

        ListView timer_box = (ListView)MainActivity.this.findViewById(R.id.motor_box);
        timer_box.setAdapter(timer_adapter);
        timer_box.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mSoundmanager.play(1);
                switch(i){
                    case 0 :
                        command_view.addCommand(Command.TIMER_SEC);
                        break;

                    case 1:
                        command_view.addCommand(Command.TIMER_MILLIS);
                        break;
                }
                mHandler.obtainMessage(FILEMANAGEMENT,FileManagement.REQUEST_SAVE_TEMP,-1).sendToTarget();
            }
        });
    }

    public void implementationJumpBox(){
        removeAllViewOnCommandWindow();

        inflater.inflate(R.layout.selected_item,left_menu_layout,true);

        final ImageView motor_back = (ImageView)MainActivity.this.findViewById(R.id.motor_back);
        motor_back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    motor_back.setImageDrawable(getResources().getDrawable(R.mipmap.box_back_on));
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    mSoundmanager.play(1);
                    try{
                        Thread.sleep(25);
                    }catch (InterruptedException e){};
                    motor_back.setImageDrawable(getResources().getDrawable(R.mipmap.box_back));

                    implementationInitIconBox();
                }
                return true;
            }
        });

        ListView motor_box = (ListView)MainActivity.this.findViewById(R.id.motor_box);
        motor_box.setAdapter(jump_adapter);
        motor_box.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mSoundmanager.play(1);
                switch(i){
                    case 0 :
                        command_view.addCommand(Command.JUMP1_UP);
                        break;

                    case 1:
                        command_view.addCommand(Command.JUMP1_DOWN);
                        break;

                    case 2 :
                        command_view.addCommand(Command.JUMP2_UP);
                        break;

                    case 3:
                        command_view.addCommand(Command.JUMP2_DOWN);
                        break;

                    case 4:
                        command_view.addCommand(Command.JUMP3_UP);
                        break;

                    case 5 :
                        command_view.addCommand(Command.JUMP3_DOWN);
                        break;

                    case 6 :
                        command_view.addCommand(Command.JUMP4_UP);
                        break;

                    case 7:
                        command_view.addCommand(Command.JUMP4_DOWN);
                        break;
                }
                mHandler.obtainMessage(FILEMANAGEMENT,FileManagement.REQUEST_SAVE_TEMP,-1).sendToTarget();
            }
        });

    }

    void ImplementationMotorSetting(final int index){
        removeAllViewOnCommandWindow();
        bottom_setting.setVisibility(View.VISIBLE);
        inflater.inflate(R.layout.motorsetting,setting_layout,true);

        ImageView M_image = (ImageView)MainActivity.this.findViewById(R.id.motor_image);
        final EditText motor_speed = (EditText)MainActivity.this.findViewById(R.id.motor_speed);
        final SeekBar motor_seekbar = (SeekBar)MainActivity.this.findViewById(R.id.motor_seekbar);

        Log.d("SPEED",String.valueOf(command_view.getCommand_icon().get(index).getValue()));
        motor_speed.setText(String.valueOf(command_view.getCommand_icon().get(index).getValue()));
        motor_seekbar.setProgress(command_view.getCommand_icon().get(index).getValue());

        M_image.setImageDrawable(new BitmapDrawable(command_view.getCommand_icon().get(index).getUnchecked_icon()));

        motor_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                motor_speed.setText(String.valueOf(i));
                command_view.updateValue(concentration_index,i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        implementationKeyboardBox(index);
    }

    void ImplementationTimerSetting(final int index){
        removeAllViewOnCommandWindow();

        bottom_setting.setVisibility(View.VISIBLE);
        inflater.inflate(R.layout.timersetting,setting_layout,true);

        ImageView timer_image = (ImageView)MainActivity.this.findViewById(R.id.timer_image);
        final EditText delay_time = (EditText)MainActivity.this.findViewById(R.id.delay_time);
        TextView unit = (TextView)MainActivity.this.findViewById(R.id.unit);
        delay_time.setText(String.valueOf(command_view.getCommand_icon().get(index).getValue()));
        timer_image.setImageDrawable(new BitmapDrawable(command_view.getCommand_icon().get(index).getUnchecked_icon()));
        if(command_view.getCommand_icon().get(index).getCommand() == Command.TIMER_SEC)
            unit.setText(" sec");
        else if(command_view.getCommand_icon().get(index).getCommand() == Command.TIMER_MILLIS)
            unit.setText(" millisecond");
        else
            unit.setText(" cycle");

        final int value = Integer.parseInt(delay_time.getText().toString());

        implementationKeyboardBox(index);

    }

    void implementationExtraIconSetting(final int index){
        Log.d(TAG,"implementation Extra Icon Setting");
//        removeAllViewOnCommandWindow();

        bottom_setting.setVisibility(View.VISIBLE);
        inflater.inflate(R.layout.selectedicon,setting_layout,true);

        ImageView icon = (ImageView)MainActivity.this.findViewById(R.id.icon);
        TextView icon_text = (TextView)MainActivity.this.findViewById(R.id.text);
        icon.setImageDrawable(new BitmapDrawable(command_view.getCommand_icon().get(index).getUnchecked_icon()));

        icon.setImageDrawable(new BitmapDrawable(command_view.getCommand_icon().get(index).getUnchecked_icon()));
    }

    //Implementation Keyboard Box
    private void implementationKeyboardBox(final int index){

        Log.d("ARRAY","size : " + String.valueOf(keyboard_array.size()));

        inflater.inflate(R.layout.keyboard_layout,left_menu_layout,true);

        keyboard_value = (EditText)findViewById(R.id.keyboard_value);

        number[0] = (ImageView)MainActivity.this.findViewById(R.id.zero);
        number[1] = (ImageView)MainActivity.this.findViewById(R.id.one);
        number[2] = (ImageView)MainActivity.this.findViewById(R.id.two);
        number[3] = (ImageView)MainActivity.this.findViewById(R.id.three);
        number[4] = (ImageView)MainActivity.this.findViewById(R.id.four);
        number[5] = (ImageView)MainActivity.this.findViewById(R.id.five);
        number[6] = (ImageView)MainActivity.this.findViewById(R.id.six);
        number[7] = (ImageView)MainActivity.this.findViewById(R.id.seven);
        number[8] = (ImageView)MainActivity.this.findViewById(R.id.eight);
        number[9] = (ImageView)MainActivity.this.findViewById(R.id.nine);
        number[10] = (ImageView)MainActivity.this.findViewById(R.id.enter);
        number[11] = (ImageView)MainActivity.this.findViewById(R.id.delete);

        for(int i=0; i<number.length ; i++)
            number[i].setOnTouchListener(KeyboardTouchListener);

        isKeyboard = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(keyboard_array.size() != 0){
                    keyboard_array.remove(0);
                }
                for(byte c : String.valueOf(command_view.getCommand_icon().get(concentration_index).getValue()).getBytes()){
                    keyboard_array.add((char)c);
                }
                while(isKeyboard){
                    keyboard_number = String.valueOf(command_view.getCommand_icon().get(concentration_index).getValue());

                    mHandler.obtainMessage(KEYBOARD_VALUE,index,-1,keyboard_number).sendToTarget();
                    try{
                        Thread.sleep(100);
                    }catch (InterruptedException e){};
                }
            }
        }).start();

    }


    private void create_menu_and_update(final int index){

        inflater.inflate(R.layout.menulayout,menu_layout,true);
        final ImageView remove = (ImageView) MainActivity.this.findViewById(R.id.remove);
        final ImageView move_left = (ImageView)MainActivity.this.findViewById(R.id.move_left);
        final ImageView move_right = (ImageView)MainActivity.this.findViewById(R.id.move_right);
        final ImageView settingSave = (ImageView)MainActivity.this.findViewById(R.id.motor_save) ;

        remove.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    remove.setImageDrawable(getResources().getDrawable(R.mipmap.remove_on));
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    try {
                        Thread.sleep(20);
                    }catch (InterruptedException e){};

                    remove.setImageDrawable(getResources().getDrawable(R.mipmap.remove));

                    command_view.removeComand(index);
                    if(index == command_view.getCommand_icon().size()){
                        command_view.setPrev_concentration_index(-1);
                        concentration_index = -1;
                        implementationInitIconBox();
                        mHandler.obtainMessage(ICON_ON_TOUCH_EVENT,CommandWindowView.TOUCH_NO_ICON,-1).sendToTarget();
                    }
                    else {
                        command_view.getCommand_icon().get(index).icon_checked();
                        mHandler.obtainMessage(ICON_ON_TOUCH_EVENT, CommandWindowView.INFLATE_CHILDREN_LAYOUT, index).sendToTarget();
                    }

                    Log.d(TAG,"concentration_index = " + concentration_index);
                    mHandler.obtainMessage(FILEMANAGEMENT,FileManagement.REQUEST_SAVE_TEMP,-1).sendToTarget();
                }
                return true;
            }
        });

        move_left.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    move_left.setImageDrawable(getResources().getDrawable(R.mipmap.move_left_on));
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    mSoundmanager.play(1);

                    move_left.setImageDrawable(getResources().getDrawable(R.mipmap.move_left));

                    command_view.getCommand_icon().get(index).icon_unchecked();
                    command_view.moveLeft(index);

                    if(index >0) {
                        command_view.getCommand_icon().get(index - 1).icon_checked();
                        mHandler.obtainMessage(ICON_ON_TOUCH_EVENT, CommandWindowView.INFLATE_CHILDREN_LAYOUT, index-1).sendToTarget();
                    }
                    else{
                        command_view.getCommand_icon().get(index).icon_unchecked();
                        implementationInitIconBox();
                    }
                    mHandler.obtainMessage(FILEMANAGEMENT,FileManagement.REQUEST_SAVE_TEMP,-1).sendToTarget();


                }
                return true;
            }
        });

        move_right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){

                    move_right.setImageDrawable(getResources().getDrawable(R.mipmap.move_right_on));

                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    mSoundmanager.play(1);

                    move_right.setImageDrawable(getResources().getDrawable(R.mipmap.move_right));

                    command_view.getCommand_icon().get(index).icon_unchecked();
                    command_view.moveRight(index);

                    if(index < command_view.getCommand_icon().size()-1) {
                        command_view.getCommand_icon().get(index + 1).icon_checked();
                        mHandler.obtainMessage(ICON_ON_TOUCH_EVENT, CommandWindowView.INFLATE_CHILDREN_LAYOUT, index+1).sendToTarget();
                    }
                    else{
                        command_view.getCommand_icon().get(index).icon_unchecked();
                        implementationInitIconBox();
                    }
                    mHandler.obtainMessage(FILEMANAGEMENT,FileManagement.REQUEST_SAVE_TEMP,-1).sendToTarget();
                }
                return true;
            }
        });

        settingSave.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    settingSave.setImageDrawable(getResources().getDrawable(R.mipmap.cancel_on));
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    mSoundmanager.play(1);
                    try{
                        Thread.sleep(20);
                    }catch (InterruptedException e){};
                    settingSave.setImageDrawable(getResources().getDrawable(R.mipmap.cancel));

                    command_view.getCommand_icon().get(concentration_index).icon_unchecked();

                    implementationInitIconBox();
                    command_view.invalidate();

                    mHandler.obtainMessage(FILEMANAGEMENT,FileManagement.REQUEST_SAVE_TEMP,-1).sendToTarget();

                }
                return true;
            }
        });

    }

    private void implementationSaveAsBox(){

        AlertDialog.Builder save_dialog = new AlertDialog.Builder(this);
        save_dialog.setTitle("Save As");
        final LinearLayout save_layout = (LinearLayout)View.inflate(this,R.layout.saveaslayout,null);
        final EditText nameValue = (EditText)save_layout.findViewById(R.id.namevalue);
        nameValue.setText(thisFileName.substring(0,thisFileName.length()-4));
        nameValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameValue.setText("");
            }
        });
        save_dialog.setView(save_layout);
        save_dialog.setCancelable(false);
        save_dialog.setPositiveButton("저장", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(command_view.getCommand_icon().size() > 0) {

                    String name_temp = nameValue.getText().toString();
                    if(mFileManagement.fileNameCheck(name_temp)){
                        Log.d(TAG,"possible");
                        if (mFileManagement.createFiles(name_temp)) {
//                    mFileManagement
                            if (mFileManagement.writeCommandOnFile(name_temp + ".drs", command_view.getCommand_icon())) {
                                isSavedFile = true;
                                thisFileName = name_temp + ".drs";
                                command_text.setText(thisFileName);
                                command_text.setTextSize(20);
                                command_text.setTextColor(Color.BLACK);
                                implementationInitIconBox();
                            } else {
                                Toast.makeText(MainActivity.this, "쓰기 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    else{
                        Log.d(TAG,"impossible");
                        Toast.makeText(MainActivity.this,"파일의 이름을 확인해주세요.",Toast.LENGTH_SHORT).show();
                        implementationSaveAsBox();
                    }
                    // write data and Save

//                    if (mFileManagement.createFiles(name_temp)) {
////                    mFileManagement
//                        if (mFileManagement.writeCommandOnFile(name_temp + ".drs", command_view.getCommand_icon())) {
//                            isSavedFile = true;
//                            thisFileName = name_temp + ".drs";
//                            command_text.setText("Command   - " + String.valueOf(thisFileName));
//                            command_text.setTextSize(20);
//                            command_text.setTextColor(Color.BLACK);
//                            implementationInitIconBox();
//                        } else {
//                            Toast.makeText(MainActivity.this, "쓰기 실패", Toast.LENGTH_SHORT).show();
//                        }
//                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"저장할 내용이 없습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("취소",null).create().show();
    }

    //Implementation MyFile
    private void implementationOpenBox(){
        removeAllViewOnCommandWindow();
        ismenu_open = true;
        appmenu_open.setImageDrawable(getResources().getDrawable(R.mipmap.appmenu_on));

        inflater.inflate(R.layout.appmenu_layout,left_menu_layout,true);

        final ListView fileList = (ListView)MainActivity.this.findViewById(R.id.file_list);
        final CustomAdapter1 mAdapter  = mFileManagement.getMyFileAdapter();
        fileList.setAdapter(mAdapter);

        fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {  // open
                mSoundmanager.play(1);
                isSavedFile = true;

                mFileManagement.readCommandFromFile(position);
                thisFileName = mFileManagement.getFilelist()[position].getName();
                command_text.setText(thisFileName);

                implementationInitIconBox();
            }
        });

        fileList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { // delete
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                mSoundmanager.play(1);

                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(MainActivity.this);

                LinearLayout title_layout = new LinearLayout(MainActivity.this);
                title_layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                title_layout.setGravity(View.TEXT_ALIGNMENT_CENTER);

                inflater.inflate(R.layout.delete_dialog_topi,title_layout,true);

                deleteDialog.setCustomTitle(title_layout);
                deleteDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        mFileManagement.deleteFile(mFileManagement.getFilelist()[position].getName());

                        CustomAdapter1 mAdapter = mFileManagement.getMyFileAdapter();
                        fileList.setAdapter(mAdapter);
                    }
                });

                deleteDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });

                deleteDialog.create();
                deleteDialog.show();

                return true;
            }
        });

    }


    private void removeAllViewOnCommandWindow(){
        isKeyboard = false; // keyboard initialize

        ismenu_open = false;    // application menu initialize
        appmenu_open.setImageDrawable(getResources().getDrawable(R.mipmap.appmenu));

        if(command_text == null){
            command_text = new TextView(this);
            appmenu.addView(command_text);
        }

        left_menu_layout.removeAllViews(); // left menu initialize

        setting_layout.removeAllViews();    // bottom | left setting layout initialize
        menu_layout.removeAllViews();       // bottom | right icon_menu layout initialize
        bottom_setting.setVisibility(View.GONE);
    }

    private void initializeCommandWindow(){
        if (!mTutorial.equals(null)) {
            Log.d(TAG,"stop Tutorial Thread");
            mTutorial.stopTutorial();
        }
        command_view.setCurrentMode(CommandWindowView.NORMAL_MODE);
        removeAllViewOnCommandWindow();
        command_window.removeAllViews();
        command_window.addView(command_view);

        implementationInitIconBox();
        command_text.setTextColor(Color.BLACK);
        command_text.setText(thisFileName);

    }


    // TouchListener

    private View.OnTouchListener KeyboardTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                if(keyboard_array.size()>0) {
                    if (keyboard_array.get(0) == '0') {
                        keyboard_array.remove(0);
                    }
                }
                switch(view.getId()){
                    case R.id.zero :
                        number[0].setImageDrawable(getResources().getDrawable(R.mipmap.zero_2));
                        keyboard_array.add('0');

                        break;

                    case R.id.one :
                        number[1].setImageDrawable(getResources().getDrawable(R.mipmap.one_2));
                        keyboard_array.add('1');
                        break;

                    case R.id.two :
                        number[2].setImageDrawable(getResources().getDrawable(R.mipmap.two_2));
                        keyboard_array.add('2');
                        break;

                    case R.id.three :
                        number[3].setImageDrawable(getResources().getDrawable(R.mipmap.three_2));
                        keyboard_array.add('3');
                        break;

                    case R.id.four :
                        number[4].setImageDrawable(getResources().getDrawable(R.mipmap.four_2));
                        keyboard_array.add('4');
                        break;

                    case R.id.five :
                        number[5].setImageDrawable(getResources().getDrawable(R.mipmap.five_2));
                        keyboard_array.add('5');
                        break;

                    case R.id.six :
                        number[6].setImageDrawable(getResources().getDrawable(R.mipmap.six_2));
                        keyboard_array.add('6');
                        break;

                    case R.id.seven :
                        number[7].setImageDrawable(getResources().getDrawable(R.mipmap.seven_2));
                        keyboard_array.add('7');
                        break;

                    case R.id.eight :
                        number[8].setImageDrawable(getResources().getDrawable(R.mipmap.eight_2));
                        keyboard_array.add('8');
                        break;

                    case R.id.nine :
                        number[9].setImageDrawable(getResources().getDrawable(R.mipmap.nine_2));
                        keyboard_array.add('9');
                        break;

                    case R.id.enter :
//                        number[10].setImageDrawable(getResources().getDrawable(R.mipmap.enter_2));
//                        char[] numberOfKeyboard = new char[keyboard_array.size()];
//                        for(int i=0 ; i<keyboard_array.size(); i++){
//                            numberOfKeyboard[i] = keyboard_array.get(i);
//                        }
//                        if(numberOfKeyboard.length != 0) {
//                            keyboard_number = new String(numberOfKeyboard);
//                        }
//                        Log.d("KEYBOARD_NUMBER",keyboard_number);
//                        int keyboard_valueToSend = Integer.parseInt(keyboard_number);
//                        if(keyboard_valueToSend > 255 || keyboard_valueToSend < 0){
//                            Toast.makeText(MainActivity.this,"0~255 까지의 값을 입력해주세요",Toast.LENGTH_SHORT).show();
//                            isKeyboard = false;
//                            implementationKeyboardBox(concentration_index);
//                        }
//                        else {
//                            command_view.updateValue(concentration_index, Integer.parseInt(keyboard_number));
//                            setting_layout.removeAllViews();
//                            mHandler.obtainMessage(ICON_ON_TOUCH_EVENT, CommandWindowView.INFLATE_CHILDREN_LAYOUT, concentration_index).sendToTarget();
//                        }

                        break;

                    case R.id.delete :
                        number[11].setImageDrawable(getResources().getDrawable(R.mipmap.delete_2));
                        if(keyboard_array.size() > 0){
                            keyboard_array.remove(keyboard_array.size()-1);
                            if(keyboard_array.size() == 0){
                                keyboard_array.add('0');
                            }
                        }
                        break;
                }
                if(keyboard_array.size()>0) {

                    char[] value_temp = new char[keyboard_array.size()];
                    for (int i = 0; i < keyboard_array.size(); i++)
                        value_temp[i] = keyboard_array.get(i);

                    keyboard_number = new String(value_temp);
                    Log.d(TAG,"keyboardNum : " + keyboard_number);
//                    if(command_view.getCommand_icon().get(concentration_index).getCommand() == Command.TIMER_MILLIS){
//                        if(Integer.parseInt(keyboard_number) > 1000){
//                            keyboard_array.remove(keyboard_array.size()-1);
//                            for (int i = 0; i < keyboard_array.size(); i++)
//                                value_temp[i] = keyboard_array.get(i);
//
//                            keyboard_number = new String(value_temp);
//                        }
//                    }
//                    else if(command_view.getCommand_icon().get(concentration_index).getCommand() >= 12
//                            && command_view.getCommand_icon().get(concentration_index).getCommand() < 19){
//                        if(Integer.parseInt(keyboard_number) > 255){
//                            keyboard_array.remove(keyboard_array.size()-1);
//                            for (int i = 0; i < keyboard_array.size(); i++)
//                                value_temp[i] = keyboard_array.get(i);
//
//                            keyboard_number = new String(value_temp);
//                        }
//                    }
//                    else if(command_view.getCommand_icon().get(concentration_index).getCommand() >= 22
//                            && command_view.getCommand_icon().get(concentration_index).getCommand() < 30){
//                        if(Integer.parseInt(keyboard_number) > 20){
//                            keyboard_array.remove(keyboard_array.size()-1);
//                            for (int i = 0; i < keyboard_array.size(); i++)
//                                value_temp[i] = keyboard_array.get(i);
//
//                            keyboard_number = new String(value_temp);
//                        }
//                    }
                }
                if(concentration_index != -1) {
                    command_view.getCommand_icon().get(concentration_index).setValue(Integer.parseInt(keyboard_number));
                    command_view.invalidate();
                }

            }
            else if(motionEvent.getAction() == MotionEvent.ACTION_UP){

                mSoundmanager.play(1);
                number[0].setImageDrawable(getResources().getDrawable(R.mipmap.zero));
                number[1].setImageDrawable(getResources().getDrawable(R.mipmap.one));
                number[2].setImageDrawable(getResources().getDrawable(R.mipmap.two));
                number[3].setImageDrawable(getResources().getDrawable(R.mipmap.three));
                number[4].setImageDrawable(getResources().getDrawable(R.mipmap.four));
                number[5].setImageDrawable(getResources().getDrawable(R.mipmap.five));
                number[6].setImageDrawable(getResources().getDrawable(R.mipmap.six));
                number[7].setImageDrawable(getResources().getDrawable(R.mipmap.seven));
                number[8].setImageDrawable(getResources().getDrawable(R.mipmap.eight));
                number[9].setImageDrawable(getResources().getDrawable(R.mipmap.nine));
                number[10].setImageDrawable(getResources().getDrawable(R.mipmap.enter));
                number[11].setImageDrawable(getResources().getDrawable(R.mipmap.delete));
            }

            return true;
        }
    };

    private AdapterView.OnItemClickListener appmenuItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            mSoundmanager.play(1);
            switch(position){
                case 0 :

                    if (command_view.getCommand_icon().size() == 1 && command_view.getCommand_icon().get(0).getCommand() == Command.START) {
                        isSavedFile = false;
                        thisFileName = mFileManagement.makeFileNameUsingData() +".drs";
                        implementationInitIconBox();
                        while (command_view.getCommand_icon().size() != 0) {
                            command_view.removeComand(0);
                        }

                        command_text.setText(String.valueOf(thisFileName));
                        command_text.setTextSize(20);
                        command_text.setTextColor(Color.BLACK);

                        command_view.addCommand(Command.START);
                        command_view.invalidate();
                    }
                    else {
                        if (!mFileManagement.isFileExist(thisFileName)) {
                            AlertDialog.Builder saveReqeustDialog = new AlertDialog.Builder(MainActivity.this);
                            LinearLayout title_layout = new LinearLayout(MainActivity.this);
                            title_layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            title_layout.setGravity(View.TEXT_ALIGNMENT_CENTER);

                            title_layout = (LinearLayout) View.inflate(MainActivity.this, R.layout.saverequest_topic, null);
                            TextView topic = (TextView) title_layout.findViewById(R.id.request_topic);
                            topic.setText("저장되지 않은 파일입니다. 저장하시겠습니까?");

                            saveReqeustDialog.setCustomTitle(title_layout);
                            saveReqeustDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    if (!mFileManagement.isFileExist(thisFileName)) {
                                        implementationSaveAsBox();
                                    } else {
                                        Log.d(getClass().getSimpleName(), "저장된 파일 -> name : " + thisFileName);
                                        mFileManagement.writeCommandOnFile(thisFileName, command_view.getCommand_icon());
                                    }

                                }
                            });

                            saveReqeustDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    isSavedFile = false;
                                    thisFileName = mFileManagement.makeFileNameUsingData() +".drs";
                                    implementationInitIconBox();
                                    while (command_view.getCommand_icon().size() != 0) {
                                        command_view.removeComand(0);
                                    }

                                    command_text.setText(thisFileName);
                                    command_text.setTextSize(20);
                                    command_text.setTextColor(Color.BLACK);

                                    command_view.addCommand(Command.START);
                                    command_view.invalidate();
                                }
                            });
                            saveReqeustDialog.create();
                            saveReqeustDialog.show();

                        } else { // already Saved with such name
                            Log.d("file_check", "file_Check");

                            if(mFileManagement.isCommandSavedinThisFile(thisFileName,command_view.getCommand_icon())){
                                Log.d(TAG,"not changed");
                                isSavedFile = false;
                                thisFileName = mFileManagement.makeFileNameUsingData() +".drs";
                                implementationInitIconBox();
                                while (command_view.getCommand_icon().size() != 0) {
                                    command_view.removeComand(0);
                                }

                                command_text.setText(thisFileName);
                                command_text.setTextSize(20);
                                command_text.setTextColor(Color.BLACK);

                                command_view.addCommand(Command.START);
                                command_view.invalidate();
                            }
                            else{
                                Log.d(TAG,"changed");
                                mFileManagement.writeCommandOnFile(thisFileName, command_view.getCommand_icon());
                                isSavedFile = false;
                                thisFileName = mFileManagement.makeFileNameUsingData() +".drs";
                                implementationInitIconBox();
                                while (command_view.getCommand_icon().size() != 0) {
                                    command_view.removeComand(0);
                                }

                                command_text.setText(thisFileName);
                                command_text.setTextSize(20);
                                command_text.setTextColor(Color.BLACK);

                                command_view.addCommand(Command.START);
                                command_view.invalidate();

                            }

                        }
                    }

                    break;

                case 1 :
                    if(command_view.getCommand_icon().size() > 0 ) {
                        if (!isSavedFile) {
                            implementationSaveAsBox();
                            Log.d(getClass().getSimpleName(), "저장되지 않은 파일");
                        } else {
                            Log.d(getClass().getSimpleName(), "저장된 파일 -> name : " + thisFileName);
                            mFileManagement.writeCommandOnFile(thisFileName, command_view.getCommand_icon());
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"저장할 내용이 없습니다.",Toast.LENGTH_SHORT).show();
                    }
                    break;

                case 2 :
                    implementationSaveAsBox();
                    break;

                case 3 :
                    implementationOpenBox();
                    break;

                case 4 :
                    if(mFileManagement.readBTAddress() != null) {
                        Intent play_intent = new Intent(getApplicationContext(), ArduinoPlayActivity.class);
                        play_intent.putExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS, mFileManagement.readBTAddress());
                        startActivity(play_intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"블루투스 기기를 선택해주세요.",Toast.LENGTH_SHORT).show();
                    }

                    break;

                case 5 :
                    if(mFileManagement.readBTAddress() != null) {
                        if(mFileManagement.isFileExist(thisFileName)){
                            isfinishedWrite  = false;
                            mFileManagement.writeCommandOnFile(thisFileName,command_view.getCommand_icon());
                            Log.d(TAG,"This file is exist");
                            check = new CommandCheck(MainActivity.this,command_view.getCommand_icon(),mHandler);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while(!isfinishedWrite){
                                        try{
                                            Thread.sleep(2);
                                        }catch (InterruptedException e){};
                                    }
                                    check.startCheckCommand();
                                }
                            }).start();
                        }
                        else{
                            Log.d(TAG,"this file is not exist");
                            isRequestUpload = true;

                            implementationSaveAsBox();
                        }

                    }
                    else{
                        Toast.makeText(MainActivity.this,"블루투스 기기를 선택해주세요",Toast.LENGTH_SHORT).show();
                    }

                    break;

                case 6 :
                    implementationBTSettting();
                    break;

                case 7 :
                    Toast.makeText(getApplicationContext(),"튜토리얼을 준비중 입니다.",Toast.LENGTH_SHORT).show();
//                    command_window.removeAllViews();
//                    removeAllViewOnCommandWindow();
//                    command_text.setText("");
//                    mTutorial = new Tutorial(MainActivity.this,MainActivity.this, mHandler);
//                    appmenu_open.setImageDrawable(getResources().getDrawable(R.mipmap.cancel));
//                    command_view.setCurrentMode(CommandWindowView.TUTORIAL_MODE);
                    break;
            }
//            ismenu_open = false;
        }

    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult" + resultCode);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode != Activity.RESULT_OK) {
                    mBluetoothService.scanDevice();
                } else {//cancel button
                    Log.d(TAG, "Bluetooth is not enable");
                }
                break;

            case REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
//                    mBluetoothService.getDeviceInfo(data);
                    mFileManagement.writeBtAddressOnFile(data.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS));
                    command_text.setText("address : " + data.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS));
                }
                break;

            case REQUEST_TUTORIAL_ACTIVITY:
                if (resultCode == 0) {
                    mTutorial.display_IntroView();
                }
                break;

            case REQUEST_TUTORIAL_FROM_INTRO:
                if (resultCode == 1) {
                    command_window.removeAllViews();
                    removeAllViewOnCommandWindow();
                    command_text.setText("");
                    mTutorial = new Tutorial(MainActivity.this, MainActivity.this, mHandler);
                    appmenu_open.setImageDrawable(getResources().getDrawable(R.mipmap.cancel));
                    command_view.setCurrentMode(CommandWindowView.TUTORIAL_MODE);
                }

                break;

            case REQUEST_UPLOAD_ACTIVITY:
                Log.w(TAG,"request Code" + "REQUEST_UPLOAD_ACTIVITY / " + "resultCode = " + resultCode);
                if (resultCode == 1) {
                    Log.d(TAG,"finished checking Command" + true);
                    ArrayList<Integer> command_data = new ArrayList<Integer>();
                    for (int i = 0; i < command_view.getCommand_icon().size(); i++) {
                        command_data.add(command_view.getCommand_icon().get(i).getCommand());
                        command_data.add(command_view.getCommand_icon().get(i).getValue());
                        command_data.add(command_view.getCommand_icon().get(i).getValue2());
                    }

                    UploadManager mUploadManager = new UploadManager(MainActivity.this, command_data,mFileManagement.readBTAddress());

                }
                else if(resultCode == 0){ // Bad
                    Log.d(TAG,"finished checking Command" + false);
                    Toast.makeText(MainActivity.this,"명령어 오류",Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"Error : " + check.getErrorlist().size());
                    command_view.setCurrentMode(CommandWindowView.ERROR_MODE);
                    command_view.setErrorlist(check.getErrorlist());
                    command_view.invalidate();
                    command_text.setTextColor(Color.RED);
                    command_text.setText("돌아가시려면 여기를 눌러주세요.");
//                    left_menu_layout.setVisibility(View.GONE);
                    removeAllViewOnCommandWindow();
//                    implementationErrorListBox();
                    command_text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mSoundmanager.play(0);
                            command_view.setCurrentMode(CommandWindowView.NORMAL_MODE);
                            command_text.setTextColor(Color.BLACK);
                            command_text.setText(thisFileName);
                            command_text.setOnClickListener(null);
                            command_view.invalidate();
                            left_menu_layout.setVisibility(View.VISIBLE);
                            implementationInitIconBox();
                        }
                    });
                    removeAllViewOnCommandWindow();
                    Log.w(TAG,"implementation Error Box");
                    check.implementationErrorBox();

                    ismenu_open = false;
                }
                else if(resultCode == -1){
                    Log.d(TAG,"Failed to check command data");
                }
                break;
        }
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        AudioManager mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN )
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI);

        else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP )
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);

        else if(keyCode == KeyEvent.KEYCODE_BACK){

                ExitDialog exitDialog = new ExitDialog(MainActivity.this);

        }
        return true;
    }


    private void implementationBTSettting(){
        removeAllViewOnCommandWindow();
        String address = mFileManagement.readBTAddress();
        if(address != null && address != ""){
            command_text.setText("BT address : " + address);
        }
        else{
            command_text.setText("장치가 선택되지 않았습니다.");
        }
        inflater.inflate(R.layout.selected_item,left_menu_layout,true);
        ismenu_open = true;
        appmenu_open.setImageDrawable(getResources().getDrawable(R.mipmap.appmenu_on));

        ListView bt_list = (ListView)MainActivity.this.findViewById(R.id.motor_box);
        final ImageView bt_back = (ImageView)MainActivity.this.findViewById(R.id.motor_back);

        CustomAdapter1 mAdapter = new CustomAdapter1(MainActivity.this);

        mAdapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.appmenu_bt),"나의\n블루투스"));
        mAdapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.appmenu_bt),"통신 Test"));
        mAdapter.addItem(new Custom1_Item(getResources().getDrawable(R.mipmap.appmenu_bt),"초기화"));

        bt_list.setAdapter(mAdapter);

        bt_back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    bt_back.setImageDrawable(getResources().getDrawable(R.drawable.box_back_on));
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    mSoundmanager.play(1);
                    try{
                        Thread.sleep(25);
                    }catch (InterruptedException e){};
                    bt_back.setImageDrawable(getResources().getDrawable(R.drawable.box_back));
                    command_text.setText(thisFileName);

                    implementationAppMenuBox();
                }
                return true;
            }
        });
        Log.d(TAG,String.valueOf(ismenu_open));

        bt_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch(position){
                    case 0 :
                        mSoundmanager.play(1);
                        if (mBluetoothService.getDeviceState()) {
                            mBluetoothService.setReadRunning(true);
                            mBluetoothService.enableBluetooth();
                        } else {
                            finish();
                        }
                        break;

                    case 1:
                        if(mFileManagement.readBTAddress() != null && mFileManagement.readBTAddress() != "") {
                            mSoundmanager.play(1);
                            isRequestTest = true;
                            Intent Btcheck = new Intent(MainActivity.this, BTCheckActivity.class);
                            Btcheck.putExtra("BT", mFileManagement.readBTAddress());
                            startActivityForResult(Btcheck, REQUEST_BT_CHECK);
                            overridePendingTransition(R.anim.fade, R.anim.hold);
                        }
                        else{
                            Toast.makeText(MainActivity.this,"나의 블루투스 기기를 선택해주세요",Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 2 :
                        mSoundmanager.play(1);
                        mFileManagement.writeBtAddressOnFile("");
                        Toast.makeText(MainActivity.this,"나의 블루투스 기기가 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                        command_text.setText("장치가 선택되지 않았습니다.");
                        break;

                }
            }
        });

    }

    private class ExitDialog{
        private AlertDialog.Builder exitDialog ;

        public ExitDialog(Context context) {
            ConstraintLayout exitView = (ConstraintLayout)View.inflate(context,R.layout.exitlayout,null);
            final ImageView cancel = (ImageView)exitView.findViewById(R.id.cancel);
            final ImageView ok = (ImageView)exitView.findViewById(R.id.ok);

            exitDialog = new AlertDialog.Builder(context);
            exitDialog.setCancelable(false);
            exitDialog.setView(exitView);
            final AlertDialog dialog = exitDialog.create();
            dialog.show();
            cancel.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view,MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        cancel.setImageDrawable(getResources().getDrawable(R.mipmap.dialog_cancel_on));
                    }
                    else if(event.getAction() == MotionEvent.ACTION_UP){
                        mSoundmanager.play(0);

                        cancel.setImageDrawable(getResources().getDrawable(R.mipmap.cancel));
                        dialog.dismiss();
                    }
                    return true;
                }
            });

            ok.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        ok.setImageDrawable(getResources().getDrawable(R.mipmap.dialog_ok));
                    }
                    else if(event.getAction() == MotionEvent.ACTION_UP){
                        mSoundmanager.play(0);

                        ok.setImageDrawable(getResources().getDrawable(R.mipmap.dialog_ok_ok));
                        if (command_view.getCommand_icon().size() == 1 && command_view.getCommand_icon().get(0).getCommand() == Command.START) {
                            if (mBluetoothService.getState() == BluetoothService.STATE_CONNECTED) {
                                mBluetoothService.stop();
                            }
                            mFileManagement.removeTempFile();
                            finish();
                        }
                        else {
                            if (!mFileManagement.isFileExist(thisFileName)) {
                                AlertDialog.Builder saveReqeustDialog = new AlertDialog.Builder(MainActivity.this);
                                LinearLayout title_layout = new LinearLayout(MainActivity.this);
                                title_layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                title_layout.setGravity(View.TEXT_ALIGNMENT_CENTER);

                                title_layout = (LinearLayout) View.inflate(MainActivity.this, R.layout.saverequest_topic, null);
                                TextView topic = (TextView) title_layout.findViewById(R.id.request_topic);
                                topic.setText("저장되지 않은 파일입니다. 저장하시겠습니까?");


                                saveReqeustDialog.setCustomTitle(title_layout);
                                saveReqeustDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        if (!mFileManagement.isFileExist(thisFileName)) {
                                            isRequestFinish = true;
                                            implementationSaveAsBox();
                                        } else {
                                            Log.d(getClass().getSimpleName(), "저장된 파일 -> name : " + thisFileName);
                                            mFileManagement.writeCommandOnFile(thisFileName, command_view.getCommand_icon());
                                        }

                                    }
                                });

                                saveReqeustDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        mFileManagement.removeTempFile();
                                        finish();
                                    }
                                });

                                saveReqeustDialog.create();
                                saveReqeustDialog.show();
                            } else { // already Saved with such name
                                Log.d("file_check", "file_Check");
                                if(mFileManagement.isCommandSavedinThisFile(thisFileName,command_view.getCommand_icon())){
                                    Log.d(TAG,"file_not changed");

                                    finish();
                                }
                                else{
                                    Log.d(TAG,"file_changed");
                                    mFileManagement.writeCommandOnFile(thisFileName, command_view.getCommand_icon());
                                    finish();
                                }
                                mFileManagement.removeTempFile();
                            }
                        }
                    }
                    return true;
                }
            });
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        if(mFileManagement.isFileExist(thisFileName)) {
            if (mFileManagement.isCommandSavedinTempFile(mFileManagement.readCommandFromFile(thisFileName))) {
                Log.d(TAG, "TempFile and command is equals");
                mFileManagement.removeTempFile();
            } else {
                Log.d(TAG, "is not equal");
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
