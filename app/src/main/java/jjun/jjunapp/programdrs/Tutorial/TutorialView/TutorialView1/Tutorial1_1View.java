package jjun.jjunapp.programdrs.Tutorial.TutorialView.TutorialView1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
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

public class Tutorial1_1View extends View {

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

    private LinearLayout command_window,left_menu_layout, bottom_setting;
    private ImageView app_menu_btn;
    private SoundManager mSoundManager;
    private NextBtn next;


    public Tutorial1_1View(final Context context, final Activity mActivity, Handler mHandler) {
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

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bottom_setting = (LinearLayout)mActivity.findViewById(R.id.bottom_setting);
                        Bitmap bottom_setting_back = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.setting_box_back);
                        Bitmap bottom_selected = Bitmap.createBitmap(bottom_setting_back.getWidth(), bottom_setting_back.getHeight(),Bitmap.Config.ARGB_8888);
                        Paint paint = new Paint();
                        paint.setStrokeWidth(10);
                        paint.setColor(Color.RED);
                        paint.setStyle(Paint.Style.STROKE);
                        Canvas imageCanvas = new Canvas(bottom_selected);
                        imageCanvas.drawRect(0,0,imageCanvas.getWidth(),imageCanvas.getHeight(),paint);
                        bottom_setting.setBackground(new BitmapDrawable(mContext.getResources(),bottom_selected));

                        left_menu_layout = (LinearLayout)mActivity.findViewById(R.id.left_layout);
                        Bitmap left_back = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.icon_box_back);
                        Bitmap left_back_trans = Bitmap.createBitmap(left_back.getWidth(),left_back.getHeight(),Bitmap.Config.ARGB_8888);
                        Canvas imageCanvas2 = new Canvas(left_back_trans);
                        paint.setColor(Color.BLUE);
                        paint.setStrokeWidth(20);
                        paint.setTextSize(80);
                        paint.setTextAlign(Paint.Align.CENTER);

                        Paint textPaint = new Paint();
                        textPaint.setTextAlign(Paint.Align.CENTER);
                        textPaint.setTextSize(100);
                        textPaint.setColor(Color.BLUE);

                        imageCanvas2.drawRect(0,0,imageCanvas2.getWidth(),imageCanvas2.getHeight(),paint);
                        imageCanvas2.drawText("Keyboard",left_back_trans.getWidth()/2, textPaint.getTextSize()*3/2,textPaint);
                        left_menu_layout.setBackground(new BitmapDrawable(mContext.getResources(),left_back_trans));
                    }
                });


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
                    T0_Handler.obtainMessage(UPDATE_UI).sendToTarget();
                    try{
                        Thread.sleep(100);
                    }catch (InterruptedException e){};

                }

                while(step[2]){
                    if(drawable_alpha == 0) {
                        drawable_alpha = 255;
                        T0_Handler.obtainMessage(UPDATE_UI).sendToTarget();
                    }
                    else{
                        waitCount++;
                        if(waitCount == 100){
                            step[3] = true;
                            step[2] = false;
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

                while(step[3]){
                    T0_Handler.obtainMessage(UPDATE_UI).sendToTarget();
                    try{
                        Thread.sleep(100);
                    }catch (InterruptedException e){};

                }

                waitCount = 0;
                while(step[4]){
                    if(drawable_alpha == 0){
                        drawable_alpha = 255;
                        T0_Handler.obtainMessage(UPDATE_UI).sendToTarget();
                    }
                    else{
                        waitCount++;
                        if(waitCount == 200){
                            setStopThread();
                            Intent intent = new Intent();
                            intent.setAction(TransparentActivity.REQUEST_INITIAL_COMMAND);
                            Log.d(TAG,"start thread");
                            mActivity.sendBroadcast(intent);
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
            if(step[0] || step[1]){
                drawIconTuto(canvas);
            }
            else if(step[2] || step[3]){
                drawIconTuto2(canvas);
            }

            else if(step[4]){
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
        paint[5].setColor(Color.BLACK);
        paint[5].setTextSize(35.0f);
        paint[5].setTextAlign(Paint.Align.LEFT);
        paint[5].setAlpha(drawable_alpha);

        if(step[0]){
            Bitmap character = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.appmenu);

            //Answer
            String[] answer = {
                    "아래에 표시된 부분이 바로 특정 아이콘에 대한 설정을 할 수 있는 설정창입니다.",
                    " 1) 가운데를 기준으로 왼쪽은 모터의 속성을 변경할 수 있는 창입니다. ",
                    " - 모터 명령어의 경우 모터의 속도를 스크롤 또는 왼쪽 메뉴에 있는 키보드를 이용하여 설정 할 수 있습니다.",
                    " - 시간 명령어의 경우 지연시간을 왼쪽 키보드를 통해 설정 할 수 있습니다.",
                    " - 점프 명령어의 경우 왼쪽 키보드를 통해 반복 횟수를 설정할 수 있습니다.",
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

        if(step[1]){
            paint[0].setAlpha(255);
            paint[1].setAlpha(255);
            paint[2].setAlpha(255);
            paint[3].setAlpha(255);
            paint[4].setAlpha(255);
            paint[5].setAlpha(255);

            Bitmap character = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.appmenu);

            //Answer
            String[] answer = {
                    "아래에 표시된 부분이 바로 특정 아이콘에 대한 설정을 할 수 있는 설정창입니다.",
                    " 1) 가운데를 기준으로 왼쪽은 모터의 속성을 변경할 수 있는 창입니다. ",
                    " - 모터 명령어의 경우 모터의 속도를 스크롤 또는 왼쪽 메뉴에 있는 키보드를 이용하여 설정 할 수 있습니다.",
                    " - 시간 명령어의 경우 지연시간을 왼쪽 키보드를 통해 설정 할 수 있습니다.",
                    " - 점프 명령어의 경우 왼쪽 키보드를 통해 반복 횟수를 설정할 수 있습니다.",
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

    void drawIconTuto2(Canvas canvas){
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
        paint[5].setColor(Color.BLACK);
        paint[5].setTextSize(35.0f);
        paint[5].setTextAlign(Paint.Align.LEFT);
        paint[5].setAlpha(drawable_alpha);

        if(step[2]){
            Bitmap character = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.appmenu);

            //Answer
            String[] answer = {
                    "아래에 표시된 부분이 바로 특정 아이콘에 대한 설정을 할 수 있는 설정창입니다.",
                    " 2) 가운데를 기준으로 왼쪽은 아이콘을 편집하는 설정창입니다.  ",
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

        if(step[3]){
            paint[0].setAlpha(255);
            paint[1].setAlpha(255);
            paint[2].setAlpha(255);
            paint[3].setAlpha(255);
            paint[4].setAlpha(255);
            paint[5].setAlpha(255);

            Bitmap character = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.appmenu);

            //Answer
            String[] answer = {
                    "아래에 표시된 부분이 바로 특정 아이콘에 대한 설정을 할 수 있는 설정창입니다.",
                    " 2) 가운데를 기준으로 왼쪽은 아이콘을 편집하는 설정창입니다.  ",
            };
            Bitmap[] icon_setting = {
                    BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.cancel),
                    BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.move_left),
                    BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.move_right),
                    BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.remove),
            } ;

            String[] icon_content ={
                    "-> 명령어 설정 창을 종료하는 버튼입니다.",
                    "-> 선택한 명령어를 왼쪽으로 한 칸 이동하는 버튼입니다.",
                    "-> 선택한 명령어를 오른쪽으로 한 칸 이동하는 버튼입니다.",
                    "-> 선택된 명령어 아이콘을 삭제하는 버튼입니다."
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

            float answer_height = (float)(paint[4].getTextSize() * cut_answer.length * 1.5 + icon_setting[0].getHeight() *3/2*(icon_setting.length));

            canvas.drawRect(character.getWidth()*5/4,size[1]/5+character.getHeight()/2
                    ,size[0]-character.getWidth()*3 + paint[4].getTextSize()*3/2,character.getHeight()*5/4 + answer_height + paint[4].getTextSize()*2,
                    paint[1]);

            for(int i=0; i<cut_answer.length; i++){
                canvas.drawText(cut_answer[i],character.getWidth()*5/4+paint[4].getTextSize()/2,
                        paint[4].getTextSize()*5/4+character.getHeight()/2+size[1]/5 + paint[4].getTextSize()*3/2*i,paint[4]);
            }

            if(size[1] < character.getHeight()*5/4 + answer_height + paint[4].getTextSize()*2 + paint[4].getTextSize()){
                float addedSize = character.getHeight()*5/4 + character.getHeight()*5/4 + answer_height + paint[4].getTextSize()*2 - size[1];
                this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(size[1]+addedSize+paint[4].getTextSize())));
            }

            canvas.drawBitmap(character,0,size[1]/5,paint[1]);
            canvas.drawText("DRS",character.getWidth()/2, size[1]/5+character.getHeight()+paint[1].getTextSize(),paint[2]);

            float last_Y = paint[4].getTextSize()*5/4+character.getHeight()/2+size[1]/5 + paint[4].getTextSize()*7/2;

            last_Y += icon_setting[0].getHeight()/2;
            float icon_X = character.getWidth()*5/4 + paint[4].getTextSize()/2;

            float[] content_width = new float[icon_content.length];
            float content_max_width = 0;
            for(int i=0; i<icon_content.length;i++) {
                content_width[i] = paint[4].measureText(icon_content[i]);
                if(content_width[i] > content_max_width)
                    content_max_width = content_width[i];
            }
            float content_width_max = answer_width_max - icon_setting[0].getWidth()*2;

            String[] cut_content = cutting_String(icon_content,content_width,content_width_max,paint[4]);

            int[] string_index = new int[icon_content.length];
            int index =0 ;
            for(int i=0; i<cut_content.length ; i++){
                if(cut_content[i].charAt(0) == '-'){
                    string_index[index++] = i;
                }
            }

            index = 0;
            for(int i=0 ; i<icon_setting.length ; i++){
                canvas.drawBitmap(icon_setting[i],icon_X, last_Y ,null);
                if(index != 3) {
                    int content_count =0;
                    for (int j = string_index[index]; j < string_index[index + 1]; j++) {
                        canvas.drawText(cut_content[j],icon_X+icon_setting[i].getWidth()*5/4,
                                last_Y+paint[4].getTextSize()*content_count++*3/2 + paint[4].getTextSize(),paint[4]);
                    }
                    index++;
                }
                else if(index==3){
                    int content_count = 0;
                    for(int j= string_index[index]; j<cut_content.length ; j++){
                        canvas.drawText(cut_content[j],icon_X+icon_setting[i].getWidth()*5/4,
                                last_Y+paint[4].getTextSize()*content_count++*3/2 + paint[4].getTextSize(),paint[4]);
                    }
                }
                last_Y += icon_setting[i].getHeight()*3/2;
            }

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
        paint[2].setTextAlign(Paint.Align.CENTER);
        paint[2].setAlpha(drawable_alpha);

        paint[4] = new Paint();
        paint[4].setColor(Color.BLACK);
        paint[4].setTextSize(40.0f);
        paint[4].setTextAlign(Paint.Align.CENTER);
        paint[4].setAlpha(drawable_alpha);

        Bitmap character = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.appmenu);

        String answer1 = "그럼 지금부터 직접 아이콘을 편집해보겠습니다. ";
        String answer2 = "화면에 나타난 예시와 같이 아이콘을 편집해보세요.";
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
            if(step[1]){
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
            if(step[0]) {
                drawable_alpha = 255;
                step[0] = false;
                step[1] = true;
            }

            else if(step[1]){
                Bitmap temp = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.move_right);
                if(event.getX()>next.getX() && event.getX() < next.getX() +temp.getWidth()){
                    if(event.getY() > next.getY() && event.getY() < next.getY()+temp.getHeight()){
                        mSoundManager.play(0);
                        try{
                            Thread.sleep(20);
                        }catch (InterruptedException e){};
                        next.unclicked();
                        step[1] = false;
                        step[2] = true;
                        drawable_alpha = 0;
                        invalidate();

                    }
                }
            }
            else if(step[2]){
                step[2] = false;
                step[3]= true;
            }
            else if(step[3]){
                Bitmap temp = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.move_right);
                if(event.getX()>next.getX() && event.getX() < next.getX() +temp.getWidth()){
                    if(event.getY() > next.getY() && event.getY() < next.getY()+temp.getHeight()){
                        mSoundManager.play(0);
                        try{
                            Thread.sleep(20);
                        }catch (InterruptedException e){};
                        next.unclicked();
                        step[3] = false;
                        step[4] = true;
                        drawable_alpha = 0;
                        invalidate();
                    }
                }
            }
            else if(step[4] ){
                step[4] = false;
                Intent intent = new Intent();
                intent.setAction(TransparentActivity.REQUEST_INITIAL_COMMAND);
                Log.d(TAG,"start thread");
                mActivity.sendBroadcast(intent);
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
