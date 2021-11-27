package com.example.socialley;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CustomPostAdapter extends RecyclerView.Adapter<com.example.socialley.CustomPostAdapter.MyHolder> {

    Context context;
    String myuid;
    private DatabaseReference liekeref, postref;
    boolean mprocesslike = false;

    public CustomPostAdapter(Context context, List<Post> modelPosts) {
        this.context = context;
        this.posts = modelPosts;
        myuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        liekeref = FirebaseDatabase.getInstance().getReference().child("Likes");
        postref = FirebaseDatabase.getInstance().getReference().child("Posts");
    }

    List<Post> posts;

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.all_posts, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") final int position) {
        String uid = posts.get(position).getUid();
        String uname = posts.get(position).getUname();
        String title = posts.get(position).getTitle();
        String description = posts.get(position).getDescription();
        String ptime = posts.get(position).getPtime();
        String pimage = posts.get(position).getPimage();
        String plikes= posts.get(position).getPlike();
        String uimage = posts.get(position).getUimage();
        String uemail = posts.get(position).getUemail();
        String pcomments = posts.get(position).getPcomments();
        //final String pid = posts.get(position).getPtime();
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(ptime));
        String timedate = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        holder.name.setText(uname);
        holder.title.setText(title);
        holder.description.setText(description);
        holder.time.setText(timedate);
        holder.like.setText(plikes + " Likes");
        holder.comments.setText(pcomments + " Comments");

        //setLikes(holder, ptime);
        try {
            Glide.with(context).load(pimage).into(holder.picture);
        } catch (Exception e) {

        }
        holder.image.setVisibility(View.VISIBLE);
        try {
            Glide.with(context).load(uimage).into(holder.image);
        } catch (Exception e) {

        }


        holder.likebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int plike = Integer.parseInt(posts.get(position).getPlike());
                mprocesslike = true;
                final String postid = posts.get(position).getPtime();
                liekeref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (mprocesslike) {
                            if (dataSnapshot.child(postid).hasChild(myuid)) {
                                postref.child(postid).child("plikes").setValue("" + (plike - 1));
                                liekeref.child(postid).child(myuid).removeValue();
                                mprocesslike = false;
                            } else {
                                postref.child(postid).child("plikes").setValue("" + (plike + 1));
                                liekeref.child(postid).child(myuid).setValue("Liked");
                                mprocesslike = false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void deletePost(final String pid, String image) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Deleting");
        StorageReference picref = FirebaseStorage.getInstance().getReferenceFromUrl(image);
        picref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Query query = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("ptime").equalTo(pid);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            dataSnapshot1.getRef().removeValue();
                        }
                        pd.dismiss();
                        Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return posts.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView picture, image;
        TextView name, time, title, description, like, comments;
        //ImageButton more;
        ImageView likebtn, commentbtn;
        LinearLayout profile;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.pimage);
            image = itemView.findViewById(R.id.uimage);
            name = itemView.findViewById(R.id.uname);
            time = itemView.findViewById(R.id.ptime);
            //more = itemView.findViewById(R.id.morebtn);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            like = itemView.findViewById(R.id.plikes);
            comments = itemView.findViewById(R.id.pcomments);
            likebtn = itemView.findViewById(R.id.likebtn);
            commentbtn = itemView.findViewById(R.id.commentbtn);
            profile = itemView.findViewById(R.id.profilelayout);
        }
    }
}