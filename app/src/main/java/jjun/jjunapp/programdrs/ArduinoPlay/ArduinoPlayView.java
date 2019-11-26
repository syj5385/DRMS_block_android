package jjun.jjunapp.programdrs.ArduinoPlay;

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
import android.view.View;
import android.widget.Toast;



import java.util.ArrayList;

import jjun.jjunapp.programdrs.Command.CommandIcon;
import jjun.jjunapp.programdrs.R;

/**
 * Created by jjunj on 2017-09-13.
 */

public class ArduinoPlayView extends View  {

    // Macro Setting

    private static final int horizontal = 8;
    private static final int vertical = 25;
    private static final int CURRENT_MODE = 5;

    public static final int NORMAL_MODE = 0;
    public static final int COPY_MODE = 1;
    public static final int EDIT_MODE = 2;

    private Activity mActivity;
    private Handler mHandler;
    private Context mContext;
    private int position_X[] = new int[horizontal];
    private int position_Y[] = new int[vertical];

    private Handler ViewHandler;

    private int index_X = 0;
    private int index_Y = 0;

    private int view_width, view_height;

    private int current_index = -1;
    private int current_X;
    private int current_Y;

    //Object
    private ArrayList<CommandIcon> command_icon = new ArrayList<CommandIcon>();
    private Paint paint = new Paint();
    private Paint line_paint = new Paint();
    private Paint[] textpaint = new Paint[4];


    public ArduinoPlayView(Context context, Handler mHandler , int[] position_X, int[] position_Y ) {
        super(context);

        this.mActivity = mActivity;
        this.mHandler = mHandler;
        mContext = context;
        this.position_X = position_X;
        this.position_Y = position_Y;
        this.view_width = view_width;
        this.view_height = view_height;

        line_paint.setColor(Color.BLACK);
        line_paint.setStrokeWidth((float)5.0);
        line_paint.setAntiAlias(true);


        textpaint[0] = new Paint(); // motor
        textpaint[0].setTextSize(50);
        textpaint[0].setColor(Color.RED);
        textpaint[0].setTextAlign(Paint.Align.CENTER);

        textpaint[1] = new Paint();
        textpaint[1].setColor(Color.BLACK);
        textpaint[1].setStyle(Paint.Style.STROKE);
        textpaint[1].setStrokeWidth(4);

        textpaint[2] = new Paint(); // jump
        textpaint[2].setTextSize(50);
        textpaint[2].setColor(Color.argb(255,34,116,28));
        textpaint[2].setTextAlign(Paint.Align.CENTER);

        textpaint[3] = new Paint(); // timer
        textpaint[3].setTextSize(50);
        textpaint[3].setColor(Color.BLUE);
        textpaint[3].setTextAlign(Paint.Align.CENTER);


        ViewHandler = new ViewHandler();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(command_icon == null){
            Log.e("View","null");
        }
        if(command_icon.size() > 1) {
           for(int i=0 ; i<command_icon.size()-1; i++){
               if(i % (horizontal-1) != (horizontal -2)) {
                   canvas.drawLine(command_icon.get(i).getX() + (command_icon.get(i).getDisplayed_icon().getWidth() / 2)
                           , command_icon.get(i).getY() + (command_icon.get(i).getDisplayed_icon().getHeight() / 2)
                           , command_icon.get(i + 1).getX() + (command_icon.get(i).getDisplayed_icon().getWidth() / 2)
                           , command_icon.get(i + 1).getY() + (command_icon.get(i).getDisplayed_icon().getHeight() / 2)
                           , line_paint);
               }
               else{
                   canvas.drawLine(command_icon.get(i).getX()+command_icon.get(i).getDisplayed_icon().getWidth()/2,
                           command_icon.get(i).getY()+command_icon.get(i).getDisplayed_icon().getHeight()/2,
                           command_icon.get(i).getX() + (command_icon.get(i).getDisplayed_icon().getWidth()*5/4)
                           ,command_icon.get(i).getY() + (command_icon.get(i).getDisplayed_icon().getHeight() / 2)
                           ,line_paint);
                   canvas.drawLine(command_icon.get(i).getX() + (command_icon.get(i).getDisplayed_icon().getWidth()*5/4)
                           ,command_icon.get(i).getY() + (command_icon.get(i).getDisplayed_icon().getHeight() / 2-line_paint.getStrokeWidth()/2)
                           ,command_icon.get(i).getX() + (command_icon.get(i).getDisplayed_icon().getWidth()*5/4)
                           ,((command_icon.get(i).getY() + (command_icon.get(i).getDisplayed_icon().getHeight() / 2)) +  (command_icon.get(i+1).getY() + (command_icon.get(i+1).getDisplayed_icon().getHeight() / 2)))/2 + line_paint.getStrokeWidth()/2
                           ,line_paint);

                   canvas.drawLine(command_icon.get(i).getX() + (command_icon.get(i).getDisplayed_icon().getWidth()*5 / 4)
                           ,((command_icon.get(i).getY() + (command_icon.get(i).getDisplayed_icon().getHeight() / 2)) +  (command_icon.get(i+1).getY() + (command_icon.get(i+1).getDisplayed_icon().getHeight() / 2)))/2
                           ,command_icon.get(i + 1).getX() + (command_icon.get(i+1).getDisplayed_icon().getWidth() / 2)
                           ,((command_icon.get(i).getY() + (command_icon.get(i).getDisplayed_icon().getHeight() / 2)) +  (command_icon.get(i+1).getY() + (command_icon.get(i+1).getDisplayed_icon().getHeight() / 2)))/2
                           ,line_paint);

                   canvas.drawLine(command_icon.get(i + 1).getX() + (command_icon.get(i+1).getDisplayed_icon().getWidth() / 2)
                           ,((command_icon.get(i).getY() + (command_icon.get(i).getDisplayed_icon().getHeight() / 2)) +  (command_icon.get(i+1).getY() + (command_icon.get(i+1).getDisplayed_icon().getHeight() / 2)))/2 - line_paint.getStrokeWidth()/2
                           ,command_icon.get(i + 1).getX() + (command_icon.get(i+1).getDisplayed_icon().getWidth() / 2)
                           ,command_icon.get(i+1).getY() + (command_icon.get(i).getDisplayed_icon().getHeight() / 2)
                           ,line_paint);
               }
           }

            for(int i=0; i<command_icon.size(); i++) {
                canvas.drawBitmap(command_icon.get(i).displayed_icon, command_icon.get(i).getX(), command_icon.get(i).getY(), paint);
                float textLength = textpaint[0].measureText(String.valueOf(command_icon.get(i).getValue()));
                float textX = command_icon.get(i).getX()+command_icon.get(i).getDisplayed_icon().getWidth()/2;
                float textY = command_icon.get(i).getY()+command_icon.get(i).getDisplayed_icon().getHeight()+textpaint[0].getTextSize();


                if(command_icon.get(i).getCommand() >=12 && command_icon.get(i).getCommand() < 19)
                    canvas.drawText(String.valueOf(command_icon.get(i).getValue()), textX, textY,textpaint[0]);
                else if(command_icon.get(i).getCommand() == 20 || command_icon.get(i).getCommand() ==21)
                    canvas.drawText(String.valueOf(command_icon.get(i).getValue()), textX, textY,textpaint[3]);
                else if(command_icon.get(i).getCommand() >= 22 && command_icon.get(i).getCommand() < 30){
                    if(command_icon.get(i).getCommand() % 2 == 0){
                        canvas.drawText(String.valueOf(command_icon.get(i).getValue()), textX, textY,textpaint[2]);
                    }
                }

                if(command_icon.get(i).getCommand() >=12 && command_icon.get(i).getCommand() < 22) {
                    if(command_icon.get(i).getCommand() != 19) {

                        canvas.drawRect(textX - textLength / 2 - textpaint[0].getTextSize() / 10, textY - textpaint[0].getTextSize() * 11 / 10,
                                textX + textLength / 2 + textpaint[0].getTextSize() / 10, textY + textpaint[0].getTextSize() / 10, textpaint[1]);
                    }

                }
                else if(command_icon.get(i).getCommand() >= 22 && command_icon.get(i).getCommand() < 30){
                    if(command_icon.get(i).getCommand() % 2 == 0){
                        canvas.drawRect(textX - textLength / 2 - textpaint[0].getTextSize() / 10, textY - textpaint[0].getTextSize() * 11 / 10,
                                textX + textLength / 2 + textpaint[0].getTextSize() / 10, textY + textpaint[0].getTextSize() / 10, textpaint[1]);
                    }
                }
            }

        }
        for(int i=0; i<command_icon.size(); i++) {
            canvas.drawBitmap(command_icon.get(i).displayed_icon, command_icon.get(i).getX(), command_icon.get(i).getY(), paint);
        }

        if(current_index != -1){
//            canvas.drawLine(command_icon.get(current_index).getX(), command_icon.get(current_index).getY(),
//                    command_icon.get(current_index).getX() + command_icon.get(current_index).getDisplayed_icon().getWidth()/2,
//                    command_icon.get(current_index).getY() + command_icon.get(current_index).getDisplayed_icon().getHeight(),line_paint);

            Bitmap current = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.appmenu);
            canvas.drawBitmap(current,
                    (command_icon.get(current_index).getX() + command_icon.get(current_index).getDisplayed_icon().getWidth()/2) - (current.getWidth()/2),
                    (command_icon.get(current_index).getY() + command_icon.get(current_index).getDisplayed_icon().getHeight()/2) - (current.getHeight()/2)
                      ,paint);
        }

    }

    public void addCommand(int command, int value, int value2){
        if(index_Y < vertical-1){

            int index = index_X + index_Y*(horizontal-1);
            command_icon.add(new CommandIcon(mContext,position_X[index_X],position_Y[index_Y],command,value,value2,false));

            index_X++;

            Log.d("INDEX" , "x: " + String.valueOf(index_X) + "/ y: " +String.valueOf(index_Y) );

            if(index_X == horizontal-1 ) {
                index_X = 0;
                index_Y ++;
                if(index_Y == vertical-1) {
                    Toast.makeText(mContext, "마지막 아이콘 입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else{
            Toast.makeText(mContext,"더 이상 아이콘을 추가할 수 없습니다.",Toast.LENGTH_SHORT).show();
        }
        invalidate();
    }

    public void removeAllCommand(){
        while(command_icon.size() != 0){
            command_icon.remove(0);
        }
        index_X = 0;
        index_Y = 0;
        invalidate();
    }

    public void setPosition(int[] position_x, int[] position_y){
        this.position_X = position_x;
        this.position_Y = position_y;
    }

    public ArrayList<CommandIcon> getCommand_icon(){
        return command_icon;
    }

    public void updateValue(int index, int value){

        command_icon.get(index).setValue(value);
    }

    public void setIndex_X(int indexX){
        index_X = indexX;

    }

    public void setIndex_Y(int indexY){
        index_Y = indexY;

    }



   private class ViewHandler extends Handler{
       public ViewHandler() {
           super();
       }

       @Override
       public void handleMessage(Message msg) {
           super.handleMessage(msg);

       }
   }

   public Handler getViewHandler(){
       return ViewHandler;
   }

   public void setCurrent_index(int current_index){
       this.current_index = current_index;
       this.current_X = current_index % (horizontal-1);
       this.current_Y = current_index / (horizontal-1);


   }

}
