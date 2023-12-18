package com.injamul.newsapp;

import android.content.Context;

import java.util.List;

public class NewsRepository {
    private NewsDao newsDao;

    public NewsRepository(Context context) {
        NewsDatabase newsDatabase = NewsDatabase.getDatabase(context);
        newsDao = newsDatabase.newsDao();
    }

    public long saveNewsToDatabase(NewsEntity article) {
        NewsEntity newsEntity = new NewsEntity(
                article.getTitle(),
                article.getDescription(),
                article.getUrlToImage(),
                article.getUrl(),
                article.getContent(),
                false
        );

        long insertionResult = newsDao.insertNews(newsEntity);

        if (insertionResult != -1) {
            // Optionally, provide feedback about successful insertion
        } else {
            // Handle the case where insertion failed
            // Optionally, log an error or provide feedback to the user
        }

        return insertionResult;
    }

    public void markNewsAsRead(long id) {
        newsDao.updateReadStatus((int) id, true);
    }

    public NewsEntity getNewsByUrl(String url) {
        return newsDao.getNewsByUrl(url);
    }

    public List<NewsEntity> getAllNewsSync() {
        return newsDao.getAllNewsSync();
    }

}
