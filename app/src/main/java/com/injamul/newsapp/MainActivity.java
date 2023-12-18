package com.injamul.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CategoryRvAdapter.CategoryClickInterface {

    private RecyclerView newsRV, categoryRV;
    private ProgressBar loadingPB;
    private ArrayList<Articles>articlesArrayList;
    private ArrayList<CategoryRvModel>categoryRvModelArrayList;
    private CategoryRvAdapter categoryRvAdapter;
    private NewsRvAdapter newsRvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsRV = findViewById(R.id.NewsContent);
        categoryRV = findViewById(R.id.categories);
        loadingPB = findViewById(R.id.newsLoading);

        articlesArrayList = new ArrayList<>();
        categoryRvModelArrayList = new ArrayList<>();
        newsRvAdapter = new NewsRvAdapter(articlesArrayList, this);
        categoryRvAdapter = new CategoryRvAdapter(categoryRvModelArrayList, this,this::onCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRvAdapter);
        categoryRV.setAdapter(categoryRvAdapter);
        getCategories();
        getNews("All");
        newsRvAdapter.notifyDataSetChanged();
    }


    @Override
    public void onCategoryClick(int position) {
        String category =categoryRvModelArrayList.get(position).getCategory();
        getNews(category);

    }

    private void getCategories(){

        categoryRvModelArrayList.add(new CategoryRvModel("All","https://images.unsplash.com/photo-1656077217715-bdaeb06bd01f?q=80&w=1964&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRvModelArrayList.add(new CategoryRvModel("Technology","https://images.unsplash.com/photo-1580584126903-c17d41830450?q=80&w=1939&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRvModelArrayList.add(new CategoryRvModel("Science","https://images.unsplash.com/photo-1628595351029-c2bf17511435?q=80&w=1932&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRvModelArrayList.add(new CategoryRvModel("Business","https://images.unsplash.com/photo-1495996797143-9fc04e79e304?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRvModelArrayList.add(new CategoryRvModel("Entertainment","https://images.unsplash.com/photo-1481328101413-1eef25cc76c0?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRvModelArrayList.add(new CategoryRvModel("Sports","https://images.unsplash.com/photo-1525973132219-a04334a76080?q=80&w=1985&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRvModelArrayList.add(new CategoryRvModel("Health","https://images.unsplash.com/photo-1659019479972-82d9e3e8cfb7?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRvAdapter.notifyDataSetChanged();
    }
    //API key: 0c40b5fba39b4991ac6cb31bcd0cc45f
    private void getNews(String category) {
        // Checking if the device is connected to the internet
        if (isNetworkConnected()) {
            loadingPB.setVisibility(View.VISIBLE);
            articlesArrayList.clear();

            String categoryURL = "https://newsapi.org/v2/top-headlines?country=us&category=" + category + "&apiKey=0c40b5fba39b4991ac6cb31bcd0cc45f";
            String url = "https://newsapi.org/v2/top-headlines?country=us&excludeDomains=stackoverflow.com&sortBy=publishedAt&language=en&apiKey=0c40b5fba39b4991ac6cb31bcd0cc45f";
            String BASE_URL = "https://newsapi.org/";

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ApiClient apiClient = retrofit.create(ApiClient.class);
            Call<NewsRequestModel> call;

            if (category.equals("All")) {
                call = apiClient.getAllNews(url);
            } else {
                call = apiClient.getNewsByCategory(categoryURL);
            }

            call.enqueue(new Callback<NewsRequestModel>() {
                @Override
                public void onResponse(Call<NewsRequestModel> call, Response<NewsRequestModel> response) {
                    // Handle the response when the network request is successful
                    NewsRequestModel newsRequestModel = response.body();
                    loadingPB.setVisibility(View.GONE);
                    ArrayList<Articles> articles = newsRequestModel.getArticles();
                    for (int i = 0; i < articles.size(); i++) {
                        articlesArrayList.add(new Articles(articles.get(i).getTitle(), articles.get(i).getDescription(), articles.get(i).getUrlToImage(), articles.get(i).getUrl(), articles.get(i).getContent()));
                    }
                    newsRvAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<NewsRequestModel> call, Throwable t) {
                    // Handle the failure when the network request fails
                    Toast.makeText(MainActivity.this, "Failed to get news. Please check your internet connection.", Toast.LENGTH_SHORT).show();
                    loadingPB.setVisibility(View.GONE);
                }
            });
        } else {
            // Display a message if there is no internet connection
            Toast.makeText(MainActivity.this, "No internet connection available", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to check network connectivity
    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}