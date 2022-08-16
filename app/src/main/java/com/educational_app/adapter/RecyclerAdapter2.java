package com.educational_app.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.educational_app.R;
import com.educational_app.activity.ChapterActivity;
import com.educational_app.model.RecyclerViewItem;
import com.bumptech.glide.Glide;

import java.util.List;


public class RecyclerAdapter2 extends RecyclerView.Adapter<RecyclerAdapter2.MyViewHolder>{

    private final List<RecyclerViewItem> recyclerViewItems ;
    private final Context context;
    String homeID;
    private OnItemClickListener onItemClickListener;

    public RecyclerAdapter2(List<RecyclerViewItem> recyclerViewItems, Context context,String homeID) {
        this.recyclerViewItems = recyclerViewItems;
        this.context = context;
        this.homeID = homeID;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item2, parent, false);
        return new MyViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holders, int position) {
        RecyclerViewItem recyclerViewItem = recyclerViewItems.get(position);
        Glide.with(context)
                .load(recyclerViewItem.getImageUrl())
                .fitCenter()
                .into(holders.imageView);

        holders.title.setText(recyclerViewItem.getTitle());

    }

    @Override
    public int getItemCount() {
        return recyclerViewItems.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

        TextView title;
        ImageView imageView;

        OnItemClickListener onItemClickListener;

        public MyViewHolder(View itemView, OnItemClickListener onItemClickListener) {

            super(itemView);

            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.img);
            this.onItemClickListener = onItemClickListener;

        }

        @Override
        public void onClick(View v) {

           // Toast.makeText(context, "position " + getAdapterPosition()+" "+recyclerViewItems.get(getAdapterPosition()).getTitle(), Toast.LENGTH_SHORT).show();
          int position = getAdapterPosition();
            Intent intent= new Intent(context,ChapterActivity.class);
                  intent.putExtra("coursePosition",position);
                  intent.putExtra("courseID",homeID);
            intent.putExtra("topicName",recyclerViewItems.get(getAdapterPosition()).getTitle());
              Log.i("Tag","CoursePosition from adapter = "+getAdapterPosition());
                  context.startActivity(intent);





        }
    }
}