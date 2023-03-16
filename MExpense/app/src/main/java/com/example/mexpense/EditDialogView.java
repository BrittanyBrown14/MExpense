package com.example.mexpense;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditDialogView {
    private String dbColumn;
    private String infoChange;
    private String editOption;

    Spinner inputSpinner;
    RadioGroup dialogRG;
    RadioButton dialogRB;
    EditText editInput;
    Spinner spinner;
    private Dialog dialog;
    private final Calendar calender = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    private boolean isSuccess;

    public void showDialog(Activity activity, String msg){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_edit_dialog);

        editInput = dialog.findViewById(R.id.editTextInput);
        dialogRG = dialog.findViewById(R.id.dialogRadioGroup);
        inputSpinner = dialog.findViewById(R.id.dialogInputSpinner);

        ImageButton cancelBtn = dialog.findViewById(R.id.cancel_Btn_dialog);
        editInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String text  = spinner.getSelectedItem().toString();
                if (text.equals("Return Date")|| text.equals("Departure Date"))
                {
                    int dialogCount = 0;
                    if(dialogCount == 0)
                    {
                        new DatePickerDialog(dialog.getContext(),date, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH)).show();
                        dialogCount++;
                    }
                }
            }
        });
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calender.set(Calendar.YEAR, year);
                calender.set(Calendar.MONTH,month);
                calender.set(Calendar.DAY_OF_MONTH,day);

                String myFormat="dd/MM/yyyy";
                SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.UK);
                editInput.setText(dateFormat.format(calender.getTime()));

            }
        };
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

        spinner = dialog.findViewById(R.id.dialog_spinner);
        ArrayAdapter spinnerAdapter = new ArrayAdapter(dialog.getContext(), R.layout.spinner_layout, dialog.getContext().getResources().getStringArray(R.array.edit_trip_options));
        spinner.setAdapter(spinnerAdapter);

        Spinner transportSpinner = dialog.findViewById(R.id.dialogInputSpinner);
        ArrayAdapter spinnerTransportAdapter = new ArrayAdapter(dialog.getContext(), R.layout.spinner_layout, dialog.getContext().getResources().getStringArray(R.array.transport_options));
        transportSpinner.setAdapter(spinnerTransportAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("ResourceAsColor")
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                editOption = spinner.getSelectedItem().toString();
                switch(editOption)
                {
                    case "Trip Name":
                        dbColumn = "TripName";
                        editInput.setVisibility(View.VISIBLE);
                        editInput.setFocusable(true);
                        dialogRG.setVisibility(View.INVISIBLE);
                        inputSpinner.setVisibility(View.INVISIBLE);
                        break;
                    case "Trip Destination":
                        dbColumn = "TripDestination";
                        editInput.setVisibility(View.VISIBLE);
                        editInput.setFocusable(true);
                        dialogRG.setVisibility(View.INVISIBLE);
                        inputSpinner.setVisibility(View.INVISIBLE);
                        break;
                    case "Departure Date":
                        dbColumn = "DepartureDate";
                        editInput.setVisibility(View.VISIBLE);
                        editInput.setFocusable(false);
                        dialogRG.setVisibility(View.INVISIBLE);
                        inputSpinner.setVisibility(View.INVISIBLE);
                        break;
                    case "Return Date":
                        dbColumn = "ReturnDate";
                        editInput.setVisibility(View.VISIBLE);
                        editInput.setFocusable(false);
                        dialogRG.setVisibility(View.INVISIBLE);
                        inputSpinner.setVisibility(View.INVISIBLE);
                        break;
                    case "Risk Assessment":
                        dbColumn = "RiskAssessment";
                        editInput.setVisibility(View.INVISIBLE);
                        editInput.setFocusable(true);
                        dialogRG.setVisibility(View.VISIBLE);
                        inputSpinner.setVisibility(View.INVISIBLE);
                        break;
                    case "Is Overseas":
                        dbColumn = "IsOverseas";
                        editInput.setVisibility(View.INVISIBLE);
                        editInput.setFocusable(true);
                        dialogRG.setVisibility(View.VISIBLE);
                        inputSpinner.setVisibility(View.INVISIBLE);
                        break;
                    case "Transport Mode":
                        dbColumn = "TransportMode";
                        editInput.setVisibility(View.INVISIBLE);
                        editInput.setFocusable(true);
                        dialogRG.setVisibility(View.INVISIBLE);
                        inputSpinner.setVisibility(View.VISIBLE);
                        break;
                    case "Description":
                        dbColumn = "Description";
                        editInput.setVisibility(View.VISIBLE);
                        editInput.setFocusable(true);
                        dialogRG.setVisibility(View.INVISIBLE);
                        inputSpinner.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button saveBtn = dialog.findViewById(R.id.save_Btn_dialog);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                DatabaseHelper databaseHelper = new DatabaseHelper(dialog.getContext(), "User_Login.db");

                saveEdit();
                if(isSuccess)
                {
                    boolean success = databaseHelper.editTripRecord();

                    if (success)
                    {
                        Toast.makeText(dialog.getContext(), R.string.dialog_edit_saved, Toast.LENGTH_SHORT).show();
                        databaseHelper.close();
                        dialog.dismiss();
                    }
                    else
                    {
                        Toast.makeText(dialog.getContext(), R.string.dialog_edit_not_saved, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(dialog.getContext(), R.string.details_invalid, Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void saveEdit()
    {
        int dialogId = dialogRG.getCheckedRadioButtonId();
        if (dialogId != -1)
        {
            dialogRB = dialog.findViewById(dialogId);
        }
        else
            dialogRB = null;

        infoChange = editInput.getText().toString();
        switch(editOption)
        {
            case "Trip Name":
                dbColumn = "TripName";
                if (!infoChange.isEmpty() || !infoChange.equals(""))
                {
                    UserDataDTO.getInstance().getTripDetailsModel().setTripColumnName(dbColumn);
                    UserDataDTO.getInstance().getTripDetailsModel().setUpdateData(infoChange);
                    isSuccess = true;
                }
                else
                    isSuccess = false;

                break;
            case "Trip Destination":
                dbColumn = "TripDestination";
                if (!infoChange.isEmpty() || !infoChange.equals(""))
                {
                    UserDataDTO.getInstance().getTripDetailsModel().setTripColumnName(dbColumn);
                    UserDataDTO.getInstance().getTripDetailsModel().setUpdateData(infoChange);
                    isSuccess = true;
                }
                else
                    isSuccess = false;

                break;
            case "Departure Date":
                dbColumn = "DepartureDate";
                if (!infoChange.isEmpty() || !infoChange.equals(""))
                {
                    UserDataDTO.getInstance().getTripDetailsModel().setTripColumnName(dbColumn);
                    UserDataDTO.getInstance().getTripDetailsModel().setUpdateData(infoChange);
                    isSuccess = true;
                }
                else
                    isSuccess = false;

                break;
            case "Return Date":
                dbColumn = "ReturnDate";
                if (!infoChange.isEmpty() || !infoChange.equals(""))
                {
                    UserDataDTO.getInstance().getTripDetailsModel().setTripColumnName(dbColumn);
                    UserDataDTO.getInstance().getTripDetailsModel().setUpdateData(infoChange);
                    isSuccess = true;
                }
                else
                    isSuccess = false;

                break;
            case "Risk Assessment":
                dbColumn = "RiskAssessment";
                if (getRadioInput())
                {
                    UserDataDTO.getInstance().getTripDetailsModel().setTripColumnName(dbColumn);
                    UserDataDTO.getInstance().getTripDetailsModel().setUpdateData(getRadioInput().toString());
                    isSuccess = true;
                }
                else
                    isSuccess = false;

                break;
            case "Is Overseas":
                // change to radio buttons for input
                dbColumn = "IsOverseas";
                if (getRadioInput())
                {
                    UserDataDTO.getInstance().getTripDetailsModel().setTripColumnName(dbColumn);
                    UserDataDTO.getInstance().getTripDetailsModel().setUpdateData(getRadioInput().toString());
                    isSuccess = true;
                }
                else
                    isSuccess = false;

                break;
            case "TransportMode":
                // change to spinner for input
                infoChange = inputSpinner.getSelectedItem().toString();
                dbColumn = "TransportMode";
                if (infoChange.equals("---"))
                {
                    UserDataDTO.getInstance().getTripDetailsModel().setTripColumnName(dbColumn);
                    UserDataDTO.getInstance().getTripDetailsModel().setUpdateData(infoChange);
                    isSuccess = true;
                }
                else
                    isSuccess = false;

                break;
            case "Description":
                dbColumn = "Description";
                if (!infoChange.isEmpty() || !infoChange.equals(""))
                {
                    UserDataDTO.getInstance().getTripDetailsModel().setTripColumnName(dbColumn);
                    UserDataDTO.getInstance().getTripDetailsModel().setUpdateData(infoChange);
                    isSuccess = true;
                }
                else
                    isSuccess = false;

                break;
        }
    }

    private Boolean getRadioInput()
    {
        if (dialogRB != null) {
            String input = dialogRB.getText().toString();

            return input.equals("Yes");
        } else {
            return false;
        }
    }
}
