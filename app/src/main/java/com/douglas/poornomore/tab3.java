package com.douglas.poornomore;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
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


        //
        final EditText savingsCat = rootView.findViewById(R.id.AddSavings);
        Button addSavingsBtn = rootView.findViewById(R.id.addSavingsBtn);

        final List<String> arraySavings = new ArrayList<>();
        arraySavings.add("Savings");

        addSavingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arraySavings.add(savingsCat.getText().toString());

            }
        });

        Spinner s = (Spinner) rootView.findViewById(R.id.spinnerFrom);
        ArrayAdapter<String> savingsAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, arraySavings);
        savingsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(savingsAdapter);

        //


        return rootView;

    }

}
