package com.itproject.android.androidtvsample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class AcceptReserveActivity extends AppCompatActivity {

    Firebase acceptedreservationref,userRef;
    ArrayList<String> mAReservation= new  ArrayList<>();
    ArrayList<String> mDUrl= new  ArrayList<>();
    ArrayList<String> mSongs= new  ArrayList<>();
    ArrayList<String> mTime= new  ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    ListView mlvAreserve;
    String ktvemail,roomname,numberofhours, playlistkey,username;
    ProgressBar pgbar;
    Button downloadAReserve;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_reserve);


        Intent intent =getIntent();
        ktvemail=intent.getStringExtra("EMAIL").toString().replace("."," ");
        roomname=intent.getStringExtra("ROOM#");

        acceptedreservationref=new Firebase("https://songtogo-f2eae.firebaseio.com/KTV-Bar/"+ktvemail+"/Room/"+roomname);


        mlvAreserve=(ListView) findViewById(R.id.lvareserve);
        pgbar=(ProgressBar)findViewById(R.id.progressBar);
        downloadAReserve=(Button) findViewById(R.id.btndownloadsong);

        acceptedreservationref.addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot snapshot) {

                Reservation newPost = snapshot.getValue(Reservation.class);
                username=newPost.getUsername();
                playlistkey=newPost.getPlaylistkey();
                numberofhours=newPost.getNumberofhours();
                retrieve();
               }


            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(AcceptReserveActivity.this,"ERROR",Toast.LENGTH_SHORT).show();
            }
        });



    }
    public void retrieve()
    {
        arrayAdapter=new ArrayAdapter(this,R.layout.list_item,R.id.txtlistitem,mSongs);
        userRef=new Firebase("https://songtogo-f2eae.firebaseio.com/users/"+username+"/Playlist/"+playlistkey+"/Songs");

        userRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                pgbar.setVisibility(ProgressBar.GONE);
                String sname=dataSnapshot.child("songname").getValue(String.class);
                String durl=dataSnapshot.child("songurl").getValue(String.class);
                mSongs.add(sname);
                mDUrl.add(durl);
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

        downloadAReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<mDUrl.size();i++)
                {
                    Toast.makeText(AcceptReserveActivity.this, mDUrl.get(i).toString(), Toast.LENGTH_SHORT).show();
                }
                Intent newActivity = new Intent(AcceptReserveActivity.this,ListActivity.class);
                startActivity(newActivity);
            }
        });

    }


}
