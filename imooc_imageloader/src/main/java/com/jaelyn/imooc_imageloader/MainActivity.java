package com.jaelyn.imooc_imageloader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bean.FolderBean;
import uti.ListImageDirPopupWindow;

public class MainActivity extends Activity {

    private RelativeLayout mButtonLy;
    private TextView mDirName, mDirCount;

    private File mCurrentDir;
    private int mMaxCount;

    private List<FolderBean> mFolderBean = new ArrayList<FolderBean>();

    private ProgressDialog mProgressDialog;

    private static final int DATA_LOADED = 0x110;

    private GridView mGridView;
    private ImageAdapter mImageAdapter;

    private List<String> mImgs;

    private ListImageDirPopupWindow mDirPopupWindowl;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            if (msg.what == DATA_LOADED) {
                mProgressDialog.dismiss();
                //绑定数据到View中
                data2View();

                initDirPopupWindow();
            }
        }
    };

    private void initDirPopupWindow() {

        mDirPopupWindowl = new ListImageDirPopupWindow(this, mFolderBean);
        mDirPopupWindowl.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });

        mDirPopupWindowl.setOnDirSelectedListener(new ListImageDirPopupWindow.OnDirSelectedListener() {
            @Override
            public void onSeleted(FolderBean folderBean) {
                mCurrentDir = new File(folderBean.getDir());

                mImgs = Arrays.asList(mCurrentDir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        if (filename.endsWith(".jpg")
                                || filename.endsWith(".jepg")
                                || filename.endsWith(".png")) {
                            return true;
                        }
                        return false;
                    }
                }));

                mImageAdapter = new ImageAdapter(MainActivity.this,
                        mImgs,mCurrentDir.getAbsolutePath());
                mGridView.setAdapter(mImageAdapter);

                mDirCount.setText(mImgs.size()+"");
                mDirName.setText(folderBean.getName());
                mDirPopupWindowl.dismiss();

            }
        });
    }

    /**
     * 内容区域变亮
     */
    private void lightOn() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }

    /**
     * 内容区域变暗
     */
    private void lightOf() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = .3f;
        getWindow().setAttributes(lp);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initDatas();
        initEvent();
    }

    private void initView() {

        mGridView = (GridView) findViewById(R.id.id_gridView);
        mButtonLy = (RelativeLayout) findViewById(R.id.id_button_ly);
        mDirName = (TextView) findViewById(R.id.id_dir_name);
        mDirCount = (TextView) findViewById(R.id.id_dir_count);
    }


   //----------------------------------------


    /*
    * 利用ContentProvider扫描手机所有的图片
    * */
    private void initDatas() {

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "当前存储卡不可用", Toast.LENGTH_SHORT).show();
            return;
        }

        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

        new Thread() {
            public void run() {

                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = MainActivity.this.getContentResolver();

                //只查询jpeg和png的图片
                Cursor cursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[] { "image/jpeg", "image/png" }, MediaStore.Images.Media.DATE_MODIFIED);


                Set<String> mDirPaths = new HashSet<String>();


                if(cursor == null){
                    return;
                }

                while (cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    Log.i("text","path---:"+path);
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null) {
                        continue;
                    }

                    String dirPath = parentFile.getAbsolutePath();

                    FolderBean folderBean = null;
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        folderBean = new FolderBean();
                        folderBean.setDir(dirPath);
                        folderBean.setFirstImgPath(path);
                    }

                    if (parentFile.list() == null) {
                        continue;
                    }

                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg")
                                    || filename.endsWith(".jpeg")
                                    || filename.endsWith(".png")) {
                                return true;
                            }
                            return false;
                        }
                    }).length;

                    folderBean.setCount(picSize);
                    mFolderBean.add(folderBean);

                    Log.i("text","picSize -------------  "+picSize);

                    if (picSize > mMaxCount) {
                        mMaxCount = picSize;
                        mCurrentDir = parentFile;
                    }

                }
                cursor.close();
                //扫描完成，回收变量内存
                //mDirPaths = null;

                //通知Handler扫描图片完成
                mHandler.sendEmptyMessage(DATA_LOADED);
            }
        }.start();
    }

    private void initEvent() {
        mButtonLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDirPopupWindowl.setAnimationStyle(R.style.dir_popuwindow_anim);
                mDirPopupWindowl.showAsDropDown(mButtonLy, 0, 0);

                lightOf();
            }
        });
    }

    private void data2View() {

        if (mCurrentDir == null) {
            Toast.makeText(this, "未扫描到任何图片", Toast.LENGTH_SHORT).show();
            return;
        }

        mImgs = Arrays.asList(mCurrentDir.list());

        mImageAdapter = new ImageAdapter(this, mImgs, mCurrentDir.getAbsolutePath());
        mGridView.setAdapter(mImageAdapter);

        mDirCount.setText(mMaxCount + "");
        mDirName.setText(mCurrentDir.getName()+"");
    }
}
