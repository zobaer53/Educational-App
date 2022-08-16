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
import com.educational_app.adapter.CourseRecyclerAdapter;
import com.educational_app.adapter.CourseRecyclerAdapter2;
import com.educational_app.adapter.CourseRecyclerAdapter3;
import com.educational_app.adapter.CourseRecyclerAdapter4;
import com.educational_app.adapter.CourseRecyclerAdapter5;
import com.educational_app.adapter.CourseRecyclerAdapter6;
import com.educational_app.model.Course;
import com.educational_app.model.CourseListModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllCourseActivity extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    TextView topDevTextView,heading2,heading3,heading4,heading5,heading6;
    RecyclerView RecyclerView1,RecyclerView2,RecyclerView3,RecyclerView4,RecyclerView5,RecyclerView6;

    CourseRecyclerAdapter courseRecyclerAdapter1;
    CourseRecyclerAdapter2 courseRecyclerAdapter2;
    CourseRecyclerAdapter3 courseRecyclerAdapter3;
    CourseRecyclerAdapter4 courseRecyclerAdapter4;
    CourseRecyclerAdapter5 courseRecyclerAdapter5;
    CourseRecyclerAdapter6 courseRecyclerAdapter6;


    Course courseRecyclerViewItem1,courseRecyclerViewItem2,courseRecyclerViewItem3,
            courseRecyclerViewItem4,courseRecyclerViewItem5;

    List<Course> recyclerList1 = new ArrayList<>();
    List<Course> recyclerList2 = new ArrayList<>();
    List<Course> recyclerList3 = new ArrayList<>();
    List<Course> recyclerList4 = new ArrayList<>();
    List<Course> recyclerList5 = new ArrayList<>();
    List<Course> recyclerList6 = new ArrayList<>();


    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    int itemSize1=0,itemSize2=0,itemSize3=0,itemSize4=0,itemSize5=0;

    CourseListModel courseListModel;
    List<CourseListModel> courseList = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        setContentView(R.layout.activity_all_course);
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
        topDevTextView = findViewById(R.id.allCourseTextview);
        heading2 = findViewById(R.id.secondHeading);
        heading3 = findViewById(R.id.thirdHeading);
        heading4 = findViewById(R.id.fourthHeading);
        heading5 = findViewById(R.id.fifthHeading);
        heading6 = findViewById(R.id.sixthHeading);
        RecyclerView1 = findViewById(R.id.RecyclerView1);
        RecyclerView2 = findViewById(R.id.RecyclerView2);
        RecyclerView3 = findViewById(R.id.RecyclerView3);
        RecyclerView4 = findViewById(R.id.RecyclerView4);
        RecyclerView5 = findViewById(R.id.RecyclerView5);
        RecyclerView6 = findViewById(R.id.RecyclerView6);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        courseRecyclerViewItem1 = new Course();
        courseRecyclerViewItem2 = new Course();
        courseRecyclerViewItem3 = new Course();
        courseRecyclerViewItem4 = new Course();
        courseRecyclerViewItem5 = new Course();

        topDevTextView.setText("All Courses");

        mDatabaseReference.child("coursesList").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        courseListModel = new CourseListModel(dataSnapshot.child("courseID").getValue().toString(),
                                dataSnapshot.child("courseName").getValue().toString(),
                                dataSnapshot.child("courseDetails").getValue().toString());
                        courseList.add(courseListModel);
                        Log.i("Tag","coursesList= "+courseList.size()+"CourseName= "+courseListModel.getCourseName());
                    }
                    heading2.setText("All "+courseList.get(0).getCourseName()+" Course");
                    heading3.setText("All "+courseList.get(1).getCourseName()+" Course");
                    heading4.setText("All "+courseList.get(2).getCourseName()+" Course");
                    heading5.setText("All "+courseList.get(3).getCourseName()+" Course");
                    //heading6.setText("All "+courseList.get(4).getCourseName()+" Course");

                    loadDEVRecyclerView1Firebase();

                    loadRecyclerView2Firebase();
                    loadRecyclerView3Firebase();
                    loadRecyclerView4Firebase();
                    loadRecyclerView5Firebase();
                   // loadRecyclerView6Firebase();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void handleOnBackPress() {

        startActivity(new Intent(AllCourseActivity.this,OnBoardingScreenActivity.class));
        finish();
    }
    private void loadDEVRecyclerView1Firebase() {
        mDatabaseReference.child(courseList.get(0).getCourseName()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot contentSnapShot : dataSnapshot.getChildren()) {
                    courseRecyclerViewItem1 = new Course(contentSnapShot.child("courseId").getValue().toString(),
                            contentSnapShot.child("url").getValue().toString(),
                            contentSnapShot.child("title").getValue().toString(),
                            contentSnapShot.child("name").getValue().toString(),
                            contentSnapShot.child("price").getValue().toString());
                    recyclerList1.add(courseRecyclerViewItem1);
                    itemSize1++;
                    Log.i("Tag", "Title1= " +courseRecyclerViewItem1.getTitle() + " Url1 = "+courseRecyclerViewItem1.getImageUrl() );

                }
                mDatabaseReference.child(courseList.get(1).getCourseName()).addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot contentSnapShot : dataSnapshot.getChildren()) {
                            courseRecyclerViewItem2 = new Course(contentSnapShot.child("courseId").getValue().toString(),
                                    contentSnapShot.child("url").getValue().toString(),
                                    contentSnapShot.child("title").getValue().toString(),
                                    contentSnapShot.child("name").getValue().toString(),
                                    contentSnapShot.child("price").getValue().toString());
                            recyclerList1.add(courseRecyclerViewItem2);
                            itemSize2++;
                            Log.i("Tag", "Title1= " +courseRecyclerViewItem2.getTitle() + " Url1 = "+courseRecyclerViewItem2.getImageUrl() );
                        }
                        mDatabaseReference.child(courseList.get(2).getCourseName()).addValueEventListener(new ValueEventListener() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot contentSnapShot : dataSnapshot.getChildren()) {
                                    courseRecyclerViewItem3 = new Course(contentSnapShot.child("courseId").getValue().toString(),
                                            contentSnapShot.child("url").getValue().toString(),
                                            contentSnapShot.child("title").getValue().toString(),
                                            contentSnapShot.child("name").getValue().toString(),
                                            contentSnapShot.child("price").getValue().toString());
                                    recyclerList1.add(courseRecyclerViewItem3);
                                    itemSize3++;
                                    Log.i("Tag", "Title1= " + courseRecyclerViewItem3.getTitle() + " Url1 = "+courseRecyclerViewItem3.getImageUrl() );
                                }
                                mDatabaseReference.child(courseList.get(3).getCourseName()).addValueEventListener(new ValueEventListener() {
                                    @SuppressLint("NotifyDataSetChanged")
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot contentSnapShot : dataSnapshot.getChildren()) {
                                            courseRecyclerViewItem4 = new Course(contentSnapShot.child("courseId").getValue().toString(),
                                                    contentSnapShot.child("url").getValue().toString(),
                                                    contentSnapShot.child("title").getValue().toString(),
                                                    contentSnapShot.child("name").getValue().toString(),
                                                    contentSnapShot.child("price").getValue().toString());
                                            recyclerList1.add(courseRecyclerViewItem4);
                                            itemSize4++;
                                            Log.i("Tag", "Title1= " + courseRecyclerViewItem4.getTitle() + " Url1 = "+courseRecyclerViewItem4.getImageUrl() );
                                        }
                                     /*   mDatabaseReference.child(courseList.get(4).getCourseName()).addValueEventListener(new ValueEventListener() {
                                            @SuppressLint("NotifyDataSetChanged")
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot contentSnapShot : dataSnapshot.getChildren()) {
                                                    courseRecyclerViewItem5 = new Course(contentSnapShot.child("courseId").getValue().toString(),
                                                            contentSnapShot.child("url").getValue().toString(),
                                                            contentSnapShot.child("title").getValue().toString(),
                                                            contentSnapShot.child("name").getValue().toString(),
                                                            contentSnapShot.child("price").getValue().toString());
                                                    recyclerList1.add(courseRecyclerViewItem5);
                                                    itemSize5++;
                                                    Log.i("Tag", "Title1= " + courseRecyclerViewItem5.getTitle() + " Url1 = "+courseRecyclerViewItem5.getImageUrl() );
                                                }






                                                RecyclerView.LayoutManager layoutManager
                                                        = new LinearLayoutManager(AllCourseActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                                RecyclerView1.setLayoutManager(layoutManager);
                                                courseRecyclerAdapter1 = new CourseRecyclerAdapter(recyclerList1,AllCourseActivity.this,itemSize1,itemSize2,itemSize3,itemSize4,itemSize5);
                                                RecyclerView1.setAdapter(courseRecyclerAdapter1);

                                                courseRecyclerAdapter1.notifyDataSetChanged();
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });

                                      */

                                        RecyclerView.LayoutManager layoutManager
                                                = new LinearLayoutManager(AllCourseActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                        RecyclerView1.setLayoutManager(layoutManager);
                                        courseRecyclerAdapter1 = new CourseRecyclerAdapter(recyclerList1,AllCourseActivity.this,itemSize1,itemSize2,itemSize3,itemSize4,itemSize5);
                                        RecyclerView1.setAdapter(courseRecyclerAdapter1);

                                        courseRecyclerAdapter1.notifyDataSetChanged();
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                                RecyclerView.LayoutManager layoutManager
                                        = new LinearLayoutManager(AllCourseActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                RecyclerView1.setLayoutManager(layoutManager);
                                courseRecyclerAdapter1 = new CourseRecyclerAdapter(recyclerList1,AllCourseActivity.this,itemSize1,itemSize2,itemSize3,itemSize4,itemSize5);
                                RecyclerView1.setAdapter(courseRecyclerAdapter1);

                                courseRecyclerAdapter1.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });

                        RecyclerView.LayoutManager layoutManager
                                = new LinearLayoutManager(AllCourseActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        RecyclerView1.setLayoutManager(layoutManager);
                        courseRecyclerAdapter1 = new CourseRecyclerAdapter(recyclerList1,AllCourseActivity.this,itemSize1,itemSize2,itemSize3,itemSize4,itemSize5);
                        RecyclerView1.setAdapter(courseRecyclerAdapter1);

                        courseRecyclerAdapter1.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                RecyclerView.LayoutManager layoutManager
                        = new LinearLayoutManager(AllCourseActivity.this, LinearLayoutManager.HORIZONTAL, false);
                RecyclerView1.setLayoutManager(layoutManager);
                courseRecyclerAdapter1 = new CourseRecyclerAdapter(recyclerList1,AllCourseActivity.this,itemSize1,itemSize2,itemSize3,itemSize4,itemSize5);
                RecyclerView1.setAdapter(courseRecyclerAdapter1);

                courseRecyclerAdapter1.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void loadRecyclerView4Firebase() {
        mDatabaseReference.child(courseList.get(2).getCourseName()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot contentSnapShot : dataSnapshot.getChildren()) {
                    courseRecyclerViewItem1 = new Course(contentSnapShot.child("courseId").getValue().toString(),
                            contentSnapShot.child("url").getValue().toString(),
                            contentSnapShot.child("title").getValue().toString(),
                            contentSnapShot.child("name").getValue().toString(),
                            contentSnapShot.child("price").getValue().toString());
                    recyclerList4.add(courseRecyclerViewItem1);
                    Log.i("Tag", "Title1= " + courseRecyclerViewItem1.getTitle() + " Url1 = "+courseRecyclerViewItem1.getImageUrl() );
                }
                Log.i("Tag", "itemSize1= " +itemSize1 );

                //recyclerView for 3rd course
                RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager
                        (AllCourseActivity.this, LinearLayoutManager.HORIZONTAL, false);
                RecyclerView4.setLayoutManager(layoutManager4);
                courseRecyclerAdapter4 = new CourseRecyclerAdapter4(recyclerList4,AllCourseActivity.this);
                RecyclerView4.setAdapter(courseRecyclerAdapter4);
                courseRecyclerAdapter4.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void loadRecyclerView3Firebase() {
        itemSize1=0;
        mDatabaseReference.child(courseList.get(1).getCourseName()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot contentSnapShot : dataSnapshot.getChildren()) {
                    courseRecyclerViewItem1 = new Course(contentSnapShot.child("courseId").getValue().toString(),
                            contentSnapShot.child("url").getValue().toString(),
                            contentSnapShot.child("title").getValue().toString(),
                            contentSnapShot.child("name").getValue().toString(),
                            contentSnapShot.child("price").getValue().toString());
                    recyclerList3.add(courseRecyclerViewItem1);
                    Log.i("Tag", "Title1= " + courseRecyclerViewItem1.getTitle() + " Url1 = "+courseRecyclerViewItem1.getImageUrl() );
                }
                //recyclerView for 2nd course
                RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager
                        (AllCourseActivity.this, LinearLayoutManager.HORIZONTAL, false);
                RecyclerView3.setLayoutManager(layoutManager2);
                courseRecyclerAdapter3 = new CourseRecyclerAdapter3(recyclerList3,AllCourseActivity.this);
                RecyclerView3.setAdapter(courseRecyclerAdapter3);
                courseRecyclerAdapter3.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void loadRecyclerView2Firebase() {
        mDatabaseReference.child(courseList.get(0).getCourseName()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot contentSnapShot : dataSnapshot.getChildren()) {
                    courseRecyclerViewItem1 = new Course(contentSnapShot.child("courseId").getValue().toString(),
                            contentSnapShot.child("url").getValue().toString(),
                            contentSnapShot.child("title").getValue().toString(),
                            contentSnapShot.child("name").getValue().toString(),
                            contentSnapShot.child("price").getValue().toString());
                    recyclerList2.add(courseRecyclerViewItem1);

                    Log.i("Tag", "Title1= " + courseRecyclerViewItem1.getTitle() + " Url1 = "+courseRecyclerViewItem1.getImageUrl() );
                }
                //recyclerView for 1st course
                RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager
                        (AllCourseActivity.this, LinearLayoutManager.HORIZONTAL, false);
                RecyclerView2.setLayoutManager(layoutManager1);
                courseRecyclerAdapter2 = new CourseRecyclerAdapter2(recyclerList2,AllCourseActivity.this);
                RecyclerView2.setAdapter(courseRecyclerAdapter2);
                courseRecyclerAdapter2.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void loadRecyclerView5Firebase() {
        mDatabaseReference.child(courseList.get(3).getCourseName()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot contentSnapShot : dataSnapshot.getChildren()) {

                    courseRecyclerViewItem1 = new Course(contentSnapShot.child("courseId").getValue().toString(),
                            contentSnapShot.child("url").getValue().toString(),
                            contentSnapShot.child("title").getValue().toString(),
                            contentSnapShot.child("name").getValue().toString(),
                            contentSnapShot.child("price").getValue().toString());
                    recyclerList5.add(courseRecyclerViewItem1);

                    Log.i("Tag", "Title1= " + courseRecyclerViewItem1.getTitle() + " Url1 = "+courseRecyclerViewItem1.getImageUrl() );
                }
                //recyclerView for 1st course
                RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager
                        (AllCourseActivity.this, LinearLayoutManager.HORIZONTAL, false);
                RecyclerView5.setLayoutManager(layoutManager1);
                courseRecyclerAdapter5 = new CourseRecyclerAdapter5(recyclerList5,AllCourseActivity.this);
                RecyclerView5.setAdapter(courseRecyclerAdapter5);
                courseRecyclerAdapter5.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void loadRecyclerView6Firebase() {
        mDatabaseReference.child(courseList.get(4).getCourseName()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot contentSnapShot : dataSnapshot.getChildren()) {
                    courseRecyclerViewItem1 = new Course(contentSnapShot.child("courseId").getValue().toString(),
                            contentSnapShot.child("url").getValue().toString(),
                            contentSnapShot.child("title").getValue().toString(),
                            contentSnapShot.child("name").getValue().toString(),
                            contentSnapShot.child("price").getValue().toString());
                    recyclerList6.add(courseRecyclerViewItem1);
                    Log.i("Tag", "Title1= " + courseRecyclerViewItem1.getTitle() + " Url1 = "+courseRecyclerViewItem1.getImageUrl() );
                }
                //recyclerView for 5th course
                RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager
                        (AllCourseActivity.this, LinearLayoutManager.HORIZONTAL, false);
                RecyclerView6.setLayoutManager(layoutManager1);
                courseRecyclerAdapter6 = new CourseRecyclerAdapter6(recyclerList6,AllCourseActivity.this);
                RecyclerView6.setAdapter(courseRecyclerAdapter6);
                courseRecyclerAdapter6.notifyDataSetChanged();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.courseMenuCart) {
            Intent intent = new Intent(getApplicationContext(),CartActivity.class);
            startActivity(intent);
            //finish();
            return true;
        }
        else if (id == R.id.courseMenuHelp) {
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

    @Override
    protected void onPause() {
        super.onPause();
       // overridePendingTransition(R.anim.fadein, R.anim.fadeout);

    }
}