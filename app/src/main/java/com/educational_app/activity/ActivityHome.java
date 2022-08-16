package com.educational_app.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.educational_app.R;
import com.educational_app.Utility.NetworkChangeListener;
import com.educational_app.adapter.RecyclerAdapter;
import com.educational_app.adapter.RecyclerAdapter2;
import com.educational_app.model.RecyclerViewItem;
import com.educational_app.adapter.SliderAdapter;
import com.educational_app.model.SliderItem;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class ActivityHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    DrawerLayout drawer;
    NavigationView navigationView;
    View navHeader;
    TextView navText, spin;
    List<String> courses;
    ImageView profile;

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;

    SliderAdapter adapter;
    SliderView sliderView;
    List<SliderItem> mSliderList = new ArrayList<>();
    SliderItem slideritem;

    RecyclerView recyclerView, recyclerView2;
    RecyclerAdapter recyclerAdapter;
    RecyclerAdapter2 recyclerAdapter2;
    RecyclerViewItem recyclerViewItem, recyclerViewItem2;
    List<RecyclerViewItem> recyclerList = new ArrayList<>();
    List<RecyclerViewItem> recyclerList2 = new ArrayList<>();

    private String homeID, phone;
    int doubleBackToExitPressed = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        // toolbar.setTitle("C Programming");
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navHeader = navigationView.getHeaderView(0);
        navText = navHeader.findViewById(R.id.name_top);
        profile = navHeader.findViewById(R.id.profileImageView);
        spin = navHeader.findViewById(R.id.coursesspinner);
        courses = new ArrayList<>();
        courses.add("Select course");


        //FireBase Hooks
        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        phone = getIntent().getStringExtra("phone");
        Log.i("Tag", "Home phone = " + phone);

        //get home id for selected course id
        homeID = getIntent().getStringExtra("courseID"); //course id from setup profile activity
        Log.i("Tag", "received CourseID= " + homeID);


        loadSliderFirebase(); //get title and url from firebase
        sliderView = findViewById(R.id.imageSlider);
        adapter = new SliderAdapter(mSliderList, this);//pass the title and url list to adapter
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        // for recycler view 1
        loadRecyclerView2Firebase();
        recyclerView2 = findViewById(R.id.topicHelpRecyclerView);
        RecyclerView.LayoutManager layoutManager2 = new GridLayoutManager(ActivityHome.this, 3);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerAdapter2 = new RecyclerAdapter2(recyclerList2, this, homeID);
        recyclerView2.setAdapter(recyclerAdapter2);


        //for recycler view 2
        loadRecyclerViewFirebase();
        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivityHome.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new RecyclerAdapter(recyclerList, this);
        recyclerView.setAdapter(recyclerAdapter);


        LoadNavHeader();
        navHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityHome.this, Profile.class);
                intent.putExtra("phone", phone);
                intent.putExtra("homeID", homeID);
                startActivity(intent);
                //finish();
            }
        });


    }


    private void LoadNavHeader() {

        mDatabaseReference.child("Users").child(phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    navText.setText(snapshot.child("name").getValue().toString());

                    spin.setText(snapshot.child("course").getValue().toString());

                    if (snapshot.child("uimage").exists()) {

                        Glide.with(getApplicationContext()).load(snapshot.child("uimage").getValue().toString()).into(profile);
                    } else {

                        profile.setImageResource(R.drawable.blank_profile_picture);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadRecyclerViewFirebase() {
        mDatabaseReference.child("home").child(homeID).child("recyclerViewItem").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot contentSnapShot : dataSnapshot.getChildren()) {
                    recyclerViewItem = new RecyclerViewItem(contentSnapShot.child("title").getValue().toString(), contentSnapShot.child("url").getValue().toString());
                    recyclerList.add(recyclerViewItem);
                    // Log.i("Tag", "Title1= " + recyclerViewItem.getTitle() + " Url1 = "+recyclerViewItem.getImageUrl() );
                }
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void loadRecyclerView2Firebase() {
        mDatabaseReference.child("home").child(homeID).child("recyclerViewItem2").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot contentSnapShot : dataSnapshot.getChildren()) {
                    recyclerViewItem2 = new RecyclerViewItem(contentSnapShot.child("title").getValue().toString(), contentSnapShot.child("url").getValue().toString());
                    recyclerList2.add(recyclerViewItem2);
                    //  Log.i("Tag", "Title2= " + recyclerViewItem2.getTitle() + " Url2 = "+recyclerViewItem2.getImageUrl() );
                }
                recyclerAdapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void loadSliderFirebase() {
        mDatabaseReference.child("home").child(homeID).child("slider").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot contentSnapShot : dataSnapshot.getChildren()) {
                    slideritem = new SliderItem(contentSnapShot.child("title").getValue().toString(), contentSnapShot.child("url").getValue().toString());
                    mSliderList.add(slideritem);
                    //Log.i("Tag", "Title= " + slideritem.getDescription() + " Url = "+slideritem.getImageUrl() );
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        if (doubleBackToExitPressed == 2) {

            //  FirebaseAuth.getInstance().signOut();
            finishAffinity();
            System.exit(0);
        } else {
            doubleBackToExitPressed++;
            Toast.makeText(this, "Please press Back again to exit", Toast.LENGTH_SHORT).show();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressed = 1;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {


            case R.id.nav_logout:

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ActivityHome.this, OnBoardingScreenActivity.class));
                finish();
                break;

            case R.id.nav_profile:

                Intent intent = new Intent(ActivityHome.this, Profile.class);
                intent.putExtra("phone", phone);
                intent.putExtra("homeID", homeID);
                startActivity(intent);
                //finish();
                break;

            case R.id.nav_allCourses:

                Intent intent1 = new Intent(ActivityHome.this, AllCourseActivity.class);
                startActivity(intent1);
                //finish();
                break;

            case R.id.nav_share:

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = " App Link";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Download Now");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
            case R.id.nav_about_us:

                intent1 = new Intent(ActivityHome.this, AboutUsActivity.class);
                startActivity(intent1);
                //finish();
                break;
            case R.id.nav_myCourses:

                intent1 = new Intent(ActivityHome.this, MyCoursesActivity.class);
                startActivity(intent1);
                //finish();
                break;

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}
