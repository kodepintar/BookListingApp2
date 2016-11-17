package com.example.android.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by grandia on 11/17/2016.
 */

public class BookLoader extends AsyncTaskLoader<ArrayList<Book>> {

    //url for http request
    private String mUrl;

    public BookLoader(Context context, String url ) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Book> loadInBackground() {
        ArrayList<Book> booksList = QueryUtils.extractBook(mUrl);
        return booksList;
    }


}
