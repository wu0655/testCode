package com.example.appcomponents;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class DebugFunction {

    public static String getFileLineMethod() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        StringBuffer toStringBuffer = new StringBuffer(" [")
                .append(traceElement.getFileName()).append(" | Line:")
                .append(traceElement.getLineNumber()).append(" | Func:")
                .append(traceElement.getMethodName()).append(" | ")
                .append("Pid:").append(getPid()).append(" | ")
                .append("Tid:").append(getTid())
                .append("]");
        return toStringBuffer.toString();
    }

    public static long getPid() {
        //return Thread.currentThread().getId();
        return android.os.Process.myPid();
    }

    public static int getTid() {
        return android.os.Process.myTid();
    }

    public static String _FILE_() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        return traceElement.getFileName();
    }


    public static String _FUNC_() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        return traceElement.getMethodName();
    }

    public static int _LINE_() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        return traceElement.getLineNumber();
    }

    public static String _TIME_() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(now);
    }

}