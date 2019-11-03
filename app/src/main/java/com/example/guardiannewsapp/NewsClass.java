package com.example.guardiannewsapp;


/**
 * Class to make and define objects of news.
 */
public class NewsClass {

    // Define String of News(title,author,description,publishDate).
    private String news_title;
    private String author_name;
    private String webURL;
    private String date;
    private String news_section_name;

    /**
     * Constructor to make objects from News .
     *
     * @param news_title  it takes news title  string.
     * @param author_name it takes news author name string.
     * @param webURL      it takes news description string.
     * @param date        it takes news publish date string.
     */
    NewsClass(String news_title, String author_name, String webURL, String date, String news_section_name) {
        this.news_title = news_title;
        this.author_name = author_name;
        this.webURL = webURL;
        this.date = date;
        this.news_section_name = news_section_name;
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
     * @return news webURL.
     */
    String getWebURL() {
        return webURL;
    }

    /**
     * @return news publish date.
     */
    String getDate() {
        return date;
    }

    /**
     * @return news Section Name.
     */
    String getNews_section_name() {
        return news_section_name;
    }
}