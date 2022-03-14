package com.pm.projetpkmn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class PostDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        WebView myWebView = (WebView) findViewById(R.id.webview);

        String url = getIntent().getStringExtra("slug");
        //url = "https://hytale.com/api/blog/post/slug/" + url; pour l'api
        Log.d("url", url);
        myWebView.loadUrl(url);

    }
}