package com.itproject.android.androidtvsample;

import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
//    ListView lv;
//    String Items[];
    CountDownT timer;
    VideoView mVv;
    Firebase rootref;
    TextView mtxtcommand,showtimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootref=new Firebase("https://songtogo-f2eae.firebaseio.com/NotificationRequest");
        mVv=(VideoView) findViewById(R.id.videoViewtest);
        mtxtcommand=(TextView) findViewById(R.id.txtcommand);
        showtimer=(TextView) findViewById(R.id.txttimer);
        timer=new CountDownT(10000,1000);
        showtimer.setText("10");
        mVv.setVideoURI(Uri.parse("android.resource://com.itproject.android.androidtvsample/"+R.raw.ikawlamang));

        Firebase ref=rootref.child("notification");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            String m=dataSnapshot.getValue(String.class);
                mtxtcommand.setText(m);

                if(mtxtcommand.getText().equals("PLAY"))
                {
                    mVv.start();
                }
                else if(mtxtcommand.getText().equals("PAUSE"))
                {
                    mVv.pause();
                }
                else if(mtxtcommand.getText().equals("STOP")){
                    mVv.pause();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



//    lv=(ListView) findViewById(R.id.listView);
//
//
//        final ArrayList<File> myVids = findVids(Environment.getExternalStorageDirectory());
//        Items=new String[myVids.size()];
//
//        for(int i=0 ; i < myVids.size() ; i++)
//        {
//
//            Items[i]=myVids.get(i).getName().toString();
//        }
//
//ArrayAdapter<String> adp= new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,Items);
//lv.setAdapter(adp);
//
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                startActivity(new Intent(getApplicationContext(),Player.class).putExtra("pos",position).putExtra("videolist",myVids));
//            }
//        });
//    }
//
//    public ArrayList<File> findVids(File root)
//    {
//        ArrayList<File> arrayl=new ArrayList<File>();
//        File[] files=root.listFiles();
//
//        for(File singlefile : files)
//        {
//            if(singlefile.isDirectory() && !singlefile.isHidden())
//            {
//   arrayl.addAll(findVids(singlefile));
//            }
//            else {
//            if(singlefile.getName().endsWith(".mp3") || singlefile.getName().endsWith(".kar"))
//                {
//                    arrayl.add(singlefile);
//                }
//            }
//            }
//return arrayl;
//        }
//    public void toast(String text)
//    {
//        Toast.makeText(getApplicationContext(),text, Toast.LENGTH_SHORT).show();
//  }

    }

    public void start(View view)
    {
timer.start();
    }

    public void stop(View view)
    {
timer.cancel();
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
        }

    }

}
