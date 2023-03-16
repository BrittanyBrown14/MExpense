package com.example.mexpense;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {
    Spinner expenseSpinner;
    EditText otherType;
    EditText expenseCurrency;
    EditText expenseAmount;
    EditText expenseDate;
    TextView expenseTime;
    EditText expenseComments;
    EditText expenseInputID;
    ImageButton cancelBtn;

    int tripID;
    int TempId;

    private final Calendar calender = Calendar.getInstance();
    private SimpleDateFormat currentTime;

    DatabaseHelper databaseHelper;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        // Object Initialisation
        databaseHelper = new DatabaseHelper(AddExpenseActivity.this, "User_Login.db");

        expenseSpinner = findViewById(R.id.expenseTripSpinner);
        otherType = findViewById(R.id.expenseOtherText);
        expenseCurrency = findViewById(R.id.expenseCurrencyText);
        expenseAmount = findViewById(R.id.expenseAmountText);
        expenseDate = findViewById(R.id.expenseDateText);
        expenseTime = findViewById(R.id.expenseTimeText);
        expenseComments = findViewById(R.id.expenseCommentsText);
        expenseInputID = findViewById(R.id.expenseInputIDText);
        cancelBtn = findViewById(R.id.cancelButton);

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_layout, getResources().getStringArray(R.array.expense_types));
        expenseSpinner.setAdapter(adapter);

        // Checks the spinner value to change the EditText visibility
        expenseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String spinnerItem = expenseSpinner.getSelectedItem().toString();
                if (spinnerItem.equals("Other"))
                {
                    otherType.setVisibility(View.VISIBLE);
                }
                else
                    otherType.setVisibility(View.GONE);
                }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Sets the date picked when the dialog is created to today's date
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calender.set(Calendar.YEAR, year);
                calender.set(Calendar.MONTH,month);
                calender.set(Calendar.DAY_OF_MONTH,day);
                updateText();
            }
        };

        // Closes the dialog
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Cancel();
            }
        });

        // When the EditText is clicked, the calender dialog is shown and the date can be chosen using that.
        expenseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int dialogCount = 0;
                if(dialogCount == 0)
                {
                    new DatePickerDialog(AddExpenseActivity.this,date,
                            calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH)).show();
                    dialogCount++;
                }
            }
        });

        // Setting the Expense time to the current time
        currentTime = new SimpleDateFormat("HH:mm");
        expenseTime.setText(getResources().getString(R.string.expenses_time) + " " +  currentTime.format(new Date()));
    }

    // Save confirmation dialog
    public void saveExpenseDialog(View view)
    {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_message)
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        saveExpense();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void saveExpense()
    {
        // Various validations
        String spinnerItem = expenseSpinner.getSelectedItem().toString();
        if (!areFieldsEmpty(expenseInputID.getText().toString(), expenseCurrency.getText().toString(), expenseAmount.getText().toString(), expenseDate.getText().toString())
               && !spinnerItem.equals("---"))
        {
            if (dateFormatValidation(expenseDate.getText().toString()))
            {
                if(dateTimeValidation(expenseDate.getText().toString()))
                {
                    if (otherType.getVisibility() == View.VISIBLE && otherType.getText().toString().isEmpty())
                    {
                        Toast.makeText(this, R.string.details_invalid, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        // Checks if the trip ID is in the database
                        ExpensesModel expensesModel;
                        if (getExpenseTripID(Integer.parseInt(expenseInputID.getText().toString())))
                        {
                            double money = Double.parseDouble(expenseAmount.getText().toString());
                            tripID = UserDataDTO.getInstance().getExpensesModel().getExpenseTripID();

                            //Adds all the information as new model object
                            try
                            {
                                expensesModel = new ExpensesModel
                                    (
                                        tripID,
                                        -1,
                                        expenseCurrency.getText().toString(),
                                        expenseSpinner.getSelectedItem().toString(),
                                        otherType.getText().toString(),
                                        money,
                                        expenseDate.getText().toString(),
                                        currentTime.format(new Date()),
                                        expenseComments.getText().toString()
                                    );
                                try
                                {
                                    //Adds all the information to the database
                                    boolean success = databaseHelper.addExpenseRecord(expensesModel);
                                    databaseHelper.close();
                                    if (success) {
                                        Toast.makeText(getApplicationContext(), R.string.dialog_expense_saved, Toast.LENGTH_SHORT).show();
                                        clearText();
                                        Cancel();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), R.string.dialog_expense_not_saved, Toast.LENGTH_SHORT).show();
                                    }
                                }
                                catch (Exception ex)
                                {
                                    throw new RuntimeException(ex);
                                }
                            }
                            catch (Exception ex) {
                                Toast.makeText(getApplicationContext(), R.string.error_text + "" + ex, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(AddExpenseActivity.this,R.string.dialog_trip_null,Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(this, R.string.date_current_invalid, Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(this, R.string.date_format_invalid, Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, R.string.details_invalid, Toast.LENGTH_SHORT).show();
        }

    }

    // Takes the user to the Welcome page
    public void Cancel()
    {
        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(intent);
    }

    // Clears all the text from the field
    public void clearText()
    {
        expenseSpinner.setSelection(0);
        otherType.setText("");
        expenseInputID.setText("");
        expenseCurrency.setText("");
        expenseAmount.setText("");
        expenseDate.setText("");
        expenseTime.setText("");
        expenseComments.setText("");
    }

    // Checks if all the strings are empty and Returns the value as a Boolean
    public boolean areFieldsEmpty(String string1, String string2, String string3, String string4)
    {
        return string1.isEmpty()||string2.isEmpty()||string3.isEmpty()||string4.isEmpty();
    }

    // Checks the format of the date that is inputted
    public boolean dateFormatValidation(String dateToValidate) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        //To make strict date format validation
        formatter.setLenient(true);
        Date parsedDate = null;
        try {
            parsedDate = formatter.parse(dateToValidate);
            return true;

        } catch (Exception exception) {

        }
        return false;
    }

    // Checks if the start date is after the end date
    private boolean dateTimeValidation(String date1)
    {
        SimpleDateFormat dateVal = new SimpleDateFormat("dd/MM/yyyy");
        dateVal.setLenient(true);

        boolean value = false;
        try {
            value = dateVal.parse(date1).before(new Date());
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        return value;
    }

    // Updates the EditText to match the date chosen on the calender dialog
    private void updateText()
    {
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
        expenseDate.setText(dateFormat.format(calender.getTime()));
    }

    // Gets the expense trip ID from the db to check against the ID inputted by the user
    public boolean getExpenseTripID(int id)
    {
        // Opening SQLite database write permission.
        SQLiteDatabase sqlDB = databaseHelper.getWritableDatabase();
        // Adding search email query to cursor.
        Cursor cursor = sqlDB.query(DatabaseHelper.TRIP_DETAILS, null, " " + DatabaseHelper.COLUMN_TRIP_ID + "=?", new String[]{expenseInputID.getText().toString()}, null, null, null);
        while (cursor.moveToNext()) {
            if (cursor.isFirst()) {
                cursor.moveToFirst();
                // Stores allTrips user info with entered email.
                TempId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRIP_ID));
                // Closing cursor.
                //getExpenseTripID(Integer.parseInt(expenseInputID.getText().toString()));
                cursor.close();
            }
        }
        if(TempId == id)
        {
            UserDataDTO.getInstance().getExpensesModel().setExpenseTripID(TempId);
            return true;
        }
        else {
            return false;
        }
    }
}