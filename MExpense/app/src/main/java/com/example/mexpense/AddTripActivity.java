package com.example.mexpense;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class AddTripActivity extends AppCompatActivity {

    EditText tripName;
    EditText tripDestination;
    EditText DepartureDate;
    EditText ReturnDate;
    EditText TripDescription;
    RadioGroup riskAssess;
    RadioGroup isOverseas;
    Spinner transportMode;
    int userID;
    int riskAssessId;
    int isOverseasId;
    RadioButton riskAssessInput;
    RadioButton isOverseasInput;
    ImageButton cancelBtn;

    private final Calendar calender = Calendar.getInstance();
    private int calText;
    SimpleDateFormat dateVal;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        dateVal = new SimpleDateFormat("dd/MM/yyyy");
        userID = UserDataDTO.getInstance().getTripDetailsModel().getTripUserID();

        // Initializing objects for 'Add trip'
        tripName = findViewById(R.id.editTextTripName);
        tripDestination = findViewById(R.id.editTextTripDestination);
        DepartureDate = findViewById(R.id.editTextDepartureDate);
        ReturnDate = findViewById(R.id.editTextReturnDate);
        TripDescription = findViewById(R.id.editTextTripDescription);
        riskAssess = findViewById(R.id.radioGroup);
        isOverseas = findViewById(R.id.radioGroup2);
        transportMode = findViewById(R.id.transportSpinner);
        cancelBtn = findViewById(R.id.cancelButton);

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_layout, getResources().getStringArray(R.array.transport_options));
        transportMode.setAdapter(adapter);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calender.set(Calendar.YEAR, year);
                calender.set(Calendar.MONTH,month);
                calender.set(Calendar.DAY_OF_MONTH,day);
                updateText();
            }
        };

        DepartureDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int dialogCount = 0;
                if(dialogCount == 0)
                {
                    new DatePickerDialog(AddTripActivity.this,date, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH)).show();
                    calText = DepartureDate.getId();
                    dialogCount++;
                }
            }
        });
        ReturnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int dialogCount = 0;
                if(dialogCount == 0)
                {
                    new DatePickerDialog(AddTripActivity.this,date, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH)).show();
                    calText = ReturnDate.getId();
                    dialogCount++;
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Cancel();
            }
        });
    }

    public void saveTripDialog(View view)
    {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_message)
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        saveTrip();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }
    public void saveTrip()
    {
        if (!areFieldsEmpty(tripName.getText().toString(),tripDestination.getText().toString(),
                DepartureDate.getText().toString(),ReturnDate.getText().toString()))
        {
            if(dateFormatValidation(DepartureDate.getText().toString()) && dateFormatValidation(ReturnDate.getText().toString()) )
            {
                if (dateTimeValidation(DepartureDate.getText().toString(), ReturnDate.getText().toString()))
                {
                    TripDetailsModel tripDetailsModel;
                    riskAssessId = riskAssess.getCheckedRadioButtonId();
                    isOverseasId = isOverseas.getCheckedRadioButtonId();

                    if (riskAssessId != -1) {
                        riskAssessInput = findViewById(riskAssessId);
                    } else
                        riskAssessInput = null;

                    if (isOverseasId != -1) {
                        isOverseasInput = findViewById(isOverseasId);
                    } else
                        isOverseasInput = null;

                    try {
                        tripDetailsModel = new TripDetailsModel
                                (
                                    userID,
                                    -1,
                                    tripName.getText().toString(),
                                    tripDestination.getText().toString(),
                                    DepartureDate.getText().toString(),
                                    ReturnDate.getText().toString(),
                                    getRiskRadioInput(),
                                    TripDescription.getText().toString(),
                                    getOverseasRadioInput(),
                                    transportMode.getSelectedItem().toString()
                                );

                        DatabaseHelper dataBaseHelper = new DatabaseHelper(AddTripActivity.this, "User_Login.db");

                        boolean success = dataBaseHelper.addTripRecord(tripDetailsModel);
                        dataBaseHelper.close();

                        // Change this to be a dialog inviting the user to add an expense
                        // If not, take them back to the welcome page
                        if (success) {
                            Toast.makeText(getApplicationContext(), R.string.dialog_trip_saved, Toast.LENGTH_SHORT).show();
                            clearText();
                            Cancel();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.dialog_trip_not_saved, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), R.string.error_text + " " , Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(this, R.string.date_invalid, Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(this,R.string.date_format_invalid, Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, R.string.details_invalid, Toast.LENGTH_SHORT).show();
        }
    }

    public void Cancel()
    {
        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(intent);
    }

    private boolean getRiskRadioInput()
    {
        if (riskAssessInput != null) {
            String input = riskAssessInput.getText().toString();

            return input.equals("Yes");
        } else {
            return false;
        }
    }

    private boolean getOverseasRadioInput()
    {
        if (isOverseasInput != null) {
            String input = isOverseasInput.getText().toString();

            return input.equals("Yes");
        } else {
            return false;
        }
    }

    public void clearText()
    {
        tripName.setText("");
        tripDestination.setText("");
        DepartureDate.setText("");
        ReturnDate.setText("");
        riskAssess.clearCheck();
        TripDescription.setText("");
        isOverseas.clearCheck();
        transportMode.setSelection(0);
    }

    public boolean areFieldsEmpty(String string1, String string2, String string3, String string4)
    {
        return string1.isEmpty()||string2.isEmpty()||string3.isEmpty()||string4.isEmpty();
    }

    public boolean dateTimeValidation(String date1, String date2)
    {
        dateVal.setLenient(true);

        boolean value;
        try {
            //If start date is after the end date
            if (Objects.requireNonNull(dateVal.parse(date1)).before(dateVal.parse(date2))) {
                value = true;//If start date is before end date
            } else value = Objects.equals(dateVal.parse(date1), dateVal.parse(date2));//If two dates are equal
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        return value;
    }

    private boolean dateFormatValidation(String dateToValidate) {
        //To make strict date format validation
        dateVal.setLenient(true);
        Date parsedDate = null;
        try {
            parsedDate = dateVal.parse(dateToValidate);
            return true;

        } catch (Exception exception) {
            //Handle exception
        }
        return false;
    }

    private void updateText(){
        String myFormat="dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.UK);
        if (calText == DepartureDate.getId())
        {
            DepartureDate.setText(dateFormat.format(calender.getTime()));
        }
        else
            ReturnDate.setText(dateFormat.format(calender.getTime()));

    }

}