package com.example.socialley;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Patterns;
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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    Button sign_in_button;
    FirebaseAuth auth_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth_user = FirebaseAuth.getInstance();
        email = findViewById(R.id.sign_in_email);
        password = findViewById(R.id.sign_in_password);
        sign_in_button = findViewById(R.id.sign_in_button);
        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                    String email_text = email.getText().toString().trim();
                    String password_text = password.getText().toString().trim();
                    if (TextUtils.isEmpty(email_text)) {
                        email.setError("Please enter your Email address");
                        email.requestFocus();
                    }
                    else if (TextUtils.isEmpty(password_text)) {
                        password.setError("Please enter your password");
                        password.requestFocus();
                    }
                    else if (!Patterns.EMAIL_ADDRESS.matcher(email_text).matches()) {
                        email.setError("Please enter a valid Email address");
                        email.requestFocus();
                    }
                    else {
                        auth_user.signInWithEmailAndPassword(email_text, password_text)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            // there was an error
                                            if (password.length() < 8) {
                                                Toast.makeText(getApplicationContext(),"Password must be more than 8 digit",Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
                    }
            }
        };
        sign_in_button.setOnClickListener(buttonClickListener);
    }
}