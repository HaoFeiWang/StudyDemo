package com.whf.sounddemo;

import android.media.*;
import android.media.AudioManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * PCM数据的播放，字节流播放
 * Created by WHF on 2017/3/11.
 */
public class AudioTrackManager {

    private AudioTrack mAudioTrack;
    private int mBuffer = 2048;
    private boolean isPlaying = false;
    private String path;


    public void initAudioTrack() {

        mBuffer = Math.max(mBuffer, AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO
                , AudioFormat.ENCODING_PCM_16BIT));

        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC
                , 44100, AudioFormat.CHANNEL_OUT_MONO
                , AudioFormat.ENCODING_PCM_16BIT
                , mBuffer, AudioTrack.MODE_STREAM);

    }


    public void playAudioTrack(Sound sound) {
        isPlaying = true;
        path = sound.getPath();
        new Thread(new AudioTrackRunnable()).start();
        mAudioTrack.play();
    }

    private class AudioTrackRunnable implements Runnable {

        @Override
        public void run() {
            byte[] data = new byte[mBuffer];
            File file = new File(path);
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
                while (isPlaying) {
                    int len = inputStream.read(data);
                    if (len != -1) {
                        mAudioTrack.write(data, 0, len);
                    } else {
                        isPlaying = false;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


