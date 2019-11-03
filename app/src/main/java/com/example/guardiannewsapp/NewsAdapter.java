package com.example.guardiannewsapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom Adapter to arrange news items in List View.
 */
public class NewsAdapter extends ArrayAdapter<NewsClass> {

    // private List<NewsClass> newsList = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param context  it takes context.
     * @param resource it takes resource id.
     * @param news     it takes array of data.
     */
    NewsAdapter(@NonNull Context context, int resource, ArrayList<NewsClass> news) {
        super(context, resource, news);
    }

    /**
     * define view to take list_item elements and put it in layout.
     *
     * @param position    it takes every news element position from the arraylist.
     * @param convertView it takes view and inflates list_item in parent.
     * @param parent      it takes parent.
     * @return it returns View of each News Element with its data according to list_item.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Define new View and attach give it convertview.
        // View view = convertView;

        //Check if convertview is null and inflate list_item layout into it.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,
                    parent,
                    false);
        }

        // create object from NewsClass and get its position.
        NewsClass newsClass = getItem(position);

        // declare and define news title TextView.
        TextView news_title = convertView.findViewById(R.id.news_title);
        // declare and define news Author TextView.
        TextView news_author_name = convertView.findViewById(R.id.news_author_name);
        // put element title and attach it to its TextView.
        news_title.setText(newsClass.getNews_title());
        // put element author and attach it to its TextView.
        news_author_name.setText("Author : " + newsClass.getAuthor_name());

        TextView news_section_name = convertView.findViewById(R.id.news_section_name);
        // declare and define news Author TextView.
        TextView news_publish_date = convertView.findViewById(R.id.news_publish_date);
        // put element title and attach it to its TextView.
        news_section_name.setText("Section : " + newsClass.getNews_section_name());
        // put element author and attach it to its TextView.
        news_publish_date.setText("Published At : " + newsClass.getDate());


        return convertView;
    }

}
