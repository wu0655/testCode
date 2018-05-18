package com.example.appcomponents;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by wupeng on 18-4-24.
 */

public class HandlerThreadTest extends Service {
    private static final String TAG = "AppComponent-HandlerThreadTest";

    static public HandlerThread mHandlerThread;
    static public Handler mHandler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // TODO: It would be nice to have an option to hold a partial wakelock
        // during processing, and to have a static startService(Context, Intent)
        // method that would launch the service & hand off a wakelock.
        Log.i(TAG, DebugFunction.getFileLineMethod());
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, DebugFunction.getFileLineMethod());
        return START_NOT_STICKY;
    }

    public static Handler getHandler() {
        if (mHandler == null) {
            mHandlerThread = new HandlerThread(TAG);
            mHandlerThread.start();

            mHandler = new Handler(mHandlerThread.getLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    Log.i(TAG, DebugFunction.getFileLineMethod());
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case Constant.TYPE_START:
                            break;
                        default:
                            break;
                    }

                }
            };
        }
        return mHandler;
    }
}
