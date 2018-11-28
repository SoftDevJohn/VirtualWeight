package com.costigan.virtualweight;

import org.joda.time.LocalDate;
import org.junit.Test;

import static org.junit.Assert.*;

public class MfpScreenScraperTest {

    @Test
    public void screenScrape() {
        MfpScreenScraper mpfSc = new MfpScreenScraper();
        //mpfSc.screenScrape( statusTextView );
        mpfSc.screenScrape();

        int calories = mpfSc.calories;

        assertEquals(1234,calories);
    }


    //@Test
    public void getTodaysCalories() {
        MfpScreenScraper mpfSc = new MfpScreenScraper();
        TodaysCalories tc = mpfSc.getCaloriesForToday();
        assertEquals("2018-11-28", tc.getDateAsMfpString());
    }

    @Test
    public void getCaloriesForDate() {
        String octDate = "2018-10-29";
        LocalDate date = org.joda.time.LocalDate.parse(octDate, TodaysCalories.DATE_FORMATTER);
        MfpScreenScraper mpfSc = new MfpScreenScraper();
        TodaysCalories tc = mpfSc.getCaloriesForDate(date);
        assertEquals(octDate, tc.getDateAsMfpString());
        assertEquals(1534, tc.getCaloriesIn());
        //assertEquals(702, tc.getCaloriesOut());

    }



}