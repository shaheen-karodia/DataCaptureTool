package activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.skoov.datacapturetool.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class InputActivity extends AppCompatActivity {
    /* This activity is responsible for taking in the input of themes and questions from the user
    *  and stores these in arrays which are carried through to the following activities for use
    *  in the recording object. */


    // Create variables and lists used in the activity.
    ArrayList <String> themes = new ArrayList<String>();
    ArrayList <String> questions = new ArrayList<String>();
    ArrayList <String> interviewDetails = new ArrayList<String>();
    EditText themeTextbox, questionTextbox;
    CoordinatorLayout coordinatorLayout;
    TextInputLayout inputLayoutTheme, inputLayoutQuestion;
    Button lastUsed, noQuestionsAddedText, noThemesAddedText;
    Toolbar mToolbar;
    String projectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialise activity and variables required for various aspects of
        // the UI, including the EditTexts.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        Intent intent = getIntent();
        interviewDetails = intent.getStringArrayListExtra("details");
        projectName = intent.getStringExtra("projectName");

        themeTextbox = (EditText) findViewById(R.id.themeTextbox);
        inputLayoutTheme = (TextInputLayout) findViewById(R.id.input_layout_themeEditText);
        questionTextbox = (EditText) findViewById(R.id.questionTextbox);
        inputLayoutQuestion = (TextInputLayout) findViewById(R.id.input_layout_questionEditText);
        lastUsed = (Button)findViewById(R.id.previousList);
        noQuestionsAddedText = (Button)findViewById(R.id.noQuestionsAddedText);
        noThemesAddedText = (Button)findViewById(R.id.noThemesAddedText);

        lastUsed.setOnClickListener(new View.OnClickListener() {
            // Button which launches an AlertDialog containing a list of the previously created projects,
            // from which the user can select a project name, which invokes the onClick method below.
            @Override
            public void onClick(View v) {
                String [] previousProjectsList = new String[0];
                try {
                    previousProjectsList = getPreviousProjectNames();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (previousProjectsList==null) {
                    Snackbar.make(coordinatorLayout, "No recently created projects.", Snackbar.LENGTH_LONG).show();
                }
                else if (previousProjectsList[0]=="") {
                    Snackbar.make(coordinatorLayout, "No recently created projects.", Snackbar.LENGTH_LONG).show();
                }
                else {
                    AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(InputActivity.this);
                    alertdialogbuilder.setTitle("Select a previously created project: ");
                    AlertDialog.Builder builder = alertdialogbuilder.setItems(previousProjectsList, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Add the themes and questions of the previously created project to the
                            // current theme and question lists, upon the selection of a
                            // previously created project.
                            String selectedProject[] = null;
                            try {
                                selectedProject = getPreviousProject(which).split("~");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (selectedProject[1].split("`").length == 0 && selectedProject[2].split("`").length == 0)
                                Snackbar.make(coordinatorLayout, "Loaded 0 themes and 0 questions from project \'" +
                                        selectedProject[0] + "\'.", Snackbar.LENGTH_LONG).show();
                            else {
                                int noPreviousThemesAdded = 0;
                                for (String t : selectedProject[1].split("`")) {
                                    themes.add(t);
                                    ++noPreviousThemesAdded;
                                }
                                if (themes.size() == 1)
                                    noThemesAddedText.setText("1 theme added");
                                else
                                    noThemesAddedText.setText(themes.size() + " themes added");
                                int noPreviousQuestionsAdded = 0;
                                for (String q : selectedProject[2].split("`")) {
                                    questions.add(q);
                                    ++noPreviousQuestionsAdded;
                                }
                                if (questions.size() == 1)
                                    noQuestionsAddedText.setText("1 question added");
                                else
                                    noQuestionsAddedText.setText(questions.size() + " questions added");
                                Snackbar.make(coordinatorLayout, "Loaded " + noPreviousThemesAdded +
                                        " themes and " + noPreviousQuestionsAdded + " questions from " +
                                        "project \'" + selectedProject[0] + "\'.", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
                    AlertDialog dialog = alertdialogbuilder.create();
                    dialog.show();
                }
            }
        });

        themeTextbox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            // Add text currently in the EditText to the list of themes and
            // clear the EditText and refocus to allow for more additions of themes.
            // (this method is used when the ENTER key on the keyboard is pressed)
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND && !themeTextbox.getText().toString().trim().isEmpty()) {
                    themes.add(themeTextbox.getText().toString().trim());
                    Snackbar.make(coordinatorLayout, "Theme \'" + themeTextbox.getText().toString().trim() + "\' added.",
                            Snackbar.LENGTH_LONG).show();
                    handled = true;
                    if (themes.size()==1)
                        noThemesAddedText.setText("1 theme added");
                    else
                        noThemesAddedText.setText(themes.size() + " themes added");
                }
                themeTextbox.setText("");
                themeTextbox.setFocusableInTouchMode(true);
                themeTextbox.setFocusable(true);
                themeTextbox.requestFocus();
                return handled;
            }
        });

        questionTextbox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            // Add text currently in the EditText to the list of questions and
            // clear the EditText and refocus to allow for more additions of questions.
            // (this method is used when the ENTER key on the keyboard is pressed)
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND  && !questionTextbox.getText().toString().trim().isEmpty()) {
                    questions.add(questionTextbox.getText().toString().trim());
                    Snackbar.make(coordinatorLayout, "Question \'" + questionTextbox.getText().toString().trim() + "\' added.",
                            Snackbar.LENGTH_LONG).show();
                    handled = true;
                    if (questions.size()==1)
                        noQuestionsAddedText.setText("1 question added");
                    else
                        noQuestionsAddedText.setText(questions.size() + " questions added");
                }
                questionTextbox.setText("");
                questionTextbox.setFocusableInTouchMode(true);
                questionTextbox.setFocusable(true);
                questionTextbox.requestFocus();
                return handled;
            }
        });

    }


    public void addTheme(View view) {
        // Add text currently in the EditText to the list of themes and
        // clear the EditText and refocus to allow for more additions of themes.
        // (this method is for the add theme button)
        if (!themeTextbox.getText().toString().trim().isEmpty()) {
            themes.add(themeTextbox.getText().toString().trim());
            Snackbar.make(coordinatorLayout, "Theme \'" + themeTextbox.getText().toString().trim() + "\' added.",
                    Snackbar.LENGTH_LONG).show();
            if (themes.size()==1)
                noThemesAddedText.setText("1 theme added");
            else
                noThemesAddedText.setText(themes.size() + " themes added");
            themeTextbox.setText("");
            themeTextbox.setFocusableInTouchMode(true);
            themeTextbox.setFocusable(true);
            themeTextbox.requestFocus();
        }
    }

    public void addQuestion(View view) {
        // Add text currently in the EditText to the list of questions and
        // clear the EditText and refocus to allow for more additions of questions.
        // (this method is for the add question button)
        if (!questionTextbox.getText().toString().trim().isEmpty()) {
            questions.add(questionTextbox.getText().toString().trim());
            Snackbar.make(coordinatorLayout, "Question \'" + questionTextbox.getText().toString().trim() + "\' added.",
                    Snackbar.LENGTH_LONG).show();
            if (questions.size()==1)
                noQuestionsAddedText.setText("1 question added");
            else
                noQuestionsAddedText.setText(questions.size() + " questions added");
            questionTextbox.setText("");
            questionTextbox.setFocusableInTouchMode(true);
            questionTextbox.setFocusable(true);
            questionTextbox.requestFocus();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public String getPreviousProject(int n) throws IOException {
        // Get the entire string containing the project name, themes and annotations of a
        // previously created project, from the recents.csv file.
        String recentFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/Data Capture Tool/files";
        File recentFile = new File(recentFilePath);
        if (!recentFile.exists()) {
            return null;
        }
        File recentsFile = new File(recentFile, "recents.csv");
        FileReader reader = new FileReader(recentsFile);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int i;
        i = reader.read();
        while (i != -1) {
            byteArrayOutputStream.write(i);
            i = reader.read();
        }
        reader.close();

        return byteArrayOutputStream.toString().split("\n")[n];
    }

    public void sendMessage(View view) throws IOException {
        // Sends theme, question and detailValue lists to the next activity.
        // Also updates recent themes/questions list file
        updateRecent();
        Intent intent = new Intent(this, RecordingActivity.class);
        Bundle b = new Bundle();
        b.putStringArrayList("themes", themes);
        b.putStringArrayList("questions", questions);
        b.putStringArrayList("details", interviewDetails);
        intent.putExtra("projectName", projectName);
        intent.putExtras(b);
        startActivity(intent);
    }

    public String[] getPreviousProjectNames() throws IOException {
        // Reads in the recents.csv file and returns a String[] of the names,
        // and only names, of the previously created projects
        String recentFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/Data Capture Tool/files/";
        String[] projectNames = null;
        File recentFolder = new File(recentFilePath);
        if (!recentFolder.exists()) {
            return null;
        }
        File recentsFile = new File(recentFolder, "recents.csv");
        FileReader reader = new FileReader(recentsFile);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int i;
        i = reader.read();
        while (i != -1) {
            byteArrayOutputStream.write(i);
            i = reader.read();
        }
        reader.close();

        String[] projects = byteArrayOutputStream.toString().split("\n");
        byteArrayOutputStream.close();
        projectNames = new String[projects.length];
        for (int n = 0; n < projectNames.length; ++n) {
            projectNames[n] = projects[n].trim().split("~")[0];
        }

        return projectNames;
    }

    public boolean updateRecent() throws IOException {
        // Updates the recents.csv file to include the added themes and annotations as a
        // project, so that these can be accessed and used for another project later
        String recentFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/Data Capture Tool/files";
        File recentFile = new File(recentFilePath);
        if (!recentFile.exists()) {
            recentFile.mkdirs();
        }
        File recentsFile = new File(recentFile, "recents.csv");
        FileWriter writer = new FileWriter(recentsFile, true);

        String lineToAdd = projectName + "~";
        for (int i = 0; i < themes.size(); i++) {
            if (i==0)
                lineToAdd += themes.get(i);
            else
                lineToAdd += "`" + themes.get(i);
        }
        lineToAdd += "~";
        for (int j = 0; j < questions.size(); j++) {
            if (j==0)
                lineToAdd += questions.get(j);
            else
                lineToAdd += "`" + questions.get(j);
        }

        writer.append(lineToAdd+"\n");
        writer.close();

        return true;
    }
}
