package com.costigan.virtualweight;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.widget.TextView;

import org.joda.time.DateTime;
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

    @Deprecated
    public TodaysCalories getCaloriesForDateXX(LocalDate searchDate) {
        TodaysCalories tc = new TodaysCalories();
        tc.setDate(searchDate);
//        tc.setCaloriesIn(1534);
        tc.setCaloriesOut(702);

        try {
            //try logging in
            String url = VwConstants.URL_LOGIN;

            Connection.Response res = Jsoup
                    .connect(url)
                    .data("username",  VwConstants.USERNAME )
                    .data("password", VwConstants.PASSWORD )
                    .method(Connection.Method.POST)
                    .execute();

            Document doc = res.parse();

            Map<String, String> cookies = res.cookies();


            //TODO
            Document doc2 = Jsoup
                    .connect(getUrl(searchDate))
                    .cookies(cookies)
                    .get();

            //See https://data-lessons.github.io/library-webscraping-DEPRECATED/02-csssel/
            Elements totalElement = doc2.select("tfoot tr > :nth-child(2)");

            //TODO: Add in error handling

            List totalList = totalElement.eachText();
            String totalString = (String)totalList.get(0);
            totalString = totalString.replace(",","");
            int  totalInt = Integer.parseInt(totalString);
            tc.setCaloriesIn(totalInt);

            //This works perfectly to here
            //TODO
            //1) Choose a fixed past date in mfp with known values
            //2) Move the HTML code out if here into MfpScreenScraper
            //3) Cam I parameterise out the username,passowrd and url to a file that isn;t uploaded to git
            //4) Create a Junit to run these tests
            //5) Sort out threading


        } catch (Exception ex) {
            return null;
        }







        return tc;
    }



    public void screenScrape() {
        screenScrape(null);

    }


    @Deprecated
    public void screenScrape(TextView statusTextView) {
//        LocalDate searchDate = new LocalDate(2018,10,27);
        LocalDate searchDate = new LocalDate(2018, 10, 29);
        try {
            TodaysCalories tc = getCaloriesForDate(searchDate);
            statusTextView.setText("Calories today = "+tc);
        } catch (Exception ex) {
            statusTextView.setText("ex=" + ex);
            if (statusTextView != null) {
            }

        }

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
        return VwConstants.URL_QUERY+VwConstants.USERNAME+"?from="+mfpDate+"&to="+mfpDate;
    }

    private LocalDate getDateFromAmericanDateString(String americanDate) {
        //String americanDate = "october 29, 2018";
        DateTimeFormatter dtf = DateTimeFormat.forPattern("MMMM dd, YYYY");
        return org.joda.time.LocalDate.parse(americanDate, dtf);
    }

}
