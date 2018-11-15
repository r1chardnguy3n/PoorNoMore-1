package com.douglas.poornomore;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        final DatabaseHelper dbh = new DatabaseHelper(getApplicationContext());

        TextView btnSignup = findViewById(R.id.btnSignup);
        final EditText txtSignin = findViewById(R.id.txtSignin);
        Button btnSignin = findViewById(R.id.btnSignin);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, CreateAccount.class));
                dbh.close();
            }
        });

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtSignin.getText().toString();

                if (dbh.login(email)) {
                    startActivity(new Intent(SignIn.this, MainActivity.class));
                    dbh.close();

                } else {
                    Toast.makeText(getApplicationContext(),"Account not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
