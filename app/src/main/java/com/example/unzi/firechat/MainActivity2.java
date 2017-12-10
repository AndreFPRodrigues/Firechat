package com.example.unzi.firechat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private static final String TAG = MainActivity2.class.getCanonicalName();

    private EditText edt_message;
    private String myEmail;

    private ListView lv;

    private List<String> chat;
    private ArrayAdapter<String> adapter;

    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get email
        Bundle b = getIntent().getExtras();
        myEmail = b.getString("email");
        edt_message = (EditText) findViewById(R.id.edt_chat_message);

        chat = new ArrayList<String>();

        lv = findViewById(R.id.chat_list);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, chat);

        // Assign adapter to ListView
        lv.setAdapter(adapter);

        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("chat");


        // Read from the database
        myRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    FireMessage value =dataSnapshot.getValue(FireMessage.class);
                    Log.d(TAG, "Value is: " + value.getMessageText());
                    updateUI(value);


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
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void updateUI(FireMessage value) {
        chat.add(value.getMessageText());
        adapter.notifyDataSetChanged();

    }

    public void sendMessage(View view) {
        String chatMessage = edt_message.getText().toString();
        FireMessage fm = new FireMessage(chatMessage, myEmail);
        myRef.push().setValue(fm);

    }
}
