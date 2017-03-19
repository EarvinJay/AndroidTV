package com.itproject.android.androidtvsample;


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.itproject.android.androidtvsample.lyriker.LyricsFileExtractor;
import com.itproject.android.androidtvsample.lyriker.ParaMuKanta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    VideoView video;
    Uri uri, txturi;
    Runnable play;
    Handler handlerplay = new Handler();
    int position, duration, totaldelay;
    static MediaPlayer mp;
    ArrayList<File> mySongs;
    Button mbtnplay, mbtnprev, mbtnnext;
    CountDownT timer;
    Firebase rootref;
    TextView mtxtcommand, showtimer;
    File file;
    TextView mlyrics;
    StringBuilder text;
    BufferedReader br;
    String line, a = "\\", tex;
    String nofhours, uname;
    String[] phraseplay;
    String[] syllableplay;
    Scroller mScroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent i = getIntent();
        nofhours = i.getStringExtra("HOURS");
        uname = i.getStringExtra("UNAME");
        Bundle b = i.getExtras();
        mySongs = (ArrayList) b.getParcelableArrayList("songlist");
        position = b.getInt("pos", 0);

        mlyrics = (TextView) findViewById(R.id.txtlyrics);
//        mlyrics.setSelected(true);
//        mlyrics.setMovementMethod(new ScrollingMovementMethod());
//        mlyrics.setEllipsize(TextUtils.TruncateAt.MARQUEE);
//        mScroller=new Scroller(MainActivity.this,new LinearInterpolator());
//        mlyrics.setScroller(mScroller);
//        mScroller.startScroll(0,0,0,2000,280000);
        video = (VideoView) findViewById(R.id.backgroundvideo);
        mbtnplay = (Button) findViewById(R.id.btnplay);
        mbtnprev = (Button) findViewById(R.id.btnprev);
        mbtnnext = (Button) findViewById(R.id.btnnext);

        mbtnplay.setOnClickListener(this);
        mbtnprev.setOnClickListener(this);
        mbtnnext.setOnClickListener(this);

        if (mp != null) {
            mp.stop();
            mp.release();

        }

        uri = Uri.parse(mySongs.get(position).toString());
        txturi = Uri.parse(mySongs.get(position).toString().replace(".kar", ".txt"));
        video.setVideoURI(Uri.parse("android.resource://com.itproject.android.androidtvsample/" + R.raw.karaokebg));
        mp = MediaPlayer.create(getApplicationContext(), uri);
        video.start();
        //duration=mp.getDuration();
        //Toast.makeText(MainActivity.this,mp.getDuration()+"",Toast.LENGTH_SHORT).show();
        mp.start();

        final ParaMuKanta pmk = LyricsFileExtractor.readTextFile(txturi + "");

        handlerplay.post( play=new Runnable()
            {

            SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss.SSS");
            int x = 0;
            int currentLyric = 0;
                int mins=0;

                public void run() {
                    try {
                    mlyrics.setText(pmk.getLyrics().get(currentLyric).replace("/","").replace("\\",""));
                        x++;
                            if (x == sdf.parse(pmk.getTiming().get(currentLyric)).getSeconds()) {
                                currentLyric++;
                            }
                            else if (x == 60) {
                                x=0;
                                mins++;
                            }
                       handlerplay.postDelayed(play, 1000);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    catch (IndexOutOfBoundsException e) {
                     finish();
                    }
                }
        });




        rootref=new Firebase("https://songtogo-f2eae.firebaseio.com/users/"+uname+"/NotificationRequest");
        mtxtcommand=(TextView) findViewById(R.id.txtcommand);
        showtimer=(TextView) findViewById(R.id.txttimer);
        timer=new CountDownT(20000,1000);



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

                else if(mtxtcommand.getText().equals("Out"))
                {


//                    startActivity(new Intent(getApplicationContext(),WelcomeActivity.class));
                    mp.stop();
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }

                else if(mtxtcommand.getText().equals("PREVIOUS")){
                    mp.stop();
                    mp.release();
                    video.stopPlayback();
                    video.suspend();
                    handlerplay.removeCallbacks(play);
                   // mScroller.startScroll(0,0,0,2000,280000);
                    position=(position-1<0)?mySongs.size()-1: position-1;
                    if(position-1<0)
                    {
                        position=mySongs.size()-1;
                    }
                    else{
                        position=position-1;}
                    uri= Uri.parse(mySongs.get(position).toString());
                    txturi = Uri.parse(mySongs.get(position).toString().replace(".kar", ".txt"));
                    mp= MediaPlayer.create(getApplicationContext(),uri);
                    mp.start();
                    video.start();

                    final ParaMuKanta pmk = LyricsFileExtractor.readTextFile(txturi + "");

                    handlerplay.post( play=new Runnable()
                    {

                        SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss.SSS");
                        int x = 0;
                        int currentLyric = 0;
                        int mins=0;

                        public void run() {
                            try {
                                mlyrics.setText(pmk.getLyrics().get(currentLyric).replace("/","").replace("\\",""));
                                x++;
                                if (x == sdf.parse(pmk.getTiming().get(currentLyric)).getSeconds()) {
                                    currentLyric++;
                                }
                                else if (x == 60) {
                                    x=0;
                                    mins++;
                                }
                                handlerplay.postDelayed(play, 1000);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            catch (IndexOutOfBoundsException e) {
                                finish();
                            }
                        }
                    });


                }

                else if(mtxtcommand.getText().equals("NEXT")){
                    mp.stop();
                    mp.release();
                    video.suspend();
                    video.stopPlayback();
                    handlerplay.removeCallbacks(play);
                   // mScroller.startScroll(0,0,0,2000,280000);
                    position=(position+1)%mySongs.size();
                    uri= Uri.parse(mySongs.get(position).toString());
                    txturi = Uri.parse(mySongs.get(position).toString().replace(".kar", ".txt"));
                    mp= MediaPlayer.create(getApplicationContext(),uri);
                    mp.start();
                    video.start();

                    final ParaMuKanta pmk = LyricsFileExtractor.readTextFile(txturi + "");

                    handlerplay.post( play=new Runnable()
                    {

                        SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss.SSS");
                        int x = 0;
                        int currentLyric = 0;
                        int mins=0;

                        public void run() {
                            try {
                                mlyrics.setText(pmk.getLyrics().get(currentLyric).replace("/","").replace("\\",""));
                                x++;
                                if (x == sdf.parse(pmk.getTiming().get(currentLyric)).getSeconds()) {
                                    currentLyric++;
                                }
                                else if (x == 60) {
                                    x=0;
                                    mins++;
                                }
                                handlerplay.postDelayed(play, 1000);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            catch (IndexOutOfBoundsException e) {
                                finish();
                            }
                        }
                    });



                }


                else if(mtxtcommand.getText().equals("BACK")){
                    Intent i=new Intent(getApplicationContext(),ListActivity.class);
                    i.putExtra("UNAME",uname);
                    i.putExtra("HOURS",nofhours);
                    startActivity(i);
                    finish();
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
              //  mScroller.startScroll(0,0,0,2000,280000);
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
                    tex=text.toString().substring(text.indexOf("\\")+1);
                    tex=tex.replace("MTrk","˜");
                    StringTokenizer tokens = new StringTokenizer(tex, "˜");
                    tex= tokens.nextToken().toString();
                    tex=tex.toString().replace("/","\n").replace("�p�","").replace("�P�","")
                            .replace("�`�","").replace("�X�","").replace("x�","").replace("�@�","").replace("�h�","")
                            .replace("�$�","").replace("�J�","").replace("�4�","").replace("�0�","").replace("�(�","").replace("�|�","")
                            .replace("�l�","").replace("�t�","").replace("�D�","").replace("�L�","").replace("�d�","").replace("�j�","")
                            .replace("�,�","").replace("�&�","").replace("�!�","").replace("�*�","").replace("�K�","").replace("�n�","")
                            .replace("�:�","").replace("�A�","").replace("�%�","").replace("�3�","").replace("�a�","").replace("�~�","")
                            .replace("Z�","").replace("<�","").replace("`�","").replace("|�","").replace("T�","").replace("H�","").replace("X�","")
                            .replace("8�","").replace("9�","").replace("=�","").replace("Y�","").replace(";�","").replace("{�","").replace("v�","")
                            .replace("z�","").replace("w�","").replace("y�","").replace("[�","").replace("]�","").replace("[�","").replace("^�","")
                            .replace("c�","").replace("1�","").replace("_�","").replace("0�","").replace("b�","").replace("2�","")
                            .replace("�","")
                            .replace(" ","");




//                    handlerplay.post( new Runnable(){
//                        private int n = 0;
//
//                        public void run() {
//                        phraseplay = tex.split("\\\\");
//                            mlyrics.setText(phraseplay[n]);
//                            n++;
//                            if( n < phraseplay.length)
//                            {
//                                handlerplay.postDelayed(this, 5000);
//                            }
//
//                        }
 //                   });
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"dont exist",Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.btnprev:
                mp.stop();
                mp.release();
              //  mScroller.startScroll(0,0,0,2000,280000);
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
                    tex=text.toString().substring(text.indexOf("\\")+1);
                    tex=tex.replace("MTrk","˜");
                    StringTokenizer tokens = new StringTokenizer(tex, "˜");
                    tex= tokens.nextToken().toString();
                    tex=tex.toString().replace("/","\n").replace("�p�","").replace("�P�","")
                            .replace("�`�","").replace("�X�","").replace("x�","").replace("�@�","").replace("�h�","")
                            .replace("�$�","").replace("�J�","").replace("�4�","").replace("�0�","").replace("�(�","").replace("�|�","")
                            .replace("�l�","").replace("�t�","").replace("�D�","").replace("�L�","").replace("�d�","").replace("�j�","")
                            .replace("�,�","").replace("�&�","").replace("�!�","").replace("�*�","").replace("�K�","").replace("�n�","")
                            .replace("�:�","").replace("�A�","").replace("�%�","").replace("�3�","").replace("�a�","").replace("�~�","")
                            .replace("Z�","").replace("<�","").replace("`�","").replace("|�","").replace("T�","").replace("H�","").replace("X�","")
                            .replace("8�","").replace("9�","").replace("=�","").replace("Y�","").replace(";�","").replace("{�","").replace("v�","")
                            .replace("z�","").replace("w�","").replace("y�","").replace("[�","").replace("]�","").replace("[�","").replace("^�","")
                            .replace("c�","").replace("1�","").replace("_�","").replace("0�","").replace("b�","").replace("2�","")
                            .replace("�","")
                            .replace(" ","");




//                    handlerplay.post( new Runnable(){
//                        private int o = 0;
//
//                        public void run() {
//                            phraseplay = tex.split("\\\\");
//                            mlyrics.setText(phraseplay[o]);
//                            o++;
//                            if( o < phraseplay.length)
//                            {
//                                handlerplay.postDelayed(this, 5000);
//                            }
//
//                        }
//                    });
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
