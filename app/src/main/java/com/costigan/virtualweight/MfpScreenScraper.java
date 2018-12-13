package com.costigan.virtualweight;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.costigan.virtualweight.Calorie.MFP_DATE_FORMAT;

public class MfpScreenScraper implements ScreenScraper {
    public int calories = 0;

    /**
     * Return an object coontaining the list of Calories from the given date up to today.
     * @param username
     * @param password
     * @param fromDate
     * @return
     * @throws Exception
     */
    @Override
    public TotalCalories getTotalCaloriesDateToToday(String username, String password, LocalDate fromDate) throws Exception {

        LocalDate today = org.joda.time.LocalDate.now();
        return getTotalCaloriesForDates(username, password, fromDate, today);
    }


    private TotalCalories getTotalCaloriesForDates(String username, String password, LocalDate fromDate, LocalDate toDate) throws Exception {
        TotalCalories tc = new TotalCalories();
        List<Calorie> list = getCaloriesForDates(username, password, fromDate, toDate);

        tc.setNumberOfDaysBeforeToday(0);
        tc.setHistorticCaloriesIn(0);
        tc.setHistoricCaloriesOut(0);

        //Get all calories up to yesterday
        for (int i = 0; i < (list.size() - 1); i++) {
            Calorie cal = list.get(i);
            tc.setNumberOfDaysBeforeToday(tc.getNumberOfDaysBeforeToday() + 1);
            tc.setHistorticCaloriesIn(tc.getHistorticCaloriesIn() + cal.getCaloriesIn());
            tc.setHistoricCaloriesOut(tc.getHistoricCaloriesOut() + cal.getCaloriesOut());
            tc.setLatestDateMidnightBeforeToday(cal.getDate()); //Take the last date before today
        }

        //Now get todays calories
        if (list.size() >= 1) {
            Calorie cal = list.get(list.size() - 1);
            tc.setToday(cal.getDate());
            tc.setTodayCaloriesIn(cal.getCaloriesIn());
            tc.setTodayCaloriesOut(cal.getCaloriesOut());
        } else {
        }
        return tc;
    }

    /**
     * GOOD
     */
    private List<Calorie> getCaloriesForDates(String username, String password, LocalDate fromDate, LocalDate toDate) throws Exception {
        List<Calorie> list = new ArrayList<Calorie>();
        String url = VwConstants.URL_LOGIN;

        Connection.Response res = Jsoup
                .connect(url)
                .data("username", username)
                .data("password", password)
                .method(Connection.Method.POST)
                .execute();

        Document doc = res.parse();

        Map<String, String> cookies = res.cookies();

        // now iterate over each date
        for (LocalDate searchDate = fromDate; (searchDate.isBefore(toDate) || searchDate.isEqual(toDate)); searchDate = searchDate.plusDays(1)) {
            Calorie calorie = new Calorie();
            list.add(calorie);

            Document doc2 = Jsoup
                    .connect(getUrl(username,searchDate))
                    .cookies(cookies)
                    .get();

            //See https://data-lessons.github.io/library-webscraping-DEPRECATED/02-csssel/
            //Scrape te Date
            Elements dateElement = doc2.select("h2[id=date]");

            //Scrape the date
            //If we dont have at least a date, then something went wrong
            //The date should also be the date that was searched for
            if (dateElement.size() != 0) {
                if (dateElement.text().contains("No diary entries")) {
                    continue;
                }

                LocalDate dt = getDateFromAmericanDateString(dateElement.text());

                //Make sure both are the same
                if (searchDate.compareTo(dt) == 0) {
                    calorie.setDate(dt);
                } else {
                    //Mismatched dates, don't process
                    continue;
                }

            } else {
                continue;
            }

            //Scrape the Calories In
            Elements caloriesInElement = doc2.select("table[id=food] > tfoot tr > :nth-child(2)");
            if (caloriesInElement.size() != 0) {
                List caloriesInList = caloriesInElement.eachText();
                String caloriesInString = (String) caloriesInList.get(0);
                caloriesInString = caloriesInString.replace(",", "");
                int caloriesInInt = Integer.parseInt(caloriesInString);
                calorie.setCaloriesIn(caloriesInInt);
            } else {
                calorie.setCaloriesIn(0);
            }

            //Scrape scalories ut
            Elements caloriesOutElement = doc2.select("#excercise > tfoot tr > :nth-child(2)");
            if (caloriesOutElement.size() != 0) {
                List caloriesOutList = caloriesOutElement.eachText();
                String caloriesOutString = (String) caloriesOutList.get(0);
                caloriesOutString = caloriesOutString.replace(",", "");
                int caloriesOutInt = Integer.parseInt(caloriesOutString);
                calorie.setCaloriesOut(caloriesOutInt);
            } else {
                calorie.setCaloriesOut(0);
            }

        }

        return list;
    }

    @NonNull
    private String getUrl(String username,LocalDate searchDate) {

        String mfpDate = searchDate.toString(MFP_DATE_FORMAT);
//        return VwConstants.URL_QUERY+VwConstants.USERNAME+"?from=2018-10-29&to=2018-10-29";
        return VwConstants.URL_QUERY + username + "?from=" + mfpDate + "&to=" + mfpDate;
    }

    private LocalDate getDateFromAmericanDateString(String americanDate) {
        //String americanDate = "october 29, 2018";
        DateTimeFormatter dtf = DateTimeFormat.forPattern("MMMM dd, YYYY");
        return org.joda.time.LocalDate.parse(americanDate, dtf);
    }

}
