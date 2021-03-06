package com.douglas.poornomore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        final DatabaseHelper dbh = new DatabaseHelper(getApplicationContext());

        final EditText txtName = findViewById(R.id.txtName);
        final EditText txtEmail = findViewById(R.id.txtEmail);
        final EditText txtIncome = findViewById(R.id.txtIncome);
        Button btnCreate = findViewById(R.id.btnCreate);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txtName.getText().toString();
                String email = txtEmail.getText().toString();
                double income = Double.parseDouble(txtIncome.getText().toString());

                if (dbh.addClient(name, email, income)) {
                    if (dbh.login(email)) {
                        if (dbh.addSavings("Uncategorized")) {
                            startActivity(new Intent(CreateAccount.this, MainActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Failed to create savings account", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),"Failed to login", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Failed to create account", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
