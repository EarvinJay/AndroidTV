package com.itproject.android.androidtvsample;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class AcceptReserveActivity extends AppCompatActivity {

    public static final int progress_bar_type = 0;
    ProgressDialog pDialog;
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

                    new DownloadFileFromUrl().execute(mDUrl.get(i).toString());


                }

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent newActivity = new Intent(AcceptReserveActivity.this,ListActivity.class);
                        startActivity(newActivity);
                    }
                }, 5000);


            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }


  class DownloadFileFromUrl extends AsyncTask<String, Integer, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }


        @Override
        protected String doInBackground(String... f_url) {
            int count;


            try
            {
                for(int i=0;i<f_url.length;i++)
                {
                    URL url = new URL(f_url[i]);
                    URLConnection conection = url.openConnection();
                    conection.connect();
                    //  String urlFileName = FilenameUtils.getName(f_url[i]);

                    String[] pathparts= url.getPath().split("\\/");
                    String filename= pathparts[pathparts.length-1].split("\\.", 1)[0];



                    int lenghtOfFile = conection.getContentLength();
                    InputStream input = new BufferedInputStream(
                            url.openStream(), 8192);

                    System.out.println("Data::" + f_url[i]);
                    // Output stream to write file
                    OutputStream output = new FileOutputStream(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename.replace("admin%2F","").replace(".kar","")));

                    byte data[] = new byte[1024];

                    long total = 0;

                    while((count = input.read(data))!=-1)
                    {
                        total += count;
                        // publishing the progress....
                        // After this onProgressUpdate will be called
                        publishProgress((int) ((total * 100)/lenghtOfFile));

                        // writing data to file
                        output.write(data, 0, count);
                    }

                    output.flush();

                    // closing streams
                    output.close();
                    input.close();


                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            pDialog.setProgress(progress[0]);
        }


        @Override
        protected void onPostExecute(String file_url) {
            dismissDialog(progress_bar_type);

            // Displaying downloaded image into image view
            // Reading image path from sdcard
            // String imagePath = Environment.getExternalStorageDirectory()
            //       .toString() + "/downloaded.jpg";
        }
    }

}
