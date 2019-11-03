package com.example.guardiannewsapp;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Class to handle HTTP Requests and JSON parsing.
 */
public class Utils {

    private static final String LOG_TAG = Utils.class.getSimpleName();


    /**
     * Fetching data from url and storing it in ArrayList of type NewsClass.
     *
     * @param requestUrl function requires url.
     * @return it return an ArrayList of type NewsClass.
     */
    static ArrayList<NewsClass> fetchNewsData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back.
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an  object.
        // Return the {@link NewsClass}
        return extractFeatureFromJson(jsonResponse);
    }

    /**
     * Function to create URL object from given String URL.
     *
     * @param stringUrl it takes String of URL
     * @return Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        // declare new URL and initalizing it
        URL url = null;

        // surround with try and catch
        try {
            // initialize URL and set param to string to create URL
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // return URL
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     *
     * @param url it takes URL which we get from previos function.
     * @return it return JSON Response.
     * @throws IOException url may make exception of this type
     */
    private static String makeHttpRequest(URL url) throws IOException {
        // declare new empty String.
        String jsonResponse = "";

        // Check if the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        // declare new HttpURLConnection.
        HttpURLConnection urlConnection = null;
        // declare new InputStream.
        InputStream inputStream = null;
        // surround with try and catch
        try {
            // create HTTP request.
            urlConnection = (HttpURLConnection) url.openConnection();
            // set request method.
            urlConnection.setRequestMethod("GET");
            // connect to server.
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                // get InputStream with data to read it.
                inputStream = urlConnection.getInputStream();
                // read from stream and return string then saved it in jsonResponse.
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the News JSON results.", e);
        } finally {
            // if urlConnection return with data
            if (urlConnection != null) {
                // then disconnect .
                urlConnection.disconnect();
            }
            // if inputStream return with data.
            if (inputStream != null) {
                //  close the inputStream.
                inputStream.close();
            }
        }
        // return jsonResponse with data.
        return jsonResponse;
    }

    /**
     * readFromStream to the given InputStream then return String (JSON).
     *
     * @param inputStream it takes inputStream.
     * @return return String of JSON Response.
     * @throws IOException inputStream may make exception.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        // initialize StringBuilder.
        StringBuilder stringBuilder = new StringBuilder();
        // if inputStream return with data.
        if (inputStream != null) {
            // create and initialize InputStreamReader with two params (inputStream, Symbol rate utf-8).
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            // create and initialize BufferedReader with one param (InputStreamReader).
            BufferedReader reader = new BufferedReader(inputStreamReader);
            // new String to save data from BufferedReader line by line.
            String line = reader.readLine();
            // loop to update String line by line and append this lines to stringBuilder.
            while (line != null) {
                stringBuilder.append(line);
                line = reader.readLine();
            }
        }
        // return String.
        return stringBuilder.toString();
    }

    /**
     * extractFeatureFromJson to the given String then return ArrayList<NewsClass>.
     *
     * @param newsJSON it takes json objects.
     * @return ArrayList of data From JSON.
     */
    private static ArrayList<NewsClass> extractFeatureFromJson(String newsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news to.
        ArrayList<NewsClass> news = new ArrayList<>();
        // surround with try and catch.
        try {
            // initialize baseJSONObject.
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            // initialize and get response from baseJSONObject.
            JSONObject response = baseJsonResponse.getJSONObject("response");
            // initialize JSONArray results to get items array.
            JSONArray itemsArray = response.getJSONArray("results");
            // loop to get all items in itemsArray.
            for (int i = 0; i < itemsArray.length(); i++) {
                // JSONObject to get data from each item.
                JSONObject firstitem = itemsArray.getJSONObject(i);

                // Get News Title.
                // String to get news webTitle.
                String title;
                if (firstitem.has("webTitle")) {

                    title = firstitem.getString("webTitle");
                } else {
                    title = "Title not Found";
                }

                //Get News Section Name.
                // String to get news Section Name.
                String sectionName;
                if (firstitem.has("sectionName")) {

                    sectionName = firstitem.getString("sectionName");
                } else {
                    sectionName = "Section Name not Found";
                }


                //Get News Publishing Date.
                // String to get news webPublicationDate.
                String date;
                if (firstitem.has("webPublicationDate")) {

                    date = firstitem.getString("webPublicationDate");
                } else {
                    date = "Published Date not Found";
                }

                // Get News Description Link.
                // String to get news webUrl.
                String webURL;
                if (firstitem.has("webUrl")) {

                    webURL = firstitem.getString("webUrl");
                } else {
                    webURL = "WebURL not Found";
                }

                JSONArray firstitemTags = firstitem.getJSONArray("tags");
                JSONObject firstitemTags0 = firstitemTags.getJSONObject(0);
                //Get News Author Name.
                // String to get news Author Name.
                String author;
                if (firstitemTags0.has("firstName") || firstitemTags0.has("lastName")) {

                    String authorFirstName = firstitemTags0.getString("firstName");
                    String authorLastName = firstitemTags0.getString("lastName");
                    author = authorFirstName + " " + authorLastName;
                } else {
                    author = "Author Name not found";
                }


                // add every object from JSON Response to News ArrayList.
                news.add(new NewsClass(title, author, webURL, date, sectionName));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the News JSON results", e);
        }
        // return Arraylist news.
        return news;
    }


}
