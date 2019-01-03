package com.example.deepgandhi.movieguidedemo;

public class childPojo {
    String season_no,season_name,no_of_episode,air_date;

    public childPojo(String season_no, String season_name, String no_of_episode, String air_date) {
        this.season_no = season_no;
        this.season_name = season_name;
        this.no_of_episode = no_of_episode;
        this.air_date = air_date;
    }

    public String getSeason_no() {
        return season_no;
    }

    public void setSeason_no(String season_no) {
        this.season_no = season_no;
    }

    public String getSeason_name() {
        return season_name;
    }

    public void setSeason_name(String season_name) {
        this.season_name = season_name;
    }

    public String getNo_of_episode() {
        return no_of_episode;
    }

    public void setNo_of_episode(String no_of_episode) {
        this.no_of_episode = no_of_episode;
    }

    public String getAir_date() {
        return air_date;
    }

    public void setAir_date(String air_date) {
        this.air_date = air_date;
    }
}
