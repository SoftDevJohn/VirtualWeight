package com.costigan.virtualweight;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.joda.time.LocalDate;
import org.junit.Test;

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

        String endDateStr = "2018-11-30";
        LocalDate endDate = org.joda.time.LocalDate.parse(endDateStr, Calorie.DATE_FORMATTER);
        MfpScreenScraper mpfSc = new MfpScreenScraper();

        TotalCalories total = mpfSc.getTotalCaloriesDateToToday(vws.getDayAfterStartDate());

        assertEquals(153499, total.getNetCalories(2625));

//        assertEquals(153499, total.getNetWeight(vws.getBmr(),vws.getStartWeight()),0.001);





    }
}