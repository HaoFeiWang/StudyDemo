package com.whf.sounddemo;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 使用AudioRecord进行音频录制，字节流录制
 * Created by WHF on 2017/3/9.
 */

public class AudioRecordManager {
    private AudioRecord mAudioRecord;
    private volatile boolean isRecording = false;
    private int mBuffSize;
    private static final int BUFFER_SIZE = 2048;

    public AudioRecordManager() {

    }

    public void initAudioRecord() {
        mBuffSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        mBuffSize = Math.max(mBuffSize,BUFFER_SIZE);
        //参数列表
        //声音源、采样频率、声道数、位宽、缓冲区大小（为一帧大小的2-N倍，常用getMinBufferSize()取得）
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100
                , AudioFormat.CHANNEL_IN_MONO
                , AudioFormat.ENCODING_PCM_16BIT,
                mBuffSize);

    }

    public void startRecord() {
        mAudioRecord.startRecording();
        isRecording = true;
        new Thread(new AudioCaptureRunnable()).start();
    }

    public void stopRecord(){
        mAudioRecord.stop();
        isRecording = false;
        mAudioRecord.release();
        mAudioRecord = null;
    }

    private class AudioCaptureRunnable implements Runnable{
        @Override
        public void run() {
            long data = System.currentTimeMillis();
            File file = new File(Utils.getNewFilePath(0x99,data));
            FileOutputStream outputStream = null;
            byte[] buffer = new byte[mBuffSize];
            try {
                outputStream = new FileOutputStream(file,true);
                while (isRecording) {
                    int ret = mAudioRecord.read(buffer, 0, mBuffSize);
//                if (ret == AudioRecord.ERROR_INVALID_OPERATION) {
//                    Log.e(":::::::", "ERROR_INVALID_OPERATION");
//                } else if (ret == AudioRecord.ERROR_BAD_VALUE) {
//                    Log.e(":::::::", "Error ERROR_BAD_VALUE");
//                }else {
//                    Utils.saveBuffer(buffer,data,ret);
//                }
                    if(ret!=-1){
                        outputStream.write(buffer,0,ret);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
