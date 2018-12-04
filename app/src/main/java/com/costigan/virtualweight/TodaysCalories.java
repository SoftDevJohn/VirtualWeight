package com.costigan.virtualweight;

//These replace the old ones

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TodaysCalories {
    public final static DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");
    public static final String MFP_DATE_FORMAT = "yyyy-MM-dd";



    private LocalDate date = null;
    private int caloriesIn = 0;
    private int caloriesOut = 0;


    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDate(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        this.date = org.joda.time.LocalDate.parse(stringDate, formatter);
    }


    public LocalDate getDate() {
        return date;
    }

    public String getDateAsMfpString() {
        return date.toString(MFP_DATE_FORMAT);
    }



    public int getCaloriesIn() {
        return caloriesIn;
    }

    public int getNetCalories() {
        return caloriesIn-caloriesOut;
    }

    public double getNetWeight() {
        return getNetCalories()/7700;
    }

    public void setCaloriesIn(int caloriesIn) {
        this.caloriesIn = caloriesIn;
    }

    public int getCaloriesOut() {
        return caloriesOut;
    }

    public void setCaloriesOut(int caloriesOut) {
        this.caloriesOut = caloriesOut;
    }

    @Override
    public String toString() {
        return "TodaysCalories{" +
                "date=" + date +
                ", caloriesIn=" + caloriesIn +
                ", caloriesOut=" + caloriesOut +
                '}';
    }
}
