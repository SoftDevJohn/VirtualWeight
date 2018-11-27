package com.costigan.virtualweight;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class VwFileManagerTest {
    private static VwSettings settings = new VwSettings();

    /**
     * Write configuration information the file
     *
     * @throws Exception
     */
    @BeforeClass
    public static void writeFile() throws Exception{
        Context ctx = InstrumentationRegistry.getTargetContext();
        VwFileManager fm = new VwFileManager();

        settings.setUserName("myusername");
        settings.setPassword("mypassword");
        settings.setBmr(2690);
        settings.setTargetWeight(79.4);

        //String mfpUserName = "myusername";
        //String mfpPassword = "mypassword";
        //String bmr = "2690";
        //String targetWeight = "79.4";
        //String ConfigurationLine = mfpUserName+","+mfpPassword+","+bmr+","+targetWeight;
        String ConfigurationLine = settings.toString();


        fm.writeFile(ctx, VwFileManager.SETTINGS_FILE, ConfigurationLine);

    }

    /**
     * Read configuration informatin from file
     * @throws Exception
     */
    @Test
    public void readFile() throws Exception {
        Context ctx = InstrumentationRegistry.getTargetContext();
        VwFileManager fm = new VwFileManager();

        //Now read from this file
        StringBuffer stringBuffer = new StringBuffer();
        fm.readFile(ctx, VwFileManager.SETTINGS_FILE, stringBuffer);

        assertEquals(settings.toString(), stringBuffer.toString().trim());

    }


}
