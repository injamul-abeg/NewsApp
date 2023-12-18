package com.injamul.newsapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsRvAdapter extends RecyclerView.Adapter<NewsRvAdapter.ViewHolder> {

    private ArrayList<Articles>ArticleArraylist;
    private Context context;

    public NewsRvAdapter(ArrayList<Articles> articleArraylist, Context context) {
        ArticleArraylist = articleArraylist;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsRvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item,parent,false);
        return new NewsRvAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsRvAdapter.ViewHolder holder, int position) {
        Articles articles = ArticleArraylist.get(position);
        holder.titleTV.setText(articles.getTitle());
        holder.subTitleTV.setText(articles.getDescription());
        Picasso.get().load(articles.getUrlToImage()).into(holder.newsIV);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,NewsDetailActivity.class);
                intent.putExtra("title", articles.getTitle());
                intent.putExtra("content", articles.getContent());
                intent.putExtra("description", articles.getDescription());
                intent.putExtra("image", articles.getUrlToImage());
                intent.putExtra("url", articles.getUrl());
                context.startActivity(intent);
            }
        });
        /*// Set background or other visual indicator based on isRead status
        if (articles.isRead()) {
            // Mark as read
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.pink));
        } else {
            // Mark as unread
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.black_shade_1));
        }*/

        /*
        holder.itemView.setOnClickListener(v -> {
            // Toggle isRead status
            articles.setRead(!articles.isRead());

            // Update UI
            notifyItemChanged(position);

            // Handle other actions, e.g., open the article details
            // openArticleDetails(article);
        });*/
    }

    @Override
    public int getItemCount() {
        return ArticleArraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTV, subTitleTV;
        private ImageView newsIV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.TVnewsHeading);
            subTitleTV = itemView.findViewById(R.id.TVsubHeading);
            newsIV = itemView.findViewById(R.id.IVnews);
        }
    }
}
