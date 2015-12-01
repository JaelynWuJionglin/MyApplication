package com.jaelyn.entities;

import java.io.Serializable;

/**
 * Created by Jaelyn on 2015/12/1.
 * 线程信息
 */
public class ThreadInfo {
    private  int id,start,end,finished;
    private String url;

    public ThreadInfo() {
        super();
    }

    public ThreadInfo(int id, int start, int end, int finished, String url) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.finished = finished;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ThreadInfo{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                ", finished=" + finished +
                ", url='" + url + '\'' +
                '}';
    }

    /**
     * Created by Jaelyn on 2015/12/1.
     * 文件信息
     */
    public static class FileInfo implements Serializable {
        private int id;
        private String url;  //下载链接
        private  String fileName; //文件名
        private  int length; //文件长度
        private  int finished; //下载进度

        public FileInfo() {
            super();
        }

        public FileInfo(int id, String url, String fileName, int length, int finished) {
            this.id = id;
            this.url = url;
            this.fileName = fileName;
            this.length = length;
            this.finished = finished;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public int getFinished() {
            return finished;
        }

        public void setFinished(int finished) {
            this.finished = finished;
        }

        @Override
        public String toString() {
            return "FileInfo{" +
                    "id=" + id +
                    ", url='" + url + '\'' +
                    ", fileName='" + fileName + '\'' +
                    ", length=" + length +
                    ", finished=" + finished +
                    '}';
        }
    }
}
