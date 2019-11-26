package jjun.jjunapp.programdrs.Command;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import jjun.jjunapp.programdrs.Activity.MainActivity;
import jjun.jjunapp.programdrs.CustomAdapter.CustomAdapter1.Custom1_Item;
import jjun.jjunapp.programdrs.CustomAdapter.CustomAdapter1.CustomAdapter1;
import jjun.jjunapp.programdrs.CustomAdapter.CustomAdatper2.Custom2_Item;
import jjun.jjunapp.programdrs.CustomAdapter.CustomAdatper2.CustomAdapter2;
import jjun.jjunapp.programdrs.R;

/**
 * Created by jjunj on 2017-10-23.
 */

public class CommandCheck  {

    private static final String TAG = "CommandCheck";
    private static final int COMMAND_CHECK = 11;


    private Activity mActivity;
    private Handler mHandler;
    private ArrayList<CommandIcon> command_list;
    private ArrayList<ErrorContent> errorlist = new ArrayList<ErrorContent>();

    private boolean[] checklist = new boolean[10];


    public CommandCheck(Activity mActivity,ArrayList<CommandIcon> command_list,Handler mHandler) {
        this.mActivity = mActivity;
        this.command_list = command_list;
        this.mHandler = mHandler;
        for(int i=0; i<checklist.length;i++){
            checklist[i] = false;
        }

    }

    public void startCheckCommand(){
        boolean checkresult = true;
        Intent commandcheckIntent = new Intent(mActivity, CommandCheckActivity.class);
        mActivity.startActivityForResult(commandcheckIntent, MainActivity.REQUEST_UPLOAD_ACTIVITY);

        checklist[0] = checkStartEndCommand();
        checklist[1] = checkLengthOfCommand();
        checklist[2] = checkDuplicationStartCommand();
        checklist[3] = checkDuplicationEndCommand();
        checklist[4] = checkLocationStartCommand();
        checklist[5] = checkLocationEndCommand();
        checklist[6] = checkTimerValue();
        checklist[7] = checkJumpValue();
        checklist[8] = checkJumpLocation();


        if(!checklist[0] || !checklist[1] || !checklist[2] || !checklist[3] || !checklist[4] || !checklist[5] || !checklist[6] || !checklist[7] || !checklist[8])
            checkresult = false;

        Log.d(TAG,"check Result : " + checkresult + " / " + errorlist.size() + " error");
        for(int i=0 ; i<checklist.length; i++){
            Log.w(TAG,"checklist["+i+"] = " + checklist[i]);
        }


        try{
            Thread.sleep(2000);
        }catch (InterruptedException e){};
        Log.d(TAG,"sendBroadCast");
        Intent intent = new Intent();
        intent.putExtra("Check",errorlist.size());
        intent.setAction(CommandCheckActivity.REQUEST_FINISH_ACTIVITY);
        mActivity.sendBroadcast(intent);
    }

    private boolean checkStartEndCommand(){
        boolean success = true;
        int startCount = 0;
        int endCount = 0;

        for(int i=0; i<command_list.size(); i++) {
            if (command_list.get(i).getCommand() == Command.START)   // check the number of startCommand
                startCount++;
        }

        for(int i=0; i<command_list.size(); i++) {
            if (command_list.get(i).getCommand() == Command.END)   // check the number of startCommand
                endCount++;
        }

        if(startCount == 0) {
            success = false;
            errorlist.add(new ErrorContent(ErrorContent.BasicCommand, -1));
        }
        if(endCount == 0){
            success = false;
            errorlist.add(new ErrorContent(ErrorContent.BasicCommand,-1));
        }

        if(startCount > 0 && endCount > 0){
            success = true;
        }

        return success;
    }

    private boolean checkLengthOfCommand(){
        boolean success ;
        if(command_list.size() < 1){
            success = false;
            errorlist.add(new ErrorContent(ErrorContent.Command_size,-1));
        }
        else{
            success = true;
        }

        return success;
    }

    private boolean checkDuplicationStartCommand(){ // checklist[0]
        boolean success;
        int startCount = 0;

        for(int i=0; i<command_list.size(); i++) {
            if (command_list.get(i).getCommand() == Command.START) {  // check the number of startCommand
                startCount++;
                if(startCount > 1){
                    errorlist.add(new ErrorContent(ErrorContent.START_duplicate, i));
                }
            }
        }

        if(startCount > 1) {
            success = false;
        }
        else
            success = true;

        return success;
    }

    private boolean checkDuplicationEndCommand(){   // checklist[1]
        boolean success;
        int endCount = 0;

        for(int i=0; i<command_list.size(); i++) {
            if (command_list.get(i).getCommand() == Command.END) {    // check the number of endCommand
                endCount++;
                if(endCount > 1)
                    errorlist.add(new ErrorContent(ErrorContent.END_duplicate, i));
            }
        }
        if(endCount > 1) {
            success = false;

        }
        else
            success = true;

        return success;
    }

    private boolean checkLocationStartCommand(){
        boolean success;
        if(command_list.get(0).getCommand() == Command.START){
            success = true;
        }
        else{
            success = false;
            for(int i=1; i<command_list.size(); i++){
                if(command_list.get(i).getCommand() == Command.START){
                    errorlist.add(new ErrorContent(ErrorContent.START_location,i));
                }

            }
        }
        return success;
    }

    private boolean checkLocationEndCommand(){
        boolean success;
        if(command_list.get(command_list.size()-1).getCommand() == Command.END){
            success = true;
        }
        else{
            success = false;
            for(int i=0; i<command_list.size()-1; i++){
                if(command_list.get(i).getCommand() == Command.END){
                    errorlist.add(new ErrorContent(ErrorContent.END_location,i));
                }

            }
        }
        return success;
    }

    private boolean checkTimerValue(){
        boolean success = true;
        for(int i=0; i<command_list.size(); i++){
            if(command_list.get(i).getCommand() == Command.TIMER_SEC || command_list.get(i).getCommand() == Command.TIMER_MILLIS){
                if (command_list.get(i).getValue() == 0) {
                    errorlist.add(new ErrorContent(ErrorContent.Timer_value,i));
                    if(success == true)
                        success = false;
                }
            }
        }
        return success;
    }

    private boolean checkJumpValue(){
        boolean success = true;
        for(int i=0; i<command_list.size(); i++){
            if(command_list.get(i).getCommand() == Command.JUMP1_UP || command_list.get(i).getCommand() == Command.JUMP2_UP
                    || command_list.get(i).getCommand() == Command.JUMP3_UP || command_list.get(i).getCommand() == Command.JUMP4_UP){
                int value = command_list.get(i).getValue();
                if(value == 0){
                    errorlist.add(new ErrorContent(ErrorContent.Jump_value,i));
                    if(success)
                        success = false;
                }
            }
        }

        return success;
    }

    private boolean checkJumpLocation(){
        boolean success = true;
        for(int i=0; i<command_list.size(); i++){
            if(command_list.get(i).getCommand() == Command.JUMP1_DOWN){
                for(int j=i; j<command_list.size() ; j++){
                    if(command_list.get(j).getCommand() == Command.JUMP1_UP){
                        break;
                    }
                    if(j == command_list.size()-1 && command_list.get(j).getCommand() != Command.JUMP1_UP){
                        errorlist.add(new ErrorContent(ErrorContent.Jump_location,i));
                        if(success)
                            success = false;

                    }
                }
            }
        }

        int jump1_upCount = 0;
        int jump1_downCount = 0;
        for(int i=0; i<command_list.size(); i++){
            int command = command_list.get(i).getCommand();
            if(command == Command.JUMP1_UP){
                jump1_upCount ++;
            }
            if(command == Command.JUMP1_DOWN){
                jump1_downCount++;
            }
        }
        if(jump1_downCount != jump1_upCount){
            if(success)
                success = false;

            errorlist.add(new ErrorContent(ErrorContent.Jump_set,-1));
        }

        for(int i=0; i<command_list.size(); i++){
            if(command_list.get(i).getCommand() == Command.JUMP2_DOWN){
                for(int j=i; j<command_list.size() ; j++){
                    if(command_list.get(j).getCommand() == Command.JUMP2_UP){
                        break;
                    }
                    if(j == command_list.size()-1 && command_list.get(j).getCommand() != Command.JUMP2_UP){
                        errorlist.add(new ErrorContent(ErrorContent.Jump_location,i));
                        if(success)
                            success = false;

                    }
                }
            }
        }

        int jump2_upCount = 0;
        int jump2_downCount = 0;
        for(int i=0; i<command_list.size(); i++){
            int command = command_list.get(i).getCommand();
            if(command == Command.JUMP2_UP){
                jump2_upCount ++;
            }
            if(command == Command.JUMP2_DOWN){
                jump2_downCount++;
            }
        }
        if(jump2_downCount != jump2_upCount){
            if(success)
                success = false;

            errorlist.add(new ErrorContent(ErrorContent.Jump_set,-1));
        }



        for(int i=0; i<command_list.size(); i++){
            if(command_list.get(i).getCommand() == Command.JUMP3_DOWN){
                for(int j=i; j<command_list.size() ; j++){
                    if(command_list.get(j).getCommand() == Command.JUMP3_UP){
                        break;
                    }
                    if(j == command_list.size()-1 && command_list.get(j).getCommand() != Command.JUMP3_UP){
                        errorlist.add(new ErrorContent(ErrorContent.Jump_location,i));
                        if(success)
                            success = false;

                    }
                }
            }
        }

        int jump3_upCount = 0;
        int jump3_downCount = 0;
        for(int i=0; i<command_list.size(); i++){
            int command = command_list.get(i).getCommand();
            if(command == Command.JUMP3_UP){
                jump3_upCount ++;
            }
            if(command == Command.JUMP3_DOWN){
                jump3_downCount++;
            }
        }
        if(jump3_downCount != jump3_upCount){
            if(success)
                success = false;

            errorlist.add(new ErrorContent(ErrorContent.Jump_set,-1));
        }


        for(int i=0; i<command_list.size(); i++){
            if(command_list.get(i).getCommand() == Command.JUMP4_DOWN){
                for(int j=i; j<command_list.size() ; j++){
                    if(command_list.get(j).getCommand() == Command.JUMP4_UP){
                        break;
                    }
                    if(j == command_list.size()-1 && command_list.get(j).getCommand() != Command.JUMP4_UP){
                        errorlist.add(new ErrorContent(ErrorContent.Jump_location,i));
                        if(success)
                            success = false;

                    }
                }
            }
        }

        int jump4_upCount = 0;
        int jump4_downCount = 0;
        for(int i=0; i<command_list.size(); i++){
            int command = command_list.get(i).getCommand();
            if(command == Command.JUMP4_UP){
                jump4_upCount ++;
            }
            if(command == Command.JUMP4_DOWN){
                jump4_downCount++;
            }
        }
        if(jump4_downCount != jump4_upCount){
            if(success)
                success = false;

            errorlist.add(new ErrorContent(ErrorContent.Jump_set,-1));
        }
        return success;
    }

    public void implementationErrorBox(){

        LinearLayout left_layout = (LinearLayout)mActivity.findViewById(R.id.icon_box);
        LayoutInflater inflater = (LayoutInflater)mActivity.getLayoutInflater();
        inflater.inflate(R.layout.selected_item,left_layout,true);

        final ListView error_list = (ListView)mActivity.findViewById(R.id.motor_box);
        final CustomAdapter2 mAdapter = new CustomAdapter2(mActivity);
        final ImageView box_back = (ImageView)mActivity.findViewById(R.id.motor_back);
        box_back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    box_back.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.box_back_on));
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    try{
                        Thread.sleep(20);
                    }catch (InterruptedException e){}
                    box_back.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.box_back));
                    mHandler.obtainMessage(COMMAND_CHECK).sendToTarget();
                }
                return true;
            }
        });

        for(int i=0; i<errorlist.size();i++) {
            if(errorlist.get(i).getIndex() != -1) {
                mAdapter.addItem(new Custom2_Item(new BitmapDrawable(command_list.get(errorlist.get(i).getIndex()).getUnchecked_icon()),
                        "index : "+errorlist.get(i).getIndex() , "\n code : " + errorlist.get(i).getErrorcode()));
            }
        }

        for(int i=0; i<errorlist.size();i++) {
            if(errorlist.get(i).getIndex() == -1){
                mAdapter.addItem(new Custom2_Item(mActivity.getResources().getDrawable(R.mipmap.error),
                        "스케치 오류" , "\n code : " + errorlist.get(i).getErrorcode()));
            }
        }

        error_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String temp = ((Custom2_Item) mAdapter.getItem(i)).getData()[1];
                if(temp.length() > 0) {
                    int errorCode = Integer.parseInt(temp.substring(temp.length()-1));
                    ErrorContent tempContent = new ErrorContent(0,0);
                    String errorContent = tempContent.getContent(errorCode);
                    Log.d(TAG,"ErrorContent " + errorCode);
                    Toast.makeText(mActivity,   errorContent, Toast.LENGTH_SHORT).show();

                }

            }
        });
        error_list.setAdapter(mAdapter);

    }

    public ArrayList<ErrorContent> getErrorlist(){return errorlist;}
}
