package com.programmingtubeofficial.expensr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class transaction_history extends AppCompatActivity {
    ArrayList<TransactionModel> transactionModelList = new ArrayList<>();
    TransactionAdapter txnAdapter;
    CurrencyType currencyType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        transactionModelList = (ArrayList<TransactionModel>) getIntent().getSerializableExtra("models");
        currencyType = (CurrencyType) getIntent().getSerializableExtra("currency");
        RecyclerView txnList = findViewById(R.id.txnHistoryList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        txnList.setLayoutManager(layoutManager);
        txnAdapter = new TransactionAdapter(transaction_history.this, transactionModelList, currencyType);
        txnAdapter.setRecent(false);
        txnAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                updateSummary(txnAdapter.getTransactionSummary());
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                updateSummary(txnAdapter.getTransactionSummary());
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                updateSummary(txnAdapter.getTransactionSummary());
            }
        });
        for (TransactionModel txnModel :
                transactionModelList) {
            if(txnModel.getType().equals(TransactionType.CREDIT)){
                txnAdapter.addTransactionIncome(txnModel.getAmount());
            }else if(txnModel.getType().equals(TransactionType.DEBIT)){
                txnAdapter.addTransactionExpense(txnModel.getAmount());
            }
        }
        updateSummary(txnAdapter.getTransactionSummary());
        ((RecyclerView)findViewById(R.id.txnHistoryList)).setAdapter(txnAdapter);

        ImageView lblBack = findViewById(R.id.imgBackHist);
        lblBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDashboard();
            }
        });
    }

    private void openDashboard(){
        Intent loginIntent = new Intent(transaction_history.this, dashboard.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(loginIntent);
        finish();
    }


    private void updateSummary(TransactionSummary txnSummary){
        TextView txtSavingsSummary = (TextView)findViewById(R.id.savingsSummaryHist);
        ((TextView)findViewById(R.id.incomeSummaryHist)).setText(currencyType.label + " " + String.valueOf(txnSummary.getIncome()));
        ((TextView)findViewById(R.id.expenseSummaryHist)).setText(currencyType.label + " " + String.valueOf(txnSummary.getExpenses()));
        txtSavingsSummary.setText(currencyType.label + " " + String.valueOf(txnSummary.getSavings()));
        if(txnSummary.getSavings() < 0){
            txtSavingsSummary.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.redShade));
        }else{
            txtSavingsSummary.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.lightGreen));
        }
    }
}