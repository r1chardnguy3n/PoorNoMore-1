package com.douglas.poornomore;


import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ExpenseTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_two, container, false);

        final DatabaseHelper dbh = new DatabaseHelper(getActivity());
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        TextView title = rootView.findViewById(R.id.title_expenses);

        title.setText(new SimpleDateFormat("MMM dd, yyyy", Locale.CANADA).format(new Date()));

        View view;
        Cursor trans = dbh.getTrans(new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).format(new Date()));

        while (trans.moveToNext()) {
            view = inflater.inflate(R.layout.card_template, null);
            ViewGroup main = rootView.findViewById(R.id.insertPoint);
            ImageView image = view.findViewById(R.id.imgTemplate);
            TextView text = view.findViewById(R.id.textTemplate);
            TextView value = view.findViewById(R.id.valueTemplate);

            switch (trans.getString(3)) {
                case "Transportation":
                    image.setImageResource(R.drawable.round_commute_black_36dp);
                    break;
                case "Groceries":
                    image.setImageResource(R.drawable.round_local_grocery_store_black_36dp);
                    break;
                case "Liquor":
                    image.setImageResource(R.drawable.round_local_bar_black_36dp);
                    break;
                case "Clothing":
                    image.setImageResource(R.drawable.round_store_mall_directory_black_36dp);
                    break;
                case "Restaurants":
                    image.setImageResource(R.drawable.round_local_dining_black_36dp);
                    break;
            }

            text.setText(trans.getString(3));
            value.setText(nf.format(trans.getDouble(4)));

            main.addView(view);
        }

        trans.close();

        return rootView;

    }
}
