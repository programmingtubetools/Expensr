package com.programmingtubeofficial.expensr;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;

public class dashboard extends AppCompatActivity {
    RecyclerView txnList;
    ArrayList<TransactionModel> transactionModels;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    private CurrencyType currency = CurrencyType.RUPEE;
    FirebaseAuth mAuth;
    private final TransactionType[] txnTypes = {TransactionType.CREDIT, TransactionType.DEBIT, TransactionType.TRANSFER};
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Update View with User
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            System.exit(1);
        }
        TextView txtUsername = findViewById(R.id.txtUsername);
        txtUsername.setText(user.getDisplayName().toString().split(" ")[0]);
        transactionModels = getTransactionModels();
        TransactionAdapter txnAdapter = new TransactionAdapter(this, transactionModels, currency);
        txnAdapter.registerAdapterDataObserver(new AdapterDataObserver() {

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                ((TextView)findViewById(R.id.lblTxn)).setText("Recent Transactions (" + String.valueOf(transactionModels.size()) + ")");
                updateSummary(txnAdapter.getTransactionSummary());
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        txnList = findViewById(R.id.txnList);
        txnList.setLayoutManager(layoutManager);
        txnList.setAdapter(txnAdapter);

        ImageView btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Toast.makeText(getApplicationContext(), "Logged out successfully.", Toast.LENGTH_SHORT).show();
                Intent loginIntent = new Intent(getApplicationContext(), login.class);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginIntent);
                finish();
            }
        });

        // add new transaction details
        findViewById(R.id.btnQuickAdd).setOnClickListener(view -> {
            String txnTitle = ((EditText) findViewById(R.id.txtTxnTitle)).getText().toString();
            String rawAmt = ((EditText)findViewById(R.id.txtTxnAmount)).getText().toString();
            String txnDate = ((EditText)findViewById(R.id.txtTxnDate)).getText().toString();
            boolean isOk = verifyInputs(txnTitle, txnDate, rawAmt);
            if(isOk){
                double txnAmt = Double.parseDouble(rawAmt);
                txnAdapter.setCurrency(this.currency);
                TransactionType txnType = txnTypes[((Spinner)findViewById(R.id.txtTxnType)).getSelectedItemPosition()];
                TransactionModel txnDetail = new TransactionModel(txnTitle, txnDate, txnAmt, txnType);
                transactionModels.add(txnDetail);
                if(txnType.equals(TransactionType.CREDIT)){
                    txnAdapter.addTransactionIncome(txnAmt);
                }else if(txnType.equals(TransactionType.DEBIT)){
                    txnAdapter.addTransactionExpense(txnAmt);
                }
                txnAdapter.notifyItemInserted(transactionModels.size()-1);
                txnAdapter.notifyItemRangeChanged(transactionModels.size() - 1, transactionModels.size());
                txnAdapter.notifyDataSetChanged();
                updateSummary(txnAdapter.getTransactionSummary());
            }else{
                Log.e("Error", String.valueOf(new StringBuilder().append(txnTitle).append(" ").append(rawAmt).append(" ").append(txnDate).append(" ").append(isOk)));
            }
        });

        // fill transaction type spinner
        Spinner txnSpinner = findViewById(R.id.txtTxnType);
        ArrayList<String> txnTypesNames = new ArrayList<>();
        txnTypesNames.add(TransactionType.CREDIT.name());
        txnTypesNames.add(TransactionType.DEBIT.name());
        txnTypesNames.add(TransactionType.TRANSFER.name());

        ArrayAdapter adapter=new ArrayAdapter<>(dashboard.this, android.R.layout.simple_spinner_item, txnTypesNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        txnSpinner.setAdapter(adapter);

        EditText txnDate = findViewById(R.id.txtTxnDate);
        txnDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    showDialog(999);
                    return true;
                }
                return false;
            }
        });
        txnDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    showDialog(999);
                }
            }
        });

        ((ImageView)findViewById(R.id.currencyImage)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currencyImageRes = changeCurrencyType(currency);
                ((ImageView)findViewById(R.id.currencyImage)).setImageResource(currencyImageRes);
                updateSummary(txnAdapter.getTransactionSummary());
                txnAdapter.setCurrency(currency);
                txnAdapter.notifyDataSetChanged();
            }
        });

        findViewById(R.id.btnTransactionHistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Under development", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private TransactionSummary getPreviousSummary(){
        TransactionSummary txnSummary = new TransactionSummary();
        for(TransactionModel model : transactionModels){
            double amount = model.getAmount();
            switch (model.getType()){
                case DEBIT:
                    txnSummary.addExpenses(amount);
                    break;
                case CREDIT:
                    txnSummary.addIncome(amount);
                    break;
            }
        }
        txnSummary.setCurrencyType(currency);
        updateSummary(txnSummary);
        return txnSummary;
    }

    private void updateSummary(TransactionSummary txnSummary){
        Log.d("Warn", "updateSummary: updateSummaryCalled");
        TextView txtSavingsSummary = (TextView)findViewById(R.id.savingsSummary);
        ((TextView)findViewById(R.id.incomeSummary)).setText(currency.label + " " + String.valueOf(txnSummary.getIncome()));
        ((TextView)findViewById(R.id.expenseSummary)).setText(currency.label + " " + String.valueOf(txnSummary.getExpenses()));
        txtSavingsSummary.setText(currency.label + " " + String.valueOf(txnSummary.getSavings()));
        if(txnSummary.getSavings() < 0){
            txtSavingsSummary.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.redShade));
        }else{
            txtSavingsSummary.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.lightGreen));
        }
    }

    private int changeCurrencyType(CurrencyType previousCurrency){
        int currencyImageRes = 700134;
        switch (previousCurrency)
        {
            case DOLLAR:
                this.currency = CurrencyType.EURO;
                currencyImageRes = R.drawable.currency_euro;
                break;
            case RUPEE:
                this.currency = CurrencyType.DOLLAR;
                currencyImageRes = R.drawable.currency_dollar;
                break;
            case EURO:
                this.currency = CurrencyType.RUPEE;
                currencyImageRes = R.drawable.currency_rupee;
                break;
        }
        Toast.makeText(this, "Currency type changed to: " + this.currency.name(), Toast.LENGTH_SHORT).show();
        return currencyImageRes;
    }

    private ArrayList<TransactionModel> getTransactionModels(){
        return new ArrayList<>(); // test
    }

    private boolean verifyInputs(String title, String date, String amount){
        return !(title.isEmpty() || date.isEmpty() || amount.isEmpty());
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);

            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dpd = new DatePickerDialog(this,
                    myDateListener, year, month, day);
            dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
            return dpd;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    setDate(arg1, arg2+1, arg3);
                }
            };

    private void setDate(int year, int month, int day){
        EditText txnDate = findViewById(R.id.txtTxnDate);
        String strMonth = String.valueOf(month);
        String strDay = String.valueOf(day);
        if(strDay.length() == 1){
            strDay = "0" + strDay;
        }
        if(strMonth.length() == 1){
            strMonth = "0" + strMonth;
        }
        txnDate.setText(strDay + "-" + strMonth + "-" + String.valueOf(year));
    }
}