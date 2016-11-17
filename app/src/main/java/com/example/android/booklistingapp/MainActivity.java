package com.example.android.booklistingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Book>> {

    //http query base
    private final static String GOOGLE_APIWEB =
            "https://www.googleapis.com/books/v1/volumes?maxResults=7&q=";

    //id for the loader
    private final static int BOOKLOADER_ID = 0;

    //the user edit text, useful for checking user input
    private EditText mEditText;

    //the empty view
    private TextView mEmptyTextView;

    //the adapter for listview
    private BookAdapter mAdapter;

    //loading indicator
    View mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set on click listener
        mEditText = (EditText) findViewById(R.id.edit_search);
        Button button_search = (Button) findViewById(R.id.button_search);

        //pass in empty list into adapter
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        //get the loading indicator and set it to GONE during start
        mLoadingIndicator = findViewById(R.id.loading_indicator);
        mLoadingIndicator.setVisibility(View.GONE);

        //set adapter and empty state
        ListView bookListView = (ListView) findViewById(R.id.listview);
        mEmptyTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyTextView);
        bookListView.setAdapter(mAdapter);

        //set onclick listener for the search button
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                //check for internet connectivity
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    //set loading indicator in case network takes long to load
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    mEmptyTextView.setText(R.string.loading);

                    //check whether user has input any string in edit text
                    if (!mEditText.getText().toString().isEmpty()) {
                        //destroy current loader if it is started
                        if (getLoaderManager().getLoader(BOOKLOADER_ID) != null) {
                            getLoaderManager().destroyLoader(BOOKLOADER_ID);
                        }
                        getLoaderManager().initLoader(BOOKLOADER_ID, null, MainActivity.this);
                    }
                }
                // if there is no connectivity display error
                else {
                    //to correctly display the empty textview, need to make the listview empty
                    //in case the connection goes out in the middle of using
                    mAdapter.clear();
                    mLoadingIndicator.setVisibility(View.GONE);
                    mEmptyTextView.setText(R.string.no_connection);
                }
            }
        });
    }

    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int i, Bundle bundle) {
        StringBuilder buildUrl = new StringBuilder();
        buildUrl.append(GOOGLE_APIWEB).append(mEditText.getText().toString());
        return new BookLoader(this, buildUrl.toString());
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {
        //clean adapter
        mAdapter.clear();
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> books) {
        //set empty state when no data is found
        mEmptyTextView.setText(R.string.no_data);

        //clear the adapter of previous data
        mAdapter.clear();

        //set loading indicator to GONE
        mLoadingIndicator.setVisibility(View.GONE);

        //update adapter if list is not null
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }
}
