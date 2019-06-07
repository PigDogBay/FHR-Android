package com.pigdogbay.foodhygieneratings;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pigdogbay.lib.utils.ActivityUtils;


public class AboutFragment extends Fragment {

    public static final String TAG = "about";

    public AboutFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        rootView.findViewById(R.id.aboutBtnRate).setOnClickListener(v -> showWebPage(getActivity(),R.string.market_app_url));
        rootView.findViewById(R.id.aboutBtnSendFeedback).setOnClickListener(v -> sendFeedback(getActivity()));
        rootView.findViewById(R.id.aboutBtnTellFriends).setOnClickListener(v -> tellFriends(getActivity()));
        rootView.findViewById(R.id.aboutBtnLegal).setOnClickListener(v -> showLegalNotices());
        rootView.findViewById(R.id.aboutBtnReleaseNotes).setOnClickListener(v -> showWebPage(getActivity(),R.string.release_notes_url));
        rootView.findViewById(R.id.aboutBtnMoreApps).setOnClickListener(v -> showWebPage(getActivity(),R.string.market_pigdogbay_apps));
        rootView.findViewById(R.id.aboutBtnGooglePolicy).setOnClickListener(v -> showWebPage(getActivity(),R.string.google_data_policy));
        rootView.findViewById(R.id.aboutBtnPrivatePolicy).setOnClickListener(v -> showWebPage(getActivity(),R.string.privacy_policy_url));
        rootView.findViewById(R.id.aboutBtnEuConsent).setOnClickListener(v -> ((MainActivity)getActivity()).showEuConsentForm());

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("About");
    }
    public static void showWebPage(Activity activity, int urlId)
    {
        try
        {
            ActivityUtils.ShowWebPage(activity, activity.getString(urlId));
        }
        catch (ActivityNotFoundException e)
        {
            Toast.makeText(activity, activity.getString(R.string.web_error), Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void showLegalNotices(){
        ActivityUtils.showInfoDialog(getActivity(), R.string.legal_title, R.string.legal,R.string.ok);
    }

    public static void sendFeedback(Activity activity)
    {
        ActivityUtils.SendEmail(
                activity,
                new String[]{activity.getString(R.string.email)},
                activity.getString(R.string.about_button_feedback_subject),
                activity.getString(R.string.about_button_feedback_body));
    }
    public static void tellFriends(Activity activity){
        ActivityUtils.SendEmail(
                activity,
                new String[]{""},
                activity.getString(R.string.about_tell_a_friend_subject),
                activity.getString(R.string.about_tell_a_friend_body));

    }

}
