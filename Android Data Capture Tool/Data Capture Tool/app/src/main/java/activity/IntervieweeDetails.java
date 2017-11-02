package activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.skoov.datacapturetool.R;

import java.util.ArrayList;

public class IntervieweeDetails extends AppCompatActivity {
    /* This activity is responsible for taking in the input details of the interviewee, from the user,
    *  and stores these in an array which is carried through to the following activities for use
    *  in the recording object. */

    // Create variables for various aspects of UI
    EditText name, surname, additionalDetail, additionalDetailValue;
    TextInputLayout inputLayoutName, inputLayoutSurname, inputLayoutAdditional, inputLayoutValue;
    ArrayList <String> intervieweeDetails;
    CoordinatorLayout coordinatorLayout;
    Button addNames, addAdditional, noDetailsAddedText;
    Toolbar mToolbar;
    String projectName;
    boolean namesAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialise activity with relevant textboxes and buttons for user input
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interviewee_details);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        name = (EditText) findViewById(R.id.nameField);
        inputLayoutName = (TextInputLayout)findViewById(R.id.input_layout_name);
        surname = (EditText) findViewById(R.id.surnameField);
        inputLayoutSurname = (TextInputLayout) findViewById(R.id.input_layout_surname);
        //Name of the additionalDetail additionalValueDetail eg DateOfBirth, LevelofEducation etc
        additionalDetail = (EditText) findViewById(R.id.detail);
        inputLayoutAdditional = (TextInputLayout) findViewById(R.id.input_layout_additional);
        //Corresponding value eg. 12 Aug 1994, High School etc
        additionalDetailValue = (EditText) findViewById(R.id.detailValue);
        inputLayoutValue = (TextInputLayout) findViewById(R.id.input_layout_value);

        addNames = (Button)findViewById(R.id.addNamesButton);
        addAdditional = (Button)findViewById(R.id.addAdditionalButton);
        noDetailsAddedText = (Button)findViewById(R.id.noDetailsAdded);

        Intent intent = getIntent();
        projectName = intent.getStringExtra("projectName");

        name.addTextChangedListener(new MyTextWatcher(name));
        surname.addTextChangedListener(new MyTextWatcher(surname));
        additionalDetail.addTextChangedListener(new MyTextWatcher(additionalDetail));
        additionalDetailValue.addTextChangedListener(new MyTextWatcher(additionalDetailValue));
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

    public void goToThemesInput(View view) {
        // Send details of interview to next activity
        if (validateName() && namesAdded) {
            Intent intent = new Intent(this, InputActivity.class);
            Bundle b = new Bundle();
            b.putStringArrayList("details", intervieweeDetails);
            b.putString("projectName", projectName);
            intent.putExtras(b);
            startActivity(intent);
        }
        else if (validateName()) {
            Snackbar.make(coordinatorLayout, "Name and surname not yet added to interviewee detail list.", Snackbar.LENGTH_LONG).show();
        }
    }

    public void addAdditionalDetail(View view) {
        // Add additionalDetail detail to detail array upon button press
        if (validateValue()) {
            if (intervieweeDetails == null)
                intervieweeDetails = new ArrayList<String>();
            intervieweeDetails.add(additionalDetail.getText().toString().trim() + ": " +
                    additionalDetailValue.getText().toString().trim());
            Snackbar.make(coordinatorLayout, "Additional detail " + additionalDetail.getText().toString().trim() + ": " +
                    additionalDetailValue.getText().toString().trim() + " added to interviewee details.", Snackbar.LENGTH_LONG).show();
            additionalDetailValue.setText("");
            additionalDetail.setText("");
            additionalDetail.setFocusableInTouchMode(true);
            additionalDetail.setFocusable(true);
            additionalDetail.requestFocus();
            if (intervieweeDetails.size()==1)
                noDetailsAddedText.setText("1 detail added");
            else
                noDetailsAddedText.setText(intervieweeDetails.size()+" details added");

        }
    }

    public void addNameAndSurname(View view) {
        // Add name and surname to beginning of interviewees details array
        if (validateName()) {
            if (intervieweeDetails == null)
                intervieweeDetails = new ArrayList<String>();
            intervieweeDetails.add(0, "Name: " + name.getText().toString().trim());
            intervieweeDetails.add(1, "Surname: " + surname.getText().toString().trim());
            noDetailsAddedText.setText(intervieweeDetails.size()+" details added");
            Snackbar.make(coordinatorLayout, "Name and surname added to interviewee details.",
                    Snackbar.LENGTH_LONG).show();
            inputLayoutName.setErrorEnabled(false);
            name.setEnabled(false);
            name.clearFocus();
            inputLayoutSurname.setErrorEnabled(false);
            surname.setEnabled(false);
            surname.clearFocus();
            addNames.setEnabled(false);
            addNames.setClickable(false);
            addNames.setText("Name and surname added");
            namesAdded = true;
        }
    }

    private class MyTextWatcher implements TextWatcher {
        // Class to watch textboxes and monitor changes to notify for errors on input
        private View view;
        private MyTextWatcher(View view) {
            this.view = view;
        }
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        public void afterTextChanged(Editable editable) {
            // For running respective monitoring methods for textboxes
            switch (view.getId()) {
                case R.id.nameField:
                    validateName();
                    break;
                case R.id.surnameField:
                    validateSurname();
                    break;
                case R.id.detailValue:
                    validateValue();
                    break;
                case R.id.detail:
                    validateValue();
                    break;
            }
        }
    }

    private void requestFocus(View view) {
        // Used for requesting focus (opening keyboard) on textboxes when necessary
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateName() {
        // Monitor name textbox field
        if (name.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError("No name entered");
            requestFocus(name);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateSurname() {
        // Monitor surname textbox field
        if (surname.getText().toString().trim().isEmpty()) {
            inputLayoutSurname.setError("No surname entered");
            requestFocus(surname);
            return false;
        } else {
            inputLayoutSurname.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateValue() {
        // Monitor additionalDetail detail and value field
        if (additionalDetail.getText().toString().trim().isEmpty()) {
            if (additionalDetailValue.getText().toString().trim().isEmpty()) {
                inputLayoutAdditional.setError("No additional detail entered");
                inputLayoutValue.setError("No value for additional detail entered");
                requestFocus(additionalDetail);
                return false;
            } else if (!additionalDetailValue.getText().toString().trim().isEmpty()) {
                inputLayoutAdditional.setError("No additional detail entered");
                inputLayoutValue.setErrorEnabled(false);
                return false;
            }
        }
        else if (!additionalDetail.getText().toString().trim().isEmpty()) {
            if (additionalDetailValue.getText().toString().trim().isEmpty()) {
                inputLayoutValue.setError("No value for additional detail entered");
                inputLayoutAdditional.setErrorEnabled(false);
                return false;
            } else {
                inputLayoutValue.setErrorEnabled(false);
                inputLayoutAdditional.setErrorEnabled(false);
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        // Dialog alert which prompts the user for confirmation to move back to the previous page
        // in the application, upon the press of the back button.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Back to introductory page");
        builder.setMessage("Are you sure? Going back to the introductory page will clear the " +
                "added interviewee details, themes and questions for this project.");
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

}