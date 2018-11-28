package com.costigan.virtualweight;

public class VwSettings {

    String userName = "";
    String password = "";
    int bmr = 0;
    double targetWeight = 0.0;

    /**
     * Default vonstructor
     */
    public VwSettings(){
    }
    public VwSettings(String line) throws Exception{
     parseLine(this,line);
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

    private static void parseLine(VwSettings settings,String line) throws Exception{
        //Parse the string into this object
        String[] fields = line.split(",");
        if( fields.length != 4){
            throw new Exception("Should be 4 fields, not: " + fields.length);
        }
        settings.setUserName(fields[0]);
        settings.setPassword(fields[1]);
        settings.setBmr( Integer.parseInt( fields[2] ) );
        settings.setTargetWeight( Double.parseDouble(fields[3]) );
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

    @Override
    public String toString() {
        return getUserName()+","+getPassword()+","+getBmr()+","+getTargetWeight();
    }

}
