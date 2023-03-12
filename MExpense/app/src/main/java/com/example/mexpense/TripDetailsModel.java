package com.example.mexpense;

public class TripDetailsModel {

    private int TripUserID;
    private int TripID;
    private String TripName;
    private String TripDestination;
    private String DepartureDate;
    private String ReturnDate;
    private Boolean RiskAssessment;
    private String Description;
    private Boolean IsOverseas;
    private String TransportMode;

    private String UpdateData;
    private String TripColumnName;

    public String toString()
    {
        return  "\nTrip ID: "+ TripID +
                "\nTrip Name: " + TripName +
                "\nTrip Destination: " + TripDestination +
                "\nDeparture Date: " + DepartureDate +
                "\nReturn Date: " + ReturnDate +
                "\nRisk Assessment: " + RiskAssessment +
                "\nIs Overseas: " + IsOverseas +
                "\nTransport Mode: " + TransportMode +
                "\nDescription: " + Description + "\n";
    }

    public TripDetailsModel(int tripUserID, int tripID, String tripName, String tripDestination, String departureDate,
                            String returnDate, Boolean riskAssessment, String description, Boolean isOverseas, String transportMode)
    {
        TripUserID = tripUserID;
        TripID = tripID;
        TripName = tripName;
        TripDestination = tripDestination;
        DepartureDate = departureDate;
        ReturnDate = returnDate;
        RiskAssessment = riskAssessment;
        Description = description;
        IsOverseas = isOverseas;
        TransportMode = transportMode;
    }

    public TripDetailsModel() {

    }

    public int getTripUserID() {
        return TripUserID;
    }

    public void setTripUserID(int tripUserID) {
        TripUserID = tripUserID;
    }

    public int getTripID() {
        return TripID;
    }

    public void setTripID(int tripID) {
        TripID = tripID;
    }

    public String getTripName() {
        return TripName;
    }

    public void setTripName(String tripName) {
        TripName = tripName;
    }

    public String getTripDestination() {
        return TripDestination;
    }

    public void setTripDestination(String tripDestination) {
        TripDestination = tripDestination;
    }

    public String getDepartureDate() {
        return DepartureDate;
    }

    public void setDepartureDate(String departureDate) {
        DepartureDate = departureDate;
    }

    public String getReturnDate() {
        return ReturnDate;
    }

    public void setReturnDate(String returnDate) {
        ReturnDate = returnDate;
    }

    public Boolean getRiskAssessment() {
        return RiskAssessment;
    }

    public void setRiskAssessment(Boolean riskAssessment) {
        RiskAssessment = riskAssessment;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Boolean getOverseas() {
        return IsOverseas;
    }

    public void setOverseas(Boolean overseas) {
        IsOverseas = overseas;
    }

    public String getTransportMode() {
        return TransportMode;
    }

    public void setTransportMode(String transportMode) {
        TransportMode = transportMode;
    }

    public String getTripColumnName() {
        return TripColumnName;
    }

    public void setTripColumnName(String tripColumnName) {
        TripColumnName = tripColumnName;
    }
    public String getUpdateData() {
        return UpdateData;
    }

    public void setUpdateData(String updateData) {
        UpdateData = updateData;
    }
}
