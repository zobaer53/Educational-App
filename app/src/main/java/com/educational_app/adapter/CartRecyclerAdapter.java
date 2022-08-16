package com.educational_app.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.educational_app.R;
import com.educational_app.activity.CartActivity;
import com.educational_app.database.DataBase;
import com.educational_app.model.Order;
import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.MyViewHolder> {
    private final Context context;
    private final List<Order> coursesList;
    String courseName,item;


    public CartRecyclerAdapter(Context context, List<Order> coursesList, String courseName, String item) {
        this.context = context;
        this.coursesList = coursesList;
        this.courseName = courseName;
        this.item = item;
    }

    @NonNull
    @Override
    public CartRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.cart_recycle_item,parent,false);
        return new MyViewHolder(view);
    }
    int count=0;

    @Override
    public void onBindViewHolder(@NonNull CartRecyclerAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Order courseList = coursesList.get(position);
        Glide.with(context)
                .load(courseList.getImageUrl())
                //.fitCenter()
                .into(holder.cartImage);


        holder.cartTitleTV.setText(courseList.getProductName());
        holder.cartInstructorName.setText(courseList.getInstructorName());
        int total=Integer.parseInt(courseList.getPrice());
        Locale locale = new Locale("hi","IN");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        holder.cartPrice.setText(format.format(total));




        holder.remove.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onClick(View view) {

                //Toast.makeText(context, "Remove clicked"+position, Toast.LENGTH_SHORT).show();

                coursesList.remove(position);
                new DataBase((context)).clearCart();


                for(Order item: coursesList){
                    new DataBase((context)).addToCart(item);
                    count++;

                }

                Intent intent = new Intent(context,CartActivity.class);

                context.startActivity(intent);
                ((Activity)context).finish();


            }
        });
    }

    public int updatedItemCount(){

        return  count;
    }

    @Override
    public int getItemCount() {
        return coursesList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView cartImage;
        TextView cartTitleTV,cartInstructorName,cartPrice,remove;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cartImage = itemView.findViewById(R.id.cartImageView);
            cartTitleTV = itemView.findViewById(R.id.cartTitleTV);
            cartInstructorName = itemView.findViewById(R.id.cartInstructorName);
            cartPrice = itemView.findViewById(R.id.cartPriceTV);

            remove = itemView.findViewById(R.id.remove);

        }


    }
}
