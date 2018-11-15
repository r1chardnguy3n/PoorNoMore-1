package com.douglas.poornomore;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddExpense extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        final DatabaseHelper dbh = new DatabaseHelper(getApplicationContext());

        final Calendar calendar = Calendar.getInstance();
        final TextView selectDate = findViewById(R.id.transDate);
        final EditText txtAmt = findViewById(R.id.transAmt);
        Button btnAdd = findViewById(R.id.btnAdd);

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
}
