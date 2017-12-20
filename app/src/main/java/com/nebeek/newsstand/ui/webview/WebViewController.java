package com.nebeek.newsstand.ui.webview;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nebeek.newsstand.R;
import com.nebeek.newsstand.controller.base.HeaderController;

import butterknife.BindView;

public class WebViewController extends HeaderController implements WebViewContract.View {

    @BindView(R.id.web_view)
    WebView webView;

    private String url;

    public static WebViewController newInstance(String url) {
        WebViewController instance = new WebViewController();
        instance.url = url;
        return instance;
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_web_view, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        initHeader();
        initWebView();
    }

    private void initHeader() {
        headerBackButton.setText(getActivity().getString(R.string.icon_close));
//        headerBackButton.setVisibility(View.VISIBLE);
    }

    private void initWebView() {
        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(url);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
