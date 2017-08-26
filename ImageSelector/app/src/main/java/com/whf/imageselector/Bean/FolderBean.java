package com.whf.imageselector.Bean;

/**
 * Created by WHF on 2016/6/3.
 */
public class FolderBean {
    /**
     * 当前文件夹的路径
     */
    private String dirPath;
    /**
     * 当前文件夹下第一张图片的路径
     */
    private String firstImgPath;
    /**
     * 当前文件夹的名称
     */
    private String name;
    /**
     * 当前文件夹下图片个数
     */
    private int count;

    public String getDirPath() {
        return dirPath;
    }

    public String getName() {
        return name;
    }

    public String getFirstImgPath() {
        return firstImgPath;
    }

    public int getCount() {
        return count;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
        int lastIndexOf=this.dirPath.lastIndexOf("/");
        this.name=this.dirPath.substring(lastIndexOf);
    }

    public void setFirstImgPath(String firstImgPath) {
        this.firstImgPath = firstImgPath;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
