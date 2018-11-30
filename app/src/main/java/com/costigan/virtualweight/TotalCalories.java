package com.costigan.virtualweight;

import org.joda.time.LocalDate;

import java.util.Calendar;

public class TotalCalories {

    int bmr = 0;
    //Historic calculation up to teh latest day at midnight before today
    private LocalDate latestDateMidnightBeforeToday = null;
    private int numberOfDaysBeforeToday = 0;
    private int historticCaloriesIn = 0;
    private int historicCaloriesOut = 0;

    //Todays calculation
    private LocalDate today = null;
    private int todayCaloriesIn = 0;
    private int todayCaloriesOut = 0;

    public LocalDate getLatestDateMidnightBeforeToday() {
        return latestDateMidnightBeforeToday;
    }

    public void setLatestDateMidnightBeforeToday(LocalDate latestDateMidnightBeforeToday) {
        this.latestDateMidnightBeforeToday = latestDateMidnightBeforeToday;
    }

    public int getNumberOfDaysBeforeToday() {
        return numberOfDaysBeforeToday;
    }

    public void setNumberOfDaysBeforeToday(int numberOfDaysBeforeToday) {
        this.numberOfDaysBeforeToday = numberOfDaysBeforeToday;
    }

    public int getHistorticCaloriesIn() {
        return historticCaloriesIn;
    }

    public void setHistorticCaloriesIn(int historticCaloriesIn) {
        this.historticCaloriesIn = historticCaloriesIn;
    }

    public int getHistoricCaloriesOut() {
        return historicCaloriesOut;
    }

    public void setHistoricCaloriesOut(int historicCaloriesOut) {
        this.historicCaloriesOut = historicCaloriesOut;
    }

    public LocalDate getToday() {
        return today;
    }

    public void setToday(LocalDate today) {
        this.today = today;
    }

    public int getTodayCaloriesIn() {
        return todayCaloriesIn;
    }

    public void setTodayCaloriesIn(int todayCaloriesIn) {
        this.todayCaloriesIn = todayCaloriesIn;
    }

    public int getTodayCaloriesOut() {
        return todayCaloriesOut;
    }

    public void setTodayCaloriesOut(int todayCaloriesOut) {
        this.todayCaloriesOut = todayCaloriesOut;
    }

    public int getTotalBmrToYesterday(int bmr){
        return bmr * getNumberOfDaysBeforeToday();
    }

    public int getBmrSinceMidnight(int bmr) {
        return (int)((double)bmr * getFractionOfDay());
    }

    private static double getFractionOfDay() {
        double d = getSecondsSinceMidnight() / (double)86400000;
        return getRoundedValue(d,3);
    }
    public static double getRoundedValue(double d, int places) {
        double scale = Math.pow(10, places);
        return Math.round(d * scale) / scale;
    }


    private static long getSecondsSinceMidnight() {
        Calendar now = Calendar.getInstance();
        Calendar midnight = Calendar.getInstance();

        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);
        midnight.set(Calendar.MILLISECOND, 0);

        return now.getTimeInMillis() - midnight.getTimeInMillis();
    }

    public int getTotalCaloriesIn() {
        return getHistorticCaloriesIn()+getTodayCaloriesIn();
    }

    public int getTotalCaloriesOut() {
        return getHistoricCaloriesOut()+getTodayCaloriesOut();
    }

    public int getTotalBmr(int bmr) {
        return getTotalBmrToYesterday(bmr) + getBmrSinceMidnight(bmr);
    }

    public int getNetCalories(int bmr) {
        return getTotalCaloriesIn() - getTotalCaloriesOut() - getTotalBmr(bmr);
    }

    public double getNetWeightChange(int bmr) {
        double netWeight = (double)getNetCalories(bmr) / (double)7700;
        //Round to two decomal places
        int intWeight = (int)(netWeight * 100);
        netWeight = (double)intWeight/(double)100;
        return netWeight;
    }

    public double getNetWeight(int bmr, double oldWeight) {
        return oldWeight + getNetWeightChange(bmr);
    }

    @Override
    public String toString() {
        return "TotalCalories{" +
                "latestDateMidnightBeforeToday=" + latestDateMidnightBeforeToday +
                ", numberOfDaysBeforeToday=" + numberOfDaysBeforeToday +
                ", historticCaloriesIn=" + historticCaloriesIn +
                ", historicCaloriesOut=" + historicCaloriesOut +
                ", today=" + today +
                ", todayCaloriesIn=" + todayCaloriesIn +
                ", todayCaloriesOut=" + todayCaloriesOut +
                '}';
    }
}
