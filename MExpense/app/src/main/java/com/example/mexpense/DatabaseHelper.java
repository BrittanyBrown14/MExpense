package com.example.mexpense;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.sax.SAXResult;

public class DatabaseHelper extends SQLiteOpenHelper {
    // For Login DB
    public static final String USER_LOGIN = "User_Login";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_FIRST_NAME = "FirstName";
    public static final String COLUMN_LAST_NAME = "LastName";
    public static final String COLUMN_COMPANY = "Company";
    public static final String COLUMN_DOB = "DoB";
    public static final String COLUMN_EMAIL = "Email";
    public static final String COLUMN_PHONE_NUMBER = "PhoneNumber";
    public static final String COLUMN_USERNAME = "Username";
    public static final String COLUMN_PASSWORD = "Password";

    // For Trips DB
    public static final String TRIP_DETAILS = "Trip_Details";
    public static final String COLUMN_TRIP_USER_ID = "TripUserID";
    public static final String COLUMN_TRIP_ID = "TripID";
    public static final String COLUMN_TRIP_NAME = "TripName";
    public static final String COLUMN_TRIP_DESTINATION = "TripDestination";
    public static final String COLUMN_TRIP_DEPARTURE_DATE = "DepartureDate";
    public static final String COLUMN_TRIP_RETURN_DATE = "ReturnDate";
    public static final String COLUMN_TRIP_RISK_ASSESSMENT = "RiskAssessment";
    public static final String COLUMN_TRIP_DESCRIPTION = "Description";
    public static final String COLUMN_TRIP_IS_OVERSEAS = "IsOverseas";
    public static final String COLUMN_TRIP_TRANSPORT = "TransportMode";

    // For Expense DB
    public static final String EXPENSE_TABLE = "Expenses";
    public static final String COLUMN_EXPENSE_TRIP_ID = "ExpenseTripID";
    public static final String COLUMN_EXPENSE_ID = "ExpenseID";
    public static final String COLUMN_EXPENSE_CURRENCY = "ExpenseCurrency";
    public static final String COLUMN_EXPENSE_TYPE = "ExpenseType";
    public static final String COLUMN_EXPENSE_OTHER_TYPE = "ExpenseOtherType";
    public static final String COLUMN_EXPENSE_AMOUNT = "Amount";
    public static final String COLUMN_EXPENSE_DATE = "Date";
    public static final String COLUMN_EXPENSE_TIME = "Time";
    public static final String COLUMN_EXPENSE_COMMENTS = "Comments";
    public String databaseName;


    @Override
    //Is called the first time the db is accessed.
    //There should be code in here to create a new db
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createLoginTableStatement = "CREATE TABLE IF NOT EXISTS "
                + USER_LOGIN
                + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COLUMN_FIRST_NAME + " TEXT NOT NULL, "
                + COLUMN_LAST_NAME + " TEXT NOT NULL, "
                + COLUMN_COMPANY + " TEXT NOT NULL, "
                + COLUMN_DOB + " TEXT, "
                + COLUMN_EMAIL + " TEXT, "
                + COLUMN_PHONE_NUMBER + " INTEGER, "
                + COLUMN_USERNAME + " TEXT NOT NULL, "
                + COLUMN_PASSWORD + " TEXT NOT NULL)";

        String createTripTableStatement  = "CREATE TABLE IF NOT EXISTS "
                + TRIP_DETAILS
                + " (" + COLUMN_TRIP_USER_ID + " INTEGER REFERENCES " + USER_LOGIN + " (" + COLUMN_ID + "), "
                + COLUMN_TRIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COLUMN_TRIP_NAME + " TEXT NOT NULL, "
                + COLUMN_TRIP_DESTINATION + " TEXT NOT NULL, "
                + COLUMN_TRIP_DEPARTURE_DATE + " TEXT NOT NULL, "
                + COLUMN_TRIP_RETURN_DATE + " TEXT NOT NULL, "
                + COLUMN_TRIP_RISK_ASSESSMENT + " BOOL NOT NULL, "
                + COLUMN_TRIP_DESCRIPTION + " TEXT, "
                + COLUMN_TRIP_IS_OVERSEAS + " BOOL NOT NULL,  "
                + COLUMN_TRIP_TRANSPORT + " TEXT NOT NULL)";

        String createExpenseTableStatement  = "CREATE TABLE IF NOT EXISTS "
                + EXPENSE_TABLE
                + " (" + COLUMN_EXPENSE_TRIP_ID + " INTEGER REFERENCES " + TRIP_DETAILS + " (" + COLUMN_TRIP_ID + "), "
                + COLUMN_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COLUMN_EXPENSE_CURRENCY + " TEXT NOT NULL, "
                + COLUMN_EXPENSE_TYPE + " TEXT NOT NULL, "
                + COLUMN_EXPENSE_OTHER_TYPE + " TEXT NOT NULL, "
                + COLUMN_EXPENSE_AMOUNT + " INTEGER NOT NULL, "
                + COLUMN_EXPENSE_DATE + " TEXT NOT NULL, "
                + COLUMN_EXPENSE_TIME + " TEXT NOT NULL, "
                + COLUMN_EXPENSE_COMMENTS + " TEXT)";

        sqLiteDatabase.execSQL(createLoginTableStatement);
        sqLiteDatabase.execSQL(createTripTableStatement);
        sqLiteDatabase.execSQL(createExpenseTableStatement);
    }

    //this is called when the db version number changes
    //prevents the app from breaking when the db design changes
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public DatabaseHelper(Context context, String databaseName) {
        super(context, databaseName, null, 1);
        this.databaseName = databaseName;
    }

    // All the database queries for adding, deleting and editing trips
    public List<TripDetailsModel> getAllTrips()
    {
        int userID = UserDataDTO.getInstance().getTripDetailsModel().getTripUserID();
        List<TripDetailsModel> returnList = new ArrayList<>();
        //get data from db

        String queryAll = "SELECT * FROM " + TRIP_DETAILS + " WHERE " + COLUMN_TRIP_USER_ID + " IS " + userID;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryAll, null);

        if (cursor.moveToFirst())
        {
            // loop through the results and create new customer objects and put them in the return list
            do {
                int tripUserID = userID;
                int tripID = cursor.getInt(1);
                String tripName = cursor.getString(2);
                String tripDestination = cursor.getString(3);
                String tripDepartureDate = cursor.getString(4);
                String tripReturnDate = cursor.getString(5);
                boolean tripRiskAssessment = cursor.getInt(6) == 1;
                String tripDescription = cursor.getString(7);
                boolean tripIsOverseas = cursor.getInt(8) == 1;
                String tripTransportMode = cursor.getString(9);


                TripDetailsModel newTrip = new TripDetailsModel(tripUserID,tripID,tripName,tripDestination,tripDepartureDate,
                        tripReturnDate,tripRiskAssessment,tripDescription,tripIsOverseas,tripTransportMode);
                returnList.add(newTrip);
            } while (cursor.moveToNext());
        }

        //close the cursor and the db when everything is done
        cursor.close();
        db.close();
        return returnList;
    }

    public boolean addTripRecord(TripDetailsModel tripDetailsModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TRIP_USER_ID, tripDetailsModel.getTripUserID());
        cv.put(COLUMN_TRIP_NAME, tripDetailsModel.getTripName());
        cv.put(COLUMN_TRIP_DESTINATION, tripDetailsModel.getTripDestination());
        cv.put(COLUMN_TRIP_DEPARTURE_DATE, tripDetailsModel.getDepartureDate());
        cv.put(COLUMN_TRIP_RETURN_DATE, tripDetailsModel.getReturnDate());
        cv.put(COLUMN_TRIP_RISK_ASSESSMENT, tripDetailsModel.getRiskAssessment());
        cv.put(COLUMN_TRIP_IS_OVERSEAS, tripDetailsModel.getOverseas());
        cv.put(COLUMN_TRIP_TRANSPORT, tripDetailsModel.getTransportMode());
        cv.put(COLUMN_TRIP_DESCRIPTION, tripDetailsModel.getDescription());

        long insert = db.insert(TRIP_DETAILS, null, cv);
        if (insert == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void deleteTripRecord()
    {
        try
        {
            int userID = UserDataDTO.getInstance().getTripDetailsModel().getTripUserID();
            int listID = UserDataDTO.getInstance().getTripDetailsModel().getTripID();

            String queryDelete = "DELETE FROM " + TRIP_DETAILS + " WHERE " + COLUMN_TRIP_USER_ID + " IS " + userID +
                    " AND " + COLUMN_TRIP_ID + " IS " + listID;
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(queryDelete);
            db.close();
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public boolean editTripRecord()
    {
        try
        {
            int userID = UserDataDTO.getInstance().getTripDetailsModel().getTripUserID();
            int listID = UserDataDTO.getInstance().getTripDetailsModel().getTripID();
            String column = UserDataDTO.getInstance().getTripDetailsModel().getTripColumnName();
            String dataInfo = UserDataDTO.getInstance().getTripDetailsModel().getUpdateData();

            String idk = " UPDATE " +TRIP_DETAILS+ " SET " +column+ " = " + "\""+dataInfo+ "\""+
                    " WHERE " +COLUMN_TRIP_USER_ID+ " IS " +userID+
                    " AND " +COLUMN_TRIP_ID+ " IS " +listID;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(idk, null);

            cursor.moveToNext();
            cursor.close();
            db.close();
            return true;
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public void deleteAllTripRecord()
    {
        try
        {
            int userID = UserDataDTO.getInstance().getTripDetailsModel().getTripUserID();

            String queryDelete = "DELETE FROM " + TRIP_DETAILS + " WHERE " + COLUMN_TRIP_USER_ID + " IS " + userID;
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(queryDelete);
            db.close();
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }


    // All the database queries for adding, deleting and editing trips
    public List<ExpensesModel> getAllExpenses()
    {
        int tripID = UserDataDTO.getInstance().getTripDetailsModel().getTripID();
        List<ExpensesModel> returnList = new ArrayList<>();
        //get data from db

        String queryAll = "SELECT * FROM " + EXPENSE_TABLE + " WHERE " + COLUMN_EXPENSE_TRIP_ID + " IS " + tripID;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryAll, null);

        if (cursor.moveToFirst())
        {
            // loop through the results and create new customer objects and put them in the return list
            do {
                int expenseTripID = cursor.getInt(0);
                int expenseID = cursor.getInt(1);
                String expenseCurrency = cursor.getString(2);
                String expenseType = cursor.getString(3);
                String expenseOtherType = cursor.getString(4);
                int expenseAmount = cursor.getInt(5);
                String expenseDate = cursor.getString(6);
                String expenseTime = cursor.getString(7);
                String expenseComments = cursor.getString(8);


                ExpensesModel newExpense = new ExpensesModel(expenseTripID,expenseID,expenseCurrency,expenseType,expenseOtherType,
                        expenseAmount,expenseDate,expenseTime,expenseComments);
                returnList.add(newExpense);
            } while (cursor.moveToNext());
        }

        //close the cursor and the db when everything is done
        cursor.close();
        db.close();
        return returnList;
    }

    public boolean addExpenseRecord(ExpensesModel expensesModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_EXPENSE_TRIP_ID, expensesModel.getExpenseTripID());
        cv.put(COLUMN_EXPENSE_CURRENCY, expensesModel.getExpenseCurrency());
        cv.put(COLUMN_EXPENSE_TYPE, expensesModel.getExpenseType());
        cv.put(COLUMN_EXPENSE_OTHER_TYPE, expensesModel.getExpenseOtherType());
        cv.put(COLUMN_EXPENSE_AMOUNT, expensesModel.getAmount());
        cv.put(COLUMN_EXPENSE_DATE, expensesModel.getDate());
        cv.put(COLUMN_EXPENSE_TIME, expensesModel.getTime());
        cv.put(COLUMN_EXPENSE_COMMENTS, expensesModel.getComments());

        long insert = db.insert(EXPENSE_TABLE, null, cv);
        if (insert == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}