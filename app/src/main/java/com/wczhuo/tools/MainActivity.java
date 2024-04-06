package com.wczhuo.tools;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.Manifest;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;

    private boolean isRefuse;

    private WebView wbH5;
    private String url;
    private WebView webView;
    private ProgressBar progressBar;

    protected CustomViewGroup blockingView = null;

    static class MyTimerTask extends TimerTask {
        public void run() {
            System.out.println("我爱你中国");
        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hideSystemUI();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !isRefuse) {// android 11  且 不是已经被拒绝
            // 先判断有没有权限
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1024);
            }
        }

        // 暂定5秒，等待网络连接，开机启动时等待wifi连接成功
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        url = "http://192.168.2.15/";

//        Toast toast = Toast.makeText(MainActivity.this,url, Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER,100,100);
//        toast.show();

        wbH5 = findViewById(R.id.wbH5);

        wbH5.setWebChromeClient(new MainActivity.MyWebChromeClient());//实现页面加载时的进度条功能
        wbH5.setWebViewClient(new MainActivity.MyWebViewClient());//这步就是注册监听你在第一个网站上的点击事件，好进行跳转

        // 下面的是进行网站的一些细节的设置------------------------------------------
        wbH5.getSettings().setJavaScriptEnabled(true);
//        wbH5.getSettings().setAppCacheEnabled(true);
        wbH5.getSettings().setAllowFileAccess(true);
        wbH5.getSettings().setUseWideViewPort(true);
        wbH5.getSettings().setLoadWithOverviewMode(true);
        // 关闭缓存
//        wbH5.clearCache(true);
        wbH5.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
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

            wbH5.setWebChromeClient(new MainActivity.MyWebChromeClient());
            wbH5.setWebViewClient(new MainActivity.MyWebViewClient());

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

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (request.isForMainFrame()) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    wbH5.loadUrl(url);
                }
            }
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            wbH5.loadUrl(url);
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

    // 带回授权结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1024 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 检查是否有权限
            if (Environment.isExternalStorageManager()) {
                isRefuse = false;
                // 授权成功
            } else {
                isRefuse = true;
                // 授权失败
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
                /*String strMachineId = "123123";
                SDFileHelper sdObj = new SDFileHelper();
                try {
                    sdObj.savaFileToSD("machineId.txt", strMachineId);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }*/
            }
        }
    }
}