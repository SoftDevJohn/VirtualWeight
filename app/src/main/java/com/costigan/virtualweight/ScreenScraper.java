package com.costigan.virtualweight;

import org.joda.time.LocalDate;

/**
 * Strategy design pattern for an interface implementing a screen scraper to retrieve
 * calories.
 */
public interface ScreenScraper {
    public TotalCalories getTotalCaloriesDateToToday(String username, String password, LocalDate fromDate) throws Exception;
    }
