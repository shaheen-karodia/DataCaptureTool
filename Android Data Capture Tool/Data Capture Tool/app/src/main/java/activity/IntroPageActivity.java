package activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.skoov.datacapturetool.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IntroPageActivity extends AppCompatActivity {
    /* Introductory activity which houses an EditText requiring the user to
        enter the desired name of the project to be created.
     */

    // Create variables for various aspects of UI
    Button proceedButton;
    TextInputLayout textInputLayoutProjectName;
    EditText projectNameTextbox;
    Toolbar mToolbar;
    String projectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialise intro page activity with textbox for project name input and
        // button to proceed to next page
        super.onCreate(savedInstanceState);

        if (getIntent().getBooleanExtra("Exit me", false)) {
            finish();
            return;
        }

        setContentView(R.layout.activity_intro_page);
        proceedButton = (Button) findViewById(R.id.proceedButton);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        textInputLayoutProjectName = (TextInputLayout) findViewById(R.id.input_layout_projectName);
        projectNameTextbox = (EditText) findViewById(R.id.addProjectNameField);
        TextView textView = (TextView)findViewById(R.id.textView);
        final Typeface buttonFont = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Medium.ttf");
        proceedButton.setTypeface(buttonFont);
        final Typeface textViewFont = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Light.ttf");
        textView.setTypeface(textViewFont);
        proceedButton.setTypeface(buttonFont);

    }

    public void goToIntervieweeInput (View view) {
        // Checks if user has entered project name in textbox.
        // If there is no name entered, the project is saved with the date and time
        // as the project name. Else, if the user has entered a name, it is checked if a project
        // with this name already exists, in which case the user is prompted to enter another name,
        // otherwise the project name is saved as the provided name.
        String projectFilesPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Data Capture Tool/projects/";
        if (projectNameTextbox.getText().toString().trim().isEmpty()) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy__HH_mm_ss");
            Date now = new Date();
            projectName = formatter.format(now);
            Intent intent = new Intent(this, IntervieweeDetails.class);
            intent.putExtra("projectName", projectName);
            startActivity(intent);
        }
        else {
            projectName = projectNameTextbox.getText().toString().trim().toString();
            File projectFolder = new File(projectFilesPath+projectName);
            if (projectFolder.exists()) {
                textInputLayoutProjectName.setError("Project already exists; Enter another project name.");
            }
            else {
                Intent intent = new Intent(this, IntervieweeDetails.class);
                intent.putExtra("projectName", projectName);
                startActivity(intent);
            }
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
}
