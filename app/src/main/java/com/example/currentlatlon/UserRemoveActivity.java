package com.example.currentlatlon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import Model.PointMap;
import Model.User;

public class UserRemoveActivity extends AppCompatActivity {

    private ListView userListViewRemove;
    private TextView infoUser;
    private EditText nameUser;
    private Button deleteUser;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    public String objectName;
    private ArrayList<String> userInfoArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_remove);

        userListViewRemove = findViewById(R.id.userListViewRemove);
        infoUser = findViewById(R.id.infoUser);
        nameUser = findViewById(R.id.nameUser);
        deleteUser = findViewById(R.id.deleteUser);

        infoUser.setTextIsSelectable(true);
        database = FirebaseDatabase.getInstance();

        userInfoArrayList = new ArrayList<String>();



        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = database.getReference("Users");
                //reference.child("-ndsadas").removeValue(); // usuwa uzytkownika o nazwie w child
                String nazwa = nameUser.getText().toString();

                if(nazwa.length() > 1){
                    reference.child(nazwa).removeValue(); // usuwa uzytkownika o nazwie w child
                    infoUser.setText("Usunieto użytkownika o podanej nazwie w bazie: " + nazwa);
                }else{
                    infoUser.setText("Nie udalo sie usunac użytkownika");
                }
            }
        });

        userListViewRemove.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                infoUser.setText(String.valueOf(userListViewRemove.getItemAtPosition(position)));
            }
        });

        initializeListView();

    }

    private void initializeListView() {

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,userInfoArrayList);


        reference = database.getReference("Users");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String s) {
                String value = snapshot.getValue(User.class).toString();
                objectName = snapshot.getKey();
                userInfoArrayList.add("id: " + objectName + "\n" + value);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String s) {
                objectName = snapshot.getKey();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                userInfoArrayList.remove(snapshot.getValue().toString());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        userListViewRemove.setAdapter(adapter);
    }
}