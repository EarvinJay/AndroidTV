package com.itproject.android.androidtvsample;


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    VideoView video;
    Uri uri;
    int position;
    static MediaPlayer mp;
    ArrayList<File> mySongs;
    Button mbtnplay,mbtnprev,mbtnnext;
    CountDownT timer;
    Firebase rootref;
    TextView mtxtcommand,showtimer;
    File file;
    TextView mlyrics;
    StringBuilder text;
    BufferedReader br;
    String line,a="\\";
    String nofhours;
    Scroller mScroller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent i=getIntent();
        nofhours=i.getStringExtra("HOURS");
        Bundle b=i.getExtras();
        mySongs= (ArrayList) b.getParcelableArrayList("songlist");
        position= b.getInt("pos",0);

        mlyrics=(TextView) findViewById(R.id.txtlyrics);
        mlyrics.setMovementMethod(new ScrollingMovementMethod());
        mlyrics.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        mScroller=new Scroller(MainActivity.this,new LinearInterpolator());
        mlyrics.setScroller(mScroller);
        mScroller.startScroll(0,0,0,2000,280000);
        video=(VideoView) findViewById(R.id.backgroundvideo);
        mbtnplay=(Button) findViewById(R.id.btnplay);
        mbtnprev=(Button) findViewById(R.id.btnprev);
        mbtnnext=(Button) findViewById(R.id.btnnext);

        mbtnplay.setOnClickListener(this);
        mbtnprev.setOnClickListener(this);
        mbtnnext.setOnClickListener(this);

        if(mp!=null)
        {
            mp.stop();
            mp.release();

        }

        uri= Uri.parse(mySongs.get(position).toString());
        video.setVideoURI(Uri.parse("android.resource://com.itproject.android.androidtvsample/"+R.raw.karaokebg));
        mp=MediaPlayer.create(getApplicationContext(),uri);
        video.start();
        mp.start();

        file = new File(""+uri);
        if(file.exists())
        {

             text = new StringBuilder();

            try {
              br = new BufferedReader(new FileReader(file));


                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
            }
            catch (IOException e) {

            }

            mlyrics.setText(text.toString().replace("�p�","").replace("�P�","").replace("�","")
                    .replace("1","").replace("2","").replace("3","").replace("4","").replace("5","")
                    .replace("6","").replace("7","").replace("8","").replace("9","").replace("0","")
                    .replace("/","\n").replace(">","").replace("<","").replace("@","").replace("&","")
                    .replace("+","").replace("-","").replace("*","").replace("=","").replace("^","")
                    .replace("!","").replace("(","").replace(")","").replace("#","").replace(a,"\n\n")
                    .replace("$",""));
        }
        else
        {
            Toast.makeText(getApplicationContext(),"dont exist",Toast.LENGTH_SHORT).show();
        }

        rootref=new Firebase("https://songtogo-f2eae.firebaseio.com/NotificationRequest");
        mtxtcommand=(TextView) findViewById(R.id.txtcommand);
        showtimer=(TextView) findViewById(R.id.txttimer);
        timer=new CountDownT(20000,1000);
       // showtimer.setText("10");
        //timer.start();


        Firebase ref=rootref.child("notification");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            String m=dataSnapshot.getValue(String.class);
                mtxtcommand.setText(m);

                if(mtxtcommand.getText().equals("PLAY"))
                {
                    mp.start();
                    video.start();
                }
                else if(mtxtcommand.getText().equals("PAUSE"))
                {
                    mp.pause();
                    video.pause();
                }
                else if(mtxtcommand.getText().equals("STOP")){
                    mp.stop();
                    video.pause();
                }

                else if(mtxtcommand.getText().equals("PREVIOUS")){
                    mp.stop();
                    mp.release();
                    mScroller.startScroll(0,0,0,2000,280000);
                    position=(position-1<0)?mySongs.size()-1: position-1;
                    if(position-1<0)
                    {
                        position=mySongs.size()-1;
                    }
                    else{
                        position=position-1;}
                    uri= Uri.parse(mySongs.get(position).toString());
                    mp= MediaPlayer.create(getApplicationContext(),uri);
                    mp.start();
                    video.start();

                    file = new File(""+uri);
                    if(file.exists())
                    {

                        text = new StringBuilder();

                        try {
                         br= new BufferedReader(new FileReader(file));


                            while ((line = br.readLine()) != null) {
                                text.append(line);
                                text.append('\n');
                            }
                        }
                        catch (IOException e) {

                        }

                        mlyrics.setText(text.toString().replace("�p�","").replace("�P�","").replace("�","")
                                .replace("1","").replace("2","").replace("3","").replace("4","").replace("5","")
                                .replace("6","").replace("7","").replace("8","").replace("9","").replace("0","")
                                .replace("/","\n").replace(">","").replace("<","").replace("@","").replace("&","")
                                .replace("+","").replace("-","").replace("*","").replace("=","").replace("^","")
                                .replace("!","").replace("(","").replace(")","").replace("#","").replace(a,"\n\n")
                                .replace("$",""));
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"dont exist",Toast.LENGTH_SHORT).show();
                    }

                }

                else if(mtxtcommand.getText().equals("NEXT")){
                    mp.stop();
                    mp.release();

                    mScroller.startScroll(0,0,0,2000,280000);
                    position=(position+1)%mySongs.size();
                    uri= Uri.parse(mySongs.get(position).toString());
                    mp= MediaPlayer.create(getApplicationContext(),uri);
                    mp.start();
                    video.start();

                    file = new File(""+uri);
                    if(file.exists())
                    {

                         text = new StringBuilder();

                        try {
                          br = new BufferedReader(new FileReader(file));


                            while ((line = br.readLine()) != null) {
                                text.append(line);
                                text.append('\n');
                            }
                        }
                        catch (IOException e) {

                        }

                        mlyrics.setText(text.toString().replace("�p�","").replace("�P�","").replace("�","")
                                .replace("1","").replace("2","").replace("3","").replace("4","").replace("5","")
                                .replace("6","").replace("7","").replace("8","").replace("9","").replace("0","")
                                .replace("/","\n").replace(">","").replace("<","").replace("@","").replace("&","")
                                .replace("+","").replace("-","").replace("*","").replace("=","").replace("^","")
                                .replace("!","").replace("(","").replace(")","").replace("#","").replace(a,"\n\n")
                                .replace("$",""));
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"dont exist",Toast.LENGTH_SHORT).show();
                    }

                }


                else if(mtxtcommand.getText().equals("BACK")){
                    startActivity(new Intent(getApplicationContext(),ListActivity.class));
                }




            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    public void start(View view)
    {
timer.start();
    }

    public void stop(View view)
    {
timer.cancel();
    }

    @Override
    public void onClick(View v) {

        int id =v.getId();
        switch(id)
        {
            case R.id.btnplay:
                if(mp.isPlaying())
                {
                    mbtnplay.setText("PLAY");
                    mp.pause();
                    video.pause();

                }
                else {
                    mbtnplay.setText("PAUSE");
                    mp.start();
                    video.start();

                }
                break;

            case R.id.btnnext:
                mp.stop();
                mp.release();
                mScroller.startScroll(0,0,0,2000,280000);
                position=(position+1)%mySongs.size();
                uri= Uri.parse(mySongs.get(position).toString());
                mp= MediaPlayer.create(getApplicationContext(),uri);
                mp.start();
                video.start();

                file = new File(""+uri);
                if(file.exists())
                {

                    text = new StringBuilder();

                    try {
                     br = new BufferedReader(new FileReader(file));


                        while ((line = br.readLine()) != null) {
                            text.append(line);
                            text.append('\n');
                        }
                    }
                    catch (IOException e) {

                    }
                    mlyrics.setText(text.toString().replace("�p�","").replace("�P�","").replace("�","")
                            .replace("1","").replace("2","").replace("3","").replace("4","").replace("5","")
                            .replace("6","").replace("7","").replace("8","").replace("9","").replace("0","")
                            .replace("/","\n").replace(">","").replace("<","").replace("@","").replace("&","")
                            .replace("+","").replace("-","").replace("*","").replace("=","").replace("^","")
                            .replace("!","").replace("(","").replace(")","").replace("#","").replace(a,"\n\n")
                            .replace("$",""));
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"dont exist",Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.btnprev:
                mp.stop();
                mp.release();
                mScroller.startScroll(0,0,0,2000,280000);
                position=(position-1<0)?mySongs.size()-1: position-1;
                if(position-1<0)
                {
                    position=mySongs.size()-1;
                }
                else{
                    position=position-1;}
                uri= Uri.parse(mySongs.get(position).toString());
                mp= MediaPlayer.create(getApplicationContext(),uri);
                mp.start();
                video.start();

                file = new File(""+uri);
                if(file.exists())
                {
                    text = new StringBuilder();

                    try {
                    br = new BufferedReader(new FileReader(file));


                        while ((line = br.readLine()) != null) {
                            text.append(line);
                            text.append('\n');
                        }
                    }
                    catch (IOException e) {

                    }
                    mlyrics.setText(text.toString().replace("�p�","").replace("�P�","").replace("�","")
                            .replace("1","").replace("2","").replace("3","").replace("4","").replace("5","")
                            .replace("6","").replace("7","").replace("8","").replace("9","").replace("0","")
                            .replace("/","\n").replace(">","").replace("<","").replace("@","").replace("&","")
                            .replace("+","").replace("-","").replace("*","").replace("=","").replace("^","")
                            .replace("!","").replace("(","").replace(")","").replace("#","").replace(a,"\n\n")
                            .replace("$",""));
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"dont exist",Toast.LENGTH_SHORT).show();
                }

                break;

        }

    }

    public  class CountDownT extends CountDownTimer
    {

        public CountDownT(long MilliSeconds,long TimeGap)
        {
            super(MilliSeconds,TimeGap);
        }
        @Override
        public void onTick(long l)
        {
showtimer.setText((l/1000)+"");
        }

        public void onFinish()
        {
            showtimer.setText("END TIMER");
            mp.stop();
            Intent intent=new Intent(MainActivity.this,WelcomeActivity.class);
           // finish();
            startActivity(intent);

        }

    }

}
