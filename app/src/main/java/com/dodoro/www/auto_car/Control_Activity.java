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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

    private float upX, upY, downX, downY;
    private TextView tv1, tv2, tv_time, tv_temp, tv_humi;

    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隱藏ActionBar方法2，注意extends Activity，而非AppCompatActivity，因為AppCompatActivity上方的父類別有ActionBar有ActionBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_control_);
//        getSupportActionBar().hide();

        //抓firebase溫濕度數據
        myRef = FirebaseDatabase.getInstance().getReference("TemperatureHumidity");

        //方向四鍵
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

        //webView顯示影像
        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.loadUrl("http://192.168.0.1:5000");
//        webView.loadUrl("http://wwww.gooogle.com.tw");

        //webView1連結操控http...get
        webView1 = new WebView(this);

        //監聽imagebutton -- 作用-按鍵操控
        btn_top.setOnClickListener(this);
        btn_down.setOnClickListener(this);
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);

        //監聽觸控 -- 作用-滑動操控
        rl.setOnTouchListener(this);
        //監聽firebase有無新增數據
        myRef.addChildEventListener(this);

    }

    //按鍵操控 - 四方向
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton1:
                control_web = "http://" + ip_address + ":5000/?control=front";
                webView1.loadUrl(control_web);
                break;
            case R.id.imageButton2:
                control_web = "http://" + ip_address + ":5000/?control=back";
                webView1.loadUrl(control_web);
                break;
            case R.id.imageButton3:
                control_web = "http://" + ip_address + ":5000/?control=left";
                webView1.loadUrl(control_web);
                break;
            case R.id.imageButton4:
                control_web = "http://" + ip_address + ":5000/?control=right";
                webView1.loadUrl(control_web);
                break;
        }

    }
/*
    滑動操控
    原理說明:
    1.紀錄手指觸控螢幕時的位置，touchchk紀錄
    2.紀錄拖曳滑動的位置
    3.兩者做計算，距離超過150(自訂)時，且以touchchk=true(做一次性輸出)，才執行車子運行動作，避免一直重複輸出指令
    4.計算角度(jiaodu)作用，判定方向性，例如，手指滑動右上方2點鐘方向，以45度角作分界，超過45度角判定為"上"，低於45度角判定為"下"
*/
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 判斷觸控的動作
        switch (event.getAction()) {
            // 按下
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();  // 觸控的 X 軸位置
                downY = event.getY();  // 觸控的 Y 軸位置
                touchchk = true;
                return true;
            // 拖曳
            case MotionEvent.ACTION_MOVE:
                upX = event.getX();
                upY = event.getY();
                float x = Math.abs(upX - downX);
                float y = Math.abs(upY - downY);
                double z = Math.sqrt(x * x + y * y);
                int jiaodu = Math.round((float) (Math.asin(y / z) / Math.PI * 180));//計算角度

                Log.d("test",String.valueOf(x) + "\t" + String.valueOf((downX - upX)));

                //輸出"上"
                if (((downY - upY) > 150) && jiaodu > 45 && touchchk){
                    control_web = "http://" + ip_address + ":5000/?control=front";
                    webView1.loadUrl(control_web);
                    touchchk = false;
                }
                //輸出"下"
                else if(((downY - upY) < -150) && jiaodu > 45 && touchchk){
                    control_web = "http://" + ip_address + ":5000/?control=back";
                    webView1.loadUrl(control_web);
                    touchchk = false;
                }
                //輸出"左"
                else if(((downX - upX) > 150) && jiaodu <= 45 && touchchk){
                    control_web = "http://" + ip_address + ":5000/?control=left";
                    webView1.loadUrl(control_web);
                    touchchk = false;
                }
                //輸出"右"
                else if(((downX - upX) < -150) && jiaodu <= 45 && touchchk){
                    control_web = "http://" + ip_address + ":5000/?control=right";
                    webView1.loadUrl(control_web);
                    touchchk = false;
                }
                return true;
            // 手指放開時，輸出STOP指令
            case MotionEvent.ACTION_UP:
                    control_web = "http://" + ip_address + ":5000/?control=stop";
                    webView1.loadUrl(control_web);
                    touchchk = false;
                return true;
        }
        return super.onTouchEvent(event);
    }

    //firebase 資料庫讀取
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

App 狀態: 1.影像仍有傳輸 2.指令有顯示出來
造成結果: 傳輸數個指令後，車子無法再接收指令，而造成車子依最後一個指令而動作，數分鐘後，又可接收指令

可能問題點:
1.傳送端
2.接收端

依App上顯示有傳送訊息，但Pi沒收到

結論:
有可能是因為new object(匿名物件)使用太過頻繁，造成手機空間不足或其他因素，使得手機沒釋放出object(匿名物件)，而導致沒new object，造成指令傳輸停擺

註: 測試手機有空間不足的因素，目前尚未拿另一支手機測試


*/