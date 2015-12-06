package com.slavafleer.moviemanager.data;

public class Movie {

    /** Id given automatically to each object creating. */
    private static int counter = 0;

    private String id;
    private String subject;
    private String body;
    // TODO:For now url is a String, need to decide later if it must be URL.
    private String url;

    public Movie() {
        setId("m" + ++counter);
    }

    public Movie(String subject, String body, String url) {
        setId("m" + ++counter);
        this.subject = subject;
        this.body = body;
        this.url = url;
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

    @Override
    public String toString() {
        return subject;
    }
}
