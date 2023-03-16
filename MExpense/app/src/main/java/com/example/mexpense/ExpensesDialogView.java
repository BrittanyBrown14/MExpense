package com.example.mexpense;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.List;

public class ExpensesDialogView {

    private Dialog dialog;
    ListView expensesLV;
    //Button cancelBtn;
    ArrayAdapter lvArray;
    List<ExpensesModel> allExpenses;

    public void showDialog(Activity activity, String msg) {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_expenses_dialog);

        expensesLV = dialog.findViewById(R.id.expensesListView);
        ImageButton cancelBtn = dialog.findViewById(R.id.cancelButton);

        // Closes the dialog
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

        // Gets the information from the database and populate the listview with that data
        DatabaseHelper databaseHelper = new DatabaseHelper(dialog.getContext(), "User_Login.db");
        allExpenses = databaseHelper.getAllExpenses();

        lvArray = new ArrayAdapter(dialog.getContext(), R.layout.spinner_layout, allExpenses);
        expensesLV.setAdapter(lvArray);
        showAllTrips(allExpenses);

    }

    private void showAllTrips(List<ExpensesModel> allExpenses) {
        lvArray = new ArrayAdapter<ExpensesModel>(dialog.getContext(), R.layout.spinner_layout, allExpenses);
        expensesLV.setAdapter(lvArray);
    }
}
