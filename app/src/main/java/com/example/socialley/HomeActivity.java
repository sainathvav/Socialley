package com.example.socialley;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;

public class HomeActivity extends AppCompatActivity {
    Button logout_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        logout_button  = findViewById(R.id.log_out_button);
        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                if (v.getId() == R.id.log_out_button) {
                    FirebaseAuth.getInstance().signOut();
                }
            }
        };
    }
}
