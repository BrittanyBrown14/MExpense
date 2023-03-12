package com.example.mexpense;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class SearchTripActivity extends AppCompatActivity {

    ListView tripLV;
    Button deleteAllBtn;
    Button backBtn;
    EditText searchTxt;

    List<TripDetailsModel> allTrips;
    ArrayAdapter arrayAdapter;
    ArrayAdapter lvArray;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        String currentIntent = extras.getString("tripLayout");

        try {
            if (currentIntent.equals("trip_search_layout")) {
                setContentView(R.layout.activity_search);
            } else {
                Toast.makeText(this, "error:" + currentIntent, Toast.LENGTH_LONG).show();

            }
        } catch (Exception ex) {
            Toast.makeText(this, "error:" + ex, Toast.LENGTH_LONG).show();
        }

        deleteAllBtn = findViewById(R.id.deleteAllButton);
        backBtn = findViewById(R.id.backButton);
        tripLV = findViewById(R.id.tripListView);
        searchTxt = findViewById(R.id.searchText);

        databaseHelper = new DatabaseHelper(SearchTripActivity.this, "User_Login.db");
        allTrips = databaseHelper.getAllTrips();
        showAllTrips(allTrips);

        lvArray = new ArrayAdapter(this, R.layout.spinner_layout, allTrips);
        tripLV.setAdapter(lvArray);
        tripLV.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int listID = allTrips.get(position).getTripID();
                UserDataDTO.getInstance().getTripDetailsModel().setTripID(listID);
                EditOrDelete();
                showAllTrips(allTrips);
            }
        });

        searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                SearchTripActivity.this.lvArray.getFilter().filter(charSequence);
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void EditOrDelete ()
    {
        new AlertDialog.Builder(this)
                .setTitle(R.string.data_change_title)
                .setMessage(R.string.data_change_message)
                .setPositiveButton(R.string.edit_data, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        editTrip();
                    }
                })
                .setNeutralButton(R.string.delete_data, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteTrip();
                    }
                })
                .setNeutralButton("CHANGE TEXT TO 'SHOW EXPENSES'", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showExpenses();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void editTrip()
    {
        EditDialogView alert = new EditDialogView();
        alert.showDialog(this, String.valueOf("CHANGE!!!!!!!"));

    }
    private void showExpenses()
    {
        ExpensesDialogView alert = new ExpensesDialogView();
        alert.showDialog(this, String.valueOf(R.string.dialog_custom_title));
    }
    private void deleteTrip()
    {
        databaseHelper.deleteTripRecord();
        showAllTrips(databaseHelper.getAllTrips());
    }

    public void deleteAllTrips(View view)
    {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_all_confirm_title)
                .setMessage(R.string.delete_all_confirm_message)
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        databaseHelper.deleteAllTripRecord();
                        showAllTrips(databaseHelper.getAllTrips());
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void showAllTrips(List<TripDetailsModel> allTrips) {
        arrayAdapter = new ArrayAdapter<TripDetailsModel>(SearchTripActivity.this, R.layout.spinner_layout, allTrips);
        tripLV.setAdapter(arrayAdapter);
    }

    public void back(View view)
    {
        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(intent);
    }

}