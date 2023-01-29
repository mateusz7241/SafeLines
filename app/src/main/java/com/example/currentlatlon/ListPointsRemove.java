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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import Model.PointMap;

public class ListPointsRemove extends AppCompatActivity {

    private ListView listViewRemove;
    private TextView info;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Button deletePoint;
    private EditText namePoint;
    public String objectName;
    private ArrayList<String> locationInfoArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_points_remove);

        listViewRemove = findViewById(R.id.listViewRemove);
        info = findViewById(R.id.info);
        deletePoint = findViewById(R.id.deletePoint);
        namePoint = findViewById(R.id.namePoint);

        info.setTextIsSelectable(true);
        database = FirebaseDatabase.getInstance();

        locationInfoArrayList = new ArrayList<String>();


        deletePoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = database.getReference("Points");
                //reference.child("-ndsadas").removeValue(); // usuwa punkt o nazwie w child
                String nazwa = namePoint.getText().toString();

                if(nazwa.length() > 1){
                    reference.child(nazwa).removeValue(); // usuwa punkt o nazwie w child
                    info.setText("Usunieto punkt o podanej nazwie: " + nazwa);
                }else{
                    info.setText("Nie udalo sie usunac punktu");
                }

            }
        });


        /**
         listPointsRemove.getItemAtPosition(id) -> zwraca mi obiekt o danym id
         */

        listViewRemove.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                info.setText(String.valueOf(listViewRemove.getItemAtPosition(position)));
            }
        });

        initializeListView();


    }

    private void initializeListView() {

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,locationInfoArrayList);


        reference = database.getReference("Points");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String s) {
                String value = snapshot.getValue(PointMap.class).toString();
                objectName = snapshot.getKey();
                locationInfoArrayList.add("id: " + objectName + "\n" + value);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String s) {
                objectName = snapshot.getKey();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                locationInfoArrayList.remove(snapshot.getValue().toString());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listViewRemove.setAdapter(adapter);
    }
}