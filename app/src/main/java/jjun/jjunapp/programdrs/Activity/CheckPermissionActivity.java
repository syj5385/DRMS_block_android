package jjun.jjunapp.programdrs.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import jjun.jjunapp.programdrs.R;


/**
 * Created by jjunj on 2017-09-18.
 */

public class CheckPermissionActivity extends AppCompatActivity {
    private boolean running = false;

    private static final int ALLPERMISSION_GRANGTED = 0;

    private static final int REQUEST_PERMISSION = 0;
    private static final int PERMISSION_RESULT_OK = 1;

    private Button checkPermission;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_checkpermission);

        getSupportActionBar().hide();
//        getActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        checkPermission = (Button)findViewById(R.id.permission_check);
        checkPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                running = true;

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        while(running){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                int permissionResult = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
                                int permissionResult2 = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
                                int permissionResult3 = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

                                if (permissionResult == PackageManager.PERMISSION_GRANTED
                                        && permissionResult2 == PackageManager.PERMISSION_GRANTED
                                        && permissionResult3 == PackageManager.PERMISSION_GRANTED) {

                                    mHandler.obtainMessage(ALLPERMISSION_GRANGTED, -1,-1).sendToTarget();;
                                    running = false;
                                }

                            }
                        }
                    }
                }).start();


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int permissionResult = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
                    int permissionResult2 = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
                    int permissionResult3 = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    if (permissionResult == PackageManager.PERMISSION_DENIED || permissionResult2 == PackageManager.PERMISSION_DENIED
                            || permissionResult3 == PackageManager.PERMISSION_DENIED) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) || shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
                                || shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(CheckPermissionActivity.this);
                            dialog.setTitle("권한이 필요합니다.").setMessage("이 기능을 사용하기 위해서는 단말기의 권한이 필요합니다. 계속 하시겠습니까")
                                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                                        Manifest.permission.ACCESS_FINE_LOCATION}, 1000);


                                            }
                                        }
                                    }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(CheckPermissionActivity.this, "기능을 취소했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }).create().show();

                        } else {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},1000);
                        }
                    }
                    else{

                    }
                }
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();

        running = false;
    }

    public static final String REQUEST_TUTORIAL = "requset tutorial";
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

           if(msg.what == ALLPERMISSION_GRANGTED){
//               setResult(PERMISSION_RESULT_OK);

               Intent intent = new Intent(CheckPermissionActivity.this,MainActivity.class);
               intent.putExtra(REQUEST_TUTORIAL,1);
               startActivity(intent);
               overridePendingTransition(R.anim.fade, R.anim.hold);

               try{
                   Thread.sleep(100);
               }catch (InterruptedException e){};

               finish();

           }
        }
    };


}
