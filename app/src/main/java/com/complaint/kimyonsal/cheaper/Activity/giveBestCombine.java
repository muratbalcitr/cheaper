package com.complaint.kimyonsal.cheaper.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.complaint.kimyonsal.cheaper.R;
import com.complaint.kimyonsal.cheaper.config;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by kimyonsal on 18.04.2017.
 */

public class giveBestCombine extends AppCompatActivity{
    private Button btnSend;
    private EditText edtForm;
    private String item[];
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine);
        config.fbdataBase = FirebaseDatabase.getInstance();
        config.dbRef=config.fbdataBase.getReference().child("Posts");

        item= new String[]{"Teknoloji", "Giyim", "Yiyecek-Icecek", "PaketSecim"};
        btnSend= (Button) findViewById(R.id.btnSend);
        edtForm=(EditText)findViewById(R.id.edtValue);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getValue= edtForm.getText().toString();
                config.dbRef.push().setValue(getValue);
            }
        });


    }

}
