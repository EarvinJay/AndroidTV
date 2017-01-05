package com.itproject.android.androidtvsample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class WelcomeActivity extends AppCompatActivity {

    Button mbtnstart;
    EditText mtxtemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mbtnstart=(Button) findViewById(R.id.btnstart);
        mtxtemail=(EditText) findViewById(R.id.txtemail);

        mbtnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WelcomeActivity.this,AcceptReserveActivity.class);
                intent.putExtra("EMAIL",mtxtemail.getText().toString());
                startActivity(intent);
            }
        });
    }
}
