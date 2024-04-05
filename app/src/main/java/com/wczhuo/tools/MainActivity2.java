package com.wczhuo.tools;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.StatusBarManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.lang.reflect.Method;

public class MainActivity2 extends AppCompatActivity {

    private WebView wbH5;
    private WebView webView;
    private ProgressBar progressBar;

    protected CustomViewGroup blockingView = null;

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        hideSystemUI();

//        disableStatusBar();

//        PhoneStatusBar a1 = new PhoneStatusBar();
//        (StatusBarManager)getBaseContext().getSystemService(Context.STATUS_BAR_SERVICE);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            @SuppressLint("WrongConstant") StatusBarManager mStatusBarManager = (StatusBarManager)getSystemService(Context.STATUS_BAR_SERVICE);
//            mStatusBarManager.disable(StatusBarManager.DISABLE_EXPAND);
//        }

        String strMchineId = "";
        String strScreenId = "1";
        // 读取设置的机器号
        try {
            SDFileHelper sdObj = new SDFileHelper();
            strMchineId = sdObj.readFromSD("machineId.txt");
            String[] arr = strMchineId.split(",");
            strMchineId = arr[0];
            if (arr.length > 1) {
                strScreenId = arr[1];
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String url = "https://m.ebeic.com/machine/index/category?mid=" + strMchineId;
        url = "https://machine.ebeic.com/?mid=" + strMchineId + "&sid=" + strScreenId;

//        Toast toast = Toast.makeText(MainActivity2.this,url, Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER,100,100);
//        toast.show();

        wbH5 = findViewById(R.id.wbH5);

        wbH5.setWebChromeClient(new MyWebChromeClient());//实现页面加载时的进度条功能
        wbH5.setWebViewClient(new MyWebViewClient());//这步就是注册监听你在第一个网站上的点击事件，好进行跳转

        // 下面的是进行网站的一些细节的设置------------------------------------------
        wbH5.getSettings().setJavaScriptEnabled(true);
//        wbH5.getSettings().setAppCacheEnabled(true);
        wbH5.getSettings().setAllowFileAccess(true);
        wbH5.getSettings().setUseWideViewPort(true);
        wbH5.getSettings().setLoadWithOverviewMode(true);
        wbH5.getSettings().setDomStorageEnabled(true);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            wbH5.getSettings().setDisplayZoomControls(true);
        else
            wbH5.getSettings().setBuiltInZoomControls(true);*/

        wbH5.loadUrl(url);
    }

    /*protected void disableStatusBar() {

        WindowManager manager = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));

        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |

                // this is to enable the notification to receive touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |

                // Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        localLayoutParams.height = (int) (40 * getResources().getDisplayMetrics().scaledDensity);
        localLayoutParams.format = PixelFormat.TRANSPARENT;

        blockingView = new CustomViewGroup(this);
        manager.addView(blockingView, localLayoutParams);
    }*/

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int progress) {
            if (progress == 100) progress = 0;
//            progressBar.setProgress(progress);
        }
    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            wbH5.setWebChromeClient(new MyWebChromeClient());
            wbH5.setWebViewClient(new MyWebViewClient());

            // findWB.getSettings().setJavaScriptEnabled(true);
//            wbH5.getSettings().setAppCacheEnabled(true);
            wbH5.getSettings().setAllowFileAccess(true);
            wbH5.getSettings().setUseWideViewPort(true);
            wbH5.getSettings().setLoadWithOverviewMode(true);
            wbH5.getSettings().setDomStorageEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                wbH5.getSettings().setDisplayZoomControls(true);
            else wbH5.getSettings().setBuiltInZoomControls(true);
            wbH5.loadUrl(url);

            return true;
        }

    }

    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private WebChromeClient webChromeClient = new WebChromeClient() {
        //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(webView.getContext());
            localBuilder.setMessage(message).setPositiveButton("确定", null);
            localBuilder.setCancelable(false);
            localBuilder.create().show();

            Log.i("ansen", "点击页面");

            //注意:
            //必须要这一句代码:result.confirm()表示:
            //处理结果为确定状态同时唤醒WebCore线程
            //否则不能继续点击按钮
            result.confirm();
            return true;
        }

        //获取网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            Log.i("ansen", "网页标题:" + title);
        }

        //加载进度回调
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
//            progressBar.setProgress(newProgress);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("ansen", "是否有上一个页面:" + webView.canGoBack());
        if (webView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {//点击返回按钮的时候判断有没有上一页
            webView.goBack(); // goBack()表示返回webView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}