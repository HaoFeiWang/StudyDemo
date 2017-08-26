package com.whf.sounddemo;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private Button mRecoderButton,mAudioButton;
    private RecyclerView mSoundRecyclerView;
    private MediaManager mMediaManager;
    private AudioRecordManager mAudioRecordManager;
    private SoundAdapter mSoundAdapter;
    private ArrayList<Sound> mSoundArrayList = new ArrayList<>();

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMediaManager = new MediaManager();
        mAudioRecordManager =new AudioRecordManager();
        mRecoderButton = (Button) findViewById(R.id.media_button);
        mAudioButton = (Button) findViewById(R.id.audio_button);
        mRecoderButton.setOnTouchListener(this);
        mAudioButton.setOnTouchListener(this);
        initRecyclerView();
    }

    private void initRecyclerView(){
        mSoundRecyclerView = (RecyclerView) findViewById(R.id.sound_list);
        mSoundRecyclerView.setLayoutManager(new LinearLayoutManager(this
                ,LinearLayoutManager.VERTICAL,false));
        mSoundAdapter = new SoundAdapter(mSoundArrayList,this);

        mSoundAdapter.addItemClickListener(new SoundAdapter.OnRecyclerItemClickLinstener() {
            @Override
            public void onItemClick(View view, Sound sound) {
                if(sound.getName().substring(sound.getName().lastIndexOf(".")).equals(".pcm")){
                    AudioTrackManager manager = new AudioTrackManager();
                    manager.initAudioTrack();
                    manager.playAudioTrack(sound);
                }else{
                    mMediaManager.resetPlayer();
                    mMediaManager.initPlayer(sound.getPath());
                }
            }
        });

        mSoundAdapter.addItemMenuClickListener(new SoundAdapter.OnItemMenuClickLinstener() {
            @Override
            public void onItemMenuClick(View view, Sound sound) {
                showItemMenu(sound);
            }
        });

        mSoundRecyclerView.setAdapter(mSoundAdapter);
        mSoundRecyclerView.addItemDecoration(new Decoration(this));
        initData();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v.getId() == R.id.media_button){
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                mMediaManager.initRecoder();
                mMediaManager.startRecord();
            }else if(event.getAction() == MotionEvent.ACTION_UP){
                mMediaManager.stopRecord();
                initData();
            }
        }else if(v.getId() == R.id.audio_button){
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                mAudioRecordManager.initAudioRecord();
                mAudioRecordManager.startRecord();
            }else if(event.getAction() == MotionEvent.ACTION_UP){
                mAudioRecordManager.stopRecord();
                initData();
            }
        }

        return true;
    }

    public void initData(){
        new AsyncTask<Void,Void,ArrayList<Sound>>() {
            @Override
            protected ArrayList<Sound> doInBackground(Void... params) {
                return Utils.getSoundList();
            }

            @Override
            protected void onPostExecute(ArrayList<Sound> sounds) {
                super.onPostExecute(sounds);
                mSoundAdapter.setSoundList(sounds);
            }
        }.execute();
    }

    public void deletSound(final Sound sound){
        new AsyncTask<Void,Void,ArrayList<Sound>>() {
            @Override
            protected ArrayList<Sound> doInBackground(Void... params) {
                if (sound!=null){
                    Utils.deleteFile(sound.getPath());
                }
                return Utils.getSoundList();
            }

            @Override
            protected void onPostExecute(ArrayList<Sound> sounds) {
                super.onPostExecute(sounds);
                mSoundAdapter.setSoundList(sounds);
            }
        }.execute();
    }

    public void showItemMenu(final Sound sound){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选项");
        builder.setItems(R.array.menu_item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        deletSound(sound);
                        break;
                    case 1:
                        break;
                }
            }
        });
        builder.show();
    }
}
