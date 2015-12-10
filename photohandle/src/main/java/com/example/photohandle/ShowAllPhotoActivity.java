package com.example.photohandle;

import java.util.ArrayList;
import java.util.List;

import com.example.photohandle.utils.PhoteBean;
import com.example.photohandle.utils.ShowAllPhotosAdapter;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ShowAllPhotoActivity extends Activity {
	
	private GridView gv;
	private ShowAllPhotosAdapter adapter;
	
	private static final String[]STORE_IMAGES={
		MediaStore.Images.Media.DISPLAY_NAME,
		MediaStore.Images.Media.LATITUDE,
		MediaStore.Images.Media.LONGITUDE,
		MediaStore.Images.Media._ID,
		MediaStore.Images.Media.BUCKET_ID,
		MediaStore.Images.Media.BUCKET_DISPLAY_NAME,		
	};
	private  List<PhoteBean>list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_allphoto_activity);
		gv = (GridView) findViewById(R.id.photo_gridview);
		list =getPhotos();
		adapter = new ShowAllPhotosAdapter(getApplicationContext(), list);
		gv.setAdapter(adapter);
		
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				// TODO Auto-generated method stub
				if(list.get(position).isSelect()){
					list.get(position).setSelect(false);
				}else {
					list.get(position).setSelect(true);
					
				}
				adapter.notifyDataSetChanged();
			}
		});
		
	}
	
	
	private List<PhoteBean> getPhotos(){
		List<PhoteBean>list = new ArrayList<PhoteBean>();
		Cursor cursor = MediaStore.Images.Media.query(getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES);
		//Map<String, PhoteBean>countMap = new HashMap<>();
		PhoteBean photo =null;
		while (cursor.moveToNext()) {
			String id = cursor.getString(3);
			String displayname = cursor.getString(0);
			photo = new PhoteBean();
			photo.setDisplayname(displayname);
			photo.setId(id);
			list.add(photo);
		}
		cursor.close();
		return list;
	}
}
