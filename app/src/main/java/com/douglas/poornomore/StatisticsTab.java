package com.douglas.poornomore;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StatisticsTab extends Fragment {

    private Context mContext;
    LinearLayout mLayout;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_one, container, false);

        double totExp = 0;
        double traExp = 0;
        double groExp = 0;
        double liqExp = 0;
        double cloExp = 0;
        double resExp = 0;
        double othExp = 0;


        final DatabaseHelper dbh = new DatabaseHelper(getActivity());
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        TextView dailyAmt = rootView.findViewById(R.id.dailyAmount);
        TextView totAmt = rootView.findViewById(R.id.totalExpensesAmount);
        TextView traAmt = rootView.findViewById(R.id.transportation_Value);
        TextView groAmt = rootView.findViewById(R.id.groceries_value);
        TextView liqAmt = rootView.findViewById(R.id.liquor_value);
        TextView cloAmt = rootView.findViewById(R.id.clothing_value);
        TextView resAmt = rootView.findViewById(R.id.restaurants_value);
        TextView othAmt = rootView.findViewById(R.id.other_value);

        if (dbh.onOpen()) {
            Toast.makeText(getActivity(),"Updated records for today", Toast.LENGTH_SHORT).show();
        }

        Cursor limit = dbh.getLimit();
        while (limit.moveToNext()) {
            double amt = limit.getDouble(0);
            dailyAmt.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).format(amt));
        }
        limit.close();

        Cursor trans = dbh.getTransMo(new SimpleDateFormat("yyyy-MM", Locale.CANADA).format(new Date()));
        while (trans.moveToNext()) {
            double amt = trans.getDouble(4);
            switch (trans.getString(3)) {
                case "Transportation":
                    traExp += amt;
                    break;
                case "Groceries":
                    groExp += amt;
                    break;
                case "Liquor":
                    liqExp += amt;
                    break;
                case "Clothing":
                    cloExp += amt;
                    break;
                case "Restaurants":
                    resExp += amt;
                    break;
                default:
                    othExp += amt;
                    break;
            }
            totExp += amt;
        }
        trans.close();

        totAmt.setText(nf.format(totExp));
        traAmt.setText(nf.format(traExp));
        groAmt.setText(nf.format(groExp));
        liqAmt.setText(nf.format(liqExp));
        cloAmt.setText(nf.format(cloExp));
        resAmt.setText(nf.format(resExp));
        othAmt.setText(nf.format(othExp));

        //Dynamically add savings cards
        Cursor savings = dbh.getSavings();
        View view;

        while (savings.moveToNext()) {
            view = inflater.inflate(R.layout.card_template, null);
            ViewGroup main = rootView.findViewById(R.id.insertPoint);
            TextView text = view.findViewById(R.id.textTemplate);
            TextView value = view.findViewById(R.id.valueTemplate);

            text.setText(savings.getString(0));
            value.setText(nf.format(savings.getDouble(1)));

            main.addView(view);
        }

        return rootView;

    }
}
