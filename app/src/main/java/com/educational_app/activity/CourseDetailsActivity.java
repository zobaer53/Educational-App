package com.educational_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.educational_app.R;
import com.educational_app.Utility.NetworkChangeListener;
import com.educational_app.adapter.RecyclerAdapterForTwoItem;
import com.educational_app.database.DataBase;
import com.educational_app.model.Course;
import com.educational_app.model.Order;
import com.educational_app.model.RecyclerViewItem;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CourseDetailsActivity extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    ImageView courseImage,instructorImage;
    TextView courseTitleTV,courseDescriptionTV,studentCountTV,instructorNameTV,instructorNameTV2,instructorTitleTV,courseCountTV,price;
    Button addToCartButton;
    List<Order> courseList = new ArrayList<>();

    Course courses;
    int coursePosition;
    String courseName,courseTag,item,courseDetails,studentCount,courseCount,instructorURl,instructorTitle;

    RecyclerView whatLearnRecyclerView,fileInfoRecyclerView,includesRecyclerView,requirementRecyclerView;
    RecyclerAdapterForTwoItem whatLearnAdapterForTwoItem,fileInfoAdapter,includesAdapter,requirementAdapter;
    RecyclerViewItem whatLearnItem,fileInfoItem,includesItem,requirementItem;
    List<RecyclerViewItem> whatLearnList = new ArrayList<>();
    List<RecyclerViewItem> fileInfoList = new ArrayList<>();
    List<RecyclerViewItem> includesList = new ArrayList<>();
    List<RecyclerViewItem> requirementList = new ArrayList<>();


    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    Course recyclerViewItem;
    List<Course> recyclerList = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

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

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        courseImage = findViewById(R.id.courseImageView);
        courseTitleTV = findViewById(R.id.courseTitleTextView);
        courseDescriptionTV = findViewById(R.id.courseDiscriptionTextView);
        studentCountTV = findViewById(R.id.studentCount);
        courseCountTV = findViewById(R.id.courseCount);
        instructorNameTV = findViewById(R.id.instructorNameTextView);
        instructorNameTV2 = findViewById(R.id.instructorNameTextView2);
        instructorTitleTV = findViewById(R.id.instructorSkill);
        instructorImage = findViewById(R.id.instructorPhoto);
        price = findViewById(R.id.priceTextView);
        addToCartButton = findViewById(R.id.addToCartButton);

        coursePosition = getIntent().getIntExtra("coursePosition",0);
        courseName = getIntent().getStringExtra("courseName");
        courseTag = getIntent().getStringExtra("courseDetails");

        Log.i("Tag","position from course details "+coursePosition+"courseName "+courseName+"course Details "+courseTag);

        loadCourseDetailsFirebase();
        loadDEVRecyclerViewFirebase();
        loadWhatLearnRecyclerView();
        loadFileInfoRecyclerView();
        loadIncludesRecyclerView();
        loadRequirementRecyclerView();

        whatLearnRecyclerView = findViewById(R.id.whatLearnRecyclerView);
        RecyclerView.LayoutManager whatLearnLM = new LinearLayoutManager(CourseDetailsActivity.this);

        whatLearnRecyclerView.setLayoutManager(whatLearnLM);
        whatLearnAdapterForTwoItem = new RecyclerAdapterForTwoItem(whatLearnList,this);
        whatLearnRecyclerView.setAdapter(whatLearnAdapterForTwoItem);

        fileInfoRecyclerView = findViewById(R.id.fileInfoRecyclerView);
        RecyclerView.LayoutManager fileInfoLM = new LinearLayoutManager(CourseDetailsActivity.this);

        fileInfoRecyclerView.setLayoutManager(fileInfoLM);
        fileInfoAdapter = new RecyclerAdapterForTwoItem(fileInfoList,this);
       fileInfoRecyclerView.setAdapter(fileInfoAdapter);

        includesRecyclerView = findViewById(R.id.includesRecyclerView);
        RecyclerView.LayoutManager includesLM = new LinearLayoutManager(CourseDetailsActivity.this);

        includesRecyclerView.setLayoutManager(includesLM);
        includesAdapter = new RecyclerAdapterForTwoItem(includesList,this);
        includesRecyclerView.setAdapter(includesAdapter);

        requirementRecyclerView = findViewById(R.id.requirementRecyclerView);
        RecyclerView.LayoutManager requirementLM = new LinearLayoutManager(CourseDetailsActivity.this);

        requirementRecyclerView.setLayoutManager(requirementLM);
        requirementAdapter = new RecyclerAdapterForTwoItem(requirementList,this);
        requirementRecyclerView.setAdapter(requirementAdapter);

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((FirebaseAuth.getInstance().getCurrentUser()!=null)){
                    mDatabaseReference.child(courseName).child(String.valueOf(coursePosition)).addValueEventListener(new ValueEventListener() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                            courses = new Course(dataSnapshot1.child("courseId").getValue().toString(),
                                    dataSnapshot1.child("url").getValue().toString(),
                                    dataSnapshot1.child("title").getValue().toString(),
                                    dataSnapshot1.child("name").getValue().toString(),
                                    dataSnapshot1.child("price").getValue().toString());
                            try {
                                int i=0;
                                courseList = new DataBase(getApplicationContext()).getCarts();

                                if(!courseList.isEmpty()) {
                                    for (Order order : courseList) {
                                        if (order.getProductId().equals(courses.getId())) {
                                            i = 1;
                                            break;
                                        }
                                    }
                                    if(i==0){
                                        new DataBase(getBaseContext()).addToCart(new Order(
                                                courses.getId(),
                                                courses.getImageUrl(),
                                                courses.getTitle(),
                                                courses.getName(),
                                                "1",
                                                courses.getPrice()
                                        ));
                                        Toast.makeText(CourseDetailsActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(CourseDetailsActivity.this, "Already added to cart", Toast.LENGTH_SHORT).show();
                                    }
                                }else {

                                    new DataBase(getBaseContext()).addToCart(new Order(
                                            courses.getId(),
                                            courses.getImageUrl(),
                                            courses.getTitle(),
                                            courses.getName(),
                                            "1",
                                            courses.getPrice()
                                    ));
                                    Toast.makeText(CourseDetailsActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();

                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                }else {
                     new AlertDialog.Builder(CourseDetailsActivity.this)
                            .setIcon(R.drawable.login_icon)

                            .setTitle("Signin to Add item")

                            .setMessage("")

                            .setPositiveButton("Signin", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Intent intent = new Intent(CourseDetailsActivity.this,SignIn.class);
                                    startActivity(intent);
                                    finish();

                                }
                            })

                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();
                }
                }
        });

    }

    private void handleOnBackPress() {
        super.onBackPressed();
    }

    private void loadRequirementRecyclerView() {
        mDatabaseReference.child("coursesListDetails").child(courseTag).child(String.valueOf(coursePosition)).child("requirements").addValueEventListener(new ValueEventListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){

                    requirementItem = new RecyclerViewItem(snapshot.child("value").getValue().toString(),
                            snapshot.child("url").getValue().toString());
                    requirementList.add(requirementItem);
                    // Log.i("Tag", "Title1= " + whatLearnItem.getTitle() + " Url1 = "+whatLearnItem.getImageUrl() );

                }
                requirementAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void loadIncludesRecyclerView() {
        mDatabaseReference.child("coursesListDetails").child(courseTag).child(String.valueOf(coursePosition)).child("includes").addValueEventListener(new ValueEventListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){

                    includesItem = new RecyclerViewItem(snapshot.child("value").getValue().toString(),
                            snapshot.child("url").getValue().toString());
                    includesList.add(includesItem);
                    // Log.i("Tag", "Title1= " + whatLearnItem.getTitle() + " Url1 = "+whatLearnItem.getImageUrl() );

                }
                includesAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void loadFileInfoRecyclerView() {

        mDatabaseReference.child("coursesListDetails").child(courseTag).child(String.valueOf(coursePosition)).child("fileInfo").addValueEventListener(new ValueEventListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){

                    fileInfoItem = new RecyclerViewItem(snapshot.child("value").getValue().toString(),
                            snapshot.child("url").getValue().toString());
                    fileInfoList.add(fileInfoItem);
                   // Log.i("Tag", "Title1= " + whatLearnItem.getTitle() + " Url1 = "+whatLearnItem.getImageUrl() );

                }
                fileInfoAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void loadWhatLearnRecyclerView() {

        mDatabaseReference.child("coursesListDetails").child(courseTag).child(String.valueOf(coursePosition)).child("whatLearn").addValueEventListener(new ValueEventListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){

                    whatLearnItem = new RecyclerViewItem(snapshot.child("value").getValue().toString(),
                            snapshot.child("url").getValue().toString());
                    whatLearnList.add(whatLearnItem);
                   // Log.i("Tag", "Title1= " + whatLearnItem.getTitle() + " Url1 = "+whatLearnItem.getImageUrl() );

                }
                whatLearnAdapterForTwoItem.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void loadDEVRecyclerViewFirebase() {
        mDatabaseReference.child(courseName).child(String.valueOf(coursePosition)).addValueEventListener(new ValueEventListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    recyclerViewItem = new Course(dataSnapshot.child("courseId").getValue().toString(),
                            dataSnapshot.child("url").getValue().toString(),
                            dataSnapshot.child("title").getValue().toString(),
                            dataSnapshot.child("name").getValue().toString(),
                            dataSnapshot.child("price").getValue().toString());
                    recyclerList.add(recyclerViewItem);
                 //    Log.i("Tag", "Title1= " + recyclerViewItem.getTitle() + " Url1 = "+recyclerViewItem.getImageUrl() );

                Glide.with(getApplicationContext())
                        .load(recyclerViewItem.getImageUrl())
                        .fitCenter()
                        .into(courseImage);
                courseTitleTV.setText(recyclerViewItem.getTitle());
                Locale locale = new Locale("hi","IN");
                NumberFormat format = NumberFormat.getCurrencyInstance(locale);
                price.setText(format.format(Integer.parseInt(recyclerViewItem.getPrice())));
                instructorNameTV.setText("Created by "+recyclerViewItem.getName());
                instructorNameTV2.setText(recyclerViewItem.getName());


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


    private void loadCourseDetailsFirebase() {
        mDatabaseReference.child("coursesListDetails").child(courseTag).child(String.valueOf(coursePosition)).addValueEventListener(new ValueEventListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    courseDetails=dataSnapshot.child("details").getValue().toString();
                    courseCount = dataSnapshot.child("courseCount").getValue().toString();
                    instructorTitle = dataSnapshot.child("instructorTitle").getValue().toString();
                    instructorURl = dataSnapshot.child("instructorURL").getValue().toString();
                    studentCount = dataSnapshot.child("studentCount").getValue().toString();


                    courseDescriptionTV.setText(courseDetails);
                    instructorTitleTV.setText(instructorTitle);
                    studentCountTV.setText(studentCount+" Students");
                    courseCountTV.setText(courseCount+" Courses");
                    Glide.with(getApplicationContext())
                            .load(dataSnapshot.child("instructorURL").getValue().toString())
                            .into(instructorImage);

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem i) {

        int id = i.getItemId();
        if (id == R.id.courseMenuCart) {

            Intent intent = new Intent(CourseDetailsActivity.this,CartActivity.class);
            intent.putExtra("courseName",courseName);
            intent.putExtra("itemNumber",item);
            startActivity(intent);
            finish();
            return true;

        }   else if (id == R.id.courseMenuHelp) {
            Intent intent = new Intent(getApplicationContext(),HelpActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(i);
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