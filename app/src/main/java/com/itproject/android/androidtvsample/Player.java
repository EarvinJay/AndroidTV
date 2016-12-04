//package com.itproject.android.androidtvsample;
//
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.MediaController;
//import android.widget.SeekBar;
//import android.widget.VideoView;
//
//import java.io.File;
//import java.util.ArrayList;
//
//public class Player extends AppCompatActivity implements View.OnClickListener {
//
//    MediaController mediaController;
//    static MediaPlayer mp;
//    VideoView video;
//    Uri uri;
//    int position;
//    SeekBar sb;
//    Button btnforward,btnbackward,btnnext,btnprev,btnpause;
//    ArrayList<File> myVids;
//    Thread updateSeekbar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_player);
//
//        btnpause=(Button) findViewById(R.id.btnpause);
//        btnbackward=(Button) findViewById(R.id.btnBackward);
//        btnforward=(Button) findViewById(R.id.btnForward);
//        btnnext=(Button) findViewById(R.id.btnNext);
//        btnprev=(Button) findViewById(R.id.btnPrev);
//        video=(VideoView) findViewById(R.id.videoView);
//
//        btnpause.setOnClickListener(this);
//        btnbackward.setOnClickListener(this);
//        btnforward.setOnClickListener(this);
//        btnnext.setOnClickListener(this);
//        btnprev.setOnClickListener(this);
//
//        mediaController= new MediaController(this);
//        mediaController.setAnchorView(video);
//
//
//        sb=(SeekBar) findViewById(R.id.seekBar);
//        updateSeekbar= new Thread()
//        {
//            @Override
//            public void run() {
//                int totalduration=mp.getDuration();
//                int currentposition=0;
//
//                while(currentposition < totalduration)
//                {
//                    try{
//                            sleep(500);
//                        currentposition=mp.getCurrentPosition();
//                        sb.setProgress(currentposition);
//                    }
//                    catch (InterruptedException e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
//
//               // super.run();
//            }
//        };
//
//
//        if(mp!=null)
//        {
//            mp.stop();
//            mp.release();
//
//
//        }
//
//
//        Intent i=getIntent();
//       Bundle b=i.getExtras();
//
//        myVids= (ArrayList) b.getParcelableArrayList("videolist");
//         position= b.getInt("pos");
//
//         uri= Uri.parse(myVids.get(position).toString());
//
////        video.setMediaController(mediaController);
////        video.setVideoURI(uri);
////        video.requestFocus();
//        mp= MediaPlayer.create(getApplicationContext(),uri);
//        mp.start();
//        //video.start();
//
//        sb.setMax(mp.getDuration());
//        updateSeekbar.start();
//
//        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//            mp.seekTo(seekBar.getProgress());
//            }
//        });
//    }
//
//    @Override
//    public void onClick(View v) {
//        int id =v.getId();
//        switch(id)
//        {
//            case R.id.btnpause:
//                if(mp.isPlaying())
//                {
//                    btnpause.setText("|>");
//                    mp.pause();
//                    video.setMediaController(mediaController);
//                    video.setVideoURI(uri);
//                    video.requestFocus();
//                    video.stopPlayback();
//
//                }
//                else {
//                    btnpause.setText("||");
//
//                    video.setMediaController(mediaController);
//                    video.setVideoURI(uri);
//                    video.requestFocus();
//                   // video.resume();
//                    mp.start();
//
//                }
//                break;
//
//            case R.id.btnForward:
//                mp.seekTo(mp.getCurrentPosition()+5000);
//                break;
//
//            case R.id.btnBackward:
//                mp.seekTo(mp.getCurrentPosition()-5000);
//                break;
//
//            case R.id.btnNext:
//                mp.stop();
//                mp.release();
//
//                position=(position+1)%myVids.size();
//                 uri= Uri.parse(myVids.get(position).toString());
//                mp= MediaPlayer.create(getApplicationContext(),uri);
//                video.setMediaController(mediaController);
//                video.setVideoURI(uri);
//                video.requestFocus();
//            //    video.start();
//                mp.start();
//                sb.setMax(mp.getDuration());
//
//                break;
//
//            case R.id.btnPrev:
//                mp.stop();
//                mp.release();
//
//                position=(position-1<0)?myVids.size()-1: position-1;
////                if(position-1<0)
////                {
////                    position=myVids.size()-1;
////                }
////                else{
////                position=position-1;}
//                 uri= Uri.parse(myVids.get(position).toString());
//                mp= MediaPlayer.create(getApplicationContext(),uri);
//                video.setMediaController(mediaController);
//                video.setVideoURI(uri);
//                video.requestFocus();
//                //video.start();
//            mp.start();
//                sb.setMax(mp.getDuration());
//                break;
//
//        }
//    }
//}
