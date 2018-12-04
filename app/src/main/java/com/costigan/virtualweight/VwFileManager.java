package com.costigan.virtualweight;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * Manages file I/O.
 */
public class VwFileManager {

    public static final String SETTINGS_FILE = "VwSettings.txt";

    public void writeSettings(Context ctx, String fileName, VwSettings settings) throws Exception {
        FileOutputStream fos = ctx.openFileOutput(fileName, Context.MODE_PRIVATE);
        fos.write(settings.toWriteLine().getBytes());
        fos.close();
    }

    public void writeFile(Context ctx, String fileName, VwSettings settings) throws Exception {
        String ConfigurationLine = settings.toWriteLine();
        writeFile(ctx, fileName, ConfigurationLine);

    }

    @Deprecated
    //TODO make private
    public void writeFile(Context ctx, String fileName, String line) throws Exception {
        FileOutputStream fos = ctx.openFileOutput(fileName, Context.MODE_PRIVATE);
        fos.write(line.getBytes());
        fos.close();
    }

    public String readFile(Context ctx, String fileName, StringBuffer stringBuffer) throws Exception {

        //Now read from this file
        BufferedReader inputReader = new BufferedReader(
                new InputStreamReader(
                        ctx.openFileInput(fileName)));
        String inputString;
        while ((inputString = inputReader.readLine()) != null) {
            stringBuffer.append(inputString + "\n");
        }
        return inputString;
    }


    public VwSettings getSettingsFromFile(Context ctx, String fileName,VwSettings vws) throws Exception {

        VwFileManager fm = new VwFileManager();

        //Now read from this file
        StringBuffer stringBuffer = new StringBuffer();
        fm.readFile(ctx, VwFileManager.SETTINGS_FILE, stringBuffer);

        vws.parseLine(vws,stringBuffer.toString().trim());

        return vws;

    }



}
