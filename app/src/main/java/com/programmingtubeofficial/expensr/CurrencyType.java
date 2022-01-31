package com.programmingtubeofficial.expensr;

public enum CurrencyType {
    DOLLAR("$", "USD"),
    RUPEE("₹", "INR"),
    EURO("€", "EUR");

    public final String label;
    public final String quote;
    private CurrencyType(String label, String quote) {
        this.label = label;
        this.quote = quote;
    }
}
