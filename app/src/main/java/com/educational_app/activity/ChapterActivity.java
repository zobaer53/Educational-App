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
import com.educational_app.adapter.ChapterRecyclerAdapter;
import com.educational_app.adapter.TestRecyclerAdapter;
import com.educational_app.model.RecyclerViewItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChapterActivity extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    RecyclerView chapterRecyclerView,testRecyclerView;
    RecyclerViewItem recyclerViewItem,testRecyclerItem;
    ChapterRecyclerAdapter adapter;
    TestRecyclerAdapter testAdapter;
    List<RecyclerViewItem> recyclerList = new ArrayList<>();
    List<RecyclerViewItem> recyclerList2 = new ArrayList<>();

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;

    int selectedTopic;
    String courseID,topicName;
    TextView topicNameTextView,topicCountTextView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        topicNameTextView = findViewById(R.id.topicName);
        topicCountTextView = findViewById(R.id.topicNum);

        selectedTopic = getIntent().getIntExtra("coursePosition",0);
        topicName = getIntent().getStringExtra("topicName");
        courseID = getIntent().getStringExtra("courseID");
        Log.i("Tag","selected Topic = "+selectedTopic+" courseID = "+courseID+" topicName= "+topicName);

        topicNameTextView.setText(topicName);


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


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();


        //For chapter name
        chapterRecyclerView = findViewById(R.id.chapterRecyclerView);
       loadCChapterNameFromFirebase();
        RecyclerView.LayoutManager layoutManager
                = new LinearLayoutManager(ChapterActivity.this, LinearLayoutManager.HORIZONTAL, false);
        chapterRecyclerView.setLayoutManager(layoutManager);
        adapter = new ChapterRecyclerAdapter(recyclerList,this, courseID, topicName);
        chapterRecyclerView.setAdapter(adapter);

        //for test paper name
        testRecyclerView = findViewById(R.id.testRecyclerView);
        loadTestNameFromFirebase();
        RecyclerView.LayoutManager layoutManager2
                = new LinearLayoutManager(ChapterActivity.this, LinearLayoutManager.HORIZONTAL, false);
        testRecyclerView.setLayoutManager(layoutManager2);
        testAdapter = new TestRecyclerAdapter(recyclerList2,this,courseID, topicName);
        testRecyclerView.setAdapter(testAdapter);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void handleOnBackPress() {
        super.onBackPressed();
    }

    private void loadCChapterNameFromFirebase() {
        mDatabaseReference.child("chapter").child(courseID).child(String.valueOf(selectedTopic)).addValueEventListener(new ValueEventListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot contentSnapShot : dataSnapshot.getChildren()) {
                    recyclerViewItem = new RecyclerViewItem(contentSnapShot.child("chapterName").getValue().toString(),contentSnapShot.child("url").getValue().toString());
                    recyclerList.add(recyclerViewItem);
                    // Log.i("Tag", "Title1= " + count + " Url1 = "+adapter.getItemCount() );
                    topicCountTextView.setText(recyclerList.size()+" Chapter");
                   // Log.i("Tag"," Topic count = "+count);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

    private void loadTestNameFromFirebase() {
        mDatabaseReference.child("testName").child(courseID).child(String.valueOf(selectedTopic)).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot contentSnapShot : dataSnapshot.getChildren()) {
                    testRecyclerItem = new RecyclerViewItem(contentSnapShot.child("testName").getValue().toString(),contentSnapShot.child("url").getValue().toString());
                    recyclerList2.add(testRecyclerItem);
                  // Log.i("Tag", "Title1= " + testRecyclerItem.getTitle() + " Url1 = "+testRecyclerItem.getImageUrl() );
                }
                testAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chapter_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.chapterNameHelp) {
            Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
            startActivity(intent);
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