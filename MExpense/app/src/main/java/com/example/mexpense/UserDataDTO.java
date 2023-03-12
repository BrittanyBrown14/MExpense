package com.example.mexpense;

public class UserDataDTO {
    private UserLoginModel userLoginModel= new UserLoginModel();
    private TripDetailsModel tripDetailsModel = new TripDetailsModel();
    private ExpensesModel expensesModel = new ExpensesModel();

    private UserDataDTO(){}
    private static volatile UserDataDTO INSTANCE = null;

    public static UserDataDTO getInstance(){
        if(INSTANCE == null) {
            synchronized (UserDataDTO.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UserDataDTO();
                }
            }
        }
        return INSTANCE;
    }

    public UserLoginModel getUserLoginModel() {
        return userLoginModel;
    }

    public void setUserLoginModel(UserLoginModel userLoginModel) {
        this.userLoginModel = userLoginModel;
    }

    public TripDetailsModel getTripDetailsModel() {
        return tripDetailsModel;
    }

    public void setTripDetailsModel(TripDetailsModel tripDetailsModel) {
        this.tripDetailsModel = tripDetailsModel;
    }

    public ExpensesModel getExpensesModel() {
        return expensesModel;
    }

    public void setExpensesModel(ExpensesModel expensesModel) {
        this.expensesModel = expensesModel;
    }

    public static UserDataDTO getINSTANCE() {
        return INSTANCE;
    }

    public static void setINSTANCE(UserDataDTO INSTANCE) {
        UserDataDTO.INSTANCE = INSTANCE;
    }
}
