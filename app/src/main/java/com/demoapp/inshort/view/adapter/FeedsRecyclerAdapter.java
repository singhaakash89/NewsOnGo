package com.demoapp.inshort.view.adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demoapp.inshort.Logger.Logger;
import com.demoapp.inshort.R;
import com.demoapp.inshort.constants.Constants;
import com.demoapp.inshort.constants.ServiceConstants;
import com.demoapp.inshort.datastore.StandardStorageHelper;
import com.demoapp.inshort.datastore.accessor.NewsAccessor;
import com.demoapp.inshort.datastore.model.NewsModel;
import com.demoapp.inshort.datastore.schema.NewsSchemaBuilder;
import com.demoapp.inshort.view.activities.WebviewActivity;
import com.demoapp.inshort.view.toast.ToastManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by aakashsingh on 09/06/17.
 */

public class FeedsRecyclerAdapter extends RecyclerView.Adapter<FeedsRecyclerAdapter.ViewHolder> implements Constants, ServiceConstants {

    private static final String TAG = FeedsRecyclerAdapter.class.getSimpleName();
    private static List<NewsModel> newsModelListFav;
    private Context mContext;
    private List<NewsModel> newsModelList, newsModelListTemp;
    private Typeface tf_500;
    private Typeface tf_700;
    private RecyclerViewCallBacklistener recyclerViewCallBacklistener;
    private Intent intent;

    public FeedsRecyclerAdapter(RecyclerViewCallBacklistener recyclerViewCallBacklistener, List<NewsModel> newsModelList) {
        this.newsModelList = newsModelList;
        newsModelListFav = new ArrayList<>();
        this.recyclerViewCallBacklistener = recyclerViewCallBacklistener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        loadTypeface();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_news_v1, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.newsHeadlinetv.setText(newsModelList.get(position).getTITLE());
            holder.publisherTV.setText(newsModelList.get(position).getPUBLISHER());
            holder.timeStampTV.setText(String.valueOf(convertTimeWithTimeZome(newsModelList.get(position).getTIMESTAMP())));
            isRecyclerViewReady();
            updateFavItems(holder, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateFavItems(ViewHolder holder, int pos) {
        try {
            Cursor cursor = StandardStorageHelper.getInstance().queryFromDB(NewsSchemaBuilder.TABLE_NAME, NewsAccessor.getTableProjection());
            List<NewsModel> newsModelList = getNewsModelList(cursor);
            for (NewsModel newsModel : newsModelList) {
                if (newsModel.getID() == this.newsModelList.get(pos).getID()) {
                    holder.favIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_fav_1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<NewsModel> getNewsModelList(Cursor cursor) {
        Logger.putInDebugLog(TAG, "Inside", "getNewsModelList");
        newsModelListTemp = new ArrayList<>();
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
        Logger.putInDebugLog(TAG, "id : " + id, ";");
        newsModel.setID(id);
        return newsModel;
    }


    private void isRecyclerViewReady() {
        recyclerViewCallBacklistener.isRecyclerViewUpdated();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return newsModelList.size();
    }

    private void loadTypeface() {
        // Loading Font Face
        tf_500 = Typeface.createFromAsset(mContext.getAssets(), FONTPATH_MUSEOSANS_500);
        tf_700 = Typeface.createFromAsset(mContext.getAssets(), FONTPATH_MUSEOSANS_700);
    }

    public String convertTimeWithTimeZome(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(time);
        String curTime = String.format("%02d:%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
        return curTime;
    }

    public interface RecyclerViewCallBacklistener {
        void isRecyclerViewUpdated();

        void insertIntoDB(NewsModel newsModel);

        void deleteFromDB(NewsModel newsModel);

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView newsHeadlinetv;
        private TextView publisherTV;
        private TextView timeStampTV;
        private ImageView favIV;
        private TextView tapTV;
        private RelativeLayout containerLayout;

        public ViewHolder(View view) {
            super(view);

            newsHeadlinetv = (TextView) view.findViewById(R.id.newsHeadlineTV);
            publisherTV = (TextView) view.findViewById(R.id.publisherTV);
            timeStampTV = (TextView) view.findViewById(R.id.timstampTV);
            tapTV = (TextView) view.findViewById(R.id.tapTV);
            favIV = (ImageView) view.findViewById(R.id.favIV);
            containerLayout = (RelativeLayout) view.findViewById(R.id.containerLayout);

            //set tyeface
            newsHeadlinetv.setTypeface(tf_700);
            publisherTV.setTypeface(tf_500);
            timeStampTV.setTypeface(tf_500);
            tapTV.setTypeface(tf_500);

            favIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (favIV.getDrawable().getConstantState() == mContext.getResources().getDrawable(R.drawable.ic_fav_2).getConstantState()) {
                        favIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_fav_1));
                        recyclerViewCallBacklistener.insertIntoDB(newsModelList.get(getAdapterPosition()));
                        ToastManager.getInstance().showSimpleToastShort("Added to favourites");
                    } else {
                        favIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_fav_2));
                        recyclerViewCallBacklistener.deleteFromDB(newsModelList.get(getAdapterPosition()));
                        ToastManager.getInstance().showSimpleToastShort("Removed from favourites");
                    }
                }
            });

            //set onClick Listener
            containerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openWebViewActivity(newsModelList.get(getAdapterPosition()).getURL(), newsModelList.get(getAdapterPosition()).getTITLE(), newsModelList.get(getAdapterPosition()).getHOSTNAME());
                }
            });
        }

        private void openWebViewActivity(String newsUrl, String appBarTitle, String appBarTitleDesc) {
            intent = new Intent(mContext, WebviewActivity.class);
            Bundle animation = ActivityOptions.makeCustomAnimation(mContext, R.anim.new_activity_slide_left_to_right, R.anim.old_activity_slide_left_to_right).toBundle();
            intent.putExtra(NEWS_URL, newsUrl);
            intent.putExtra(APP_BAR_TITLE, appBarTitle);
            intent.putExtra(APP_BAR_TITLE_DESC, appBarTitleDesc);
            mContext.startActivity(intent, animation);
        }
    }
}
