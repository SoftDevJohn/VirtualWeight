package com.costigan.virtualweight;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class MfpScreenScraperTest {

    @Test
    public void getCaloriesForToday() {
    }


    //****Ths will be incoperatted into the view eventually*****
    @Test
    public void getCaloriesForGivenDate() throws Exception{
        Context ctx = InstrumentationRegistry.getTargetContext();
        VwFileManager fm = new VwFileManager();

        //Now read from this file
        StringBuffer stringBuffer = new StringBuffer();
        fm.readFile(ctx, VwFileManager.SETTINGS_FILE, stringBuffer);
        VwSettings vws = new VwSettings(stringBuffer.toString().trim());

        //returns the 26-oct
        LocalDate dayAfterStartDate = vws.getDayAfterStartDate();

        String octDate = "2018-10-29";
        LocalDate date = org.joda.time.LocalDate.parse(octDate, TodaysCalories.DATE_FORMATTER);
        MfpScreenScraper mpfSc = new MfpScreenScraper();
        //TodaysCalories tc = mpfSc.getCaloriesForDate(date);
        //assertEquals(153499, tc.getCaloriesIn());

        //List<TodaysCalories> tc = mpfSc.getCaloriesForDates(date,date);
        //todo:
        //1) Get calories between the given dates inclusive
        //2) Scan list for latest date before today and save that back into the configuraion fule





        assertEquals(153499, "tc.getCaloriesIn()");



    }
}