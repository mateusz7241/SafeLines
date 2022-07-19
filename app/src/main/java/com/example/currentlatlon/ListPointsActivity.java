package com.example.currentlatlon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class ListPointsActivity extends AppCompatActivity {

    private Button addPoint;
    private ListView listPoints;
    private TextView infoTV;
    private EditText LatLngEditText;

    private ArrayList<String> locationInfo;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_points);

        addPoint = findViewById(R.id.addPoint);
        listPoints = findViewById(R.id.listPoints);
        infoTV = findViewById(R.id.infoTV);
        LatLngEditText = findViewById(R.id.LatLngEditText);


        locationInfo = new ArrayList<String>();
        Collections.addAll(locationInfo,new String[] {"Lokacja 1","Lokacja 2","Lokacja 3","Lokacja 4","Lokacja 5"});

        arrayAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,locationInfo);
        listPoints.setAdapter(arrayAdapter);

        listPoints.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                infoTV.setText(getString(R.string.listOnClick) +" "+ locationInfo.get(position));
            }
        });
        listPoints.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                locationInfo.remove(position);
                listPoints.invalidateViews();
                return true;
            }
        });

        addPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!LatLngEditText.getText().toString().isEmpty()){
                    locationInfo.add(LatLngEditText.getText().toString());
                    listPoints.invalidateViews();
                    LatLngEditText.setText("");
                }
            }
        });
    }
}