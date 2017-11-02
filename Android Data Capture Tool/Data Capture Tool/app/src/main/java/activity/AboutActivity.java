package activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.example.skoov.datacapturetool.R;

public class AboutActivity extends AppCompatActivity {
    /*
        Simple activity to show about details of the app in a TextView.3
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView tv = (TextView)findViewById(R.id.aboutTextView);
        tv.setText("Created by: \n\t\t\t Shaheen Karodia," +
                "\n\t\t\t Shaheel Kooverjee \n\t\t\t James Lloyd (Project Leader) \n\n\n" +
                "Capstone Project for CSC3003SS at the University of Cape Town, September 2016.");

    }
}
