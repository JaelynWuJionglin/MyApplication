package bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaelyn on 2015/12/10.
 */
public class FolderBean {

    private String dir;//当前文件夹的路径
    private String firstImgPath;//第一张图片的路径
    private String name;
    private int count;

    /*public FolderBean() {

    }*/

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;

        int lastIndexOf = this.dir.lastIndexOf("/")+1;
        this.name = this.dir.substring(lastIndexOf);
    }

    public String getFirstImgPath() {
        return firstImgPath;
    }

    public void setFirstImgPath(String firstImgPath) {
        this.firstImgPath = firstImgPath;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
