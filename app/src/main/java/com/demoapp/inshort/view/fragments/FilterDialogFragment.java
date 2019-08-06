package com.demoapp.inshort.view.fragments;


import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.demoapp.inshort.R;
import com.demoapp.inshort.constants.Constants;

import java.util.ArrayList;
import java.util.List;

import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment;

/**
 * Use SupportBlurDialogFragment to give background blur effect
 */
public class FilterDialogFragment extends SupportBlurDialogFragment implements Constants, View.OnClickListener {

    private static final String TITLE = "title";
    private static final String MESSAGE = "message";
    public static int selectedViewId;
    private static FilterDialogFragment fragment;
    private static CallBackListener callBackListener;
    private static List<String> filterList;
    private Typeface tf_300;
    private Typeface tf_900;
    private Typeface tf_500;
    private boolean isFilterEnabled;
    private TextView filter_1;
    private TextView filter_2;
    private TextView filter_3;
    private TextView filter_4;
    private TextView applyTV;
    private TextView headerTV;

    public FilterDialogFragment() {
        // Required empty public constructor
    }

    public static FilterDialogFragment newInstance(CallBackListener callBackListener, List<String> filterList) {
        if (fragment != null) {
            fragment.dismiss();
        } else {
            FilterDialogFragment.filterList = filterList;
            FilterDialogFragment.callBackListener = callBackListener;
            fragment = new FilterDialogFragment();
        }
        return fragment;
    }

    public static void resetFilterList() {
        filterList = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Loading Font Face
        tf_300 = Typeface.createFromAsset(getActivity().getAssets(), FONTPATH_MUSEOSANS_300);
        tf_500 = Typeface.createFromAsset(getActivity().getAssets(), FONTPATH_MUSEOSANS_500);
        tf_900 = Typeface.createFromAsset(getActivity().getAssets(), FONTPATH_MUSEOSANS_900);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.InfoAppDialogTheme);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            //dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_filter, container, false);
        headerTV = (TextView) view.findViewById(R.id.headerTV);
        applyTV = (TextView) view.findViewById(R.id.applyTV);
        filter_1 = (TextView) view.findViewById(R.id.filter_1);
        filter_2 = (TextView) view.findViewById(R.id.filter_2);
        filter_3 = (TextView) view.findViewById(R.id.filter_3);
        filter_4 = (TextView) view.findViewById(R.id.filter_4);
        filter_1.setBackground(getActivity().getResources().getDrawable(R.drawable.layout_background_saffron_border_v1));
        filter_2.setBackground(getActivity().getResources().getDrawable(R.drawable.layout_background_saffron_border_v1));
        filter_3.setBackground(getActivity().getResources().getDrawable(R.drawable.layout_background_saffron_border_v1));
        filter_4.setBackground(getActivity().getResources().getDrawable(R.drawable.layout_background_saffron_border_v1));
        filter_1.setTypeface(tf_500);
        filter_2.setTypeface(tf_500);
        filter_3.setTypeface(tf_500);
        filter_4.setTypeface(tf_500);
        headerTV.setTypeface(tf_500);
        applyTV.setTypeface(tf_500);

        //select view on the basis of list filters
        if (filterList.size() > 0) {
            for (String s : filterList) {
                if (s.equalsIgnoreCase(BUSINESS)) {
                    filter_1.setBackground(getActivity().getResources().getDrawable(R.drawable.layout_background_green_solid));
                    filter_1.setTextColor(getActivity().getResources().getColor(R.color.white));
                } else if (s.equalsIgnoreCase(HEALTH)) {
                    filter_2.setBackground(getActivity().getResources().getDrawable(R.drawable.layout_background_green_solid));
                    filter_2.setTextColor(getActivity().getResources().getColor(R.color.white));
                } else if (s.equalsIgnoreCase(ENTERTAINMENT)) {
                    filter_3.setBackground(getActivity().getResources().getDrawable(R.drawable.layout_background_green_solid));
                    filter_3.setTextColor(getActivity().getResources().getColor(R.color.white));
                } else if (s.equalsIgnoreCase(SCIENCE)) {
                    filter_4.setBackground(getActivity().getResources().getDrawable(R.drawable.layout_background_green_solid));
                    filter_4.setTextColor(getActivity().getResources().getColor(R.color.white));
                }
            }
        }else {
            filter_1.setBackground(getActivity().getResources().getDrawable(R.drawable.layout_background_saffron_border_v1));
            filter_1.setTextColor(getActivity().getResources().getColor(R.color.black));

            filter_2.setBackground(getActivity().getResources().getDrawable(R.drawable.layout_background_saffron_border_v1));
            filter_2.setTextColor(getActivity().getResources().getColor(R.color.black));

            filter_3.setBackground(getActivity().getResources().getDrawable(R.drawable.layout_background_saffron_border_v1));
            filter_3.setTextColor(getActivity().getResources().getColor(R.color.black));

            filter_4.setBackground(getActivity().getResources().getDrawable(R.drawable.layout_background_saffron_border_v1));
            filter_4.setTextColor(getActivity().getResources().getColor(R.color.black));
        }

        //listener
        filter_1.setOnClickListener(this);
        filter_2.setOnClickListener(this);
        filter_3.setOnClickListener(this);
        filter_4.setOnClickListener(this);
        applyTV.setOnClickListener(this);

        return view;
    }

    public void onSelectionChangedToggleForFilter(TextView textView, int pos, String filterName) {
        if (textView.getBackground().getConstantState().equals(getActivity().getResources().getDrawable(R.drawable.layout_background_saffron_border_v1).getConstantState())) {
            textView.setBackground(getActivity().getResources().getDrawable(R.drawable.layout_background_green_solid));
            textView.setTextColor(getActivity().getResources().getColor(R.color.white));
            filterList.add(filterName);
        } else if (textView.getBackground().getConstantState() == getActivity().getResources().getDrawable(R.drawable.layout_background_green_solid).getConstantState()) {
            textView.setBackground(getActivity().getResources().getDrawable(R.drawable.layout_background_saffron_border_v1));
            textView.setTextColor(getActivity().getResources().getColor(R.color.black));
            filterList.remove(filterName);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.filter_1) {
            isFilterEnabled = true;
            onSelectionChangedToggleForFilter(filter_1, 0, BUSINESS);
            debugLog();
            callBackListener.enableFilter(filterList, isFilterEnabled);
        } else if (id == R.id.filter_2) {
            isFilterEnabled = true;
            onSelectionChangedToggleForFilter(filter_2, 1, HEALTH);
            debugLog();
            callBackListener.enableFilter(filterList, isFilterEnabled);
        } else if (id == R.id.filter_3) {
            isFilterEnabled = true;
            onSelectionChangedToggleForFilter(filter_3, 2, ENTERTAINMENT);
            debugLog();
            callBackListener.enableFilter(filterList, isFilterEnabled);
        } else if (id == R.id.filter_4) {
            isFilterEnabled = true;
            onSelectionChangedToggleForFilter(filter_4, 3, SCIENCE);
            debugLog();
            callBackListener.enableFilter(filterList, isFilterEnabled);
        } else if (id == R.id.applyTV) {
            dismiss();
        }
    }

    private void debugLog() {
        System.out.print("filterList.size : " + filterList.size() + ";");
        for (String s : filterList) {
            System.out.print("filterList.element : " + s + ";");
        }
    }

    public interface CallBackListener {
        void enableFilter(List<String> filterList, boolean isFilterEnabled);
    }
}
