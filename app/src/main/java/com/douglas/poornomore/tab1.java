package com.douglas.poornomore;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.Locale;

public class tab1 extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_one, container, false);

        final DatabaseHelper dbh = new DatabaseHelper(getActivity());
        TextView dailyAmt = rootView.findViewById(R.id.dailyAmount);

        if (dbh.onOpen()) {
            Toast.makeText(getActivity(),"Updated records for today", Toast.LENGTH_SHORT).show();
        }

        Cursor limit = dbh.getLimit();
        while (limit.moveToNext()) {
            double amt = limit.getDouble(0);
            dailyAmt.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).format(amt));
        }
        return rootView;
    }
}
