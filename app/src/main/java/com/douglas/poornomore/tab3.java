package com.douglas.poornomore;

import android.database.Cursor;
import android.database.DatabaseUtils;
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
import android.widget.Toast;

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
        final EditText savingsCat = rootView.findViewById(R.id.AddSavings);
        final EditText savingsAmt = rootView.findViewById(R.id.transferAmt);
        final Spinner fromSav = rootView.findViewById(R.id.spinnerFrom);
        final Spinner toSav = rootView.findViewById(R.id.spinnerTo);
        Button addSavingsBtn = rootView.findViewById(R.id.addSavingsBtn);
        final Button moveSav = rootView.findViewById(R.id.MoveSavings);

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

        addSavingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dbh.addSavings(savingsCat.getText().toString())) {
                    Toast.makeText(getActivity(),"Added savings account",Toast.LENGTH_SHORT).show();
                    savingsCat.setText("");
                } else {
                    Toast.makeText(getActivity(),"Failed to add savings account",Toast.LENGTH_SHORT).show();
                }

            }
        });

        final List<String> arraySavings = new ArrayList<>();

        Cursor savings = dbh.getSavings();

        while (savings.moveToNext()) {
            arraySavings.add(savings.getString(0));
        }

        savings.close();

        ArrayAdapter<String> savingsAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, arraySavings);
        savingsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSav.setAdapter(savingsAdapter);
        toSav.setAdapter(savingsAdapter);

        moveSav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor acct = dbh.getSavingsAmt(fromSav.getSelectedItem().toString());
                acct.moveToNext();
                double acctAmt = acct.getDouble(0);
                double moveAmt = 0;

                try {
                    moveAmt = Double.parseDouble(savingsAmt.getText().toString());

                    if (!fromSav.getSelectedItem().toString().equals(toSav.getSelectedItem().toString())) {
                        if (moveAmt < acctAmt) {
                            if (dbh.moveSavings(fromSav.getSelectedItem().toString(), toSav.getSelectedItem().toString(), moveAmt)) {
                                Toast.makeText(getActivity(),"Transfer success!", Toast.LENGTH_SHORT).show();
                                moveSav.setText("");
                            } else {
                                Toast.makeText(getActivity(),"Transfer failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(),"Insufficient funds to transfer", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(),"Please select a different account", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(getActivity(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
                }
                acct.close();
            }
        });

        return rootView;

    }

}
