package com.costigan.virtualweight;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Serializable;

/**
 *
 * File Format: username,password,bmr,startDate,startWeight,targetWeight
 */
public class VwSettings implements Serializable {
    //Implements serializable so we  can pass this object in Intents

    String userName = "";
    String password = "";
    int bmr = 0;
    LocalDate startDate;
    double startWeight = 0.0;
    double targetWeight = 0.0;

    /**
     * Default vonstructor
     */
    public VwSettings(){
    }

    public VwSettings(String line) throws Exception{
        parseLine(this,line);
    }

    public VwSettings(String userName, String passwoed,int bmr,LocalDate startDate,double startWeight,double targetWeight) throws Exception{
        this.userName = userName;
        this.password = userName;
        this.bmr = bmr;
        this.startDate = startDate;
        this.startWeight = startWeight;
        this.targetWeight = targetWeight;
    }

    /**
     * Parse the string into this object
     * @param line
     * @return
     */
    public static VwSettings valueOf(String line) throws Exception{
        VwSettings settings = new VwSettings();
        parseLine(settings,line);
        return settings;
    }

    public static void parseLine(VwSettings settings,String line) throws Exception{
        //Parse the string into this object
        String[] fields = line.split(",");
        if( fields.length != 6){
            throw new Exception("Should be 6 fields, not: " + fields.length);
        }
        settings.setUserName(fields[0]);
        settings.setPassword(fields[1]);
        settings.setBmr( Integer.parseInt( fields[2] ) );

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        LocalDate ld = LocalDate.parse(fields[3], formatter);

        settings.setStartDate( ld );
        settings.setStartWeight( Double.parseDouble(fields[4]) );
        settings.setTargetWeight( Double.parseDouble(fields[5]) );
    }




    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBmr() {
        return bmr;
    }

    public void setBmr(int bmr) {
        this.bmr = bmr;
    }

    public double getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(double targetWeight) {
        this.targetWeight = targetWeight;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getDayAfterStartDate() {
        //As we alreday have the weight on the start date, we don't dont need t retrieve these calores
        //Instead retrieve it from the following daay.
        DateTime dt = startDate.toDateTimeAtStartOfDay();
        dt = dt.plusDays(1);
        return dt.toLocalDate();
    }




    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public void setStartDateASYYYYMMMDD(String string) {
        //String string = "2018-10-29";
        LocalDate date = org.joda.time.LocalDate.parse(string, TodaysCalories.DATE_FORMATTER);
        setStartDate(date);
    }




    public double getStartWeight() {
        return startWeight;
    }

    public void setStartWeight(double startWeight) {
        this.startWeight = startWeight;
    }




    public String toWriteLine() {
        return userName + "," + password + "," + bmr + "," + startDate + "," + startWeight + "," + targetWeight;
    }


    @Override
    public String toString() {
        return "VwSettings{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", bmr=" + bmr +
                ", startDate=" + startDate +
                ", startWeight=" + startWeight +
                ", targetWeight=" + targetWeight +
                '}';
    }

    @Override
    public boolean equals (Object obj)
    {
        if (this==obj) return true;
        if (this == null) return false;
        if (this.getClass() != obj.getClass()) return false;

        // Class name is Employ & have lastname
        VwSettings vws = (VwSettings) obj ;
        return  userName.equals(vws.getUserName()) &&
                password.equals(vws.getPassword()) &&
                bmr == vws.getBmr() &&
                startDate.equals(vws.getStartDate() ) &&
                startWeight == vws.getStartWeight() &&
                targetWeight == vws.getTargetWeight() ;
    }

}
