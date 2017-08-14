package com.dodoro.www.auto_car;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Control_Activity extends Activity implements Button.OnClickListener, View.OnTouchListener {
    WebView webView,webView1;
    ImageButton btn_right, btn_left, btn_top, btn_down;
    String ip_address, control_web;
    RelativeLayout rl;
    boolean touchchk;

    private float upX, upY, downX, downY;
    private TextView tv1, tv2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_control_);
//        getSupportActionBar().hide();

        btn_top = (ImageButton) findViewById(R.id.imageButton1);
        btn_down = (ImageButton) findViewById(R.id.imageButton2);
        btn_left = (ImageButton) findViewById(R.id.imageButton3);
        btn_right = (ImageButton) findViewById(R.id.imageButton4);

        rl = (RelativeLayout)findViewById(R.id.relativelayout);
        tv1 = (TextView) findViewById(R.id.textView1);
        tv2 = (TextView) findViewById(R.id.textView2);

        ip_address = "192.168.0.1";

        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.loadUrl("http://192.168.0.1:5000");
//        webView.loadUrl("http://wwww.gooogle.com.tw");

        btn_top.setOnClickListener(this);
        btn_down.setOnClickListener(this);
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);

        rl.setOnTouchListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton1:
                control_web = "http://" + ip_address + ":5000/?control=front";
//                new WebView(this).loadUrl(control_web);
                webView1.loadUrl(control_web);
                Toast.makeText(Control_Activity.this, ip_address, Toast.LENGTH_SHORT).show();
                break;
            case R.id.imageButton2:
                control_web = "http://" + ip_address + ":5000/?control=back";
//                new WebView(this).loadUrl(control_web);
                webView1.loadUrl(control_web);
                Toast.makeText(Control_Activity.this, "down", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imageButton3:
                control_web = "http://" + ip_address + ":5000/?control=left";
//                new WebView(this).loadUrl(control_web);
                webView1.loadUrl(control_web);
                Toast.makeText(Control_Activity.this, "left", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imageButton4:
                control_web = "http://" + ip_address + ":5000/?control=right";
//                new WebView(this).loadUrl(control_web);
                webView1.loadUrl(control_web);
                Toast.makeText(Control_Activity.this, "right", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float X = event.getX();
        float Y = event.getY();

        switch (event.getAction()) { // 判斷觸控的動作

            case MotionEvent.ACTION_DOWN: // 按下
                downX = event.getX();  // 觸控的 X 軸位置
                downY = event.getY();  // 觸控的 Y 軸位置
                touchchk = true;
                tv1.setText("點擊!!");
                tv2.setText("X軸 : " + String.valueOf(downX) + "\tY軸 : " + String.valueOf(downY) + ((touchchk == true)?"\ttrue":"\tfalse"));
                return true;
            case MotionEvent.ACTION_MOVE: // 拖曳
                upX = event.getX();
                upY = event.getY();
                float x = Math.abs(upX - downX);
                float y = Math.abs(upY - downY);
                double z = Math.sqrt(x * x + y * y);
                int jiaodu = Math.round((float) (Math.asin(y / z) / Math.PI * 180));//角度
                tv2.setText("X軸 : " + String.valueOf(upX) + "\tY軸 : " + String.valueOf(upY));
//                if (upY < downY && jiaodu > 45)
                if (((downY - upY) > 150) && jiaodu > 45 && touchchk){//上
                    control_web = "http://" + ip_address + ":5000/?control=front";
//                    new WebView(this).loadUrl(control_web);
                    webView1.loadUrl(control_web);
                    tv1.setText("角度:" + jiaodu + ", 動作:上\n" + control_web + ((touchchk == true)?"\ttrue":"\tfalse"));
                    touchchk = false;
                } //else if (upY > downY && jiaodu > 45) {//下
                    else if(((downY - upY) < -150) && jiaodu > 45 && touchchk){
                    control_web = "http://" + ip_address + ":5000/?control=back";
//                    new WebView(this).loadUrl(control_web);
                    webView1.loadUrl(control_web);
                    tv1.setText("角度:" + jiaodu + ", 動作:下\n" + control_web + ((touchchk == true)?"\ttrue":"\tfalse"));
                    touchchk = false;
                } //else if (upX < downX && jiaodu <= 45) {//左
                    else if(((downX - upX) > 150) && jiaodu <= 45 && touchchk){
                    control_web = "http://" + ip_address + ":5000/?control=left";
//                    new WebView(this).loadUrl(control_web);
                    webView1.loadUrl(control_web);
                    tv1.setText("角度:" + jiaodu + ", 動作:左\n" + control_web + ((touchchk == true)?"\ttrue":"\tfalse"));
                    touchchk = false;
                } //else if (upX > downX && jiaodu <= 45) {//右
                    else if(((downX - upX) < -150) && jiaodu <= 45 && touchchk){
                    control_web = "http://" + ip_address + ":5000/?control=right";
//                    new WebView(this).loadUrl(control_web);
                    webView1.loadUrl(control_web);
                    tv1.setText("角度:" + jiaodu + ", 動作:右\n" + control_web + ((touchchk == true)?"\ttrue":"\tfalse"));
//                    touchchk = false;
                }
//                new WebView(this).loadUrl(control_web);
//                touchchk = false;
                return true;
            case MotionEvent.ACTION_UP: // 放開
                    control_web = "http://" + ip_address + ":5000/?control=stop";
//                    new WebView(this).loadUrl(control_web);
                    webView1.loadUrl(control_web);
                    tv1.setText("動作:離開\n" + control_web);
                    touchchk = false;
                return true;
        }

        return super.onTouchEvent(event);
    }

}