package com.whf.sounddemo;

import android.media.MediaPlayer;
import android.media.MediaRecorder;

import java.io.IOException;

/**
 *
 * 使MediaRecoder进行音频录制、MedioPlayer进行播放
 * Created by WHF on 2017/3/7.
 */

public class MediaManager {

    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;

    private int mEncoder;
    private int mOutputFormat;

    public MediaManager() {
        mOutputFormat = MediaRecorder.OutputFormat.AAC_ADTS;
        mEncoder = MediaRecorder.AudioEncoder.AAC;
    }

    public void initRecoder() {
        try {
            mRecorder = new MediaRecorder();
            //设置声音源 语音识别：VOICE_RECOGNITION 语音通话：VOICE_COMMUNICATION(回音消除、噪音拟制)
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //文件容器，设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
            //，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
            mRecorder.setOutputFormat(mOutputFormat);
            //设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样
            mRecorder.setAudioEncoder(mEncoder);

            //所有Android系统都支持的采样频率
            mRecorder.setAudioSamplingRate(44100);
            //较好的一个比特率
            mRecorder.setAudioEncodingBitRate(96000);

            mRecorder.setOutputFile(Utils.getNewFilePath(mOutputFormat,0));
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void startRecord() {
        if (mRecorder == null) {
            return;
        }
        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
        }

    }

    public void stopRecord() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public void initPlayer(String soundPath) {
        mPlayer = new MediaPlayer();
        mPlayer.reset();
        mPlayer.setOnCompletionListener(new OnCompletListener());
        try {
            mPlayer.setDataSource(soundPath);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void resetPlayer() {
        if (mPlayer != null) {
            mPlayer.reset();
            mPlayer.release();
            mPlayer = null;
        }
    }

    private class OnCompletListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            resetPlayer();
        }
    }


    public void setEncoder(int encoder) {
        this.mEncoder = encoder;
    }

    public void setOutputFormat(int outputFormat) {
        this.mOutputFormat = outputFormat;
    }
}
