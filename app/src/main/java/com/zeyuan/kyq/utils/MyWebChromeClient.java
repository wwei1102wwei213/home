package com.zeyuan.kyq.utils;

import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by Administrator on 2016/9/19.
 *
 *
 * @author wwei
 */
public class MyWebChromeClient extends WebChromeClient {

    private WebCall webCall;

    public void setWebCall(WebCall webCall) {
        this.webCall = webCall;
    }

    // For Android 3.0+
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        if (webCall != null)
            webCall.fileChose(uploadMsg);
    }

    // For Android < 3.0
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        openFileChooser(uploadMsg, "");
    }

    // For Android > 4.1.1
    public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                String acceptType, String capture) {
        openFileChooser(uploadMsg, acceptType);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        LogCustom.i("ZYS","newProgress:"+newProgress);
        webCall.forProgress(newProgress);
        super.onProgressChanged(view, newProgress);
    }

    // For Android > 5.0
    @Override
    public boolean onShowFileChooser(WebView webView,
                                     ValueCallback<Uri[]> filePathCallback,
                                     FileChooserParams fileChooserParams) {
        LogCustom.i("ZYS","onShowFileChooser 11");
        if (webCall != null)
            webCall.fileChose5(filePathCallback);
        return true;
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        if (webCall!=null){
            webCall.forTitle(title);
        }
    }

    public interface WebCall {
        void fileChose(ValueCallback<Uri> uploadMsg);

        void fileChose5(ValueCallback<Uri[]> uploadMsg);

        void forTitle(String title);

        void forProgress(int pos);
    }



}

