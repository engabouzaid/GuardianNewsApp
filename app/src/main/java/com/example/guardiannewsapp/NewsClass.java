package com.example.guardiannewsapp;


/**
 * Class to make and define objects of news.
 */
public class NewsClass {

    // Define String of News(title,author,description,publishDate).
    private String news_title;
    private String author_name;
    private String description;
    private String date;

    /**
     * Constructor to make objects from News .
     *
     * @param news_title  it takes news title  string.
     * @param author_name it takes news author name string.
     * @param description it takes news description string.
     * @param date        it takes news publish date string.
     */
    NewsClass(String news_title, String author_name, String description, String date) {
        this.news_title = news_title;
        this.author_name = author_name;
        this.description = description;
        this.date = date;
    }

    /**
     * @return news title.
     */
    String getNews_title() {
        return news_title;
    }

    /**
     * @return news author name.
     */
    String getAuthor_name() {
        return author_name;
    }

    /**
     * @return news description.
     */
    String getDescription() {
        return description;
    }

    /**
     * @return news publish date.
     */
    String getDate() {
        return date;
    }
}