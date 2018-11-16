package com.douglas.poornomore;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.Locale;

public class tab3 extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_three, container, false);

        TextView name = rootView.findViewById(R.id.name_value);
        TextView email = rootView.findViewById(R.id.email_value);
        TextView income = rootView.findViewById(R.id.income_value);

        final DatabaseHelper dbh = new DatabaseHelper(getActivity());
        Cursor c = dbh.getClient();

        while (c.moveToNext()) {
            String nm = c.getString(0);
            String em = c.getString(1);
            double in = c.getDouble(2);

            name.setText(nm);
            email.setText(em);
            income.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).format(in));
        }

        return rootView;
    }
}
