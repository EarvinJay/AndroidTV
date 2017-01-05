package com.itproject.android.androidtvsample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

public class AcceptReserveActivity extends AppCompatActivity {

    Firebase acceptedreservationref;
    ArrayList<String> mAReservation= new  ArrayList<>();
    ArrayList<String> mUName= new  ArrayList<>();
    ArrayList<String> mTime= new  ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    ListView mlvAreserve;
    String ktvemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_reserve);


        Intent intent =getIntent();
        ktvemail=intent.getStringExtra("EMAIL").toString().replace("."," ");

        acceptedreservationref=new Firebase("https://songtogo-f2eae.firebaseio.com/KTV-Bar/"+ktvemail+"/AcceptedReservation");
        arrayAdapter=new ArrayAdapter(this,R.layout.list_item,R.id.txtlistitem,mAReservation);
        mlvAreserve=(ListView) findViewById(R.id.lvareserve);

        acceptedreservationref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               // String dates=dataSnapshot.child("dates").getValue(String.class);
                String numberofhours=dataSnapshot.child("numberofhours").getValue(String.class);
                String playlistkey=dataSnapshot.child("playlistkey").getValue(String.class);
                String username=dataSnapshot.child("username").getValue(String.class);
                mAReservation.add(playlistkey);
                mUName.add(username);
                mTime.add(numberofhours);
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                arrayAdapter.notifyDataSetChanged();
            }
        });

        mlvAreserve.setAdapter(arrayAdapter);

        mlvAreserve.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int pos=position;
                String val =(String) mlvAreserve.getItemAtPosition(pos);
                Intent newActivity = new Intent(AcceptReserveActivity.this,DownloadSongsActivity.class);
                newActivity.putExtra("PKey",val);
                newActivity.putExtra("UName",mUName.get(pos));
                newActivity.putExtra("mTime",mTime.get(pos));
                startActivity(newActivity);

            }
        });

    }
}
