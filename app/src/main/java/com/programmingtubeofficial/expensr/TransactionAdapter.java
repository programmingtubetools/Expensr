package com.programmingtubeofficial.expensr;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder>{

    private Context context;
    private ArrayList<TransactionModel> transactionModels;
    private CurrencyType currency;
    private DatabaseReference databaseReference;
    private TransactionSummary transactionSummary;
    private boolean recent = true;

    public boolean isRecent() {
        return recent;
    }

    public void setRecent(boolean recent) {
        this.recent = recent;
    }

    public void setTransactionSummary(TransactionSummary transactionSummary) {
        this.transactionSummary = transactionSummary;
    }

    public void addTransactionExpense(double amount){
        this.transactionSummary.addExpenses(amount);
    }

    public void addTransactionIncome(double amount){
        this.transactionSummary.addIncome(amount);
    }

    public void removeTransactionExpense(double amount){
        this.transactionSummary.removeExpenses(amount);
    }

    public void removeTransactionIncome(double amount){
        this.transactionSummary.removeIncome(amount);
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public TransactionSummary getTransactionSummary() {
        return transactionSummary;
    }

    public void setCurrency(CurrencyType currency) {
        this.currency = currency;
    }

    private static final DecimalFormat df = new DecimalFormat("0.00");
    public TransactionAdapter(Context context, ArrayList<TransactionModel> transactionModels, CurrencyType currency) {
        this.context = context;
        this.transactionModels = transactionModels;
        this.currency = currency;
        this.transactionSummary = new TransactionSummary();
        //this.initTransactionSummary();
    }

    private void initTransactionSummary(){
        this.transactionSummary = new TransactionSummary();
        for (TransactionModel model :
                this.transactionModels) {
            double amount = model.getAmount();
            if(model.getType().equals(TransactionType.DEBIT)){
                this.transactionSummary.addExpenses(amount);
            }else if(model.getType().equals(TransactionType.CREDIT)){
                this.transactionSummary.addIncome(amount);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionModel model = transactionModels.get(position);

        holder.txnTitle.setText(model.getTitle());
        holder.txnAmt.setText(new StringBuilder().append(this.currency.label).append(" ").append(df.format(model.getAmount())).toString());
        holder.txnIcon.setImageResource(model.getIcon());
        holder.txnDate.setText(model.getDate());
        if(model.getType().equals(TransactionType.DEBIT)){
            holder.txnAmt.setTextColor(ContextCompat.getColor(context, R.color.redShade));
        }else if(model.getType().equals(TransactionType.CREDIT)){
            holder.txnAmt.setTextColor(ContextCompat.getColor(context, R.color.moneyGreen));
        }else{
            holder.txnAmt.setTextColor(ContextCompat.getColor(context, R.color.cobaltBlue));
        }
        // save to database
        //updateTransaction(context, databaseReference, model, false);
    }
    public static void updateTransaction(Context ctx, DatabaseReference dbRef, TransactionModel txnDetail, boolean delete) {
        if(delete){
            ValueEventListener deleteListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot deleteSnapshot: snapshot.getChildren()) {
                        TransactionModel deleteModel = deleteSnapshot.getValue(TransactionModel.class);
                        if(deleteModel.getId() == txnDetail.getId() && deleteModel.getUser().equals(txnDetail.getUser())){
                            deleteSnapshot.getRef().removeValue();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            try {
                dbRef.addListenerForSingleValueEvent(deleteListener);
            }catch (Exception e){
                Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return;
        }
        try{
            dbRef.push().setValue(txnDetail);
        }catch (Exception e){
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        
        return this.transactionModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView txnIcon, btnRemove;
        private TextView txnTitle, txnAmt, txnDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            if((getItemCount() > 0) && (getAdapterPosition() > -1)){
                TransactionModel model = transactionModels.get(getAdapterPosition());
                double amount = model.getAmount();
                if(model.getType().equals(TransactionType.DEBIT)){
                    transactionSummary.addExpenses(amount);
                }else if(model.getType().equals(TransactionType.CREDIT)){
                    transactionSummary.addIncome(amount);
                }
            }
            txnIcon = itemView.findViewById(R.id.txnIcon);
            txnTitle = itemView.findViewById(R.id.txnTitle);
            txnDate = itemView.findViewById(R.id.txnDate);
            txnAmt = itemView.findViewById(R.id.txnAmt);
            btnRemove = itemView.findViewById(R.id.btnRemoveTxnDetails);
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    TransactionModel prevModel = transactionModels.get(pos);
                    // delete transaction
                    updateTransaction(context, databaseReference, prevModel, true);
                    if(prevModel.getType().equals(TransactionType.CREDIT)){
                        transactionSummary.removeIncome(prevModel.getAmount());
                    }else if(prevModel.getType().equals(TransactionType.DEBIT)){
                        transactionSummary.removeExpenses(prevModel.getAmount());
                    }
                    transactionModels.remove(pos);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, transactionModels.size());
                }
            });
        }
    }
}
