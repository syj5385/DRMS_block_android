package jjun.jjunapp.programdrs.Tutorial;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import jjun.jjunapp.programdrs.ArduinoPlay.ArduinoPlayActivity;
import jjun.jjunapp.programdrs.ArduinoPlay.ArduinoPlayManager;
import jjun.jjunapp.programdrs.Command.Command;
import jjun.jjunapp.programdrs.Communication.BluetoothService;
import jjun.jjunapp.programdrs.Communication.DeviceListActivity;
import jjun.jjunapp.programdrs.CustomAdapter.CustomAdapter1.Custom1_Item;
import jjun.jjunapp.programdrs.CustomAdapter.CustomAdapter1.CustomAdapter1;
import jjun.jjunapp.programdrs.FileManagement.FileManagement;
import jjun.jjunapp.programdrs.R;
import jjun.jjunapp.programdrs.Sound.SoundManager;
import jjun.jjunapp.programdrs.Tutorial.TutorialView.TutorialWindowView;

public class TutorialCommandActivity extends AppCompatActivity {

    // Bluetooth MACRO
    private static final String TAG = TutorialCommandActivity.class.getClass().getSimpleName();

    // Bluetooth Variables
    private static final int MODE_REQUEST = 1;
    public static final boolean D = true;

    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    public static final int REQUEST_TUTORIAL0_0 = 3;
    public static final int REQUEST_TUTORIAL0_1 = 4;
    public static final int REQUEST_TUTORIAL0_2 = 5;
    public static final int REQUEST_TUTORIAL1_0 = 6;
    public static final int REQUEST_TUTORIAL1_1 = 7;

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

    private int currentMode = NORMAL_MODE;

    public static final int NORMAL_MODE = -1;
    public static final int TUTORIAL0 = 0;
    public static final int TUTORIAL1 = 1;
    public static final int TUTORIAL1_0 = 10;
    public static final int TUTORIAL2 = 2;

    public static final int STAGE_OVER = 0;

    // Object
    private Handler mHandler = new MyHandler();
    private TextToSpeech myTTS;
    private LayoutInflater inflater;

    private CustomAdapter1 motor_adapter = new CustomAdapter1(this);
    private CustomAdapter1 timer_adapter = new CustomAdapter1(this);
    private CustomAdapter1 jump_adapter = new CustomAdapter1(this);
    private CustomAdapter1 selectionBoxAdapter = new CustomAdapter1(this);
    private TutorialWindowView command_view;  // main view / drawing Icon and Making ArrayList to manage data list
    private FileManagement mFileManagement;
    private BluetoothService mBluetoothService = null;
//    private ArrayList<CommandIcon> copy_command = new ArrayList<CommandIcon>();
    private SoundManager mSoundmanager;
    private ArduinoPlayManager mArduinoPlayManager;
    private Tutorial mTutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initialize_UserViews();

        int tutorial_level = getIntent().getIntExtra("TUTORIAL",0);
        Log.d(TAG,"Tutorial : " + String.valueOf(tutorial_level));
        Intent transparent_intent;
        switch(tutorial_level){
            case 0 :
                transparent_intent = new Intent(this,TransparentActivity.class);
                transparent_intent.putExtra("TUTORIAL",REQUEST_TUTORIAL0_0);
                startActivityForResult(transparent_intent,REQUEST_TUTORIAL0_0 );
                break;

            case 1 :
                transparent_intent = new Intent(this,TransparentActivity.class);
                transparent_intent.putExtra("TUTORIAL",REQUEST_TUTORIAL1_0);
                startActivityForResult(transparent_intent,REQUEST_TUTORIAL1_0 );
                break;

            default :
                transparent_intent = new Intent(this,TransparentActivity.class);
                break;
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
                if(currentMode == NORMAL_MODE) {
//                    startActivity(new Intent(getApplicationContext(),TransparentActivity.class));
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
                        mSoundmanager.play(0);
                        implementationInitIconBox();
                    }
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
                float layout_width = command_window.getWidth();
                float layout_height = command_window.getHeight();
                mHandler.obtainMessage(COMMAND_LAYOUT_SIZE, (int) layout_width, (int) layout_height).sendToTarget();
                if(command_view == null) {
                    command_view = new TutorialWindowView(TutorialCommandActivity.this,TutorialCommandActivity.this, mHandler, iconX_position, iconY_position);
                    command_view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 8000));
                    command_window.addView(command_view);

//                    command_view.addCommand(Command.START);
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

        command_text = new TextView(TutorialCommandActivity.this);
        command_text.setText("Command   - " + String.valueOf(thisFileName));
        command_text.setTextSize(20);
        command_text.setGravity(View.TEXT_ALIGNMENT_CENTER);
        command_text.setTextColor(Color.BLACK);

        appmenu.addView(command_text);

        mFileManagement = new FileManagement(this,mHandler);
        thisFileName = "Tutorial";
        mBluetoothService = new BluetoothService(TutorialCommandActivity.this,mHandler,"CodingDRS");

        mSoundmanager = new SoundManager(this);
        mArduinoPlayManager = new ArduinoPlayManager(this,this,mHandler);

        inflater = getLayoutInflater();


        new Thread(new Runnable() {
            @Override
            public void run() {
                while(command_view == null){
                    try{
                        Thread.sleep(3);
                    } catch (InterruptedException e){};
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


    }

    // Handler Object to Receive Message
    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
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
                    switch(msg.arg1){
                        case TutorialWindowView.INFLATE_CHILDREN_LAYOUT:
                            mSoundmanager.play(1);
                            removeAllViewOnCommandWindow();

                            bottom_setting.setVisibility(View.VISIBLE);


                            concentration_index = msg.arg2;
                            Log.w("Concentration_index","Concentration Index = " + String.valueOf(concentration_index));

                            int command_temp = command_view.getCommand_icon().get(msg.arg2).getCommand();
                            if((command_temp >= Command.MOTOR3 && command_temp <= Command.STOP)){
                                ImplementationMotorSetting(concentration_index); // implementation Motor_setting Layout
                            }
                            else if(command_temp == Command.TIMER_MILLIS || command_temp == Command.TIMER_SEC){

                                ImplementationTimerSetting(concentration_index); // implementation timer_setting Layout
                            }
                            else if(command_temp == Command.START){
//                                inflater.inflate(R.layout.timersetting,setting_layout,true);
//                                ImplementationTimerSetting(concentration_index);
                            }

                            else if(command_temp == Command.END){

                            }

                            else if(command_temp >= Command.JUMP1_UP && command_temp <= Command.JUMP4_DOWN){

                            }

                            create_menu_and_update(concentration_index);
                            command_view.invalidate();
                            break;

                        case TutorialWindowView.TOUCH_NO_ICON :
                            if(currentMode == NORMAL_MODE) {
                                mSoundmanager.play(1);

                                bottom_setting.setVisibility(View.GONE);
                                for (int i = 0; i < command_view.getCommand_icon().size(); i++)
                                    command_view.getCommand_icon().get(i).icon_unchecked();


                                Log.d("COMMANDVIEW", "NOTOUCH");

                                command_view.invalidate();
                                implementationInitIconBox();
                            }
                            break;
                    }

                    break;

                case KEYBOARD_VALUE :
                    final String value = (String)msg.obj;
                    final int index = msg.arg1;

                    keyboard_value.setText(value);

                    break;
//
//                case MOTOR_VALUE_UPDATE :
//                    EditText edit = (EditText)msg.obj;
//                    edit.setText(String.valueOf(msg.arg1));
//                    break;
//
                case FILEMANAGEMENT :
                    switch(msg.arg1){
                        case FileManagement.FINISHED_WRITE_DATA :

                            break;

                        case FileManagement.FINISHED_READ_DATA:

                            int[] Command = (int[])msg.obj;

                            command_view.removeAllCommand();
                            command_view.setIndex_X(0);
                            command_view.setIndex_Y(0);

                            int index_temp = 0;

                            for(int i=0 ; i <Command.length/2 ; i++){
                                Log.d("DATA", String.valueOf(Command[i]));
                                command_view.addCommand(Command[index_temp++],Command[index_temp++],0);
                            }

                            break;
                    }
                    break;

                case MESSAGE_STATE_CHANGE:
                    if (D) Log.i(TAG, "MESSAGE_STATE_CHANGE:" + msg.arg1);

                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Toast.makeText(getApplicationContext(), "블루투스 연결에 성공하였습니다.\nDevice : "
                                    + mBluetoothService.address1, Toast.LENGTH_SHORT).show();
                            mArduinoPlayManager.bluetoothConnected(mFileManagement.readBTAddress());
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
                    command_text.setText("Tutorial");
                    command_text.setTextColor(getResources().getColor(R.color.lineColor));
                    command_text.setTextSize(20);

                    removeAllViewOnCommandWindow();
                    implementationInitIconBox();
                    command_window.removeAllViews();
                    command_window.addView(command_view);

                    try{
                        Thread.sleep(100);
                    }catch (InterruptedException e){}


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

        ListView appmenu_list = (ListView)TutorialCommandActivity.this.findViewById(R.id.file_list);
        CustomAdapter1 mAdapter = new CustomAdapter1(TutorialCommandActivity.this);
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
        final ImageView copy_icon = (ImageView) TutorialCommandActivity.this.findViewById(R.id.copy_icon);
        final ImageView onview_icon = (ImageView) TutorialCommandActivity.this.findViewById(R.id.onview);

        icon_box_list.setAdapter(selectionBoxAdapter);
        icon_box_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mSoundmanager.play(1);
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
                        if(currentMode == NORMAL_MODE) {
                            command_view.addCommand(Command.START);
                        }
                        else if(currentMode == TUTORIAL0){

                        }
                        break;

                    case 4:
                        if(currentMode == NORMAL_MODE) {
                            command_view.addCommand(Command.END, 0,0);
                        }
                        else{

                        }
                        break;
                }
            }
        });

        copy_icon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(currentMode == NORMAL_MODE) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        mSoundmanager.play(1);
                        if (command_view.getCurrentMode() == TutorialWindowView.NORMAL_MODE) {
                            command_view.getViewHandler().obtainMessage(CURRENT_MODE, TutorialWindowView.COPY_MODE, -1).sendToTarget();
                            command_text.setText("복사할 항목을 선택하여 붙여넣기를 누르세요");
                            command_text.setTextSize(20);
                            command_text.setTextColor(getResources().getColor(R.color.lineColor));
//                        onview_icon.setVisibility(View.VISIBLE);
//                        copy_icon.setVisibility(View.GONE);


                        } else if (command_view.getCurrentMode() == TutorialWindowView.COPY_MODE) {
                            command_view.getViewHandler().obtainMessage(CURRENT_MODE, TutorialWindowView.NORMAL_MODE, -1).sendToTarget();
                            command_text.setText("Command   - " + String.valueOf(thisFileName));
                            command_text.setTextSize(20);
                            command_text.setTextColor(Color.BLACK);
                            for (int i = 0; i < command_view.getCommand_icon().size(); i++) {
                                command_view.getCommand_icon().get(i).icon_unchecked();
                            }
//                        onview_icon.setVisibility(View.GONE);
//                        copy_icon.setVisibility(View.VISIBLE);

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
                if(currentMode == NORMAL_MODE) {
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
                        command_view.getViewHandler().obtainMessage(CURRENT_MODE, TutorialWindowView.NORMAL_MODE, -1).sendToTarget();
                        command_text.setText("Command   - " + String.valueOf(thisFileName));
                        command_text.setTextSize(20);
                        command_text.setTextColor(Color.BLACK);
                        for (int i = 0; i < command_view.getCommand_icon().size(); i++) {
                            command_view.getCommand_icon().get(i).icon_unchecked();
                        }
                        command_view.invalidate();
                    }
                }
                return true;
            }
        });
    }

    private void implementationMotorBox(){
        removeAllViewOnCommandWindow();

        inflater.inflate(R.layout.selected_item,left_menu_layout,true);
        final ImageView motor_back = (ImageView)TutorialCommandActivity.this.findViewById(R.id.motor_back);
        motor_back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    motor_back.setImageDrawable(getResources().getDrawable(R.drawable.box_back_on));
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    mSoundmanager.play(1);
                    try{
                        Thread.sleep(25);
                    }catch (InterruptedException e){};
                    motor_back.setImageDrawable(getResources().getDrawable(R.drawable.box_back));

                    implementationInitIconBox();
                }
                return true;
            }
        });

        ListView motor_box = (ListView)TutorialCommandActivity.this.findViewById(R.id.motor_box);
        motor_box.setAdapter(motor_adapter);
        motor_box.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mSoundmanager.play(1);
                if(currentMode == NORMAL_MODE) {
                    switch (i) {
                        case 0:
                            command_view.addCommand(Command.MOTOR3);
                            break;

                        case 1:
                            command_view.addCommand(Command.MOTOR5);
                            break;

                        case 2:
                            command_view.addCommand(Command.MOTOR6);
                            break;

                        case 3:
                            command_view.addCommand(Command.MOTOR9);
                            break;

                        case 4:
                            command_view.addCommand(Command.MOTOR10);
                            break;

                        case 5:
                            command_view.addCommand(Command.MOTOR11);
                            break;

                        case 6:
                            command_view.addCommand(Command.ALLMOTOR);
                            break;

                        case 7:
                            command_view.addCommand(Command.STOP);
                            break;
                    }
                }
                else if(currentMode == TUTORIAL0){

                }
            }
        });

    }

    private void implementationTimerBox(){
        removeAllViewOnCommandWindow();

        inflater.inflate(R.layout.selected_item,left_menu_layout,true);

        final ImageView timer_back = (ImageView)TutorialCommandActivity.this.findViewById(R.id.motor_back);
        timer_back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    timer_back.setImageDrawable(getResources().getDrawable(R.mipmap.box_back_on));
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    mSoundmanager.play(1);
                    try{
                        Thread.sleep(25);
                    }catch (InterruptedException e){};
                    timer_back.setImageDrawable(getResources().getDrawable(R.mipmap.box_back));
                    implementationInitIconBox();
                }
                return true;
            }
        });

        ListView timer_box = (ListView)TutorialCommandActivity.this.findViewById(R.id.motor_box);
        timer_box.setAdapter(timer_adapter);
        timer_box.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mSoundmanager.play(1);
                switch(i){
                    case 0 :
                        if(currentMode == NORMAL_MODE) {
                            command_view.addCommand(Command.TIMER_SEC);
                        }
                        else if(currentMode == TUTORIAL0){

                        }
                        break;

                    case 1:
                        if(currentMode == NORMAL_MODE) {
                            command_view.addCommand(Command.TIMER_MILLIS);
                        }
                        else if(currentMode == TUTORIAL0){

                        }
                        break;
                }
            }
        });
    }

    public void implementationJumpBox(){
        removeAllViewOnCommandWindow();

        inflater.inflate(R.layout.selected_item,left_menu_layout,true);

        final ImageView motor_back = (ImageView)TutorialCommandActivity.this.findViewById(R.id.motor_back);
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

        ListView motor_box = (ListView)TutorialCommandActivity.this.findViewById(R.id.motor_box);
        motor_box.setAdapter(jump_adapter);
        motor_box.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mSoundmanager.play(1);
                if(currentMode == NORMAL_MODE) {
                    switch (i) {
                        case 0:
                            command_view.addCommand(Command.JUMP1_UP);
                            break;

                        case 1:
                            command_view.addCommand(Command.JUMP1_DOWN);
                            break;

                        case 2:
                            command_view.addCommand(Command.JUMP2_UP);
                            break;

                        case 3:
                            command_view.addCommand(Command.JUMP2_DOWN);
                            break;

                        case 4:
                            command_view.addCommand(Command.JUMP3_UP);
                            break;

                        case 5:
                            command_view.addCommand(Command.JUMP3_DOWN);
                            break;

                        case 6:
                            command_view.addCommand(Command.JUMP4_UP);
                            break;

                        case 7:
                            command_view.addCommand(Command.JUMP4_DOWN);
                            break;
                    }
                }
                else if(currentMode == TUTORIAL0){

                }
            }
        });

    }

    void ImplementationMotorSetting(final int index){
        removeAllViewOnCommandWindow();
        bottom_setting.setVisibility(View.VISIBLE);
        inflater.inflate(R.layout.motorsetting,setting_layout,true);

        ImageView M_image = (ImageView)TutorialCommandActivity.this.findViewById(R.id.motor_image);
        final EditText motor_speed = (EditText)TutorialCommandActivity.this.findViewById(R.id.motor_speed);
        final SeekBar motor_seekbar = (SeekBar)TutorialCommandActivity.this.findViewById(R.id.motor_seekbar);

        Log.d("SPEED",String.valueOf(command_view.getCommand_icon().get(index).getValue()));
        motor_speed.setText(String.valueOf(command_view.getCommand_icon().get(index).getValue()));
        motor_seekbar.setProgress(command_view.getCommand_icon().get(index).getValue());

        switch(command_view.getCommand_icon().get(index).getCommand()){
            case Command.MOTOR3 :
                M_image.setImageDrawable(getResources().getDrawable(R.mipmap.motor3));
                break;

            case Command.MOTOR5 :
                M_image.setImageDrawable(getResources().getDrawable(R.mipmap.motor5));
                break;

            case Command.MOTOR6 :
                M_image.setImageDrawable(getResources().getDrawable(R.mipmap.motor6));
                break;

            case Command.MOTOR9 :
                M_image.setImageDrawable(getResources().getDrawable(R.mipmap.motor9));
                break;

            case Command.MOTOR10 :
                M_image.setImageDrawable(getResources().getDrawable(R.mipmap.motor10));
                break;

            case Command.MOTOR11 :
                M_image.setImageDrawable(getResources().getDrawable(R.mipmap.motor11));
                break;
        }


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

        ImageView timer_image = (ImageView)TutorialCommandActivity.this.findViewById(R.id.timer_image);
        final EditText delay_time = (EditText)TutorialCommandActivity.this.findViewById(R.id.delay_time);
        delay_time.setText(String.valueOf(command_view.getCommand_icon().get(index).getValue()));
        timer_image.setImageDrawable(getResources().getDrawable(R.mipmap.timer));

        final int value = Integer.parseInt(delay_time.getText().toString());
        Log.d("TIMER",String.valueOf(value));

        implementationKeyboardBox(index);

    }

    //Implementation Keyboard Box
    private void implementationKeyboardBox(final int index){

        Log.d("ARRAY","size : " + String.valueOf(keyboard_array.size()));

        inflater.inflate(R.layout.keyboard_layout,left_menu_layout,true);

        keyboard_value = (EditText)findViewById(R.id.keyboard_value);

        number[0] = (ImageView)TutorialCommandActivity.this.findViewById(R.id.zero);
        number[1] = (ImageView)TutorialCommandActivity.this.findViewById(R.id.one);
        number[2] = (ImageView)TutorialCommandActivity.this.findViewById(R.id.two);
        number[3] = (ImageView)TutorialCommandActivity.this.findViewById(R.id.three);
        number[4] = (ImageView)TutorialCommandActivity.this.findViewById(R.id.four);
        number[5] = (ImageView)TutorialCommandActivity.this.findViewById(R.id.five);
        number[6] = (ImageView)TutorialCommandActivity.this.findViewById(R.id.six);
        number[7] = (ImageView)TutorialCommandActivity.this.findViewById(R.id.seven);
        number[8] = (ImageView)TutorialCommandActivity.this.findViewById(R.id.eight);
        number[9] = (ImageView)TutorialCommandActivity.this.findViewById(R.id.nine);
        number[10] = (ImageView)TutorialCommandActivity.this.findViewById(R.id.enter);
        number[11] = (ImageView)TutorialCommandActivity.this.findViewById(R.id.delete);

        for(int i=0; i<number.length ; i++)
            number[i].setOnTouchListener(KeyboardTouchListener);

        isKeyboard = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(keyboard_array.size() != 0){
                    keyboard_array.remove(0);
                }
                while(isKeyboard){
                    String keyboard_number = "0";

                    if(keyboard_array.size()>0) {
                        char[] value_temp = new char[keyboard_array.size()];
                        for (int i = 0; i < keyboard_array.size(); i++)
                            value_temp[i] = keyboard_array.get(i);

                        keyboard_number = new String(value_temp);
                    }

                    mHandler.obtainMessage(KEYBOARD_VALUE,index,-1,keyboard_number).sendToTarget();
                    try{
                        Thread.sleep(50);
                    }catch (InterruptedException e){};

                }
            }
        }).start();

    }

    private void create_menu_and_update(final int index){

        inflater.inflate(R.layout.menulayout,menu_layout,true);
        final ImageView remove = (ImageView) TutorialCommandActivity.this.findViewById(R.id.remove);
        final ImageView move_left = (ImageView)TutorialCommandActivity.this.findViewById(R.id.move_left);
        final ImageView move_right = (ImageView)TutorialCommandActivity.this.findViewById(R.id.move_right);
        final ImageView settingSave = (ImageView)TutorialCommandActivity.this.findViewById(R.id.motor_save) ;

        remove.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    remove.setImageDrawable(getResources().getDrawable(R.mipmap.remove_on));
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
//                    mSoundmanager.play(1);
                    try {
                        Thread.sleep(20);
                    }catch (InterruptedException e){};

                    remove.setImageDrawable(getResources().getDrawable(R.mipmap.remove));

                    command_view.removeComand(index);
                    if(index == command_view.getCommand_icon().size()){
                        command_view.setPrev_concentration_index(-1);
                        implementationInitIconBox();
                    }
                    else {
                        command_view.getCommand_icon().get(index).icon_checked();
                        mHandler.obtainMessage(ICON_ON_TOUCH_EVENT, TutorialWindowView.INFLATE_CHILDREN_LAYOUT, index).sendToTarget();
                    }

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
//                    mSoundmanager.play(1);

                    move_left.setImageDrawable(getResources().getDrawable(R.mipmap.move_left));

                    command_view.getCommand_icon().get(index).icon_unchecked();
                    command_view.moveLeft(index);

                    if(index >0) {
                        command_view.getCommand_icon().get(index - 1).icon_checked();
                        mHandler.obtainMessage(ICON_ON_TOUCH_EVENT, TutorialWindowView.INFLATE_CHILDREN_LAYOUT, index-1).sendToTarget();
                    }
                    else{
                        command_view.getCommand_icon().get(index).icon_unchecked();
                        implementationInitIconBox();
                    }


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
//                    mSoundmanager.play(1);

                    move_right.setImageDrawable(getResources().getDrawable(R.mipmap.move_right));

                    command_view.getCommand_icon().get(index).icon_unchecked();
                    command_view.moveRight(index);

                    if(index < command_view.getCommand_icon().size()-1) {
                        command_view.getCommand_icon().get(index + 1).icon_checked();
                        mHandler.obtainMessage(ICON_ON_TOUCH_EVENT, TutorialWindowView.INFLATE_CHILDREN_LAYOUT, index+1).sendToTarget();
                    }
                    else{
                        command_view.getCommand_icon().get(index).icon_unchecked();
                        implementationInitIconBox();
                    }
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

                }
                return true;
            }
        });

    }

    private void implementationSaveAsBox(){
        AlertDialog.Builder save_dialog = new AlertDialog.Builder(this);
        save_dialog.setTitle("Save As");
        final LinearLayout save_layout = (LinearLayout)View.inflate(this,R.layout.saveaslayout,null);
        save_dialog.setView(save_layout);
        EditText nameValue = (EditText)save_layout.findViewById(R.id.namevalue);
        save_dialog.setPositiveButton("저장", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(command_view.getCommand_icon().size() > 0) {
                    EditText nameValue = (EditText)save_layout.findViewById(R.id.namevalue);
                    String name_temp = nameValue.getText().toString();
                    if(mFileManagement.fileNameCheck(name_temp)){
                        Log.d(TAG,"possible");
                    }
                    else{
                        Log.d(TAG,"impossible");
                    }
//                    // write data and Save
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
//                            Toast.makeText(TutorialCommandActivity.this, "쓰기 실패", Toast.LENGTH_SHORT).show();
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

        inflater.inflate(R.layout.appmenu_layout,left_menu_layout,true);

        final ListView fileList = (ListView)TutorialCommandActivity.this.findViewById(R.id.file_list);
        CustomAdapter1 mAdapter  = mFileManagement.getMyFileAdapter();
        fileList.setAdapter(mAdapter);

        fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {  // open
                mSoundmanager.play(1);
                isSavedFile = true;
                thisFileName = mFileManagement.getFilelist()[position].getName();
                command_text.setText("Command   -  " + String.valueOf(thisFileName));
                command_text.setTextSize(20);
                command_text.setTextColor(Color.BLACK);

                mFileManagement.readCommandFromFile(position);

                implementationInitIconBox();
            }
        });

        fileList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { // delete
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                mSoundmanager.play(1);

                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(TutorialCommandActivity.this);

                LinearLayout title_layout = new LinearLayout(TutorialCommandActivity.this);
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


    void removeAllViewOnCommandWindow(){
        isKeyboard = false; // keyboard initialize

        ismenu_open = false;    // application menu initialize
        appmenu_open.setImageDrawable(getResources().getDrawable(R.mipmap.appmenu));

        if(command_text == null){
            command_text = new TextView(this);
            appmenu.addView(command_text);
        }
//        command_text.setText("Command   - " + String.valueOf(thisFileName)); // command text initiailize;
//        command_text.setTextSize(20);
//        command_text.setGravity(View.TEXT_ALIGNMENT_CENTER);
//        command_text.setTextColor(Color.BLACK);
//
//        for(int i=0; i<command_view.getCommand_icon().size() ; i++){    // initialize Command Layout
//            command_view.getCommand_icon().get(i).icon_unchecked();
//        }

        left_menu_layout.removeAllViews(); // left menu initialize

        setting_layout.removeAllViews();    // bottom | left setting layout initialize
        menu_layout.removeAllViews();       // bottom | right icon_menu layout initialize
        bottom_setting.setVisibility(View.GONE);
    }


    // TouchListener

    private View.OnTouchListener KeyboardTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
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
                        number[10].setImageDrawable(getResources().getDrawable(R.mipmap.enter_2));
                        char[] numberOfKeyboard = new char[keyboard_array.size()];
                        for(int i=0 ; i<keyboard_array.size(); i++){
                            numberOfKeyboard[i] = keyboard_array.get(i);
                        }
                        if(numberOfKeyboard.length != 0) {
                            keyboard_number = new String(numberOfKeyboard);
                        }
                        Log.d("KEYBOARD_NUMBER",keyboard_number);
                        int keyboard_valueToSend = Integer.parseInt(keyboard_number);
                        if(keyboard_valueToSend > 255 || keyboard_valueToSend < 0){
                            Toast.makeText(TutorialCommandActivity.this,"0~255 까지의 값을 입력해주세요",Toast.LENGTH_SHORT).show();
                            isKeyboard = false;
                            implementationKeyboardBox(concentration_index);
                        }
                        else {
                            command_view.updateValue(concentration_index, Integer.parseInt(keyboard_number));
                            setting_layout.removeAllViews();
                            mHandler.obtainMessage(ICON_ON_TOUCH_EVENT, TutorialWindowView.INFLATE_CHILDREN_LAYOUT, concentration_index).sendToTarget();
                        }

                        break;

                    case R.id.delete :
                        number[11].setImageDrawable(getResources().getDrawable(R.mipmap.delete_2));
                        if(keyboard_array.size() > 0){
                            keyboard_array.remove(keyboard_array.size()-1);
                        }
                        break;
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
            if(currentMode == NORMAL_MODE) {
                switch (position) {
                    case 0:
                        if (command_view.getCommand_icon().size() == 1 && command_view.getCommand_icon().get(0).getCommand() == Command.START) {
                            isSavedFile = false;
                            thisFileName = mFileManagement.makeFileNameUsingData() + ".drs";
                            implementationInitIconBox();
                            while (command_view.getCommand_icon().size() != 0) {
                                command_view.removeComand(0);
                            }

                            command_text.setText("Command   - " + String.valueOf(thisFileName));
                            command_text.setTextSize(20);
                            command_text.setTextColor(Color.BLACK);

                            command_view.addCommand(Command.START);
                            command_view.invalidate();
                        } else {
                            if (!mFileManagement.isFileExist(thisFileName)) {
                                AlertDialog.Builder saveReqeustDialog = new AlertDialog.Builder(TutorialCommandActivity.this);
                                LinearLayout title_layout = new LinearLayout(TutorialCommandActivity.this);
                                title_layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                title_layout.setGravity(View.TEXT_ALIGNMENT_CENTER);

                                title_layout = (LinearLayout) View.inflate(TutorialCommandActivity.this, R.layout.saverequest_topic, null);
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
                                        thisFileName = mFileManagement.makeFileNameUsingData() + ".drs";
                                        implementationInitIconBox();
                                        while (command_view.getCommand_icon().size() != 0) {
                                            command_view.removeComand(0);
                                        }

                                        command_text.setText("Command   - " + String.valueOf(thisFileName));
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
                                String current_command = "";
                                int index = 0;
                                for (int i = 0; i < command_view.getCommand_icon().size(); i++) {
                                    current_command += String.valueOf(command_view.getCommand_icon().get(i).getCommand());
                                    current_command += String.valueOf(command_view.getCommand_icon().get(i).getValue());
                                }
                                ArrayList<int[]> readCommand = mFileManagement.readCommandFromFile(thisFileName);

//                                Log.d("Current_command", current_command);
//                                Log.d("Read_command", readCommand);

//                                if (current_command.equals(readCommand)) {
//
//                                    isSavedFile = false;
//                                    thisFileName = mFileManagement.makeFileNameUsingData() + ".drs";
//                                    implementationInitIconBox();
//                                    while (command_view.getCommand_icon().size() != 0) {
//                                        command_view.removeComand(0);
//                                    }
//
//                                    command_text.setText("Command   - " + String.valueOf(thisFileName));
//                                    command_text.setTextSize(20);
//                                    command_text.setTextColor(Color.BLACK);
//
//                                    command_view.addCommand(Command.START);
//                                    command_view.invalidate();
//
//                                } else {
//
//                                    mFileManagement.writeCommandOnFile(thisFileName, command_view.getCommand_icon());
//                                    isSavedFile = false;
//                                    thisFileName = mFileManagement.makeFileNameUsingData() + ".drs";
//                                    implementationInitIconBox();
//                                    while (command_view.getCommand_icon().size() != 0) {
//                                        command_view.removeComand(0);
//                                    }
//
//                                    command_text.setText("Command   - " + String.valueOf(thisFileName));
//                                    command_text.setTextSize(20);
//                                    command_text.setTextColor(Color.BLACK);
//
//                                    command_view.addCommand(Command.START);
//                                    command_view.invalidate();
//
//                                }
                            }
                        }

                        break;

                    case 1:
                        if (command_view.getCommand_icon().size() > 0) {
                            if (!isSavedFile) {
                                implementationSaveAsBox();
                                Log.d(getClass().getSimpleName(), "저장되지 않은 파일");
                            } else {
                                Log.d(getClass().getSimpleName(), "저장된 파일 -> name : " + thisFileName);
                                mFileManagement.writeCommandOnFile(thisFileName, command_view.getCommand_icon());
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "저장할 내용이 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 2:
                        implementationSaveAsBox();
                        break;

                    case 3:
                        implementationOpenBox();
                        break;

                    case 4:
                        Intent play_intent = new Intent(getApplicationContext(), ArduinoPlayActivity.class);
                        play_intent.putExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS, mFileManagement.readBTAddress());
                        startActivity(play_intent);

                        break;

                    case 5:
                        if (mFileManagement.readBTAddress() != null) {
                            ArrayList<Integer> command_data = new ArrayList<Integer>();
                            for (int i = 0; i < command_view.getCommand_icon().size(); i++) {
                                command_data.add(command_view.getCommand_icon().get(i).getCommand());
                                command_data.add(command_view.getCommand_icon().get(i).getValue());
                            }

                            overridePendingTransition(R.anim.fade, R.anim.hold);
                        } else {
                            Toast.makeText(TutorialCommandActivity.this, "블루투스 기기를 선택해주세요", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case 6:
                        if (mBluetoothService.getDeviceState()) {
                            mBluetoothService.setReadRunning(true);
                            mBluetoothService.enableBluetooth();
                        } else {
                            finish();
                        }
                        break;

                    case 7:
                        command_window.removeAllViews();
                        removeAllViewOnCommandWindow();
                        command_text.setText("");
                        mTutorial = new Tutorial(TutorialCommandActivity.this, TutorialCommandActivity.this, mHandler);
//                    currentMode = TUTORIAL_MODE;

                        break;
                }

                ismenu_open = false;
                appmenu_open.setImageDrawable(getResources().getDrawable(R.mipmap.appmenu));
            }
            else if(currentMode == TUTORIAL0){
                String[] appmenu_tuto_toast = {
                        "New : 새로운 파일을 엽니다.",
                        "Save : 작성한 파일을 저장합니다.",
                        "Save As : 작성한 파일을 다른 이름으로 저장합니다.",
                        "My File : 자신이 작성한 프로그램을 엽니다.",
                        "Play : 아두이노에 업로드된 프로그램을 실행합니다.",
                        "Upload : 자신이 작성한 프로그램을 업로드 합니다.",
                        "BT Setting : 자신의 블루투스 모듈을 등록합니다.",
                        "Tutorial : 어플리케이션 사용방법에 대해 알아봅니다."
                };

                Toast.makeText(getApplicationContext(),appmenu_tuto_toast[position],Toast.LENGTH_SHORT).show();
            }
        }

    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"requestCode : " + requestCode);
        Log.d(TAG, "onActivityResult" + resultCode);
        if(resultCode ==1){
            finish();
        }

        switch (requestCode) {
            case REQUEST_ENABLE_BT :
                if (resultCode != Activity.RESULT_OK) {
                    mBluetoothService.scanDevice();
                } else {//cancel button
                    Log.d(TAG, "Bluetooth is not enable");
                }
                break;

            case REQUEST_CONNECT_DEVICE :
                if (resultCode == Activity.RESULT_OK) {
//                    mBluetoothService.getDeviceInfo(data);
                    mFileManagement.writeBtAddressOnFile(data.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS));
                }
                break;

            case REQUEST_TUTORIAL0_0 :
                if(resultCode == STAGE_OVER){
                    command_window.removeAllViews();
                    final ImageView next = new ImageView(TutorialCommandActivity.this);
                    next.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    next.setImageDrawable(getResources().getDrawable(R.mipmap.move_right));

                    next.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                                next.setImageDrawable(getResources().getDrawable(R.mipmap.move_right_on));
                            }
                            else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                                try{
                                    Thread.sleep(20);
                                }
                                catch (InterruptedException e){};
                                next.setImageDrawable(getResources().getDrawable(R.mipmap.move_right));

                                mSoundmanager.play(0);
                                command_window.removeAllViews();
                                command_window.addView(command_view);
                                removeAllViewOnCommandWindow();
                                implementationInitIconBox();
                                Intent transparent_intent = new Intent(TutorialCommandActivity.this,TransparentActivity.class);
                                transparent_intent.putExtra("TUTORIAL",REQUEST_TUTORIAL0_1);
                                startActivityForResult(transparent_intent,REQUEST_TUTORIAL0_1);
                            }

                            return true;
                        }
                    });

                    Space space = new Space(TutorialCommandActivity.this);
                    space.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,20));

                    TextView next_text = new TextView(TutorialCommandActivity.this);
                    next_text.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    next_text.setText("각각 어플리케이션 메뉴를 눌러보세요. \n계속 진행하시려면 위 버튼을 눌러주세요");
                    next_text.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    next_text.setTextSize(20);

                    command_window.addView(next);
                    command_window.addView(space);
                    command_window.addView(next_text);

                }

                break;

            case REQUEST_TUTORIAL0_1 :
                Log.d(TAG,"Stage Over" + requestCode);
                if(resultCode ==0) {
                    command_window.removeAllViews();
                    final ImageView next = new ImageView(TutorialCommandActivity.this);
                    next.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    next.setImageDrawable(getResources().getDrawable(R.mipmap.move_right));

                    next.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                                next.setImageDrawable(getResources().getDrawable(R.mipmap.move_right_on));
                            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                                try {
                                    Thread.sleep(20);
                                } catch (InterruptedException e) {
                                }
                                ;
                                next.setImageDrawable(getResources().getDrawable(R.mipmap.move_right));

                                mSoundmanager.play(0);
                                command_window.removeAllViews();
                                command_window.addView(command_view);
                                removeAllViewOnCommandWindow();
                                implementationInitIconBox();
                                Intent transparent_intent = new Intent(TutorialCommandActivity.this, TransparentActivity.class);
                                transparent_intent.putExtra("TUTORIAL", REQUEST_TUTORIAL0_2);
                                startActivityForResult(transparent_intent, REQUEST_TUTORIAL0_2);
                                setResult(0);

                            }

                            return true;
                        }
                    });

                    Space space = new Space(TutorialCommandActivity.this);
                    space.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));

                    TextView next_text = new TextView(TutorialCommandActivity.this);
                    next_text.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    next_text.setText("각각 어플리케이션 메뉴를 눌러보세요. \n계속 진행하시려면 위 버튼을 눌러주세요");
                    next_text.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    next_text.setTextSize(20);

                    command_window.addView(next);
                    command_window.addView(space);
                    command_window.addView(next_text);
                }

                break;

            case REQUEST_TUTORIAL0_2 :
                command_view.setCommandViewhint(TutorialWindowView.tuto0_2);
                command_view.invalidate();
                currentMode = NORMAL_MODE;

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
            finish();
            setResult(1);
        }
        return true;
    }

    private boolean tuto_touchcheck = false ;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG,"Received");
            if (action.equals(TransparentActivity.REQUEST_APP_MENU)) {
                removeAllViewOnCommandWindow();
                implementationAppMenuBox();
                mSoundmanager.play(0);
                ismenu_open = true;
                currentMode = TUTORIAL0;
            } else if (action.equals(TransparentActivity.REQUEST_ADD_COMMAND_IN_T10)) {
                int[] command = {Command.START, Command.MOTOR3,Command.MOTOR10, Command.TIMER_SEC,Command.END};
                for(int i=0; i<command.length;i++){
                    command_view.addCommand(command[i]);
                    command_view.invalidate();
                }
            }
            else if(action.equals(TransparentActivity.REQUEST_SETTING_LAYOUT)){
                final int index = intent.getIntExtra("index",0);
                currentMode = TUTORIAL1_0;
                tuto_touchcheck = true;
                command_view.setCommandViewhint(TutorialWindowView.tuto1_0);
                command_view.invalidate();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(tuto_touchcheck) {
                            if (command_view.getCommand_icon().get(index).getIconChecked()) {
                                Intent intent = new Intent(TutorialCommandActivity.this, TransparentActivity.class);
                                intent.putExtra("TUTORIAL", REQUEST_TUTORIAL1_1);
                                startActivityForResult(intent, REQUEST_TUTORIAL1_1);
                                tuto_touchcheck = false;
                                command_view.setCommandViewhint(-1);

                            }
                            try{
                                Thread.sleep(20);
                            }catch (InterruptedException e){};
                        }
                    }
                }).start();

            }
            else if(action.equals(TransparentActivity.REQUEST_INITIAL_COMMAND)){
                for(int i=0; i<command_view.getCommand_icon().size() ; i++){
                    command_view.getCommand_icon().get(i).icon_unchecked();
                }

                currentMode = NORMAL_MODE;
                removeAllViewOnCommandWindow();
                implementationInitIconBox();
                Log.d(TAG,String.valueOf(ismenu_open));

                command_view.setCommandViewhint(TutorialWindowView.tuto1_1);
                command_view.invalidate();
                tuto_touchcheck = true;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(tuto_touchcheck){
                            if(command_view.getCommand_icon().size() == 5) {
                                if (command_view.getCommand_icon().get(0).getCommand() == Command.START &&
                                        command_view.getCommand_icon().get(1).getCommand() == Command.MOTOR5 &&
                                        command_view.getCommand_icon().get(2).getCommand() == Command.MOTOR3 &&
                                        command_view.getCommand_icon().get(3).getCommand() == Command.TIMER_SEC &&
                                        command_view.getCommand_icon().get(4).getCommand() == Command.END) {
                                    if (command_view.getCommand_icon().get(1).getValue() == 100 &&
                                            command_view.getCommand_icon().get(2).getValue() == 100 &&
                                            command_view.getCommand_icon().get(3).getValue() == 2) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(TutorialCommandActivity.this, "OK", Toast.LENGTH_SHORT).show();
                                                tuto_touchcheck = false;
                                                finish();
                                            }
                                        });
                                    }
                                }
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                }
                                ;
                            }
                            else if(command_view.getCommand_icon().size() > 5){
                                command_view.removeAllCommand();
                                Toast.makeText(TutorialCommandActivity.this,"현재는 명령어가 5개를 넘을 수 없습니다.",Toast.LENGTH_SHORT).show();
                                command_view.invalidate();

                            }
                        }
                    }
                }).start();
            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(TransparentActivity.REQUEST_APP_MENU);
        filter.addAction(TransparentActivity.REQUEST_SETTING_LAYOUT);
        filter.addAction(TransparentActivity.REQUEST_ADD_COMMAND_IN_T10);
        filter.addAction(TransparentActivity.REQUEST_INITIAL_COMMAND);
        registerReceiver(mReceiver,filter);

        Log.d(TAG,"Register Receiver");
    }

    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(mReceiver);
        setResult(0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mReceiver);
        tuto_touchcheck = false;
    }


}
