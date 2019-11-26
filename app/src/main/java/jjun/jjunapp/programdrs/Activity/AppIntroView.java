package jjun.jjunapp.programdrs.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import jjun.jjunapp.programdrs.R;

/**
 * Created by jjunj on 2017-10-19.
 */

public class AppIntroView extends View {

    private Context mContext;
    private Activity mActivity;

    private Character mainCharacter;
    private Paint[] paint = new Paint[5];

    public AppIntroView(Context mContext, Activity mActivity) {
        super(mContext);

        this.mContext = mContext;
        this.mActivity = mActivity;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("View","Size : " + canvas.getWidth() + " / " + canvas.getHeight());

        mainCharacter = new Character(canvas,0,0);
        float[] position = {
                canvas.getWidth()-mainCharacter.getImage().getWidth()*5/4,
                canvas.getHeight()-mainCharacter.getImage().getHeight()*5/4
        };

        mainCharacter.setPosition(position);
        mainCharacter.drawCharacter();

        float box_X = canvas.getWidth()-mainCharacter.getImage().getWidth()*5/4;
        float box_init_X = box_X-mainCharacter.getImage().getWidth()*5/2;
        float box_Y = canvas.getHeight()-mainCharacter.getImage().getHeight()*5/4;
        float box_inint_Y = box_Y-mainCharacter.getImage().getHeight()*3/2;

        paint[0] = new Paint();
        paint[0].setColor(Color.BLACK);
        paint[0].setStyle(Paint.Style.STROKE);
        paint[0].setStrokeWidth(15);

        paint[1] = new Paint();
        paint[1].setColor(Color.LTGRAY);
        paint[1].setStyle(Paint.Style.FILL);

        paint[2] = new Paint();
        paint[2].setColor(Color.BLACK);
        paint[2].setTextSize(80.0f);
        paint[2].setTextAlign(Paint.Align.CENTER);

//        canvas.drawRect(box_X-mainCharacter.getImage().getWidth()*5/2,box_Y-mainCharacter.getImage().getHeight()*3/2,
//                box_X,box_Y,paint[0]);

        canvas.drawLine(box_init_X,box_inint_Y, box_X,box_inint_Y,paint[0]);
        canvas.drawLine(box_init_X,box_inint_Y,box_init_X,box_Y,paint[0]);
        canvas.drawLine(box_init_X,box_Y,box_X,box_Y,paint[0]);
        canvas.drawLine(box_X,box_inint_Y,box_X,box_Y-mainCharacter.getImage().getHeight()/5,paint[0]);
        canvas.drawLine(box_X-paint[0].getStrokeWidth()/2,box_Y,box_X+mainCharacter.getImage().getWidth()/5,box_Y +mainCharacter.getImage().getHeight()/5,paint[0]);
        canvas.drawLine(box_X,box_Y-mainCharacter.getImage().getHeight()/5-paint[0].getStrokeWidth()/2, box_X+mainCharacter.getImage().getWidth()/5,box_Y +mainCharacter.getImage().getHeight()/5,paint[0]);

        canvas.drawText("어서오세요^^",(box_init_X+box_X)/2,(box_inint_Y+box_Y)/2+paint[2].getTextSize()/2, paint[2] );

    }

    private class Character{

        private Canvas canvas ;
        private Bitmap image;
        private float x,y;

        public Character(Canvas canvas, float x, float y) {
            image = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.character);
            this.x = x;
            this.y = y;
            this.canvas = canvas;

        }

        public void drawCharacter(){
            canvas.drawBitmap(image,x, y,null);
        }

        public void setPosition(float[] position){
            x = position[0];
            y = position[1];
        }

        public Bitmap getImage(){
            return image;
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            mActivity.finish();
        }
        return super.onTouchEvent(event);
    }

    public void createRequestTutorialDialog(){
        final AlertDialog.Builder requestTutoDialog = new AlertDialog.Builder(mContext);

        requestTutoDialog.setCustomTitle((LinearLayout) View.inflate(mContext,R.layout.requesttuto_topic,null));

        ConstraintLayout request_layout = (ConstraintLayout)View.inflate(mContext,R.layout.activity_requesttuto,null);
        request_layout.setPadding(10,10,10,10);
        requestTutoDialog.setView(request_layout);


        requestTutoDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mActivity.setResult(1);
                mActivity.finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mActivity.finish();
            }
        }).create();

        requestTutoDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mActivity.finish();
            }
        });

        requestTutoDialog.show();
    }
}
