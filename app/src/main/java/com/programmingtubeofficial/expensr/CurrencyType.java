package com.programmingtubeofficial.expensr;

public enum CurrencyType {
    DOLLAR("$", "USD", R.drawable.currency_dollar),
    RUPEE("₹", "INR", R.drawable.currency_rupee),
    EURO("€", "EUR", R.drawable.currency_euro);

    public final String label;
    public final String quote;
    public final int image;
    private CurrencyType(String label, String quote, int image) {
        this.label = label;
        this.quote = quote;
        this.image = image;
    }
    public static CurrencyType getCurrency(String quote){
        for (CurrencyType currencyType : CurrencyType.values()) {
            if(currencyType.quote.equals(quote)){
                return currencyType;
            }
        }
        return CurrencyType.RUPEE;
    }
}
