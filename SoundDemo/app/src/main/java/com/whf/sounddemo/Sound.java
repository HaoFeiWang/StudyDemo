package com.whf.sounddemo;

/**
 * Created by WHF on 2017/3/7.
 */
public class Sound {
    private String mName;
    private String mPath;
    private String mType;

    public Sound() {
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        this.mPath = path;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }
}
