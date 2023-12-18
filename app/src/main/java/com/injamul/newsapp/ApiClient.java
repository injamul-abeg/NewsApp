package com.injamul.newsapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiClient {
    @GET
    Call<NewsRequestModel>getAllNews(@Url String url);

    @GET
    Call<NewsRequestModel>getNewsByCategory(@Url String url);
}
