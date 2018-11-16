package com.douglas.poornomore;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddExpense extends AppCompatActivity {

    Dialog chooseCategoryPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        final DatabaseHelper dbh = new DatabaseHelper(getApplicationContext());

        final Calendar calendar = Calendar.getInstance();
        final TextView selectDate = findViewById(R.id.transDate);
        final TextView textCategory = (TextView)findViewById(R.id.chooseCategory);
        chooseCategoryPopup = new Dialog(this);

        final EditText txtAmt = findViewById(R.id.transAmt);
        Button btnAdd = findViewById(R.id.btnAdd);

        //Select Date Methods
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                selectDate.setText(String.format(Locale.getDefault(),"%d/%d/%d", month+1, dayOfMonth, year));

            }
        };

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddExpense.this, date, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Choose category method call
        textCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCategoryPopup(v);
            }
        });



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectDate.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"Please enter the date",Toast.LENGTH_SHORT).show();
                }

                double amt = Double.parseDouble(txtAmt.getText().toString());

                if (dbh.addTrans(new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).format(calendar.getTime()),"Name","Category",amt)) {
                    Toast.makeText(getApplicationContext(),"Added transaction",Toast.LENGTH_SHORT).show();
                    dbh.updLimit(amt);
                    finish();
                }

            }
        });
    }

    //Method for category popup menu
    public void ShowCategoryPopup(View v){
        chooseCategoryPopup.setContentView(R.layout.choosecategorypopup);
        chooseCategoryPopup.show();
        chooseCategoryPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final CardView cvTransport, cvGroceries, cvLiqour, cvRestaurants, cvClothing, cvOther ;
        final TextView txtCancel,txtChooseCategory;

        cvTransport = (CardView)chooseCategoryPopup.findViewById(R.id.transportationCategory);
        cvGroceries = (CardView)chooseCategoryPopup.findViewById(R.id.groceriesCategory);
        cvLiqour = (CardView)chooseCategoryPopup.findViewById(R.id.liquorCategory);
        cvRestaurants = (CardView)chooseCategoryPopup.findViewById(R.id.restaurantsCetegory);
        cvClothing = (CardView)chooseCategoryPopup.findViewById(R.id.clothingCategory);
        cvOther = (CardView)chooseCategoryPopup.findViewById(R.id.otherCategory);
        txtCancel = (TextView)chooseCategoryPopup.findViewById(R.id.cancelCategory);
        txtChooseCategory = (TextView)findViewById(R.id.chooseCategory);

        //Transportation On Click
        cvTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvTransport.setCardBackgroundColor(getResources().getColor(R.color.gradGreen));
                Drawable img = getBaseContext().getResources().getDrawable(R.drawable.commutewhite);
                txtChooseCategory.setText("Transportation");
                txtChooseCategory.setCompoundDrawablesWithIntrinsicBounds(img,null,null,null);
                chooseCategoryPopup.dismiss();

            }
        });

        //Groceries On Click
        cvGroceries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable img = getBaseContext().getResources().getDrawable(R.drawable.grocerywhite);
                txtChooseCategory.setCompoundDrawablesWithIntrinsicBounds(img,null,null,null);
                txtChooseCategory.setText("Groceries");
                chooseCategoryPopup.dismiss();
            }
        });

        //Liquor On Click
        cvLiqour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable img = getBaseContext().getResources().getDrawable(R.drawable.liqourwhite);
                txtChooseCategory.setCompoundDrawablesWithIntrinsicBounds(img,null,null,null);
                txtChooseCategory.setText("Liqour");
                chooseCategoryPopup.dismiss();
            }
        });

        //Restaurant On Click
        cvRestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable img = getBaseContext().getResources().getDrawable(R.drawable.diningwhite);
                txtChooseCategory.setCompoundDrawablesWithIntrinsicBounds(img,null,null,null);
                txtChooseCategory.setText("Restaurants");
                chooseCategoryPopup.dismiss();
            }
        });

        //Clothing On Click
        cvClothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable img = getBaseContext().getResources().getDrawable(R.drawable.storewhite);
                txtChooseCategory.setCompoundDrawablesWithIntrinsicBounds(img,null,null,null);
                txtChooseCategory.setText("Clothing");
                chooseCategoryPopup.dismiss();
            }
        });

        //Other On Click
        cvOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable img = getBaseContext().getResources().getDrawable(R.drawable.helpwhite);
                txtChooseCategory.setCompoundDrawablesWithIntrinsicBounds(img,null,null,null);
                txtChooseCategory.setText("Other");
                chooseCategoryPopup.dismiss();
            }
        });


        //Cancel On Click
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtCancel.setTextColor(getResources().getColor(R.color.cancelRed));
                chooseCategoryPopup.dismiss();
            }
        });


    }


}
