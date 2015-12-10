package com.example.photohandle.utils;

import java.util.List;

import com.example.photohandle.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoAdapter extends BaseAdapter{
   
	private Context context;
	private List<PhoteBean>list;

	
	public PhotoAdapter(Context context,List<PhoteBean>list){
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
        ViewHolder holder=null;
        if(convertView==null){
        	holder = new ViewHolder();
        	convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_main, null);
        	holder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
        	holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
        	convertView.setTag(holder);
        }else {
			
        	holder= (ViewHolder) convertView.getTag();
		}

        Bitmap bitmap = Thumbnails.getThumbnail(context.getContentResolver(), Integer.parseInt(list.get(position).getId()), Thumbnails.MICRO_KIND, null);
        holder.iv_head.setImageBitmap(bitmap);
      holder.tv_title.setText(list.get(position).getDisplayname());
		return convertView;
	}
   class ViewHolder{
	   private ImageView iv_head;
	   private TextView tv_title;
   }
}
