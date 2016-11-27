package com.example.android.booklistingapp;

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
 * Created by grandia on 11/17/2016.
 */

public final class QueryUtils {

    public QueryUtils() {}

    public static ArrayList<Book> extractBook(String stringUrl) {

        ArrayList<Book> returnBooks = new ArrayList<>();

        try {
            String jsonResponse = makeHttpRequest(createUrl(stringUrl));
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);

            JSONArray jsonItemArray = baseJsonResponse.getJSONArray("items");

            for (int i = 0 ; i < jsonItemArray.length() ; i++) {
                JSONObject currentItem = jsonItemArray.getJSONObject(i);
                JSONObject volumeInfo = currentItem.getJSONObject("volumeInfo");

                //temp list to save author data
                ArrayList<String> authorsList = new ArrayList<>();

                //some books have no author data, check if it is true
                JSONArray authorsArray = volumeInfo.getJSONArray("authors");

                if (authorsArray != null) {
                    try {
                        for (int a = 0; a < authorsArray.length(); a++) {
                            authorsList.add(authorsArray.getString(a));
                        }
                    }
                    catch (JSONException ex){
                        authorsList.add("Author Unknown");
                    }
                }

                String title = volumeInfo.getString("title");

                returnBooks.add(new Book(authorsList, title));
            }
        }
        catch (JSONException ex) {
            Log.e("QueryUtils", "Error parsing JSON", ex);
            return null;
        }
        catch (MalformedURLException ex) {
            Log.e("QueryUtils", "Error on createUrl method", ex);
        }
        catch (IOException ex) {
            Log.e("QueryUtils", "Error on requesting HTTP", ex);
        }

        return returnBooks;
    }

    //make request and return json response
    private static String makeHttpRequest(URL url) throws IOException {
        String returnJson = null;

        if (url == null) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setConnectTimeout(10000);
        urlConnection.setReadTimeout(10000);
        urlConnection.connect();

        if (urlConnection.getResponseCode() == 200) {
            inputStream = urlConnection.getInputStream();
            returnJson = readFromStream(inputStream);
        }

        if (urlConnection != null) {
            urlConnection.disconnect();
        }
        if (inputStream != null) {
            inputStream.close();
        }

        return returnJson;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder returnData = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null)
            {
                returnData.append(line);
                line = bufferedReader.readLine();
            }
        }

        return returnData.toString();
    }

    //format string into URL
    private static URL createUrl (String url) {
        URL returnUrl = null;

        try {
            returnUrl = new URL(url);
        }
        catch (MalformedURLException ex){
            Log.e("QueryUtils", "Error on createUrl method", ex);
            return null;
        }

        return returnUrl;
    }
}
