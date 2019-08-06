package com.demoapp.inshort.view.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.demoapp.inshort.R;
import com.demoapp.inshort.constants.Constants;
import com.demoapp.inshort.constants.ServiceConstants;
import com.demoapp.inshort.datastore.StandardStorageHelper;
import com.demoapp.inshort.datastore.model.NewsModel;
import com.demoapp.inshort.datastore.schema.NewsSchemaBuilder;
import com.demoapp.inshort.view.adapter.FeedsRecyclerAdapter;
import com.demoapp.inshort.view.toast.ToastManager;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * Created by aakashsingh on 12/03/17.
 */

public class FeedsFragment extends Fragment implements Constants, ServiceConstants, FeedsRecyclerAdapter.RecyclerViewCallBacklistener, View.OnClickListener, SortingDialogFragment.CallBackListener, FilterDialogFragment.CallBackListener {

    private static final String TAG = FeedsFragment.class.getSimpleName();
    private String SORTING_TYPE;
    private boolean isFilterEnabled = false;
    private List<String> filterList;
    private Context mContext;
    private TextView sortTV, filterTV;
    private FeedsRecyclerAdapter feedsRecyclerAdapter;
    private RecyclerViewPager recyclerViewPager;
    private LinearLayoutManager linearLayoutManager;
    private List<NewsModel> newsModelList, newsModelListFinal, filteredNewsModelList;
    private AsyncHttpClient client;
    private String jsonString;
    private View view;
    private ProgressBar progressBar;
    private boolean userScrolled = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount, counter;
    private List<List<NewsModel>> newsModelListMutable_Partition, newsModelListMutable_PartitionOldToNew, newsModelListMutable_PartitionNewToOld, newsModelListMutable_PartitionWithFilter;
    private Typeface tf_300;
    private Typeface tf_700;
    private Typeface tf_500;
    private Typeface tf_900;
    private RelativeLayout sortAndFilterLayout;
    private int h1;
    private int mid;
    private int oldScrollY;
    private int cureentScrollY;
    private Dialog dialog;

    public FeedsFragment() {

    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //[V.IMPORTANT  : BELOW IS IMP FOR UPDATING SECTOR NAME AND OTHER ATTRIBUTES FROM OTHER ACTIVITY]
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        view = layoutInflater.inflate(R.layout.fragment_feeds, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //init UI
        initUI();

        //set font
        setTypeFace();

        //reset sort selection if any
        SortingDialogFragment.resetSelectedId();
        FilterDialogFragment.resetFilterList();

        //init FAB
        //initFAB();

        //post to Inshort API for json
        //postToYoutubeApiForViewCount(INSHORT_OUTLINK_API_URL);

        //init List Data
        initAllListData(jsonString);

        //set onclick listener
        setOnClickListener();

    }

    private void setTypeFace() {
        // Loading Font Face
        tf_300 = Typeface.createFromAsset(mContext.getAssets(), FONTPATH_MUSEOSANS_300);
        tf_700 = Typeface.createFromAsset(mContext.getAssets(), FONTPATH_MUSEOSANS_700);
        tf_500 = Typeface.createFromAsset(mContext.getAssets(), FONTPATH_MUSEOSANS_500);
        tf_900 = Typeface.createFromAsset(mContext.getAssets(), FONTPATH_MUSEOSANS_900);

        sortTV.setTypeface(tf_500);
        filterTV.setTypeface(tf_500);
    }

    private void setOnClickListener() {
        sortTV.setOnClickListener(this);
        filterTV.setOnClickListener(this);
    }

    private void initUI() {
        dialog = new Dialog(mContext);
        filterList = new ArrayList<>();
        sortTV = (TextView) view.findViewById(R.id.sortTV);
        filterTV = (TextView) view.findViewById(R.id.filterTV);
        recyclerViewPager = (RecyclerViewPager) view.findViewById(R.id.recyclerView);
        sortAndFilterLayout = (RelativeLayout) view.findViewById(R.id.sortAndFilterLayout);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        newsModelListMutable_Partition = new ArrayList<>();
        newsModelListMutable_PartitionOldToNew = new ArrayList<>();
        newsModelListMutable_PartitionNewToOld = new ArrayList<>();
        newsModelListFinal = new ArrayList<>();
        newsModelList = new ArrayList<>();
        newsModelListMutable_PartitionWithFilter = new ArrayList<>();
        filteredNewsModelList = new ArrayList<>();
    }

    private void initNewsRecyclerView() {
        SORTING_TYPE = NO_SORTING;
        feedsRecyclerAdapter = new FeedsRecyclerAdapter(this, newsModelListFinal);
        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerViewPager.setLayoutManager(linearLayoutManager);
        recyclerViewPager.setAdapter(feedsRecyclerAdapter);
        feedsRecyclerAdapter.notifyDataSetChanged();
    }

    private void postToYoutubeApiForViewCount(String url) {
        client = new AsyncHttpClient();
        //System.out.println("postToYoutubeApiForViewCount : " + url + ";");
        client.get(url, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String res) {
                        // called when response HTTP status is "200 OK"
                        //System.out.println("AsyncHttpClient_Response : " + res + ";");
                        jsonString = res;

                        NewsModel[] newsModelArray = new Gson().fromJson(jsonString, NewsModel[].class);
                        newsModelList = Arrays.asList(newsModelArray);

                        //part list
                        partList();

                        //init News RecyclerView
                        initNewsRecyclerView();

                        /**
                         * setting Resetting oldScrollY
                         * oldScrollY = 0
                         */
                        oldScrollY = 0;

                        //set recycler view scroll listener
                        addListener();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        //hide progressbar
                        ToastManager.getInstance().showSimpleToastShort("Request failed. Please try again later");
                    }
                }
        );
    }

    private void initAllListData(String jsonString) {
        NewsModel[] newsModelArray = new Gson().fromJson(jsonString, NewsModel[].class);
        newsModelList = Arrays.asList(newsModelArray);

        //part list
        partList();

        //init News RecyclerView
        initNewsRecyclerView();

        /**
         * setting Resetting oldScrollY
         * oldScrollY = 0
         */
        oldScrollY = 0;

        //set recycler view scroll listener
        addListener();
    }

    private void partList() {
        newsModelListMutable_Partition = Lists.partition(newsModelList, 7);
        List<NewsModel> newsModels = newsModelListMutable_Partition.get(0);
        newsModelListFinal.addAll(newsModels);
        counter = 1;
    }

    private void partListOldToNew() {
        newsModelListMutable_PartitionOldToNew = Lists.partition(newsModelList, 7);
        List<NewsModel> newsModels = newsModelListMutable_PartitionOldToNew.get(0);
        newsModelListFinal.clear();
        newsModelListFinal.addAll(newsModels);
        counter = 1;
    }

    private void partListNewToOld() {
        newsModelListMutable_PartitionNewToOld = Lists.partition(newsModelList, 7);
        List<NewsModel> newsModels = newsModelListMutable_PartitionNewToOld.get(0);
        newsModelListFinal.clear();
        newsModelListFinal.addAll(newsModels);
        counter = 1;
    }

    private void partListWithFilter() {
        try {
            if (filteredNewsModelList.size() > 0) {
                newsModelListMutable_PartitionWithFilter = new ArrayList<>();
                newsModelListMutable_PartitionWithFilter = Lists.partition(filteredNewsModelList, 7);
                List<NewsModel> newsModels = newsModelListMutable_PartitionWithFilter.get(0);
                newsModelListFinal.clear();
                newsModelListFinal.addAll(newsModels);
                counter = 1;
            } else {
                isFilterEnabled = false;
                newsModelListMutable_Partition = Lists.partition(newsModelList, 7);
                List<NewsModel> newsModels = newsModelListMutable_Partition.get(0);
                newsModelListFinal.addAll(newsModels);
                counter = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void addListener() {
        recyclerViewPager.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // If scroll state is touch scroll then set userScrolled
                // true
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //measuring height of recycler view
                cureentScrollY = dy;

                System.out.println("oldScrollY :" + oldScrollY);
                System.out.println("cureentScrollY :" + cureentScrollY);
                System.out.println("====================================");

                if (oldScrollY < cureentScrollY && sortAndFilterLayout.getVisibility() == View.GONE) {
                    sortAndFilterLayout.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.SlideInUp).duration(300).repeat(1).pivot(100, 100).playOn(sortAndFilterLayout);
                } else if (oldScrollY > cureentScrollY && sortAndFilterLayout.getVisibility() == View.VISIBLE) {
                    sortAndFilterLayout.setVisibility(View.GONE);
                }

                // Here get the child count, item count and visibleitems
                // from layout manager
                visibleItemCount = linearLayoutManager.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                // Now check if userScrolled is true and also check if
                // the item is end then update recycler view and set
                // userScrolled to false
                if (userScrolled && (visibleItemCount + pastVisiblesItems) == totalItemCount) {
                    userScrolled = false;
                    updateRecyclerView();
                }

                //changing old scroll value
                oldScrollY = cureentScrollY;
            }
        });
    }

    private void updateRecyclerView() {
        // Show Progress Layout
        progressBar.setVisibility(View.VISIBLE);
        // Handler to show refresh for a period of time you can use async task
        // while commnunicating server
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchNewsItems();
                feedsRecyclerAdapter.notifyDataSetChanged();
                ToastManager.getInstance().showSimpleToastShort("News updated");
                progressBar.setVisibility(View.GONE);
            }
        }, 1000);
    }

    private void fetchNewsItems() {
        if (SORTING_TYPE.equalsIgnoreCase(NO_SORTING) || !isFilterEnabled) {
            if (counter < newsModelListMutable_Partition.size()) {
                List<NewsModel> list = newsModelListMutable_Partition.get(counter);
                newsModelListFinal.addAll(list);
                counter++;
            }
        } else if (SORTING_TYPE.equalsIgnoreCase(OLD_TO_NEW)) {
            if (counter < newsModelListMutable_PartitionOldToNew.size()) {
                List<NewsModel> list = newsModelListMutable_PartitionOldToNew.get(counter);
                newsModelListFinal.addAll(list);
                counter++;
            }
        } else if (SORTING_TYPE.equalsIgnoreCase(NEW_TO_OLD)) {
            if (counter < newsModelListMutable_PartitionNewToOld.size()) {
                List<NewsModel> list = newsModelListMutable_PartitionNewToOld.get(counter);
                newsModelListFinal.addAll(list);
                counter++;
            }
        } else if (isFilterEnabled) {
            if (counter < newsModelListMutable_PartitionWithFilter.size()) {
                List<NewsModel> list = newsModelListMutable_PartitionWithFilter.get(counter);
                newsModelListFinal.addAll(list);
                counter++;
            }
        }
    }

    @Override
    public void isRecyclerViewUpdated() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.sortTV) {
            SortingDialogFragment.newInstance(this).show(getActivity().getSupportFragmentManager(), null);
        } else if (id == R.id.filterTV) {
            FilterDialogFragment.newInstance(this, filterList).show(getActivity().getSupportFragmentManager(), null);
        }
    }

    @Override
    public void oldToNewSorting() {
        //sorting type selection
        SORTING_TYPE = OLD_TO_NEW;

        //progressbar visible
        progressBar.setVisibility(View.VISIBLE);

        //Reverse arraylist entries
        Collections.sort(newsModelList);

        //part list again
        partListOldToNew();

        initNewsRecyclerView();

    }

    @Override
    public void newToOld() {
        SORTING_TYPE = NEW_TO_OLD;

        //progressbar visible
        progressBar.setVisibility(View.VISIBLE);

        //Reverse arraylist entries
        Collections.reverse(newsModelList);

        //part list again
        partListNewToOld();

        initNewsRecyclerView();
    }

    @Override
    public void enableFilter(List<String> filterList, boolean isFilterEnabled) {
        this.isFilterEnabled = isFilterEnabled;
        filteredNewsModelList = new ArrayList<>();
        for (NewsModel model : newsModelList) {
            for (int i = 0; i < filterList.size(); i++) {
                try {
                    if (filterList.get(i).toString().trim().equalsIgnoreCase(model.getCATEGORY().trim())) {
                        filteredNewsModelList.add(model);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.print("filteredNewsModelList.size : " + filteredNewsModelList.size() + ";");

        partListWithFilter();

        initNewsRecyclerView();
    }

    @Override
    public void insertIntoDB(NewsModel newsModel) {
        try {
            StandardStorageHelper.getInstance().insertToDB(NewsSchemaBuilder.TABLE_NAME, newsModel.getContentValues());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteFromDB(NewsModel newsModel) {
        try {
            StandardStorageHelper.getInstance().deleteFromDB(NewsSchemaBuilder.TABLE_NAME, newsModel.getID());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}