package com.demoapp.inshort.view.fragments;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.demoapp.inshort.Logger.Logger;
import com.demoapp.inshort.R;
import com.demoapp.inshort.constants.Constants;
import com.demoapp.inshort.datastore.StandardStorageHelper;
import com.demoapp.inshort.datastore.accessor.NewsAccessor;
import com.demoapp.inshort.datastore.model.NewsModel;
import com.demoapp.inshort.datastore.schema.NewsSchemaBuilder;
import com.demoapp.inshort.view.adapter.FavouriteRecyclerAdapter;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by aakashsingh on 12/03/17.
 */

public class FavouriteFragment extends Fragment implements Constants, FavouriteRecyclerAdapter.RecyclerViewCallBacklistener {

    private static final String TAG = FavouriteFragment.class.getSimpleName();
    private Context context;
    private Typeface tf_300;
    private Typeface tf_700;
    private Typeface tf_500;
    private Typeface tf_900;
    private TextView headrTV, noDataTV, countTV;
    private RecyclerViewPager recyclerViewPager;
    private ProgressBar progressBar;
    private View view;
    private List<NewsModel> newsModelList;
    private FavouriteRecyclerAdapter favouriteRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;

    public FavouriteFragment() {

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
        view = layoutInflater.inflate(R.layout.fragment_favourites, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //init UI
        initUI();

        //set font
        setTypeFace();

        //init list from DB
        initFavList();

        //init recycler view
        initNewsRecyclerView();
    }

    private void initFavList() {
        try {
            Cursor cursor = StandardStorageHelper.getInstance().queryFromDB(NewsSchemaBuilder.TABLE_NAME, NewsAccessor.getTableProjection());
            newsModelList = getNewsModelList(cursor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<NewsModel> getNewsModelList(Cursor cursor) {
        Logger.putInDebugLog(TAG, "Inside", "getNewsModelList");
        List<NewsModel> newsModelListTemp = new ArrayList<>();
        Logger.putInDebugLog(TAG, "cursor.getCount() : ", "" + cursor.getCount() + " -null");
        if (cursor != null && cursor.getCount() > 0) {
            Logger.putInDebugLog(TAG, "Inside", "if - cursor");
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Logger.putInDebugLog(TAG, "Inside", "while - cursor");
                NewsModel newsModel = createNewsFromCursor(cursor);
                newsModelListTemp.add(newsModel);
                cursor.moveToNext();
            }
            cursor.close();
        }
        Logger.putInDebugLog(TAG, "newsModelListTemp.size()", "" + newsModelListTemp.size() + " value");
        return newsModelListTemp;
    }


    public NewsModel createNewsFromCursor(Cursor cursor) {
        Logger.putInDebugLog(TAG, "Inside", "createUserFromCursor");
        NewsModel newsModel = new NewsModel();
        int id = cursor.getInt(cursor.getColumnIndex(NewsAccessor.ID));
        String title = cursor.getString(cursor.getColumnIndex(NewsAccessor.TITLE));
        String url = cursor.getString(cursor.getColumnIndex(NewsAccessor.URL));
        String publisher = cursor.getString(cursor.getColumnIndex(NewsAccessor.PUBLISHER));
        String category = cursor.getString(cursor.getColumnIndex(NewsAccessor.CATEGORY));
        String hostname = cursor.getString(cursor.getColumnIndex(NewsAccessor.HOSTNAME));
        long timestamp = cursor.getLong(cursor.getColumnIndex(NewsAccessor.TIMESTAMP));
        Logger.putInDebugLog(TAG, "id : " + id, ";");
        Logger.putInDebugLog(TAG, "title : " + title, ";");
        Logger.putInDebugLog(TAG, "url : " + url, ";");
        Logger.putInDebugLog(TAG, "publisher : " + publisher, ";");
        Logger.putInDebugLog(TAG, "category : " + category, ";");
        Logger.putInDebugLog(TAG, "hostname : " + hostname, ";");
        Logger.putInDebugLog(TAG, "timestamp : " + timestamp, ";");
        newsModel.setID(id);
        newsModel.setTITLE(title);
        newsModel.setURL(url);
        newsModel.setPUBLISHER(publisher);
        newsModel.setCATEGORY(category);
        newsModel.setHOSTNAME(hostname);
        newsModel.setTIMESTAMP(timestamp);
        return newsModel;
    }

    private void initUI() {
        newsModelList = new ArrayList<>();
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        headrTV = (TextView) view.findViewById(R.id.headerTV);
        noDataTV = (TextView) view.findViewById(R.id.noDataTV);
        countTV = (TextView) view.findViewById(R.id.countTV);
        recyclerViewPager = (RecyclerViewPager) view.findViewById(R.id.recyclerView);
    }

    private void setTypeFace() {
        // Loading Font Face
        tf_300 = Typeface.createFromAsset(getActivity().getAssets(), FONTPATH_MUSEOSANS_300);
        tf_700 = Typeface.createFromAsset(getActivity().getAssets(), FONTPATH_MUSEOSANS_700);
        tf_500 = Typeface.createFromAsset(getActivity().getAssets(), FONTPATH_MUSEOSANS_500);
        tf_900 = Typeface.createFromAsset(getActivity().getAssets(), FONTPATH_MUSEOSANS_900);
        headrTV.setTypeface(tf_500);
        noDataTV.setTypeface(tf_500);
        countTV.setTypeface(tf_500);
    }

    private void initNewsRecyclerView() {
        if (newsModelList.size() > 0) {
            favouriteRecyclerAdapter = new FavouriteRecyclerAdapter(this, newsModelList);
            linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerViewPager.setLayoutManager(linearLayoutManager);
            recyclerViewPager.setAdapter(favouriteRecyclerAdapter);
            favouriteRecyclerAdapter.notifyDataSetChanged();
            updateNoDataText(newsModelList.size(), true);
            updateArticleCount(newsModelList.size());
        } else {
            updateNoDataText(newsModelList.size(), false);
            progressBar.setVisibility(View.GONE);
            updateArticleCount(newsModelList.size());
        }
    }

    private void updateArticleCount(int size) {
        if (size > 0) {
            countTV.setText("(" + size + ")");
        } else {
            countTV.setText("(0)");
        }
    }

    @Override
    public void isRecyclerViewUpdated() {
        progressBar.setVisibility(View.GONE);
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

    @Override
    public void updateNoDataText(int size, boolean flag) {
        if (flag) {
            noDataTV.setVisibility(View.GONE);
            updateArticleCount(size);
        } else {
            noDataTV.setVisibility(View.VISIBLE);
            updateArticleCount(size);
        }
    }
}