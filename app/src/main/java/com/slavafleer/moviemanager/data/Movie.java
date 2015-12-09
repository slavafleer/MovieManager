package com.slavafleer.moviemanager.data;

public class Movie {

    /** Id given automatically to each object creating. */
    private static int counter = 0;

    private String id;
    private String subject;
    private String body;
    private String url;
    private float rating; // from 1 to 10, 0 is not jet rated.
    private boolean isWatched;

    public Movie() {
        setId("m" + ++counter);

    }

    public Movie(String subject, String body, String url) {
        setId("m" + ++counter);
        this.subject = subject;
        this.body = body;
        this.url = url;
    }

    public Movie(String id, String subject, String body, String url) {
        this.id = id;
        this.subject = subject;
        this.body = body;
        this.url = url;
    }

    public Movie(String id, String subject, String body, String url, float rating, boolean isWatched) {
        this.id = id;
        this.subject = subject;
        this.body = body;
        this.url = url;
        this.rating = rating;
        this.isWatched = isWatched;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static int getCounter() {
        return counter;
    }

    public static void setCounter(int counter) {
        if(counter >= 0) {
            Movie.counter = counter;
        }
    }

    public boolean getIsWatched() {
        return isWatched;
    }

    public void setIsWatched(boolean isWatched) {
        this.isWatched = isWatched;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return subject;
    }
}
