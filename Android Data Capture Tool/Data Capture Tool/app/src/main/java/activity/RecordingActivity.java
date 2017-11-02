package activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.skoov.datacapturetool.R;
import com.example.skoov.datacapturetool.Recording;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RecordingActivity extends AppCompatActivity  {
    /* This is the main activity which is responsible for creating a recording and adding annotations
       to it. ListViews display the lists of themes and questions which can be selected to add
       annotations to the recording.
     */

    // Create variables required for the interface, as well as the MediaRecorder object
    ImageButton record, stop;
    private MediaRecorder recorder;
    private String outputFile;
    long annotationTime = 0;
    Chronometer chrono;
    Recording recording;
    TextView tv, recordStopTextView;
    Toolbar mToolbar;
    ArrayList<String> intervieweeDetails, themes, questions;
    ArrayList<Integer> questionsAsked;
    TextInputLayout inputLayoutAddQuestion, inputLayoutAddTheme;
    EditText editTextAddQuestion, editTextAddTheme;
    ListView questionsListView, themesListView;
    Button addAdditionalThemeButton, addAdditionalQuestionButton, viewIntervieweeDetailsButton;
    boolean isRecording = false;
    String projectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialise the required variables created above and get the arrays from the previous
        // activities to add to the recording object.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        Intent intent = getIntent();
        themes = intent.getStringArrayListExtra("themes");
        questions = intent.getStringArrayListExtra("questions");
        questionsAsked = new ArrayList<Integer>();
        intervieweeDetails = intent.getStringArrayListExtra("details");
        projectName = intent.getStringExtra("projectName");

        record = (ImageButton)findViewById(R.id.record);
        stop = (ImageButton)findViewById(R.id.stop);
        addAdditionalQuestionButton = (Button)findViewById(R.id.addAdditionalQuestionButton);
        addAdditionalThemeButton = (Button)findViewById(R.id.addAdditionalThemeButton);
        viewIntervieweeDetailsButton = (Button)findViewById(R.id.viewIntervieweeDetailsButton);
        tv = (TextView)findViewById(R.id.textView);
        recordStopTextView = (TextView)findViewById(R.id.recordStopTextView);

        chrono =  (Chronometer)findViewById(R.id.chronometer);
        questionsListView = (ListView)findViewById(R.id.questionList);
        themesListView = (ListView)findViewById(R.id.themeList);

        stop.setEnabled(false);
        stop.setVisibility(View.INVISIBLE);
        record.setEnabled(true);
        record.setVisibility(View.VISIBLE);

        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Data Capture Tool/projects/" + projectName + "/" + projectName + ".mp3";

        questionsListView.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, questions));
        themesListView.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, themes));

        editTextAddQuestion = (EditText)findViewById(R.id.addQuestionField);
        inputLayoutAddQuestion = (TextInputLayout)findViewById(R.id.input_layout_addQuestion);
        editTextAddTheme = (EditText)findViewById(R.id.addThemeField);
        inputLayoutAddTheme = (TextInputLayout)findViewById(R.id.input_layout_addTheme);

        editTextAddTheme.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            // Add the editor action for the editText
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // If the user presses enter on the keyboard, the theme in the EditText is then
                // entered into the themes array.
                boolean handled = true;
                String themeAdded = "";
                if (actionId == EditorInfo.IME_ACTION_DONE && !editTextAddTheme.getText().toString().trim().isEmpty()) {
                    themeAdded = editTextAddTheme.getText().toString().trim();
                    themes.add(themeAdded);
                    Snackbar.make(coordinatorLayout, "Theme \'" + themeAdded + "\' added.", Snackbar.LENGTH_LONG).show();
                    handled = false;
                }
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                editTextAddTheme.getText().clear();
                editTextAddTheme.clearFocus();
                imm.hideSoftInputFromWindow(editTextAddTheme.getWindowToken(), 0);
                return handled;
            }
        });

        addAdditionalThemeButton.setOnClickListener(new View.OnClickListener() {
            // Set onClick for the add theme button on the interface
            @Override
            public void onClick(View v) {
                // If the user presses the button, the theme in the EditText is then
                // entered into the themes array.
                String themeAdded = "";
                if (!editTextAddTheme.getText().toString().trim().isEmpty()) {
                    themeAdded = editTextAddTheme.getText().toString().trim();
                    themes.add(themeAdded);
                    Snackbar.make(coordinatorLayout, "Theme \'" + themeAdded + "\' added.", Snackbar.LENGTH_LONG).show();
                }
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                editTextAddTheme.getText().clear();
                editTextAddTheme.clearFocus();
                imm.hideSoftInputFromWindow(editTextAddTheme.getWindowToken(), 0);
            }
        });

        editTextAddQuestion.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            // Add the editor action for the editText
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // If the user presses enter on the keyboard, the question in the EditText is then
                // entered into the questions array.
                boolean handled = true;
                String questionAdded = "";
                if (actionId == EditorInfo.IME_ACTION_DONE && !editTextAddQuestion.getText().toString().trim().isEmpty()) {
                    questionAdded = editTextAddQuestion.getText().toString().trim();
                    questions.add(questionAdded);
                    Snackbar.make(coordinatorLayout, "Question \'" + questionAdded + "\' added.", Snackbar.LENGTH_LONG).show();
                    handled = false;
                }
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                editTextAddQuestion.getText().clear();
                editTextAddQuestion.clearFocus();
                imm.hideSoftInputFromWindow(editTextAddQuestion.getWindowToken(), 0);
                return handled;
            }
        });

        addAdditionalQuestionButton.setOnClickListener(new View.OnClickListener() {
            // Set onClick for the add question button on the interface
            @Override
            public void onClick(View v) {
                // If the user presses the button, the question in the EditText is then
                // entered into the questions array.
                String questionAdded = "";
                if (!editTextAddQuestion.getText().toString().trim().isEmpty()) {
                    questionAdded = editTextAddQuestion.getText().toString().trim();
                    questions.add(questionAdded);
                    Snackbar.make(coordinatorLayout, "Question \'" + questionAdded + "\' added.", Snackbar.LENGTH_LONG).show();
                }
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                editTextAddQuestion.getText().clear();
                editTextAddQuestion.clearFocus();
                imm.hideSoftInputFromWindow(editTextAddQuestion.getWindowToken(), 0);
            }
        });

        questionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // Set onClick for each item in the ListView
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                // On the click of an item, the selected question annotation is added to the
                // recording (if the recording has been started) and the question is not able to
                // be asked again (and hence more than one annotation per question cannot be made).
                if (isRecording) {
                    String selectedQuestion = questions.get(position);
                    if (questionsAsked.contains(position))
                        Snackbar.make(coordinatorLayout, "Question \'" + selectedQuestion + "\' already asked. Select another question or theme.", Snackbar.LENGTH_LONG).show();
                    else {
                        view.setBackgroundColor(Color.GRAY);
                        questionsAsked.add(position);
                        annotationTime = SystemClock.elapsedRealtime() - chrono.getBase();
                        recording.addQuestionAnnotation(selectedQuestion, System.currentTimeMillis(), annotationTime);
                        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss:SS");
                        Snackbar.make(coordinatorLayout, "Added annotation for \'" + selectedQuestion + "\' at " + sdf.format(annotationTime), Snackbar.LENGTH_LONG).show();
                    }
                }
                else
                    Snackbar.make(coordinatorLayout, "Cannot annotate - Not recording.", Snackbar.LENGTH_LONG).show();
            }
        });

        themesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // Set onClick for each item in the ListView
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // On the click of an item, the selected theme annotation is added to the recording.
                if (isRecording){
                    String selectedTheme = themes.get(position);//Arrays.asList(themesArray).get(position);
                    annotationTime = SystemClock.elapsedRealtime() - chrono.getBase();
                    recording.addThemeAnnotation(selectedTheme, System.currentTimeMillis(), annotationTime);
                    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss:SS");
                    Snackbar.make(coordinatorLayout, "Added annotation for \'" + selectedTheme + "\' at " + sdf.format(annotationTime), Snackbar.LENGTH_LONG).show();
            }
                else
                    Snackbar.make(coordinatorLayout, "Cannot annotate - Not recording.", Snackbar.LENGTH_LONG).show();
            }
        });

        viewIntervieweeDetailsButton.setOnClickListener(new View.OnClickListener() {
            // Add onClick to the view interviewee details button
            @Override
            public void onClick(View v) {
                // Displays an AlertDialog displaying the interviewee details obtained from
                // the interviewee details array.
                AlertDialog.Builder builder = new AlertDialog.Builder(RecordingActivity.this);
                builder.setTitle("Interviewee Details");
                String intervieweeDetailsString = "";
                for (String detail : intervieweeDetails)
                    intervieweeDetailsString += detail+"\n";
                builder.setMessage(intervieweeDetailsString);
                builder.setNegativeButton("Back to recording", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        record.setOnClickListener(new View.OnClickListener() {
            // Sets the onClick for the record button
            @Override
            public void onClick(View v) {
                // Created the recorder object and sets the path to create the file.
                // The recording is started and the record button is disabled and becomes
                // invisible. The stop button then becomes visible and is enabled.
                String projectFilesPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/Data Capture Tool/projects/";
                File projectFolder = new File(projectFilesPath+projectName);
                projectFolder.mkdirs();

                recorder=new MediaRecorder();
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                recorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                recorder.setAudioEncodingBitRate(256000);
                recorder.setOutputFile(outputFile);
                recording = new Recording(themes, questions, intervieweeDetails, projectName);
                try {
                    recorder.prepare();
                    recorder.start();
                    isRecording = true;
                    chrono.setBase(SystemClock.elapsedRealtime());
                    chrono.start();
                } catch(IllegalStateException e) {
                    e.printStackTrace();
                } catch(IOException e) {
                    e.printStackTrace();
                }
                record.setEnabled(false);
                record.setVisibility(View.INVISIBLE);
                stop.setEnabled(true);
                stop.setVisibility(View.VISIBLE);
                recordStopTextView.setText("STOP");
                Snackbar.make(coordinatorLayout, "Began recording...", Snackbar.LENGTH_LONG).show();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            // Sets the onClick for the stop recording button
            @Override
            public void onClick(View v) {
                // The recorder is stopped and released. The necessary JSON and annotation files
                // are then created and the recording object is passed to the playback activity,
                // which is started at this point.
                recorder.stop();
                isRecording = false;
                chrono.stop();
                recorder.release();
                recorder = null;
                stop.setEnabled(false);

                try {
                    recording.projectDetailsToJSON();
                } catch(IOException e) {
                    e.printStackTrace();
                }
                try {
                    recording.questionsToJSON();
                } catch(IOException e) {
                    e.printStackTrace();
                }
                try {
                    recording.themesToJSON();
                } catch(IOException e) {
                    e.printStackTrace();
                }
                try {
                    recording.exportAnnotations();
                    Snackbar.make(coordinatorLayout, "Annotation list created.", Snackbar.LENGTH_LONG).show();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                Snackbar.make(coordinatorLayout, "Stopped recording.", Snackbar.LENGTH_LONG).show();
                sendMessage(v);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        // Set an alert dialog box prompting the user to confirm if they would like to go back
        // or stay on the currrent page.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Back to themes and questions input");
        builder.setMessage("Are you sure? Going back to the previous page will clear the recently added themes and questions.");
        builder.setPositiveButton("Yes, go back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("No, stay on this page", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void sendMessage (View view) {
        // Send the project name and the recording object to the PlaybackActivity.
        Intent intent = new Intent(this, PlaybackActivity.class);
        Bundle b = new Bundle();
        b.putString("projectName", projectName);
        b.putSerializable("recording", recording);
        intent.putExtras(b);
        startActivity(intent);
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

}