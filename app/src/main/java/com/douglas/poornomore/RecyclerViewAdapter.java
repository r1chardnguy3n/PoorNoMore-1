package com.douglas.poornomore;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    List<ExpenseData> mData;
    Context mContext;

    public RecyclerViewAdapter(Context mContext, List<ExpenseData> mData) {
        this.mContext = mContext;
        this.mData = mData;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_expense, viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.dateText.setText(mData.get(i).getmDate());
        viewHolder.categoryText.setText(mData.get(i).getmCategory());
        viewHolder.amountText.setText(mData.get(i).getmAmount());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView dateText, categoryText, amountText;
        CardView parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.expense_date);
            categoryText = itemView.findViewById(R.id.expense_category);
            amountText = itemView.findViewById(R.id.expense_amount);
            parentLayout = itemView.findViewById(R.id.parent_Layout);
        }
    }
}
