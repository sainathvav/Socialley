package com.example.socialley;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;


public class MyProfileFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase database;
    DatabaseReference reference;
    StorageReference storageReference;

    Button update_username_button;
    Button update_password_button;

    EditText edit_username;
    EditText edit_password;

    EditText usernameField;
    EditText passwordField;

    String oldUsername;
    String oldPassword;

    ImageView profilepic;
    Uri imageUri;
    ProgressBar pb;

    Boolean start;

    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    private static final int IMAGE_PICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICK_CAMERA_REQUEST = 400;
    String[] cameraPermission;
    String[] storagePermission;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    private Boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private Boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private ActivityResultLauncher<String> requestCameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {

                } else {
                    Toast.makeText(this.getContext(),"Sorry, this activity requires camera permission",Toast.LENGTH_LONG).show();
                }
            });

    private ActivityResultLauncher<String> requestStoragePermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {

                } else {
                    Toast.makeText(this.getContext(),"Sorry, this activity requires storage permission",Toast.LENGTH_LONG).show();
                }
            });

    ActivityResultLauncher<String> pickFromGallery =
            registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    imageUri = uri;
                    uploadProfileCoverPhoto(imageUri);
                }
            });

    ActivityResultLauncher<Intent> pickFromCamera =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        uploadProfileCoverPhoto(imageUri);
                    }
                    else {
                        Toast.makeText(getContext(), "Data is null",Toast.LENGTH_LONG).show();
                    }
                }
            });

    private void uploadProfileCoverPhoto(final Uri uri) {

        String filePathName = "Users/Profile_Pics/" + firebaseUser.getUid();
        StorageReference storageReference1 = storageReference.child(filePathName);
        storageReference1.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;

                final Uri downloadUri = uriTask.getResult();
                if (uriTask.isSuccessful()) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("profilePic", downloadUri.toString());
                    reference.child(firebaseUser.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Updated", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Error Updating ", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        update_password_button = view.findViewById(R.id.update_password_button);
        update_username_button = view.findViewById(R.id.update_username_button);

        edit_password = view.findViewById(R.id.edit_password);
        edit_username = view.findViewById(R.id.edit_username);

        profilepic = view.findViewById(R.id.profile_image);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");


        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Select image from");
                String[] options = {"Camera", "Gallery"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            if (!checkCameraPermission()) {
                                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
                                requestCameraPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            }
                            else {
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(MediaStore.Images.Media.TITLE, "Temp_pic");
                                contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
                                imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                pickFromCamera.launch(cameraIntent);
                                Toast.makeText(v.getContext(),"Camera Permission has already been granted",Toast.LENGTH_LONG).show();
                            }
                        } else if (which == 1) {
                            if (!checkStoragePermission()) {
                                requestStoragePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            }
                            else {
                                pickFromGallery.launch("image/*");
                                Toast.makeText(v.getContext(),"Storage Permission has already been granted",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
                builder.create().show();
            }
        });

        reference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usernameField = view.findViewById(R.id.edit_username);
                passwordField = view.findViewById(R.id.edit_password);
                if (dataSnapshot.exists()) {
                    oldUsername = dataSnapshot.child("username").getValue().toString();
                    oldPassword = dataSnapshot.child("password").getValue().toString();
                    usernameField.setText(oldUsername);
                    passwordField.setText(oldPassword);
                    if (dataSnapshot.child("profilePic").getValue().toString() != "") {
                        try{
                            Glide.with(getActivity().getApplicationContext()).load(dataSnapshot.child("profilePic").getValue().toString()).into(profilepic);
                        }
                        catch (Exception e) {
                            Toast.makeText(getContext(),"Somri", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(getContext(),"Sorry", Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        update_username_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = edit_username.getText().toString().trim();
                if (TextUtils.isEmpty(newUsername)) {
                    edit_username.setError("Username can't be empty");
                    edit_username.requestFocus();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("Are you sure you want to change your username?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username").setValue(newUsername);
                            oldUsername = newUsername;
                            dialog.cancel();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            usernameField.setText(oldUsername);
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }

        });


        update_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = edit_password.getText().toString().trim();
                if (newPassword.length() < 8) {
                    edit_password.setError("Password should be minimum 8 characters long");
                    edit_password.requestFocus();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("Are you sure you want to change your password?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("password").setValue(newPassword);
                            FirebaseAuth.getInstance().getCurrentUser().updatePassword(newPassword);
                            oldPassword = newPassword;
                            dialog.cancel();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            passwordField.setText(oldPassword);
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });

        return view;
    }
}