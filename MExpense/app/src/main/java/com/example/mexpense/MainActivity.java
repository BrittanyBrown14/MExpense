package com.example.mexpense;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText userText;
    EditText companyText;
    EditText passwordText;

    String username;
    String companyName;
    String password;


    String TempPassword = "NOT_FOUND" ;
    static String TempFirstName = "NOT_FOUND" ;
    static String TempLastName = "NOT_FOUND" ;
    static int Tempid = 0;

    DatabaseHelper databaseHelper;
    SQLiteDatabase sqlDB;
    Cursor cursor;
    UserDataDTO userDataSingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userText = findViewById(R.id.editTextUserName);
        companyText = findViewById(R.id.editTextCompanyName);
        passwordText = findViewById(R.id.editTextPassword);

        databaseHelper = new DatabaseHelper(MainActivity.this, "User_Login.db");
        userDataSingleton = UserDataDTO.getInstance();
    }
    public void login(View view)
    {
        username = userText.getText().toString();
        companyName = companyText.getText().toString();
        password = passwordText.getText().toString();

        try
        {
            if(!areFieldsEmpty(username,companyName,password)) {
                // Opening SQLite database write permission.
                sqlDB = databaseHelper.getWritableDatabase();
                // Adding search email query to cursor.
                cursor = sqlDB.query(DatabaseHelper.USER_LOGIN, null, " " + DatabaseHelper.COLUMN_USERNAME + "=?",new String[]{username} , null, null, null );
                while (cursor.moveToNext()) {
                    if (cursor.isFirst()) {
                        cursor.moveToFirst();
                        // Stores allTrips user info with entered email.
                        getUserInfo();
                        // Closing cursor.
                        cursor.close();
                    }
                }
                // Calling method to check final result ..
                CheckFinalResult(password);
                userDataSingleton.getUserLoginModel().setID(Tempid);
            }
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public boolean areFieldsEmpty(String username, String companyName, String password)
    {
        return username.isEmpty()||companyName.isEmpty()||password.isEmpty();
    }

    public void getUserInfo()
    {
        TempPassword = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD));
        TempFirstName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FIRST_NAME));
        TempLastName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LAST_NAME));
        Tempid = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
    }
    public void CheckFinalResult(String password)
    {
        if(TempPassword.equals(password))
        {
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);

            userDataSingleton.getUserLoginModel().setFirstName(TempFirstName);
            userDataSingleton.getUserLoginModel().setLastName(TempLastName);

            Toast.makeText(MainActivity.this,"Login Successful " + TempFirstName + " " + TempLastName, Toast.LENGTH_SHORT).show();

            startActivity(intent);
        }
        else {
            Toast.makeText(MainActivity.this,"UserName or Password is Wrong, Please Try Again.",Toast.LENGTH_LONG).show();
        }
        TempPassword = "NOT_FOUND" ;
    }
}