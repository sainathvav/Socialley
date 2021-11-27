package com.example.socialley;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity{
    Button logout_button;
    NavigationBarView bnv;

    public NavigationBarView.OnItemSelectedListener myListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    MyHomeFragment homefragment = new MyHomeFragment();
                    FragmentTransaction homefragmentTransaction = getSupportFragmentManager().beginTransaction();
                    homefragmentTransaction.replace(R.id.content, homefragment, "");
                    homefragmentTransaction.commit();
                    return true;

                case R.id.myprofile:
                    MyProfileFragment profilefragment = new MyProfileFragment();
                    FragmentTransaction profilefragmentTransaction = getSupportFragmentManager().beginTransaction();
                    profilefragmentTransaction.replace(R.id.content, profilefragment, "");
                    profilefragmentTransaction.commit();
                    return true;

                case R.id.chat:
                    ChatFragment chatfragment = new ChatFragment();
                    FragmentTransaction chatfragmentTransaction = getSupportFragmentManager().beginTransaction();
                    chatfragmentTransaction.replace(R.id.content, chatfragment, "");
                    chatfragmentTransaction.commit();
                    return true;

                case R.id.friends:
                    FriendsFragment friendsFragment = new FriendsFragment();
                    FragmentTransaction friendsFragmentTransaction = getSupportFragmentManager().beginTransaction();
                    friendsFragmentTransaction.replace(R.id.content, friendsFragment, "");
                    friendsFragmentTransaction.commit();
                    return true;

                case R.id.add_posts:
                    AddPostFragment postsFragment = new AddPostFragment();
                    FragmentTransaction addPostFragmentTransaction = getSupportFragmentManager().beginTransaction();
                    addPostFragmentTransaction.replace(R.id.content, postsFragment, "");
                    addPostFragmentTransaction.commit();
                    return true;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bnv =  (NavigationBarView) findViewById(R.id.bottomNav);
        bnv.setOnItemSelectedListener(myListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                return true;
            case R.id.action_all_users:
                AllUsersFragment allUsersFragment = new AllUsersFragment();
                FragmentTransaction allUsersFragmentTransaction = getSupportFragmentManager().beginTransaction();
                allUsersFragmentTransaction.replace(R.id.content, allUsersFragment, "");
                allUsersFragmentTransaction.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
