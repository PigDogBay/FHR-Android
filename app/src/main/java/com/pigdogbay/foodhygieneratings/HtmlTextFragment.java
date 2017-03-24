package com.pigdogbay.foodhygieneratings;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class HtmlTextFragment extends Fragment {
    private static final String ARG_RESOURCE_ID = "resource id";
    public static final String TAG = "html text";

    private int resourceId;

    public HtmlTextFragment() {
    }

    public static HtmlTextFragment newInstance(int resourceId) {
        HtmlTextFragment fragment = new HtmlTextFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RESOURCE_ID, resourceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            resourceId = getArguments().getInt(ARG_RESOURCE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        WebView webView = (WebView) inflater.inflate(R.layout.fragment_html_text, container, false);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        String html = getString(resourceId);
        webView.loadDataWithBaseURL(null, html, "text/HTML", "UTF-8", null);
        webView.setBackgroundColor(Color.TRANSPARENT);
        return webView;
    }

}
