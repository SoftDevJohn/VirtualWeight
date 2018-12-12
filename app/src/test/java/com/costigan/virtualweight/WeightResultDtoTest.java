package com.costigan.virtualweight;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WeightResultDtoTest {

    WeightResultDto dto = new WeightResultDto();
    @Before
    public void setUp() {


        dto.setYesterdaysWeight(80.5);
        dto.setBmr(2065);
        dto.setCaloriesOut(500);
        dto.setCaloriesIn(1500);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getWeight() {
    }

    @Test
    public void getBmr() {
        assertEquals(dto.getBmr(),2065,0.01);
    }

    /**
     * Supperting fucntion
     * @return
     */
    public double getBmrSoFar() {
        double fractionOfDay = WeightResultDto.getFractionOfDay();
        double bmr = dto.getBmr();
        return bmr * fractionOfDay;
    }


    @Test
    public void getBmrSinceMidnight() {
        double bmrSoFar = getBmrSoFar();
        assertEquals(dto.getBmrSinceMidnight(),bmrSoFar,0.01);
    }



    @Test
    public void getCaloriesOut() {
        assertEquals(dto.getCaloriesOut(),500,0.01);
    }

    @Test
    public void getCaloriesIn() {
        assertEquals(dto.getCaloriesIn(),1500,0.01);
    }

    @Test
    public void getNetCalories() {
        assertEquals(dto.getNetCalories(),-1065,0.01);
    }

    @Test
    public void getNetCaloriesSinceMidnight() {
        double bmrSoFar = getBmrSoFar();
        double caloriesIn = dto.getCaloriesIn();
        double caloriesOut = dto.getCaloriesOut();
        double netCalories = caloriesIn - caloriesOut - bmrSoFar;
        assertEquals(dto.getNetCaloriesSinceMidnight(),netCalories,0.01);
    }

    @Test
    public void getWeightChangeSinceMidnight() {
        double bmrSoFar = getBmrSoFar();
        double caloriesIn = dto.getCaloriesIn();
        double caloriesOut = dto.getCaloriesOut();
        double netCalories = caloriesIn - caloriesOut - bmrSoFar;
        double weightChange = netCalories / 7700;
        assertEquals(dto.getWeightChangeSinceMidnight(),weightChange,0.01);
    }

    @Test
    public void getCyrrentWeight() {
        double yesterdaysWeight  = dto.getYesterdaysWeight();
        double weightChange = yesterdaysWeight + dto.getWeightChangeSinceMidnight();
        assertEquals(dto.getCurrentWeight(),weightChange,0.01);
    }


}