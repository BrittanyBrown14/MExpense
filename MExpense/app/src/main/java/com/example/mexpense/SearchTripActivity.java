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
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.List;

public class SearchTripActivity extends AppCompatActivity {

    ListView tripLV;
    Button deleteAllBtn;
    ImageButton backBtn;
    EditText searchTxt;

    List<TripDetailsModel> allTrips;
    ArrayAdapter arrayAdapter;
    ArrayAdapter lvArray;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_trip);

        // Initializing objects
        deleteAllBtn = findViewById(R.id.deleteAllButton);
        backBtn = findViewById(R.id.backButton);
        tripLV = findViewById(R.id.tripListView);
        searchTxt = findViewById(R.id.searchText);

        databaseHelper = new DatabaseHelper(SearchTripActivity.this, "User_Login.db");
        allTrips = databaseHelper.getAllTrips();
        showAllTrips(allTrips);

        lvArray = new ArrayAdapter(this, R.layout.spinner_layout, allTrips);
        tripLV.setAdapter(lvArray);

        // When an item is clicked, the TripID is gotten and is set as the TripID in the model
        // An alert dialog is shown so the user can choose to edit a trip, delete a trip or see the expenses linked to that trip
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

        // As the user types, the ListView is filtered to match what the user is typing
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

    // The alert dialog where the user can choose to edit a trip, delete a trip or see the trip expenses
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
                .setNegativeButton(R.string.delete_data, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteTrip();
                    }
                })
                .setNeutralButton(R.string.dialog_show_expenses, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showExpenses();
                    }
                }).show();

    }

    // Opens a custom dialog to handle editing the trip
    private void editTrip()
    {
        EditDialogView alert = new EditDialogView();
        alert.showDialog(this, String.valueOf(R.string.dialog_custom_title));

    }

    // Opens a custom dialog to show the user the expenses connected to the trip
    private void showExpenses()
    {
        ExpensesDialogView alert = new ExpensesDialogView();
        alert.showDialog(this, String.valueOf(R.string.dialog_expense_title));
    }

    // Shows a confirmation dialog, confirming that the user wants to delete this trip
    private void deleteTrip()
    {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_confirm_title)
                .setMessage(R.string.delete_confirm_message)
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        databaseHelper.deleteTripRecord();
                        showAllTrips(databaseHelper.getAllTrips());
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    // Shows a confirmation dialog, confirming that the user wants to delete all trips
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

    // Populates the listview and shows the user all the trips
    private void showAllTrips(List<TripDetailsModel> allTrips) {
        arrayAdapter = new ArrayAdapter<TripDetailsModel>(SearchTripActivity.this, R.layout.spinner_layout, allTrips);
        tripLV.setAdapter(arrayAdapter);
    }

    // Takes the user back to the Welcome page
    public void back(View view)
    {
        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(intent);
    }

}