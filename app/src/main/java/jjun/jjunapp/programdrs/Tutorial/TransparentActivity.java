package jjun.jjunapp.programdrs.Tutorial;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import jjun.jjunapp.programdrs.R;
import jjun.jjunapp.programdrs.Tutorial.TutorialView.TutorialView0.Tutorial0View;
import jjun.jjunapp.programdrs.Tutorial.TutorialView.TutorialView0.Tutorial0_1View;
import jjun.jjunapp.programdrs.Tutorial.TutorialView.TutorialView0.Tutorial0_2View;
import jjun.jjunapp.programdrs.Tutorial.TutorialView.TutorialView1.Tutorial1_0View;
import jjun.jjunapp.programdrs.Tutorial.TutorialView.TutorialView1.Tutorial1_1View;

/**
 * Created by jjunj on 2017-09-27.
 */

public class TransparentActivity extends AppCompatActivity {

    private static final int COMMAND_LAYOUT_SIZE = 0;

    private static final String TAG = TransparentActivity.class.getClass().getSimpleName();
    private int current_stage;

    private float command_layout_width, command_layout_height;

    private LinearLayout command_window,app_menu_open,bottom_setting;

    private Tutorial0View t0_view;
    private Tutorial0_1View t0_1_view;
    private Tutorial0_2View t0_2_view;
    private Tutorial1_0View t1_0_view;
    private Tutorial1_1View t1_1_view;

    // broadCast Macro
    public static final String REQUEST_TUTORIAL_WINDOW = "TUTORIAL_REQUEST";
    public static final String REQUEST_APP_MENU = "REQUEST_APP_MENU";
    public static final String REQUEST_ADD_COMMAND_IN_T10 = "REQUEST_COMMAND_10";
    public static final String REQUEST_SETTING_LAYOUT = "REQUEST_SETTING_LAYOUT";
    public static final String REQUEST_INITIAL_COMMAND = "REQUEST_INITIAL_CONNAMD";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        command_window = (LinearLayout)this.findViewById(R.id.command_window);
        app_menu_open = (LinearLayout)findViewById(R.id.appmenu_open);

        current_stage = getIntent().getIntExtra("TUTORIAL",0);
        bottom_setting = (LinearLayout)findViewById(R.id.bottom_setting);
        Bitmap bottom_setting_back = BitmapFactory.decodeResource(this.getResources(),R.drawable.setting_box_back);
        Bitmap back = Bitmap.createBitmap(bottom_setting_back.getWidth(),bottom_setting_back.getHeight(), Bitmap.Config.ARGB_8888);

        bottom_setting.setBackground(new BitmapDrawable(this.getResources(),back));

        if(current_stage == TutorialCommandActivity.REQUEST_TUTORIAL1_1){
            bottom_setting.setVisibility(View.VISIBLE);
        }

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
                    float layout_height = getWindow().getWindowManager().getDefaultDisplay().getHeight() - app_menu_open.getHeight();
                    mHandler.obtainMessage(COMMAND_LAYOUT_SIZE, (int) layout_width, (int) layout_height).sendToTarget();
                }

            }
        });
        Log.d(TAG,"Size : " + String.valueOf(command_window.getWidth()) + "\n" + String.valueOf(command_window.getHeight()));
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(current_stage == TutorialCommandActivity.REQUEST_TUTORIAL0_0) {
                    while (t0_view == null) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            command_window.addView(t0_view);
                        }
                    });
                }
                else if(current_stage == TutorialCommandActivity.REQUEST_TUTORIAL0_1){
                    while (t0_1_view == null) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            command_window.addView(t0_1_view);
                        }
                    });
                }
                else if(current_stage == TutorialCommandActivity.REQUEST_TUTORIAL0_2){
                    while(t0_2_view == null){
                        try{
                            Thread.sleep(10);
                        }catch (InterruptedException e){}
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            command_window.addView(t0_2_view);
                        }
                    });
                }
                else if(current_stage == TutorialCommandActivity.REQUEST_TUTORIAL1_0){
                    while(t1_0_view == null){
                        try{
                            Thread.sleep(10);
                        }catch (InterruptedException e){}
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            command_window.addView(t1_0_view);
                        }
                    });
                }
                else if(current_stage == TutorialCommandActivity.REQUEST_TUTORIAL1_1){
                    while(t1_1_view == null){
                        try{
                            Thread.sleep(10);
                        }catch (InterruptedException e){}
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            command_window.addView(t1_1_view);
                        }
                    });
                }

            }
        }).start();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int[] size = new int[2];
            switch (msg.what){
                case COMMAND_LAYOUT_SIZE :
                    Log.d(TAG,"Received Msg");
                    command_layout_width = msg.arg1;
                    command_layout_height= msg.arg2;
                    switch(current_stage){
                        case TutorialCommandActivity.REQUEST_TUTORIAL0_0 :
                            t0_view = new Tutorial0View(TransparentActivity.this, TransparentActivity.this, mHandler);
                            t0_view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    msg.arg2));
                            size[0] = msg.arg1;
                            size[1] = msg.arg2;
                            t0_view.setSize(size);
                            break;

                        case TutorialCommandActivity.REQUEST_TUTORIAL0_1 :
                            t0_1_view = new Tutorial0_1View(TransparentActivity.this,TransparentActivity.this,mHandler);
                            t0_1_view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    msg.arg2));
                            size[0] = msg.arg1;
                            size[1] = msg.arg2;
                            t0_1_view.setSize(size);
                            break;

                        case TutorialCommandActivity.REQUEST_TUTORIAL0_2 :
                            t0_2_view = new Tutorial0_2View(TransparentActivity.this,TransparentActivity.this,mHandler);
                            t0_2_view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    msg.arg2));
                            size[0] = msg.arg1;
                            size[1] = msg.arg2;
                            t0_2_view.setSize(size);
                            break;

                        case TutorialCommandActivity.REQUEST_TUTORIAL1_0 :
                            t1_0_view = new Tutorial1_0View(TransparentActivity.this,TransparentActivity.this,mHandler);
                            t1_0_view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    msg.arg2));
                            size[0] = msg.arg1;
                            size[1] = msg.arg2;
                            t1_0_view.setSize(size);
                            break;

                        case TutorialCommandActivity.REQUEST_TUTORIAL1_1 :
                            t1_1_view = new Tutorial1_1View(TransparentActivity.this,TransparentActivity.this,mHandler);
                            t1_1_view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    msg.arg2));
                            size[0] = msg.arg1;
                            size[1] = msg.arg2;
                            t1_1_view.setSize(size);
                            break;
                    }

                    break;
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if(current_stage == TutorialCommandActivity.REQUEST_TUTORIAL0_0)
            t0_view.setStopThread();
        else if(current_stage == TutorialCommandActivity.REQUEST_TUTORIAL0_1){
            t0_1_view.setStopThread();
        }
        else if(current_stage == TutorialCommandActivity.REQUEST_TUTORIAL0_2){
            t0_2_view.setStopThread();
        }
        else if(current_stage == TutorialCommandActivity.REQUEST_TUTORIAL1_0){
            t1_0_view.setStopThread();
        }
        else if(current_stage == TutorialCommandActivity.REQUEST_TUTORIAL1_1){
            t1_1_view.setStopThread();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        ImageView app_menu_btn = (ImageView)findViewById(R.id.app_menu_tutorial);
        app_menu_btn.setOnClickListener(null);
        switch(current_stage){
            case TutorialCommandActivity.REQUEST_TUTORIAL0_0 :
                Log.d(TAG, "STAGE OVER " + TutorialCommandActivity.STAGE_OVER);
                setResult(TutorialCommandActivity.STAGE_OVER);
                break;

            case TutorialCommandActivity.REQUEST_TUTORIAL0_1 :
                Log.d(TAG,"STAGE OVER " + current_stage + "\t" + TutorialCommandActivity.STAGE_OVER);
                setResult(TutorialCommandActivity.STAGE_OVER);
                break;

            case TutorialCommandActivity.REQUEST_TUTORIAL0_2 :
                Log.d(TAG,"STAGE OVER " + current_stage + "\t" + TutorialCommandActivity.STAGE_OVER);
                setResult(TutorialCommandActivity.STAGE_OVER);
                break;

            case TutorialCommandActivity.REQUEST_TUTORIAL1_0 :
                Log.d(TAG,"STAGE OVER " + current_stage + "\t" + TutorialCommandActivity.STAGE_OVER);
                setResult(TutorialCommandActivity.STAGE_OVER);
                break;

            case TutorialCommandActivity.REQUEST_TUTORIAL1_1 :
                Log.d(TAG,"STAGE OVER " + current_stage + "\t" + TutorialCommandActivity.STAGE_OVER);
                setResult(TutorialCommandActivity.STAGE_OVER);
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

        else if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(1);
            finish();
        }

        return true;
    }
}
