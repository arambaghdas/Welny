package com.massage.welny.log;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class FileUtils {

    private static final String FILE_NAME = "request_logs.txt";
    private static final int READ_BLOCK_SIZE = 100;

    public static void writeFileOnInternalStorage(Context mContext, String text){
        /*
        File file = new File(mContext.getFilesDir(), "text");
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            File gpxfile = new File(file, "sample");
            FileWriter writer = new FileWriter(gpxfile, true);
            writer.append(text);
            writer.append("\n");
            writer.flush();
            writer.close();
        } catch (Exception e) { }
        */
    }

    public static ArrayList<String> readFromInternalStorage(Context mContext) {
        ArrayList<String> stringArrayList = new ArrayList<>();

        File fileEvents = new File(mContext.getFilesDir()+"/text/sample");
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileEvents));
            String line;
            while ((line = br.readLine()) != null) {
                Log.v("readFromInternalStorage", "line: " + line);
                if (!line.equals("\n")) {
                    stringArrayList.add(line);
                }
            }
            br.close();
        } catch (IOException e) {

        }

        ArrayList<String> stringArrayList1 = new ArrayList<>(stringArrayList);
        Collections.reverse(stringArrayList1);

        return stringArrayList1;
    }

}
