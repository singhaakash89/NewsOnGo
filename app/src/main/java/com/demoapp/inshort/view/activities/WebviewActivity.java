package com.demoapp.inshort.view.activities;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.demoapp.inshort.Logger.Logger;
import com.demoapp.inshort.R;
import com.demoapp.inshort.constants.Constants;

import static com.demoapp.inshort.constants.Constants.APP_BAR_TITLE;
import static com.demoapp.inshort.constants.Constants.APP_BAR_TITLE_DESC;
import static com.demoapp.inshort.constants.Constants.FONTPATH_MUSEOSANS_300;
import static com.demoapp.inshort.constants.Constants.FONTPATH_MUSEOSANS_500;
import static com.demoapp.inshort.constants.Constants.FONTPATH_MUSEOSANS_700;
import static com.demoapp.inshort.constants.Constants.FONTPATH_MUSEOSANS_900;
import static com.demoapp.inshort.constants.Constants.NEWS_URL;


/**
 * Created by aakashsingh on 16/09/17.
 */

public class WebviewActivity extends AppCompatActivity implements Constants {

    private static final String TAG = WebviewActivity.class.getSimpleName();
    private Typeface tf_300;
    private Typeface tf_700;
    private Typeface tf_500;
    private Typeface tf_900;
    private Toolbar toolbar;
    private ImageView closeIV;
    private String newsUrl;
    private String appBarTitle;
    private String appBarTitleDesc;
    private ProgressBar mProgressBar;
    private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        //fetch intent data
        initData();

        //init app bar
        initAppBar();

        //init webview
        initWebview();

        //render wepage
        renderWebPage(newsUrl);

        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebviewActivity.this.finish();
                overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
            }
        });
    }

    private void initWebview() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
    }

    private void initData() {
        newsUrl = getIntent().getStringExtra(NEWS_URL);
        appBarTitle = getIntent().getStringExtra(APP_BAR_TITLE);
        appBarTitleDesc = getIntent().getStringExtra(APP_BAR_TITLE_DESC);
    }

    private void initAppBar() {
        // Loading Font Face
        tf_300 = Typeface.createFromAsset(getAssets(), FONTPATH_MUSEOSANS_300);
        tf_700 = Typeface.createFromAsset(getAssets(), FONTPATH_MUSEOSANS_700);
        tf_500 = Typeface.createFromAsset(getAssets(), FONTPATH_MUSEOSANS_500);
        tf_900 = Typeface.createFromAsset(getAssets(), FONTPATH_MUSEOSANS_900);

        //toolbar
        toolbar = (Toolbar) findViewById(R.id.appBar);
        TextView titleAppBar = (TextView) toolbar.findViewById(R.id.titleAppBar);
        TextView titleAppBarDesc = (TextView) toolbar.findViewById(R.id.titleAppBarDesc);
        closeIV = (ImageView) toolbar.findViewById(R.id.closeIV);
        titleAppBar.setText(appBarTitle);
        titleAppBarDesc.setText(appBarTitleDesc);
        titleAppBar.setTypeface(tf_700);
        titleAppBarDesc.setTypeface(tf_500);
        setSupportActionBar(toolbar);
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


    @Override
    public void onBackPressed() {
        WebviewActivity.this.finish();
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
