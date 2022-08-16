package com.educational_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.educational_app.R;
import com.educational_app.Utility.NetworkChangeListener;
import com.educational_app.adapter.CartRecyclerAdapter;
import com.educational_app.database.DataBase;
import com.educational_app.model.Order;
import com.educational_app.model.Request;
import com.educational_app.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CartActivity extends AppCompatActivity implements PaymentResultListener {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    private static final String TAG = CartActivity.class.getSimpleName();

    RecyclerView cartRecyclerView;
    CartRecyclerAdapter cartRecyclerAdapter;
    List<Order> courseList = new ArrayList<>();
    FirebaseDatabase mFirebaseDataBase;
    DatabaseReference mDatabaseReference;
    String courseName,item;
    HashMap<String, Object> userMap;
    int count=0,total=0;;
    TextView cartItemCount,totalPrice;
    Button placeOderButton;

    String currentUserID,phone;
    User user ;

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {

            mDatabaseReference.child("Users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        if(snapshot.exists()){
                            String uid = dataSnapshot.child("uid").getValue().toString();
                            if(currentUserID.equals(uid)) {
                                phone = dataSnapshot.getKey();
                                user = new User(dataSnapshot.child("name").getValue().toString(),
                                        dataSnapshot.child("uimage").getValue().toString(),
                                        dataSnapshot.getKey(),dataSnapshot.child("email").getValue().toString());

                            }
                        }

                    }
                    Log.i("Tag"," phone "+phone);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Checkout.preload(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(" Cart");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnBackPress();
            }
        });

        placeOderButton = findViewById(R.id.buttonPlaceOrder);

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cartItemCount = findViewById(R.id.cartItemCount);
        totalPrice = findViewById(R.id.total);

        mFirebaseDataBase= FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDataBase.getReference();

        userMap = new HashMap<>();

        courseName = getIntent().getStringExtra("courseName");
        item = getIntent().getStringExtra("itemNumber");
        Log.i("Tag"," from course details courseName "+courseName+" course Details "+item);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){

            courseList = new DataBase(this).getCarts();
            for(Order order: courseList){
                total+=Integer.parseInt(order.getPrice());
                count++;
                cartItemCount.setText(""+count+" Item");
                Locale locale = new Locale("hi","IN");
                NumberFormat format = NumberFormat.getCurrencyInstance(locale);
                totalPrice.setText(format.format(total));
            }
            cartRecyclerAdapter = new CartRecyclerAdapter(this,courseList,courseName,item);
            RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(CartActivity.this) ;
            cartRecyclerView.setLayoutManager(layoutManager);
            cartRecyclerView.setAdapter(cartRecyclerAdapter);

        }
        placeOderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(count > 0) {
                    if (FirebaseAuth.getInstance().getCurrentUser()!=null) {

                        startPayment();


                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Cart Empty", Toast.LENGTH_SHORT).show();
                    //finish();
                }
            }
        });


    }
    public void startPayment() {


        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
       // checkout.setKeyID("");

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", user.getUserName());
            options.put("description", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
           // options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", total*100);//pass amount in currency subunits
            options.put("prefill.email", user.getUserEmail());
            options.put("prefill.contact",user.getUserPhone());
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }




    private void handleOnBackPress() {

        super.onBackPressed();

    }

    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            if (FirebaseAuth.getInstance().getCurrentUser()!=null) {

                Request request = new Request(

                        String.valueOf(total),
                        phone,
                        courseList
                );

                mDatabaseReference.child("OrderRequests").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child(String.valueOf(System.currentTimeMillis())).setValue(request);
                new DataBase(getBaseContext()).clearCart();
                Toast.makeText(getApplicationContext(), "Thank you , Order Placed", Toast.LENGTH_SHORT).show();
                finish();

            }
            Intent intent = new Intent(CartActivity.this, ThankYouActivity.class);
            startActivity(intent);
            finish();

        } catch (Exception e) {
            Log.e("Tag", "Exception in onPaymentSuccess", e);
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

            Log.i("Tag", "Exception in onPaymentError", e);

        }
    }


    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

}