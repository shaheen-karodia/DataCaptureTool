package activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.skoov.datacapturetool.R;
import com.example.skoov.datacapturetool.Recording;

import java.io.IOException;
import java.util.Arrays;

public class PlaybackActivity extends AppCompatActivity {
    /* This activity allows for the playback of the recording which would have been created in
       the previous recording activity. Playback from the list of added annotations is also allowed.
     */

    // Create variables for the various aspects of the UI and the media player and recording
    ImageButton play, stopPlay, playAnnotation, pause;
    Button newInterview;
    MediaPlayer media;
    long annotationTime = 0;
    String[] annotations;
    Recording recording;
    Toolbar mToolbar;
    String projectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialise playback activity with necessary buttons to allow user to playback,
        // stop and pause the recording.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        Intent intent = getIntent();
        recording = (Recording) intent.getSerializableExtra("recording");
        projectName = intent.getStringExtra("projectName");

        play = (ImageButton)findViewById(R.id.play);
        stopPlay = (ImageButton)findViewById(R.id.stopPlay);
        playAnnotation = (ImageButton)findViewById(R.id.playAnnotation);
        pause = (ImageButton)findViewById(R.id.pause);
        newInterview = (Button)findViewById(R.id.newInterview);

        stopPlay.setEnabled(false);
        pause.setEnabled(false);
        pause.setVisibility(View.INVISIBLE);
        stopPlay.setVisibility(View.INVISIBLE);

        Snackbar.make(coordinatorLayout, "Recording, annotation and JSON files have been created.", Snackbar.LENGTH_LONG).show();

        play.setOnClickListener(new View.OnClickListener() {
            // Sets the onClick for the play button
            @Override
            public void onClick(View v) throws IllegalStateException, SecurityException, IllegalStateException {
                // Creates the media player object if not created, and starts it, upon the press of the
                // play button. This button then disappears and is disabled, and the play and pause
                // buttons are enabled.
                play.setEnabled(false);
                play.setVisibility(View.INVISIBLE);
                pause.setEnabled(true);
                pause.setVisibility(View.VISIBLE);
                stopPlay.setEnabled(true);
                stopPlay.setVisibility(View.VISIBLE);
                newInterview.setEnabled(false);
                if (media==null) {
                    media = new MediaPlayer();
                    try {
                        media.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath()
                                + "/Data Capture Tool/projects/" + projectName + "/" + projectName + ".mp3");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        media.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                media.start();
                Snackbar.make(coordinatorLayout, "Playing back audio...", Snackbar.LENGTH_LONG).show();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (media!=null) {
                    media.pause();
                }
                play.setEnabled(true);
                play.setVisibility(View.VISIBLE);
                stopPlay.setEnabled(true);
                stopPlay.setVisibility(View.VISIBLE);
                pause.setEnabled(false);
                pause.setVisibility(View.INVISIBLE);
                newInterview.setEnabled(true);
                Snackbar.make(coordinatorLayout, "Paused audio playback.", Snackbar.LENGTH_LONG).show();
            }
        });

        stopPlay.setOnClickListener(new View.OnClickListener() {
            // Sets the onClick for the stop button.
            @Override
            public void onClick(View v) {
                // Stops the media player object, and disables and makes the stop and pause buttons
                // invisible. The play button becomes visible and enabled again.
                if (media!=null) {
                    media.stop();
                    media.release();
                    media = null;
                }
                play.setEnabled(true);
                play.setVisibility(View.VISIBLE);
                stopPlay.setEnabled(false);
                stopPlay.setVisibility(View.INVISIBLE);
                pause.setEnabled(false);
                pause.setVisibility(View.INVISIBLE);
                newInterview.setEnabled(true);
                if (annotationTime!=0)
                    playAnnotation.setEnabled(true);
                Snackbar.make(coordinatorLayout, "Stopped audio playback.", Snackbar.LENGTH_LONG).show();
            }
        });

        playAnnotation.setOnClickListener(new View.OnClickListener() {
            // Sets the onClick for the playAnnotation button.
            @Override
            public void onClick(View v) {
                // Upon the press of this button, an alert dialog is created and shows a list of the
                // annotations made to the recording. Upon selecting an annotation, the media player
                // is set to play at the respective time of the annotation.
                if (recording.getAnnotationList().length == 0) {
                    Snackbar.make(coordinatorLayout, "No annotations made to this recording.", Snackbar.LENGTH_LONG).show();
                }
                else {
                    AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(PlaybackActivity.this);
                    annotations = recording.getAnnotationList();
                    alertdialogbuilder.setTitle("Select an annotation: ");
                    AlertDialog.Builder builder = alertdialogbuilder.setItems(annotations, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String selectedAnnotation = Arrays.asList(annotations).get(which);
                            if (media == null) {
                                media = new MediaPlayer();
                                try {
                                    media.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath()
                                            + "/Data Capture Tool/projects/" + projectName + "/" + projectName + ".mp3");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    media.prepare();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            String annotationStringAndTime[] = selectedAnnotation.split(" ~ ");
                            String timeArray[] = annotationStringAndTime[1].trim().split(":");
                            int time = Integer.parseInt(timeArray[0])*60000 + Integer.parseInt(timeArray[1])*1000 + Integer.parseInt(timeArray[2]);
                            media.seekTo((int)time);
                            media.start();
                            Snackbar.make(coordinatorLayout, "Playing back marked annotation \'" + annotationStringAndTime[0] +"\' at " + annotationStringAndTime[1] + " min...", Snackbar.LENGTH_LONG).show();
                            stopPlay.setEnabled(true);
                        }
                    });
                    AlertDialog dialog = alertdialogbuilder.create();
                    dialog.show();
                }
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

    public void sendMessage(View v) {
        // For the onClick of the new interview project button.
        // Stack of activities is cleared and a new task for the introPage activity is created.
        Intent intent = new Intent(this, IntroPageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // Set an alert dialog box prompting the user to confirm if they would like to go back
        // or stay on the currrent page.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you would like to exit?");
        builder.setPositiveButton("Yes, exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), IntroPageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Exit me", true);
                startActivity(intent);
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

}