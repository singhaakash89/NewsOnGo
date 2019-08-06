package com.demoapp.inshort.view.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.demoapp.inshort.Logger.Logger;
import com.demoapp.inshort.R;
import com.demoapp.inshort.constants.Constants;


/**
 * Created by aakashsingh on 12/03/17.
 */

public class ProfileFragment extends Fragment implements Constants {

    private static final String TAG = ProfileFragment.class.getSimpleName();
    private static final String URL = "https://www.linkedin.com/in/aakash-singh-75277360/";
    private Context context;
    private ProgressBar mProgressBar;
    private WebView webView;
    private View view;
    private Typeface tf_700;
    private Typeface tf_500;
    private Toolbar toolbar;
    private ImageView closeIV;

    public ProfileFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //[V.IMPORTANT  : BELOW IS IMP FOR UPDATING SECTOR NAME AND OTHER ATTRIBUTES FROM OTHER ACTIVITY]
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        view = layoutInflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initAppBar();

        initWebview();

        renderWebPage(URL);
    }

    private void initAppBar() {
        // Loading Font Face
        tf_700 = Typeface.createFromAsset(getActivity().getAssets(), FONTPATH_MUSEOSANS_700);
        tf_500 = Typeface.createFromAsset(getActivity().getAssets(), FONTPATH_MUSEOSANS_500);

        TextView titleAppBar = (TextView) view.findViewById(R.id.titleAppBar);
        TextView titleAppBarDesc = (TextView) view.findViewById(R.id.titleAppBarDesc);
        titleAppBar.setText("My Linked Profile");
        titleAppBarDesc.setText(URL);
        titleAppBar.setTypeface(tf_700);
        titleAppBarDesc.setTypeface(tf_500);
    }

    private void initWebview() {
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        webView = (WebView) view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
    }

    // Custom method to render a web page
    protected void renderWebPage(String urlToRender) {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Logger.putInDebugLog(TAG, "onReceivedError", "");
                Logger.putInDebugLog(TAG, "errorCode", "" + errorCode + "");
                Logger.putInDebugLog(TAG, "description", "" + description + "");
                Logger.putInDebugLog(TAG, "failingUrl", "" + failingUrl + "");
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // Do something on page loading started
                // Visible the progressbar
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // Do something when page loading finished
                //Toast.makeText(BlogReadingActivity.this, "Page Loaded.", Toast.LENGTH_SHORT).show();
            }

        });

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                // Update the progress bar with page loading progress
                mProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    // Hide the progressbar
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });

        webView.loadUrl(urlToRender);
    }

}