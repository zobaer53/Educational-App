package com.educational_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.educational_app.R;
import com.educational_app.Utility.NetworkChangeListener;
import com.educational_app.adapter.MyCoursesAdapter;
import com.educational_app.model.Course;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyCoursesActivity extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    RecyclerView myCourseRecyclerView;
    MyCoursesAdapter myCoursesAdapter;
    Course courseRecyclerViewItem;
    List<Course> recyclerList1 = new ArrayList<>();
    int itemSize1 = 0, itemSize2 = 0, itemSize3 = 0, itemSize4 = 0, itemSize5 = 0;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;

    String currentUserID, phone;
    String orderPhone;

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            mDatabaseReference.child("Users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (snapshot.exists()) {
                            String uid = dataSnapshot.child("uid").getValue().toString();
                            if (currentUserID.equals(uid)) {
                                phone = dataSnapshot.getKey();
                            }
                        }

                    }
                    Log.i("Tag", " phone " + phone);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnBackPress();
            }
        });

        myCourseRecyclerView = findViewById(R.id.myCourseRecyclerView);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        RecyclerView.LayoutManager layoutManager
                = new LinearLayoutManager(MyCoursesActivity.this, LinearLayoutManager.HORIZONTAL, false);
        myCourseRecyclerView.setLayoutManager(layoutManager);
        loadDataFromFirebase();


    }

    private void handleOnBackPress() {
        super.onBackPressed();
    }

    private void loadDataFromFirebase() {
        mDatabaseReference.child("OrderRequests").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot contentSnapShot : dataSnapshot.getChildren()) {
                    orderPhone = contentSnapShot.child("phone").getValue().toString();
                    // if(orderPhone.equals(phone)){
                    String time = contentSnapShot.getKey();
                    Log.i("Tag", "Order  time= " + time);

                    assert time != null;
                    mDatabaseReference.child("OrderRequests").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child(time).child("courses").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                Log.i("Tag", "In=1 ");

                                courseRecyclerViewItem = new Course(snapshot1.child("productId").getValue().toString(),
                                        snapshot1.child("imageUrl").getValue().toString(),
                                        snapshot1.child("productName").getValue().toString(),
                                        snapshot1.child("instructorName").getValue().toString(),
                                        snapshot1.child("price").getValue().toString());

                                recyclerList1.add(courseRecyclerViewItem);
                                Log.i("Tag", "Order  title= " + courseRecyclerViewItem.getTitle() + " Url1 = " + courseRecyclerViewItem.getImageUrl());
                                Log.i("Tag", "Order  time= " + time);
                            }

                            myCoursesAdapter = new MyCoursesAdapter(recyclerList1, MyCoursesActivity.this, itemSize1, itemSize2, itemSize3, itemSize4, itemSize5);
                            myCourseRecyclerView.setAdapter(myCoursesAdapter);
                            myCoursesAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    // }
                }
                //Log.i("Tag", "itemSize1= " +itemSize1 );
                myCoursesAdapter = new MyCoursesAdapter(recyclerList1, MyCoursesActivity.this, itemSize1, itemSize2, itemSize3, itemSize4, itemSize5);
                myCourseRecyclerView.setAdapter(myCoursesAdapter);
                myCoursesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}