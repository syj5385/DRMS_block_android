package jjun.jjunapp.programdrs.Command;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import jjun.jjunapp.programdrs.R;

/**
 * Created by jjunj on 2017-10-23.
 */

public class CommandCheckActivity extends AppCompatActivity{

    public static final String REQUEST_FINISH_ACTIVITY = "finish";

    private TextView monitor_text;
    private ProgressBar commandcheckProgress;
    private Handler myHandler;

    private CommandCheck check;
    private boolean gotoNext = true;

    private ArrayList<CommandIcon> command_list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commandcheck);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        myHandler = new Handler();
        check = new CommandCheck(this,null,myHandler);

        monitor_text = (TextView)findViewById(R.id.monitor_text);
        commandcheckProgress = (ProgressBar)findViewById(R.id.command_check_progress);

        new Thread(new Runnable() {
            @Override
            public void run() {
                myHandler.postDelayed(startCheck,500);
            }
        }).start();

    }

    public Runnable startCheck = new Runnable() {
        @Override
        public void run() {
            commandcheckProgress.setVisibility(View.VISIBLE);
            monitor_text.setText("작성한 명령어를 확인하는 중입니다. 잠시만 기다려 주세요.");

        }
    };


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Check ", "Received");
            String action = intent.getAction();
            if (action.equals(REQUEST_FINISH_ACTIVITY) ) {
                int checkresult = intent.getIntExtra("Check",-1);
                commandcheckProgress.setVisibility(View.INVISIBLE);
                if(checkresult > 0){       // exist error
                    monitor_text.setText(checkresult + " 개의 에러가 발견되었습니다.");
                    CommandCheckActivity.this.setResult(0);
                }
                else if(checkresult == -1){ // check failed
                    monitor_text.setText("명령어 체크를 실패했습니다. 다시 시도해주세요.");
                    CommandCheckActivity.this.setResult(-1);
                }
                else if(checkresult == 0){ // not error
                    monitor_text.setText("업로드 화면으로 이동합니다.");
                    CommandCheckActivity.this.setResult(1);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(gotoNext) {
                            finish();
                        }
                    }
                },3000);

            }
        }
    };


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Toast.makeText(this,"명령어 체크 중에는 종료할 수 없습니다.",Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(REQUEST_FINISH_ACTIVITY);

        registerReceiver(mReceiver,filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }
}
