package com.whf.sounddemo;

import android.os.Environment;

import java.io.File;

/**
 * Created by WHF on 2017/3/7.
 */

public class Constant {
    public static final String REC_DIR_NAME = Environment.getExternalStorageDirectory()
            +File.separator + "SoundDemo" + File.separator + "Rec";
}
