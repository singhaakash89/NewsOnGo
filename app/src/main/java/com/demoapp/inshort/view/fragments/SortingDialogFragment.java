package com.demoapp.inshort.view.fragments;


import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.demoapp.inshort.R;
import com.demoapp.inshort.constants.Constants;

import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment;

/**
 * Use SupportBlurDialogFragment to give background blur effect
 */
public class SortingDialogFragment extends SupportBlurDialogFragment implements Constants {

    private static final String TITLE = "title";
    private static final String MESSAGE = "message";
    public static int selectedViewId;
    private static SortingDialogFragment fragment;
    private static CallBackListener callBackListener;
    private Typeface tf_300;
    private Typeface tf_900;
    private Typeface tf_500;

    public SortingDialogFragment() {
        // Required empty public constructor
    }

    public static SortingDialogFragment newInstance(CallBackListener callBackListener) {
        if (fragment != null) {
            fragment.dismiss();
        } else {
            SortingDialogFragment.callBackListener = callBackListener;
            fragment = new SortingDialogFragment();
        }
        return fragment;
    }

    public static void resetSelectedId() {
        selectedViewId = 0;
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
        final View view = inflater.inflate(R.layout.dialog_shorting, container, false);
        final TextView headerTV = (TextView) view.findViewById(R.id.headerTV);
        final TextView sort_1 = (TextView) view.findViewById(R.id.sort_1);
        final TextView sort_2 = (TextView) view.findViewById(R.id.sort_2);
        sort_1.setTypeface(tf_500);
        sort_2.setTypeface(tf_500);
        headerTV.setTypeface(tf_500);

        //update earlier selection
        if (selectedViewId == R.id.sort_1) {
            sort_1.setBackground(getActivity().getResources().getDrawable(R.drawable.layout_background_green_solid));
            sort_1.setTextColor(getActivity().getResources().getColor(R.color.white));
            sort_2.setBackground(getActivity().getResources().getDrawable(R.drawable.layout_background_saffron_border_v1));
            sort_2.setTextColor(getActivity().getResources().getColor(R.color.black));
        } else if (selectedViewId == R.id.sort_2) {
            sort_2.setBackground(getActivity().getResources().getDrawable(R.drawable.layout_background_green_solid));
            sort_2.setTextColor(getActivity().getResources().getColor(R.color.white));
            sort_1.setBackground(getActivity().getResources().getDrawable(R.drawable.layout_background_saffron_border_v1));
            sort_1.setTextColor(getActivity().getResources().getColor(R.color.black));
        } else {
            sort_2.setBackground(getActivity().getResources().getDrawable(R.drawable.layout_background_saffron_border_v1));
            sort_1.setBackground(getActivity().getResources().getDrawable(R.drawable.layout_background_saffron_border_v1));
            sort_1.setTextColor(getActivity().getResources().getColor(R.color.black));
            sort_2.setTextColor(getActivity().getResources().getColor(R.color.black));
        }

        //click listener
        sort_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedViewId = R.id.sort_1;
                sort_1.setTextColor(getActivity().getResources().getColor(R.color.white));
                onSelectionChangedToggleForSorting(sort_1, sort_2, OLD_TO_NEW);
                callBackListener.oldToNewSorting();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                }, 200);
            }
        });

        sort_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedViewId = R.id.sort_2;
                sort_2.setTextColor(getActivity().getResources().getColor(R.color.white));
                onSelectionChangedToggleForSorting(sort_2, sort_1, NEW_TO_OLD);
                callBackListener.newToOld();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                }, 200);
            }
        });
        return view;
    }

    public void onSelectionChangedToggleForSorting(TextView textView1, TextView textview2, String sortName) {
        if (textView1.getBackground().getConstantState().equals(getActivity().getResources().getDrawable(R.drawable.layout_background_saffron_border_v1).getConstantState())) {
            textView1.setBackground(getActivity().getResources().getDrawable(R.drawable.layout_background_green_solid));
            textview2.setTextColor(getActivity().getResources().getColor(R.color.black));
            textview2.setBackground(getActivity().getResources().getDrawable(R.drawable.layout_background_saffron_border_v1));
        } else if (textView1.getBackground().getConstantState() == getActivity().getResources().getDrawable(R.drawable.layout_background_green_solid).getConstantState()) {
            textView1.setBackground(getActivity().getResources().getDrawable(R.drawable.layout_background_saffron_border_v1));
            textView1.setTextColor(getActivity().getResources().getColor(R.color.black));
        }
    }

    public interface CallBackListener {
        void oldToNewSorting();

        void newToOld();
    }
}
