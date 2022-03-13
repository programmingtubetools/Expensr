package com.programmingtubeofficial.expensr;

import java.io.Serializable;

public class TransactionModel implements Serializable {
    private String title;
    private int icon;
    private double amount;
    private TransactionType type;
    private String date;
    private String user;
    private int id;

    public TransactionModel(){}
    public TransactionModel(String title, String date, double amount, TransactionType type) {
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIcon() {
        if(this.type.equals(TransactionType.CREDIT)){
            this.icon = R.drawable.credit_new_icon;
        }else if(this.type.equals(TransactionType.DEBIT)){
            this.icon = R.drawable.debit_icon;
        }else{
             this.icon = R.drawable.self_transfer_icon;
        }
        return icon;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}
