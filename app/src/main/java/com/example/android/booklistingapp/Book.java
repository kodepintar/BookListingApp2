package com.example.android.booklistingapp;

import java.util.ArrayList;

/**
 * Created by grandia on 11/17/2016.
 */

public class Book {

    /* member variables */
    //storing author name
    private ArrayList<String> mAuthorList;

    //storing title name
    private String mTitle;

    /* constructor */
    public Book(ArrayList<String> author, String title) {
        mAuthorList = author;
        mTitle = title;
    }

    /* getter */
    public ArrayList<String> getAuthor() {
        return mAuthorList;
    }

    public String getTitle() {
        return mTitle;
    }
}
