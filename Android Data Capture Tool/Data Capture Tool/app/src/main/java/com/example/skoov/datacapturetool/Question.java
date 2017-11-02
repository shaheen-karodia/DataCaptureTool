package com.example.skoov.datacapturetool;

import java.io.Serializable;

/**
 * Created by skoov on 8/14/2016.
 */
public class Question extends Annotation implements Serializable {
    /*A specific instance of the Annotation class, adding a String variable for the Question name added */
    String question;

    public Question(int id, long timeOf, long time) {
        super(id, timeOf, time);
    }

    public void addQuestion(String question) {
        this.question = question;
    }

    public String toString() {
        return this.question;
    }

}
