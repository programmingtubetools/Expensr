package com.programmingtubeofficial.expensr;

public class TransactionModel {
    private String title;
    private int icon;
    private double amount;
    private TransactionType type;
    private String date;

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
