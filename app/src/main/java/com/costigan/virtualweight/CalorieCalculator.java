package com.costigan.virtualweight;

import org.joda.time.LocalDate;

import java.util.List;

public class CalorieCalculator {
    private List<TodaysCalories> calories;
    private LocalDate today;

    public CalorieCalculator(List<TodaysCalories> calories, LocalDate today){
        this.calories = calories;
        this.today = today;
    }


    //public getTotalCaloriesBeforeToday(List<TodaysCalories> calories){
    //    this.calories = calories;
    //}



    /*
           Context ctx = InstrumentationRegistry.getTargetContext();
        VwFileManager fm = new VwFileManager();

        //Now read from this file
        StringBuffer stringBuffer = new StringBuffer();
        fm.readFile(ctx, VwFileManager.SETTINGS_FILE, stringBuffer);
        VwSettings vws = new VwSettings(stringBuffer.toString().trim());

        //returns the 26-oct
        LocalDate dayAfterStartDate = vws.getDayAfterStartDate();

        String startDateStr = "2018-10-26";
        LocalDate startDate = org.joda.time.LocalDate.parse(startDateStr, TodaysCalories.DATE_FORMATTER);
        String endDateStr = "2018-10-29";
        LocalDate endDate = org.joda.time.LocalDate.parse(endDateStr, TodaysCalories.DATE_FORMATTER);


        MfpScreenScraper mpfSc = new MfpScreenScraper();


        List<TodaysCalories> list = mpfSc.getCaloriesForDates(startDate,endDate);
        //todo:
        //1) Get calories between the given dates inclusive
        //2) Scan list for latest date before today and save that back into the configuraion fule


        //TODO
        //Sum the calories
        //Get Yesterdays date
        //cc = CalorieCalculator(list);
        //cc.sumCaloriesToYesterdayMidnight();
        //cc.caloriesForToday()
        //cc.calloriesUpToToday();


     */
}
