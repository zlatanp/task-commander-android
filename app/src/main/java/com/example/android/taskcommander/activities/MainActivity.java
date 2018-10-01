package com.example.android.taskcommander.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.taskcommander.R;
import com.example.android.taskcommander.adapters.NavDrawerListAdapter;
import com.example.android.taskcommander.fragments.MainFragment;
import com.example.android.taskcommander.model.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import com.google.firebase.messaging.FirebaseMessaging;

import static com.example.android.taskcommander.R.array.navDrawerFooterList;
import static com.example.android.taskcommander.R.array.navDrawerList;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, InternetConnectivityListener {

    private FirebaseUser fbUser;

    private ProgressDialog progressDialog;

    private InternetAvailabilityChecker mInternetAvailabilityChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Firebase messaging
        FirebaseMessaging.getInstance().subscribeToTopic("JavaSampleApproach");
        // [END subscribe_topics]

        InternetAvailabilityChecker.init(this);
        mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        mInternetAvailabilityChecker.addInternetConnectivityListener(this);

        fbUser = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Changing avatar...");


//        int[] icons = getResources().getIntArray(R.array.navDrawerIcons);
        int[] icons = new int[]{R.drawable.ic_group_black_24px, R.drawable.ic_assignment_black_24px, R.drawable.ic_chat_black_24px};

        NavDrawerListAdapter myAdapter = new NavDrawerListAdapter(getResources().getStringArray(navDrawerList), icons, getApplicationContext());
        ListView listViewTop = (ListView) findViewById(R.id.listViewNavDrawer);
        listViewTop.setDivider(null);
        listViewTop.setAdapter(myAdapter);
        listViewTop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //TODO
                        openActivity(GroupsActivity.class);
                        break;
                    case 1:
                        openActivity(MainActivity.class);
                        break;
                    case 2:
                        openActivity(ChatActivity.class);
                        break;
                }
            }
        });

        int[] icons1 = new int[]{R.drawable.ic_share_black_24px, R.drawable.ic_info_black_24px, R.drawable.ic_directions_run_black_24px};

        NavDrawerListAdapter myAdapterFooter = new NavDrawerListAdapter(getResources().getStringArray(navDrawerFooterList), icons1, getApplicationContext());
        ListView listViewFooter = findViewById(R.id.listViewNavDrawerFooter);
        listViewFooter.setDivider(null);
        listViewFooter.setAdapter(myAdapterFooter);
        listViewFooter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //TODO
                        break;
                    case 1:
                        //TODO
                        break;
                    case 2:
                        // logout
                        logout();
                        break;
                }
            }
        });

        if (fbUser != null)
            setupNameEmail();

        // set main fragment at start
        MainFragment mainFragment = new MainFragment();
        if(getIntent().hasExtra("task")) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("task", getIntent().getSerializableExtra("task"));
            mainFragment.setArguments(bundle);
        }
        getSupportFragmentManager()
                .beginTransaction().add(R.id.frameLayout, mainFragment)
                .commit();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed!", Toast.LENGTH_SHORT).show();
    }


    private void setupNameEmail() {
        final TextView tvName = (TextView) findViewById(R.id.navDrawerName);
        final TextView tvEmail = (TextView) findViewById(R.id.navDrawerEmail);
        final ImageView ivLoginMehod = (ImageView) findViewById(R.id.imageViewHeaderLoginMethod);

        final DatabaseReference dr = FirebaseDatabase.getInstance().getReference("users").child(fbUser.getUid());
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user != null) {
                    if (user.getName() != null) {
                        tvName.setText(user.getName());
                    }
                    if (user.getEmail() != null) {
                        tvEmail.setText(user.getEmail());
                    }

                    String loginMethod = user.getLoginMethod();
                    ivLoginMehod.setImageResource(getLoginMethodImage(loginMethod));

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("DATABASE READ FAILED!");
            }
        });

    }

    private void logout() {
        if (fbUser != null) {
            FirebaseAuth.getInstance().signOut();
        }
        openActivity(LoginActivity.class);
    }


    private void openActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    private int getLoginMethodImage(String loginMethod) {
        int image = R.drawable.questionmark;

        switch (loginMethod) {
            case "google":
                image = R.drawable.google_icon;
                break;
            case "twitter":
                image = R.drawable.facebook_logo;
                break;
            case "facebook":
                image = R.drawable.twitter_icon;
                break;
        }

        return image;
    }

    @Override
    public void onBackPressed() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to quit?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInternetAvailabilityChecker.removeInternetConnectivityChangeListener(this);
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

//                finish();
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });

        return builder;
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {

        if(!isConnected){
            buildDialog(MainActivity.this).show();
        }
    }
}
