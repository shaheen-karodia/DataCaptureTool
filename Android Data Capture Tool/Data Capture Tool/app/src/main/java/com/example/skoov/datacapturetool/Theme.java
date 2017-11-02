package com.example.skoov.datacapturetool;

import java.io.Serializable;

/**
 * Created by skoov on 8/14/2016.
 */
public class Theme extends Annotation implements Serializable {
    /* A specific instance of the Annotation class, adding a String variable to store the theme added */

    String theme;

    public Theme(int id, long timeOf, long time) {
        super(id, timeOf, time);
    }

    public void addTheme(String theme) {
        this.theme = theme;
    }

    public String toString() {
        return this.theme;
    }

}
