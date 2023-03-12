package com.example.mexpense;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class EditDialogView {
    private String dbColumn;
    private String infoChange;
    private String editOption;

    Spinner inputSpinner;
    RadioGroup dialogRG;
    RadioButton dialogRB;
    EditText editInput;
    private Dialog dialog;

    public void showDialog(Activity activity, String msg){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_edit_dialog);

        editInput = dialog.findViewById(R.id.editTextInput);
        dialogRG = dialog.findViewById(R.id.dialogRadioGroup);
        inputSpinner = dialog.findViewById(R.id.dialogInputSpinner);

        Button cancelBtn = dialog.findViewById(R.id.cancel_Btn_dialog);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

        Spinner spinner = dialog.findViewById(R.id.dialog_spinner);
        ArrayAdapter spinnerAdapter = new ArrayAdapter(dialog.getContext(), R.layout.spinner_layout, dialog.getContext().getResources().getStringArray(R.array.edit_trip_options));
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("ResourceAsColor")
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                editOption = spinner.getSelectedItem().toString();
                switch(editOption)
                {
                    case "Trip Name":
                        dbColumn = "TripName";
                        editInput.setVisibility(View.VISIBLE);
                        dialogRG.setVisibility(View.INVISIBLE);
                        inputSpinner.setVisibility(View.INVISIBLE);
                        break;
                    case "Trip Destination":
                        dbColumn = "TripDestination";
                        editInput.setVisibility(View.VISIBLE);
                        dialogRG.setVisibility(View.INVISIBLE);
                        inputSpinner.setVisibility(View.INVISIBLE);
                        break;
                    case "Departure Date":
                        dbColumn = "DepartureDate";
                        editInput.setVisibility(View.VISIBLE);
                        dialogRG.setVisibility(View.INVISIBLE);
                        inputSpinner.setVisibility(View.INVISIBLE);
                        break;
                    case "Return Date":
                        dbColumn = "ReturnDate";
                        editInput.setVisibility(View.VISIBLE);
                        dialogRG.setVisibility(View.INVISIBLE);
                        inputSpinner.setVisibility(View.INVISIBLE);
                        break;
                    case "Risk Assessment":
                        // change to radio buttons for input
                        dbColumn = "RiskAssessment";
                        editInput.setVisibility(View.INVISIBLE);
                        dialogRG.setVisibility(View.VISIBLE);
                        inputSpinner.setVisibility(View.INVISIBLE);
                        break;
                    case "Is Overseas":
                        // change to radio buttons for input
                        dbColumn = "IsOverseas";
                        editInput.setVisibility(View.INVISIBLE);
                        dialogRG.setVisibility(View.VISIBLE);
                        inputSpinner.setVisibility(View.INVISIBLE);
                        break;
                    case "Transport Mode":
                        dbColumn = "TransportMode";
                        editInput.setVisibility(View.INVISIBLE);
                        dialogRG.setVisibility(View.INVISIBLE);
                        inputSpinner.setVisibility(View.VISIBLE);
                        break;
                    case "Description":
                        dbColumn = "Description";
                        editInput.setVisibility(View.VISIBLE);
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
                boolean success = databaseHelper.editTripRecord();

                if (success)
                {
                    Toast.makeText(dialog.getContext(), "Your edit was saved ", Toast.LENGTH_SHORT).show();
                    databaseHelper.close();
                    dialog.dismiss();
                }
                else
                {
                    Toast.makeText(dialog.getContext(), "Your edit were not saved. Please try again ", Toast.LENGTH_SHORT).show();

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
                UserDataDTO.getInstance().getTripDetailsModel().setTripColumnName(dbColumn);
                //UserDataDTO.getInstance().getTripDetailsModel().setTripName(infoChange);
                UserDataDTO.getInstance().getTripDetailsModel().setUpdateData(infoChange);
                break;
            case "Trip Destination":
                dbColumn = "TripDestination";
                UserDataDTO.getInstance().getTripDetailsModel().setTripColumnName(dbColumn);
                //  UserDataDTO.getInstance().getTripDetailsModel().setTripDestination(infoChange);
                UserDataDTO.getInstance().getTripDetailsModel().setUpdateData(infoChange);
                break;
            case "Departure Date":
                dbColumn = "DepartureDate";
                UserDataDTO.getInstance().getTripDetailsModel().setTripColumnName(dbColumn);
                //     UserDataDTO.getInstance().getTripDetailsModel().setDepartureDate(infoChange);
                UserDataDTO.getInstance().getTripDetailsModel().setUpdateData(infoChange);
                break;
            case "Return Date":
                dbColumn = "ReturnDate";
                UserDataDTO.getInstance().getTripDetailsModel().setTripColumnName(dbColumn);
                //     UserDataDTO.getInstance().getTripDetailsModel().setTripName(infoChange);
                UserDataDTO.getInstance().getTripDetailsModel().setUpdateData(infoChange);
                break;
            case "Risk Assessment":
                // change to radio buttons for input
                dbColumn = "RiskAssessment";
                UserDataDTO.getInstance().getTripDetailsModel().setTripColumnName(dbColumn);
                //    UserDataDTO.getInstance().getTripDetailsModel().setRiskAssessment(getRadioInput());
                UserDataDTO.getInstance().getTripDetailsModel().setUpdateData(getRadioInput().toString());
                break;
            case "Is Overseas":
                // change to radio buttons for input
                dbColumn = "IsOverseas";
                UserDataDTO.getInstance().getTripDetailsModel().setTripColumnName(dbColumn);
                UserDataDTO.getInstance().getTripDetailsModel().setUpdateData(getRadioInput().toString());
                break;
            case "TransportMode":
                // change to spinner for input
                infoChange = inputSpinner.getSelectedItem().toString();
                dbColumn = "TransportMode";
                UserDataDTO.getInstance().getTripDetailsModel().setTripColumnName(dbColumn);
                UserDataDTO.getInstance().getTripDetailsModel().setUpdateData(infoChange);

                break;
            case "Description":
                dbColumn = "Description";
                UserDataDTO.getInstance().getTripDetailsModel().setTripColumnName(dbColumn);
                UserDataDTO.getInstance().getTripDetailsModel().setUpdateData(infoChange);
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
