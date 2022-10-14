package com.vitechtoday.technologyfornepali;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vitechtoday.technologyfornepali.data.MenuData;

public class MainActivity extends AppCompatActivity {
private ArrayAdapter<String> arrayAdapter;
private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MenuData.getMenuData());
        listView= findViewById(R.id.menu_listView);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i) == "audio and FM")
                    startActivity(new Intent(MainActivity.this, AudioAndFmActivity.class));
            }
        });

    }
}