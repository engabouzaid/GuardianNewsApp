package com.example.guardiannewsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<NewsClass>> {
    // Define and Initialize news API String.
    static String NEWS_API;
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
    // Define news search string to search for.
    String s;
    // Define LoaderManager
    LoaderManager loaderManager;
    // Define Empty Text TextView.
    TextView emptyTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // define views in activity_main_layout.
        news_title = findViewById(R.id.edit_txt_news);
        search = findViewById(R.id.search_btn);
        listView = findViewById(R.id.listview);
        progressBar = findViewById(R.id.progeress_bar);

        //set progressBar visibility to INVISIBLE .
        progressBar.setVisibility(View.INVISIBLE);
        emptyTextView = findViewById(R.id.empty_list_view_text);


        // handle listview items clicks to open NewsDetails Activity to show element details.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsClass currentnews = newsAdapter.getItem(position);

                // get every news element webURL and store it in variables.

                String webURL = currentnews.getWebURL();
                if (!webURL.startsWith("http://") && !webURL.startsWith("https://"))
                    webURL = "http://" + webURL;

                Toast.makeText(MainActivity.this, "webURL" + webURL, Toast.LENGTH_LONG).show();
                Log.v("Web URL_________", webURL);

                //Start Intent to open Internet Browser with the News URL.
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webURL));
                startActivity(browserIntent);


            }
        });

        // Check Internet Connectivity.
        if (isNetworkConnected()) {
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Fetching News", Toast.LENGTH_SHORT).show();

            //https://content.guardianapis.com/search?show-tags=contributor&api-key=fb2fab34-e55f-411f-9892-af1c207c759c
            // Enter News Api and store it and give it user search word.
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("content.guardianapis.com")
                    .appendPath("search")
                    .appendQueryParameter("show-tags", "contributor")
                    .appendQueryParameter("api-key", "fb2fab34-e55f-411f-9892-af1c207c759c");

            NEWS_API = builder.build().toString();

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

                        // Enter News Api and store it and give it user search word.
                        Uri.Builder builder = new Uri.Builder();
                        builder.scheme("https")
                                .authority("content.guardianapis.com")
                                .appendPath("search")
                                .appendQueryParameter("show-tags", "contributor")
                                .appendQueryParameter("q", s)
                                .appendQueryParameter("api-key", "fb2fab34-e55f-411f-9892-af1c207c759c");

                        NEWS_API = builder.build().toString();


                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(
                                Activity.INPUT_METHOD_SERVICE);
                        if (inputMethodManager != null) {
                            inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        }

                        // empty search Box after making search.
                        news_title.setText("");

                        // hide progress Bar.
                        progressBar.setVisibility(View.INVISIBLE);
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
            //progressBar.setVisibility(View.INVISIBLE);


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
        listView.setEmptyView(emptyTextView);
        progressBar.setVisibility(View.INVISIBLE);

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