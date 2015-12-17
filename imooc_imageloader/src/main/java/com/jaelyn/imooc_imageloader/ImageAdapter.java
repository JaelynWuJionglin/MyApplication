package com.jaelyn.imooc_imageloader;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uti.ImageLoader;

/**
 * Created by Jaelyn on 2015/12/10.
 */
public class ImageAdapter extends BaseAdapter {

    private static Set<String> mSeletedImg = new HashSet<String>();

    private String mDirPath;
    private List<String> mImgPaths;
    private LayoutInflater mInflater;

    public ImageAdapter(Context context, List<String> mDatas, String dirPath) {
        this.mDirPath = dirPath;
        this.mImgPaths = mDatas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mImgPaths.size();
    }

    @Override
    public Object getItem(int position) {
        return mImgPaths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.gridview_item,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.mImg = (ImageView) convertView.findViewById(R.id.id_item_image);
            viewHolder.mSelect = (ImageButton) convertView.findViewById(R.id.id_item_select);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        //重置状态
        viewHolder.mImg.setImageResource(R.drawable.bg);
        viewHolder.mSelect.setImageResource(R.drawable.usual);
        //viewHolder.mImg.setColorFilter(null);

        //加载图片
        ImageLoader.getmInstance(3, ImageLoader.Type.LIFO).loadImage(
                mDirPath+"/"+mImgPaths.get(position),viewHolder.mImg
        );

        final String filePath = mDirPath+"/"+mImgPaths.get(position);
        viewHolder.mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //已经被选择
                if (mSeletedImg.contains(filePath)){
                    mSeletedImg.remove(filePath);
                    viewHolder.mImg.setColorFilter(null);
                    viewHolder.mSelect.setImageResource(R.drawable.usual);

                }else {//未被选择
                    mSeletedImg.add(filePath);
                    viewHolder.mImg.setColorFilter(Color.parseColor("#77000000"));
                    viewHolder.mSelect.setImageResource(R.drawable.selected);
                }
                //notifyDataSetChanged();//会闪屏
            }
        });

        if (mSeletedImg.contains(filePath)){
            viewHolder.mImg.setColorFilter(Color.parseColor("#77000000"));
            viewHolder.mSelect.setImageResource(R.drawable.selected);
        }
        return convertView;
    }

    private class ViewHolder{
        ImageView mImg;
        ImageButton mSelect;
    }
}
