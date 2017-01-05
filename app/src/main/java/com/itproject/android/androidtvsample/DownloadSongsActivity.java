package com.itproject.android.androidtvsample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

public class DownloadSongsActivity extends AppCompatActivity {

    Firebase downloadsongsref;
    ListView mlvdownloadsongs,lvdownloadurl;
    ArrayList<String> mDSongs= new  ArrayList<>();
    ArrayList<String> mDUrl= new  ArrayList<>();
    String mpkey,muname,mtime;
    ArrayAdapter<String> arrayAdapter;
    Button btndownloadsongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_songs);

        Intent intent =getIntent();
        mpkey=intent.getStringExtra("PKey");
        muname=intent.getStringExtra("UName");
        mtime=intent.getStringExtra("mTime");

        downloadsongsref=new Firebase("https://songtogo-f2eae.firebaseio.com/users/"+muname+"/Playlist/"+mpkey+"/Songs");
        mlvdownloadsongs=(ListView) findViewById(R.id.lvdownloadsongs);
        btndownloadsongs=(Button) findViewById(R.id.mbtndownloadsong);
        lvdownloadurl = new ListView(this);

        arrayAdapter=new ArrayAdapter(this,R.layout.list_item,R.id.txtlistitem,mDSongs);
        downloadsongsref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String sname=dataSnapshot.child("songname").getValue(String.class);
                String durl=dataSnapshot.child("songurl").getValue(String.class);
                mDSongs.add(sname);
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

        mlvdownloadsongs.setAdapter(arrayAdapter);



        btndownloadsongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<mDUrl.size();i++)
                {
                    Toast.makeText(DownloadSongsActivity.this, mDUrl.get(i).toString(), Toast.LENGTH_SHORT).show();
                }
                Intent newActivity = new Intent(DownloadSongsActivity.this,ListActivity.class);
                startActivity(newActivity);
            }
        });

    }
}
