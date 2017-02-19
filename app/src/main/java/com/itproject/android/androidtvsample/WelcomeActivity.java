package com.itproject.android.androidtvsample;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashSet;

public class WelcomeActivity extends AppCompatActivity {
    ListView logroom;
    AlertDialog alertDialogRoom;
    AlertDialog.Builder alertDialogBuilderRoom;
    Button mbtnstart;
    EditText mtxtemail,mroomname;
    Firebase roomref;
    int pos;
    String repktv,setRoom;
    ArrayList<String> mFinalRoom= new  ArrayList<>();
    ArrayAdapter<String> RarrayAdapterp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mbtnstart=(Button) findViewById(R.id.btnstart);
        mtxtemail=(EditText) findViewById(R.id.txtemail);
        mroomname=(EditText) findViewById(R.id.txtroomname);
        logroom = new ListView(this);



        alertDialogBuilderRoom = new AlertDialog.Builder(this);
        alertDialogBuilderRoom.setView(logroom);
        alertDialogRoom = alertDialogBuilderRoom.create();

        mbtnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WelcomeActivity.this,mtxtemail.getText().toString().replace("."," "),Toast.LENGTH_LONG).show();
                function();
                alertDialogRoom.setTitle("Room Assigned");
                alertDialogRoom.show();

            }
        });

        logroom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 pos=position;
                setRoom=""+mFinalRoom.get(pos);
                Intent intent=new Intent(WelcomeActivity.this,AcceptReserveActivity.class);
                intent.putExtra("EMAIL",mtxtemail.getText().toString());
                intent.putExtra("ROOM#",setRoom);
                startActivity(intent);

                alertDialogRoom.dismiss();
            }
        });


    }

    public void function() {
        roomref = new Firebase("https://songtogo-f2eae.firebaseio.com/KTV-Bar/" + mtxtemail.getText().toString().replace(".", " ") + "/Room");

        RarrayAdapterp = new ArrayAdapter(this,R.layout.list_item_room,R.id.txtItemRView,mFinalRoom);

        roomref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String rname = dataSnapshot.child("room").getValue(String.class);

                mFinalRoom.add(rname);
                HashSet hs = new HashSet();
                hs.addAll(mFinalRoom);
                mFinalRoom.clear();
                mFinalRoom.addAll(hs);
                RarrayAdapterp.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                RarrayAdapterp.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                RarrayAdapterp.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                RarrayAdapterp.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                RarrayAdapterp.notifyDataSetChanged();
            }
        });

        logroom.setAdapter(RarrayAdapterp);

    }

}
