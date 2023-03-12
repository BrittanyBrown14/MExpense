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
    Button searchExpenseBtn;
    ImageButton settingBtn;
    TextView textView;

    String FirstName, LastName;
    int userID;

    static String tempLayout = null;
    TripDetailsModel tripDetailsModel;
;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        tripDetailsModel = new TripDetailsModel();

        addTripBtn = findViewById(R.id.newTripButton);
        addExpenseBtn = findViewById(R.id.newExpenseButton);
        searchTripBtn = findViewById(R.id.searchTripButton);
        searchExpenseBtn = findViewById(R.id.searchExpenseButton);
        settingBtn = findViewById(R.id.settingButton);
        textView =  findViewById(R.id.textView);

        FirstName = UserDataDTO.getInstance().getUserLoginModel().getFirstName();
        LastName = UserDataDTO.getInstance().getUserLoginModel().getLastName();
        userID = UserDataDTO.getInstance().getUserLoginModel().getID();

        textView.setText(getString(R.string.welcome_page_name) + " " + FirstName + " " + LastName + "\n Your ID is: " + userID);

    }
    public void goToPage(View view)
    {
        try
        {
            switch (view.getId())
            {
                case R.id.newTripButton:
                    // do something
                    tempLayout = "trip_add_layout";

                    Intent intentTripAdd = new Intent(getApplicationContext(), AddTripActivity.class);
                    intentTripAdd.putExtra("tripLayout", tempLayout);

                    UserDataDTO.getInstance().getTripDetailsModel().setTripUserID(userID);
                    startActivity(intentTripAdd);
                    break;

                case R.id.searchTripButton:
                    tempLayout = "trip_search_layout";

                    Intent intentTripSearch = new Intent(getApplicationContext(), SearchTripActivity.class);
                    intentTripSearch.putExtra("tripLayout", tempLayout);

                    UserDataDTO.getInstance().getTripDetailsModel().setTripUserID(userID);
                    startActivity(intentTripSearch);
                break;

                case R.id.newExpenseButton:
                    Intent intentAddSearch = new Intent(getApplicationContext(), AddExpenseActivity.class);

                    UserDataDTO.getInstance().getTripDetailsModel().setTripUserID(userID);
                    startActivity(intentAddSearch);
                break;

                case R.id.searchExpenseButton:

/*                    Intent intentEnpenseSearch = new Intent(getApplicationContext(), SearchTripActivity.class);
                    intentEnpenseSearch.putExtra("tripLayout", tempLayout);

                    UserDataDTO.getInstance().getTripDetailsModel().setTripUserID(userID);
                    startActivity(intentEnpenseSearch);*/
                break;
            }
        }
        catch (Exception ex)
        {
          Toast.makeText(this, "error:" + ex, Toast.LENGTH_LONG).show();
        }
    }
}