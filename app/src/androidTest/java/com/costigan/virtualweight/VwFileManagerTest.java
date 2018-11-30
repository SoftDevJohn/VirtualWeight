package com.costigan.virtualweight;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.joda.time.LocalDate;
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

        LocalDate startDate = new LocalDate(2018,10,25);

        settings.setUserName("myusername");
        settings.setPassword("mypassword");
        settings.setBmr(2690);
        settings.setStartDate(startDate);
        settings.setStartWeight(79.4);
        settings.setTargetWeight(79.4);

        fm.writeFile(ctx, VwFileManager.SETTINGS_FILE, settings);

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
        VwSettings vws = new VwSettings(stringBuffer.toString().trim());
        assertEquals(settings, vws);

    }


}
