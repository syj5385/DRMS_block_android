package jjun.jjunapp.programdrs.Activity;

import android.Manifest;
import android.animation.LayoutTransition;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;

import jjun.jjunapp.programdrs.Command.Command;
import jjun.jjunapp.programdrs.Command.CommandWindowView;
import jjun.jjunapp.programdrs.R;


/**
 * Created by jjunj on 2017-09-18.
 */

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSION = 0;
    private static final int PERMISSION_RESULT_OK = 1;
    private static final int UPDATEUI = 1;

    int count = 0;

    private static final int TIMEOVER_SPLASH = 0 ;

    private LinearLayout drone_layout;
    private DroneView drone_view;

    private int display_width, display_height;

    private boolean running = false;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch(msg.what){
                case TIMEOVER_SPLASH :
                    Intent splash_intent;
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        int permissionResult = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
                        int permissionResult2 = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
                        int permissionResult3 = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

                        if(permissionResult == PackageManager.PERMISSION_DENIED
                                || permissionResult2 == PackageManager.PERMISSION_DENIED
                                || permissionResult3 == PackageManager.PERMISSION_DENIED){
                            splash_intent = new Intent(SplashActivity.this,CheckPermissionActivity.class);
                            startActivityForResult(splash_intent,REQUEST_PERMISSION);
                            overridePendingTransition(R.anim.fade, R.anim.hold);
                            finish();
                        }
                        else{
                            Log.d("HANDLER","OK");
                            splash_intent = new Intent(SplashActivity.this,MainActivity.class);
                            startActivity(splash_intent);
                            overridePendingTransition(R.anim.fade, R.anim.hold);
                            finish();
                        }

                    }
                    else{
                        splash_intent = new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(splash_intent);
//                        overridePendingTransition(R.anim.fade, R.anim.hold);
                        finish();
                    }
                    break;

                case UPDATEUI :
                    drone_view.invalidate();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        drone_layout = (LinearLayout) findViewById(R.id.splsh_layout);

        display_width = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        display_height = getWindow().getWindowManager().getDefaultDisplay().getHeight();

        drone_view = new DroneView(this, display_width, display_height);
        drone_view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        drone_layout.addView(drone_view);
        running = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(running){
                        drone_view.updateDrone();
                        count ++;
                    try{
                        Thread.sleep(10);
                    }catch (InterruptedException e){};


                }

            }
        }).start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_PERMISSION){
            if(resultCode == PERMISSION_RESULT_OK){
                Intent startIntent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(startIntent);
                finish();
            }

        }
    }

    private class DroneView extends View{

        private Context context;

        private Paint paint;
        private Paint line_paint ;
        private Paint red_paint;
        private Bitmap drone_image;
        private Bitmap DRMS_text;
        private int display_width ;
        private int display_height ;
        float current_x = 100;
        boolean check  = false;
        float current_y = 100;

        public DroneView(Context context,int display_width , int display_height) {
            super(context);

            this.context = context;
            this.display_width = display_width;
            this.display_height = display_height;

            paint = new Paint();
            line_paint = new Paint();
            line_paint.setStrokeWidth(20);
            line_paint.setColor(getResources().getColor(R.color.colorPrimary));

            red_paint = new Paint();
            red_paint.setStrokeWidth(20);
            red_paint.setColor(getResources().getColor(R.color.redColor));

            Log.d("SPLASH",String.valueOf(display_height) + "\n " + String.valueOf(display_width));
            drone_image = BitmapFactory.decodeResource(context.getResources(),R.mipmap.main_drone);
            DRMS_text = BitmapFactory.decodeResource(context.getResources(),R.drawable.drms_text);
            this.current_x = display_width/2 - DRMS_text.getWidth()/2 - drone_image.getWidth()/2;
            this.current_y = display_height/2 + drone_image.getHeight()/2;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawBitmap(DRMS_text,display_width/2 - DRMS_text.getWidth()/2, display_height/2 - DRMS_text.getHeight(),paint);
            canvas.drawLine(display_width/2 - DRMS_text.getWidth()/2,current_y + drone_image.getHeight()/2,current_x + drone_image.getWidth()/2,
                    current_y + drone_image.getHeight()/2,red_paint);
            canvas.drawLine(current_x + drone_image.getWidth()/2, current_y + drone_image.getHeight()/2
            ,display_width/2 + DRMS_text.getWidth()/2, current_y + drone_image.getHeight()/2,line_paint);

            canvas.drawBitmap(drone_image,current_x, current_y, paint);
            running = true;
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try{
//                        Thread.sleep(300);
//                    }catch (InterruptedException e){};
//                    while(running){
//                        current_x += 3;
//                        if(current_x > display_width/2 + DRMS_text.getWidth()/2 - drone_image.getWidth()/2){
//                            running = false;
//                            mHandler.obtainMessage(TIMEOVER_SPLASH).sendToTarget();
////                            finish();
//                            break;
//                        }
//                        else {
//                            try{
//                                Thread.sleep(140);
//                            }catch (InterruptedException e){};
//                           runOnUiThread(new Runnable() {
//                               @Override
//                               public void run() {
//                                   invalidate();
//                               }
//                           });
//                        }
//
//
//                    }
//                }
//            }).start();




        }

        public void updateDrone(){
            current_x += 3 ;
            if(current_x < display_width/2 + DRMS_text.getWidth()/2 - drone_image.getWidth()/2) {
//                mHandler.obtainMessage(UPDATEUI).sendToTarget();
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       invalidate();
                   }
               });
            }
            else{
                running = false;
                if(!check) {
                    mHandler.obtainMessage(TIMEOVER_SPLASH).sendToTarget();
                    check = true;
                }
            }
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        running = false;
        if(drone_view != null)
            drone_view = null;
    }
}
