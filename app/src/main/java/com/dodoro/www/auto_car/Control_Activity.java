package com.dodoro.www.auto_car;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dodoro.www.auto_car.TemperatureHumidity.*;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Control_Activity extends Activity implements Button.OnClickListener, View.OnTouchListener,ChildEventListener {
    WebView webView,webView1;
    ImageButton btn_right, btn_left, btn_top, btn_down;
    String ip_address, control_web;
    RelativeLayout rl;
    boolean touchchk;
    int  count = 0;

    private float upX, upY, downX, downY;
    private TextView tv1, tv2, tv_time, tv_temp, tv_humi;

    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_control_);
//        getSupportActionBar().hide();

        myRef = FirebaseDatabase.getInstance().getReference("TemperatureHumidity");

        btn_top = (ImageButton) findViewById(R.id.imageButton1);
        btn_down = (ImageButton) findViewById(R.id.imageButton2);
        btn_left = (ImageButton) findViewById(R.id.imageButton3);
        btn_right = (ImageButton) findViewById(R.id.imageButton4);

        rl = (RelativeLayout)findViewById(R.id.relativelayout);
        tv1 = (TextView) findViewById(R.id.textView1);
        tv2 = (TextView) findViewById(R.id.textView2);

        tv_time = (TextView)findViewById(R.id.textView_time) ;
        tv_temp = (TextView)findViewById(R.id.textView_temperature);
        tv_humi = (TextView)findViewById(R.id.textView_humidity);

        ip_address = "192.168.0.1";

        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
//        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.loadUrl("http://192.168.0.1:5000");
//        webView.loadUrl("http://wwww.gooogle.com.tw");

        webView1 = new WebView(this);

        btn_top.setOnClickListener(this);
        btn_down.setOnClickListener(this);
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);

        rl.setOnTouchListener(this);
        myRef.addChildEventListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton1:
                control_web = "http://" + ip_address + ":5000/?control=front";
//                new WebView(this).loadUrl(control_web);
                webView1.loadUrl(control_web);
                tv1.setText("前進\t" + control_web);
//                Toast.makeText(Control_Activity.this, ip_address, Toast.LENGTH_SHORT).show();
                break;
            case R.id.imageButton2:
                control_web = "http://" + ip_address + ":5000/?control=back";
//                new WebView(this).loadUrl(control_web);
                webView1.loadUrl(control_web);
                tv1.setText("後退\t" + control_web);
//                Toast.makeText(Control_Activity.this, "down", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imageButton3:
                control_web = "http://" + ip_address + ":5000/?control=left";
//                new WebView(this).loadUrl(control_web);
                webView1.loadUrl(control_web);
                tv1.setText("左轉\t" + control_web);
//                Toast.makeText(Control_Activity.this, "left", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imageButton4:
                control_web = "http://" + ip_address + ":5000/?control=right";
//                new WebView(this).loadUrl(control_web);
                webView1.loadUrl(control_web);
                tv1.setText("右轉\t" + control_web);
//                Toast.makeText(Control_Activity.this, "right", Toast.LENGTH_SHORT).show();
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
                count = 0;
                touchchk = true;
                tv1.setText("點擊!!" + "\n" + String.valueOf(count));
                tv2.setText("X軸 : " + String.valueOf(downX) + "\tY軸 : " + String.valueOf(downY) + ((touchchk == true)?"\nTrue":"\nFalse"));
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
                    count++;
                    tv1.setText("角度:" + jiaodu + ", 動作:上\n" + control_web + "\n" + String.valueOf(count));
                    touchchk = false;
                } //else if (upY > downY && jiaodu > 45) {//下
                    else if(((downY - upY) < -150) && jiaodu > 45 && touchchk){
                    control_web = "http://" + ip_address + ":5000/?control=back";
//                    new WebView(this).loadUrl(control_web);
                    webView1.loadUrl(control_web);
                    count++;
                    tv1.setText("角度:" + jiaodu + ", 動作:下\n" + control_web + "\n" + String.valueOf(count));
                    touchchk = false;
                } //else if (upX < downX && jiaodu <= 45) {//左
                    else if(((downX - upX) > 150) && jiaodu <= 45 && touchchk){
                    control_web = "http://" + ip_address + ":5000/?control=left";
//                    new WebView(this).loadUrl(control_web);
                    webView1.loadUrl(control_web);
                    count++;
                    tv1.setText("角度:" + jiaodu + ", 動作:左\n" + control_web + "\n" + String.valueOf(count));
                    touchchk = false;
                } //else if (upX > downX && jiaodu <= 45) {//右
                    else if(((downX - upX) < -150) && jiaodu <= 45 && touchchk){
                    control_web = "http://" + ip_address + ":5000/?control=right";
//                    new WebView(this).loadUrl(control_web);
                    webView1.loadUrl(control_web);
                    count++;
                    tv1.setText("角度:" + jiaodu + ", 動作:右\n" + control_web + "\n" + String.valueOf(count));
                    touchchk = false;
                }
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

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        getSensorInfor infor = dataSnapshot.getValue(getSensorInfor.class);
        String str_temp = String.valueOf(infor.getTemperature());
        String str_humi = String.valueOf(infor.getHumidity());

        tv_time.setText(infor.getDate());
        tv_temp.setText(str_temp);
        tv_humi.setText(str_humi);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}

/*
原本做法: 用 new WebView(this).loadUrl(control_web);

App 狀態: 1.影像仍有傳輸 2.指令想顯示出來
造成結果: 傳輸數個指令後，車子無法再接收指令，而造成車子依最後一個指令而動作，數分鐘後，又可接收指令

可能問題點:
1.傳送端
2.接收端

依App上顯示有傳送訊息，但Pi沒收到

結論:
有可能是因為new object(匿名物件)使用太過頻繁，造成手機空間不足或其他因素，使得手機沒釋放出object(匿名物件)，而導致沒new object，造成指令傳輸停擺

註: 測試手機有空間不足的因素，目前尚未拿另一支手機測試


*/