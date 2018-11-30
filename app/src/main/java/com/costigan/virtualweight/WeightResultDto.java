package com.costigan.virtualweight;

import java.util.Calendar;

public class WeightResultDto {
    private double yesterdaysWeight = -1;
    private double bmr = -1;
    private double caloriesOut = -1;
    private double caloriesIn = -1;


    //todo add in derived methids
    //todo create junit to test it
    public double getNetCalories() {
        return getCaloriesIn() - getCaloriesOut() - getBmr();
    }

    /**
     * The current calories since midnight
     * @return
     */
    public double getNetCaloriesSinceMidnight() {
        return getCaloriesIn() - getCaloriesOut() - getBmrSinceMidnight();
    }
    public String getNetCaloriesSinceMidnightAsString(){
        return String.format("%d", (int)getNetCaloriesSinceMidnight());
    }


    /**
     *
     * @return yesterdaysWeight change since midnight in Kg
     */
    public double getWeightChangeSinceMidnight() {
        return getNetCaloriesSinceMidnight()/7700;
    }
    public String getWeightChangeSinceMidnightAsString(){
        return String.format("%.1f", getWeightChangeSinceMidnight());
    }

    public double getCurrentWeight() {
        return getYesterdaysWeight()+getWeightChangeSinceMidnight();
    }

    public String getCurrentWeightAsString(){
        return String.format("%.1f", getCurrentWeight());
    }



    public double getBmrSinceMidnight() {
        return getBmr() * getFractionOfDay();
    }

    /**
     * @return the fraction of the day accurate to three decimal places, which is about to the minute
     */
    public static double getFractionOfDay() {
        double d = getSecondsSinceMidnight() / (double)86400000;
        return getRoundedValue(d,3);
    }



    public String getBmrSinceMidnightAsString(){
        return String.format("%d", (int)getBmrSinceMidnight());
    }



    public double getYesterdaysWeight() {
        return yesterdaysWeight;
    }


    public void setYesterdaysWeight(double yesterdaysWeight) {
        this.yesterdaysWeight = yesterdaysWeight;
    }


    public double getBmr() {
        return bmr;
    }

    public String getBmrAsString(){
        return String.format("%.1f", getBmr());
    }


    public void setBmr(double bmr) {
        this.bmr = bmr;
    }



    public double getCaloriesOut() {
        return caloriesOut;
    }

    public String getCaloriesOutAsString(){
        return String.format("%d", (int)getCaloriesOut());
    }

    public void setCaloriesOut(double caloriesOut) {
        this.caloriesOut = caloriesOut;
    }

    public double getCaloriesIn() {
        return caloriesIn;
    }

    public void setCaloriesIn(double caloriesIn) {
        this.caloriesIn = caloriesIn;
    }
    public String getCaloriesInAsString(){
        return String.format("%d", (int)getCaloriesIn());
    }



    public static long getSecondsSinceMidnight() {
        Calendar now = Calendar.getInstance();
        Calendar midnight = Calendar.getInstance();

        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);
        midnight.set(Calendar.MILLISECOND, 0);

        return now.getTimeInMillis() - midnight.getTimeInMillis();
    }


    public static double getRoundedValue(double d, int places) {
        double scale = Math.pow(10, places);
        return Math.round(d * scale) / scale;
    }


}
