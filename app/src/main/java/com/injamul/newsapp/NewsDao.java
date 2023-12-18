package com.injamul.newsapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NewsDao {

    @Insert
    long insertNews(NewsEntity news);

    @Update
    void updateNews(NewsEntity news);

    @Query("SELECT * FROM news_table")
    List<NewsEntity> getAllNews();

    @Query("SELECT * FROM news_table WHERE category = :category")
    List<NewsEntity> getNewsByCategory(String category);

    @Query("UPDATE news_table SET isRead = :isRead WHERE id = :id")
    void updateReadStatus(int id, boolean isRead);

    @Query("SELECT * FROM news_table WHERE url = :url")
    NewsEntity getNewsByUrl(String url);

    @Query("SELECT * FROM news_table")
    List<NewsEntity> getAllNewsSync();

}
