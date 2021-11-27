package com.example.socialley;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewProfile extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    TextView usernameField, emailField, nationalityField, genderField, locationField;
    ImageView profilePicField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        usernameField = findViewById(R.id.other_username);
        emailField = findViewById(R.id.other_email);
        nationalityField = findViewById(R.id.other_nationality);
        genderField = findViewById(R.id.other_gender);
        locationField = findViewById(R.id.other_location);
        profilePicField = findViewById(R.id.other_profile_image);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String email = extras.getString("email");
            if (email != null) {
                databaseReference.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            User user = snapshot1.getValue(User.class);
                            if (snapshot1.exists()) {
                                usernameField.setText("Username : " + user.getUsername());
                                emailField.setText("Email : " + user.getEmail());
                                if (!user.getProfilePic().equals("")) {
                                    try {
                                        Glide.with(getApplicationContext()).load(user.getProfilePic()).into(profilePicField);
                                    }
                                    catch (Exception e) {

                                    }
                                }
                                nationalityField.setText("Nationality : Indian");
                                locationField.setText("Location : AP");
                                genderField.setText("Gender : Male");

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            else {
                Toast.makeText(getApplicationContext(), "Unable to access account details", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Unable to access account details", Toast.LENGTH_SHORT).show();
        }
    }
}