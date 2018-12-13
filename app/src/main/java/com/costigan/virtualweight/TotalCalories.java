package com.costigan.virtualweight;

import org.joda.time.LocalDate;

import java.util.Calendar;

public class TotalCalories {

    public static final int SUCCESS = 0;
    public static int FAILURE = -1;

    private int status = SUCCESS;
    //DateTimeFormatter formatter = DateTimeFormatter.ofPattern(null,null);


    private int bmr = 0;
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

    //Factor
    public int getTotalBmrToYesterday(int bmr){
        return bmr * getNumberOfDaysBeforeToday();
    }

    public int getBmrSinceMidnight(int bmr) {
        return (int)((double)bmr * getFractionOfDay());
    }

    //Factor
    private static double getFractionOfDay() {
        double d = getSecondsSinceMidnight() / (double)86400000;
        return getRoundedValue(d,3);
    }

    //Factor
    public static double getRoundedValue(double d, int places) {
        double scale = Math.pow(10, places);
        return Math.round(d * scale) / scale;
    }


    //Factor
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TotalCalories{" +
                "status=" + status +
                ", bmr=" + bmr +
                ", latestDateMidnightBeforeToday=" + latestDateMidnightBeforeToday +
                ", numberOfDaysBeforeToday=" + numberOfDaysBeforeToday +
                ", historticCaloriesIn=" + historticCaloriesIn +
                ", historicCaloriesOut=" + historicCaloriesOut +
                ", today=" + today +
                ", todayCaloriesIn=" + todayCaloriesIn +
                ", todayCaloriesOut=" + todayCaloriesOut +
                '}';
    }
}
