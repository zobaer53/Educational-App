package com.educational_app.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.educational_app.R;
import com.educational_app.model.RecyclerViewItem;
import com.bumptech.glide.Glide;

import java.util.List;


public class RecyclerAdapterForTwoItem extends RecyclerView.Adapter<RecyclerAdapterForTwoItem.MyViewHolder>{

    private final List<RecyclerViewItem> recyclerViewItems ;
    private final Context context;
    private OnItemClickListener onItemClickListener;

    public RecyclerAdapterForTwoItem(List<RecyclerViewItem> recyclerViewItems, Context context) {
        this.recyclerViewItems = recyclerViewItems;
        this.context = context;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.two_itwm_recycler_view, parent, false);
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
            title = itemView.findViewById(R.id.recyclerViewTV);
            imageView = itemView.findViewById(R.id.recyclerViewIcon);
            this.onItemClickListener = onItemClickListener;

        }

        @Override
        public void onClick(View v) {

            //Toast.makeText(context, "position  " + getAdapterPosition() + " " + recyclerViewItems.get(getAdapterPosition()).getTitle(), Toast.LENGTH_SHORT).show();


        }
    }
}
