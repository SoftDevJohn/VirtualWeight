package com.costigan.virtualweight;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * I have created twoo version of this class, this one which runs locally, and another
 * which runs on the android system in instrumentation mode.
 *
 */
public class VwSettingsTest {
    String mfpUserName = "mfpuseranme";
    String mfpPassword = "mfppw";
    String bmr = "2690";
    String targetWeight = "79.4";
    String ConfigurationLine = mfpUserName + "," + mfpPassword + "," + bmr + "," + targetWeight;

    @Test
    public void valueOf() throws Exception {
        String line = "a,b,1,2.7";
        VwSettings settings = VwSettings.valueOf(line);
        assertEquals(line,settings.toString());
    }

    @Test
    public void stringConstructor() throws Exception {
        String line = "a,b,1,2.7";
        VwSettings settings = new VwSettings(line);
        assertEquals(line,settings.toString());
    }

}