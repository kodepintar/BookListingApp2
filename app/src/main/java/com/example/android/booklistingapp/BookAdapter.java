package com.example.android.booklistingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by grandia on 11/17/2016.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, ArrayList<Book> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView ==null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Book currentBook = getItem(position);

        StringBuilder authorsName = new StringBuilder();
        ArrayList<String> authorsList = currentBook.getAuthor();

        for (int i = 0 ; i < authorsList.size() ; i++) {
            if (i != authorsList.size() - 1) {
                authorsName.append(authorsList.get(i)).append(",");
            }
            else {
                authorsName.append(authorsList.get(i));
            }
        }

        TextView textView_author = (TextView) listItemView.findViewById(R.id.text_author);
        textView_author.setText(authorsName.toString());

        TextView textView_title = (TextView) listItemView.findViewById(R.id.text_title);
        textView_title.setText(currentBook.getTitle());

        return listItemView;
    }

}
