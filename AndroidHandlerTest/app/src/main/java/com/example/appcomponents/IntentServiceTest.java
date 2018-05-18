package com.example.appcomponents;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by wupeng on 18-4-24.
 */

public class IntentServiceTest extends IntentService {
    private static final String TAG = "AppComponent-IntentService";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    public IntentServiceTest() {
        super("IntentServiceTest");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, DebugFunction.getFileLineMethod());
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (Constant.ACTION_EXCEPTION_HAPPEN.equals(action)) {
                Log.i(TAG, DebugFunction.getFileLineMethod());
            }
        }
    }
}
