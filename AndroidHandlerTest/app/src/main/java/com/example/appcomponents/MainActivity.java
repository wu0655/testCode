package com.example.appcomponents;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "AppComponent-AppCompatActivity";

    private Button b1,b2,b3;
    private Handler hHandle;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();

        b1 = (Button)findViewById(R.id.red);
        b2 = (Button)findViewById(R.id.green);
        b3 = (Button)findViewById(R.id.blue);

        hHandle = HandlerThreadTest.getHandler();

        Intent serviceIntent = new Intent(mContext, IntentServiceTest.class);
        serviceIntent.setAction(Constant.ACTION_EXCEPTION_HAPPEN);
        startService(serviceIntent);

        Log.i(TAG, DebugFunction.getFileLineMethod());

        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (hHandle != null) {
                    Log.i(TAG, DebugFunction.getFileLineMethod());
                    hHandle.post(mRunnable);
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Message msg = hHandle.obtainMessage(Constant.TYPE_START);
                Log.i(TAG, DebugFunction.getFileLineMethod());
                hHandle.sendMessage(msg);
            }
        });

        /*
        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tv.setBackgroundColor(Color.BLUE);
                tv1.setBackgroundColor(Color.BLUE);
            }
        });
        */
    }

    private Runnable mRunnable = new Runnable() {
        public void run() {
            Log.i(TAG, DebugFunction.getFileLineMethod());
        }
    };
}
