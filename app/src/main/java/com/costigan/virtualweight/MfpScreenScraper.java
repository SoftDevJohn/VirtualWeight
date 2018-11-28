package com.costigan.virtualweight;
import android.support.annotation.NonNull;
import android.widget.TextView;

import org.joda.time.LocalDate;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Map;

public class MfpScreenScraper {
    public int calories = 0;


    public TodaysCalories getCaloriesForToday() {

        LocalDate date = org.joda.time.LocalDate.now();
        return getCaloriesForDate(date);
    }

    public TodaysCalories getCaloriesForDate(LocalDate date) {
        TodaysCalories tc = new TodaysCalories();
        tc.setDate(date);
//        tc.setCaloriesIn(1534);
        tc.setCaloriesOut(702);

        try {
            //try logging in
            String url = SecureConstants.URL_LOGIN;

            Connection.Response res = Jsoup
                    .connect(url)
                    .data("username", SecureConstants.USERNAME )
                    .data("password", SecureConstants.PASSWORD )
                    .method(Connection.Method.POST)
                    .execute();

            Document doc = res.parse();

            Map<String, String> cookies = res.cookies();

            Document doc2 = Jsoup
                    .connect(getUrl())
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
        public void screenScrape(TextView statusTextView){

        if(statusTextView !=null) {
            statusTextView.setText("testing testing");
        }

        try {
            //try logging in
            String url = SecureConstants.URL_LOGIN;

            Connection.Response res = Jsoup
                    .connect(url)
                    .data("username", SecureConstants.USERNAME )
                    .data("password", SecureConstants.PASSWORD )
                    .method(Connection.Method.POST)
                    .execute();

            Document doc = res.parse();

            Map<String, String> cookies = res.cookies();

            Document doc2 = Jsoup
                    .connect(getUrl())
                    .cookies(cookies)
                    .get();

            //See https://data-lessons.github.io/library-webscraping-DEPRECATED/02-csssel/
            Elements totalElement = doc2.select("tfoot tr > :nth-child(2)");


            if(statusTextView !=null) {
                statusTextView.setText("done: " + totalElement);
            }
            List totalList = totalElement.eachText();
            String totalString = (String)totalList.get(0);
            totalString = totalString.replace(",","");
            int  totalInt = Integer.parseInt(totalString);

            calories = totalInt;
            if(statusTextView !=null) {
                statusTextView.setText("doneX: " + totalInt);
            }
            //This works perfectly to here
            //TODO
            //1) Choose a fixed past date in mfp with known values
            //2) Move the HTML code out if here into MfpScreenScraper
            //3) Cam I parameterise out the username,passowrd and url to a file that isn;t uploaded to git
            //4) Create a Junit to run these tests
            //5) Sort out threading


        } catch (Exception ex) {
            if(statusTextView !=null) {
                statusTextView.setText("Ex: " + ex);
            }
        }



    }

    @NonNull
    private String getUrl() {
        return SecureConstants.URL_QUERY+SecureConstants.USERNAME+"?from=2018-10-29&to=2018-10-29";
    }

}
