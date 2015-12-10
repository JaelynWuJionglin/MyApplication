package com.example.photohandle.utils;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;

public class ShowAllPhotosAdapter extends BaseAdapter{
   
	private Context context;
	private List<PhoteBean>list;
	private int selectItem;
	
	public ShowAllPhotosAdapter(Context context,List<PhoteBean>list){
		this.context = context;
		this.list = list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PhotoGridItem item=null;
        if(convertView==null){
        	item = new PhotoGridItem(context);
        	item.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        }else {
			item = (PhotoGridItem) convertView;
        	
		}
      Bitmap bitmap = Thumbnails.getThumbnail(context.getContentResolver(), Integer.parseInt(list.get(position).getId()), Thumbnails.MICRO_KIND, null);
      item.SetBitmap(bitmap);
      boolean flag = list.get(position).isSelect();
      item.setChecked(flag);
      return item;
	}
   
}
