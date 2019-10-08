package com.demoapp.inshort.view.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.demoapp.inshort.Logger.Logger;
import com.demoapp.inshort.R;
import com.demoapp.inshort.constants.Constants;
import com.demoapp.inshort.constants.ServiceConstants;
import com.demoapp.inshort.datastore.model.NewsModel;
import com.demoapp.inshort.view.fragments.FavouriteFragment;
import com.demoapp.inshort.view.fragments.FeedsFragment;
import com.demoapp.inshort.view.fragments.ProfileFragment;
import com.demoapp.inshort.view.toast.ToastManager;
import com.google.gson.Gson;
import com.gssirohi.materialforms.MaterialForm;
import com.gssirohi.materialforms.MaterialFormBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by aakashsingh on 16/09/17.
 */

public class HomeScreenActivity extends AppCompatActivity implements Constants, ServiceConstants {

    private static final String TAG = HomeScreenActivity.class.getSimpleName();
    private static final long EXIT_TIME_OUT = 2000;
    private Typeface tf_300;
    private Typeface tf_700;
    private Typeface tf_500;
    private Typeface tf_900;
    private Toolbar toolbar;
    private double backPressCount;
    private FragmentManager fragmentManager;
    //fragment instances
    private FeedsFragment feedsFragment;
    private FavouriteFragment favouriteFragment;
    private ProfileFragment profileFragment;
    private List<NewsModel> newsModelList;
    private AsyncHttpClient client;
    private String jsonString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //init UI
        initUI();

        //init Appbar
        //initAppBar();

        //init Bottom Navbar
        initBottomNavBar();

        //post to Inshort API for json
        postToYoutubeApiForViewCount(INSHORT_OUTLINK_API_URL);

    }

    private void initBottomNavBar() {
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_feeds);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_favorites) {
                    changeFragment(favouriteFragment, "favouriteFragment", favouriteFragment.getClass().getName());
                } else if (tabId == R.id.tab_feeds) {
                    if (null != feedsFragment) {
                        changeFragment(feedsFragment, "feedsFragment", feedsFragment.getClass().getName());
                    }
                } else if (tabId == R.id.tab_profile) {
                    changeFragment(profileFragment, "profileFragment", profileFragment.getClass().getName());
                }
            }
        });

        //If you want to listen for reselection events, here's how you do it:
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_favorites) {
                    changeFragment(favouriteFragment, "favouriteFragment", favouriteFragment.getClass().getName());
                } else if (tabId == R.id.tab_feeds) {
                    if (null != feedsFragment) {
                        changeFragment(feedsFragment, "feedsFragment", feedsFragment.getClass().getName());
                    }
                } else if (tabId == R.id.tab_profile) {
                    changeFragment(profileFragment, "profileFragment", profileFragment.getClass().getName());
                }
            }
        });
    }

    private void initUI() {
        favouriteFragment = new FavouriteFragment();
        profileFragment = new ProfileFragment();
        fragmentManager = getSupportFragmentManager();
    }

    private void initAppBar() {
        // Loading Font Face
        tf_300 = Typeface.createFromAsset(getAssets(), FONTPATH_MUSEOSANS_300);
        tf_700 = Typeface.createFromAsset(getAssets(), FONTPATH_MUSEOSANS_700);
        tf_500 = Typeface.createFromAsset(getAssets(), FONTPATH_MUSEOSANS_500);
        tf_900 = Typeface.createFromAsset(getAssets(), FONTPATH_MUSEOSANS_900);

        //toolbar
        //toolbar = (Toolbar) findViewById(R.id.appBar);
        TextView titleAppBar = (TextView) toolbar.findViewById(R.id.titleAppBar);
        titleAppBar.setText(R.string.app_name);
        titleAppBar.setTypeface(tf_700);
        setSupportActionBar(toolbar);
    }

    private void postToYoutubeApiForViewCount(String url) {
        client = new AsyncHttpClient();
        //System.out.println("postToYoutubeApiForViewCount : " + url + ";");
        client.get(url, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String res) {
                        // called when response HTTP status is "200 OK"
                        System.out.println("AsyncHttpClient_Response : " + res + ";");
                        jsonString = res;

                        NewsModel[] newsModelArray = new Gson().fromJson(jsonString, NewsModel[].class);
                        newsModelList = Arrays.asList(newsModelArray);

                        initNewsFrag();
                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        //hide progressbar
                        ToastManager.getInstance().showSimpleToastShort("Request failed. Please try again later");
                    }
                }
        );
    }

    private void initNewsFrag() {
        //inflate first fragment when app is launched
        feedsFragment = new FeedsFragment();

        //send json string to fragment
        feedsFragment.setJsonString(jsonString);

        //change fragment
        changeFragment(feedsFragment, "feedsFragment", null);

        //hide progressbar
        findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    private void changeFragment(Fragment fragment, String tag, String backStackName) {
        Logger.putInDebugLog(TAG, "inside", "changeFragment");
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.replace(R.id.contentContainer, fragment, tag);
        fragmentTransaction.addToBackStack(backStackName);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        backPressCount++;
        if (backPressCount == 1) {
            ToastManager.getInstance().showSimpleToastShort("Press again to Exit");
        } else if (backPressCount == 2) {
            //finish activity
            HomeScreenActivity.this.finish();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backPressCount = 0;
            }
        }, EXIT_TIME_OUT);
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
