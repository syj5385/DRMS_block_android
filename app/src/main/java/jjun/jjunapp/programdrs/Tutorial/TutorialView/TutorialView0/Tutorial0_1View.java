package jjun.jjunapp.programdrs.Tutorial.TutorialView.TutorialView0;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import jjun.jjunapp.programdrs.R;
import jjun.jjunapp.programdrs.Sound.SoundManager;

import static android.content.ContentValues.TAG;

/**
 * Created by jjunj on 2017-10-11.
 */

public class Tutorial0_1View extends View {

    private static final int UPDATE_UI = 0 ;

    private Context mContext;
    private Activity mActivity;
    private Handler mHandler;

    private Handler T0_Handler;

    private int[] size = new int[2];

    private int drawable_alpha = 0;
    private Paint[] paint = new Paint[7];
    private boolean[] step = new boolean[15];
    private String[] content = {"","",""};
    private String[] current_content = {"","",""};

    private LinearLayout command_window,left_menu_layout;
    private ImageView app_menu_btn;
    private SoundManager mSoundManager;
    private NextBtn next;


    public Tutorial0_1View(final Context context, final Activity mActivity, Handler mHandler) {
        super(context);

        this.mContext = context;
        this.mActivity = mActivity;
        this.mHandler = mHandler;
        T0_Handler = new MyHandler();
        mSoundManager = new SoundManager(context);

        app_menu_btn = mActivity.findViewById(R.id.app_menu_tutorial);

        for(int i=0; i<step.length ;i++){
            if(i  == 0)
                step[i] = true;
            else
                step[i] = false;

        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                drawable_alpha = 0;
                int waitCount = 0;
                try{
                    Thread.sleep(500);
                }catch (InterruptedException e){};

                while(step[0]){
                    if(drawable_alpha == 0) {
                        drawable_alpha = 255;
                        T0_Handler.obtainMessage(UPDATE_UI).sendToTarget();
                    }
                    else{
                        waitCount++;
                        if(waitCount == 100){
                            step[1] = true;
                            step[0] = false;
                            drawable_alpha = 0;
                        }
                    }
                    try{
                        Thread.sleep(20);
                    }catch (InterruptedException e){}

                }

                try{
                    Thread.sleep(100);
                }catch (InterruptedException e){};

                waitCount = 0;

                while(step[1]){
                    if(drawable_alpha == 0){
                        drawable_alpha = 255;
                        T0_Handler.obtainMessage(UPDATE_UI).sendToTarget();
                    }
                    else{
                        waitCount++;
                        if(waitCount == 100){
                            step[2] = true;
                            step[1] = false;
//                            drawable_alpha = 0;
                            Log.d(TAG,"OK");
                            Log.d(TAG,"SendBroadCast");

                            for(int i=0; i<step.length;i++){
                                Log.d(TAG,String.valueOf(i) + String.valueOf(step[i]));
                            }
                        }
                    }

                    try{
                        Thread.sleep(20);
                    }catch (InterruptedException e){};
                }

                while(step[2]){
                    try{
                        T0_Handler.obtainMessage(UPDATE_UI).sendToTarget();
                        Thread.sleep(100);
                    }catch (InterruptedException e){};
                }

                Log.d(TAG,"Start step3");

                waitCount = 0;
                try{
                    Thread.sleep(100);
                }catch (InterruptedException e){}

                while(step[3]){
                    if(drawable_alpha == 0){
                        drawable_alpha = 255;
                        T0_Handler.obtainMessage(UPDATE_UI).sendToTarget();
                    }
                    else{
                        waitCount++;
                        if(waitCount == 100){
                            step[4] = true;
                            step[3] = false;
//                            drawable_alpha = 0;
                            Log.d(TAG,"OK");
                            Log.d(TAG,"SendBroadCast");

                            for(int i=0; i<step.length;i++){
                                Log.d(TAG,String.valueOf(i) + String.valueOf(step[i]));
                            }
                        }
                    }

                    try{
                        Thread.sleep(20);
                    }catch (InterruptedException e){};
                }

                while(step[4]){
                    try{
                        T0_Handler.obtainMessage(UPDATE_UI).sendToTarget();
                        Thread.sleep(100);
                    }catch (InterruptedException e){};
                }

                waitCount = 0;
                try{
                    Thread.sleep(100);
                }catch (InterruptedException e){}

                while(step[5]){
                    if(drawable_alpha == 0){
                        drawable_alpha = 255;
                        T0_Handler.obtainMessage(UPDATE_UI).sendToTarget();
                    }
                    else{
                        waitCount++;
                        if(waitCount == 100){
                            step[6] = true;
                            step[5] = false;
//                            drawable_alpha = 0;
                            Log.d(TAG,"OK");
                            Log.d(TAG,"SendBroadCast");

                            for(int i=0; i<step.length;i++){
                                Log.d(TAG,String.valueOf(i) + String.valueOf(step[i]));
                            }
                        }
                    }

                    try{
                        Thread.sleep(20);
                    }catch (InterruptedException e){};
                }

                while(step[6]){
                    try{
                        T0_Handler.obtainMessage(UPDATE_UI).sendToTarget();
                        Thread.sleep(100);
                    }catch (InterruptedException e){};
                }

                waitCount = 0;
                try{
                    Thread.sleep(100);
                }catch (InterruptedException e){}

                while(step[7]){
                    if(drawable_alpha == 0){
                        drawable_alpha = 255;
                        T0_Handler.obtainMessage(UPDATE_UI).sendToTarget();
                    }
                    else{
                        waitCount++;
                        if(waitCount == 100){
                            step[8] = true;
                            step[7] = false;
//                            drawable_alpha = 0;
                            Log.d(TAG,"OK");
                            Log.d(TAG,"SendBroadCast");

                            for(int i=0; i<step.length;i++){
                                Log.d(TAG,String.valueOf(i) + String.valueOf(step[i]));
                            }
                        }
                    }

                    try{
                        Thread.sleep(20);
                    }catch (InterruptedException e){};
                }

                while(step[8]){
                    try{
                        T0_Handler.obtainMessage(UPDATE_UI).sendToTarget();
                        Thread.sleep(100);
                    }catch (InterruptedException e){};
                }

                waitCount = 0;
                try{
                    Thread.sleep(100);
                }catch (InterruptedException e){}


                while(step[9]){
                    if(drawable_alpha == 0){
                        drawable_alpha = 255;
                        T0_Handler.obtainMessage(UPDATE_UI).sendToTarget();
                    }
                    else{
                        waitCount++;
                        if(waitCount == 100){
                            step[10] = true;
                            step[9] = false;
//                            drawable_alpha = 0;
                            Log.d(TAG,"OK");
                            Log.d(TAG,"SendBroadCast");

                            for(int i=0; i<step.length;i++){
                                Log.d(TAG,String.valueOf(i) + String.valueOf(step[i]));
                            }
                        }
                    }

                    try{
                        Thread.sleep(20);
                    }catch (InterruptedException e){};
                }

                while(step[10]){
                    try{
                        T0_Handler.obtainMessage(UPDATE_UI).sendToTarget();
                        Thread.sleep(100);
                    }catch (InterruptedException e){};
                }

                waitCount = 0;
                try{
                    Thread.sleep(100);
                }catch (InterruptedException e){};

                while(step[11]){
                    if(drawable_alpha == 0){
                        drawable_alpha = 255;
                        T0_Handler.obtainMessage(UPDATE_UI).sendToTarget();
                    }
                    else{
                        waitCount++;
                        if(waitCount == 200){
                            setStopThread();
                            mActivity.finish();
                        }
                    }
                    try{
                        Thread.sleep(20);
                    }catch (InterruptedException e){};
                }

            }
        }).start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(size[0] != 0 && size[1] != 0) {
            if(step[0] || step[1] || step[2]){
                drawIconTuto(canvas);
            }
            else if(step[3] || step[4]){
                drawIconTuto2(canvas);
            }

            else if(step[5] || step[6]){
                drawIconTuto3(canvas);
            }

            else if(step[7] || step[8]){
                drawIconTuto4(canvas);
            }

            else if(step[9] || step[10]){
                drawIconTuto5(canvas);
            }

            else if(step[11]){
                drawRequestPractice(canvas);
            }
        }
    }

    public void setSize(int[] size){
        this.size = size;
        Bitmap arrow = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.move_right);
        Bitmap arrowOn = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.move_right_on);
        next = new NextBtn(arrow,arrowOn,size[0]-arrow.getWidth()*2, size[1]-arrow.getHeight()*2 );

        Log.d("Tutorial","Width : " + String.valueOf(size[0]) + "\n Height : " + String.valueOf(size[1]));
    }

    private void drawIconTuto(Canvas canvas){
        paint[0] = new Paint();
        paint[0].setColor(Color.YELLOW);
        paint[0].setStyle(Paint.Style.FILL);
        paint[0].setStrokeWidth(8.0f);
        paint[0].setAlpha(drawable_alpha);

        paint[1] = new Paint();
        paint[1].setAlpha(drawable_alpha);
        paint[1].setStyle(Paint.Style.FILL);
        paint[1].setStrokeWidth(8.0f);
        paint[1].setColor(Color.WHITE);
        paint[1].setTextSize(40.0f);
        paint[1].setTextAlign(Paint.Align.CENTER);

        paint[2] = new Paint();
        paint[2].setColor(Color.BLACK);
        paint[2].setTextSize(40.0f);
        paint[2].setTextAlign(Paint.Align.CENTER);
        paint[2].setAlpha(drawable_alpha);


        paint[3] = new Paint();
        paint[3].setColor(Color.BLACK);
        paint[3].setTextSize(35.0f);
        paint[3].setTextAlign(Paint.Align.CENTER);
        paint[3].setAlpha(drawable_alpha);

        paint[4] = new Paint();
        paint[4].setColor(Color.BLACK);
        paint[4].setTextSize(40.0f);
        paint[4].setTextAlign(Paint.Align.CENTER);
        paint[4].setAlpha(drawable_alpha);

        paint[5] = new Paint();
        paint[5].setColor(Color.BLACK);
        paint[5].setTextSize(35.0f);
        paint[5].setTextAlign(Paint.Align.LEFT);
        paint[5].setAlpha(drawable_alpha);

        if(step[0]){
            String question1 = mContext.getResources().getString(R.string.t01_string0);
            float width = paint[3].measureText(question1);

            canvas.drawRect(size[0]*9/10-width-paint[3].getTextSize()*2,size[1]/40,size[0]*9/10,size[1]/5,paint[0]);
            canvas.drawText(question1,((size[0]*9/10-width-paint[3].getTextSize()*2)+(size[0]*9/10))/2,((size[1]/40)+(size[1]/5))/2 +paint[3].getTextSize()/2,paint[3] );

        }

        if(step[1]){
            paint[0].setAlpha(255);
            paint[3].setAlpha(255);
            Bitmap character = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.appmenu);

            String question1 = mContext.getResources().getString(R.string.t01_string0);
            float width = paint[3].measureText(question1);

            canvas.drawRect(size[0]*9/10-width-paint[3].getTextSize()*2,size[1]/40,size[0]*9/10,size[1]/5,paint[0]);
            canvas.drawText(question1,((size[0]*9/10-width-paint[3].getTextSize()*2)+(size[0]*9/10))/2,((size[1]/40)+(size[1]/5))/2 +paint[3].getTextSize()/2,paint[3] );

            String answer1 = mContext.getResources().getString(R.string.t01_string1);
            float width1 = paint[4].measureText(answer1);
            canvas.drawRect(character.getWidth()*5/4,size[1]/5+character.getHeight()/2
                    ,character.getWidth()+width1+paint[4].getTextSize()*2
                    ,size[1]/5+character.getHeight()/2 + paint[4].getTextSize()*(7) + paint[3].getTextSize()*5/2 + paint[3].getTextSize()

                    ,paint[1]);
            canvas.drawText(answer1,((character.getWidth()*5/4)+(character.getWidth()+width1+paint[4].getTextSize()*2))/2
                    ,(size[1]/5+character.getHeight()/2)+((size[1]-character.getHeight()/2)-(size[1]/5+character.getHeight()/2))/10
                    ,paint[4]);

            String[] answer_array = {
                    mContext.getResources().getString(R.string.t01_string2),
                    mContext.getResources().getString(R.string.t01_string3),
                    mContext.getResources().getString(R.string.t01_string4),
                    mContext.getResources().getString(R.string.t01_string5),
            };

            for(int i=0; i<answer_array.length; i++){
                canvas.drawText(answer_array[i],character.getWidth()*5/4+paint[5].getTextSize()
                        ,size[1]/5+character.getHeight()/2 + paint[4].getTextSize()*(2*i+1) + paint[3].getTextSize()*5/2

                        ,paint[5]);
            }



            canvas.drawBitmap(character,0,size[1]/5,paint[1]);
            canvas.drawText("DRS",character.getWidth()/2, size[1]/5+character.getHeight()+paint[1].getTextSize(),paint[2]);

        }

        if(step[2]){
            paint[0].setAlpha(255);
            paint[1].setAlpha(255);
            paint[2].setAlpha(255);
            paint[3].setAlpha(255);
            paint[4].setAlpha(255);
            paint[5].setAlpha(255);

            Bitmap character = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.appmenu);

            String question1 = mContext.getResources().getString(R.string.t01_string0);
            float width = paint[3].measureText(question1);
            canvas.drawRect(size[0]*9/10-width-paint[3].getTextSize()*2,size[1]/40,size[0]*9/10,size[1]/5,paint[0]);
            canvas.drawText(question1,((size[0]*9/10-width-paint[3].getTextSize()*2)+(size[0]*9/10))/2,((size[1]/40)+(size[1]/5))/2 +paint[3].getTextSize()/2,paint[3] );

            String answer1 = mContext.getResources().getString(R.string.t01_string1);
            float width1 = paint[4].measureText(answer1);
            canvas.drawRect(character.getWidth()*5/4,size[1]/5+character.getHeight()/2
                    ,character.getWidth()+width1+paint[4].getTextSize()*2
                    ,size[1]/5+character.getHeight()/2 + paint[4].getTextSize()*(7) + paint[3].getTextSize()*5/2 + paint[3].getTextSize()

                    ,paint[1]);
            canvas.drawText(answer1,((character.getWidth()*5/4)+(character.getWidth()+width1+paint[4].getTextSize()*2))/2
                    ,(size[1]/5+character.getHeight()/2)+((size[1]-character.getHeight()/2)-(size[1]/5+character.getHeight()/2))/10
                    ,paint[4]);

            String[] answer_array = {
                    mContext.getResources().getString(R.string.t01_string2),
                    mContext.getResources().getString(R.string.t01_string3),
                    mContext.getResources().getString(R.string.t01_string4),
                    mContext.getResources().getString(R.string.t01_string5),
            };

            for(int i=0; i<answer_array.length; i++){
                canvas.drawText(answer_array[i],character.getWidth()*5/4+paint[5].getTextSize()
                        ,size[1]/5+character.getHeight()/2 + paint[4].getTextSize()*(2*i+1) + paint[3].getTextSize()*5/2

                        ,paint[5]);
            }

            left_menu_layout = (LinearLayout)mActivity.findViewById(R.id.left_layout);
//            left_menu_layout.setPadding(2,2,2,2);
            View icon_temp_view = new View(mContext){
                @Override
                protected void onDraw(Canvas canvas) {
                    super.onDraw(canvas);
                    Paint paint = new Paint();
                    paint.setColor(Color.RED);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(15.0f);

                    canvas.drawRect(0,0,this.getWidth(),this.getHeight(),paint);

                }
            };

            left_menu_layout.addView(icon_temp_view);
            icon_temp_view.invalidate();


            canvas.drawBitmap(character,0,size[1]/5,paint[1]);
            canvas.drawText("DRS",character.getWidth()/2, size[1]/5+character.getHeight()+paint[1].getTextSize(),paint[2]);

            canvas.drawBitmap(next.getArrow(),next.getX(),next.getY(),paint[1]);

        }
    }

    void drawIconTuto2(Canvas canvas){
        paint[0] = new Paint();
        paint[0].setColor(Color.RED);
        paint[0].setStyle(Paint.Style.FILL);
        paint[0].setStrokeWidth(8.0f);
        paint[0].setAlpha(drawable_alpha);

        paint[1] = new Paint();
        paint[1].setStyle(Paint.Style.FILL);
        paint[1].setStrokeWidth(8.0f);
        paint[1].setColor(Color.WHITE);
        paint[1].setTextSize(40.0f);
        paint[1].setTextAlign(Paint.Align.CENTER);
        paint[1].setAlpha(drawable_alpha);


        paint[2] = new Paint();
        paint[2].setColor(Color.BLACK);
        paint[2].setTextSize(40.0f);
        paint[2].setTextAlign(Paint.Align.CENTER);
        paint[2].setAlpha(drawable_alpha);


        paint[3] = new Paint();
        paint[3].setColor(Color.BLACK);
        paint[3].setTextSize(35.0f);
        paint[3].setTextAlign(Paint.Align.CENTER);
        paint[3].setAlpha(drawable_alpha);

        paint[4] = new Paint();
        paint[4].setColor(Color.BLACK);
        paint[4].setTextSize(40.0f);
        paint[4].setTextAlign(Paint.Align.CENTER);
        paint[4].setAlpha(drawable_alpha);

        paint[5] = new Paint();
        paint[5].setColor(Color.BLACK);
        paint[5].setTextSize(35.0f);
        paint[5].setTextAlign(Paint.Align.LEFT);
        paint[5].setAlpha(drawable_alpha);

        if(step[3]){
            Bitmap character = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.appmenu);

            canvas.drawRect(character.getWidth()*5/4,character.getHeight()/2
                    ,size[0]-character.getWidth()/2,size[1]-character.getWidth()/2,paint[1]);

            Bitmap[] motor = {
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.motor),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.motor3),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.motor5),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.motor6),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.motor9),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.motor10),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.motor11),
            };
            String motor_box_string ="Motor Box";
            float width = paint[4].measureText(motor_box_string);
            float rect_width = (size[0]-character.getWidth()/2) - character.getWidth()*5/4;
            float rect_height= size[1]-character.getWidth()/2 - character.getHeight()/2;
            for(int i=1; i<(motor.length); i++){
                canvas.drawLine(character.getWidth()*5/4 + rect_width/2,
                        character.getHeight()/2 + paint[4].getTextSize() + motor[0].getHeight(),
                        character.getWidth()*5/4 + rect_width*i/7 ,
                        character.getHeight()/2 + rect_height/2 - character.getHeight()/2,
                        paint[0]);
            }
            canvas.drawBitmap(motor[0], character.getWidth()*5/4 + rect_width/2 - motor[0].getWidth()/2,
                    character.getHeight()/2 + paint[4].getTextSize(),paint[4]);

            canvas.drawText(motor_box_string,character.getWidth()*5/4 + rect_width/2  + width,
                    character.getHeight()/2 + paint[4].getTextSize()+motor[0].getHeight()/2+paint[4].getTextSize()/2,paint[4]);

            for(int i=1; i<(motor.length); i++){
                canvas.drawBitmap(motor[i],character.getWidth()*5/4 + rect_width*i/7 - motor[i].getWidth()/2,
                        character.getHeight()/2 + rect_height/2 - motor[i].getHeight()/2,paint[4]);
            }

            String motor_Ex = "동작하고자 하는 모터의 번호에 따라 아이콘을 클릭하여 사용합니다.";
            canvas.drawText(motor_Ex,character.getWidth()*5/4 +rect_width/2, size[1]-character.getWidth()*2/3,paint[4]);


            canvas.drawBitmap(character,0,0,paint[1]);
            canvas.drawText("DRS",character.getWidth()/2, character.getHeight()+paint[1].getTextSize(),paint[2]);

        }

        if(step[4]){
            paint[0].setAlpha(255);
            paint[1].setAlpha(255);
            paint[2].setAlpha(255);
            paint[3].setAlpha(255);
            paint[4].setAlpha(255);
            paint[5].setAlpha(255);

            Bitmap character = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.appmenu);

            canvas.drawRect(character.getWidth()*5/4,character.getHeight()/2
                    ,size[0]-character.getWidth()/2,size[1]-character.getWidth()/2,paint[1]);

            Bitmap[] motor = {
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.motor),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.motor3),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.motor5),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.motor6),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.motor9),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.motor10),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.motor11),
            };
            String motor_box_string ="Motor Box";
            float width = paint[4].measureText(motor_box_string);
            float rect_width = (size[0]-character.getWidth()/2) - character.getWidth()*5/4;
            float rect_height= size[1]-character.getWidth()/2 - character.getHeight()/2;
            for(int i=1; i<(motor.length); i++){
                canvas.drawLine(character.getWidth()*5/4 + rect_width/2,
                        character.getHeight()/2 + paint[4].getTextSize() + motor[0].getHeight(),
                        character.getWidth()*5/4 + rect_width*i/7 ,
                        character.getHeight()/2 + rect_height/2 - character.getHeight()/2,
                        paint[0]);
            }
            canvas.drawBitmap(motor[0], character.getWidth()*5/4 + rect_width/2 - motor[0].getWidth()/2,
                    character.getHeight()/2 + paint[4].getTextSize(),paint[4]);

            canvas.drawText(motor_box_string,character.getWidth()*5/4 + rect_width/2  + width,
                    character.getHeight()/2 + paint[4].getTextSize()+motor[0].getHeight()/2+paint[4].getTextSize()/2,paint[4]);

            for(int i=1; i<(motor.length); i++){
                canvas.drawBitmap(motor[i],character.getWidth()*5/4 + rect_width*i/7 - motor[i].getWidth()/2,
                        character.getHeight()/2 + rect_height/2 - motor[i].getHeight()/2,paint[4]);
            }

            String motor_Ex = "동작하고자 하는 모터의 번호에 따라 아이콘을 클릭하여 사용합니다.";
            canvas.drawText(motor_Ex,character.getWidth()*5/4 +rect_width/2, size[1]-character.getWidth()*2/3,paint[4]);


            canvas.drawBitmap(character,0,0,paint[1]);
            canvas.drawText("DRS",character.getWidth()/2, character.getHeight()+paint[1].getTextSize(),paint[2]);

            canvas.drawBitmap(next.getArrow(),next.getX(),next.getY(),paint[0]);
        }
    }

    void drawIconTuto3(Canvas canvas){
        paint[0] = new Paint();
        paint[0].setColor(Color.RED);
        paint[0].setStyle(Paint.Style.FILL);
        paint[0].setStrokeWidth(8.0f);
        paint[0].setAlpha(drawable_alpha);

        paint[1] = new Paint();
        paint[1].setStyle(Paint.Style.FILL);
        paint[1].setStrokeWidth(8.0f);
        paint[1].setColor(Color.WHITE);
        paint[1].setTextSize(40.0f);
        paint[1].setTextAlign(Paint.Align.CENTER);
        paint[1].setAlpha(drawable_alpha);


        paint[2] = new Paint();
        paint[2].setColor(Color.BLACK);
        paint[2].setTextSize(40.0f);
        paint[2].setTextAlign(Paint.Align.CENTER);
        paint[2].setAlpha(drawable_alpha);


        paint[3] = new Paint();
        paint[3].setColor(Color.BLACK);
        paint[3].setTextSize(35.0f);
        paint[3].setTextAlign(Paint.Align.CENTER);
        paint[3].setAlpha(drawable_alpha);

        paint[4] = new Paint();
        paint[4].setColor(Color.BLACK);
        paint[4].setTextSize(40.0f);
        paint[4].setTextAlign(Paint.Align.CENTER);
        paint[4].setAlpha(drawable_alpha);

        paint[5] = new Paint();
        paint[5].setColor(Color.BLACK);
        paint[5].setTextSize(35.0f);
        paint[5].setTextAlign(Paint.Align.LEFT);
        paint[5].setAlpha(drawable_alpha);

        if(step[5]){
            Bitmap character = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.appmenu);

            canvas.drawRect(character.getWidth()*5/4,character.getHeight()/2
                    ,size[0]-character.getWidth()/2,size[1]-character.getWidth()/2,paint[1]);

            Bitmap[] timer = {
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.timer),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.timer_sec),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.timer_millis),
            };
            String motor_box_string ="Motor Box";
            float width = paint[4].measureText(motor_box_string);
            float rect_width = (size[0]-character.getWidth()/2) - character.getWidth()*5/4;
            float rect_height= size[1]-character.getWidth()/2 - character.getHeight()/2;
            for(int i=1; i<(timer.length); i++){
                canvas.drawLine(character.getWidth()*5/4 + rect_width/2,
                        character.getHeight()/2 + paint[4].getTextSize() + timer[0].getHeight(),
                        character.getWidth()*5/4 + rect_width*i/timer.length ,
                        character.getHeight()/2 + rect_height/2 - character.getHeight()/2,
                        paint[0]);
            }
            canvas.drawBitmap(timer[0], character.getWidth()*5/4 + rect_width/2 - timer[0].getWidth()/2,
                    character.getHeight()/2 + paint[4].getTextSize(),paint[4]);

            canvas.drawText(motor_box_string,character.getWidth()*5/4 + rect_width/2  + width,
                    character.getHeight()/2 + paint[4].getTextSize()+timer[0].getHeight()/2+paint[4].getTextSize()/2,paint[4]);

            for(int i=1; i<(timer.length); i++){
                canvas.drawBitmap(timer[i],character.getWidth()*5/4 + rect_width*i/timer.length - timer[i].getWidth()/2,
                        character.getHeight()/2 + rect_height/2 - timer[i].getHeight()/2,paint[4]);
            }

            String[] temp ={"단위 : sec", "단위 : millisecond"};
            canvas.drawText(temp[0],character.getWidth()*5/4 + rect_width/timer.length ,
                    character.getHeight()/2 + rect_height/2 - timer[0].getHeight()/2 + timer[0].getHeight() + paint[4].getTextSize()*3/2,
                    paint[4]
            );

            canvas.drawText(temp[1],character.getWidth()*5/4 + rect_width*2/timer.length ,
                    character.getHeight()/2 + rect_height/2 - timer[1].getHeight()/2 + timer[1].getHeight() + paint[4].getTextSize()*3/2,
                    paint[4]
            );

            String timer_Ex = "특정 동작을 유지하고자 하는 시간을 설정할 수 있는 명령입니다.";
            canvas.drawText(timer_Ex,character.getWidth()*5/4 +rect_width/2, size[1]-character.getWidth()*2/3,paint[4]);


            canvas.drawBitmap(character,0,0,paint[1]);
            canvas.drawText("DRS",character.getWidth()/2, character.getHeight()+paint[1].getTextSize(),paint[2]);

        }

        if(step[6]){
            paint[0].setAlpha(255);
            paint[1].setAlpha(255);
            paint[2].setAlpha(255);
            paint[3].setAlpha(255);
            paint[4].setAlpha(255);
            paint[5].setAlpha(255);

            Bitmap character = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.appmenu);

            canvas.drawRect(character.getWidth()*5/4,character.getHeight()/2
                    ,size[0]-character.getWidth()/2,size[1]-character.getWidth()/2,paint[1]);

            Bitmap[] timer = {
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.timer),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.timer_sec),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.timer_millis)
            };
            String motor_box_string ="Timer Box";
            float width = paint[4].measureText(motor_box_string);
            float rect_width = (size[0]-character.getWidth()/2) - character.getWidth()*5/4;
            float rect_height= size[1]-character.getWidth()/2 - character.getHeight()/2;
            for(int i=1; i<(timer.length); i++){
                canvas.drawLine(character.getWidth()*5/4 + rect_width/2,
                        character.getHeight()/2 + paint[4].getTextSize() + timer[0].getHeight(),
                        character.getWidth()*5/4 + rect_width*i/timer.length ,
                        character.getHeight()/2 + rect_height/2 - character.getHeight()/2,
                        paint[0]);
            }
            canvas.drawBitmap(timer[0], character.getWidth()*5/4 + rect_width/2 - timer[0].getWidth()/2,
                    character.getHeight()/2 + paint[4].getTextSize(),paint[4]);

            canvas.drawText(motor_box_string,character.getWidth()*5/4 + rect_width/2  + width,
                    character.getHeight()/2 + paint[4].getTextSize()+timer[0].getHeight()/2+paint[4].getTextSize()/2,paint[4]);

            for(int i=1; i<(timer.length); i++){
                canvas.drawBitmap(timer[i],character.getWidth()*5/4 + rect_width*i/timer.length - timer[i].getWidth()/2,
                        character.getHeight()/2 + rect_height/2 - timer[i].getHeight()/2,paint[4]);
            }

            String[] temp ={"단위 : sec", "단위 : millisecond"};
            canvas.drawText(temp[0],character.getWidth()*5/4 + rect_width/timer.length,
                    character.getHeight()/2 + rect_height/2 - timer[0].getHeight()/2 + timer[0].getHeight() + paint[4].getTextSize()*3/2,
                    paint[4]
            );

            canvas.drawText(temp[1],character.getWidth()*5/4 + rect_width*2/timer.length ,
                    character.getHeight()/2 + rect_height/2 - timer[1].getHeight()/2 + timer[1].getHeight() + paint[4].getTextSize()*3/2,
                    paint[4]
            );

            String timer_Ex = "특정 동작을 유지하고자 하는 시간을 설정할 수 있는 명령입니다.";
            canvas.drawText(timer_Ex,character.getWidth()*5/4 +rect_width/2, size[1]-character.getWidth()*2/3,paint[4]);


            canvas.drawBitmap(character,0,0,paint[1]);
            canvas.drawText("DRS",character.getWidth()/2, character.getHeight()+paint[1].getTextSize(),paint[2]);

            canvas.drawBitmap(next.getArrow(),next.getX(),next.getY(),paint[0]);
        }
    }

    void drawIconTuto5(Canvas canvas){
        paint[0] = new Paint();
        paint[0].setColor(Color.RED);
        paint[0].setStyle(Paint.Style.FILL);
        paint[0].setStrokeWidth(8.0f);
        paint[0].setAlpha(drawable_alpha);

        paint[1] = new Paint();
        paint[1].setStyle(Paint.Style.FILL);
        paint[1].setStrokeWidth(8.0f);
        paint[1].setColor(Color.WHITE);
        paint[1].setTextSize(40.0f);
        paint[1].setTextAlign(Paint.Align.CENTER);
        paint[1].setAlpha(drawable_alpha);


        paint[2] = new Paint();
        paint[2].setColor(Color.BLACK);
        paint[2].setTextSize(40.0f);
        paint[2].setTextAlign(Paint.Align.CENTER);
        paint[2].setAlpha(drawable_alpha);


        paint[3] = new Paint();
        paint[3].setColor(Color.BLACK);
        paint[3].setTextSize(35.0f);
        paint[3].setTextAlign(Paint.Align.CENTER);
        paint[3].setAlpha(drawable_alpha);

        paint[4] = new Paint();
        paint[4].setColor(Color.BLACK);
        paint[4].setTextSize(40.0f);
        paint[4].setTextAlign(Paint.Align.CENTER);
        paint[4].setAlpha(drawable_alpha);

        paint[5] = new Paint();
        paint[5].setColor(Color.BLACK);
        paint[5].setTextSize(35.0f);
        paint[5].setTextAlign(Paint.Align.LEFT);
        paint[5].setAlpha(drawable_alpha);

        if(step[9]){
            Bitmap character = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.appmenu);

            canvas.drawRect(character.getWidth()*5/4,character.getHeight()/2
                    ,size[0]-character.getWidth()/2,size[1]-character.getWidth()/2,paint[1]);

            Bitmap[] basic = {
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.start),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.start),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.end)
            };


            String motor_box_string ="Start/End Icon";
            float width = paint[4].measureText(motor_box_string);
            float rect_width = (size[0]-character.getWidth()/2) - character.getWidth()*5/4;
            float rect_height= size[1]-character.getWidth()/2 - character.getHeight()/2;
            for(int i=1; i<(basic.length); i++){
                canvas.drawLine(character.getWidth()*5/4 + rect_width/2,
                        character.getHeight()/2 + paint[4].getTextSize() + basic[0].getHeight(),
                        character.getWidth()*5/4 + rect_width*i/basic.length ,
                        character.getHeight()/2 + rect_height/2 - character.getHeight()/2,
                        paint[0]);
            }

            canvas.drawText(motor_box_string,character.getWidth()*5/4 + rect_width/2 ,
                    character.getHeight()/2 + paint[4].getTextSize()+basic[0].getHeight()/2+paint[4].getTextSize()/2,paint[4]);

            for(int i=1; i<(basic.length); i++){
                canvas.drawBitmap(basic[i],character.getWidth()*5/4 + rect_width*i/basic.length - basic[i].getWidth()/2,
                        character.getHeight()/2 + rect_height/2 - basic[i].getHeight()/2,paint[4]);
            }

            String timer_Ex = "프로그램의 시작과 끝을 알리는 필수 아이콘 입니다.";
            canvas.drawText(timer_Ex,character.getWidth()*5/4 +rect_width/2, size[1]-character.getWidth()*2/3,paint[4]);

            canvas.drawBitmap(character,0,0,paint[1]);
            canvas.drawText("DRS",character.getWidth()/2, character.getHeight()+paint[1].getTextSize(),paint[2]);
        }

        if(step[10]){
            paint[0].setAlpha(255);
            paint[1].setAlpha(255);
            paint[2].setAlpha(255);
            paint[3].setAlpha(255);
            paint[4].setAlpha(255);
            paint[5].setAlpha(255);

            Bitmap character = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.appmenu);

            canvas.drawRect(character.getWidth()*5/4,character.getHeight()/2
                    ,size[0]-character.getWidth()/2,size[1]-character.getWidth()/2,paint[1]);

            Bitmap[] basic = {
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.start),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.start),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.end)
            };


            String motor_box_string ="Start/End Icon";
            float width = paint[4].measureText(motor_box_string);
            float rect_width = (size[0]-character.getWidth()/2) - character.getWidth()*5/4;
            float rect_height= size[1]-character.getWidth()/2 - character.getHeight()/2;
            for(int i=1; i<(basic.length); i++){
                canvas.drawLine(character.getWidth()*5/4 + rect_width/2,
                        character.getHeight()/2 + paint[4].getTextSize() + basic[0].getHeight(),
                        character.getWidth()*5/4 + rect_width*i/basic.length ,
                        character.getHeight()/2 + rect_height/2 - character.getHeight()/2,
                        paint[0]);
            }

            canvas.drawText(motor_box_string,character.getWidth()*5/4 + rect_width/2 ,
                    character.getHeight()/2 + paint[4].getTextSize()+basic[0].getHeight()/2+paint[4].getTextSize()/2,paint[4]);

            for(int i=1; i<(basic.length); i++){
                canvas.drawBitmap(basic[i],character.getWidth()*5/4 + rect_width*i/basic.length - basic[i].getWidth()/2,
                        character.getHeight()/2 + rect_height/2 - basic[i].getHeight()/2,paint[4]);
            }


            String timer_Ex = "프로그램의 시작과 끝을 알리는 필수 아이콘 입니다.";
            canvas.drawText(timer_Ex,character.getWidth()*5/4 +rect_width/2, size[1]-character.getWidth()*2/3,paint[4]);


            canvas.drawBitmap(character,0,0,paint[1]);
            canvas.drawText("DRS",character.getWidth()/2, character.getHeight()+paint[1].getTextSize(),paint[2]);

            canvas.drawBitmap(next.getArrow(),next.getX(),next.getY(),paint[0]);
        }
    }

    void drawIconTuto4(Canvas canvas){
        paint[0] = new Paint();
        paint[0].setColor(Color.RED);
        paint[0].setStyle(Paint.Style.FILL);
        paint[0].setStrokeWidth(8.0f);
        paint[0].setAlpha(drawable_alpha);

        paint[1] = new Paint();
        paint[1].setStyle(Paint.Style.FILL);
        paint[1].setStrokeWidth(8.0f);
        paint[1].setColor(Color.WHITE);
        paint[1].setTextSize(40.0f);
        paint[1].setTextAlign(Paint.Align.CENTER);
        paint[1].setAlpha(drawable_alpha);


        paint[2] = new Paint();
        paint[2].setColor(Color.BLACK);
        paint[2].setTextSize(40.0f);
        paint[2].setTextAlign(Paint.Align.CENTER);
        paint[2].setAlpha(drawable_alpha);


        paint[3] = new Paint();
        paint[3].setColor(Color.BLACK);
        paint[3].setTextSize(35.0f);
        paint[3].setTextAlign(Paint.Align.CENTER);
        paint[3].setAlpha(drawable_alpha);

        paint[4] = new Paint();
        paint[4].setColor(Color.BLACK);
        paint[4].setTextSize(40.0f);
        paint[4].setTextAlign(Paint.Align.CENTER);
        paint[4].setAlpha(drawable_alpha);

        paint[5] = new Paint();
        paint[5].setColor(Color.BLACK);
        paint[5].setTextSize(35.0f);
        paint[5].setTextAlign(Paint.Align.LEFT);
        paint[5].setAlpha(drawable_alpha);

        if(step[7]){
            Bitmap character = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.appmenu);

            canvas.drawRect(character.getWidth()*5/4,character.getHeight()/2
                    ,size[0]-character.getWidth()/2,size[1]-character.getWidth()/2,paint[1]);

            Bitmap[] jump = {
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump1_up),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump1_up),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump2_up),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump3_up),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump4_up)
            };

            Bitmap[] jump_down = {
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump1_down),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump1_down),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump2_down),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump3_down),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump4_down)
            };
            String motor_box_string ="Jump Box";
            float width = paint[4].measureText(motor_box_string);
            float rect_width = (size[0]-character.getWidth()/2) - character.getWidth()*5/4;
            float rect_height= size[1]-character.getWidth()/2 - character.getHeight()/2;
            for(int i=1; i<(jump.length); i++){
                canvas.drawLine(character.getWidth()*5/4 + rect_width/2,
                        character.getHeight()/2 + paint[4].getTextSize() + jump[0].getHeight(),
                        character.getWidth()*5/4 + rect_width*i/jump.length ,
                        character.getHeight()/2 + rect_height/2 - character.getHeight()/2,
                        paint[0]);
            }
            canvas.drawBitmap(jump[0], character.getWidth()*5/4 + rect_width/2 - jump[0].getWidth()/2,
                    character.getHeight()/2 + paint[4].getTextSize(),paint[4]);

            canvas.drawText(motor_box_string,character.getWidth()*5/4 + rect_width/2  + width,
                    character.getHeight()/2 + paint[4].getTextSize()+jump[0].getHeight()/2+paint[4].getTextSize()/2,paint[4]);

            for(int i=1; i<(jump.length); i++){
                canvas.drawBitmap(jump[i],character.getWidth()*5/4 + rect_width*i/jump.length - jump[i].getWidth()/2,
                        character.getHeight()/2 + rect_height/2 - jump[i].getHeight()/2,paint[4]);
            }
            for(int i=1; i<(jump.length); i++){
                canvas.drawBitmap(jump_down[i],character.getWidth()*5/4 + rect_width*i/jump.length - jump_down[i].getWidth()/2,
                        character.getHeight()/2 + rect_height/2 + jump_down[i].getHeight()/2,paint[4]);
            }

            String timer_Ex = "명령어 진행위치를 특정 위치로 변화시키기 위해 사용합니다.";
            canvas.drawText(timer_Ex,character.getWidth()*5/4 +rect_width/2, size[1]-character.getWidth()*2/3,paint[4]);

            canvas.drawBitmap(character,0,0,paint[1]);
            canvas.drawText("DRS",character.getWidth()/2, character.getHeight()+paint[1].getTextSize(),paint[2]);
        }

        if(step[8]){
            paint[0].setAlpha(255);
            paint[1].setAlpha(255);
            paint[2].setAlpha(255);
            paint[3].setAlpha(255);
            paint[4].setAlpha(255);
            paint[5].setAlpha(255);

            Bitmap character = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.appmenu);

            canvas.drawRect(character.getWidth()*5/4,character.getHeight()/2
                    ,size[0]-character.getWidth()/2,size[1]-character.getWidth()/2,paint[1]);

            Bitmap[] jump = {
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump1_up),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump1_up),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump2_up),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump3_up),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump4_up)
            };

            Bitmap[] jump_down = {
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump1_down),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump1_down),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump2_down),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump3_down),
                    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump4_down)
            };
            String motor_box_string ="Jump Box";
            float width = paint[4].measureText(motor_box_string);
            float rect_width = (size[0]-character.getWidth()/2) - character.getWidth()*5/4;
            float rect_height= size[1]-character.getWidth()/2 - character.getHeight()/2;
            for(int i=1; i<(jump.length); i++){
                canvas.drawLine(character.getWidth()*5/4 + rect_width/2,
                        character.getHeight()/2 + paint[4].getTextSize() + jump[0].getHeight(),
                        character.getWidth()*5/4 + rect_width*i/jump.length ,
                        character.getHeight()/2 + rect_height/2 - character.getHeight()/2,
                        paint[0]);
            }
            canvas.drawBitmap(jump[0], character.getWidth()*5/4 + rect_width/2 - jump[0].getWidth()/2,
                    character.getHeight()/2 + paint[4].getTextSize(),paint[4]);

            canvas.drawText(motor_box_string,character.getWidth()*5/4 + rect_width/2  + width,
                    character.getHeight()/2 + paint[4].getTextSize()+jump[0].getHeight()/2+paint[4].getTextSize()/2,paint[4]);

            for(int i=1; i<(jump.length); i++){
                canvas.drawBitmap(jump[i],character.getWidth()*5/4 + rect_width*i/jump.length - jump[i].getWidth()/2,
                        character.getHeight()/2 + rect_height/2 - jump[i].getHeight()/2,paint[4]);
            }
            for(int i=1; i<(jump.length); i++){
                canvas.drawBitmap(jump_down[i],character.getWidth()*5/4 + rect_width*i/jump.length - jump_down[i].getWidth()/2,
                        character.getHeight()/2 + rect_height/2 + jump_down[i].getHeight()/2,paint[4]);
            }


            String timer_Ex = "명령어 진행위치를 특정 위치로 변화시키기 위해 사용합니다.";
            canvas.drawText(timer_Ex,character.getWidth()*5/4 +rect_width/2, size[1]-character.getWidth()*2/3,paint[4]);


            canvas.drawBitmap(character,0,0,paint[1]);
            canvas.drawText("DRS",character.getWidth()/2, character.getHeight()+paint[1].getTextSize(),paint[2]);

            canvas.drawBitmap(next.getArrow(),next.getX(),next.getY(),paint[0]);
        }
    }

    private void drawRequestPractice(Canvas canvas){
        paint[1] = new Paint();
        paint[1].setTextSize(70.0f);
        paint[1].setTextAlign(Paint.Align.CENTER);
        paint[1].setColor(Color.WHITE);
        paint[1].setAlpha(drawable_alpha);

        paint[2] = new Paint();
        paint[2].setColor(Color.BLACK);
        paint[2].setTextSize(40.0f);
        paint[2].setTextAlign(Paint.Align.CENTER);
        paint[2].setAlpha(drawable_alpha);

        paint[4] = new Paint();
        paint[4].setColor(Color.BLACK);
        paint[4].setTextSize(40.0f);
        paint[4].setTextAlign(Paint.Align.CENTER);
        paint[4].setAlpha(drawable_alpha);

        Bitmap character = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.appmenu);

        String answer1 = mContext.getResources().getString(R.string.t0_string14);
        String answer2 = "아이콘 배치에 대한 내용은 다음 튜토리얼에서 진행하겠습니다.";
        float width1 = paint[4].measureText(answer2);
        canvas.drawRect(character.getWidth()*5/4,size[1]/5+character.getHeight()/2
                ,character.getWidth()+width1+paint[4].getTextSize()*2, size[1]/5+character.getHeight()/2  + paint[4].getTextSize()*5,paint[1]);
        canvas.drawText(answer1,((character.getWidth()*5/4)+(character.getWidth()+width1+paint[4].getTextSize()*2))/2,
                size[1]/5+character.getHeight()/2  + paint[4].getTextSize()*2
                ,paint[4]);

        canvas.drawText(answer2,((character.getWidth()*5/4)+(character.getWidth()+width1+paint[4].getTextSize()*2))/2,
                size[1]/5+character.getHeight()/2  + paint[4].getTextSize()*4
                ,paint[4]);


        canvas.drawBitmap(character,0,size[1]/5,paint[1]);
        canvas.drawText("DRS",character.getWidth()/2, size[1]/5+character.getHeight()+paint[1].getTextSize(),paint[2]);
    }

    private class MyHandler extends Handler{
        public MyHandler() {
            super();
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch(msg.what){
                case UPDATE_UI :
                    invalidate();
                    break;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(step[2] || step[4] || step[6] || step[8]){
                Bitmap temp = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.move_right);
                if(event.getX()>next.getX() && event.getX() < next.getX() +temp.getWidth()){
                    if(event.getY() > next.getY() && event.getY() < next.getY()+temp.getHeight()){
                        Log.d(TAG,"Clicked");

                        next.clicked();
                        invalidate();

                    }
                }
            }
        }
        else if(event.getAction() == MotionEvent.ACTION_UP){
            if(step[0] || step[1]) {
                drawable_alpha = 255;
                step[0] = false;
                step[1] = false;
                step[2] = true;
            }

            else if(step[2]){
                Bitmap temp = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.move_right);
                if(event.getX()>next.getX() && event.getX() < next.getX() +temp.getWidth()){
                    if(event.getY() > next.getY() && event.getY() < next.getY()+temp.getHeight()){
                        mSoundManager.play(0);
                        try{
                            Thread.sleep(20);
                        }catch (InterruptedException e){};
                        next.unclicked();
                        step[2] = false;
                        step[3] = true;
                        drawable_alpha = 0;
                        invalidate();

                    }
                }
            }
            else if(step[3]){
                step[3] = false;
                step[4]= true;
            }
            else if(step[4]){
                Bitmap temp = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.move_right);
                if(event.getX()>next.getX() && event.getX() < next.getX() +temp.getWidth()){
                    if(event.getY() > next.getY() && event.getY() < next.getY()+temp.getHeight()){
                        mSoundManager.play(0);
                        try{
                            Thread.sleep(20);
                        }catch (InterruptedException e){};
                        next.unclicked();
                        step[4] = false;
                        step[5] = true;
                        drawable_alpha = 0;
                        invalidate();
                    }
                }
            }
            else if(step[5] ){
                step[5] = false;
                step[6] = true;
            }
            else if(step[6]){
                Bitmap temp = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.move_right);
                if(event.getX()>next.getX() && event.getX() < next.getX() +temp.getWidth()){
                    if(event.getY() > next.getY() && event.getY() < next.getY()+temp.getHeight()){
                        mSoundManager.play(0);
                        try{
                            Thread.sleep(20);
                        }catch (InterruptedException e){};
                        next.unclicked();
                        step[6] = false;
                        step[7] = true;
                        drawable_alpha = 0;
                        invalidate();
                    }
                }

            }

            else if(step[7] ){
                step[7] = false;
                step[8] = true;
            }
            else if(step[8]){
                Bitmap temp = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.move_right);
                if(event.getX()>next.getX() && event.getX() < next.getX() +temp.getWidth()){
                    if(event.getY() > next.getY() && event.getY() < next.getY()+temp.getHeight()){
                        mSoundManager.play(0);
                        try{
                            Thread.sleep(20);
                        }catch (InterruptedException e){};
                        next.unclicked();
                        step[8] = false;
                        step[9] = true;
                        drawable_alpha = 0;
                        invalidate();
                    }
                }

            }

            else if(step[9] ){
                step[9] = false;
                step[10] = true;
            }
            else if(step[10]){
                Bitmap temp = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.move_right);
                if(event.getX()>next.getX() && event.getX() < next.getX() +temp.getWidth()){
                    if(event.getY() > next.getY() && event.getY() < next.getY()+temp.getHeight()){
                        mSoundManager.play(0);
                        try{
                            Thread.sleep(20);
                        }catch (InterruptedException e){};
                        next.unclicked();
                        step[10] = false;
                        step[11] = true;
                        drawable_alpha = 0;
                        left_menu_layout.removeAllViews();
                        invalidate();
                    }
                }

            }
            else if(step[11]){
                mActivity.finish();
            }
        }
        return true;
    }

    private class NextBtn{
        Bitmap arrow, arrowOn, display_arrow;
        int x,y ;

        public NextBtn(Bitmap arrow,Bitmap arrowOn, int x, int y) {
            this.arrow = arrow;
            this.arrowOn = arrowOn;
            this.x = x;
            this.y = y;
            display_arrow = arrow;

        }

        public Bitmap getArrow(){
            return display_arrow;
        }

        public int getX(){
            return x;
        }

        public int getY(){
            return y;
        }

        public void clicked(){
            display_arrow = arrowOn;
        }

        public void unclicked(){
            display_arrow  =arrow;
        }



    }

    public void setStopThread(){
        for(int i=0; i<step.length;i++){
            step[i] = false;
        }
    }



}
