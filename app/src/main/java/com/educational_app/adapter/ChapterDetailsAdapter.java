package com.educational_app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.educational_app.R;
import com.educational_app.model.ChapterDetailsItem;

import java.util.List;


public class ChapterDetailsAdapter extends RecyclerView.Adapter<ChapterDetailsAdapter.MyViewHolder>{

    private List<ChapterDetailsItem> recyclerViewItems;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private int expandedPosition = -1;

    public ChapterDetailsAdapter(List<ChapterDetailsItem> recyclerViewItems, Context context) {
        this.recyclerViewItems = recyclerViewItems;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chapter_details_recycler_view_item, parent, false);
        return new MyViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holders, @SuppressLint("RecyclerView") int position) {
        ChapterDetailsItem recyclerViewItem = recyclerViewItems.get(position);
        final MyViewHolder holder = holders;

        holder.question.setText(recyclerViewItem.getQuestion());
        holder.answerTV.setText(recyclerViewItem.getAnswer());



        final boolean isExpanded = position==expandedPosition;
        holder.answer.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(context, "This is item in position " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                expandedPosition = isExpanded ? -1:position;
                TransitionManager.beginDelayedTransition(holder.answer);
                notifyItemChanged(position);
            }
        });
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

    public class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView question,answerTV;
        LinearLayout answer;


        OnItemClickListener onItemClickListener;

        public MyViewHolder(View itemView, OnItemClickListener onItemClickListener) {

            super(itemView);
            question = itemView.findViewById(R.id.questionTextView);
            answerTV = itemView.findViewById(R.id.answerTextView);
            answer = itemView.findViewById(R.id.answerLayout);
            this.onItemClickListener = onItemClickListener;

        }


    }
}