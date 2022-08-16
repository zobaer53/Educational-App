package com.educational_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.educational_app.R;
import com.educational_app.Utility.NetworkChangeListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    ImageView logo,bg,lotto;
    TextView TextView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    private String homeID, phone;
    String currentUserID;
    int delayMillis = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logo = findViewById(R.id.logo);
        bg = findViewById(R.id.bg);
       TextView = findViewById(R.id.Text);
        lotto = findViewById(R.id.lottieAnimationView);
        bg.animate().translationY(-1600).setDuration(delayMillis-1000).setStartDelay(1000);
        logo.animate().translationY(1400).setDuration(delayMillis-1000).setStartDelay(1000);
      TextView.animate().translationY(1400).setDuration(delayMillis-1000).setStartDelay(1000);
        lotto.animate().translationY(1400).setDuration(delayMillis-1000).setStartDelay(1000);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {

                    Intent intent = new Intent(MainActivity.this, ActivityHome.class);
                    intent.putExtra("courseID", homeID);
                    intent.putExtra("phone", phone);
                    if (homeID != null && phone != null) {
                        startActivity(intent);
                        finish();
                    } else {
                        delayMillis = 1000;
                        intent = new Intent(MainActivity.this, OnBoardingScreenActivity.class);
                        startActivity(intent);
                        finish();
                    }


                } else {

                    Intent intent = new Intent(MainActivity.this, OnBoardingScreenActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        }, delayMillis);
    }

    @Override
    protected void onStart() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

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
                                homeID = dataSnapshot.child("courseID").getValue().toString();

                            }
                        }

                    }
                    Log.i("Tag", "1 Firebase phone=" + phone);
                    Log.i("Tag", "2 Firebase currentUser=" + currentUserID);
                    Log.i("Tag", "3 Firebase currentUser=" + homeID);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
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