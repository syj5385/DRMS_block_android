package jjun.jjunapp.programdrs.Command;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;

import jjun.jjunapp.programdrs.R;


/**
 * Created by jjunj on 2017-09-13.
 */

public class CommandIcon {

    private Context mContext;
    public Bitmap displayed_icon;
    private Bitmap unchecked_icon, checked_icon;

    public int x,y;
    private boolean checked;
    private int command;
    private int value;
    private int value2;

    public CommandIcon(Context context, int x, int y, int command, int value, int value2, boolean checked) {
        mContext = context;
        this.x = x;
        this.y = y;
        this.command = command;
        this.value = value;
        this.value2 = value2;
        this.checked = checked;
        unchecked_icon = chooseUncheckedIcon(command);
        checked_icon = chooseCheckedIcon(command);
        if(checked)
            displayed_icon = checked_icon;
        else
            displayed_icon = unchecked_icon;

    }

    public void icon_checked(){
        checked = true;
        displayed_icon = checked_icon;
    }

    public void icon_unchecked(){
        checked = false;
        displayed_icon = unchecked_icon;
    }

    public Bitmap getDisplayed_icon(){
        return displayed_icon;
    }

    public Bitmap getUnchecked_icon(){
        return unchecked_icon;
    }

    public int getCommand(){
        return command;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void setValue(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public void setValue2(int value2){
        this.value2 = value2;
    }

    public int getValue2(){
        return value2;
    }

    public boolean getIconChecked(){
        return checked;
    }


    private Bitmap chooseUncheckedIcon(int command) {
        switch(command){
            case Command.MOTOR3 :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.motor3);

            case Command.MOTOR5 :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.motor5);

            case Command.MOTOR6 :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.motor6);

            case Command.MOTOR9 :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.motor9);

            case Command.MOTOR10 :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.motor10);

            case Command.MOTOR11 :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.motor11);

            case Command.ALLMOTOR :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.motorall);

            case Command.STOP :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.stop);

            case Command.TIMER_SEC :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.timer_sec);

            case Command.TIMER_MILLIS :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.timer_millis);

            case Command.JUMP1_UP :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump1_up);

            case Command.JUMP1_DOWN :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump1_down);

            case Command.JUMP2_UP :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump2_up);

            case Command.JUMP2_DOWN :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump2_down);

            case Command.JUMP3_UP :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump3_up);

            case Command.JUMP3_DOWN :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump3_down);

            case Command.JUMP4_UP :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump4_up);

            case Command.JUMP4_DOWN :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump4_down);

            case Command.START :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.start);

            case Command.END :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.end);

            default:
                return null;
        }
    }

    private Bitmap chooseCheckedIcon(int command) {
        switch(command){
            case Command.MOTOR3 :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.motor3_on);

            case Command.MOTOR5 :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.motor5_on);

            case Command.MOTOR6 :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.motor6_on);

            case Command.MOTOR9 :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.motor9_on);

            case Command.MOTOR10 :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.motor10_on);

            case Command.MOTOR11 :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.motor11_on);

            case Command.ALLMOTOR :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.motorall_on);

            case Command.STOP :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.stop_on);

            case Command.TIMER_SEC :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.timer_sec_on);

            case Command.TIMER_MILLIS :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.timer_millis_on);

            case Command.JUMP1_UP :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump1_up_on);

            case Command.JUMP1_DOWN :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump1_down_on);

            case Command.JUMP2_UP :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump2_up_on);

            case Command.JUMP2_DOWN :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump2_down_on);

            case Command.JUMP3_UP :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump3_up_on);

            case Command.JUMP3_DOWN :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump3_down_on);

            case Command.JUMP4_UP :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump4_up_on);

            case Command.JUMP4_DOWN :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jump4_down);

            case Command.START :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.start_on);

            case Command.END :
                return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.end_on);

            default:
                return null;
        }
    }
}
