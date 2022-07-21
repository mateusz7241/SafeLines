package com.example.currentlatlon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListPointsActivity extends AppCompatActivity {

    private Button addPointButton;
    private ListView listPoints;
    private EditText latitudeEditText,longitudeEditText,nameEditText;

    private FirebaseDatabase database;
    private DatabaseReference reference;



    private ArrayList<String> locationInfoArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_points);

        addPointButton = findViewById(R.id.addPointButton);
        listPoints = findViewById(R.id.listPoints);

        latitudeEditText = findViewById(R.id.latitudeEditText);
        longitudeEditText = findViewById(R.id.longitudeEditText);
        nameEditText = findViewById(R.id.nameEditText);


        database = FirebaseDatabase.getInstance();


        addPointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertPointData(view);
            }
        });

        locationInfoArrayList = new ArrayList<String>();

        initializeListView();


    }

    private void insertPointData(View view){
        double lat = Double.parseDouble(latitudeEditText.getText().toString());
        double lon = Double.parseDouble(longitudeEditText.getText().toString());
        String namePoint = nameEditText.getText().toString().trim();

        if(lat < -90 || lat > 90){
            latitudeEditText.setError(getString(R.string.typeLatitude));
            latitudeEditText.requestFocus();
            return;
        }
        if(lon < -180 || lon > 180){
            longitudeEditText.setError(getString(R.string.typeLongitude));
            longitudeEditText.requestFocus();
            return;
        }
        if(namePoint.isEmpty()){
            nameEditText.setError(getString(R.string.typeName));
            nameEditText.requestFocus();
            return;
        }
        reference = database.getReference("Points");
        PointMap pointMap = new PointMap(namePoint,lat,lon);
        reference.push().setValue(pointMap);
        Toast.makeText(ListPointsActivity.this, "Dodano punkt", Toast.LENGTH_SHORT).show();
    }

    private void initializeListView() {
        // creating a new array adapter for our list view.
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,locationInfoArrayList);

        // below line is used for getting reference
        // of our Firebase Database.
        reference = FirebaseDatabase.getInstance().getReference("Points");

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {
                String value = snapshot.getValue(PointMap.class).toString();
                locationInfoArrayList.add(value);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String s) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                locationInfoArrayList.remove(snapshot.getValue().toString());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listPoints.setAdapter(adapter);
    }


}