package com.example.mexpense;

public class ExpensesModel {
    private int ExpenseTripID;
    private int ExpenseID;
    private String ExpenseCurrency;
    private String ExpenseType;
    private String ExpenseOtherType;
    private int Amount;
    private String Date;
    private String Time;
    private String Comments;

    public ExpensesModel(){}

    public ExpensesModel(int expenseTripID, int expenseID, String expenseCurrency, String expenseType,
                         String expenseOtherType, int amount, String date, String time, String comments) {
        ExpenseTripID = expenseTripID;
        ExpenseID = expenseID;
        ExpenseCurrency = expenseCurrency;
        ExpenseType = expenseType;
        ExpenseOtherType = expenseOtherType;
        Amount = amount;
        Date = date;
        Time = time;
        Comments = comments;
    }

    public String toString()
    {
        return  "\nExpense Trip ID: "+ ExpenseTripID +
                "\nExpense ID: " + ExpenseID +
                "\nExpense Type: " + ExpenseType +
                "\nExpense Other Type: " + ExpenseOtherType +
                "\nCurrency: " + ExpenseCurrency +
                "\nAmount: " + Amount +
                "\nExpense Date: " + Date +
                "\nExpense Time: " + Time +
                "\nComments " + Comments + "\n";
    }

    public int getExpenseTripID() {
        return ExpenseTripID;
    }

    public void setExpenseTripID(int expenseTripID) {
        ExpenseTripID = expenseTripID;
    }

    public int getExpenseID() {
        return ExpenseID;
    }

    public void setExpenseID(int expenseID) {
        ExpenseID = expenseID;
    }

    public String getExpenseCurrency() {
        return ExpenseCurrency;
    }

    public void setExpenseCurrency(String expenseCurrency) {
        ExpenseCurrency = expenseCurrency;
    }

    public String getExpenseType() {
        return ExpenseType;
    }

    public void setExpenseType(String expenseType) {
        ExpenseType = expenseType;
    }

    public String getExpenseOtherType() {
        return ExpenseOtherType;
    }

    public void setExpenseOtherType(String expenseOtherType) {
        ExpenseOtherType = expenseOtherType;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }
}
