package com.example.mexpense;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
    Button cancelBtn;

    int tripID;
    int Tempid;

    private Calendar calender = Calendar.getInstance();
    private Cursor cursor;

    DatabaseHelper databaseHelper;
    SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        tripID = UserDataDTO.getInstance().getTripDetailsModel().getTripID();

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
        expenseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String spinnerItem = expenseSpinner.getSelectedItem().toString();
                if (spinnerItem == "Other")
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

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calender.set(Calendar.YEAR, year);
                calender.set(Calendar.MONTH,month);
                calender.set(Calendar.DAY_OF_MONTH,day);
                updateText();
            }
        };

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Cancel();
            }
        });

        expenseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                new DatePickerDialog(AddExpenseActivity.this,date,
                        calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        expenseTime.setText(timeFormat.format(new Date()));
    }

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
        if (!areFieldsEmpty(expenseInputID.getText().toString(), expenseAmount.getText().toString(), expenseDate.getText().toString(), expenseTime.getText().toString()))
        {
            if (dateFormatValidation(expenseDate.getText().toString()))
            {
                if (otherType.getVisibility() == View.VISIBLE && otherType.getText().toString().isEmpty())
                {
                    Toast.makeText(this, "Please enter all the required details. ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ExpensesModel expensesModel;
                    getTripID(Integer.parseInt(expenseInputID.getText().toString()));

                    try {
                        expensesModel = new ExpensesModel
                        (
                            tripID,
                            -1,
                            expenseCurrency.getText().toString(),
                            expenseSpinner.getSelectedItem().toString(),
                            otherType.getText().toString(),
                            Integer.parseInt(expenseAmount.getText().toString()),
                            expenseDate.getText().toString(),
                            expenseTime.getText().toString(),
                            expenseComments.getText().toString()
                        );

                        try
                        {
                            // Opening SQLite database write permission.
                            sqlDB = databaseHelper.getWritableDatabase();
                            // Adding search email query to cursor.
                            cursor = sqlDB.query(DatabaseHelper.EXPENSE_TABLE, null, " " + DatabaseHelper.COLUMN_EXPENSE_TRIP_ID + "=?",new String[]{String.valueOf(expenseInputID)} , null, null, null );
                            while (cursor.moveToNext()) {
                                if (cursor.isFirst()) {
                                    cursor.moveToFirst();
                                    // Stores allTrips user info with entered email.
                                    Tempid = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EXPENSE_TRIP_ID));
                                    // Closing cursor.

                                    cursor.close();
                                }
                            }
                            // Calling method to check final result ..

                            if(getTripID(Integer.parseInt(expenseInputID.getText().toString())))
                            {
                                DatabaseHelper dataBaseHelper = new DatabaseHelper(AddExpenseActivity.this, "User_Login.db");

                                boolean success = dataBaseHelper.addExpenseRecord(expensesModel);
                                dataBaseHelper.close();
                                if (success == true) {
                                    Toast.makeText(getApplicationContext(), "Your trip was saved ", Toast.LENGTH_SHORT).show();
                                    clearText();
                                    Cancel();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Your details were not saved. Please try again ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        catch (Exception ex)
                        {
                            throw new RuntimeException(ex);
                        }
                    }
                    catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), "error:" + ex, Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else
            {
                Toast.makeText(this, "The date is not valid. Please try again. \n Use this format: dd/MM/yyyy", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "Please enter all the required details. ", Toast.LENGTH_SHORT).show();
        }

    }

    public void Cancel()
    {
        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(intent);
    }

    public void clearText()
    {
        expenseSpinner.setSelection(0);
        otherType.setText("");
        expenseAmount.setText("");
        expenseDate.setText("");
        expenseTime.setText("");
        expenseComments.setText("");
    }

    public boolean areFieldsEmpty(String string1, String string2, String string3, String string4)
    {
        return string1.isEmpty()||string2.isEmpty()||string3.isEmpty() ||string4.isEmpty();
    }

    public boolean dateFormatValidation(String dateToValidate) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        //To make strict date format validation
        formatter.setLenient(true);
        Date parsedDate = null;
        try {
            parsedDate = formatter.parse(dateToValidate);
            return true;

        } catch (Exception exception) {
            //Handle exception
        }
        return false;
    }

    private void updateText(){
        String myFormat="dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.UK);
        expenseDate.setText(dateFormat.format(calender.getTime()));

    }

    public boolean getTripID(int id)
    {
        if(Tempid == id)
        {
            UserDataDTO.getInstance().getExpensesModel().setExpenseTripID(tripID);
            return true;
        }
        else {
            Toast.makeText(AddExpenseActivity.this,"There is no trip with this name.\n Please Try Again.",Toast.LENGTH_LONG).show();
            return false;
        }
    }

}