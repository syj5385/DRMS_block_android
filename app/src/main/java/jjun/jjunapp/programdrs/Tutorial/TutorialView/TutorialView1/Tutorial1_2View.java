package jjun.jjunapp.programdrs.Tutorial.TutorialView.TutorialView1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import jjun.jjunapp.programdrs.R;
import jjun.jjunapp.programdrs.Sound.SoundManager;
import jjun.jjunapp.programdrs.Tutorial.TransparentActivity;

import static android.content.ContentValues.TAG;

/**
 * Created by jjunj on 2017-10-11.
 */

public class Tutorial1_2View extends View {

    private static final int UPDATE_UI = 0 ;

    private Context mContext;
    private Activity mActivity;
    private Handler mHandler;

    private Handler T0_Handler;

    private int[] size = new int[2];

    private int drawable_alpha = 0;
    private Paint[] paint = new Paint[7];
    private boolean[] step = new boolean[10];
    private String[] content = {"","",""};
    private String[] current_content = {"","",""};

    private LinearLayout command_window;
    private ImageView app_menu_btn;
    private SoundManager mSoundManager;
    private NextBtn next;

    private boolean enable_Command_onTouch = false;

    private int[] positionX;
    private int[] positionY;


    public Tutorial1_2View(final Context context, final Activity mActivity, Handler mHandler) {
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
                try{
                    Thread.sleep(500);
                }catch (InterruptedException e){};
                while(step[0]){
                    if(drawable_alpha < 255) {
                        drawable_alpha += 3;
                        T0_Handler.obtainMessage(UPDATE_UI).sendToTarget();
                    }
                    else{
                        step[1] = true;
                        step[0] = false;
                    }
                    try{
                        Thread.sleep(20);
                    }catch (InterruptedException e){}
                }

                int[] index = {1,1,1};
                content[0] = mContext.getResources().getString(R.string.t10_string0);
                content[1] = mContext.getResources().getString(R.string.t10_string1);
                content[2] = mContext.getResources().getString(R.string.t10_string2);

                while(step[1]){
                    current_content[0] = "";
                    current_content[1] = "";
                    current_content[2] = "";
                    int max = content[0].length();
                    if(content[1].length() > max){
                        max = content[1].length();
                    }
                    if(content[2].length() > max){
                        max = content[2].length();
                    }

                    if(index[0] <= content[0].length()) {
                        for (int i = 0; i < index[0]; i++) {
                            current_content[0] += content[0].charAt(i);
                        }
                        index[0] ++;
                    }
                    else{
                        current_content[0] = mContext.getResources().getString(R.string.t10_string0);
                    }
                    if(index[1] <= content[1].length()) {
                        for (int i = 0; i < index[1]; i++) {
                            current_content[1] += content[1].charAt(i);
                        }
                        index[1] ++;
                    }
                    else{
                        current_content[1] = mContext.getResources().getString(R.string.t10_string1);
                    }
                    if(index[2] <= content[2].length()) {
                        for (int i = 0; i < index[2]; i++) {
                            current_content[2] += content[2].charAt(i);
                        }
                        index[2] ++;
                    }
                    else{
                        current_content[2] = mContext.getResources().getString(R.string.t10_string2);
                    }


                    if(index[0] == max+1 || index[1] == max+1 || index[2] == max+1){
                        step[1] = false;
                        step[2] = true;
                    }
                    else{
                        T0_Handler.obtainMessage(UPDATE_UI).sendToTarget();
                        try{
                            Thread.sleep(140);
                        }catch (InterruptedException e){};
                    }
                }

                while(step[2]){
                    T0_Handler.obtainMessage(UPDATE_UI).sendToTarget();
                    try{
                        Thread.sleep(100);
                    }catch (InterruptedException e){};

                }

                int waitCount = 0;

                drawable_alpha = 0;
                try{
                    Thread.sleep(100);
                }catch (InterruptedException e){};

                while(step[3]){
                    if(drawable_alpha == 0) {
                        drawable_alpha = 255;
                        T0_Handler.obtainMessage(UPDATE_UI).sendToTarget();
                    }
                    else{
                        waitCount++;
                        if(waitCount == 100){
                            step[4] = true;
                            step[3] = false;
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

                while(step[4]){
                    if(drawable_alpha == 0){
                        drawable_alpha = 255;
                        T0_Handler.obtainMessage(UPDATE_UI).sendToTarget();
                    }
                    else{
                        waitCount++;
                        if(waitCount == 100){
                            step[5] = true;
                            step[4] = false;
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

                while(step[5]){
                    try{
                        T0_Handler.obtainMessage(UPDATE_UI).sendToTarget();
                        Thread.sleep(100);
                    }catch (InterruptedException e){};
                }
                try{
                    Thread.sleep(100);
                }catch (InterruptedException e){};

                waitCount = 0;

                try{
                    Thread.sleep(100);
                }catch (InterruptedException e){};
                while(step[6]){
                    if(drawable_alpha == 0){
                        drawable_alpha = 255;
                        T0_Handler.obtainMessage(UPDATE_UI).sendToTarget();
                    }
                    else{
                        waitCount++;
                        if(waitCount == 100){
                            step[7] = true;
                            step[6] = false;
                            Log.d(TAG,"OK");
                            Log.d(TAG,"enter step8");
                            drawable_alpha = 0;
                            enable_Command_onTouch = true;

                        }
                    }
                    try{
                        Thread.sleep(20);
                    }catch (InterruptedException e){};
                }
                waitCount = 0;

                while(step[7]){
                    try{
                        T0_Handler.obtainMessage(UPDATE_UI).sendToTarget();
                        Thread.sleep(100);
                    }catch (InterruptedException e){};

                }

                waitCount = 0;
                try{
                    Thread.sleep(100);
                }catch (InterruptedException e){};

                while(step[8]){
                    if(drawable_alpha == 0){
                        drawable_alpha = 255;
                        T0_Handler.obtainMessage(UPDATE_UI).sendToTarget();
                    }
                    else{
                        waitCount++;
                        if(waitCount == 200){
                            setStopThread();
                            Intent intent = new Intent();
                            intent.putExtra("index",1);
                            intent.setAction(TransparentActivity.REQUEST_SETTING_LAYOUT);
                            Log.d(TAG,"start thread");
                            mActivity.sendBroadcast(intent);
                            mActivity.finish();
                        }
                    }
                    try{
                        Thread.sleep(20);
                    }catch (InterruptedException e){};
                }

                while(step[9]){
                    try{
                        T0_Handler.obtainMessage(UPDATE_UI).sendToTarget();
                        Thread.sleep(100);
                    }catch (InterruptedException e){};

                }


            }
        }).start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(size[0] != 0 && size[1] != 0) {
            if (step[0] || step[1]) {
                drawIntro(canvas);
            } else if (step[2]) {
                drawAllIntro(canvas);
            }
            else if(step[3] || step[4] || step[5]){
                drawAppMenuTuto(canvas);
            }
            else if(step[6] || step[7]){
                drawAppMenuTuto1(canvas);
            }
//            else if(step[8] || step[9]){
//                drawAppMenuTuto2(canvas);
//            }
            else if(step[8]){
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

    private void drawIntro(Canvas canvas){
        paint[0] = new Paint();
        paint[0].setStyle(Paint.Style.STROKE);
        paint[0].setStrokeWidth(8.0f);
        paint[0].setColor(Color.GRAY);
        paint[0].setAlpha(drawable_alpha);

        paint[1] = new Paint();
        paint[1].setTextSize(70.0f);
        paint[1].setTextAlign(Paint.Align.CENTER);
        paint[1].setColor(Color.RED);
        paint[1].setAlpha(drawable_alpha);

        paint[2] = new Paint();
        paint[2].setTextSize(70.0f);
        paint[2].setTextAlign(Paint.Align.CENTER);
        paint[2].setColor(Color.BLACK);
        paint[2].setAlpha(drawable_alpha);


        canvas.drawRect(size[0]/2-(paint[1].measureText(mContext.getResources().getString(R.string.t0_string0))/2*5/4),
                size[1]/5-paint[1].getTextSize()*2,
                size[0]/2+(paint[1].measureText(mContext.getResources().getString(R.string.t0_string0))/2*5/4)
                , size[1]*4/5+paint[1].getTextSize()*5/4,
                paint[0]);

        canvas.drawText("명령어 화면의 기능",
                size[0]/2, size[1]/5,paint[1]);

        if(step[1]){
            canvas.drawText(current_content[0], size[0]/2, size[1]*2/5,paint[2]);
            canvas.drawText(current_content[1], size[0]/2, size[1]*3/5,paint[2]);
            canvas.drawText(current_content[2], size[0]/2, size[1]*4/5,paint[2]);
        }
    }


    private void drawAllIntro(Canvas canvas){
        paint[0] = new Paint();
        paint[0].setStyle(Paint.Style.STROKE);
        paint[0].setStrokeWidth(8.0f);
        paint[0].setColor(Color.GRAY);
        paint[0].setAlpha(drawable_alpha);

        paint[1] = new Paint();
        paint[1].setTextSize(70.0f);
        paint[1].setTextAlign(Paint.Align.CENTER);
        paint[1].setColor(Color.RED);
        paint[1].setAlpha(drawable_alpha);

        paint[2] = new Paint();
        paint[2].setTextSize(70.0f);
        paint[2].setTextAlign(Paint.Align.CENTER);
        paint[2].setColor(Color.BLACK);
        paint[2].setAlpha(drawable_alpha);

        //Answer
        String[] answer = {
                "명령어 화면의 기능",
                mContext.getResources().getString(R.string.t10_string0),
                mContext.getResources().getString(R.string.t10_string1),
                mContext.getResources().getString(R.string.t10_string2),
        };

        float[] answer_width = new float[answer.length];
        float max_width = 0;
        for(int i=0; i<answer.length;i++) {
            answer_width[i] = paint[1].measureText(answer[i]);
            if(answer_width[i] > max_width)
                max_width = answer_width[i];
        }

        float answer_width_max = size[1];

        String[] cut_answer = cutting_String(answer,answer_width, answer_width_max,paint[1]);

        float answer_height = (float)(paint[1].getTextSize() * cut_answer.length * 1.5);

        for(int i=0; i<cut_answer.length; i++){
            if (i == 0)
                canvas.drawText(cut_answer[i],size[0]/2,size[1]*(i+1)/5,paint[1]);
            else
                canvas.drawText(cut_answer[i],size[0]/2,size[1]*(i+1)/5,paint[2]);
        }

        canvas.drawRect(size[0]/2-max_width/2*5/4, size[1]/5-paint[1].getTextSize()*2,size[0]/2+max_width/2*5/4, size[1]*4/5+paint[1].getTextSize()*5/4,
                paint[0]);


        canvas.drawBitmap(next.getArrow(),next.getX(),next.getY(),paint[0]);

    }

    private void drawAppMenuTuto(Canvas canvas){
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
        paint[2].setAlpha(drawable_alpha);
        paint[2].setColor(Color.BLACK);
        paint[2].setTextSize(40.0f);
        paint[2].setTextAlign(Paint.Align.CENTER);

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
            String question1 = mContext.getResources().getString(R.string.t10_string3);
            float width = paint[3].measureText(question1);

            canvas.drawRect(size[0]*9/10-width-paint[3].getTextSize()*2,size[1]/40,size[0]*9/10,size[1]/5,paint[0]);
            canvas.drawText(question1,((size[0]*9/10-width-paint[3].getTextSize()*2)+(size[0]*9/10))/2,((size[1]/40)+(size[1]/5))/2 +paint[3].getTextSize()/2,paint[3] );

        }

        if(step[4]){
            paint[0].setAlpha(255);
            paint[3].setAlpha(255);
            Bitmap character = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.appmenu);

            String question1 = mContext.getResources().getString(R.string.t10_string3);
            float width = paint[3].measureText(question1);

            canvas.drawRect(size[0]*9/10-width-paint[3].getTextSize()*2,size[1]/40,size[0]*9/10,size[1]/5,paint[0]);
            canvas.drawText(question1,((size[0]*9/10-width-paint[3].getTextSize()*2)+(size[0]*9/10))/2,((size[1]/40)+(size[1]/5))/2 +paint[3].getTextSize()/2,paint[3] );

            //Answer
            String[] answer = {
                    mContext.getResources().getString(R.string.t10_string4),
                    mContext.getResources().getString(R.string.t10_string5),
                    mContext.getResources().getString(R.string.t01_string4),
                    mContext.getResources().getString(R.string.t10_string6),
            };

            float[] answer_width = new float[answer.length];
            float max_width = 0;
            for(int i=0; i<answer.length;i++) {
                answer_width[i] = paint[5].measureText(answer[i]);
                if(answer_width[i] > max_width)
                    max_width = answer_width[i];
            }

            float answer_width_max = (size[0]-character.getWidth()*3) - character.getWidth()*5/4;
            float answer_height_max = (size[1]-character.getHeight()) - (size[1]/5);

            String[] cut_answer = cutting_String(answer,answer_width, answer_width_max,paint[5]);

            float answer_height = (float)(paint[5].getTextSize() * cut_answer.length * 1.5);

            canvas.drawRect(character.getWidth()*5/4,size[1]/5+character.getHeight()/2
                    ,size[0]-character.getWidth()*3 + paint[5].getTextSize()*3/2,character.getHeight()*5/4 + answer_height + paint[5].getTextSize()*2,
                    paint[1]);

            for(int i=0; i<cut_answer.length; i++){
                canvas.drawText(cut_answer[i],character.getWidth()*5/4+paint[5].getTextSize()/2,paint[5].getTextSize()*5/4+character.getHeight()/2+size[1]/5 + paint[5].getTextSize()*3/2*i,paint[5]);
            }

            if(size[1] < character.getHeight()*5/4 + answer_height + paint[5].getTextSize()*2){
                float addedSize = character.getHeight()*5/4 + character.getHeight()*5/4 + answer_height + paint[4].getTextSize()*2 - size[1];
                this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(size[1]+addedSize+paint[5].getTextSize())));
            }

            canvas.drawBitmap(character,0,size[1]/5,paint[1]);
            canvas.drawText("DRS",character.getWidth()/2, size[1]/5+character.getHeight()+paint[1].getTextSize(),paint[2]);

        }

        if(step[5]){
            paint[0].setAlpha(255);
            paint[1].setAlpha(255);
            paint[2].setAlpha(255);
            paint[3].setAlpha(255);
            paint[4].setAlpha(255);
            paint[5].setAlpha(255);

            Bitmap character = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.appmenu);

            String question1 = mContext.getResources().getString(R.string.t10_string3);
            float width = paint[3].measureText(question1);
            canvas.drawRect(size[0]*9/10-width-paint[3].getTextSize()*2,size[1]/40,size[0]*9/10,size[1]/5,paint[0]);
            canvas.drawText(question1,((size[0]*9/10-width-paint[3].getTextSize()*2)+(size[0]*9/10))/2,((size[1]/40)+(size[1]/5))/2 +paint[3].getTextSize()/2,paint[3] );

            //Answer
            String[] answer = {
                    mContext.getResources().getString(R.string.t10_string4),
                    mContext.getResources().getString(R.string.t10_string5),
                    mContext.getResources().getString(R.string.t01_string4),
                    mContext.getResources().getString(R.string.t10_string6),
            };

            float[] answer_width = new float[answer.length];
            float max_width = 0;
            for(int i=0; i<answer.length;i++) {
                answer_width[i] = paint[5].measureText(answer[i]);
                if(answer_width[i] > max_width)
                    max_width = answer_width[i];
            }

            float answer_width_max = (size[0]-character.getWidth()*3) - character.getWidth()*5/4;
            float answer_height_max = (size[1]-character.getHeight()) - (size[1]/5);

            String[] cut_answer = cutting_String(answer,answer_width, answer_width_max,paint[5]);

            float answer_height = (float)(paint[5].getTextSize() * cut_answer.length * 1.5);

            canvas.drawRect(character.getWidth()*5/4,size[1]/5+character.getHeight()/2
                    ,size[0]-character.getWidth()*3 + paint[5].getTextSize()*3/2,character.getHeight()*5/4 + answer_height + paint[5].getTextSize()*2,
                    paint[1]);

            for(int i=0; i<cut_answer.length; i++){
                canvas.drawText(cut_answer[i],character.getWidth()*5/4+paint[5].getTextSize()/2,paint[5].getTextSize()*5/4+character.getHeight()/2+size[1]/5 + paint[5].getTextSize()*3/2*i,paint[5]);
            }

            if(size[1] < character.getHeight()*5/4 + answer_height + paint[5].getTextSize()*2){
                float addedSize = character.getHeight()*5/4 + character.getHeight()*5/4 + answer_height + paint[4].getTextSize()*2 - size[1];
                this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(size[1]+addedSize+paint[5].getTextSize())));
            }

            canvas.drawBitmap(character,0,size[1]/5,paint[1]);
            canvas.drawText("DRS",character.getWidth()/2, size[1]/5+character.getHeight()+paint[1].getTextSize(),paint[2]);

            canvas.drawBitmap(next.getArrow(),next.getX(),next.getY(),paint[1]);

        }
    }

    void drawAppMenuTuto1(Canvas canvas){
        paint[0] = new Paint();
        paint[0].setColor(Color.YELLOW);
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
        paint[4].setTextAlign(Paint.Align.LEFT);
        paint[4].setAlpha(drawable_alpha);

        paint[5] = new Paint();
        paint[5].setColor(Color.RED);
        paint[5].setTextSize(35.0f);
        paint[5].setTextAlign(Paint.Align.LEFT);
        paint[5].setAlpha(drawable_alpha);
        paint[5].setStrokeWidth(5);
        paint[5].setStyle(Paint.Style.STROKE);




        if(step[6]){
            paint[0].setAlpha(255);
            paint[3].setAlpha(255);
            Bitmap character = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.appmenu);

            //Answer
            String[] answer = {
                    "위의 명령어들은 3번에 연결된 모터와 10번에 연결된 모터를 지정한 시간만큼 회전을 시키는 명령어 입니다.",
                    "그럼 우선 먼저 모터의 속성값을 설정해보도록 하겠습니다.",
                    "명령어 화면에 있는 모터아이콘을 한 번 클릭하면 명령어 화면 아래에 모터 속성값을 설정할 수 있는 설정 화면이 나타납니다.",
            };

            float[] answer_width = new float[answer.length];
            float max_width = 0;
            for(int i=0; i<answer.length;i++) {
                answer_width[i] = paint[4].measureText(answer[i]);
                if(answer_width[i] > max_width)
                    max_width = answer_width[i];
            }

            float answer_width_max = (size[0]-character.getWidth()*3) - character.getWidth()*5/4;
            float answer_height_max = (size[1]-character.getHeight()) - (size[1]/5);

            String[] cut_answer = cutting_String(answer,answer_width, answer_width_max,paint[4]);

            float answer_height = (float)(paint[4].getTextSize() * cut_answer.length * 1.5);

            canvas.drawRect(character.getWidth()*5/4,size[1]/5+character.getHeight()/2
                    ,size[0]-character.getWidth()*3 + paint[4].getTextSize()*3/2,character.getHeight()*5/4 + answer_height + paint[4].getTextSize()*2,
                    paint[1]);

            for(int i=0; i<cut_answer.length; i++){
                canvas.drawText(cut_answer[i],character.getWidth()*5/4+paint[4].getTextSize()/2,paint[4].getTextSize()*5/4+character.getHeight()/2+size[1]/5 + paint[4].getTextSize()*3/2*i,paint[4]);
            }

            if(size[1] < character.getHeight()*5/4 + answer_height + paint[4].getTextSize()*2 + paint[4].getTextSize()){
                float addedSize = character.getHeight()*5/4 + character.getHeight()*5/4 + answer_height + paint[4].getTextSize()*2 - size[1];
                this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(size[1]+addedSize+paint[4].getTextSize())));
            }

            canvas.drawBitmap(character,0,size[1]/5,paint[1]);
            canvas.drawText("DRS",character.getWidth()/2, size[1]/5+character.getHeight()+paint[1].getTextSize(),paint[2]);

        }

        if(step[7]){
            paint[0].setAlpha(255);
            paint[1].setAlpha(255);
            paint[2].setAlpha(255);
            paint[3].setAlpha(255);
            paint[4].setAlpha(255);
            paint[5].setAlpha(255);

            Bitmap character = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.appmenu);


            //Answer
            //Answer
            String[] answer = {
                    "위의 명령어들은 3번에 연결된 모터와 10번에 연결된 모터를 지정한 시간만큼 회전을 시키는 명령어 입니다.",
                    "그럼 우선 먼저 모터의 속성값을 설정해보도록 하겠습니다.",
                    "명령어 화면에 있는 모터아이콘을 한 번 클릭하면 명령어 화면 아래에 모터 속성값을 설정할 수 있는 설정 화면이 나타납니다.",
                    "\n",
                    "Motor3 아이콘을 누르면 아래와 같이 모터의 속성값을 설정할 수 있는 화면이 나타납니다."
            };

            float[] answer_width = new float[answer.length];
            float max_width = 0;
            for(int i=0; i<answer.length;i++) {
                answer_width[i] = paint[4].measureText(answer[i]);
                if(answer_width[i] > max_width)
                    max_width = answer_width[i];
            }

            float answer_width_max = (size[0]-character.getWidth()*3) - character.getWidth()*5/4;
            float answer_height_max = (size[1]-character.getHeight()) - (size[1]/5);

            String[] cut_answer = cutting_String(answer,answer_width, answer_width_max,paint[4]);

            float answer_height = (float)(paint[4].getTextSize() * cut_answer.length * 1.5);

            canvas.drawRect(character.getWidth()*5/4,size[1]/5+character.getHeight()/2
                    ,size[0]-character.getWidth()*3 + paint[4].getTextSize()*3/2,character.getHeight()*5/4 + answer_height + paint[4].getTextSize()*2,
                    paint[1]);

            for(int i=0; i<cut_answer.length; i++){
                canvas.drawText(cut_answer[i],character.getWidth()*5/4+paint[4].getTextSize()/2,paint[4].getTextSize()*5/4+character.getHeight()/2+size[1]/5 + paint[4].getTextSize()*3/2*i,paint[4]);
            }

            if(size[1] < character.getHeight()*5/4 + answer_height + paint[4].getTextSize()*2 + paint[4].getTextSize()){
                float addedSize = character.getHeight()*5/4 + character.getHeight()*5/4 + answer_height + paint[4].getTextSize()*2 - size[1];
                this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(size[1]+addedSize+paint[4].getTextSize())));
            }

            canvas.drawBitmap(character,0,size[1]/5,paint[1]);
            canvas.drawText("DRS",character.getWidth()/2, size[1]/5+character.getHeight()+paint[1].getTextSize(),paint[2]);


            canvas.drawBitmap(next.getArrow(),next.getX(),next.getY(),paint[1]);
        }
    }

    void drawAppMenuTuto2(Canvas canvas){
        paint[0] = new Paint();
        paint[0].setColor(Color.YELLOW);
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
        paint[4].setTextAlign(Paint.Align.LEFT);
        paint[4].setAlpha(drawable_alpha);

        paint[5] = new Paint();
        paint[5].setColor(Color.RED);
        paint[5].setTextSize(35.0f);
        paint[5].setTextAlign(Paint.Align.LEFT);
        paint[5].setAlpha(drawable_alpha);
        paint[5].setStrokeWidth(5);
        paint[5].setStyle(Paint.Style.STROKE);


        if(step[8]){
            paint[0].setAlpha(255);
            paint[3].setAlpha(255);
            Bitmap character = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.appmenu);


            //Answer
            String[] answer = {
                    "아래에 표시된 부분은 모터의 속성값을 설정할 수 있는 설정창 입니다.",
                    "1. 모터 옆에 있는 스크롤을 이용하여 모터의 속도를 설정할 수 있습니다.",
                    "2. 왼쪽에 있는 키보드를 이용하여 모터의 속도를 설정할 수도 있습니다. 이 때는 입력 후 반드시 엔터를 눌러주어야 합니다.",


            };

            float[] answer_width = new float[answer.length];
            float max_width = 0;
            for(int i=0; i<answer.length;i++) {
                answer_width[i] = paint[4].measureText(answer[i]);
                if(answer_width[i] > max_width)
                    max_width = answer_width[i];
            }

            float answer_width_max = (size[0]-character.getWidth()*3) - character.getWidth()*5/4;
            float answer_height_max = (size[1]-character.getHeight()) - (size[1]/5);

            String[] cut_answer = cutting_String(answer,answer_width, answer_width_max,paint[4]);

            float answer_height = (float)(paint[4].getTextSize() * cut_answer.length * 1.5);

            canvas.drawRect(character.getWidth()*5/4,size[1]/5+character.getHeight()/2
                    ,size[0]-character.getWidth()*3 + paint[4].getTextSize()*3/2,character.getHeight()*5/4 + answer_height + paint[4].getTextSize()*2,
                    paint[1]);

            for(int i=0; i<cut_answer.length; i++){
                canvas.drawText(cut_answer[i],character.getWidth()*5/4+paint[4].getTextSize()/2,paint[4].getTextSize()*5/4+character.getHeight()/2+size[1]/5 + paint[4].getTextSize()*3/2*i,paint[4]);
            }

            if(size[1] < character.getHeight()*5/4 + answer_height + paint[4].getTextSize()*2 + paint[4].getTextSize()){
                float addedSize = character.getHeight()*5/4 + character.getHeight()*5/4 + answer_height + paint[4].getTextSize()*2 - size[1];
                this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(size[1]+addedSize+paint[4].getTextSize())));
            }

            canvas.drawBitmap(character,0,size[1]/5,paint[1]);
            canvas.drawText("DRS",character.getWidth()/2, size[1]/5+character.getHeight()+paint[1].getTextSize(),paint[2]);

        }

        if(step[9]){
            paint[0].setAlpha(255);
            paint[1].setAlpha(255);
            paint[2].setAlpha(255);
            paint[3].setAlpha(255);
            paint[4].setAlpha(255);
            paint[5].setAlpha(255);

            Bitmap character = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.appmenu);


            //Answer
            //Answer
            String[] answer = {
                    "아래에 표시된 부분은 모터의 속성값을 설정할 수 있는 창 입니다.",
                    "1. 모터 옆에 있는 스크롤을 이용하여 모터의 속도를 설정할 수 있습니다.",
                    "2. 왼쪽에 있는 키보드를 이용하여 모터의 속도를 설정할 수도 있습니다. 이 때는 입력 후 반드시 엔터를 눌러주어야 합니다.",
                    "\n",
                    "그럼 키보드를 이용하여 3번 모터의 속도를 50으로 설정해 보세요."
            };

            float[] answer_width = new float[answer.length];
            float max_width = 0;
            for(int i=0; i<answer.length;i++) {
                answer_width[i] = paint[4].measureText(answer[i]);
                if(answer_width[i] > max_width)
                    max_width = answer_width[i];
            }

            float answer_width_max = (size[0]-character.getWidth()*3) - character.getWidth()*5/4;
            float answer_height_max = (size[1]-character.getHeight()) - (size[1]/5);

            String[] cut_answer = cutting_String(answer,answer_width, answer_width_max,paint[4]);

            float answer_height = (float)(paint[4].getTextSize() * cut_answer.length * 1.5);

            canvas.drawRect(character.getWidth()*5/4,size[1]/5+character.getHeight()/2
                    ,size[0]-character.getWidth()*3 + paint[4].getTextSize()*3/2,character.getHeight()*5/4 + answer_height + paint[4].getTextSize()*2,
                    paint[1]);

            for(int i=0; i<cut_answer.length; i++){
                canvas.drawText(cut_answer[i],character.getWidth()*5/4+paint[4].getTextSize()/2,paint[4].getTextSize()*5/4+character.getHeight()/2+size[1]/5 + paint[4].getTextSize()*3/2*i,paint[4]);
            }

            if(size[1] < character.getHeight()*5/4 + answer_height + paint[4].getTextSize()*2 + paint[4].getTextSize()){
                float addedSize = character.getHeight()*5/4 + character.getHeight()*5/4 + answer_height + paint[4].getTextSize()*2 - size[1];
                this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(size[1]+addedSize+paint[4].getTextSize())));
            }

            canvas.drawBitmap(character,0,size[1]/5,paint[1]);
            canvas.drawText("DRS",character.getWidth()/2, size[1]/5+character.getHeight()+paint[1].getTextSize(),paint[2]);


            canvas.drawBitmap(next.getArrow(),next.getX(),next.getY(),paint[1]);
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
        paint[2].setTextAlign(Paint.Align.LEFT);
        paint[2].setAlpha(drawable_alpha);

        paint[4] = new Paint();
        paint[4].setColor(Color.BLACK);
        paint[4].setTextSize(40.0f);
        paint[4].setTextAlign(Paint.Align.LEFT);
        paint[4].setAlpha(drawable_alpha);

        Bitmap character = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.appmenu);

        //Answer
        String[] answer = {
                mContext.getResources().getString(R.string.t0_string17),
                mContext.getResources().getString(R.string.t0_string18)
        };

        float[] answer_width = new float[answer.length];
        float max_width = 0;
        for(int i=0; i<answer.length;i++) {
            answer_width[i] = paint[4].measureText(answer[i]);
            if(answer_width[i] > max_width)
                max_width = answer_width[i];
        }

        float answer_width_max = (size[0]-character.getWidth()*3) - character.getWidth()*5/4;
        float answer_height_max = (size[1]-character.getHeight()) - (size[1]/5);

        String[] cut_answer = cutting_String(answer,answer_width, answer_width_max,paint[4]);

        float answer_height = (float)(paint[4].getTextSize() * cut_answer.length * 1.5);

        canvas.drawRect(character.getWidth()*5/4,size[1]/5+character.getHeight()/2
                ,size[0]-character.getWidth()*3 + paint[4].getTextSize()*3/2,character.getHeight()*5/4 + answer_height + paint[4].getTextSize()*2,
                paint[1]);

        for(int i=0; i<cut_answer.length; i++){
            canvas.drawText(cut_answer[i],character.getWidth()*5/4+paint[4].getTextSize()/2,paint[4].getTextSize()*5/4+character.getHeight()/2+size[1]/5 + paint[4].getTextSize()*3/2*i,paint[4]);
        }

        if(size[1] < character.getHeight()*5/4 + answer_height + paint[4].getTextSize()*2 + paint[4].getTextSize()){
            float addedSize = character.getHeight()*5/4 + character.getHeight()*5/4 + answer_height + paint[4].getTextSize()*2 - size[1];
            this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(size[1]+addedSize+paint[4].getTextSize())));
        }

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
            if(step[2] || step[5] || step[7]){
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
            else if(step[3] || step[4]){
                step[3] = false;
                step[4]= false;
                step[5] = true;

            }
            else if(step[5]){
                Bitmap temp = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.move_right);
                if(event.getX()>next.getX() && event.getX() < next.getX() +temp.getWidth()){
                    if(event.getY() > next.getY() && event.getY() < next.getY()+temp.getHeight()){
                        mSoundManager.play(0);
                        try{
                            Thread.sleep(20);
                        }catch (InterruptedException e){};
                        next.unclicked();
                        step[5] = false;
                        step[6] = true;
                        drawable_alpha = 0;
                        Intent intent = new Intent();
                        intent.setAction(TransparentActivity.REQUEST_ADD_COMMAND_IN_T10);
                        mContext.sendBroadcast(intent);
                        invalidate();
                    }
                }
            }
            else if(step[6]){
                step[6] = false;
                step[7] = true;
            }
            else if(step[7]){
                Bitmap temp = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.move_right);
                if(event.getX()>next.getX() && event.getX() < next.getX() +temp.getWidth()){
                    if(event.getY() > next.getY() && event.getY() < next.getY()+temp.getHeight()){
                        mSoundManager.play(0);
                        try{
                            Thread.sleep(20);
                        }catch (InterruptedException e){};
                        next.unclicked();
                        step[7] = false;
                        step[8] = true;
                        drawable_alpha = 0;
                    }
                }
            }
            else if(step[8]){
                Log.d(TAG,"onTouch in Step8");
                step[8] = false;
//                step[9] = true;
                Intent intent = new Intent();
                intent.putExtra("index",1);
                intent.setAction(TransparentActivity.REQUEST_SETTING_LAYOUT);
                mActivity.sendBroadcast(intent);
                mActivity.finish();
//                enable_Command_onTouch = true;
            }

            else if(step[9]){
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

    private String[] cutting_String(String[] answer,float[] answer_width, float answer_width_max, Paint paint) {
        int string_row_count = 0;
        int[] numberOfRow = new int[answer.length];

        for(int i=0; i<answer.length; i++){
            string_row_count += (int)((answer_width[i]/answer_width_max) +1);
            numberOfRow[i] = (int)(answer_width[i]/answer_width_max)+1;
        }

        String[] cut_string = new String[string_row_count];

        int index = 0;
        for(int i=0; i<answer.length; i++ ){
            if(numberOfRow[i] == 1){
                cut_string[index++] = answer[i];
            }
            else{
                int current_start = 0;
                for(int a=0; a<numberOfRow[i]; a++) {

                    for (int j = current_start; j < answer[i].length(); j++) {
                        if (paint.measureText(answer[i], current_start, j) >= answer_width_max) {
                            cut_string[index++] = answer[i].substring(current_start,j);
                            current_start = j;
                            break;
                        }
                        if(j == answer[i].length()-1){
                            cut_string[index++] = answer[i].substring(current_start,j);
                        }
                    }
                }
            }
        }



        return cut_string;
    }



}
