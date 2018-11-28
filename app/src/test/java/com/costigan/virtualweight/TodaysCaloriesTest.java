package com.costigan.virtualweight;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import java.text.SimpleDateFormat;

import static org.junit.Assert.*;

public class TodaysCaloriesTest {


    @Test
    public void setDate() {
        String string = "2018-10-29";
        LocalDate date = org.joda.time.LocalDate.parse(string, TodaysCalories.DATE_FORMATTER);
        TodaysCalories tc = new TodaysCalories();
        tc.setDate(string);
        assertEquals("2018-10-29", tc.getDate().toString());
    }

    @Test
    public void testAll() {
        String string = "2018-10-29";
        LocalDate date = org.joda.time.LocalDate.parse(string, TodaysCalories.DATE_FORMATTER);
        TodaysCalories tc = new TodaysCalories();
        tc.setDate(string);
        assertEquals("2018-10-29", tc.getDate().toString());

        tc.setCaloriesIn(1534);
        tc.setCaloriesOut(702);

        assertEquals("2018-10-29", tc.getDate().toString());
        assertEquals(1534, tc.getCaloriesIn() );
        assertEquals(702, tc.getCaloriesOut() );
    }


}