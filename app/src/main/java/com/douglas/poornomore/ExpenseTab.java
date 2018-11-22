package com.douglas.poornomore;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ExpenseTab extends Fragment {


    private RecyclerView recyclerView;
    private List<ExpenseData> expenseList;

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_two, container, false);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(),expenseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        return rootView;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseHelper dbh = new DatabaseHelper(getActivity());
        expenseList = new ArrayList<>();
        Cursor cursor = dbh.getTransMo(new SimpleDateFormat("yyyy-MM", Locale.CANADA).format(new Date()));

        while (cursor.moveToNext())
        {
            String date = cursor.getString(1);
            String category = cursor.getString(3);
            double amount = cursor.getDouble(4);

            expenseList.add(new ExpenseData(date, category,NumberFormat.getCurrencyInstance(Locale.getDefault()).format(amount)));
        }






        cursor.close();

    }





}
