package com.example.skoov.datacapturetool;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jameslloyd on 8/29/16.
 */
public class Interviewee implements Serializable {

    /*Class to hold the details of the interviewee
    Contains a field for name and surname, then a string array for all additional details to be added
    */
    ArrayList <String> details;

    public Interviewee (ArrayList <String> input) {
        this.details = input;
    }

    public String toString () {
        String out = "Interviewee Details \n";
        for (int i = 0; i < details.size(); i ++) {
            out += details.get(i) + "\n";
        }
        return out;
    }
}
