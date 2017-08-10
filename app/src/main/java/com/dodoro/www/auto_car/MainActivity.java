package com.dodoro.www.auto_car;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    ArrayList<HashMap<String, Object>> mylist;
    AlertDialog cdiag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
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
//                Toast.makeText(MainActivity.this,String.valueOf(position),Toast.LENGTH_SHORT).show();
                switch (position)
                {
                    case 0:
                        startActivity(new Intent(MainActivity.this,Control_Activity.class));
                        break;
                    case 1:

                        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                        View diagView = inflater.inflate(R.layout.ip_input_layout,null);
                        final EditText ed_diag = (EditText)diagView.findViewById(R.id.editText1);
                        Button btn = (Button)diagView.findViewById(R.id.button1);

                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent it = new Intent(MainActivity.this,Control_Activity.class);
                                it.putExtra("ip",ed_diag.getText().toString());
                                startActivity(it);
                                cdiag.dismiss();

                            }
                        });

                        cdiag = new AlertDialog.Builder(MainActivity.this).setTitle(R.string.input_ip_address).setView(diagView).show();
                        break;
                }

            }
        });

    }
}
