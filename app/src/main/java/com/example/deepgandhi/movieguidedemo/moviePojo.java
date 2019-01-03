package com.example.deepgandhi.movieguidedemo;

public class moviePojo {
    String movieName;
    String imageUrl;
    int id;

    public moviePojo(int id,String movieName, String imageUrl) {
        this.movieName = movieName;
        this.imageUrl = imageUrl;
        this.id=id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
