package com.kevin.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by 532259 on 1/26/2016.
 */

//Debug logger for the whole program
public class DebugLogger {

    final static String LOG_PATH = "checkers3d.log";

    private static BufferedWriter debugStream;
    static {
        try {
            debugStream = new BufferedWriter(new FileWriter(LOG_PATH));
        } catch (IOException e) {
            //it will be fine
        }
    }

    public static void log(String log) {
        try {
            debugStream.write(log);
            debugStream.newLine();
            debugStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
