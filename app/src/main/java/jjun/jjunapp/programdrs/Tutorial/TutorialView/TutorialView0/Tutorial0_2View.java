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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import jjun.jjunapp.programdrs.R;
import jjun.jjunapp.programdrs.Sound.SoundManager;
import jjun.jjunapp.programdrs.Tutorial.TutorialCommandActivity;

import static android.content.ContentValues.TAG;

/**
 * Created by jjunj on 2017-10-11.
 */

public class Tutorial0_2View extends View {

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


    public Tutorial0_2View(final Context context, final Activity mActivity, Handler mHandler) {
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
                }catch (InterruptedException e){};

                while(step[3]){
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
            else if(step[3]){
                drawRequestPractice(canvas);
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
        paint[4].setTextAlign(Paint.Align.LEFT);
        paint[4].setAlpha(drawable_alpha);

        paint[5] = new Paint();
        paint[5].setColor(Color.BLACK);
        paint[5].setTextSize(35.0f);
        paint[5].setTextAlign(Paint.Align.LEFT);
        paint[5].setAlpha(drawable_alpha);

        if(step[0]){
            String question1 = mContext.getResources().getString(R.string.t02_string0);
            float width = paint[3].measureText(question1);

            canvas.drawRect(size[0]*9/10-width-paint[3].getTextSize()*2,size[1]/40,size[0]*9/10,size[1]/5,paint[0]);
            canvas.drawText(question1,((size[0]*9/10-width-paint[3].getTextSize()*2)+(size[0]*9/10))/2,((size[1]/40)+(size[1]/5))/2 +paint[3].getTextSize()/2,paint[3] );

        }

        if(step[1]){
            paint[0].setAlpha(255);
            paint[3].setAlpha(255);
            Bitmap character = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.appmenu);

            String question1 = mContext.getResources().getString(R.string.t02_string0);
            float width = paint[3].measureText(question1);
            canvas.drawRect(size[0]*9/10-width-paint[3].getTextSize()*2,size[1]/40,size[0]*9/10,size[1]/5,paint[0]);
            canvas.drawText(question1,((size[0]*9/10-width-paint[3].getTextSize()*2)+(size[0]*9/10))/2,((size[1]/40)+(size[1]/5))/2 +paint[3].getTextSize()/2,paint[3] );

            //Answer
            String[] answer = {
                    mContext.getResources().getString(R.string.t02_string1),
                    mContext.getResources().getString(R.string.t02_string2),
                    mContext.getResources().getString(R.string.t02_string3),
                    mContext.getResources().getString(R.string.t02_string4),
                    mContext.getResources().getString(R.string.t02_string6),
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

            command_window = (LinearLayout)mActivity.findViewById(R.id.command_window);
            Paint paint_rect = new Paint();
            paint_rect.setColor(Color.RED);
            paint_rect.setStyle(Paint.Style.STROKE);
            paint_rect.setStrokeWidth(15.0f);
            canvas.drawRect(0,0,this.getWidth(),this.getHeight(),paint_rect);

            canvas.drawBitmap(character,0,size[1]/5,paint[1]);
            canvas.drawText("DRS",character.getWidth()/2, size[1]/5+character.getHeight()+paint[1].getTextSize(),paint[2]);

            canvas.drawBitmap(next.getArrow(),next.getX(),next.getY(),paint[1]);


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

            // Question
            String question1 = mContext.getResources().getString(R.string.t02_string0);
            float width = paint[3].measureText(question1);
            canvas.drawRect(size[0]*9/10-width-paint[3].getTextSize()*2,size[1]/40,size[0]*9/10,size[1]/5,paint[0]);
            canvas.drawText(question1,((size[0]*9/10-width-paint[3].getTextSize()*2)+(size[0]*9/10))/2,((size[1]/40)+(size[1]/5))/2 +paint[3].getTextSize()/2,paint[3] );

            //Answer
            String[] answer = {
                    mContext.getResources().getString(R.string.t02_string1),
                    mContext.getResources().getString(R.string.t02_string2),
                    mContext.getResources().getString(R.string.t02_string3),
                    mContext.getResources().getString(R.string.t02_string4),
                    mContext.getResources().getString(R.string.t02_string6),
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


            command_window = (LinearLayout)mActivity.findViewById(R.id.command_window);
            Paint paint_rect = new Paint();
            paint_rect.setColor(Color.RED);
            paint_rect.setStyle(Paint.Style.STROKE);
            paint_rect.setStrokeWidth(15.0f);
            canvas.drawRect(0,0,this.getWidth(),this.getHeight(),paint_rect);

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
        paint[2].setTextAlign(Paint.Align.CENTER);
        paint[2].setAlpha(drawable_alpha);

        paint[3] = new Paint();
        paint[3].setColor(Color.RED);
        paint[3].setStrokeWidth(10);

        paint[4] = new Paint();
        paint[4].setColor(Color.BLACK);
        paint[4].setTextSize(40.0f);
        paint[4].setTextAlign(Paint.Align.LEFT);
        paint[4].setAlpha(drawable_alpha);

        Bitmap character = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.appmenu);

        String[] answer = {
                mContext.getResources().getString(R.string.t02_string7),
                mContext.getResources().getString(R.string.t02_string8),

        };

        Bitmap[] pr_icon = {
                BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.start),
                BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.motor3),
                BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.motor10),
                BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.timer_sec),
                BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.end)
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

        float current_length = character.getWidth()*5/4 + pr_icon[0].getWidth()/2;
        float current_height = paint[4].getTextSize()*5/4+character.getHeight()/2+size[1]/5 + paint[4].getTextSize()*3/2*(cut_answer.length-1);
        int icon_row = 1;
        for(int i=0; i<pr_icon.length; i++){
            if(current_length < answer_width_max){

                current_length += pr_icon[i].getWidth()*3/2;
            }
            else{
                icon_row ++;
                current_length = character.getWidth()*5/4 + pr_icon[i].getWidth()/2;
                current_length += pr_icon[i].getWidth()*3/2;
            }
        }


        float answer_height = (float)(paint[4].getTextSize() * cut_answer.length * 1.5);

        canvas.drawRect(character.getWidth()*5/4,size[1]/5+character.getHeight()/2
                ,size[0]-character.getWidth()*3 + paint[4].getTextSize()*3/2,
                character.getHeight()*5/4 + answer_height + paint[4].getTextSize()*2  + pr_icon[0].getHeight() * 3/2 * icon_row,
                paint[1]);

        for(int i=0; i<cut_answer.length; i++){
            canvas.drawText(cut_answer[i],character.getWidth()*5/4+paint[4].getTextSize()/2,
                    paint[4].getTextSize()*5/4+character.getHeight()/2+size[1]/5 + paint[4].getTextSize()*3/2*i,
                    paint[4]);
        }

        current_length = character.getWidth()*5/4 + pr_icon[0].getWidth()/2;
        current_height = paint[4].getTextSize()*5/4+character.getHeight()/2+size[1]/5 + paint[4].getTextSize()*3/2*(cut_answer.length-1) + pr_icon[0].getHeight()/2;
        for(int i=0; i<pr_icon.length; i++){
            if(current_length < answer_width_max ){
                if(current_length + pr_icon[i].getWidth()*3/2 < answer_width_max && i != (pr_icon.length-1) ){
                    canvas.drawLine(current_length+pr_icon[i].getWidth()/2, current_height+pr_icon[i].getHeight()/2,
                            current_length+pr_icon[i].getWidth()*2, current_height+pr_icon[i].getHeight()/2,paint[3]);
                }
                else if(current_length + pr_icon[i].getWidth()*3/2 > answer_width_max && i != (pr_icon.length-1)){
                    canvas.drawLine(current_length+pr_icon[i].getWidth()/2, current_height+pr_icon[i].getHeight()*4/5,
                            character.getWidth()*5/4 + pr_icon[i].getWidth(), current_height+pr_icon[i].getHeight()*7/4,paint[3] );
                }
                canvas.drawBitmap(pr_icon[i],current_length,current_height,null);
                current_length += pr_icon[i].getWidth()*3/2;
            }
            else{
                current_height += pr_icon[i].getHeight()*3/2;
                current_length = character.getWidth()*5/4 + pr_icon[i].getWidth()/2;
                if(current_length + pr_icon[i].getWidth()*3/2 < answer_width_max && i != (pr_icon.length-1) ){
                    canvas.drawLine(current_length+pr_icon[i].getWidth()/2, current_height+pr_icon[i].getHeight()/2,
                            current_length+pr_icon[i].getWidth()*2, current_height+pr_icon[i].getHeight()/2,paint[3]);
                }
                canvas.drawBitmap(pr_icon[i],current_length,current_height,null);
                current_length += pr_icon[i].getWidth()*3/2;
            }
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
                mActivity.setResult(TutorialCommandActivity.STAGE_OVER);
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
        Log.w(TAG,String.valueOf(answer_width_max));

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
                    Log.w(TAG, "a : " + a +  " , current_start : " + current_start );

                    for (int j = current_start; j < answer[i].length(); j++) {
                        if (paint.measureText(answer[i], current_start, j) >= answer_width_max) {
                            cut_string[index] = answer[i].substring(current_start,j);
                            Log.w(TAG,cut_string[index++]);
                            current_start = j;
                            break;
                        }
                        if(j == answer[i].length()-1){
                            cut_string[index] = answer[i].substring(current_start,j);
                            Log.w(TAG,cut_string[index++]);
                        }
                    }
                }
            }
        }

        for(int i=0; i<string_row_count ; i++){
            Log.d(TAG,"String " + i + " : " + cut_string[i]);
        }


        return cut_string;
    }


}
