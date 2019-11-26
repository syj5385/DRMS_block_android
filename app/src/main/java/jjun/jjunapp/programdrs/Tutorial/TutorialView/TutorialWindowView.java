package jjun.jjunapp.programdrs.Tutorial.TutorialView;

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
import android.widget.Toast;

import java.util.ArrayList;

import jjun.jjunapp.programdrs.Command.Command;
import jjun.jjunapp.programdrs.Command.CommandIcon;
import jjun.jjunapp.programdrs.R;

import static android.content.ContentValues.TAG;

/**
 * Created by jjunj on 2017-09-13.
 */

public class TutorialWindowView extends View  {

    // Macro Setting
    public static final int ICON_ON_TOUCH_EVENT = 1;
    public static final int REMOVE_ALL_LAYOUT = 11;
    public static final int REMOVE_SETTING_LAYOUT = 12;
    public static final int REMOVE_ICON_LAYOUT = 13;
    public static final int INFLATE_CHILDREN_LAYOUT = 14;
    public static final int TOUCH_NO_ICON = 15;
    public static final int REMOVE_SETTINGBOX = 16;
    public static final int COPY_COMMAND_ITEM = 17;

    private static final int CURRENT_MODE = 5;

    public static final int NORMAL_MODE = 0;
    public static final int COPY_MODE = 1;
    public static final int TUTORIAL_MODE = 2;

    private Activity mActivity;
    private Handler mHandler;
    private Context mContext;
    private static final int horizontal = 6;
    private static final int vertical = 26;
    private int position_X[] = new int[horizontal];
    private int position_Y[] = new int[vertical];

    private Handler ViewHandler;

    private int index_X = 0;
    private int index_Y = 0;

    private int view_width, view_height;

    private int current_mode = NORMAL_MODE;

    private int prev_concentration_index = -1;

    //Object
    private ArrayList<CommandIcon> command_icon = new ArrayList<CommandIcon>();
    private Paint paint = new Paint();
    private Paint line_paint = new Paint();

    public TutorialWindowView(Context context,Activity mActivity, Handler mHandler , int[] position_X, int[] position_Y ) {
        super(context);

        this.mActivity = mActivity;
        this.mHandler = mHandler;
        mContext = context;
        this.position_X = position_X;
        this.position_Y = position_Y;
        this.view_width = view_width;
        this.view_height = view_height;

        line_paint.setColor(getResources().getColor(R.color.lineColor));
        line_paint.setStrokeWidth((float)10.0);
        line_paint.setAntiAlias(true);

        current_mode = NORMAL_MODE;
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
                    canvas.drawLine(command_icon.get(i).getX() + (command_icon.get(i).getDisplayed_icon().getWidth() / 2)
                            ,command_icon.get(i).getY() + (command_icon.get(i).getDisplayed_icon().getHeight() / 2)
                            ,command_icon.get(i).getX() + (command_icon.get(i).getDisplayed_icon().getWidth() / 2)
                            ,((command_icon.get(i).getY() + (command_icon.get(i).getDisplayed_icon().getHeight() / 2)) +  (command_icon.get(i+1).getY() + (command_icon.get(i+1).getDisplayed_icon().getHeight() / 2)))/2
                            ,line_paint);

                    canvas.drawLine(command_icon.get(i).getX() + (command_icon.get(i).getDisplayed_icon().getWidth() / 2)
                            ,((command_icon.get(i).getY() + (command_icon.get(i).getDisplayed_icon().getHeight() / 2)) +  (command_icon.get(i+1).getY() + (command_icon.get(i+1).getDisplayed_icon().getHeight() / 2)))/2
                            ,command_icon.get(i + 1).getX() + (command_icon.get(i+1).getDisplayed_icon().getWidth() / 2)
                            ,((command_icon.get(i).getY() + (command_icon.get(i).getDisplayed_icon().getHeight() / 2)) +  (command_icon.get(i+1).getY() + (command_icon.get(i+1).getDisplayed_icon().getHeight() / 2)))/2
                            ,line_paint);

                    canvas.drawLine(command_icon.get(i + 1).getX() + (command_icon.get(i+1).getDisplayed_icon().getWidth() / 2)
                            ,((command_icon.get(i).getY() + (command_icon.get(i).getDisplayed_icon().getHeight() / 2)) +  (command_icon.get(i+1).getY() + (command_icon.get(i+1).getDisplayed_icon().getHeight() / 2)))/2
                            ,command_icon.get(i + 1).getX() + (command_icon.get(i+1).getDisplayed_icon().getWidth() / 2)
                            ,command_icon.get(i+1).getY() + (command_icon.get(i).getDisplayed_icon().getHeight() / 2)
                            ,line_paint);
                }
            }
        }
        for(int i=0; i<command_icon.size(); i++) {
            canvas.drawBitmap(command_icon.get(i).displayed_icon, command_icon.get(i).getX(), command_icon.get(i).getY(), paint);
        }

        if(command_view_hint_layout == tuto0_2){
            displayTutorial0_2(canvas);
        }
        if(command_view_hint_layout == tuto1_0){
            displayTutorial1_0(canvas);
        }
        if(command_view_hint_layout == tuto1_1){
            displayTutorial1_1(canvas);
        }


    }

    public void addCommand(int command){
        if(index_Y < vertical-1){
//            for(int i=0; i<command_icon.size() ; i++){
//                command_icon.get(i).icon_unchecked();
//            }
            int index = index_X + index_Y*(horizontal-1);
            command_icon.add(new CommandIcon(mContext,position_X[index_X],position_Y[index_Y],command,0,0,false));


//            command_icon.get(index).icon_checked();
//            mHandler.obtainMessage(ICON_ON_TOUCH_EVENT,INFLATE_CHILDREN_LAYOUT,index).sendToTarget();

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

    public void addCommand(int command, int value, int value2){
        if(index_Y < (vertical-1)){
//            for(int i=0; i<command_icon.size() ; i++){
//                command_icon.get(i).icon_unchecked();
//            }
            int index = index_X + index_Y*(horizontal-1);
            command_icon.add(new CommandIcon(mContext,position_X[index_X],position_Y[index_Y],command,value,value2,false));

//            command_icon.get(index).icon_checked();
//            mHandler.obtainMessage(ICON_ON_TOUCH_EVENT,INFLATE_CHILDREN_LAYOUT,index).sendToTarget();

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

    public void removeComand(int index){
        if(command_icon.size() > 1){ // not 1 elements
            command_icon.remove(index);

            int temp_index_X = 0;
            int temp_index_Y = 0;

            for(int i=0; i<command_icon.size() ; i++){
                temp_index_X = i%(horizontal-1);
                temp_index_Y = i/(horizontal-1);
                command_icon.get(i).setX(position_X[temp_index_X]);
                command_icon.get(i).setY(position_Y[temp_index_Y]);
            }

            index_X = ++temp_index_X;
            index_Y = temp_index_Y ;
            if(index_X == (horizontal-1)){
                index_X = 0 ;
                index_X ++;

            }

        }
        else{   // just 1 elements
            command_icon.remove(index);
            prev_concentration_index = -1;
            index_X = 0 ;
            index_Y = 0 ;
        }
        Log.d(TAG,"prev Con : " + prev_concentration_index);
        invalidate();
    }

    public void removeAllCommand(){
        while(command_icon.size() != 0){
            command_icon.remove(0);
        }
        invalidate();
    }

    public void moveLeft(int index){
        if(index == 0){
            Toast.makeText(mContext,"명령어를 이동할 수 없습니다.",Toast.LENGTH_SHORT).show();
        }
        else {
            int temp_index_X = 0;
            int temp_index_Y = 0;

            CommandIcon temp_icon = command_icon.get(index - 1);
            command_icon.remove(index-1);
            command_icon.add(index, temp_icon);

            for(int i=0; i<command_icon.size() ; i++){
                temp_index_X = i%(horizontal-1);
                temp_index_Y = i/(horizontal-1);

                command_icon.get(i).setX(position_X[temp_index_X]);
                command_icon.get(i).setY(position_Y[temp_index_Y]);

            }

            index_X = ++temp_index_X;
            index_Y = temp_index_Y;
            if(index_X == (horizontal-1)){
                index_X = 0;
                index_Y++;
            }
        }

        invalidate();
    }

    public void moveRight(int index){
        if(index == command_icon.size()-1){
            Toast.makeText(mContext,"명령어를 이동할 수 없습니다.",Toast.LENGTH_SHORT).show();
        }
        else {
            int temp_index_X = 0;
            int temp_index_Y = 0;

            CommandIcon temp_icon = command_icon.get(index + 1);
            command_icon.remove(index+1);
            command_icon.add(index, temp_icon);

            for(int i=0; i<command_icon.size() ; i++){
                temp_index_X = i%(horizontal-1);
                temp_index_Y = i/(horizontal-1);

                command_icon.get(i).x = position_X[temp_index_X];
                command_icon.get(i).y = position_Y[temp_index_Y];

            }

            index_X = ++temp_index_X;
            index_Y = temp_index_Y;
            if(index_X == (horizontal-1)){
                index_X = 0;
                index_Y++;
            }
        }
        invalidate();
    }


    public ArrayList<CommandIcon> getCommand_icon(){
        return command_icon;
    }

    public void updateValue(int index, int value){

        command_icon.get(index).setValue(value);
    }

    int[] edit_mode_down_position ;
    int[] edit_mode_up_position ;
    int edit_mode_down_index;
    int edit_mode_up_index;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int concentration_index = -1;

        if(event.getAction() == MotionEvent.ACTION_DOWN){

//            mHandler.obtainMessage(ICON_ON_TOUCH_EVENT,REMOVE_MENU_LAYOUT,-1).sendToTarget();
//            mHandler.obtainMessage(ICON_ON_TOUCH_EVENT,REMOVE_SETTING_LAYOUT,-1).sendToTarget();
//            mHandler.obtainMessage(ICON_ON_TOUCH_EVENT,REMOVE_SETTINGBOX,-1).sendToTarget();

//            mHandler.obtainMessage(ICON_ON_TOUCH_EVENT,REMOVE_ALL_LAYOUT,-1).sendToTarget();
//            mHandler.obtainMessage(ICON_ON_TOUCH_EVENT,TOUCH_NO_ICON,-1).sendToTarget();

        }



        else if(event.getAction() == MotionEvent.ACTION_UP){
            mHandler.obtainMessage(ICON_ON_TOUCH_EVENT,REMOVE_ALL_LAYOUT,-1).sendToTarget();

            float touch_X = event.getX();
            float touch_Y = event.getY();

            for(int i=0; i<command_icon.size(); i++){
                if(touch_X > command_icon.get(i).getX()
                        && touch_X < command_icon.get(i).getX()+command_icon.get(i).getDisplayed_icon().getWidth()
                        && touch_Y > command_icon.get(i).getY()
                        && touch_Y < command_icon.get(i).getY()+command_icon.get(i).getDisplayed_icon().getHeight()){

                    concentration_index = i;

                }
            }

            Log.d("View","Concentration Index : " + String.valueOf(concentration_index));

            if(current_mode == NORMAL_MODE) {

                if (concentration_index != -1) {
                    // Icon Touch
                    if (concentration_index != prev_concentration_index) {
                        if(prev_concentration_index != -1)
                            command_icon.get(prev_concentration_index).icon_unchecked();

                        command_icon.get(concentration_index).icon_checked();
                        mHandler.obtainMessage(ICON_ON_TOUCH_EVENT, INFLATE_CHILDREN_LAYOUT, concentration_index).sendToTarget();
                        prev_concentration_index = concentration_index;
                    }
                    else{
                        command_icon.get(concentration_index).icon_unchecked();
                        mHandler.obtainMessage(ICON_ON_TOUCH_EVENT,TOUCH_NO_ICON,-1).sendToTarget();
                        prev_concentration_index = -1;
                    }

                } else {
                    // Icon not Touch
                    mHandler.obtainMessage(ICON_ON_TOUCH_EVENT, TOUCH_NO_ICON, concentration_index).sendToTarget();
                    prev_concentration_index = -1;
                }
            }
            else if(current_mode == COPY_MODE){
                if (concentration_index != -1) {
                    if (!command_icon.get(concentration_index).getIconChecked()) {
                        command_icon.get(concentration_index).icon_checked();
                        mHandler.obtainMessage(ICON_ON_TOUCH_EVENT, COPY_COMMAND_ITEM, concentration_index).sendToTarget();
                    }
                    else{
                        command_icon.get(concentration_index).icon_unchecked();
                    }
                }
            }


//            if(current_mode == NORMAL_MODE) {
//                for (int i = 0; i < command_icon.size(); i++)
//                    command_icon.get(i).icon_unchecked();
//
//                if (concentration_index < command_icon.size()) {
//
//                    command_icon.get(concentration_index).icon_checked();
//                    mHandler.obtainMessage(ICON_ON_TOUCH_EVENT, INFLATE_CHILDREN_LAYOUT, concentration_index).sendToTarget();
//                } else {
//                    mHandler.obtainMessage(ICON_ON_TOUCH_EVENT, TOUCH_NO_ICON, concentration_index).sendToTarget();
//                }
//            }
//            else if(current_mode == COPY_MODE){
//                if (concentration_index < command_icon.size()) {
//                    if(!command_icon.get(concentration_index).getIconChecked()) {
//                        command_icon.get(concentration_index).icon_checked();
////                        mHandler.obtainMessage(ICON_ON_TOUCH_EVENT, COPY_COMMAND_ITEM, concentration_index).sendToTarget();
//                    }
//                    else{
//                        command_icon.get(concentration_index).icon_unchecked();
//                    }
//                }
//                else{
//
//                }
//            }
        }


        invalidate();
        return true;
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
           if(msg.what == CURRENT_MODE ){
               switch(msg.arg1){
                   case NORMAL_MODE :
                       current_mode = NORMAL_MODE;

                       break;

                   case COPY_MODE :
                       current_mode = COPY_MODE;
                       break;

                   case TUTORIAL_MODE :
                       current_mode = TUTORIAL_MODE;
                       break;
               }
               Log.d("Current Mode", "Current Mode : " + String.valueOf(current_mode));
           }
       }
   }

   public Handler getViewHandler(){
       return ViewHandler;
   }

   public int getCurrentMode(){
       return current_mode;
   }

    private int command_view_hint_layout = -1;
    public static final int tuto0_2 =0;
    public static final int tuto1_0 = 1;
    public static final int tuto1_1 =2;

    public void setCommandViewhint(int tuto ){
        this.command_view_hint_layout = tuto;
        prev_concentration_index = -1;
    }

    private void displayTutorial0_2(Canvas canvas){
        Paint hintPaint = new Paint();
        hintPaint.setColor(Color.GRAY);
        hintPaint.setStyle(Paint.Style.STROKE);
        hintPaint.setStrokeWidth(10);

        Paint hintCheckPaint = new Paint();
        hintCheckPaint.setColor(Color.RED);
        hintCheckPaint.setStyle(Paint.Style.STROKE);
        hintCheckPaint.setStrokeWidth(10);

        Paint hintTextPaint = new Paint();
        hintTextPaint.setColor(Color.GRAY);
        hintTextPaint.setTextSize(30);
        hintTextPaint.setTextAlign(Paint.Align.CENTER);

        Bitmap[] command = {
                BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.start),
                BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.motor3),
                BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.motor10),
                BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.timer_sec),
                BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.end)
        };

        int[] command_check = {
                Command.START,Command.MOTOR3, Command.MOTOR10,Command.TIMER_SEC,Command.END
        };

        String[] text= {
                "Start","Motor3","Motor10","Timer(s)","End"
        };

        for(int i=0; i<5; i++) {
            canvas.drawRect(position_X[i],position_Y[0], position_X[i]+command[i].getWidth(),position_Y[0]+command[i].getHeight(),hintPaint );
            canvas.drawText(text[i],position_X[i]+command[i].getWidth()/2,position_Y[0]+command[i].getHeight()+ hintTextPaint.getTextSize()*3/2,hintTextPaint);
        }
        for(int i=0; i<command_icon.size();i++){
            if(command_icon.get(i).getCommand() == command_check[i]){
                canvas.drawRect(position_X[i],position_Y[0], position_X[i]+command[i].getWidth(),position_Y[0]+command[i].getHeight(),hintCheckPaint );
            }
            else{
                canvas.drawRect(position_X[i],position_Y[0], position_X[i]+command[i].getWidth(),position_Y[0]+command[i].getHeight(),hintPaint );

            }

        }

        if(command_icon.size() == 5){
            for(int i=0; i<5; i++) {
                if (command_icon.get(i).getCommand() == command_check[i]) {
                    if(i == 4){
                        mActivity.finish();
                        Toast.makeText(mContext,"OK",Toast.LENGTH_SHORT).show();

                    }
                }
                else {
                    removeAllCommand();
                    invalidate();
                    Toast.makeText(mContext,"명령어가 잘못 입력되었습니다.\n다시 선택해주세요",Toast.LENGTH_SHORT).show();
                    index_X = 0;
                    index_Y = 0;
                    break;
                }
            }
        }
    }

    public void displayTutorial1_0(Canvas canvas){
        Paint hint_paint = new Paint();
        hint_paint.setColor(Color.RED);
        hint_paint.setStrokeWidth(5);
        hint_paint.setStyle(Paint.Style.STROKE);

        Bitmap icon = command_icon.get(1).getDisplayed_icon();
        float x = command_icon.get(1).getX();
        float y = command_icon.get(1).getY();

        canvas.drawRect(x,y+3,x+icon.getWidth(),y+icon.getHeight()-3,hint_paint);

    }

    public void displayTutorial1_1(Canvas canvas){

        Bitmap[] exCommand = {
                BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.start),
                BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.motor5),
                BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.motor3),
                BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.timer_sec),
                BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.end),
        };

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);

        Paint exPaint = new Paint();
        exPaint.setColor(Color.rgb(0xEF,0xEF,0xEF));

        Paint textPaint = new Paint();
        textPaint.setTextSize(60);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.BLUE);

        canvas.drawText("<Example>",((position_X[0])+(position_X[4]+exCommand[0].getHeight()))/2,position_Y[1]-textPaint.getTextSize()/2,textPaint);
        canvas.drawRect(0,position_Y[1],position_X[4]+exCommand[0].getWidth(),position_Y[2],exPaint);



        for(int i=1; i<5; i++){
            canvas.drawLine(position_X[i-1]+exCommand[i-1].getWidth()/2,position_Y[1]+exCommand[i-1].getHeight()/2,
                    position_X[i]+exCommand[i].getWidth()/2, position_Y[1] + exCommand[i].getHeight()/2,paint);
        }
        for(int i=0; i<exCommand.length;i++) {
            canvas.drawBitmap(exCommand[i], position_X[i], position_Y[1],null);
        }

        textPaint.setTextSize(40);
        textPaint.setColor(Color.BLACK);
        canvas.drawText("위와 같이 명령어를 만들어 보세요.", ((position_X[0])+(position_X[4]+exCommand[0].getHeight()))/2,position_Y[2],textPaint);
        canvas.drawText("100",position_X[1]+exCommand[0].getWidth()/2, position_Y[1]+exCommand[0].getHeight()+textPaint.getTextSize()*3/2, textPaint);
        canvas.drawText("100",position_X[2]+exCommand[0].getWidth()/2, position_Y[1]+exCommand[0].getHeight()+textPaint.getTextSize()*3/2, textPaint);
        canvas.drawText("2",position_X[3]+exCommand[0].getWidth()/2, position_Y[1]+exCommand[0].getHeight()+textPaint.getTextSize()*3/2, textPaint);

    }

    public void setPrev_concentration_index(int index){
        this.prev_concentration_index = index;
    }

}