package com.example.photohandle;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.example.photohandle.utils.FileUtil;

public class PhotoDetailActivity extends Activity{
   
	private Bitmap bitmap = null;
	private byte[] mContent = null;
	private String id;
	private ImageView iv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_detail);
		iv = (ImageView) findViewById(R.id.iv_photo_detail);
		Intent intent = getIntent();
		 id = intent.getStringExtra("id");
		getImage();
		
	}
	private void getImage(){
		Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().
				  appendPath(id).build();
		ContentResolver resolver = getContentResolver();
		FileUtil file = new FileUtil();
		
		try {
			mContent = file.readInputStream(resolver.openInputStream(Uri.parse(uri.toString())));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bitmap = file.getBitmapFromBytes(mContent, null);
		iv.setImageBitmap(bitmap);
	}
}
