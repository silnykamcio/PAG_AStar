package com.geoida.progeo.Utils;

import android.util.Log;

/**
 * Created by Kamil on 23.07.2017.
 * Class for easy time measurements in optimalization process
 */

public final class Stoper {
    private Stoper() {}
    private static long time;
    private static String func;
    public static void start(String Func){
        func = Func;
        Log.wtf(func,"Time measurement started");
        time = System.currentTimeMillis();
    }
    public static void stop(){
        Log.wtf(func,"Process finished. Time: " + (System.currentTimeMillis()-time));
    }
}
