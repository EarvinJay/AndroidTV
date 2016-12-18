package com.itproject.android.androidtvsample;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    String m;
    String rep;
    Firebase rootref;
    ListView mlvsongs;
    String[] items;
    File file;
    int pos=0;

    public List<String> myList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mlvsongs=(ListView) findViewById(R.id.lvsongs);
        rootref=new Firebase("https://songtogo-f2eae.firebaseio.com/NotificationRequest");


        final ArrayList<File> mySongs = findSongs(Environment.getExternalStorageDirectory());
        items=new String[mySongs.size()];

        for(int i=0; i<mySongs.size();i++)
        {
            items[i]=mySongs.get(i).getName().toString();
        }


//        myList = new ArrayList<String>();
//        String root_sd = Environment.getExternalStorageDirectory().toString();
//        file = new File( root_sd + "/DCIM" ) ;
//        File[] list = file.listFiles();
//
//        for( int i=0; i< list.length; i++)
//        {
//            myList.add(list[i].getName() );
//        }
//
//
//        ArrayAdapter<String> adp=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,myList);
//        mlvsongs.setAdapter(adp);



        ArrayAdapter<String> adp=new ArrayAdapter<String>(getApplicationContext(),R.layout.list_item,R.id.txtlistitem,items);
        mlvsongs.setAdapter(adp);

        mlvsongs.setItemChecked(0,true);
        Firebase ref=rootref.child("notification");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                m=dataSnapshot.getValue(String.class);


                if(m.equals("UP"))
                {

                }
                else if(m.equals("DOWN"))
                {

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });




        mlvsongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),m,Toast.LENGTH_SHORT).show();
                view.setSelected(true);
                 startActivity(new Intent(getApplicationContext(),MainActivity.class).putExtra("pos",position).putExtra("songlist", mySongs));
            }
        });

    }

    public ArrayList<File> findSongs(File root)
    {
        ArrayList<File> al=new ArrayList<File>();
        File[] files = root.listFiles();

        for(File singleFile: files){
            if(singleFile.isDirectory() && !singleFile.isHidden())
            {
                al.addAll(findSongs(singleFile));
            }
            else{
                if(singleFile.getName().endsWith(".kar"))
                {
                    al.add(singleFile);
                }
            }
        }
        return al;
    }

}
