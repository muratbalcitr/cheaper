package com.complaint.kimyonsal.cheaper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class register extends AppCompatActivity {

    Button btnRegister;
    EditText etUsername, etMail, etPassword, etPasswordRepeat;
    RadioButton rbtnErkek, rbtnKadin;
    RadioGroup rg;
    FirebaseAuth frRegisterAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mDatabaseReference;
    String SrbtnErkek, SrbtnKadin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etUsername = (EditText) findViewById(R.id.etAdsoyad);
        etMail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPasswordRepeat = (EditText) findViewById(R.id.etPasswordRepeat);
        // find the radiobutton by returned id
        rbtnKadin = (RadioButton) findViewById(R.id.rbtnKadin);
        rbtnErkek = (RadioButton) findViewById(R.id.rbtnErkek);
        rbtnErkek.toggle();
        frRegisterAuth = FirebaseAuth.getInstance();
    }

    String RadSoyad, Remail;

    private void register() {
        RadSoyad = etUsername.getText().toString().trim();
        Remail = etMail.getText().toString().trim();
        String Rpassword = etPassword.getText().toString().trim();
        String RPasswordRepeat = etPasswordRepeat.getText().toString().trim();
        String erkek = rbtnErkek.getText().toString().trim();
        String kadin = rbtnKadin.getText().toString().trim();


        if (RadSoyad.length() < 5) {
            Toast.makeText(register.this, "Kullanıcı adını boş olamaz ve en az 5 harfli olacak  ", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(Remail)) {
            Toast.makeText(register.this, "e-mail'i boş bırakmayınız!", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(Rpassword)) {
            Toast.makeText(register.this, "parola giriniz ", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(RPasswordRepeat)) {
            Toast.makeText(register.this, "parola giriniz", Toast.LENGTH_SHORT).show();
        }

        if (!(Rpassword.equals(RPasswordRepeat))) {
            Toast.makeText(register.this, "parolalar uyuşmuyor", Toast.LENGTH_SHORT).show();
        }
        if (Rpassword.length() < 6) {
            Toast.makeText(register.this, "parola en az 6 karakter olmalı", Toast.LENGTH_SHORT).show();
        }

        if (Rpassword.length() >= 6 && Rpassword.equals(RPasswordRepeat) && RadSoyad.length() >= 5 && !Remail.matches("")) {


            frRegisterAuth.createUserWithEmailAndPassword(Remail, Rpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful() && rbtnErkek.isChecked()) {
                        FirebaseUser user = task.getResult().getUser();
                        String userId = user.getUid();
                        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("cheaper_users");

                        mDatabaseReference.child(userId).child("username").setValue(RadSoyad);

                        mDatabaseReference.child(userId).child("cinsiyet").setValue("erkek");

                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent i = new Intent(register.this, loginActivity.class);
                                startActivity(i);
                                Toast.makeText(register.this, "kayıt başarılı lütfen mailinizi kontrol ediniz", Toast.LENGTH_SHORT).show();
                            }
                        });


                    } else if (task.isSuccessful() && rbtnKadin.isChecked()) {
                        FirebaseUser user = task.getResult().getUser();
                        String userId = user.getUid();

                        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("cheaper_users");

                        mDatabaseReference.child(userId).child("username").setValue(RadSoyad);

                        mDatabaseReference.child(userId).child("cinsiyet").setValue("kadın");
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent i = new Intent(register.this, loginActivity.class);
                                startActivity(i);
                                Toast.makeText(register.this, "kayıt başarılı lütfen mailinizi kontrol ediniz", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                }
            });
        }
    }

    public void RegisterButton(View view) {
        register();
    }
}