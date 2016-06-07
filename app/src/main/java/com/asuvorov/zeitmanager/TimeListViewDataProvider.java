package com.asuvorov.zeitmanager;

/**
 * Created by ASuvorov on 07.06.2016.
 */
public class TimeListViewDataProvider {

    String kommenZeit;
    String gehenZeit;

    public TimeListViewDataProvider(String kommenZeit, String gehenZeit) {
        this.kommenZeit = kommenZeit;
        this.gehenZeit = gehenZeit;
    }

    public String getKommenZeit() {
        return kommenZeit;
    }

    public void setKommenZeit(String kommenZeit) {
        this.kommenZeit = kommenZeit;
    }

    public String getGehenZeit() {
        return gehenZeit;
    }

    public void setGehenZeit(String gehenZeit) {
        this.gehenZeit = gehenZeit;
    }

}
