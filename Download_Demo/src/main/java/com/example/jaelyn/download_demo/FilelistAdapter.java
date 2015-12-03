package com.example.jaelyn.download_demo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Services.DownloadService;
import com.jaelyn.entities.FileInfo;
import java.util.List;


/**
 * ListView适配器
 * Created by Jaelyn on 2015/12/3.
 */
public class FilelistAdapter extends BaseAdapter{

    private Context context;
    private List<FileInfo> mFileList = null;

    public FilelistAdapter(Context context, List<FileInfo> mFileList) {
        this.context = context;
        this.mFileList = mFileList;
    }

    /*
    * getCount方法用于返回需要在ListView上显示的数据的总数，
    * 要让文件集合中的文件显示在列表上，所以返回的是文件集合对象的size（）方法，
    * 返回文件个数
    */
    @Override
    public int getCount() {
        return mFileList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final FileInfo fileInfo = mFileList.get(position);
        //创建视图
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listitem,null);
            //获得布局中的控件
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
            viewHolder.buttont = (Button) convertView.findViewById(R.id.buttont);
            viewHolder.buttonj = (Button) convertView.findViewById(R.id.buttonj);
            viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            convertView.setTag(viewHolder);

            //设置视图中的控件
            viewHolder.textView.setText(fileInfo.getFileName());
            viewHolder.progressBar.setMax(100);
            viewHolder.buttonj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //通过Intent传递参数给Service
                    Intent intent = new Intent(context, DownloadService.class);
                    intent.setAction(DownloadService.ACTION_START);
                    intent.putExtra("fileInfo", fileInfo);
                    context.startService(intent);
                }
            });
            viewHolder.buttont.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //通过Intent传递参数给Service
                    Intent intent = new Intent(context, DownloadService.class);
                    intent.setAction(DownloadService.ACTION_STOP);
                    intent.putExtra("fileInfo", fileInfo);
                    context.startService(intent);
                }
            });
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //设置进度条
        viewHolder.progressBar.setProgress(fileInfo.getFinished());
        return convertView;
    }

    /*
    * 更新列表项中的进度条
    */
    public void updateProgress(int id ,int progres){
        FileInfo fileInfo = mFileList.get(id);
         /*
          * 因为在上面的getView方法中已经设置了
          * viewHolder.progressBar.setProgress(fileInfo.getFinished());
          * 所以可以这样设置进度条进度
          */
        fileInfo.setFinished(progres);
        notifyDataSetChanged();//刷新ListView
    }


    /*
     *ViewHolder就是一个零食的储存器，把每次getView方法中每次返回的View缓存起来，可以下次再用
      * 这样做的好处就是不必 每次都去布局文件中查找控件
     */
    static class ViewHolder{
        TextView textView;
        ProgressBar progressBar;
        Button buttont,buttonj;
    }
}
























