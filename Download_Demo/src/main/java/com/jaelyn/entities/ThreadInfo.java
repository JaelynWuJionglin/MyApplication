package com.jaelyn.entities;
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
}
