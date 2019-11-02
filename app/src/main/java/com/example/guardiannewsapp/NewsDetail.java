package com.example.guardiannewsapp;


import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Class NewsDetails to show detailed News.
 */
public class NewsDetail extends AppCompatActivity {

    // TextView to store and show news title.
    TextView title;
    // TextView to store and show news author.
    TextView author;
    // TextView to store and show news publication date.
    TextView date;
    // TextView to store and show news description.
    TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initialize Views in layout.
        title = findViewById(R.id.title);
        author = findViewById(R.id.author);
        date = findViewById(R.id.date);
        description = findViewById(R.id.description);


        // Get Extra String which are send from MainActivity and store it.
        String title2 = getIntent().getStringExtra("title");
        String author2 = getIntent().getStringExtra("author");
        String Date2 = getIntent().getStringExtra("date");
        String description2 = getIntent().getStringExtra("desc");


        // Set Data Extracted to Layout Views.
        title.setText(title2);
        author.setText(author2);
        date.setText("Published Date : " + Date2);
        description.setText(description2);


    }
}