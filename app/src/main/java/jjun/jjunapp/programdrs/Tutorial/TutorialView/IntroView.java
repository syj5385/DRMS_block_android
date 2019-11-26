package jjun.jjunapp.programdrs.Tutorial.TutorialView;

import android.content.AbstractThreadedSyncAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import jjun.jjunapp.programdrs.R;
import jjun.jjunapp.programdrs.Sound.SoundManager;

/**
 * Created by jjunj on 2017-09-26.
 */

public class IntroView extends View{

    private static final int INVALIDATE = 0;

    private Context context;
    private Bitmap intro_image;
    private Bitmap drone;
    private String text1 ="DR";
    private String text2 = "MAKER";
    private String text3 = "SYSTEM";
    private String content = "Application Tutorial";
    private String current_content ="";

    private Paint bitmap_paint;
    private Paint topic_paint;
    private Paint content_paint;

    private int[] size = {0,0};

    private DrawThread mThread;
    private SoundManager mSoundManager;

    private Handler IntroViewHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case INVALIDATE :

                    invalidate();
                    break;
            }
        }
    };

    private boolean running1 = false;
    private boolean running2 = false;
    private boolean drone_running = true;
    private float drone1_position[] = new float[2];
    private float interval = (float)0.01;
    private int radius = 0;
    private double radian = 0;

    public IntroView(Context context) {
        super(context);
        this.context = context;

        bitmap_paint = new Paint(); // to draw Bitmap
        bitmap_paint.setAlpha(0);

        drone = BitmapFactory.decodeResource(context.getResources(),R.drawable.drone);
        radius = (int)(drone.getWidth() * 1.414);


        intro_image = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_big);


        topic_paint = new Paint();
        topic_paint.setTextSize(80);
        topic_paint.setTextAlign(Paint.Align.LEFT);
        topic_paint.setColor(Color.BLACK);

        content_paint = new Paint();
        content_paint.setTextAlign(Paint.Align.CENTER);
        content_paint.setTextSize(70);
        content_paint.setColor(context.getResources().getColor(R.color.lineColor));

        mSoundManager = new SoundManager(context);

        running1 = true;
        running2 = true;

        mThread = new DrawThread();
        mThread.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(drone_running){
                    radian += interval;
                    try{
                        Thread.sleep(5);
                    }catch (InterruptedException e){}
                    IntroViewHandler.obtainMessage(INVALIDATE).sendToTarget();

                    if (radian >= 12.56 || radian  < 0) {
                        interval *= -1;
                    }
                }
            }
        }).start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

            if (size[0] != 0 && size[1] != 0) {
                canvas.drawBitmap(intro_image, size[0] / 2 - intro_image.getWidth() * 9 / 8, 0, bitmap_paint);
                int text_current_x = size[0] / 2 + intro_image.getWidth() / 8;
                if (bitmap_paint.getAlpha() > 0) {
                    topic_paint.setAlpha(bitmap_paint.getAlpha());
                    canvas.drawText(text1, text_current_x, intro_image.getHeight() * 3 / 10, topic_paint);
                }
                if (bitmap_paint.getAlpha() > 85) {
                    topic_paint.setAlpha(bitmap_paint.getAlpha());
                    canvas.drawText(text2, text_current_x, intro_image.getHeight() * 6 / 10, topic_paint);
                }
                if (bitmap_paint.getAlpha() > 170) {
                    topic_paint.setAlpha(bitmap_paint.getAlpha());
                    canvas.drawText(text3, text_current_x, intro_image.getHeight() * 9 / 10, topic_paint);
                }

                canvas.drawText(current_content, size[0] / 2, size[1] * 3 / 5, content_paint);
                canvas.drawBitmap(drone,drone1_position[0], drone1_position[1] + (float)(size[1]-(radius-radius*Math.cos(3.141592)) - drone.getHeight()*3/2) ,bitmap_paint);

            }

        if(drone_running){


            drone1_position[0] = (float)(radius*radian - radius*Math.sin(radian));
            drone1_position[1] = (float)(radius - radius*Math.cos(radian));
            canvas.drawBitmap(drone,drone1_position[0], drone1_position[1] + (float)(size[1]-(radius-radius*Math.cos(3.141592)) - drone.getHeight()*3/2) ,bitmap_paint);
        }
    }

    public class DrawThread extends Thread{

        int[] textPosition  = new int[2];
        public int bitmap_alpha = 0;

        public DrawThread() {

        }

        @Override
        public void run() {
            super.run();
            while(running1){
                if(size[0] != 0 && size[1] != 0) {
                    // draw Bitmap
                    bitmap_paint.setAlpha(bitmap_alpha += 3);

                    if (bitmap_alpha >= 255) {
                        running1 = false;
                    }
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                    }
                    ;
                    IntroViewHandler.obtainMessage(INVALIDATE).sendToTarget();
                }
            }
            int index = 1;
            while(running2){
                current_content = "";
                for(int i=0; i<index ; i++){
                    current_content += content.charAt(i);
                }
                IntroViewHandler.obtainMessage(INVALIDATE).sendToTarget();
                mSoundManager.play(0);

                if(index == content.length() ){

                    running2 = false;
                    drone_running = true;
//
                }
                index++;

                try{
                    Thread.sleep(80);
                }catch (InterruptedException e){};
            }
//
        }

        public int getBitmap_alpha(){
            return bitmap_alpha;
        }
    }

    public void setSize(int[] size){
        this.size = size;
        Log.d("Tutorial","Width : " + String.valueOf(size[0]) + "\n Height : " + String.valueOf(size[1]));
    }

    public boolean isRunning1(){
        return running1;
    }

    public boolean isRunning2(){
        return running2;
    }

    public boolean isDrone_running(){
        return drone_running;
    }

    public void setRunning1(boolean running1){
        this.running1 = running1;
    }

    public void setRunning2(boolean runnin2){
        this.running2 = running2;
    }

    public void setDrone_running(boolean drone_running){
        this.drone_running = drone_running;
    }

    public void stopThread(){
        running1 = false;
        running2 = false;
        drone_running = false;
    }

}
