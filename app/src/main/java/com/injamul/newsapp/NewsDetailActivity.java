package com.injamul.newsapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class NewsDetailActivity extends AppCompatActivity {

    String title, content, description, imageURL, url;
    private TextView titleTV, subDescTV, contentTV;
    private ImageView newsIV;
    private Button readNewsBtn, saveNewsBtn;

    private NewsDatabase newsDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        description = getIntent().getStringExtra("description");
        imageURL = getIntent().getStringExtra("image");
        url = getIntent().getStringExtra("url");

        titleTV = findViewById(R.id.NewsDetailTitle);
        subDescTV = findViewById(R.id.Subdesc);
        contentTV = findViewById(R.id.DetailContent);
        newsIV = findViewById(R.id.IVnewsDetail);
        readNewsBtn = findViewById(R.id.btnReadNews);
        saveNewsBtn = findViewById(R.id.btnSaveNews); // Added Save News button

        titleTV.setText(title);
        subDescTV.setText(description);
        contentTV.setText(content);
        Picasso.get().load(imageURL).into(newsIV);

        // Room database Initialization
        newsDatabase = NewsDatabase.getDatabase(getApplicationContext());

        readNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the URL in a browser
                openNewsUrlInBrowser();
            }
        });

        // Save news for offline reading
        saveNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Saving news article to the Room database
                saveNewsToDatabase();
                Log.e(TAG, "onClick: error");
                Log.d(TAG, "onClick: is d");
            }
        });
    }

    private void saveNewsToDatabase() {
        NewsEntity newsEntity = new NewsEntity(
                title,
                description,
                imageURL,
                url,
                content,
                false
        );

        // AsyncTask to perform database operation in the background
        new SaveNewsAsyncTask(getApplicationContext()).execute(newsEntity);
    }

    // AsyncTask to save news in the background
    private static class SaveNewsAsyncTask extends AsyncTask<NewsEntity, Void, Long> {
        private final Context context;

        public SaveNewsAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected Long doInBackground(NewsEntity... newsEntities) {
            // Access the Room database and insert the news
            return NewsDatabase.getDatabase(context).newsDao().insertNews(newsEntities[0]);
        }

        @Override
        protected void onPostExecute(Long newsId) {
            super.onPostExecute(newsId);
            if (newsId != -1) {
                Toast.makeText(context, "News saved for offline reading", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to save news", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Open the news URL in a browser
    private void openNewsUrlInBrowser() {
        // Open the URL in a browser
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }
}
