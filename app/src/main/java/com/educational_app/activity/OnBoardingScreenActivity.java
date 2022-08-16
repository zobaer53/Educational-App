package com.educational_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.educational_app.R;
import com.educational_app.Utility.NetworkChangeListener;
import com.educational_app.adapter.SliderAdapter;
import com.educational_app.model.SliderItem;
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

public class OnBoardingScreenActivity extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;

    SliderAdapter adapter;
    SliderView sliderView;
    List<SliderItem> mSliderList = new ArrayList<>();
    SliderItem slideritem;

    Button browseButton,signInButton;
    int doubleBackToExitPressed = 1;
    TextView tv1,tv2;
    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
       //  overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_on_boarding_screen);

        tv1 = findViewById(R.id.onBoardingTextView);
        tv2 = findViewById(R.id.onBoardingTextView2);
        linearLayout = findViewById(R.id.onBoardingLinearLayout);
        sliderView = findViewById(R.id.imageSlider);

       sliderView.setY(-1000);
        sliderView.animate().translationYBy(1000).setDuration(2000);
        tv1.setX(-1000);
        tv1.animate().translationXBy(1000).setDuration(2000);
        tv2.setX(-1000);
        tv2.animate().translationXBy(1000).setDuration(2000);
        linearLayout.setY(1000);
       linearLayout.animate().translationYBy(-1000).setDuration(2000);




        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        loadSliderFirebase(); //get title and url from firebase
        adapter = new SliderAdapter(mSliderList,this);//pass the title and url list to adapter
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        browseButton = findViewById(R.id.browseButton);
        signInButton = findViewById(R.id.signInButton);

        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnBoardingScreenActivity.this,AllCourseActivity.class));
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OnBoardingScreenActivity.this,SignIn.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadSliderFirebase() {
        mDatabaseReference.child("onBoardingSlider").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot contentSnapShot : dataSnapshot.getChildren()) {
                    slideritem = new SliderItem(contentSnapShot.child("title").getValue().toString(),contentSnapShot.child("url").getValue().toString());
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
        super.onBackPressed();
        if (doubleBackToExitPressed == 2) {
            finishAffinity();
            FirebaseAuth.getInstance().signOut();
            System.exit(0);
        }
        else {
            doubleBackToExitPressed++;
            Toast.makeText(this, "Please press Back again to exit", Toast.LENGTH_SHORT).show();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressed=1;
            }
        }, 2000);
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

    @Override
    protected void onPause() {
       // overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        super.onPause();

    }
}