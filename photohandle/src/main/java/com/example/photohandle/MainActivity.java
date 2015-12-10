package com.example.photohandle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.photohandle.utils.PhoteBean;
import com.example.photohandle.utils.PhotoAdapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;


public class MainActivity extends Activity{

	private ListView lv_main;
	private Button btn;
	private static final String[]STORE_IMAGES={
		MediaStore.Images.Media.DISPLAY_NAME,
		MediaStore.Images.Media.LATITUDE,
		MediaStore.Images.Media.LONGITUDE,
		MediaStore.Images.Media._ID,
		MediaStore.Images.Media.BUCKET_ID,
		MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
			};
	
	private List<PhoteBean>list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lv_main = (ListView) findViewById(R.id.lv_main);
		btn = (Button) findViewById(R.id.bt);
		list = getPhotos();
		PhotoAdapter adapter = new PhotoAdapter(getApplicationContext(), list);
		lv_main.setAdapter(adapter);
		lv_main.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				// TODO Auto-generated method stub
				String id = list.get(position).getId();
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), PhotoDetailActivity.class);
			
				intent.putExtra("id", id);
				startActivity(intent);
			}
		});
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), ShowAllPhotoActivity.class);
				startActivity(intent);
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
			//String imgid =cursor.getString(6);
			photo = new PhoteBean();
			photo.setDisplayname(displayname);
			photo.setId(id);
			//photo.setImageid(imgid);
			list.add(photo);
		}
		cursor.close();
		return list;
	}

}
