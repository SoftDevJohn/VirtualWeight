package com.costigan.virtualweight;

import android.os.Debug;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Map;

import static com.costigan.virtualweight.TodaysCalories.MFP_DATE_FORMAT;

public class MfpScreenScraper {
    public int calories = 0;


    public TodaysCalories getCaloriesForToday() throws Exception {

        LocalDate date = org.joda.time.LocalDate.now();
        return getCaloriesForDate(date);
    }

    /**
     * @param searchDate - the date to retrive the calorie information for
     * @return TodaysCalories
     * @throws Exception
     */
    public TodaysCalories getCaloriesForDate(LocalDate searchDate) throws Exception {
        TodaysCalories todaysCalories = new TodaysCalories();

        String url = VwConstants.URL_LOGIN;

        Connection.Response res = Jsoup
                .connect(url)
                .data("username", VwConstants.USERNAME)
                .data("password", VwConstants.PASSWORD)
                .method(Connection.Method.POST)
                .execute();

        Document doc = res.parse();

        Map<String, String> cookies = res.cookies();

        Document doc2 = Jsoup
                .connect(getUrl(searchDate))
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
                return null;
            }

            LocalDate dt = getDateFromAmericanDateString(dateElement.text());

            //Make sure both are the same
            if (searchDate.compareTo(dt) == 0) {
                todaysCalories.setDate(dt);
            } else {
                //Mismatched dates, don't process
                return null;
            }

        } else {
            return null;
        }

        //Scrape the Calories In
        Elements caloriesInElement = doc2.select("table[id=food] > tfoot tr > :nth-child(2)");
        if (caloriesInElement.size() != 0) {
            List caloriesInList = caloriesInElement.eachText();
            String caloriesInString = (String) caloriesInList.get(0);
            caloriesInString = caloriesInString.replace(",", "");
            int caloriesInInt = Integer.parseInt(caloriesInString);
            todaysCalories.setCaloriesIn(caloriesInInt);
        } else {
            todaysCalories.setCaloriesIn(0);
        }

        //Scrape scalories ut
        Elements caloriesOutElement = doc2.select("#excercise > tfoot tr > :nth-child(2)");
        if (caloriesOutElement.size() != 0) {
            List caloriesOutList = caloriesOutElement.eachText();
            String caloriesOutString = (String) caloriesOutList.get(0);
            caloriesOutString = caloriesOutString.replace(",", "");
            int caloriesOutInt = Integer.parseInt(caloriesOutString);
            todaysCalories.setCaloriesOut(caloriesOutInt);
        } else {
            todaysCalories.setCaloriesOut(0);
        }

        return todaysCalories;
    }

    @NonNull
    private String getUrl(LocalDate searchDate) {

        String mfpDate = searchDate.toString(MFP_DATE_FORMAT);
//        return VwConstants.URL_QUERY+VwConstants.USERNAME+"?from=2018-10-29&to=2018-10-29";
        return VwConstants.URL_QUERY + VwConstants.USERNAME + "?from=" + mfpDate + "&to=" + mfpDate;
    }

    private LocalDate getDateFromAmericanDateString(String americanDate) {
        //String americanDate = "october 29, 2018";
        DateTimeFormatter dtf = DateTimeFormat.forPattern("MMMM dd, YYYY");
        return org.joda.time.LocalDate.parse(americanDate, dtf);
    }

}
