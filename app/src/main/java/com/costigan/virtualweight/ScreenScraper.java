package com.costigan.virtualweight;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * Strategy design pattern for an interface implementing a screen scraper to retrieve
 * calories.
 */
public interface ScreenScraper {
    public List<Calorie> getCaloriesForDateList(List<LocalDate> retrievalDates) throws Exception;
    }
