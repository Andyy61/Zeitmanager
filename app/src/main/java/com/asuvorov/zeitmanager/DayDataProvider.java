package com.asuvorov.zeitmanager;

/**
 * Created by ASuvorov on 02.06.2016.
 *
 */
public class DayDataProvider {

    public DayDataProvider(String dayName, String date, String kommen, String gehen, String pause) {
        this.dayName = dayName;
        this.date = date;
        this.kommen = kommen;
        this.gehen = gehen;
        this.pause = pause;
    }

    String dayName;
    String date;
    String kommen;
    String gehen;
    String pause;


    public String getPause() {
        return pause;
    }

    public void setPause(String pause) {
        this.pause = pause;
    }
    public String getKommen() {
        return kommen;
    }

    public void setKommen(String kommen) {
        this.kommen = kommen;
    }

    public String getGehen() {
        return gehen;
    }

    public void setGehen(String gehen) {
        this.gehen = gehen;
    }





    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }




}
