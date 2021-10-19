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


public class RegistrationActivity extends AppCompatActivity {
    EditText email, username, password;
    Button sign_up_button, go_to_sign_in_button;
    FirebaseAuth auth_user;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        email = findViewById(R.id.sign_up_email);
        username = findViewById(R.id.sign_up_username);
        password = findViewById(R.id.sign_up_password);
        sign_up_button = findViewById(R.id.sign_up_button);
        go_to_sign_in_button = findViewById(R.id.go_to_sign_in_button);
        auth_user = FirebaseAuth.getInstance();
        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                if (v.getId() == R.id.sign_up_button) {
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("users");

                    String username_text = username.getText().toString().trim();
                    String email_text = email.getText().toString().trim();
                    String password_text = password.getText().toString().trim();
                    if (TextUtils.isEmpty(username_text)) {
                        username.setError("Can't create an account with empty username");
                        username.requestFocus();
                    } else if (TextUtils.isEmpty(password_text)) {
                        password.setError("Please enter your password");
                        password.requestFocus();
                    } else if (TextUtils.isEmpty(email_text)) {
                        email.setError("Please enter your Email address");
                        email.requestFocus();
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(email_text).matches()) {
                        email.setError("Please enter a valid email address");
                        email.requestFocus();
                    } else if (password_text.length() < 8) {
                        password.setError("Password should be minimum 8 characters long");
                        password.requestFocus();
                    } else {
                        auth_user.createUserWithEmailAndPassword(email_text, password_text)
                                .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (!task.isSuccessful()) {
                                            Toast.makeText(RegistrationActivity.this, "ERROR", Toast.LENGTH_LONG).show();
                                        } else {
                                            User newuser = new User(username_text, email_text, password_text);
                                            reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(newuser);
                                            Toast.makeText(RegistrationActivity.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
                                            finish();
                                        }
                                    }
                                });
                    }
                }
                else if (v.getId() == R.id.go_to_sign_in_button) {
                    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
        sign_up_button.setOnClickListener(buttonClickListener);
        go_to_sign_in_button.setOnClickListener(buttonClickListener);
    }
}