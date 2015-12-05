package com.slavafleer.moviemanager;

public class Movie {

    /** Id given automatically to each object creating. */
    private static int counter = 0;

    private int _id;
    private String subject;
    private String body;
    // TODO:For now url is a String, need to decide later if it must be URL.
    private String url;

    public Movie() {
        set_id(++counter);
    }

    public Movie(String subject, String body, String url) {
        set_id(++counter);
        this.subject = subject;
        this.body = body;
        this.url = url;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        // _id must be positive;
        if(_id > 0) {
            this._id = _id;
        }
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
        return _id + ": " + subject;
    }
}
