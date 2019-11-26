package jjun.jjunapp.programdrs.Command;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import jjun.jjunapp.programdrs.Activity.MainActivity;
import jjun.jjunapp.programdrs.FileManagement.FileManagement;
import jjun.jjunapp.programdrs.R;

/**
 * Created by jjunj on 2017-09-13.
 */

public class CommandWindowView extends View  {

    // Macro Setting
    public static final int ICON_ON_TOUCH_EVENT = 1;
    public static final int REMOVE_ALL_LAYOUT = 9;
    public static final int REMOVE_SETTING_LAYOUT = 12;
    public static final int REMOVE_ICON_LAYOUT = 13;
    public static final int INFLATE_CHILDREN_LAYOUT = 14;
    public static final int TOUCH_NO_ICON = 15;
    public static final int REMOVE_SETTINGBOX = 16;
    public static final int COPY_COMMAND_ITEM = 17;

    private static final int ADDCOMMAND = 11;

    private static final int CURRENT_MODE = 5;

    public static final int NORMAL_MODE = 0;
    public static final int COPY_MODE = 1;
    public static final int TUTORIAL_MODE = 2;
    public static final int ERROR_MODE = 3;

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

    public int prev_concentration_index = -1;

    //Object
    private ArrayList<CommandIcon> command_icon = new ArrayList<CommandIcon>();
    private ArrayList<ErrorContent> errorlist = new ArrayList<ErrorContent>();
    private Paint paint = new Paint();
    private Paint line_paint = new Paint();
    private Paint[] textpaint = new Paint[6];

    public CommandWindowView(Activity mActivity, Context context, Handler mHandler , int[] position_X, int[] position_Y ) {
        super(context);

        this.mActivity = mActivity;
        this.mHandler = mHandler;
        mContext = context;
        this.position_X = position_X;
        this.position_Y = position_Y;


        line_paint.setColor(getResources().getColor(R.color.lineColor));
        line_paint.setStrokeWidth((float)10.0);
        line_paint.setAntiAlias(true);

        current_mode = NORMAL_MODE;
        ViewHandler = new ViewHandler();

        textpaint[0] = new Paint(); // motor
        textpaint[0].setTextSize(50);
        textpaint[0].setColor(Color.RED);
        textpaint[0].setTextAlign(Paint.Align.CENTER);

        textpaint[1] = new Paint(); // timer
        textpaint[1].setTextSize(50);
        textpaint[1].setColor(Color.BLUE);
        textpaint[1].setTextAlign(Paint.Align.CENTER);

        textpaint[2] = new Paint(); // jump
        textpaint[2].setTextSize(50);
        textpaint[2].setColor(Color.argb(255,34,116,28));
        textpaint[2].setTextAlign(Paint.Align.CENTER);

        textpaint[3] = new Paint();
        textpaint[3].setColor(Color.BLACK);
        textpaint[3].setStyle(Paint.Style.STROKE);
        textpaint[3].setStrokeWidth(4);

        textpaint[4] = new Paint();
        textpaint[4].setColor(Color.BLUE);
        textpaint[4].setStyle(Paint.Style.STROKE);
        textpaint[4].setStrokeWidth(4);

        textpaint[5] = new Paint();
        textpaint[5].setColor(Color.RED);
        textpaint[5].setTextAlign(Paint.Align.CENTER);
        textpaint[5].setTextSize(100);
        textpaint[5].setStrokeWidth(3);

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
        }
        for(int i=0; i<command_icon.size(); i++) {
            canvas.drawBitmap(command_icon.get(i).displayed_icon, command_icon.get(i).getX(), command_icon.get(i).getY(), paint);
            float textLength = textpaint[0].measureText(String.valueOf(command_icon.get(i).getValue()));
            float textX = command_icon.get(i).getX()+command_icon.get(i).getDisplayed_icon().getWidth()/2;
            float textY = command_icon.get(i).getY()+command_icon.get(i).getDisplayed_icon().getHeight()+textpaint[0].getTextSize();


            if(command_icon.get(i).getCommand() >=12 && command_icon.get(i).getCommand() < 19)
                canvas.drawText(String.valueOf(command_icon.get(i).getValue()), textX, textY,textpaint[0]);
            else if(command_icon.get(i).getCommand() == 20 || command_icon.get(i).getCommand() ==21)
                canvas.drawText(String.valueOf(command_icon.get(i).getValue()), textX, textY,textpaint[1]);
            else if(command_icon.get(i).getCommand() >= 22 && command_icon.get(i).getCommand() < 30){
                if(command_icon.get(i).getCommand() % 2 == 0){
                    canvas.drawText(String.valueOf(command_icon.get(i).getValue()), textX, textY,textpaint[2]);
                }
            }

            if(command_icon.get(i).getCommand() >=12 && command_icon.get(i).getCommand() < 22) {
                if(command_icon.get(i).getCommand() != 19) {

                    canvas.drawRect(textX - textLength / 2 - textpaint[0].getTextSize() / 10, textY - textpaint[0].getTextSize() * 11 / 10,
                            textX + textLength / 2 + textpaint[0].getTextSize() / 10, textY + textpaint[0].getTextSize() / 10, textpaint[3]);
                }

            }
            else if(command_icon.get(i).getCommand() >= 22 && command_icon.get(i).getCommand() < 30){
                if(command_icon.get(i).getCommand() % 2 == 0){
                    canvas.drawRect(textX - textLength / 2 - textpaint[0].getTextSize() / 10, textY - textpaint[0].getTextSize() * 11 / 10,
                            textX + textLength / 2 + textpaint[0].getTextSize() / 10, textY + textpaint[0].getTextSize() / 10, textpaint[3]);
                }
            }
        }

        if(current_mode == ERROR_MODE){
            if(errorlist.size() > 0 ){
                for(int i=0; i<errorlist.size(); i++){
                    int errorindex = errorlist.get(i).getIndex();
                    int errorCode = errorlist.get(i).getErrorcode();

                    if(errorindex != -1) {
                        canvas.drawText("E", command_icon.get(errorindex).getX() + command_icon.get(errorindex).getDisplayed_icon().getWidth() / 2,
                                command_icon.get(errorindex).getY() + command_icon.get(errorindex).getDisplayed_icon().getHeight() / 2 + textpaint[5].getTextSize() / 2, textpaint[5]);
                    }
                }
            }
            else{
                Log.e("CommandView","Not Error");
            }
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

        ScrollView command_scroll = mActivity.findViewById(R.id.scrollView);
        int y = getCommand_icon().get(getCommand_icon().size()-1).getY()
                - getCommand_icon().get(getCommand_icon().size()-1).getDisplayed_icon().getHeight();
        command_scroll.smoothScrollTo(0,y);
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

            index_X = command_icon.size()%(horizontal-1);
            index_Y = command_icon.size()/(horizontal-1);

            Log.d("index","X : " + index_X + " / Y : " + index_Y);

        }
        else{   // just 1 elements
            command_icon.remove(index);
            index_X = 0 ;
            index_Y = 0 ;
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

        }

        else if(event.getAction() == MotionEvent.ACTION_UP){
            Log.w("View","currentMoe = " + current_mode);


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
                    Log.d("view","icon touch");
                    // Icon Touch
                    if (concentration_index != prev_concentration_index) {
                        Log.d("view","icon checked");
                        if(prev_concentration_index != -1)
                            command_icon.get(prev_concentration_index).icon_unchecked();

                        command_icon.get(concentration_index).icon_checked();
                        mHandler.obtainMessage(ICON_ON_TOUCH_EVENT, INFLATE_CHILDREN_LAYOUT, concentration_index).sendToTarget();
                        prev_concentration_index = concentration_index;
                    }
                    else{
                        Log.d("view","icon unchecked");
                        command_icon.get(concentration_index).icon_unchecked();
                        mHandler.obtainMessage(ICON_ON_TOUCH_EVENT,TOUCH_NO_ICON,-1).sendToTarget();
                        prev_concentration_index = -1;
                    }

                } else {
                    Log.d("view","nonicon Touch");
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
            else if(current_mode == ERROR_MODE){
                if(concentration_index != -1){
                    if(concentration_index < command_icon.size()){

                        ArrayList<String> error = new ArrayList<String>();
                        ArrayList<String> error_entire = new ArrayList<String>();
                        for(int i=0; i<errorlist.size() ; i++){
                            if(errorlist.get(i).getIndex() == concentration_index){
                                error.add(errorlist.get(i).getContent());
                                Log.d("CommandView","ErrorCode : " +  errorlist.get(i).getErrorcode());
                            }
                            if (errorlist.get(i).getIndex() == -1) {
                                error_entire.add(errorlist.get(i).getContent());
                            }
                        }

                        if(error.size()>0) {
                            AlertDialog.Builder ab_temp = new AlertDialog.Builder(mContext);
                            LinearLayout ab_view = (LinearLayout) View.inflate(mContext, R.layout.errorlayout, null);
                            TextView entireError = (TextView) ab_view.findViewById(R.id.entire_error);
                            TextView indexError = (TextView) ab_view.findViewById(R.id.index_error);

                            for (int i = 0; i < error.size(); i++) {
                                indexError.append((i + 1) + " : " + error.get(i) + "\n");
                            }
                            if(error_entire.size() > 0) {
                                for (int i = 0; i < error_entire.size(); i++) {
                                    entireError.append((i + 1) + " : " + error_entire.get(i) + "\n");
                                }
                            }
                            else{
                                entireError.append("X");
                            }

                            ab_temp.setNegativeButton("닫기", null);

                            ab_temp.setView(ab_view);
                            ab_temp.create().show();
                        }
                    }
                }
            }

            Log.w("View","Touch  End");

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

   public void setCurrentMode(int current_mode){
       this.current_mode = current_mode;
   }

   public void setPrev_concentration_index(int index){
       this.prev_concentration_index = index;
   }

   public void setErrorlist(ArrayList<ErrorContent> errorlist){
       this.errorlist = errorlist;
   }





}
