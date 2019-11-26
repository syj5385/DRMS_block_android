package jjun.jjunapp.programdrs.Tutorial;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import jjun.jjunapp.programdrs.Activity.MainActivity;
import jjun.jjunapp.programdrs.CustomAdapter.CustomAdapter1.Custom1_Item;
import jjun.jjunapp.programdrs.CustomAdapter.CustomAdapter1.CustomAdapter1;
import jjun.jjunapp.programdrs.R;
import jjun.jjunapp.programdrs.Sound.SoundManager;
import jjun.jjunapp.programdrs.Tutorial.TutorialView.IntroView;

/**
 * Created by jjunj on 2017-09-26.
 */

public class Tutorial {
    public static final int T0 = 0;
    public static final int T1 = 1;
    public static final int T2 = 2;
    public static final int T3 = 3;
    public static final int T4 = 4;

    public static final int TUTORIAL = 10;
    public static final int TUTORIALCOMMANDVIEW = 11;
    public static final int TUTORIAL_START_OVER = 100;


    private Context context;
    private Activity mActivity;
    private LinearLayout left_menu_layout, command_window, appmenu;
    private TextView command_text;
    private Handler mHandler;
    private LayoutInflater inflater;
    private SoundManager mSoundManager;

    private IntroView mIntroView;

    private Handler TutorialHandler = new MyHandler();

    private int[] iconX_position = new int[6]; // arrayOf Icon_X position
    private int[] iconY_position = new int[5]; // arrayOf Icon_Y position
    private int[] sizeofCommandView = new int[2];

    public Tutorial(Context context, Activity activity, Handler mHandler) {
        this.context = context;
        this.mActivity = activity;
        this.mHandler = mHandler;

        this.left_menu_layout = activity.findViewById(R.id.icon_box);
        this.command_window = activity.findViewById(R.id.command_window);
        this.appmenu = activity.findViewById(R.id.appmenu);



        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSoundManager = new SoundManager(context);

        implementationTutorialListView();
    }

    private void implementationTutorialListView(){
//        mHandler.obtainMessage(MainActivity.REMOVE_ALL_LAYOUT).sendToTarget();

        inflater.inflate(R.layout.appmenu_layout,left_menu_layout,true);
        TextView tutorial_topic = (TextView)mActivity.findViewById(R.id.list_topic);
        tutorial_topic.setText("Tutorial");
        ListView tutorialList = (ListView)mActivity.findViewById(R.id.file_list);
        CustomAdapter1 mAdapter = new CustomAdapter1(context);
        mAdapter.addItem(new Custom1_Item(context.getResources().getDrawable(R.mipmap.tutorial),"APP\n둘러보기"));
        mAdapter.addItem(new Custom1_Item(context.getResources().getDrawable(R.mipmap.tutorial),"명령어\n화면 기능"));
        mAdapter.addItem(new Custom1_Item(context.getResources().getDrawable(R.mipmap.tutorial),"tutorial2"));
        mAdapter.addItem(new Custom1_Item(context.getResources().getDrawable(R.mipmap.tutorial),"tutorial3"));
        mAdapter.addItem(new Custom1_Item(context.getResources().getDrawable(R.mipmap.tutorial),"tutorial4"));
        mAdapter.addItem(new Custom1_Item(context.getResources().getDrawable(R.mipmap.tutorial),"tutorial5"));
        mAdapter.addItem(new Custom1_Item(context.getResources().getDrawable(R.mipmap.tutorial),"tutorial6"));
        mAdapter.addItem(new Custom1_Item(context.getResources().getDrawable(R.mipmap.tutorial),"tutorial7"));

        tutorialList.setAdapter(mAdapter);

        tutorialList.setOnItemClickListener(tutorialItemClickListener);

        display_IntroView();

    }

    private AdapterView.OnItemClickListener tutorialItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent ;
            switch(i){
                case 0 :
                    mSoundManager.play(0);
                    mIntroView.stopThread();
                    intent = new Intent(context,TutorialCommandActivity.class);
                    intent.putExtra("TUTORIAL",0);
                    mActivity.startActivityForResult(intent,MainActivity.REQUEST_TUTORIAL_ACTIVITY);
                    mActivity.overridePendingTransition(R.anim.fade,R.anim.hold);
                    command_window.removeAllViews();

                    break;

                case 1 :
                    mSoundManager.play(0);
                    mIntroView.stopThread();
                    intent = new Intent(context,TutorialCommandActivity.class);
                    intent.putExtra("TUTORIAL",1);
                    mActivity.startActivityForResult(intent,MainActivity.REQUEST_TUTORIAL_ACTIVITY);
                    mActivity.overridePendingTransition(R.anim.fade,R.anim.hold);
                    command_window.removeAllViews();

                    break;
            }

        }
    };

    public void display_IntroView(){
        mIntroView = new IntroView(context);
        mIntroView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                mActivity.getWindow().getWindowManager().getDefaultDisplay().getHeight() - appmenu.getHeight()));

        command_window.addView(mIntroView);
        float temp = command_window.getWidth();

        int[] size = {command_window.getWidth(),mActivity.getWindow().getWindowManager().getDefaultDisplay().getHeight() - appmenu.getHeight()};
        mIntroView.setSize(size);
    }




    private class MyHandler extends Handler{
        public MyHandler() {
            super();
        }

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case TUTORIAL :

                    break;

                case TUTORIALCOMMANDVIEW :

                    break;



            }
        }
    }

    public Handler getTutorialHandler(){
        return TutorialHandler;
    }

    private void implementationTutorial(int tutorialNum){


    }

    public void stopTutorial(){
        mIntroView.stopThread();
    }





}
