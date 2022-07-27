package com.example.dailyaccount.databasesetup;

public class AccountData {
    private int id;
    private  String details,amount,status,date;

    public AccountData(String details, String amount, String status, String date) {
        this.details = details;
        this.amount = amount;
        this.status = status;
        this.date = date;
    }

    public AccountData(int id, String details, String amount, String status) {
        this.id = id;
        this.details = details;
        this.amount = amount;
        this.status = status;
    }

    public AccountData() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
