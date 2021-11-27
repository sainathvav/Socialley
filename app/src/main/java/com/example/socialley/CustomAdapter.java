package com.example.socialley;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyHolder> {

    Context context;
    FirebaseAuth firebaseAuth;
    String uid;

    public CustomAdapter(Context context, List<User> list) {
        this.context = context;
        this.list = list;
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getUid();
    }

    List<User> list;

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_users, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        String userProfilePic = list.get(position).getProfilePic();
        String userName = list.get(position).getUsername();
        String userEmail = list.get(position).getEmail();
        holder.name.setText(userName);
        holder.email.setText(userEmail);
        if (userProfilePic != "") {
            try {
                Glide.with(context).load(userProfilePic).into(holder.profilePic);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        CircleImageView profilePic;
        TextView name, email;
        Button addFriend;

        FirebaseAuth firebaseAuth;
        FirebaseUser firebaseUser;
        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("users");
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();

            profilePic = itemView.findViewById(R.id.profilePicUser);
            name = itemView.findViewById(R.id.nameUser);
            email = itemView.findViewById(R.id.emailUser);
            addFriend = itemView.findViewById(R.id.add_friend);

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(itemView.getContext(), ViewProfile.class);
                    i.putExtra("email", email.getText());
                    itemView.getContext().startActivity(i);
                }
            });

        }
    }
}
