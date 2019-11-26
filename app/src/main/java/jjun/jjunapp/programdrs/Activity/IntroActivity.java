package jjun.jjunapp.programdrs.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.net.JarURLConnection;

import jjun.jjunapp.programdrs.Command.CommandWindowView;
import jjun.jjunapp.programdrs.R;
import jjun.jjunapp.programdrs.Tutorial.Tutorial;

/**
 * Created by jjunj on 2017-10-19.
 */

public class IntroActivity extends AppCompatActivity{

    private AppIntroView introview;
    private boolean isOn = true;
    private long prev_time ;
    private Handler handler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prev_time = System.currentTimeMillis();

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        introview = new AppIntroView(this,this);
        setContentView(introview);
        handler = new Handler();


        new Thread(new Runnable() {
            @Override
            public void run() {
                while(isOn){
                    if(System.currentTimeMillis() - prev_time > 3000){
                        handler.post(dialogRunnable);
                        isOn = false;
                    }
                    try{
                        Thread.sleep(100);
                    }catch (InterruptedException e){};
                }
            }
        }).start();

    }



    private Runnable dialogRunnable = new Runnable() {
        @Override
        public void run() {
            introview.createRequestTutorialDialog();
            finish();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        isOn = false;

    }
}
