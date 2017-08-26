package com.whf.sounddemo;

import android.media.AudioFormat;
import android.media.MediaRecorder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by WHF on 2017/3/7.
 */

public class Utils {

    private static int audioFormat = 0x99;

    public static String getNewFilePath(int format,long dateMillis) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        Date date;
        if(dateMillis == 0){
            date = new Date(System.currentTimeMillis());
        }else{
            date = new Date(dateMillis);
        }

        String suffix = "mp3";
        if(format== MediaRecorder.OutputFormat.AAC_ADTS){
            suffix = "aac";
        }else if(format== MediaRecorder.OutputFormat.AMR_NB
                || format== MediaRecorder.OutputFormat.AMR_WB){
            suffix = "amr";
        }else if(format== MediaRecorder.OutputFormat.MPEG_4){
            suffix = "m4a";
        }else if(format == audioFormat){
            suffix = "pcm";
        }

        String fileName = dateFormat.format(date)+"."+ suffix;
        return getDirPath() + File.separator + fileName;

    }

    public static String getDirPath() {
        File file = new File(Constant.REC_DIR_NAME);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }


    public static ArrayList<Sound> getSoundList(){
        ArrayList<Sound> list = new ArrayList<>();
        File dir = new File(Utils.getDirPath());
        Sound sound;
        for(File file: dir.listFiles()){
            sound = new Sound();
            sound.setName(file.getName());
            sound.setPath(file.getAbsolutePath());
            list.add(sound);
        }
        return list;
    }

    public static void deleteFile(String path){
        File file = new File(path);
        if (!file.exists()){
            return;
        }
        file.delete();
    }

    public static void saveBuffer(byte[] buffer,long date,int ret) {
        File file = new File(getNewFilePath(audioFormat,date));
        FileOutputStream outPutStream = null;
        try {
            outPutStream = new FileOutputStream(file,true);
            outPutStream.write(buffer,0,ret);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                outPutStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



}
