package com.costigan.virtualweight;

import org.joda.time.LocalDate;

import java.util.Calendar;

class CalorieCalculator {
    private VwSettings settings = null;
    private TotalCalories total = null;

    public VwSettings getSettings() {
        return settings;
    }

    public void setSettings(VwSettings settings) {
        this.settings = settings;
    }

    public TotalCalories getTotal() {
        return total;
    }

    public void setTotal(TotalCalories total) {
        this.total = total;
    }

    public int getNetCalories() {
        return total.getTotalCaloriesIn() - total.getTotalCaloriesOut() - getTotalBmr();
    }

    public int getTotalBmr() {
        int bmr = getSettings().getBmr();
        return getTotalBmrToYesterday() + getBmrSinceMidnight();
    }

    public int getTotalBmrToYesterday(){
        int bmr = getSettings().getBmr();
        return bmr * total.getNumberOfDaysBeforeToday();
    }

    public int getBmrSinceMidnight() {
        int bmr = getSettings().getBmr();
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

    public double getNetWeightChange() {
        //int bmr = getSettings().getBmr();
        double netWeight = (double)getNetCalories() / (double)7700;
        //Round to two decomal places
        int intWeight = (int)(netWeight * 1000);
        netWeight = (double)intWeight/(double)1000;
        return netWeight;
    }

    public double getNetWeight() {
        return getSettings().getStartWeight() + getNetWeightChange();
    }



}
