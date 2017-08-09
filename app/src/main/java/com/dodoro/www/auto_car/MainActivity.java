package com.dodoro.www.auto_car;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    ArrayList<HashMap<String, Object>> mylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView)findViewById(R.id.listview1);

        final String connectList[] = getResources().getStringArray(R.array.connect_choice);
        int imgs[] = {R.drawable.ap_icon,R.drawable.wifi_icon};
        mylist = new ArrayList<>();
        for(int i=0; i< connectList.length; i++)
        {
            HashMap<String, Object> m1 = new HashMap<>();
            m1.put("conList",connectList[i]);
            m1.put("img", imgs[i]);
            mylist.add(m1);
        }

        SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, mylist,R.layout.connect_choice_layout,new String[]{"conList","img"},new int[]{R.id.textView1,R.id.imageView1});
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,connectList[position],Toast.LENGTH_SHORT).show();
            }
        });

    }
}
