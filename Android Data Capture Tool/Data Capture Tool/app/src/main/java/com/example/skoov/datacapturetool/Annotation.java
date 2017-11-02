package com.example.skoov.datacapturetool;

import java.io.Serializable;

public class Annotation implements Serializable {
    /* Generalised class to capture core components of all
    annotations made to a recording.

    It contains 4 instances variables and two constructor methods
     */

    int ID;
    long relativeTimeStamp;
    long absoluteTimeStamp;
    String comments;

    public Annotation() {
        this.relativeTimeStamp = 0;
        comments = "";
    }

    public Annotation(int id, long timeOf, long time) {
        this.relativeTimeStamp = time;
        this.ID = id;
        this.absoluteTimeStamp = timeOf;
        comments = "";
    }

}
