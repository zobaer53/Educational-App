package com.educational_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.educational_app.R;
import com.educational_app.Utility.NetworkChangeListener;
import com.educational_app.adapter.ChapterDetailsAdapter;
import com.educational_app.model.ChapterDetailsItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChapterDetails extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    TextView chapterName;
    RecyclerView chapterDetailsRV;
    ChapterDetailsItem chapterDetailsItem;
    ChapterDetailsAdapter chapterDetailsAdapter;
    List<ChapterDetailsItem> chapterDetailsItemList= new ArrayList<>();

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;

    int selectedTopic;
    String courseID,topicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back_icon);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnBackPress();
            }
        });

        chapterName = findViewById(R.id.chapterNameTextView);
        chapterDetailsRV = findViewById(R.id.chapterDetails);
        selectedTopic = getIntent().getIntExtra("coursePosition",0);
        courseID = getIntent().getStringExtra("courseID");
        topicName = getIntent().getStringExtra("topicName");

        chapterName.setText(topicName);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();


       loadCChapterNameFromFirebase();


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ChapterDetails.this);
        chapterDetailsRV.setLayoutManager(layoutManager);
        chapterDetailsAdapter = new ChapterDetailsAdapter(chapterDetailsItemList,this);
        chapterDetailsRV.setAdapter(chapterDetailsAdapter);
    }

    private void handleOnBackPress() {
        super.onBackPressed();
    }

    private void loadCChapterNameFromFirebase() {
        mDatabaseReference.child("chapterDetails").child(courseID).child(String.valueOf(selectedTopic)).addValueEventListener(new ValueEventListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot contentSnapShot : dataSnapshot.getChildren()) {
                   chapterDetailsItem = new ChapterDetailsItem(contentSnapShot.child("question").getValue().toString(),
                           contentSnapShot.child("answer").getValue().toString());
                    chapterDetailsItemList.add(chapterDetailsItem);
                     Log.i("Tag", "Title1= " + chapterDetailsItem.getQuestion()+ " Url1 = "+chapterDetailsItem.getAnswer() );

                }
                chapterDetailsAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chapter_details_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
           if (id == R.id.chapterNameHelp) {
            Intent intent = new Intent(getApplicationContext(),HelpActivity.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
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