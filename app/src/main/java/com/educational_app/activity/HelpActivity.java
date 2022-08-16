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
import android.view.View;

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

public class HelpActivity extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();



    RecyclerView topicHelpRecyclerView;
    RecyclerView faqRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;

    ChapterDetailsItem helpDetailsItem;
    ChapterDetailsAdapter listAdapterHelp;
    List<ChapterDetailsItem> helpDetailsItemList= new ArrayList<>();
    String[] studentFAQ = new String[] { "Refund Status: Common Questions", "Lifetime Access", "How to Find Your Missing Course"};



    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

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

        topicHelpRecyclerView = findViewById(R.id.topicHelpRecyclerView);
        faqRecyclerView = findViewById(R.id.faqRecyclerView);
        loadFromFirebase();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HelpActivity.this);
        faqRecyclerView.setLayoutManager(layoutManager);
        listAdapterHelp = new ChapterDetailsAdapter(helpDetailsItemList,this);
        faqRecyclerView.setAdapter(listAdapterHelp);






    }

    private void loadFromFirebase() {
        mDatabaseReference.child("helpActivity").addValueEventListener(new ValueEventListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot contentSnapShot : dataSnapshot.getChildren()) {
                    helpDetailsItem = new ChapterDetailsItem(contentSnapShot.child("question").getValue().toString(),
                            contentSnapShot.child("answer").getValue().toString());
                    helpDetailsItemList.add(helpDetailsItem);
                   // Log.i("Tag", "Title1= " + chapterDetailsItem.getQuestion()+ " Url1 = "+chapterDetailsItem.getAnswer() );
                }
                listAdapterHelp.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


    private void handleOnBackPress() {
        super.onBackPressed();
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