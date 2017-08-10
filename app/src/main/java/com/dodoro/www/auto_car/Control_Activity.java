package com.dodoro.www.auto_car;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class Control_Activity extends AppCompatActivity implements Button.OnClickListener{
    WebView webView;
    ImageButton btn_right, btn_left, btn_top, btn_down;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_control_);

        btn_top = (ImageButton)findViewById(R.id.imageButton1);
        btn_down = (ImageButton)findViewById(R.id.imageButton2);
        btn_left = (ImageButton)findViewById(R.id.imageButton3);
        btn_right = (ImageButton)findViewById(R.id.imageButton4);


        webView = (WebView)findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
//        webView.loadUrl("http://192.168.0.1:5000");
        webView.loadUrl("http://wwww.gooogle.com.tw");

        btn_top.setOnClickListener(this);
        btn_down.setOnClickListener(this);
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.imageButton1:
                break;
            case R.id.imageButton2:
                Toast.makeText(Control_Activity.this,"down",Toast.LENGTH_SHORT).show();
                break;
            case R.id.imageButton3:
                Toast.makeText(Control_Activity.this,"left",Toast.LENGTH_SHORT).show();
                break;
            case R.id.imageButton4:
                Toast.makeText(Control_Activity.this,"right",Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
