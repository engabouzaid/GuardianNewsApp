package com.example.guardiannewsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<NewsClass>> {
    // Define and Initialize news API String.
    static String NEWS_API = "https://content.guardianapis.com/search?api-key=fb2fab34-e55f-411f-9892-af1c207c759c";
    // Define news title EditText to search for.
    EditText news_title;
    // Define news search Button to search for.
    Button search;
    // Define progressBar.
    ProgressBar progressBar;
    // Define news listView .
    ListView listView;
    // Define news news Adapter Object.
    NewsAdapter newsAdapter;
    // Define news image ImageView .
    ImageView news;
    // Define news search string to search for.
    String s;
    // Define LoaderManager
    LoaderManager loaderManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // define views in activity_main_layout.
        news_title = findViewById(R.id.edit_txt_news);
        search = findViewById(R.id.search_btn);
        listView = findViewById(R.id.listview);
        news = findViewById(R.id.news);
        progressBar = findViewById(R.id.progeress_bar);

        //set progressBar visibility to INVISIBLE .
        progressBar.setVisibility(View.INVISIBLE);


        // handle listview items clicks to open NewsDetails Activity to show element details.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsClass currentnews = newsAdapter.getItem(position);

                // get every news element data and store it in variables.
                String title = currentnews.getNews_title();
                String author = currentnews.getAuthor_name();
                String Date = currentnews.getDate();
                String description = currentnews.getDescription();

                // Make new Intent to open NewsDetails Activity.
                Intent intent = new Intent(getApplicationContext(), NewsDetail.class);
                // send every news element data and store it in variables with the Intent.
                intent.putExtra("title", title);
                intent.putExtra("author", author);
                intent.putExtra("date", Date);
                intent.putExtra("desc", description);


                startActivity(intent);
            }
        });

        // Check Internet Connectivity.
        if (isNetworkConnected()) {
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Fetching News", Toast.LENGTH_SHORT).show();

            // Initialize LoaderManager to fetch News When User searches for a specific News.
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // get text entered by user and save it in String s.
                    s = news_title.getText().toString();

                    // check if EditText don't have anything "user didn't write anything to search for.
                    if (s.length() == 0) {
                        //make toast message to user.
                        Toast.makeText(getApplicationContext(), "Please Enter News to search for ", Toast.LENGTH_LONG).show();
                    } else {

                        // hide progressBar
                        news.setVisibility(View.GONE);

                        // Enter News Api and store it and give it user search word.
                        NEWS_API =
                                "https://content.guardianapis.com/search?q=" + s + "&api-key=fb2fab34-e55f-411f-9892-af1c207c759c";


                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(
                                Activity.INPUT_METHOD_SERVICE);
                        if (inputMethodManager != null) {
                            inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        }

                        // empty search Box after making search.
                        news_title.setText("");
                        // Clear Adapter.
                        newsAdapter.clear();
                        // Initialize LoaderManager.
                        loaderManager.initLoader(1, null, MainActivity.this).forceLoad();


                    }
                }


            });
            // Initialize LoaderManager to fetch All News When User do not search any news.
            loaderManager = getSupportLoaderManager();
            // initiate Loader Manager.
            loaderManager.initLoader(1, null, this).forceLoad();
            // hide progress bar.
            news.setVisibility(View.GONE);


        } else {
            // Toast to ask user connect to Internet to fetch News.
            Toast.makeText(this, "Please Connect To internet to fetch News", Toast.LENGTH_LONG).show();
            NEWS_API = null;
        }
    }


    @NonNull
    @Override
    public Loader<ArrayList<NewsClass>> onCreateLoader(int id, @Nullable Bundle bundle) {

        return new NewsAsyncLoader(this);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<NewsClass>> loader, ArrayList<NewsClass> data) {
        newsAdapter = new NewsAdapter(getApplicationContext(), 0, data);
        listView.setAdapter(newsAdapter);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<NewsClass>> loader) {
        newsAdapter.clear();
        newsAdapter = new NewsAdapter(getApplicationContext(), 0, (new ArrayList<NewsClass>()));
        listView.setAdapter(newsAdapter);
    }

    // Function To check Internet Connectivity
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    // Class To Load News In background by AsyncTaskLoader
    static class NewsAsyncLoader extends AsyncTaskLoader<ArrayList<NewsClass>> {

        public NewsAsyncLoader(Context context) {

            super(context);

        }

        @Override
        public ArrayList<NewsClass> loadInBackground() {
            if (NEWS_API == null) {
                return null;
            }


            ArrayList<NewsClass> Newslist = Utils.fetchNewsData(NEWS_API);
            Log.v("ArrayList Created  :  ", "   :   " + Newslist);
            return Newslist;
        }
    }


}