package com.costigan.virtualweight;

public class VirtualWeight {
    WeightResultDto dto = new WeightResultDto();
    private double weight = -1;
    public void calcuateWeight(){
        weight = 83.2;
        //TODO Calculate these values
        dto.setYesterdaysWeight(80.5);
        dto.setBmr(2065);
        dto.setCaloriesOut(500);
        dto.setCaloriesIn(1500);

    }

    //public String getWeight(){
    //    return String.format("%.1f", weight);
    //
    // }


    public WeightResultDto getWeight() {
        return dto;
    }



}
