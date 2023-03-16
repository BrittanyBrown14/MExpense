package com.example.mexpense;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {

    Button addTripBtn;
    Button addExpenseBtn;
    Button searchTripBtn;
    TextView textView;

    String FirstName, LastName;
    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Object initialization
        addTripBtn = findViewById(R.id.newTripButton);
        addExpenseBtn = findViewById(R.id.newExpenseButton);
        searchTripBtn = findViewById(R.id.searchTripButton);
        textView =  findViewById(R.id.textView);

        FirstName = UserDataDTO.getInstance().getUserLoginModel().getFirstName();
        LastName = UserDataDTO.getInstance().getUserLoginModel().getLastName();
        userID = UserDataDTO.getInstance().getUserLoginModel().getID();

        textView.setText(getString(R.string.welcome_page_name) + " \n" + FirstName + " " + LastName + "\n Your ID is: " + userID);

    }
    public void goToPage(View view)
    {
        try
        {
            // Depending on which button is pressed, it will take the user to the corresponding page and set the
            // the current userID as the TripUserID
            switch (view.getId())
            {
                case R.id.newTripButton:

                    Intent intentTripAdd = new Intent(getApplicationContext(), AddTripActivity.class);
                    UserDataDTO.getInstance().getTripDetailsModel().setTripUserID(userID);
                    startActivity(intentTripAdd);
                    break;

                case R.id.searchTripButton:
                    Intent intentTripSearch = new Intent(getApplicationContext(), SearchTripActivity.class);
                    UserDataDTO.getInstance().getTripDetailsModel().setTripUserID(userID);
                    startActivity(intentTripSearch);
                break;

                case R.id.newExpenseButton:
                    Intent intentAddSearch = new Intent(getApplicationContext(), AddExpenseActivity.class);
                    UserDataDTO.getInstance().getTripDetailsModel().setTripUserID(userID);
                    startActivity(intentAddSearch);
                break;
            }
        }
        catch (Exception ex)
        {
          Toast.makeText(this, R.string.error_text + " " + ex, Toast.LENGTH_LONG).show();
        }
    }
}