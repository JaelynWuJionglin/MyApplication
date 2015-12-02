package com.example.jaelyn.download_demo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jaelyn.entities.FileInfo;

import java.util.List;


/**
 * ListView适配器
 * Created by Jaelyn on 2015/12/3.
 */
public class FilelistAdapter extends BaseAdapter{

    private Context context;
    private List<FileInfo> mFileList = null;

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
