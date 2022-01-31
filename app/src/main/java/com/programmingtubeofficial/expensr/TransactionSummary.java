package com.programmingtubeofficial.expensr;

public class TransactionSummary {
    private double income;
    private double expenses;
    private double savings = 0;
    private CurrencyType currencyType;

    public TransactionSummary(){
        this.income = 0;
        this.expenses = 0;
        this.savings = 0;
    }

    public TransactionSummary(double income, double expenses, CurrencyType currencyType) {
        this.income = income;
        this.expenses = expenses;
        this.currencyType = currencyType;
    }

    public double getIncome() {
        return income;
    }

    public void addIncome(double income)
    {
        this.income += income;
    }
    public void removeIncome(double income){
        this.income -= income;
    }
    public double getExpenses() {
        return expenses;
    }

    public void addExpenses(double expenses) {
        this.expenses += expenses;
    }
    public void removeExpenses(double expenses){
        this.expenses -= expenses;
    }

    public double getSavings() {
        return this.income - this.expenses;
    }

    public CurrencyType getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(CurrencyType currencyType) {
        // Update Numbers According to change in CurrencyType
        // In Next Update
        this.currencyType = currencyType;
    }

    // Get Formatted strings
    private String getIncomeString(){
        return this.currencyType.label + " " + String.valueOf(this.income);
    }
    private String getExpenseString(){
        return this.currencyType.label + " " + String.valueOf(this.expenses);
    }
    private String getSavingsString(){
        return this.currencyType.label + " " + String.valueOf(this.savings);
    }
}
