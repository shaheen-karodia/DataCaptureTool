package com.example.skoov.datacapturetool;

import android.os.Environment;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Recording implements Serializable {
    /*The most important class in making a recording, created when the record button is pressed

    The class holds the lists of themes and questions defined in InputActivity, along with the
    Interviewee object with details from IntervieweeDetails

    The class manages the creation and storage of annotations. It has two SimpleDateFormat objects
    to keep track of absolute time and relative time when creating an annoation.
     */

    List<Annotation> annotationList;
    List<String> themeList;
    List<String> questionList;
    Interviewee interviewee;
    static int nextID = 1;
    SimpleDateFormat absoluteTimestamp;
    SimpleDateFormat relativeTimestamp;
    String projectName;

    public Recording(ArrayList<String> themes, ArrayList<String> questions, ArrayList<String> interviewDetails, String projectName) {
        /* Constructor for the recording class */
        annotationList = new ArrayList<Annotation>();
        themeList = themes;
        questionList = questions;
        interviewee = new Interviewee(interviewDetails);
        absoluteTimestamp = new SimpleDateFormat("HH:mm:ss");
        relativeTimestamp = new SimpleDateFormat("mm:ss:SS");
        this.projectName = projectName;
    }

    public boolean addThemeAnnotation(String theme, long timeOf, long timeStamp) {
        /* Method to add a theme to the annotationList */
        Theme annotation = new Theme (nextID, timeOf, timeStamp);
        annotation.addTheme(theme);
        annotationList.add(annotation);
        ++nextID;
        return true;
    }

    public boolean addQuestionAnnotation(String question, long timeOf, long timeStamp) {
        /* Method to add a question to the annotationList */
        Question annotation = new Question (nextID, timeOf, timeStamp);
        annotation.addQuestion(question);
        annotationList.add(annotation);
        ++nextID;
        return true;
    }

    public String toString() {
        /* Method to print the details of the interview.
        Used in the creation of a text file, summarising the interview
         */
        String out = "TimeOf   -> ID TimeStamp      ~~> Annotation   \n";
        Annotation a;
        for (int i = 0; i < annotationList.size(); i ++) {
            a = annotationList.get(i);
            out = out + absoluteTimestamp.format(a.absoluteTimeStamp) + " -> " + a.ID + "  " + relativeTimestamp.format(a.relativeTimeStamp) + " min. ";
            out += "\t ~~~> " + a.toString() + "\n";
        }
        out += this.PrintInterviewDetails();
        return out;
    }

    public void exportAnnotations() throws IOException {
        PrintWriter writer = new PrintWriter(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Data Capture Tool/projects/" + projectName + "/DetailsAndAnnotations.csv", "UTF-8");
        writer.println(this.toString());
        writer.close();
    }

    public String PrintInterviewDetails() {
        /* Method to print out the details of the Interviewee details
        Followed by the list of themes and questions predefined for the interview
         */
        String details = "\n";
        details += "Interview Details. \n\n";
        details += interviewee.toString() + "\n";
        details += "Themes: \n";
        for (int i=0; i<themeList.size(); ++i) {
            details += themeList.get(i) + "\n";
        }
        details += "\n" + "Questions: \n";
        for (int i=0; i<questionList.size(); ++i) {
            details += questionList.get(i) + "\n";
        }
        return details;
    }

    public String[] getAnnotationList() {
        /* For use in the toString () method */
        if (relativeTimestamp ==null)
            relativeTimestamp = new SimpleDateFormat("mm:ss:SS");
        String[] annotations = new String[annotationList.size()];
        for (int i=0; i<annotationList.size(); ++i) {
            annotations[i] = annotationList.get(i).toString();
            annotations[i] += " ~ " + relativeTimestamp.format(annotationList.get(i).relativeTimeStamp);
        }
        return annotations;
    }

    public void themesToJSON() throws IOException {
        /* For integration of the recording with the webtool */
        FileWriter jsonFile = new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Data Capture Tool/projects/" + projectName + "/themes.json");
        jsonFile.write("[\n");
        if (annotationList!=null) {
            for (int i = 0; i < annotationList.size(); ++i) {
                if (annotationList.get(i).getClass() == Theme.class) {
                    jsonFile.write("\t{\n");
                    jsonFile.write("\t\tThemeTitle:\'" + annotationList.get(i).toString() + "\',\n");
                    absoluteTimestamp = new SimpleDateFormat("HH:mm:ss");
                    int relativeTimeInSeconds = (int) (annotationList.get(i).relativeTimeStamp * 0.001);
                    jsonFile.write("\t\tRelativeTime:" + relativeTimeInSeconds + ",\n");
                    jsonFile.write("\t\tAbsoluteTime:\'" + absoluteTimestamp.format(
                            annotationList.get(i).absoluteTimeStamp) + "\',\n");
                    jsonFile.write("\t\tComment:\'" + annotationList.get(i).comments + "\'\n");
                    jsonFile.write("\t},\n");
                }
            }
        }
        jsonFile.write("]");
        jsonFile.close();
    }

    public void questionsToJSON() throws IOException {
        /* For integration of the recording with the webtool */
        FileWriter jsonFile = new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Data Capture Tool/projects/" + projectName + "/questions.json");
        jsonFile.write("[\n");
        if (annotationList!=null) {
            for (int i = 0; i < annotationList.size(); ++i) {
                if (annotationList.get(i).getClass() == Question.class) {
                    jsonFile.write("\t{\n");
                    jsonFile.write("\t\tQuestionTitle:\'" + annotationList.get(i).toString() + "\',\n");
                    absoluteTimestamp = new SimpleDateFormat("HH:mm:ss");
                    int relativeTimeInSeconds = (int) (annotationList.get(i).relativeTimeStamp * 0.001);
                    jsonFile.write("\t\tRelativeTime:" + relativeTimeInSeconds + ",\n");
                    jsonFile.write("\t\tAbsoluteTime:\'" + absoluteTimestamp.format(
                            annotationList.get(i).absoluteTimeStamp) + "\',\n");
                    jsonFile.write("\t\tComment:\'" + annotationList.get(i).comments + "\'\n");
                    jsonFile.write("\t},\n");
                }
            }
        }
        jsonFile.write("]");
        jsonFile.close();
    }

    public void projectDetailsToJSON() throws IOException {
        /* For integration of the recording with the webtool */
        FileWriter jsonFile = new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Data Capture Tool/projects/" + projectName + "/project.json");

        jsonFile.write("{\n");
        jsonFile.write("\tProjectTitle:\'"+projectName+"\',\n");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        jsonFile.write("\tDate:\'"+formatter.format(now)+"\',\n");
        jsonFile.write("\tAudioPath:\'"+projectName+".mp3\',\n");

        String intervieweeDetailsString = "";
        if (interviewee.details!=null) {
            for (int i = 0; i < interviewee.details.size(); i++) {
                intervieweeDetailsString += interviewee.details.get(i) + " | ";
                if (i == interviewee.details.size() - 1)
                    intervieweeDetailsString += interviewee.details.get(i);
            }
        }
        jsonFile.write("\tIntervieweeDetails:\'"+intervieweeDetailsString+"\'");
        jsonFile.write("\n}");
        jsonFile.close();
    }

}
