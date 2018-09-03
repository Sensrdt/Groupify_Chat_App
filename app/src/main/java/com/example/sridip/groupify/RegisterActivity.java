package com.example.sridip.groupify;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private EditText rname, remail, epass;
    private Button reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");

        rname = findViewById(R.id.name);
        remail = findViewById(R.id.email);
        epass = findViewById(R.id.password);
        reg = findViewById(R.id.register);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String namer = rname.getEditableText().toString().trim();
                String emailr = remail.getEditableText().toString().trim();
                String passr = epass.getEditableText().toString().trim();

                registernow(namer, emailr, passr);
            }
        });


    }

    private void registernow( final String namer, String emailr, String passr)
    {
        mAuth.createUserWithEmailAndPassword(emailr, passr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            databaseReference.child(mAuth.getCurrentUser().getUid()).child("basic").child("name").setValue(namer).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        Intent donereg = new Intent (RegisterActivity.this,StartActivity.class);
                                        startActivity(donereg);

                                    } else {
                                        Toast.makeText(RegisterActivity.this,"Unsuccesfull!!",Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        } else {
                            Toast.makeText(RegisterActivity.this,"Gone Case!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
